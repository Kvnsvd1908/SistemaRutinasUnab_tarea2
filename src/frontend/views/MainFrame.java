package frontend.views;

import backend.service.*;
import backend.model.*;
import backend.exception.GimnasioException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

// Heredamos de JFrame para crear una ventana real de Windows
public class MainFrame extends JFrame implements GimnasioListener {
    private GimnasioBackend backend;
    private CardLayout navegadorPaneles; // Permite cambiar de pantalla como pestañas de navegador
    private JPanel contenedorPrincipal;

    // Componentes de las pantallas
    private JTextArea txtVisorArchivo;
    private JTextField txtCuposCardio, txtCuposFuerza, txtSemana, txtIntensidad;
    private JTextArea txtVisorRutina;

    public MainFrame() {
        // 1. Configurar los parámetros de la ventana principal
        setTitle("Sistema de Gestión de Rutinas - UNAB Centro Deportivo");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        // 2. Inicializar el motor lógico del backend y suscribirse a sus alertas
        backend = new GimnasioBackend();
        backend.setListener(this);

        // 3. Configurar el sistema de navegación por pestañas invisibles (CardLayout)
        navegadorPaneles = new CardLayout();
        contenedorPrincipal = new JPanel(navegadorPaneles);

        // 4. Agregar las vistas requeridas por el enunciado
        contenedorPrincipal.add(crearPanelInicio(), "INICIO");
        contenedorPrincipal.add(crearPanelCarga(), "CARGA");
        contenedorPrincipal.add(crearPanelGeneracion(), "GENERACION");
        contenedorPrincipal.add(crearPanelRevision(), "REVISION");

        add(contenedorPrincipal);
        navegadorPaneles.show(contenedorPrincipal, "INICIO"); // Mostrar la pantalla de bienvenida al arrancar
    }

