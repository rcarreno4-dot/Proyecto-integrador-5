-- ==========================================================
-- PROYECTOJD - Datos base para pruebas en la BD de la U
-- Ejecutar después de conectarse como PROYECTOJD.
-- Este script NO borra tablas. Solo deja usuarios y registros mínimos.
-- Contraseña sugerida para usuarios de prueba: 1234
-- ==========================================================

-- Usuarios mínimos por rol. Ajustado a la tabla USUARIO real de la U:
-- ID_USUARIO, NOMBRE, APELLIDO, CORREO, PASSWORD, ROL, ESTADO, EMAIL, TIPO_USUARIO
MERGE INTO USUARIO u
USING (SELECT 1 id_usuario, 'Administrador' nombre, 'Principal' apellido, 'admin@proyectojd.com' correo, '1234' password, 'DIRECTOR' tipo FROM dual) d
ON (u.id_usuario = d.id_usuario)
WHEN MATCHED THEN UPDATE SET u.nombre=d.nombre, u.apellido=d.apellido, u.correo=d.correo, u.email=d.correo, u.password=d.password, u.rol=d.tipo, u.tipo_usuario=d.tipo, u.estado='ACTIVO'
WHEN NOT MATCHED THEN INSERT (id_usuario,nombre,apellido,correo,password,rol,estado,email,tipo_usuario) VALUES (d.id_usuario,d.nombre,d.apellido,d.correo,d.password,d.tipo,'ACTIVO',d.correo,d.tipo);

MERGE INTO USUARIO u
USING (SELECT 2 id_usuario, 'Carlos' nombre, 'Coordinador' apellido, 'carlos@proyectojd.com' correo, '1234' password, 'COORDINADOR' tipo FROM dual) d
ON (u.id_usuario = d.id_usuario)
WHEN MATCHED THEN UPDATE SET u.nombre=d.nombre, u.apellido=d.apellido, u.correo=d.correo, u.email=d.correo, u.password=d.password, u.rol=d.tipo, u.tipo_usuario=d.tipo, u.estado='ACTIVO'
WHEN NOT MATCHED THEN INSERT (id_usuario,nombre,apellido,correo,password,rol,estado,email,tipo_usuario) VALUES (d.id_usuario,d.nombre,d.apellido,d.correo,d.password,d.tipo,'ACTIVO',d.correo,d.tipo);

MERGE INTO USUARIO u
USING (SELECT 3 id_usuario, 'Maria' nombre, 'Docente' apellido, 'maria@proyectojd.com' correo, '1234' password, 'DOCENTE' tipo FROM dual) d
ON (u.id_usuario = d.id_usuario)
WHEN MATCHED THEN UPDATE SET u.nombre=d.nombre, u.apellido=d.apellido, u.correo=d.correo, u.email=d.correo, u.password=d.password, u.rol=d.tipo, u.tipo_usuario=d.tipo, u.estado='ACTIVO'
WHEN NOT MATCHED THEN INSERT (id_usuario,nombre,apellido,correo,password,rol,estado,email,tipo_usuario) VALUES (d.id_usuario,d.nombre,d.apellido,d.correo,d.password,d.tipo,'ACTIVO',d.correo,d.tipo);

MERGE INTO USUARIO u
USING (SELECT 4 id_usuario, 'Juan' nombre, 'Estudiante' apellido, 'juan@proyectojd.com' correo, '1234' password, 'ESTUDIANTE' tipo FROM dual) d
ON (u.id_usuario = d.id_usuario)
WHEN MATCHED THEN UPDATE SET u.nombre=d.nombre, u.apellido=d.apellido, u.correo=d.correo, u.email=d.correo, u.password=d.password, u.rol=d.tipo, u.tipo_usuario=d.tipo, u.estado='ACTIVO'
WHEN NOT MATCHED THEN INSERT (id_usuario,nombre,apellido,correo,password,rol,estado,email,tipo_usuario) VALUES (d.id_usuario,d.nombre,d.apellido,d.correo,d.password,d.tipo,'ACTIVO',d.correo,d.tipo);

MERGE INTO USUARIO u
USING (SELECT 5 id_usuario, 'Institucion' nombre, 'Receptora' apellido, 'empresa@proyectojd.com' correo, '1234' password, 'INSTITUCION' tipo FROM dual) d
ON (u.id_usuario = d.id_usuario)
WHEN MATCHED THEN UPDATE SET u.nombre=d.nombre, u.apellido=d.apellido, u.correo=d.correo, u.email=d.correo, u.password=d.password, u.rol=d.tipo, u.tipo_usuario=d.tipo, u.estado='ACTIVO'
WHEN NOT MATCHED THEN INSERT (id_usuario,nombre,apellido,correo,password,rol,estado,email,tipo_usuario) VALUES (d.id_usuario,d.nombre,d.apellido,d.correo,d.password,d.tipo,'ACTIVO',d.correo,d.tipo);

COMMIT;

-- Si alguna tabla académica no tiene datos, usar el botón "Crear Datos Base" desde los dashboards.
-- Ese botón detecta columnas existentes y crea institución, programa, curso, grupo, práctica, matrícula y rúbrica cuando sea posible.
