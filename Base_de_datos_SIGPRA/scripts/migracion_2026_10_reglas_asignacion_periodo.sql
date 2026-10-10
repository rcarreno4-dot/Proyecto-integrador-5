-- Aplica las reglas del prototipo web a una base SIGPRA ya creada.
-- Revisar y corregir periodos cuyo limite de reportes sea anterior al fin.

BEGIN;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM sigpra.periodo_practica
        WHERE fecha_limite_reportes < fecha_fin
    ) THEN
        RAISE EXCEPTION 'Hay periodos con fecha_limite_reportes anterior a fecha_fin';
    END IF;
END $$;

DO $$
DECLARE
    restriccion RECORD;
BEGIN
    FOR restriccion IN
        SELECT conname
        FROM pg_constraint
        WHERE conrelid = 'sigpra.periodo_practica'::regclass
          AND contype = 'c'
          AND pg_get_constraintdef(oid) LIKE '%fecha_limite_reportes <= fecha_fin%'
    LOOP
        EXECUTE format('ALTER TABLE sigpra.periodo_practica DROP CONSTRAINT %I', restriccion.conname);
    END LOOP;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint
        WHERE conrelid = 'sigpra.periodo_practica'::regclass
          AND conname = 'ck_periodo_limite_reportes_desde_fin'
    ) THEN
        ALTER TABLE sigpra.periodo_practica
            ADD CONSTRAINT ck_periodo_limite_reportes_desde_fin
            CHECK (fecha_limite_reportes >= fecha_fin);
    END IF;
END $$;

ALTER TABLE sigpra.asignacion
    DROP CONSTRAINT IF EXISTS asignacion_id_estudiante_id_periodo_key;

CREATE UNIQUE INDEX IF NOT EXISTS idx_asignacion_activa_estudiante_periodo
    ON sigpra.asignacion(id_estudiante, id_periodo) WHERE estado = 'ACTIVA';

COMMIT;
