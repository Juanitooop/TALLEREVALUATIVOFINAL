# Taller Evaluativo 2 — Biblioteca

> Transcripción fiel de `docs/TALLER EVALUATIVO 2.docx`. Si hay discrepancia, el documento original manda.

**Tema:** Microservicio "Biblioteca" + Frontend + Seguridad por roles
**Duración:** 1 semana
**Modalidad:** Trabajo individual o en parejas
**Evaluación:** 40% lo que muestre · 60% sustentación individual

## Objetivo del proyecto

Integrar conocimientos de bases de datos, desarrollo backend con Spring Boot, seguridad web y consumo de APIs desde el frontend. Al finalizar, entregar una solución funcional que simule el sistema de préstamos de una biblioteca.

## Competencias a desarrollar

- Diseñar un modelo relacional en MySQL para representar autores, libros, usuarios y préstamos.
- Implementar un microservicio RESTful con Spring Boot que permita CRUD completo, búsquedas, paginación y validaciones.
- Proteger el API con Spring Security usando JWT, diferenciando roles de acceso (ADMIN y LECTOR).
- Desarrollar un frontend funcional (React, Angular, Vue u otro) que consuma el API, mostrando listas, formularios y detalles.

## Estructura de la base de datos

Se parte de la tabla `autor` ya trabajada y se completa el modelo con las siguientes entidades.

### autor

| Campo | Tipo / notas |
| --- | --- |
| id | PK |
| nombre | |
| apellido | |
| nacionalidad | |
| fecha_nacimiento | |

### libro

| Campo | Tipo / notas |
| --- | --- |
| id | PK |
| titulo | |
| isbn | |
| anio | |
| autor_id | FK → autor |

### usuario

| Campo | Tipo / notas |
| --- | --- |
| id | PK |
| nombre | |
| username | |
| email | |
| password_hash | Contraseña hasheada, nunca en texto plano |
| rol | `ADMIN` \| `LECTOR` |
| estado | `ACTIVO` \| `INACTIVO` |

### prestamo

| Campo | Tipo / notas |
| --- | --- |
| id | PK |
| libro_id | FK → libro |
| usuario_id | FK → usuario |
| fecha_prestamo | |
| fecha_devolucion | |
| devuelto | bool |

## Requisitos técnicos

### Backend (Spring Boot)

- CRUD completo para cada entidad.
- Validaciones con DTOs y anotaciones.
- Paginación y búsqueda por título, autor o usuario.
- Seguridad con JWT y roles:
  - **ADMIN:** gestiona libros y usuarios.
  - **LECTOR:** solicita préstamos y consulta libros.

### Frontend

- Listado de libros con paginación.
- Detalle de libro y autor.
- Formulario para registrar préstamo (solo LECTOR).
- Panel de administración (solo ADMIN).
- Préstamos: se debe evidenciar el funcionamiento del CRUD.

## Entregables

- Script SQL con la creación de la base de datos.
- Proyecto backend funcional en Spring Boot.
- Frontend conectado al API, con CRUD operativo.
- Sustentación en clase.

---

## Decisiones propias del proyecto

> Esta sección **no** forma parte del documento original del docente. Son decisiones tomadas al momento de implementar.

- **Frontend con Angular.** El documento permite "React, Angular, Vue u otro"; se elige Angular.
- **Sin manejador global de errores** (`@RestControllerAdvice`). No lo exige el documento: una petición inválida devuelve el 400 por defecto de Spring.
