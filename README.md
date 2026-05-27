# Sistema de Gestión y Optimización de Rutinas - UNAB

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-000000?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Swing](https://img.shields.io/badge/UI-Java_Swing-blue?style=for-the-badge)

Software de escritorio desarrollado en **Java** diseñado para automatizar la planificación de entrenamientos deportivos. El sistema optimiza la selección de ejercicios desde un catálogo en texto plano, aplicando restricciones lógicas temporales para prevenir la sobrecarga y fatiga muscular.

---

##  Características Clave

* **Carga de Datos Asincrónica:** Lector de archivos planos (`.txt`) mediante hilos secundarios (`Thread`), evitando que la interfaz gráfica se congele durante procesos de lectura pesados.
* **Algoritmo de Rotación Temporal:** Filtra y descarta automáticamente ejercicios que hayan sido ejecutados en la semana actual ($W$) o en la semana inmediatamente anterior ($W-1$).
* **Validación Robusta y Excepciones:** Arquitectura con control de errores a través de excepciones personalizadas (`GimnasioException`), manejando formatos corruptos o archivos inexistentes.
* **Patrón de Diseño Observer:** Comunicación desacoplada entre el Backend y el Frontend mediante eventos y suscripciones (`GimnasioListener`).

---

##  Estructura del Proyecto (Arquitectura MVC/Service)

El proyecto sigue una organización modular estricta para asegurar un código mantenible y escalable:

```text
src/
├── backend/
│   ├── exception/      # Manejo de alertas y excepciones personalizadas
│   ├── model/          # Entidades del dominio (Ejercicio, Rutina, Enums)
│   └── service/        # Motor lógico (Algoritmo de filtrado e I/O de archivos)
├── frontend/
│   └── views/          # Interfaz gráfica de usuario basada en JFrame y CardLayout
└── Main.java           # Punto de entrada principal de la aplicación
