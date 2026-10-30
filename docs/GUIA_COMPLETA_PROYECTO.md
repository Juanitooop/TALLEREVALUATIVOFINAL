# Guía completa del proyecto Biblioteca
Fecha de revisión: 29 de octubre de 2026. Guía basada en el código de este repositorio.

## 1. Qué hace el proyecto
Es una aplicación de biblioteca para administrar autores, libros, usuarios y préstamos. Tiene dos roles: ADMIN y LECTOR. El administrador gestiona los registros; el lector consulta el catálogo, registra préstamos y devuelve los suyos.

Hay tres partes:
- **Frontend:** Angular, en `frontend/`. Se ejecuta en el navegador, normalmente en el puerto 4200.
- **Backend:** Java con Spring Boot, en `src/main/java/com/example/taller/`. Expone la API en el puerto 8080.
- **Base de datos:** las cuatro tablas viven en el servidor MySQL/MariaDB de XAMPP, en el puerto 3306. Los archivos SQL de `db/` permiten prepararlas.

No es una aplicación PHP. No debes copiar el proyecto en htdocs. Apache de XAMPP sirve para abrir phpMyAdmin; Spring Boot incorpora su propio servidor Tomcat para la API.

## 2. Mapa para encontrar rápidamente cada cosa
Todas las rutas de esta guía son relativas a la carpeta que contiene pom.xml.

| Qué buscas | Dónde está |
|---|---|
| Conexión a la base, usuario y contraseña | src/main/resources/application.properties |
| SQL original (BORRA las tablas) | db/taller_evaluativo.sql |
| Preparación sin borrar tablas | db/preparar_sin_borrar.sql |
| Guía dedicada a XAMPP | docs/BASE_DE_DATOS_XAMPP.md |
| Inicio del backend | src/main/java/com/example/taller/TallerEvaluativoApplication.java |
| Permisos, CORS, BCrypt y validación JWT | src/main/java/com/example/taller/config/SecurityConfig.java |
| Generación del JWT | src/main/java/com/example/taller/service/JwtService.java |
| Comprobación del login | src/main/java/com/example/taller/service/AuthService.java |
| Endpoint de login | src/main/java/com/example/taller/controller/AuthController.java |
| Tablas representadas en Java | src/main/java/com/example/taller/entity/ |
| Datos que recibe y devuelve la API | src/main/java/com/example/taller/dto/ |
| Acceso a la base | src/main/java/com/example/taller/repository/ |
| Operaciones y lógica | src/main/java/com/example/taller/service/ |
| Direcciones HTTP de la API | src/main/java/com/example/taller/controller/ |
| Pantallas del frontend | frontend/src/app/modules/ |
| Menú, contenido y pie | frontend/src/app/components/ |
| URLs que llama Angular | frontend/src/app/services/ |
| Guardado de sesión en el navegador | frontend/src/app/services/auth.ts |
| Envío automático del token | frontend/src/app/interceptors/auth.ts |
| Protección de rutas de Angular | frontend/src/app/guards/auth.ts |
| Rutas de las pantallas | frontend/src/app/app.routes.ts |
| Registro del interceptor y HttpClient | frontend/src/app/app.config.ts |
| Apariencia general | frontend/src/styles.css |
| Dependencias Java | pom.xml |
| Dependencias JavaScript y comandos npm | frontend/package.json |
| Enunciado de la profesora | docs/TALLER EVALUATIVO 2.docx y docs/requerimientos.md |

## 3. Abrirlo y ejecutarlo en VS Code
Abre **Archivo > Abrir carpeta** y selecciona:
`C:\Users\Usuario\Desktop\Taller-Evaluativo-Crud\Taller-Evaluativo-Crud`

La carpeta exterior tiene otra carpeta con el mismo nombre. La correcta contiene pom.xml, src, frontend y db. No abras solamente frontend si necesitas trabajar también en Java.

### Requisitos y versiones del proyecto
- pom.xml declara Java 21 y Spring Boot 4.1.1.
- package.json declara Angular ^22.1.0, TypeScript ~6.0.2 y RxJS ~7.8.0. package-lock.json fija las versiones resueltas.
- En este equipo se encontró Node 24.19.0, npm 11.17.0 y un JDK 26 instalado. El Java del PATH era 17.
- El campo packageManager declara npm 12.0.2, aunque la compilación se comprobó con el npm instalado.
- No hay Maven Wrapper (mvnw) en este proyecto. Necesitas Maven instalado o usar su ruta completa.

