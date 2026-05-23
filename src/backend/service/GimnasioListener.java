package backend.service;

public interface GimnasioListener {
    void onCargaExitosa();
    void onRutinaGenerada();
    void onErrorOcurrido(String mensaje);
}