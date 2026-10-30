# Revisión y pruebas — 29 de octubre de 2026

## Cambios realizados
- Interfaz con Arial, fondo gris claro, paneles blancos, botones básicos y tablas con bordes.
- Inicio reducido a lista de secciones con conteos.
- Se retiraron fuentes externas y el sistema elaborado de variables CSS.
- Autores, libros y usuarios ahora usan campos normales; ya no requieren escribir JSON.
- Editar carga los datos de una fila; guardar envía los campos al mismo API.
- Eliminar actúa sobre el ID de la fila y pide confirmación.
- Formularios administrativos visibles solo para ADMIN; el backend conserva la autoridad sobre los permisos.
- Mensajes básicos de error/éxito en autores, libros y usuarios, y mensajes para listas vacías.
- Búsquedas de libros/usuarios conservan su filtro al paginar y codifican el texto de la URL.
- Préstamos muestra devolución solo para el propietario lector o un administrador.
- PrestamoController valida la propiedad antes de aceptar devolución de un LECTOR. Mantiene los IDs y la fecha original de ese préstamo; asigna devolución y fecha actual.
- Script SQL adicional sin DROP TABLE y consultas de demostración.
- Documentación completa en español y archivo Biblioteca.code-workspace en la carpeta exterior para abrir la carpeta correcta en VS Code.

No se agregaron frameworks, dependencias ni patrones de arquitectura nuevos.
Había un cambio previo en AuthService.java al comenzar; se conservó y no se reescribió.

## Base de datos
Se confirmó MariaDB 10.4.32 de XAMPP en 3306.
biblioteca y libreria ya existían y no se modificaron.
Se creó tallerEvaluativo con el script preparar_sin_borrar.sql.
Se verificaron las cuatro tablas y los usuarios admin/lector.
HikariPool confirmó conexión real desde Spring Boot.

## Pruebas completadas
| Prueba | Resultado |
|---|---|
| npm.cmd run build | Correcto; Angular compila las plantillas y TypeScript |
| Maven test/package con JDK 26 y release 21 | BUILD SUCCESS |
| contextLoads existente | 1 prueba, 0 fallos, 0 errores |
| Login ADMIN y LECTOR por HTTP | Correcto, devuelve JWT |
| Acceso sin token o con token inválido | 401 |
| LECTOR consulta /usuarios | 403 |
| Crear/leer/editar autor | Correcto |
| Crear/buscar/editar libro | Correcto |
| Crear/editar usuario y login posterior | Correcto, verifica el hash BCrypt |
| Crear préstamo como lector enviando otro usuarioId | Se guarda el propietario del JWT |
| LECTOR intenta devolver un préstamo ajeno | 403 |
| LECTOR devuelve préstamo propio | Correcto y conserva propietario |
| LECTOR intenta eliminar préstamo | 403 |
| Autor con nombre/apellido vacíos | 400 |
| Paginación size=1 | Correcto |
| Eliminación de registros temporales | Correcta; consulta posterior sin el libro temporal |

Las pruebas HTTP están en scripts/probar_api.ps1. Crean datos con un identificador aleatorio y eliminan únicamente los de esa ejecución, en orden de dependencias. No se dejaron registros temporales del CRUD.
El script requiere PowerShell 7, API iniciada y usuarios de prueba disponibles:
```powershell
./scripts/probar_api.ps1
```
No reemplaza una suite completa ni comprueba todas las combinaciones de errores.

## Revisión visual
Se abrió la aplicación real en el navegador y se comprobó el login ADMIN, inicio con conteos de XAMPP, formularios de Autores/Libros y cierre de sesión.
También se verificaron el login LECTOR, su menú sin Usuarios y su formulario de préstamos.
La captura interfaz-simple.png muestra la pantalla de Libros.
La ejecución completa de CRUD se verificó por HTTP; no se afirma que cada botón haya sido recorrido en el navegador.

## Aspectos pendientes o limitados
- Hibernate advierte que el dialecto MySQL detecta la compatibilidad 5.5.5 de MariaDB. Conexión y operaciones probadas funcionaron; no se validaron todas las funciones posibles de esa combinación.
- Los préstamos siguen siendo visibles para todos los usuarios autenticados.
- Sin stock/ejemplares, comprobación de disponibilidad, renovación de JWT o revocación de tokens.
- Sin validación integral de fechas y formatos, ni manejo global de errores.
- El selector de préstamos carga un máximo de 100 libros.
- El login no distingue errores de conexión de credenciales incorrectas en su mensaje.
- La guía completa explica estas limitaciones y las preguntas relacionadas para sustentación.

## Archivos para empezar
- README.md: instrucciones rápidas y enlaces.
- docs/GUIA_COMPLETA_PROYECTO.md: arquitectura, archivos, endpoints, JWT, ejemplos y preguntas.
- docs/BASE_DE_DATOS_XAMPP.md: instalación local, tablas, relaciones, consultas y respaldo.
- db/preparar_sin_borrar.sql: preparación sin borrar filas existentes.
- db/consultas_demostracion.sql: lectura y verificación desde phpMyAdmin.
