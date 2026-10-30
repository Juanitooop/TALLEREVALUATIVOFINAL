-- Preparación sin borrar tablas. No modifica usuarios existentes.
CREATE DATABASE IF NOT EXISTS tallerEvaluativo
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

USE tallerEvaluativo;

-- Las tablas existentes se conservan; este script no migra estructuras antiguas.

-- ---------------------------------------------------------------------
-- autores
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS autores (
  id bigint NOT NULL AUTO_INCREMENT,
  nombre varchar(255) DEFAULT NULL,
  apellido varchar(255) DEFAULT NULL,
  nacionalidad varchar(255) DEFAULT NULL,
  fecha_nacimiento date DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ---------------------------------------------------------------------
-- libros
-- isbn es unico; autor_id apunta a autores.
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS libros (
  id bigint NOT NULL AUTO_INCREMENT,
  titulo varchar(255) DEFAULT NULL,
  isbn varchar(255) DEFAULT NULL,
  anio int DEFAULT NULL,
  autor_id bigint DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK278ekmuxh4b6i98mqypvacdcu (isbn),
  KEY FKspm6r4x8iif19d9gjsrvwev65 (autor_id),
  CONSTRAINT FKspm6r4x8iif19d9gjsrvwev65 FOREIGN KEY (autor_id) REFERENCES autores (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ---------------------------------------------------------------------
-- usuarios
-- email es unico; el password se guarda hasheado con BCrypt (60 chars).
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuarios (
  id bigint NOT NULL AUTO_INCREMENT,
  nombre varchar(255) DEFAULT NULL,
  username varchar(255) DEFAULT NULL,
  email varchar(255) DEFAULT NULL,
  password_hash varchar(60) DEFAULT NULL,
  rol enum('ADMIN','LECTOR') DEFAULT NULL,
  estado enum('ACTIVO','INACTIVO') DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UKkfsp0s1tflm1cwlj8idhqsad0 (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ---------------------------------------------------------------------
-- prestamos
-- libro_id apunta a libros, usuario_id apunta a usuarios.
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS prestamos (
  id bigint NOT NULL AUTO_INCREMENT,
  libro_id bigint DEFAULT NULL,
  usuario_id bigint DEFAULT NULL,
  fecha_prestamo date DEFAULT NULL,
  fecha_devolucion date DEFAULT NULL,
  devuelto bit(1) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FKqwwlmlxm19mwwnpng9945iy3 (libro_id),
  KEY FKeqd1t799y0x5ck9mdeltepy1w (usuario_id),
  CONSTRAINT FKqwwlmlxm19mwwnpng9945iy3 FOREIGN KEY (libro_id) REFERENCES libros (id),
  CONSTRAINT FKeqd1t799y0x5ck9mdeltepy1w FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ---------------------------------------------------------------------
-- Usuarios de prueba
--   admin@biblioteca.com  / admin123   (ADMIN)
--   lector@biblioteca.com / lector123  (LECTOR)
-- ---------------------------------------------------------------------
INSERT INTO usuarios (nombre, username, email, password_hash, rol, estado)
SELECT 'Admin', 'admin', 'admin@biblioteca.com', '$2a$10$ot/2/Jk2NetzAvN7EGd5RedHsA3z/L0yUrvsAvUrWBaqeyXSv9B3u', 'ADMIN', 'ACTIVO'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'admin@biblioteca.com');
INSERT INTO usuarios (nombre, username, email, password_hash, rol, estado)
SELECT 'Lector', 'lector', 'lector@biblioteca.com', '$2a$10$8dlv3azV8Fwd3/T.1vIQauKjjwxS3YQemkh8a7QsdK/4M0Vyw1jW6', 'LECTOR', 'ACTIVO'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'lector@biblioteca.com');
