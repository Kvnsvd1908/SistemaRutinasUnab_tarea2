package backend.service;

import backend.model.*;
import backend.exception.GimnasioException;
import java.io.*;
import java.util.Vector;

public class GimnasioBackend {
    private Vector<Ejercicio> inventario;
    private Rutina rutinaActual;
    private GimnasioListener listener; // El puente de comunicación con las vistas

    public GimnasioBackend() {
        this.inventario = new Vector<Ejercicio>();
    }

    // Permite que el frontend se suscriba para escuchar las respuestas
    public void setListener(GimnasioListener listener) {
        this.listener = listener;
    }

    public Vector<Ejercicio> getInventario() {
        return this.inventario;
    }

    public Rutina getRutinaActual() {
        return this.rutinaActual;
    }

    // 1. LECTOR DEL ARCHIVO PLANO CON EXCEPCIONES CONTROLADAS
    public void cargarDesdeArchivo(final String ruta) {
        // Usamos un Thread para cumplir con la notificación asincrónica de la rúbrica
        Thread hiloCarga = new Thread() {
            @Override
            public void run() {
                try {
                    File archivo = new File(ruta);
                    if (!archivo.exists()) {
                        throw new GimnasioException("El archivo '" + ruta + "' no existe en la carpeta raíz.");
                    }

                    Vector<Ejercicio> listaTemporal = new Vector<Ejercicio>();
                    BufferedReader br = new BufferedReader(new FileReader(archivo));
                    String linea;

                    while ((linea = br.readLine()) != null) {
                        linea = linea.trim();
                        // Ignorar líneas vacías o comentarios que parten con '#'
                        if (linea.isEmpty() || linea.startsWith("#")) {
                            continue;
                        }

                        String[] datos = linea.split(";");
                        // Validar información incompleta (Exigido en los requerimientos)
                        if (datos.length < 7) {
                            br.close();
                            throw new GimnasioException("Formato roto o información incompleta en el archivo.");
                        }

                        // Extraer textos crudos de las columnas
                        String codigo = datos[0].trim();
                        String nombre = datos[1].trim();
                        String tipoStr = datos[2].trim().toUpperCase();
                        String intensidadStr = datos[3].trim().toUpperCase();

                        // Validar formatos numéricos incorrectos
                        int tiempo;
                        int semana;
                        try {
                            tiempo = Integer.parseInt(datos[4].trim());
                            semana = Integer.parseInt(datos[6].trim());
                        } catch (NumberFormatException nfe) {
                            br.close();
                            throw new GimnasioException("Los campos de tiempo o semanas deben ser números válidos.");
                        }

                        String desc = datos[5].trim();

                        // Convertir textos a Enums de forma directa
                        TipoEjercicio tipo = TipoEjercicio.valueOf(tipoStr);
                        NivelIntensidad intensidad = NivelIntensidad.valueOf(intensidadStr);

                        // Crear objeto ejercicio con el molde tradicional
                        Ejercicio ej = new Ejercicio(codigo, nombre, tipo, intensidad, tiempo, desc, semana);
                        listaTemporal.add(ej);
                    }
                    br.close();

                    // Guardar la lista definitiva en memoria si no hubo fallas
                    inventario = listaTemporal;

                    // Notificar con éxito al frontend (Suscripción-Notificación)
                    if (listener != null) {
                        listener.onCargaExitosa();
                    }

                } catch (Exception ex) {
                    // Si ocurre cualquier problema, se gatilla la alerta controlada
                    if (listener != null) {
                        listener.onErrorOcurrido(ex.getMessage());
                    }
                }
            }
        };
        hiloCarga.start(); // Echa a andar el subproceso lógico
    }

    // 2. CONSTRUCTOR DE RUTINAS CON FILTROS TRADICIONALES
    public void generarRutina(final int cuposCardio, final int cuposFuerza,
                              final NivelIntensidad intensidadFiltro, final int semanaActual) {
        Thread hiloGenerador = new Thread() {
            @Override
            public void run() {
                try {
                    if (inventario.isEmpty()) {
                        throw new GimnasioException("No hay stock de ejercicios en memoria. Carga el archivo primero.");
                    }

                    Rutina nuevaRutina = new Rutina();
                    int cuentaCardio = 0;
                    int cuentaFuerza = 0;

                    // Recorremos el inventario completo buscando coincidencias
                    for (int i = 0; i < inventario.size(); i++) {
                        Ejercicio e = inventario.get(i);

                        // Filtro A: Validar si coincide la intensidad requerida
                        if (e.getIntensidad() != intensidadFiltro) {
                            continue;
                        }

                        // Filtro B: Restricción de semanas consecutivas (no usar si se usó en semana W o W-1)
                        if (e.getSemanaUltimoUso() == java.lang.Math.max(1, semanaActual) || e.getSemanaUltimoUso() == (semanaActual - 1)) {
                            continue;
                        }

                        // Filtro C: Clasificar y rellenar según los cupos solicitados
                        if (e.getTipo() == TipoEjercicio.CARDIOVASCULAR && cuentaCardio < cuposCardio) {
                            nuevaRutina.agregarEjercicio(e);
                            cuentaCardio++;
                        } else if (e.getTipo() == TipoEjercicio.FUERZA && cuentaFuerza < cuposFuerza) {
                            nuevaRutina.agregarEjercicio(e);
                            cuentaFuerza++;
                        }
                    }

                    // Verificar si se completó la cuota exacta solicitada por el usuario
                    if (cuentaCardio < cuposCardio || cuentaFuerza < cuposFuerza) {
                        throw new GimnasioException("No fue posible armar la rutina. Faltan ejercicios que cumplan la intensidad y restricción de semanas consecutivas.");
                    }

                    rutinaActual = nuevaRutina;

                    // Notificar al frontend que pase a la pantalla de revisión
                    if (listener != null) {
                        listener.onRutinaGenerada();
                    }

                } catch (Exception ex) {
                    if (listener != null) {
                        listener.onErrorOcurrido(ex.getMessage());
                    }
                }
            }
        };
        hiloGenerador.start();
    }
}