Antes de iniciar, comprueba `java -version`, `node --version`, `npm.cmd --version` y `mvn -version`. Si Maven no se reconoce, usa la ruta que aparece más abajo. No cambies java.version a 17 solo para ocultar el problema.

### Paso 1: XAMPP
Inicia MySQL en el panel XAMPP. Inicia Apache si vas a usar phpMyAdmin.
Abre http://localhost/phpmyadmin y usa Importar para ejecutar `db/preparar_sin_borrar.sql`.
No importes el SQL original sobre una base con datos que quieras conservar: contiene DROP TABLE.
La base esperada es `tallerEvaluativo` (en Windows puede aparecer como `tallerevaluativo`).

### Paso 2: terminal del backend
Desde la carpeta de pom.xml:
```powershell
mvn spring-boot:run
```
En ESTE equipo puedes usar las instalaciones encontradas:
```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-26.0.2.1'
& 'C:\Users\Usuario\Documents\Codex\2026-09-24\nec\work\apache-maven-3.9.9\bin\mvn.cmd' spring-boot:run
```
JAVA_HOME se cambia para esa terminal. Para configurar el editor Java de VS Code también debes seleccionar un JDK adecuado en su configuración de runtimes; la terminal y el editor pueden usar instalaciones distintas.

### Paso 3: segunda terminal, frontend
```powershell
cd frontend
npm.cmd install
npm.cmd start
```
Si node_modules ya está instalado y no cambiaron las dependencias, basta con npm.cmd start.
Abre http://localhost:4200. Usa localhost, porque CORS está configurado para ese origen.
Detén cada proceso con Ctrl+C en su terminal.

### Compilar y ejecutar el JAR
```powershell
mvn -DskipTests package
java -jar target/taller-0.0.1-SNAPSHOT.jar
```
El primer comando compila y empaqueta, pero NO ejecuta las pruebas.
En frontend, `npm.cmd run build` genera `dist/frontend/`.

### Usuarios iniciales
| Email | Contraseña de demostración | Rol |
|---|---|---|
| admin@biblioteca.com | admin123 | ADMIN |
| lector@biblioteca.com | lector123 | LECTOR |

Estos usuarios vienen de los scripts SQL; no se crean automáticamente por Java. Las contraseñas guardadas en la tabla son hashes BCrypt, no esos textos.

