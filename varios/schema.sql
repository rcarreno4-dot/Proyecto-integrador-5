-- SIGPRA - Esquema PostgreSQL para primera entrega
CREATE SCHEMA IF NOT EXISTS sigpra;
SET search_path TO sigpra;

CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nombre_completo VARCHAR(150) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('ESTUDIANTE', 'DOCENTE', 'DIRECTOR')),
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO')),
    creado_en TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE programa (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE entidad_receptora (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(180) NOT NULL,
    nit VARCHAR(30) UNIQUE,
    direccion VARCHAR(250),
    contacto VARCHAR(150),
    telefono VARCHAR(30),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE practica (
    id BIGSERIAL PRIMARY KEY,
    estudiante_id BIGINT NOT NULL REFERENCES usuario(id),
    docente_id BIGINT REFERENCES usuario(id),
    programa_id BIGINT NOT NULL REFERENCES programa(id),
    entidad_receptora_id BIGINT NOT NULL REFERENCES entidad_receptora(id),
    periodo VARCHAR(20) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    horas_requeridas NUMERIC(6,2) NOT NULL CHECK (horas_requeridas > 0),
    estado VARCHAR(20) NOT NULL DEFAULT 'ASIGNADA'
        CHECK (estado IN ('ASIGNADA', 'EN_CURSO', 'CERRADA', 'REABIERTA')),
    creado_en TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (fecha_fin IS NULL OR fecha_fin >= fecha_inicio)
);

CREATE TABLE bitacora (
    id BIGSERIAL PRIMARY KEY,
    practica_id BIGINT NOT NULL REFERENCES practica(id) ON DELETE CASCADE,
    fecha DATE NOT NULL,
    actividad VARCHAR(180) NOT NULL,
    descripcion TEXT NOT NULL,
    horas NUMERIC(5,2) NOT NULL CHECK (horas > 0 AND horas <= 24),
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
        CHECK (estado IN ('PENDIENTE', 'APROBADA', 'RECHAZADA')),
    creado_en TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (practica_id, fecha, actividad)
);

CREATE TABLE validacion (
    id BIGSERIAL PRIMARY KEY,
    bitacora_id BIGINT NOT NULL REFERENCES bitacora(id) ON DELETE CASCADE,
    docente_id BIGINT NOT NULL REFERENCES usuario(id),
    resultado VARCHAR(20) NOT NULL CHECK (resultado IN ('APROBADA', 'RECHAZADA')),
    observacion TEXT,
    validado_en TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (resultado = 'APROBADA' OR NULLIF(TRIM(observacion), '') IS NOT NULL)
);

CREATE INDEX idx_practica_estudiante ON practica(estudiante_id);
CREATE INDEX idx_practica_estado_periodo ON practica(estado, periodo);
CREATE INDEX idx_bitacora_practica_estado ON bitacora(practica_id, estado);

CREATE OR REPLACE VIEW reporte_avance_practica AS
SELECT
    p.id AS practica_id,
    p.estudiante_id,
    p.periodo,
    p.estado,
    p.horas_requeridas,
    COALESCE(SUM(b.horas) FILTER (WHERE b.estado = 'APROBADA'), 0) AS horas_aprobadas,
    GREATEST(p.horas_requeridas - COALESCE(SUM(b.horas) FILTER (WHERE b.estado = 'APROBADA'), 0), 0) AS horas_pendientes
FROM practica p
LEFT JOIN bitacora b ON b.practica_id = p.id
GROUP BY p.id, p.estudiante_id, p.periodo, p.estado, p.horas_requeridas;

-- CRUD de verificacion
-- INSERT INTO programa (nombre, codigo) VALUES ('Licenciatura en Educacion', 'LE-001');
-- SELECT * FROM sigpra.reporte_avance_practica;
-- UPDATE sigpra.entidad_receptora SET activo = FALSE WHERE id = 1;
-- DELETE FROM sigpra.entidad_receptora WHERE id = 1;
