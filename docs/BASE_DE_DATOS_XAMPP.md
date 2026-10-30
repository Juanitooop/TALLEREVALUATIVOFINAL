# Base de datos: XAMPP, tablas y demostración
Esta guía acompaña a GUIA_COMPLETA_PROYECTO.md. La base es una parte esencial de la aplicación.

## Qué se encontró en este equipo
XAMPP está instalado en C:\xampp. Su servidor responde en localhost:3306 como MariaDB 10.4.32.
Existían las bases biblioteca (tablas autor, libro, prestamo, usuario) y libreria (tb_autor, tb_libros). No se modificaron.
El código de este proyecto espera otra base: tallerEvaluativo, con nombres de tablas en plural.
Se preparó tallerEvaluativo con autores, libros, usuarios y prestamos mediante preparar_sin_borrar.sql.
No basta con cambiar el nombre a biblioteca: sus tablas son distintas y no se verificó equivalencia de sus columnas.

## Las cuatro piezas que no debes confundir
| Pieza | Responsabilidad |
|---|---|
| MySQL de XAMPP (MariaDB en esta instalación) | Guarda los datos, ejecuta SQL y aplica restricciones |
| Apache de XAMPP | Sirve phpMyAdmin y páginas PHP; no ejecuta este backend Java |
| phpMyAdmin | Interfaz web para administrar la base |
| Spring Boot | Se conecta mediante JDBC y ofrece endpoints HTTP |
Angular llama a Spring Boot; no llama a phpMyAdmin ni abre conexiones directas a MariaDB.

## Preparar la base desde phpMyAdmin
1. Abrir el panel XAMPP y comprobar MySQL iniciado.
2. Iniciar Apache para acceder a http://localhost/phpmyadmin.
3. En Importar, elegir db/preparar_sin_borrar.sql.
4. Ejecutar y seleccionar tallerEvaluativo en la lista lateral; Windows puede mostrarla en minúsculas.
5. Comprobar las cuatro tablas y los dos usuarios iniciales.
6. Iniciar el backend desde VS Code y observar en la consola el inicio de HikariPool y Tomcat en 8080.
El SQL contiene CREATE DATABASE y USE, por lo que selecciona la base del proyecto.

### Diferencia entre los scripts
- preparar_sin_borrar.sql: CREATE TABLE IF NOT EXISTS y usuarios solo si su email no existe. No borra tablas ni reemplaza usuarios existentes. No transforma tablas viejas que tengan estructura incompatible.
- taller_evaluativo.sql: script original de reinicio. Ejecuta DROP TABLE sobre las cuatro tablas y después las vuelve a crear. Vacía los datos; úsalo solo si quieres reiniciar esa base después de respaldarla.
- consultas_demostracion.sql: consultas de lectura para verificar estructura, relaciones y datos.

### Importar desde la terminal PowerShell
Desde la raíz del proyecto:
```powershell
& 'C:\xampp\mysql\bin\mysql.exe' -u root --default-character-set=utf8mb4
```
Dentro de la consola SQL:
```sql
SOURCE C:/Users/Usuario/Desktop/Taller-Evaluativo-Crud/Taller-Evaluativo-Crud/db/preparar_sin_borrar.sql;
SHOW DATABASES;
USE tallerEvaluativo;
SHOW TABLES;
```
Si configuraste contraseña de root, agrega -p y escríbela cuando la pida. También cambia spring.datasource.password en application.properties.

