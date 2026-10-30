# Biblioteca — Taller evaluativo

Abre en VS Code esta carpeta, la que contiene pom.xml, src, frontend y db.

También puedes abrir Biblioteca.code-workspace, ubicado en la carpeta exterior.

- [Guía completa: arquitectura, archivos, API, JWT, flujo y preguntas de sustentación](docs/GUIA_COMPLETA_PROYECTO.md)
- [Base de datos: XAMPP, tablas, relaciones, SQL y solución de problemas](docs/BASE_DE_DATOS_XAMPP.md)
- [Resultados de revisión y pruebas](docs/REVISION_Y_PRUEBAS.md)
- [Enunciado transcrito](docs/requerimientos.md)

## Inicio
1. XAMPP: inicia MySQL (y Apache si usarás phpMyAdmin).
2. Importa db/preparar_sin_borrar.sql. El SQL original taller_evaluativo.sql BORRA tablas.
3. Terminal backend: `mvn spring-boot:run`, con JDK 21 o compatible.
4. Otra terminal: `cd frontend`, `npm.cmd install`, `npm.cmd start`.
5. Abre http://localhost:4200.

En este equipo Java del PATH era 17 y Maven no estaba en PATH. La guía completa incluye las rutas de las instalaciones encontradas y los comandos exactos.

Usuarios de demostración: admin@biblioteca.com / admin123 y lector@biblioteca.com / lector123.
No necesitas colocar el proyecto en htdocs. Angular usa 4200, Spring Boot 8080 y MySQL/MariaDB 3306.
