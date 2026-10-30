-- Solo lectura: ejecutar en phpMyAdmin o cliente mysql.
USE tallerEvaluativo;
SELECT VERSION() AS motor;
SHOW TABLES;
SHOW CREATE TABLE autores;
SHOW CREATE TABLE libros;
SHOW CREATE TABLE usuarios;
SHOW CREATE TABLE prestamos;
SELECT 'autores' AS tabla, COUNT(*) AS total FROM autores
UNION ALL SELECT 'libros', COUNT(*) FROM libros
UNION ALL SELECT 'usuarios', COUNT(*) FROM usuarios
UNION ALL SELECT 'prestamos', COUNT(*) FROM prestamos;
SELECT l.id, l.titulo, l.isbn, l.anio, a.nombre, a.apellido
FROM libros l JOIN autores a ON a.id = l.autor_id ORDER BY l.id;
SELECT p.id, u.username, l.titulo, p.fecha_prestamo,
       p.fecha_devolucion, p.devuelto + 0 AS devuelto
FROM prestamos p JOIN usuarios u ON u.id = p.usuario_id
JOIN libros l ON l.id = p.libro_id ORDER BY p.id;
SELECT id, username, rol, estado, CHAR_LENGTH(password_hash) AS longitud_hash,
       LEFT(password_hash, 4) AS prefijo_hash FROM usuarios;
SELECT TABLE_NAME, COLUMN_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME
FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = DATABASE() AND REFERENCED_TABLE_NAME IS NOT NULL;
