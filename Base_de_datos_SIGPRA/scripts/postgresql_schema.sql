-- SIGPRA - Esquema inicial PostgreSQL
-- Componente relacional/transaccional.

CREATE SCHEMA IF NOT EXISTS sigpra;
SET search_path TO sigpra;

CREATE TABLE usuario (
    id_usuario BIGSERIAL PRIMARY KEY,
    nombres VARCHAR(120) NOT NULL,
    apellidos VARCHAR(120) NOT NULL,
    correo VARCHAR(180) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL CHECK (rol IN ('ESTUDIANTE', 'DOCENTE_ASESOR', 'COORDINADOR', 'DIRECTOR')),
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO')),
    creado_en TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE estudiante (
    id_estudiante BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL UNIQUE REFERENCES usuario(id_usuario),
    codigo_estudiantil VARCHAR(40) NOT NULL UNIQUE,
    documento VARCHAR(40) NOT NULL UNIQUE,
    programa VARCHAR(160) NOT NULL,
    semestre INTEGER NOT NULL CHECK (semestre BETWEEN 1 AND 12),
    estado_matricula VARCHAR(30) NOT NULL DEFAULT 'HABILITADO'
        CHECK (estado_matricula IN ('HABILITADO', 'NO_HABILITADO', 'EGRESADO'))
);

CREATE TABLE docente_asesor (
    id_docente_asesor BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL UNIQUE REFERENCES usuario(id_usuario),
    profesion VARCHAR(120),
    cupo_maximo INTEGER NOT NULL DEFAULT 10 CHECK (cupo_maximo > 0),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE coordinador_practicas (
    id_coordinador BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT NOT NULL UNIQUE REFERENCES usuario(id_usuario),
    programa VARCHAR(160) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE institucion (
    id_institucion BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(180) NOT NULL,
    nit VARCHAR(40) UNIQUE,
    direccion VARCHAR(250),
    telefono VARCHAR(50),
    correo_contacto VARCHAR(180),
    responsable_contacto VARCHAR(160),
    activa BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE convenio (
    id_convenio BIGSERIAL PRIMARY KEY,
    id_institucion BIGINT NOT NULL REFERENCES institucion(id_institucion),
    numero_convenio VARCHAR(80) NOT NULL UNIQUE,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    documento_url VARCHAR(400),
    estado VARCHAR(20) NOT NULL DEFAULT 'VIGENTE'
        CHECK (estado IN ('VIGENTE', 'VENCIDO', 'SUSPENDIDO')),
    CHECK (fecha_fin >= fecha_inicio)
);

CREATE TABLE plaza_practica (
    id_plaza BIGSERIAL PRIMARY KEY,
    id_convenio BIGINT NOT NULL REFERENCES convenio(id_convenio),
    nivel_practica VARCHAR(80) NOT NULL,
    jornada VARCHAR(40) NOT NULL CHECK (jornada IN ('MANANA', 'TARDE', 'NOCHE', 'MIXTA')),
    cupos_ofrecidos INTEGER NOT NULL CHECK (cupos_ofrecidos >= 0),
    cupos_ocupados INTEGER NOT NULL DEFAULT 0 CHECK (cupos_ocupados >= 0),
    docente_titular VARCHAR(160),
    estado VARCHAR(20) NOT NULL DEFAULT 'DISPONIBLE'
        CHECK (estado IN ('DISPONIBLE', 'SIN_CUPOS', 'INACTIVA')),
    CHECK (cupos_ocupados <= cupos_ofrecidos)
);

CREATE TABLE periodo_practica (
    id_periodo BIGSERIAL PRIMARY KEY,
    id_coordinador BIGINT NOT NULL REFERENCES coordinador_practicas(id_coordinador),
    programa VARCHAR(160) NOT NULL,
    nivel_practica VARCHAR(80) NOT NULL,
    anio INTEGER NOT NULL CHECK (anio >= 2026),
    semestre INTEGER NOT NULL CHECK (semestre IN (1, 2)),
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    fecha_limite_reportes DATE NOT NULL,
    horas_minimas INTEGER NOT NULL CHECK (horas_minimas > 0),
    estado VARCHAR(20) NOT NULL DEFAULT 'CONFIGURACION'
        CHECK (estado IN ('CONFIGURACION', 'PUBLICADO', 'CERRADO')),
    CHECK (fecha_fin >= fecha_inicio),
    CHECK (fecha_limite_reportes <= fecha_fin)
);

CREATE TABLE rubrica (
    id_rubrica BIGSERIAL PRIMARY KEY,
    id_periodo BIGINT NOT NULL REFERENCES periodo_practica(id_periodo),
    nombre VARCHAR(160) NOT NULL,
    descripcion TEXT,
    activa BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE criterio_rubrica (
    id_criterio BIGSERIAL PRIMARY KEY,
    id_rubrica BIGINT NOT NULL REFERENCES rubrica(id_rubrica) ON DELETE CASCADE,
    nombre VARCHAR(160) NOT NULL,
    descripcion TEXT,
    peso NUMERIC(5,2) NOT NULL CHECK (peso > 0 AND peso <= 100)
);

CREATE TABLE asignacion (
    id_asignacion BIGSERIAL PRIMARY KEY,
    id_estudiante BIGINT NOT NULL REFERENCES estudiante(id_estudiante),
    id_plaza BIGINT NOT NULL REFERENCES plaza_practica(id_plaza),
    id_docente_asesor BIGINT NOT NULL REFERENCES docente_asesor(id_docente_asesor),
    id_periodo BIGINT NOT NULL REFERENCES periodo_practica(id_periodo),
    fecha_asignacion DATE NOT NULL DEFAULT CURRENT_DATE,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVA'
        CHECK (estado IN ('ACTIVA', 'REASIGNADA', 'CERRADA', 'CANCELADA')),
    horas_aprobadas NUMERIC(7,2) NOT NULL DEFAULT 0 CHECK (horas_aprobadas >= 0),
    UNIQUE (id_estudiante, id_periodo)
);

CREATE TABLE registro_actividad (
    id_actividad BIGSERIAL PRIMARY KEY,
    id_asignacion BIGINT NOT NULL REFERENCES asignacion(id_asignacion) ON DELETE CASCADE,
    fecha_actividad DATE NOT NULL,
    tipo_actividad VARCHAR(120) NOT NULL,
    descripcion TEXT NOT NULL,
    horas_reportadas NUMERIC(5,2) NOT NULL CHECK (horas_reportadas > 0 AND horas_reportadas <= 24),
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
        CHECK (estado IN ('BORRADOR', 'PENDIENTE', 'APROBADA', 'DEVUELTA', 'RECHAZADA')),
    observacion_validacion TEXT,
    creado_en TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE evidencia (
    id_evidencia BIGSERIAL PRIMARY KEY,
    id_actividad BIGINT NOT NULL REFERENCES registro_actividad(id_actividad) ON DELETE CASCADE,
    mongo_document_id VARCHAR(80),
    nombre_archivo VARCHAR(220) NOT NULL,
    tipo_archivo VARCHAR(80) NOT NULL,
    archivo_url VARCHAR(500) NOT NULL,
    hash_archivo VARCHAR(128),
    estado VARCHAR(20) NOT NULL DEFAULT 'CARGADA'
        CHECK (estado IN ('CARGADA', 'VALIDADA', 'RECHAZADA')),
    cargado_en TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE visita_seguimiento (
    id_visita BIGSERIAL PRIMARY KEY,
    id_asignacion BIGINT NOT NULL REFERENCES asignacion(id_asignacion) ON DELETE CASCADE,
    id_docente_asesor BIGINT NOT NULL REFERENCES docente_asesor(id_docente_asesor),
    mongo_bitacora_id VARCHAR(80),
    fecha_visita DATE NOT NULL,
    modalidad VARCHAR(30) NOT NULL CHECK (modalidad IN ('PRESENCIAL', 'VIRTUAL', 'MIXTA')),
    hubo_asistencia BOOLEAN NOT NULL DEFAULT TRUE,
    observacion_resumen TEXT,
    creada_en TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE evaluacion (
    id_evaluacion BIGSERIAL PRIMARY KEY,
    id_visita BIGINT UNIQUE REFERENCES visita_seguimiento(id_visita),
    id_asignacion BIGINT NOT NULL REFERENCES asignacion(id_asignacion),
    id_rubrica BIGINT NOT NULL REFERENCES rubrica(id_rubrica),
    puntaje_total NUMERIC(5,2),
    concepto_final VARCHAR(40) CHECK (concepto_final IN ('APROBADO', 'APROBADO_CON_RECOMENDACIONES', 'NO_APROBADO')),
    observacion_general TEXT,
    evaluada_en TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE detalle_evaluacion (
    id_detalle BIGSERIAL PRIMARY KEY,
    id_evaluacion BIGINT NOT NULL REFERENCES evaluacion(id_evaluacion) ON DELETE CASCADE,
    id_criterio BIGINT NOT NULL REFERENCES criterio_rubrica(id_criterio),
    puntaje NUMERIC(5,2) NOT NULL CHECK (puntaje >= 0 AND puntaje <= 5),
    observacion TEXT,
    UNIQUE (id_evaluacion, id_criterio)
);

CREATE INDEX idx_convenio_institucion ON convenio(id_institucion);
CREATE INDEX idx_plaza_convenio_estado ON plaza_practica(id_convenio, estado);
CREATE INDEX idx_asignacion_periodo_estado ON asignacion(id_periodo, estado);
CREATE INDEX idx_actividad_asignacion_estado ON registro_actividad(id_asignacion, estado);
CREATE INDEX idx_evidencia_actividad ON evidencia(id_actividad);
CREATE INDEX idx_visita_asignacion ON visita_seguimiento(id_asignacion);

CREATE OR REPLACE VIEW v_estado_practicas AS
SELECT
    a.id_asignacion,
    e.codigo_estudiantil,
    u.nombres || ' ' || u.apellidos AS estudiante,
    p.programa,
    p.anio,
    p.semestre,
    i.nombre AS institucion,
    a.estado,
    p.horas_minimas,
    COALESCE(SUM(ra.horas_reportadas) FILTER (WHERE ra.estado = 'APROBADA'), 0) AS horas_aprobadas
FROM asignacion a
JOIN estudiante e ON e.id_estudiante = a.id_estudiante
JOIN usuario u ON u.id_usuario = e.id_usuario
JOIN periodo_practica p ON p.id_periodo = a.id_periodo
JOIN plaza_practica pp ON pp.id_plaza = a.id_plaza
JOIN convenio c ON c.id_convenio = pp.id_convenio
JOIN institucion i ON i.id_institucion = c.id_institucion
LEFT JOIN registro_actividad ra ON ra.id_asignacion = a.id_asignacion
GROUP BY a.id_asignacion, e.codigo_estudiantil, estudiante, p.programa, p.anio, p.semestre, i.nombre, a.estado, p.horas_minimas;