    //  PANTALLA 1 BIENVENIDA 
    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("¡Bienvenido al Optimizador de Rutinas!", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(lblTitulo, BorderLayout.NORTH);

        JTextArea txtIntro = new JTextArea("Este software automatiza la creación de rutinas personalizadas respetando la carga de datos estructurados de entrenamientos deportivos y las restricciones de semanas consecutivas para evitar fatigas.\n\nDiseñado para el cumplimiento de evaluaciones académicas.");
        txtIntro.setWrapStyleWord(true);
        txtIntro.setLineWrap(true);
        txtIntro.setEditable(false);
        txtIntro.setBackground(panel.getBackground());
        panel.add(txtIntro, BorderLayout.CENTER);

        JButton btnComenzar = new JButton("Comenzar Configuración ➔");
        btnComenzar.setFont(new Font("Arial", Font.BOLD, 14));
        btnComenzar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navegadorPaneles.show(contenedorPrincipal, "CARGA"); // Pasa a la siguiente pantalla
            }
        });
        panel.add(btnComenzar, BorderLayout.SOUTH);

        return panel;
    }

    // PANTALLA 2: CARGA DEL ARCHIVO PLANO 
    private JPanel crearPanelCarga() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(new JLabel("Archivo objetivo: ejercicios.txt"));

        JButton btnCargar = new JButton("Leer y Validar Archivo Plano");
        btnCargar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Llama al motor asincrónico pasándole el archivo de la raíz
                backend.cargarDesdeArchivo("ejercicios.txt");
            }
        });
        panelSuperior.add(btnCargar);
        panel.add(panelSuperior, BorderLayout.NORTH);

        txtVisorArchivo = new JTextArea("Haz clic en el botón de arriba para cargar el inventario de ejercicios...");
        txtVisorArchivo.setEditable(false);
        panel.add(new JScrollPane(txtVisorArchivo), BorderLayout.CENTER);

        JButton btnSiguiente = new JButton("Ir a Generar Rutina ➔");
        btnSiguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navegadorPaneles.show(contenedorPrincipal, "GENERACION");
            }
        });
        panel.add(btnSiguiente, BorderLayout.SOUTH);

        return panel;
    }

    // PANTALLA 3: FORMULARIO DE GENERACIÓN 
    private JPanel crearPanelGeneracion() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblSub = new JLabel("Pauta de Selección y Restricciones Temporales");
        lblSub.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblSub, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridLayout(4, 2, 10, 10));
        formulario.add(new JLabel("Cupos Ejercicios Cardio (ej: 2):"));
        txtCuposCardio = new JTextField("2");
        formulario.add(txtCuposCardio);

        formulario.add(new JLabel("Cupos Ejercicios Fuerza (ej: 2):"));
        txtCuposFuerza = new JTextField("2");
        formulario.add(txtCuposFuerza);

        formulario.add(new JLabel("Nivel Intensidad (BASICO, INTERMEDIO, AVANZADO, ALTO_RENDIMIENTO):"));
        txtIntensidad = new JTextField("INTERMEDIO");
        formulario.add(txtIntensidad);

        formulario.add(new JLabel("Semana de Planificación Actual (ej: 3):"));
        txtSemana = new JTextField("3");
        formulario.add(txtSemana);

        panel.add(formulario, BorderLayout.CENTER);

        JButton btnProcesar = new JButton("Aplicar Algoritmo de Coincidencias y Generar");
        btnProcesar.setFont(new Font("Arial", Font.BOLD, 13));
        btnProcesar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int cardio = Integer.parseInt(txtCuposCardio.getText().trim());
                    int fuerza = Integer.parseInt(txtCuposFuerza.getText().trim());
                    int sem = Integer.parseInt(txtSemana.getText().trim());
                    NivelIntensidad intens = NivelIntensidad.valueOf(txtIntensidad.getText().trim().toUpperCase());

                    backend.generarRutina(cardio, fuerza, intens, sem);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Error en los datos: Verifica que la intensidad esté bien escrita en Mayúsculas.", "Atención", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        panel.add(btnProcesar, BorderLayout.SOUTH);

        return panel;
    }

    //  PANTALLA 4: REVISIÓN DE LA RUTINA GENERADA Y RESUMEN
    private JPanel crearPanelRevision() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Rutina Personalizada Propuesta", JLabel.LEFT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblTitulo, BorderLayout.NORTH);

        txtVisorRutina = new JTextArea();
        txtVisorRutina.setEditable(false);
        panel.add(new JScrollPane(txtVisorRutina), BorderLayout.CENTER);

        JButton btnVolver = new JButton("⌨ Ajustar Parámetros / Volver");
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navegadorPaneles.show(contenedorPrincipal, "GENERACION");
            }
        });
        panel.add(btnVolver, BorderLayout.SOUTH);

        return panel;
    }

    //   IMPLEMENTACIÓN DEL LISTENER 

    @Override
    public void onCargaExitosa() {
        // Volvemos al hilo de la interfaz gráfica usando SwingUtilities para actualizar textos de forma segura
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Vector<Ejercicio> inv = backend.getInventario();
                StringBuilder sb = new StringBuilder();
                sb.append("✓ ¡Archivo leído correctamente!\n");
                sb.append("Total de ejercicios disponibles en catálogo: ").append(inv.size()).append("\n\n");
                sb.append("Listado resumido detectado:\n");

                for (int i = 0; i < inv.size(); i++) {
                    Ejercicio ej = inv.get(i);
                    sb.append(" - [").append(ej.getCodigoIdentificador()).append("] ")
                            .append(ej.getNombre()).append(" (").append(ej.getIntensidad()).append(")\n");
                }
                txtVisorArchivo.setText(sb.toString());
                JOptionPane.showMessageDialog(MainFrame.this, "Catálogo deportivo cargado con éxito en memoria.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    @Override
    public void onRutinaGenerada() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Rutina rut = backend.getRutinaActual();
                StringBuilder sb = new StringBuilder();
                sb.append("=========================================\n");
                sb.append("        RESUMEN ESTADÍSTICO DE LA RUTINA\n");
                sb.append("=========================================\n");
                sb.append("• Tiempo Total Estimado: ").append(rut.getTiempoTotal()).append(" minutos.\n");
                sb.append("• Cantidad Total de Ejercicios: ").append(rut.getCantidadTotal()).append(" bloques.\n");
                sb.append("  -> Ejercicios de Cardio incluidos: ").append(rut.getCantidadPorTipo(TipoEjercicio.CARDIOVASCULAR)).append("\n");
                sb.append("  -> Ejercicios de Fuerza incluidos: ").append(rut.getCantidadPorTipo(TipoEjercicio.FUERZA)).append("\n\n");
                sb.append("=========================================\n");
                sb.append("             DESGLOSE DETALLADO\n");
                sb.append("=========================================\n");

                Vector<Ejercicio> lista = rut.getEjercicios();
                for (int i = 0; i < lista.size(); i++) {
                    Ejercicio e = lista.get(i);
                    sb.append(i + 1).append(". ").append(e.getNombre().toUpperCase()).append("\n")
                            .append("   Cod: ").append(e.getCodigoIdentificador())
                            .append(" | Duración: ").append(e.getTiempoEstimadoMinutos()).append(" min.\n")
                            .append("   Ejecución: ").append(e.getDescripcionEjecucion()).append("\n")
                            .append("-----------------------------------------\n");
                }

                txtVisorRutina.setText(sb.toString());
                navegadorPaneles.show(contenedorPrincipal, "REVISION"); // Cambia de pantalla automáticamente para ver el resultado
            }
        });
    }

    @Override
    public void onErrorOcurrido(final String mensaje) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Despliega una alerta emergente elegante controlando las Excepciones
                JOptionPane.showMessageDialog(MainFrame.this, mensaje, "Error del Sistema", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