## 4. Conexión a la base, explicada
En application.properties:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tallerEvaluativo?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```
- jdbc:mysql indica el protocolo del driver JDBC incluido en pom.xml.
- localhost:3306 es el servidor y puerto.
- tallerEvaluativo es el nombre de la base, no una carpeta del frontend.
- root es el usuario de base; la contraseña está vacía en esta configuración local.
- createDatabaseIfNotExist=true permite crear la base si el usuario tiene permisos.
- ddl-auto=update permite a Hibernate ajustar tablas según las entidades. No inserta los usuarios de prueba ni sustituye un respaldo.
- Spring Boot configura el DataSource y su pool Hikari a partir de estas propiedades. El proyecto no tiene un archivo PHP de conexión ni abre una conexión manual por cada controlador.
- El navegador nunca se conecta directamente a 3306: solo llama al backend en 8080.
- XAMPP de este equipo responde como MariaDB 10.4.32. La aplicación usa MySQL Connector/J. Hibernate detectó una versión de compatibilidad 5.5.5 y mostró una advertencia de dialecto; eso debe distinguirse de una conexión fallida.

## 5. Tablas, campos y relaciones
### autores → Autor.java
id: Long/BIGINT, PK autoincremental.
nombre, apellido, nacionalidad: String/VARCHAR.
fechaNacimiento: LocalDate/DATE, columna fecha_nacimiento.

### libros → Libro.java
id: PK; titulo; isbn único; anio Integer/INT; autorId Long, columna autor_id.
autorId es FK a autores.id. Muchos libros pueden pertenecer a un autor.

### usuarios → Usuario.java
id: PK; nombre; username; email único; passwordHash (password_hash, longitud 60);
rol: ADMIN o LECTOR; estado: ACTIVO o INACTIVO.
username no tiene una restricción unique en esta implementación.
Rol.java y Estado.java son enums. @Enumerated(EnumType.STRING) guarda sus nombres.

### prestamos → Prestamo.java
id: PK; libroId → libros.id; usuarioId → usuarios.id;
fechaPrestamo y fechaDevolucion: LocalDate; devuelto: Boolean.
Un usuario puede tener muchos préstamos. Un libro puede aparecer en muchos préstamos.

```text
autores (1) ─── (N) libros (1) ─── (N) prestamos (N) ─── (1) usuarios
```

Las relaciones @ManyToOne permiten leer el objeto relacionado. En Libro se guarda autorId, mientras autor usa @JoinColumn(insertable=false, updatable=false). Así dos propiedades no intentan escribir la misma columna. Prestamo hace lo mismo con libroId/libro y usuarioId/usuario.
No hay borrado en cascada configurado: una FK puede impedir eliminar un autor con libros, o un libro/usuario con préstamos.

## 6. Capas del backend y archivos
### Arranque
TallerEvaluativoApplication.main llama SpringApplication.run. @SpringBootApplication habilita la configuración y el descubrimiento de componentes del paquete.

### Controladores
- AuthController: POST /auth/login; recibe AuthRequestDto y devuelve AuthResponseDto.
- AutorController: lista paginada, consulta por ID, guarda/actualiza y elimina autores.
- LibroController: lo mismo para libros y búsqueda por título/nombre/apellido del autor.
- UsuarioController: lo mismo para usuarios y búsqueda por nombre/username.
- PrestamoController: lista, consulta, guarda y elimina préstamos. Usa JwtAuthenticationToken para identificar al usuario que pide el préstamo. Para LECTOR, asigna el usuario autenticado al crear y solo permite devolver préstamos propios al actualizar. Conserva libro, usuario y fecha original en esa devolución.

@RestController convierte respuestas Java a JSON. @RequestMapping define el prefijo.
@GetMapping, @PostMapping y @DeleteMapping definen métodos HTTP.
@RequestBody convierte JSON en DTO; @PathVariable lee IDs de la ruta; @RequestParam lee parámetros de consulta.
@Valid activa las restricciones del DTO antes de ejecutar el método.

### Servicios
AutorService, LibroService y PrestamoService llaman findAll(PageRequest.of(...)), findById, save y deleteById.
LibroService también delega buscar.
UsuarioService hace esas operaciones, incluye buscar y aplica passwordEncoder.encode antes de guardar.
AuthService busca el email sin distinguir mayúsculas, rechaza usuarios inactivos y verifica BCrypt. Genera la respuesta con id, token, email y rol.
JwtService genera el JWT; se explica más adelante.

@Service registra estas clases para inyección. @Autowired, los constructores y el inject de Angular resuelven dependencias en sus respectivos frameworks; Angular y Spring no comparten un contenedor.

### Repositorios
Los cuatro extienden JpaRepository<Entidad, Long>. Spring genera la implementación.
AutorRepository y PrestamoRepository no agregan consultas propias.
LibroRepository.buscar usa @Query con título, nombre o apellido del autor.
UsuarioRepository.buscar usa nombre o username; findByEmailIgnoreCase sirve para login e identificación.
Las consultas @Query son **JPQL**, no SQL puro: mencionan entidades Libro/Usuario y propiedades Java. Hibernate las transforma en SQL.
lower convierte a minúsculas; like y los porcentajes permiten coincidencias parciales; Pageable limita los resultados.

### DTOs: entrada, salida y validaciones
| Archivo | Entrada y validación | Salida |
|---|---|---|
| AuthRequestDto | email y password con @NotBlank | Se usa como petición |
| AuthResponseDto | Se construye en AuthService | id, token, email, rol |
| AutorDto | nombre y apellido obligatorios; fechaNacimiento @Past | Datos del autor |
| LibroDto | titulo obligatorio y autorId no nulo | Datos del libro y AutorDto anidado |
| UsuarioDto | nombre, username, email y password no vacíos; rol y estado no nulos | Datos sin copiar el hash ni el valor de la contraseña |
| PrestamoDto | libroId, usuarioId y fechaPrestamo no nulos | IDs y objetos LibroDto y UsuarioDto anidados |

toEntity convierte DTO a entidad; fromEntity convierte entidad a DTO.
@Past admite null si no se combina con @NotNull.
@NotBlank no valida el formato de un email. El input email de Angular ayuda en la interfaz, pero UsuarioDto no tiene @Email.
UsuarioDto.password sigue siendo obligatorio al editar: el formulario pide una contraseña y el servicio vuelve a hashearla. No implementa “dejar vacío para conservar”.
La respuesta de UsuarioDto puede incluir password:null, pero nunca se copia passwordHash.
No hay un @RestControllerAdvice ni mensajes de error centralizados.

## 7. API completa
Base: http://localhost:8080. No existe prefijo /api.

| Método/ruta | Función | Permiso |
|---|---|---|
| POST /auth/login | Login | Público |
| GET /autores?page=0&size=10 | Lista paginada | Autenticado |
| GET /autores/{id} | Uno | Autenticado |
| POST /autores | Crear o actualizar | ADMIN |
| DELETE /autores/{id} | Borrar | ADMIN |
| GET /libros?page=0&size=10 | Lista paginada | Autenticado |
| GET /libros/buscar?texto=...&page=0&size=10 | Buscar | Autenticado |
| GET /libros/{id} | Uno | Autenticado |
| POST /libros | Crear o actualizar | ADMIN |
| DELETE /libros/{id} | Borrar | ADMIN |
| GET /usuarios?page=0&size=10 | Lista paginada | ADMIN |
| GET /usuarios/buscar?texto=...&page=0&size=10 | Buscar | ADMIN |
| GET /usuarios/{id} | Uno | ADMIN |
| POST /usuarios | Crear o actualizar | ADMIN |
| DELETE /usuarios/{id} | Borrar | ADMIN |
| GET /prestamos?page=0&size=10 | Lista paginada | Autenticado |
| GET /prestamos/{id} | Uno | Autenticado |
| POST /prestamos | Crear/actualizar | ADMIN o LECTOR; lector restringido a su creación/devolución |
| DELETE /prestamos/{id} | Borrar | ADMIN |

/error está permitido por la configuración para el manejo de errores.
Las reglas se evalúan en el orden escrito. anyRequest().hasRole("ADMIN") cubre lo restante.
Las consultas de préstamos actualmente muestran todos los préstamos a ambos roles; no son una vista privada de “mis préstamos”.
El API permite al administrador crear préstamos, aunque el formulario de creación se muestra solo al lector, siguiendo el enunciado.

### Crear y actualizar
Este proyecto usa POST para las dos operaciones. Sin id se crea; con el id de un registro existente se actualiza mediante repository.save. No hay PUT ni PATCH.
Usa un ID realmente existente para editar; no se garantiza que un ID arbitrario cree una fila.
Las operaciones de escritura devuelven void: no debes esperar un JSON con el registro creado.

Ejemplo de autor:
```json
{"nombre":"Gabriel","apellido":"García Márquez","nacionalidad":"Colombiana","fechaNacimiento":"1927-03-06"}
```
Ejemplo de libro (sustituye autorId por el ID real):
```json
{"titulo":"Cien años de soledad","isbn":"9780307474728","anio":1967,"autorId":1}
```
Ejemplo de usuario:
```json
{"nombre":"Ana","username":"ana","email":"ana@ejemplo.com","password":"claveDemo123","rol":"LECTOR","estado":"ACTIVO"}
```
Ejemplo de préstamo:
```json
{"libroId":1,"usuarioId":2,"fechaPrestamo":"2026-10-29","fechaDevolucion":null,"devuelto":false}
```
Para actualizar se agrega id y se envían los demás campos requeridos. No es una actualización parcial.
Al crear como lector, usuarioId se sustituye por su ID real en el backend, pero el DTO todavía exige enviarlo porque @Valid se ejecuta antes.

### Paginación
El backend cuenta desde cero. page=0 es la primera página.
Page contiene content (filas), number (página), totalPages y totalElements, entre otros metadatos.
El frontend muestra number+1 si hay páginas y 0 de 0 si la lista está vacía.
Los listados usan normalmente size=10. El selector de libros de préstamos carga hasta 100 libros.
No hay búsqueda de préstamos ni búsqueda independiente de autores.

## 8. JWT explicado de principio a fin
JWT significa JSON Web Token. Es un comprobante firmado que la API puede validar.
Tiene tres partes separadas por puntos: cabecera, contenido y firma.
El contenido se codifica; **no se cifra**. No se guardan contraseñas dentro.

### Flujo del login
1. login.html recoge email y password con ngModel.
2. login.ts ejecuta entrar(), llama Auth.login y se suscribe a la respuesta.
3. services/auth.ts usa HttpClient.post a http://localhost:8080/auth/login con JSON.
4. El login es público en SecurityConfig.
5. AuthController recibe el DTO validado y llama a AuthService.login.
6. UsuarioRepository.findByEmailIgnoreCase consulta MySQL/MariaDB.
7. AuthService exige estado ACTIVO. Si está inactivo devuelve 403. Si no existe el email o falla la contraseña, devuelve 401.
8. passwordEncoder.matches compara la contraseña recibida con el hash BCrypt.
9. JwtService.generar crea sub=email, rol=[ADMIN o LECTOR], iat=hora de emisión y exp=hora de vencimiento.
10. Firma con HS256 mediante JwtEncoder y la clave app.jwt.secret.
11. app.jwt.vigencia=120 significa 120 minutos, es decir, dos horas.
12. AuthResponseDto devuelve id, token, email y rol al navegador.
13. guardarSesion almacena esos cuatro valores en localStorage y actualiza signals.
14. Angular navega a /dashboard.

### A dónde se envía después
El interceptor de frontend/src/app/interceptors/auth.ts lee el token y clona la petición:
```http
GET /libros?page=0&size=10 HTTP/1.1
Host: localhost:8080
Authorization: Bearer <token>
```
No lo manda como contraseña, como parámetro de URL ni en una cookie. El login queda excluido.
El interceptor actual agrega el token a las peticiones de HttpClient salvo login; todos los servicios actuales apuntan a localhost:8080. Si en el futuro agregas servicios externos, debes restringir el interceptor al origen del backend.

### Quién lo valida
SecurityConfig configura oauth2ResourceServer(...jwt...). Spring Security instala sus filtros; no hay una clase JwtFilter escrita a mano.
JwtDecoder verifica la firma y los tiempos del token. JwtAuthenticationConverter toma el claim rol y le agrega ROLE_; ADMIN se transforma en ROLE_ADMIN.
hasRole("ADMIN") comprueba esa autoridad.
Esto sucede antes de llegar al controlador. Un token alterado no se vuelve válido por cambiar el localStorage.

### Autenticación y autorización
Autenticación: comprobar quién eres.
Autorización: comprobar qué puedes hacer.
El guard de Angular evita entrar visualmente a ciertas rutas, pero la protección real está en Spring Security y en las comprobaciones del controlador.
Cambiar rol en localStorage puede alterar qué botones ves; no cambia la firma ni el rol del JWT.

### Contraseña, hash y clave JWT no son lo mismo
- Contraseña: texto que ingresa el usuario.
- Hash BCrypt: resultado de una función de contraseña con sal, guardado en usuarios.password_hash. No se descifra. matches verifica una candidata.
- Clave JWT: secreto del servidor para firmar/verificar tokens. Está en application.properties y no debe mandarse al frontend.
- Token: credencial temporal emitida después de comprobar la contraseña.
No hace falta comparar encode(password) con el hash guardado: BCrypt genera una sal diferente y por eso se usa matches.

### Cierre de sesión, vencimiento y estado
logout borra localStorage y pone los signals en null. El menú redirige a /login.
No hay endpoint de logout ni lista de tokens revocados en el servidor.
Un token copiado sigue siendo utilizable hasta vencer. Cambiar rol/estado/contraseña en la base no revoca de forma general tokens ya emitidos.
estaLogueado solo comprueba que exista token; no interpreta exp. No hay refresh token ni renovación automática ni redirección global al recibir 401.
La API es STATELESS: no guarda una sesión HTTP por usuario. Eso no significa que la aplicación no use base de datos.

### CORS y CSRF
CORS permite al navegador que http://localhost:4200 llame al backend. Puertos distintos son orígenes distintos.
No reemplaza los permisos por rol y no bloquea por sí solo herramientas como Postman.
CSRF, HTTP Basic y formLogin están desactivados. Esta aplicación utiliza Bearer explícito y no una cookie de sesión enviada automáticamente.

## 9. Frontend, archivo por archivo
### Arranque y estructura
- index.html: documento inicial, idioma español, título, favicon y app-root. Usa Arial del sistema, sin fuentes externas.
- main.ts: bootstrapApplication(App, appConfig).
- app.ts: componente raíz; el signal title está declarado pero no se utiliza en su plantilla.
- app.html: router-outlet donde se dibuja la ruta.
- app.css: vacío; el estilo compartido está en styles.css.
- app.config.ts: router, HttpClient, interceptor y escuchadores globales de errores.
- app.routes.ts: /login público; Main protegido por authGuard; inicio redirige a /dashboard; /usuarios tiene adminGuard. No hay ruta comodín.
- components/main.ts/html/css: reúne Nav, router-outlet y Footer; su CSS está vacío.
- components/nav/nav.ts: inyecta Auth/Router y define salir. Su HTML muestra enlaces, email y rol; Usuarios aparece solo para ADMIN. Su CSS organiza el bloque de sesión.
- components/footer/footer.ts/html/css: pie con autor y año; el estilo efectivo está en styles.css.

### Pantallas (cada una tiene .ts, .html y .css)
- login: formulario de email/contraseña, entrar y mensaje de error.
- dashboard: inicio sencillo con conteos obtenidos de totalElements y enlaces. Solo pide usuarios si el rol es ADMIN.
- autores: lista y detalle para ambos roles; formulario, edición y eliminación para ADMIN.
- libros: lista, búsqueda por título/autor y detalle con datos del autor; edición ADMIN. Se ingresa ID del autor, consultable en Autores.
- usuarios: solo ADMIN; búsqueda, creación, edición y eliminación. No muestra contraseñas.
- prestamos: formulario solo LECTOR, lista general, devolución propia para lector o de cualquier préstamo para administrador, eliminación ADMIN.
Los CSS de autores/libros/usuarios están vacíos porque comparten styles.css.
styles.css define fondo gris claro, paneles blancos, botones azules/grises, tablas con bordes y adaptación básica a pantallas pequeñas. base.css y tokens.css quedaron como notas y ya no se importan.

### Servicios HTTP
autores.ts, libros.ts, usuarios.ts y prestamos.ts construyen las URLs y exponen listar, buscarUno, guardar y eliminar. Libros/usuarios agregan buscar; encodeURIComponent protege el texto de búsqueda al ponerlo en la URL.
auth.ts gestiona login y sesión. Los servicios no dibujan HTML ni acceden directamente a la base.

### Conceptos Angular presentes
- Component: une TypeScript, HTML y CSS; los componentes usan imports propios, sin un AppModule.
- signal: estado reactivo; se lee con pagina() y se cambia con pagina.set(...).
- ngModel: une campo de formulario y propiedad.
- FormsModule: habilita ngModel y ngForm.
- (click) y (ngSubmit): llaman métodos.
- [disabled]: vincula una propiedad del botón.
- @if: muestra bloques según condición.
- @for y track registro.id: repiten filas identificadas por ID; @empty muestra una lista vacía.
- RouterLink/RouterOutlet: navegación y lugar donde se dibuja una pantalla.
- HttpClient devuelve Observables; subscribe atiende next/error y pone en marcha la petición.
- {...formulario}: copia sus propiedades a otro objeto.
- any: tipado flexible ya usado en este proyecto; no equivale a validación de datos.

## 10. Recorrido de una operación completa
Ejemplo: ADMIN crea un libro.
```text
Formulario en libros.html
  → guardar() en libros.ts
  → LibrosService.guardar()
  → interceptor agrega Authorization
  → POST localhost:8080/libros
  → Spring Security valida JWT y ADMIN
  → LibroController valida LibroDto
  → toEntity()
  → LibroService.saveOrUpdate()
  → LibroRepository.save()
  → Hibernate genera SQL por JDBC
  → tabla libros en XAMPP
  → respuesta HTTP
  → Angular vuelve a listar y actualiza la tabla
