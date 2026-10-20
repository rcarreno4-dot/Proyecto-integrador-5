-- DDL POSTGRESQL CON CAMPO DE MOTIVO DE RECHAZO
CREATE TABLE IF NOT EXISTS registro_actividad (
    id_actividad BIGSERIAL PRIMARY KEY,
    id_asignacion BIGINT NOT NULL,
    fecha_actividad DATE NOT NULL,
    tipo_actividad VARCHAR(120) NOT NULL,
    descripcion TEXT NOT NULL,
    horas_reportadas NUMERIC(5,2) NOT NULL,
    estado VARCHAR(20) DEFAULT 'PENDIENTE' CHECK (estado IN ('BORRADOR', 'PENDIENTE', 'APROBADA', 'DEVUELTA', 'RECHAZADA')),
    observacion_validacion TEXT,
    motivo_rechazo TEXT,
    creado_en TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);