## Conexión exacta
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tallerEvaluativo?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```
application.properties está en src/main/resources.
No cambies la contraseña de root sin actualizar esa configuración.
Si cambiaste el puerto de XAMPP, reemplaza 3306 en la URL.
El driver está en pom.xml: com.mysql:mysql-connector-j.
JPA usa Hibernate para crear consultas; Hikari reutiliza conexiones.
ddl-auto=update no crea los usuarios iniciales y no garantiza conservar datos ante cualquier cambio futuro del modelo. Para una modificación estructural, respalda antes.

## Diccionario de datos del script
### autores
| Columna | Tipo SQL | Regla y significado |
|---|---|---|
| id | BIGINT AUTO_INCREMENT | PK |
| nombre | VARCHAR(255) | Nombre; obligatorio mediante DTO |
| apellido | VARCHAR(255) | Apellido; obligatorio mediante DTO |
| nacionalidad | VARCHAR(255) | Opcional |
| fecha_nacimiento | DATE | Opcional; si se envía, DTO exige fecha pasada |

### libros
| Columna | Tipo SQL | Regla y significado |
|---|---|---|
| id | BIGINT AUTO_INCREMENT | PK |
| titulo | VARCHAR(255) | Obligatorio en DTO |
| isbn | VARCHAR(255) | UNIQUE; identifica la edición |
| anio | INT | Año, sin validación de rango en DTO |
| autor_id | BIGINT | FK autores.id; no nulo en DTO |

### usuarios
| Columna | Tipo SQL | Regla y significado |
|---|---|---|
| id | BIGINT AUTO_INCREMENT | PK |
| nombre | VARCHAR(255) | Obligatorio en DTO |
| username | VARCHAR(255) | Obligatorio; no es único en BD |
| email | VARCHAR(255) | UNIQUE; se utiliza para login |
| password_hash | VARCHAR(60) | Hash BCrypt; nunca contraseña en claro |
| rol | ENUM('ADMIN','LECTOR') | Permisos del usuario |
| estado | ENUM('ACTIVO','INACTIVO') | Controla si puede iniciar sesión |

### prestamos
| Columna | Tipo SQL | Regla y significado |
|---|---|---|
| id | BIGINT AUTO_INCREMENT | PK |
| libro_id | BIGINT | FK libros.id |
| usuario_id | BIGINT | FK usuarios.id |
| fecha_prestamo | DATE | Fecha inicial obligatoria en DTO |
| fecha_devolucion | DATE | Opcional; se actualiza al devolver |
| devuelto | BIT(1) | 0 pendiente, 1 devuelto |

El SQL permite NULL en muchos campos que el DTO exige. Son dos capas distintas: una inserción directa por phpMyAdmin no pasa por las validaciones Java.
La clave primaria es única y no nula. AUTO_INCREMENT asigna IDs; puede haber huecos tras eliminaciones.
UNIQUE evita repetir email o ISBN no nulos. No exige que todo campo tenga un valor.
Las tablas usan InnoDB, utf8mb4 y la colación utf8mb4_general_ci.

## Relaciones e integridad referencial
```text
autores.id  ← libros.autor_id
libros.id   ← prestamos.libro_id
usuarios.id ← prestamos.usuario_id
```
Autor → libros: 1:N.
Libro → préstamos: 1:N.
Usuario → préstamos: 1:N.
Préstamo relaciona usuario y libro a lo largo del tiempo.

¿Por qué guardar IDs y no repetir autor/usuario completos? Evita duplicar datos de la persona en cada fila. Las consultas JOIN recuperan los datos relacionados.
Las FK evitan referencias inexistentes. Un libro con autor_id inexistente no debe insertarse.
No hay ON DELETE CASCADE. Para eliminar datos de demostración: préstamo primero, después libro y usuario de prueba, y al final autor.

## Consultas para mostrar en clase
Puedes copiar todo db/consultas_demostracion.sql en la pestaña SQL de phpMyAdmin.

### Ver libros con autores
```sql
SELECT l.id, l.titulo, l.isbn, l.anio,
       a.nombre, a.apellido
FROM libros l
JOIN autores a ON a.id = l.autor_id
ORDER BY l.id;
```

### Ver préstamos con usuario y libro
```sql
SELECT p.id, u.username, l.titulo, p.fecha_prestamo,
       p.fecha_devolucion, p.devuelto + 0 AS devuelto
