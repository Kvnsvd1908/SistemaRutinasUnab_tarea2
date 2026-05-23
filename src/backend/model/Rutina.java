package backend.model;

import java.util.Vector;

public class Rutina {
    // Lista de ejercicios que componen la rutina
    private Vector<Ejercicio> ejercicios;

    // Constructor para inicializar la lista vacía
    public Rutina() {
        this.ejercicios = new Vector<Ejercicio>();
    }

    // Agregar un ejercicio a la rutina
    public void agregarEjercicio(Ejercicio e) {
        this.ejercicios.add(e);
    }

    // Obtener todos los ejercicios
    public Vector<Ejercicio> getEjercicios() {
        return this.ejercicios;
    }

    // Contar el total de ejercicios
    public int getCantidadTotal() {
        return this.ejercicios.size();
    }

    // Contar cuántos ejercicios hay de un tipo específico (CARDIOVASCULAR o FUERZA)
    public int getCantidadPorTipo(TipoEjercicio tipo) {
        int contador = 0;
        for (int i = 0; i < ejercicios.size(); i++) {
            if (ejercicios.get(i).getTipo() == tipo) {
                contador++;
            }
        }
        return contador;
    }

    // Contar cuántos ejercicios hay de una intensidad específica
    public int getCantidadPorIntensidad(NivelIntensidad intensidad) {
        int contador = 0;
        for (int i = 0; i < ejercicios.size(); i++) {
            if (ejercicios.get(i).getIntensidad() == intensidad) {
                contador++;
            }
        }
        return contador;
    }

    // Calcular el tiempo total sumando los minutos de cada ejercicio
    public int getTiempoTotal() {
        int suma = 0;
        for (int i = 0; i < ejercicios.size(); i++) {
            // Llama exactamente a getTiempoEstimadoMinutos en español
            suma += ejercicios.get(i).getTiempoEstimadoMinutos();
        }
        return suma;
    }
}