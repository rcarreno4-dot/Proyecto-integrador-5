-- SIGPRA - Pruebas CRUD de tablas basicas PostgreSQL
-- Objetivo: evidenciar CREATE, READ, UPDATE y DELETE sin dejar datos de prueba permanentes.
-- Ejecutar sobre la base de datos sigpra despues de cargar postgresql_schema.sql.

\set ON_ERROR_STOP on
\timing on
\echo '============================================================'
\echo 'SIGPRA - INICIO DE PRUEBAS CRUD'
\echo '============================================================'

SET search_path TO sigpra;

BEGIN;

-- Limpieza preventiva por si una ejecucion anterior quedo incompleta.
DELETE FROM plaza_practica
WHERE id_convenio IN (
    SELECT id_convenio FROM convenio WHERE numero_convenio = 'CONV-CRUD-2026-001'
);
DELETE FROM convenio WHERE numero_convenio = 'CONV-CRUD-2026-001';
DELETE FROM institucion WHERE nit = 'CRUD-TEST-2026';
DELETE FROM usuario WHERE correo = 'crud.sigpra@prueba.local';

\echo ''
\echo '1. CREATE - insercion de datos de prueba'

INSERT INTO institucion (
    nombre, nit, direccion, telefono, correo_contacto, responsable_contacto, activa
) VALUES (
    'Institucion Prueba CRUD SIGPRA',
    'CRUD-TEST-2026',
    'Calle 1 # 2-03',
    '6000000',
    'contacto.crud@prueba.local',
    'Contacto Inicial',
    TRUE
);

INSERT INTO usuario (
    nombres, apellidos, correo, password_hash, rol, estado
) VALUES (
    'Usuario',
    'Prueba CRUD',
    'crud.sigpra@prueba.local',
    'HASH_DE_PRUEBA_NO_PRODUCTIVO',
    'COORDINADOR',
    'ACTIVO'
);

INSERT INTO convenio (
    id_institucion, numero_convenio, fecha_inicio, fecha_fin, documento_url, estado
)
SELECT
    id_institucion,
    'CONV-CRUD-2026-001',
    DATE '2026-01-15',
    DATE '2026-12-15',
    'storage/sigpra/pruebas/convenio_crud.pdf',
    'VIGENTE'
FROM institucion
WHERE nit = 'CRUD-TEST-2026';

INSERT INTO plaza_practica (
    id_convenio, nivel_practica, jornada, cupos_ofrecidos, cupos_ocupados,
    docente_titular, estado
)
SELECT
    id_convenio,
    'PRACTICA I',
    'MANANA',
    3,
    0,
    'Docente Titular Prueba',
    'DISPONIBLE'
FROM convenio
WHERE numero_convenio = 'CONV-CRUD-2026-001';

\echo ''
\echo '2. READ - consulta de los registros creados'

SELECT id_institucion, nombre, nit, telefono, responsable_contacto, activa
FROM institucion
WHERE nit = 'CRUD-TEST-2026';

SELECT id_usuario, nombres, apellidos, correo, rol, estado
FROM usuario
WHERE correo = 'crud.sigpra@prueba.local';

SELECT c.id_convenio, c.numero_convenio, c.fecha_inicio, c.fecha_fin, c.estado,
       i.nombre AS institucion
FROM convenio c
JOIN institucion i ON i.id_institucion = c.id_institucion
WHERE c.numero_convenio = 'CONV-CRUD-2026-001';

SELECT pp.id_plaza, pp.nivel_practica, pp.jornada, pp.cupos_ofrecidos,
       pp.cupos_ocupados, pp.estado, c.numero_convenio
FROM plaza_practica pp
JOIN convenio c ON c.id_convenio = pp.id_convenio
WHERE c.numero_convenio = 'CONV-CRUD-2026-001';

\echo ''
\echo '3. UPDATE - modificacion de datos'

UPDATE institucion
SET telefono = '6111111',
    responsable_contacto = 'Contacto Actualizado'
WHERE nit = 'CRUD-TEST-2026';

UPDATE usuario
SET estado = 'INACTIVO'
WHERE correo = 'crud.sigpra@prueba.local';

UPDATE convenio
SET fecha_fin = DATE '2027-01-31'
WHERE numero_convenio = 'CONV-CRUD-2026-001';

UPDATE plaza_practica
SET cupos_ofrecidos = 5
WHERE id_convenio = (
    SELECT id_convenio FROM convenio WHERE numero_convenio = 'CONV-CRUD-2026-001'
);

\echo ''
\echo 'Verificacion posterior al UPDATE'

SELECT nombre, nit, telefono, responsable_contacto
FROM institucion
WHERE nit = 'CRUD-TEST-2026';

SELECT correo, rol, estado
FROM usuario
WHERE correo = 'crud.sigpra@prueba.local';

SELECT numero_convenio, fecha_fin, estado
FROM convenio
WHERE numero_convenio = 'CONV-CRUD-2026-001';

SELECT pp.nivel_practica, pp.cupos_ofrecidos, pp.cupos_ocupados, pp.estado
FROM plaza_practica pp
JOIN convenio c ON c.id_convenio = pp.id_convenio
WHERE c.numero_convenio = 'CONV-CRUD-2026-001';

\echo ''
\echo '4. DELETE - eliminacion en orden de dependencias'

DELETE FROM plaza_practica
WHERE id_convenio = (
    SELECT id_convenio FROM convenio WHERE numero_convenio = 'CONV-CRUD-2026-001'
);

DELETE FROM convenio
WHERE numero_convenio = 'CONV-CRUD-2026-001';

DELETE FROM institucion
WHERE nit = 'CRUD-TEST-2026';

DELETE FROM usuario
WHERE correo = 'crud.sigpra@prueba.local';

\echo ''
\echo '5. VERIFICACION FINAL - todos los conteos deben ser 0'

SELECT
    (SELECT COUNT(*) FROM institucion WHERE nit = 'CRUD-TEST-2026') AS instituciones_prueba,
    (SELECT COUNT(*) FROM usuario WHERE correo = 'crud.sigpra@prueba.local') AS usuarios_prueba,
    (SELECT COUNT(*) FROM convenio WHERE numero_convenio = 'CONV-CRUD-2026-001') AS convenios_prueba,
    (SELECT COUNT(*)
       FROM plaza_practica pp
       JOIN convenio c ON c.id_convenio = pp.id_convenio
      WHERE c.numero_convenio = 'CONV-CRUD-2026-001') AS plazas_prueba;

COMMIT;

\echo ''
\echo '============================================================'
\echo 'SIGPRA - FIN DE PRUEBAS CRUD'
\echo 'Si no hubo errores y los conteos finales son 0, la prueba termino correctamente.'
\echo '============================================================'