FROM prestamos p
JOIN usuarios u ON u.id = p.usuario_id
JOIN libros l ON l.id = p.libro_id
ORDER BY p.id;
```
devuelto + 0 muestra el BIT como número en vez de representación binaria.

### Mostrar que la contraseña es un hash, sin mostrarlo completo
```sql
SELECT id, username, rol, estado,
       CHAR_LENGTH(password_hash) AS longitud_hash,
       LEFT(password_hash, 4) AS prefijo_hash
FROM usuarios;
```
No uses INSERT con password_hash='123456': el login espera BCrypt.
Crea usuarios con el formulario de administrador para que UsuarioService aplique encode.

### Efecto del CRUD
- Crear autor/libro desde Angular: aparece una nueva fila en phpMyAdmin.
- Editar un libro: conserva id y cambia sus columnas.
- Crear préstamo: se guarda usuario_id y libro_id, más fechas y devuelto=0.
- Devolver: mismo ID de préstamo, devuelto=1 y fecha_devolucion actualizada.
- Eliminar: desaparece la fila si no tiene dependencias que lo impidan.
Actualizar la vista de phpMyAdmin es necesario para ver las modificaciones.

## Respaldo y restauración
En phpMyAdmin selecciona tallerEvaluativo → Exportar → formato SQL → Guardar.
Para restaurar, inspecciona antes si el archivo contiene DROP/CREATE/USE y si reemplazará tus datos.
No copies solo la carpeta de archivos internos de MySQL mientras el servidor escribe.
El SQL inicial del repositorio no es un respaldo de las filas que hayas creado después.
Los datos físicos de XAMPP están bajo C:\xampp\mysql\data; se administran mediante el servidor, no editando esos archivos.

## Fallos habituales
| Síntoma | Qué comprobar |
|---|---|
| Connection refused / Communications link failure | MySQL iniciado, puerto y permisos de conexión |
| Access denied for user root | Usuario/contraseña de application.properties |
| Unknown database | Nombre exacto e importación del script |
| Table doesn't exist | Script importado en la base correcta; nombres en plural |
| Duplicate entry | Email o ISBN repetido |
| Cannot add/update child row | autor_id, libro_id o usuario_id inexistente |
| Cannot delete parent row | Hay registros que dependen de esa fila |
| phpMyAdmin no abre | Apache/puerto HTTP; eso no demuestra por sí solo que MySQL esté apagado |
| Login falla pese a existir usuario | Hash BCrypt correcto, contraseña y estado ACTIVO |
| Java falla antes de conectarse | JDK adecuado y dependencias; no es necesariamente fallo de BD |

La revisión observó una advertencia: Hibernate usa MySQLDialect y MariaDB anuncia compatibilidad 5.5.5. La conexión se estableció. No se cambió el motor ni se actualizaron versiones para este trabajo. No presentes esta combinación como una validación de todas las funciones SQL posibles.

## Preguntas rápidas sobre la base
**¿phpMyAdmin es la base?** No, es una interfaz para administrarla.
**¿Dónde se configura el nombre?** En spring.datasource.url y en los scripts SQL.
**¿Quién genera los IDs?** El servidor con AUTO_INCREMENT; Java usa GenerationType.IDENTITY.
**¿Por qué fechaNacimiento termina como fecha_nacimiento?** Por el mapeo de nombres de Hibernate; algunas otras columnas tienen @Column explícito.
**¿Dónde se ve la FK en Java?** @ManyToOne y @JoinColumn, además del campo ID que se escribe.
**¿Qué valida MySQL y qué valida Java?** BD: claves, referencias y tipos; DTO: campos requeridos y algunas reglas de valores.
**¿Qué pasa si apago XAMPP?** El frontend puede seguir dibujándose, pero las operaciones que requieren BD fallarán.
**¿Qué demuestra que funciona de verdad?** Crear/editar desde la interfaz y comprobar las mismas filas mediante SQL.
