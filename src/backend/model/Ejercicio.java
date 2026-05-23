package backend.model;

public class Ejercicio {
    private String codigoIdentificador;
    private String nombre;
    private TipoEjercicio tipo;
    private NivelIntensidad intensidad;
    private int tiempoEstimadoMinutos;
    private String descripcionEjecucion;
    private int semanaUltimoUso;

    public Ejercicio(String codigoIdentificador, String nombre, TipoEjercicio tipo,
                     NivelIntensidad intensidad, int tiempoEstimadoMinutos,
                     String descripcionEjecucion, int semanaUltimoUso) {
        this.codigoIdentificador = codigoIdentificador;
        this.nombre = nombre;
        this.tipo = tipo;
        this.intensidad = intensidad;
        this.tiempoEstimadoMinutos = tiempoEstimadoMinutos;
        this.descripcionEjecucion = descripcionEjecucion;
        this.semanaUltimoUso = semanaUltimoUso;
    }

    public String getCodigoIdentificador() { return codigoIdentificador; }
    public String getNombre() { return nombre; }
    public TipoEjercicio getTipo() { return tipo; }
    public NivelIntensidad getIntensidad() { return intensidad; }
    public int getTiempoEstimadoMinutos() { return tiempoEstimadoMinutos; }
    public String getDescripcionEjecucion() { return descripcionEjecucion; }
    public int getSemanaUltimoUso() { return semanaUltimoUso; }
}