```
La lectura recorre el camino inverso: filas → entidad → DTO → JSON → signal → HTML.
Los datos no se guardan en el archivo HTML. Recargar el navegador no borra las filas de la base.

## 11. Otros archivos y carpetas del proyecto
- pom.xml: versión Java, Spring Boot y dependencias de seguridad, OAuth2 resource server/JWT, validación, JPA, MVC, driver MySQL y pruebas.
- src/test/.../TallerEvaluativoApplicationTests.java: contextLoads con @SpringBootTest; carga el contexto completo y necesita la base disponible con esta configuración. No prueba por sí solo todo el CRUD.
- frontend/package-lock.json: versiones resueltas; no se edita manualmente.
- frontend/angular.json: configuración build/serve/test, estilos, assets y límites de tamaño.
- frontend/tsconfig.json: opciones base TypeScript; tsconfig.app.json configura la aplicación y tsconfig.spec.json las pruebas.
- frontend/.editorconfig y .prettierrc: formato del código.
- frontend/public/favicon.ico: icono del navegador.
- frontend/README.md: entrada a la guía del frontend.
- .gitignore y frontend/.gitignore: archivos que no se versionan.
- .gitattributes: reglas Git sobre archivos/finales de línea.
- .git/: historial y estado del repositorio; no es código de la app.
- .vscode/settings.json: ajustes locales del editor; .vscode está ignorado por Git.
- nbactions.xml: acciones antiguas de NetBeans, no necesarias para VS Code.
- skills-lock.json y .atl/: metadatos de herramientas de asistencia; no ejecutan la biblioteca.
- .github/modernize/: metadatos/scripts de herramientas de modernización Java, no controladores ni pantallas.
- target/: clases compiladas, JAR y reportes Maven; regenerable.
- frontend/node_modules/: librerías descargadas; regenerable con npm install.
- frontend/.angular/: caché de Angular; no es código propio.
- frontend/dist/: salida compilada del frontend.
- docs/: enunciado, guías y resultados de revisión.
- scripts/probar_api.ps1: prueba HTTP local con datos temporales, comprobaciones de CRUD/roles y limpieza de los registros que crea. Requiere PowerShell 7 y los servicios iniciados.
- Biblioteca.code-workspace (en la carpeta exterior): abre la raíz correcta en VS Code, sin cambiar configuraciones globales del editor.
No debes estudiar cada archivo de node_modules como si fuera tu implementación.

## 12. Limitaciones que debes conocer
1. No hay control de existencias, ejemplares ni bloqueo de préstamos simultáneos del mismo libro.
2. No se valida en backend que fechaDevolucion sea posterior a fechaPrestamo.
3. Los listados de préstamos no filtran por usuario; ambos roles pueden leerlos.
4. El selector de préstamos solo carga los primeros 100 libros.
5. Cambios de rol/estado no revocan JWT emitidos y logout solo borra la copia local.
6. La clave JWT está escrita en configuración y las credenciales son de práctica local.
7. Los errores de integridad de BD no tienen traducción centralizada a mensajes detallados.
8. Las consultas por ID retornan Optional; no se definió expresamente 404 para todos los recursos inexistentes.
9. No hay límites propios para page/size ni orden explícito en los listados.
10. El login muestra “Email o contraseña incorrectos” también ante fallos de conexión o cuenta inactiva.
11. Los controles del frontend ayudan a usar la app, pero el backend no valida todos los formatos y rangos.
12. No hay suite automática completa frontend/backend. Compilar no equivale a comprobar todo el sistema.
13. Es una API y un frontend separados; el proyecto no implementa una arquitectura distribuida con varios microservicios, gateway o colas.
14. MariaDB de XAMPP no es idéntico a MySQL 8; la configuración actual usa el driver/dialecto MySQL y genera una advertencia de compatibilidad.

## 13. Preguntas posibles de la profesora
**¿Dónde está la conexión?** En application.properties, propiedades spring.datasource. Spring crea el DataSource; los repositorios lo usan mediante JPA/Hibernate.

**¿Dónde está el front y el back?** Front en frontend/src; back en src/main/java. El primero corre en el navegador y el segundo en la JVM.

**¿Dónde se guardan realmente los datos?** En las tablas de tallerEvaluativo del servidor de XAMPP. Los SQL son instrucciones para prepararlas, no la base viva.

**¿Por qué XAMPP y Spring Boot?** XAMPP proporciona la base y phpMyAdmin; Spring Boot proporciona la API. Apache no ejecuta Java.

**¿Qué es una PK y una FK?** PK identifica una fila; FK referencia otra tabla y mantiene integridad. Ejemplo: libros.autor_id referencia autores.id.

**¿Qué relación hay entre autor y libro?** Uno a muchos, vista como @ManyToOne desde Libro.

**¿Qué hace JPA y qué hace Hibernate?** JPA define el modelo de persistencia; Hibernate es la implementación usada que transforma objetos/operaciones en SQL.

**¿Por qué Repository es una interfaz sin implementación escrita?** Spring Data genera la implementación de JpaRepository.

**¿Es SQL lo que hay en @Query?** Es JPQL: usa entidades y propiedades Java. El SQL real lo genera Hibernate.

**¿Para qué sirve el DTO?** Define el contrato HTTP, valida datos y evita devolver directamente la entidad, especialmente el hash de contraseña.

**¿Cómo se actualiza?** POST con ID existente y los campos requeridos; save guarda el estado. No hay PUT en este proyecto.

**¿Cómo funciona la búsqueda?** El texto viaja como parámetro; @Query usa lower y like sobre campos del libro/autor o usuario.

**¿Para qué sirve paginar?** Para traer solo un grupo de registros, no toda la tabla. Se envían page y size y se recibe Page.

**¿Dónde se crea el token?** JwtService.generar, después de verificar la contraseña en AuthService.

**¿Dónde queda y cómo viaja?** localStorage; interceptor lo agrega como Authorization: Bearer.

**¿Qué contiene?** Email en sub, rol, fecha de emisión y vencimiento. No contiene la contraseña.

**¿Está cifrado?** No. Está firmado con HS256; el contenido se puede leer, pero no modificar sin invalidar la firma.

**¿Quién valida el JWT si no hay JwtFilter.java?** Los filtros de Spring Security configurados por oauth2ResourceServer y JwtDecoder.

**¿Cuánto dura?** 120 minutos según app.jwt.vigencia.

**¿Qué diferencia hay entre 401 y 403?** 401 suele indicar falta de autenticación válida; 403, acceso prohibido. Aquí una cuenta inactiva también recibe 403 al intentar login.

**¿Puedo cambiar localStorage a ADMIN?** Eso cambia la interfaz, pero no el rol firmado ni los permisos del backend.

**¿Se puede descifrar BCrypt?** No; se verifica una contraseña con matches. El hash no es un cifrado reversible.

**¿Cerrar sesión destruye el token en el servidor?** No. Se borra localmente; no hay revocación.

**¿Para qué está CORS?** Permite peticiones del origen Angular autorizado desde el navegador.

**¿Qué evita que un lector solicite para otro usuario?** El backend obtiene su email del JWT, busca su usuario y reemplaza usuarioId en la creación.

**¿Qué evita que un lector devuelva el préstamo de otro?** PrestamoController compara el propietario con el usuario autenticado y devuelve 403 si no coincide.

**¿Qué pasa al borrar un autor con libros?** La FK impide eliminarlo; primero deben resolverse las dependencias.

**¿Qué probarías primero si no carga la página?** Separar las capas: Angular en 4200, API en 8080, XAMPP en 3306; revisar consola, Network y logs.

## 14. Orden recomendado para la sustentación
1. Mostrar en VS Code pom.xml, frontend y application.properties.
2. Mostrar en phpMyAdmin las cuatro tablas y las FK.
3. Iniciar sesión ADMIN; crear autor y luego libro.
4. Mostrar las filas nuevas en phpMyAdmin y relacionar sus IDs.
5. Buscar libro, abrir detalle y editarlo.
6. Crear un usuario de demostración; explicar el hash sin divulgarlo.
7. Entrar como LECTOR, pedir un préstamo y devolverlo.
8. Mostrar cambios de fecha/devuelto en la base.
9. Mostrar en Network la petición y la cabecera Authorization sin compartir el token.
10. Explicar los roles y demostrar un 401/403 con pruebas controladas.
11. Eliminar primero el préstamo de demostración, después libro/usuario y al final autor.
12. Reconocer las limitaciones del apartado 12 si te preguntan por mejoras.
