# Consulta individual de préstamos: ADMIN y propietario LECTOR

## Cambio
Archivo: src/main/java/com/example/taller/controller/PrestamoController.java.
Método: getOne. Ruta: GET /prestamos/{prestamoId}.

1. Busca el préstamo con getPrestamo(id).
2. Si no existe, lanza ResponseStatusException con NOT_FOUND (404).
3. Obtiene el email de la identidad JWT validada por Spring Security.
4. Busca al usuario en UsuarioRepository.
5. ADMIN puede consultar cualquier préstamo existente.
6. LECTOR solo puede consultar si su id coincide con prestamo.usuarioId.
7. Si el préstamo existe y no tiene permiso, devuelve FORBIDDEN (403).
8. Si está autorizado, devuelve PrestamoDto con HTTP 200.

No se utiliza un usuarioId enviado por el cliente para decidir quién consulta.
No se modificaron el listado general ni las operaciones de escritura.
No se creó, modificó ni eliminó ningún registro para implementar o probar esta regla.

## Explicación para sustentar
“Primero consulto si el préstamo existe: si no existe devuelvo 404. Después identifico al usuario mediante el JWT. Si es administrador permito la consulta; si es lector comparo su ID con el propietario del préstamo. Si no coincide devuelvo 403. Si tiene permiso, devuelvo el préstamo.”

## Postman
Importa postman/Consulta-prestamos.postman_collection.json usando Import.

Configura estas variables en la colección (contraseñas solo en valores locales, no subirlas a Git):
- baseUrl: http://localhost:8080
- email_admin / password_admin: administrador existente.
- email_lectorA / password_lectorA: primer lector existente.
- email_lectorB / password_lectorB: segundo lector existente, distinto del primero.
- prestamoA: ID existente de un préstamo del lector A.
- prestamoB: ID existente de un préstamo del lector B.
- inexistente: ID confirmado como inexistente.

La colección guarda automáticamente los tokens obtenidos en los tres logins.
Las siguientes consultas usan Authorization: Bearer con el token correspondiente.
Los tests de Postman comprueban los códigos HTTP y, en los casos 200, el ID y propietario de la respuesta.
Ejecuta primero los tres logins y después las consultas en el orden de la colección.
No exportes tokens o contraseñas después de la demostración.

| Identidad | Consulta | Resultado |
|---|---|---|
| ADMIN | prestamoA y prestamoB | 200 en ambos |
| LECTOR A | prestamoA | 200 |
| LECTOR B | prestamoB | 200 |
| LECTOR A | prestamoB | 403 |
| LECTOR B | prestamoA | 403 |
| ADMIN, LECTOR A, LECTOR B | inexistente | 404 |

Esto demuestra éxito (200), préstamo ajeno (403) e inexistente (404), usando dos lectores y un administrador.
En Postman, muestra el método/URL, estado HTTP y Test Results. No muestres el token.
Para el GET usa Body: none.
Reinicia el backend tras compilar los cambios; la versión anterior no tiene esta comprobación.
Si solo vas a hacer la demostración, puedes arrancar con:
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.jpa.hibernate.ddl-auto=none
Así Hibernate no intentará actualizar el esquema durante ese arranque.

## Situación real encontrada
La base tallerEvaluativo configurada en application.properties tenía:
- admin@biblioteca.com, ADMIN, id 1.
- lector@biblioteca.com, LECTOR, id 2.
- Ningún préstamo.

Por tanto NO se ejecutó la demostración Postman contra datos reales ni se inventaron resultados.
Faltan un segundo lector y préstamos existentes para realizarla.
Si los datos están en otra instalación/base, hay que usar esa conexión y sus credenciales.
No ejecutar scripts de preparación ni scripts/probar_api.ps1 para este enunciado: ese script previo crea y elimina datos y no corresponde a esta prueba de solo lectura.

## Pruebas aisladas
src/test/java/com/example/taller/controller/PrestamoControllerConsultaTest.java usa MockMvc y Mockito.
Simula en memoria un administrador, dos lectores y dos préstamos. No inicia Spring Boot ni conecta a MySQL.
Comprueba 200 para ADMIN y propietarios, 403 para ambos accesos cruzados y 404 para las tres identidades.
El token/identidad de estas pruebas está simulado: verifican el controlador y sus respuestas HTTP, no reemplazan la demostración real de login/JWT con Postman.
La identidad simulada se inyecta como principal solo en las peticiones del test; no modifica la seguridad de la aplicación.
Ejecutar únicamente:
mvn -Dtest=PrestamoControllerConsultaTest test

## Corrección de configuración
Los comentarios escritos después de spring.datasource.url, username y password se movieron a líneas separadas.
En archivos .properties, un # después del valor no funciona como comentario de fin de línea: pasaba a formar parte de la URL o credencial.
No se cambió la base elegida ni se alteraron sus datos.


Resultado verificado: 6 pruebas aisladas ejecutadas, 0 fallos y 0 errores (BUILD SUCCESS). La demostración Postman con datos reales continúa pendiente por falta de registros.
