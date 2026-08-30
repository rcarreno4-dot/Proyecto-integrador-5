-- ==========================================================
-- PROYECTOJD - FIX relación Programa / Práctica
-- Ejecutar solo si la tabla PRACTICA no tiene ID_PROGRAMA
-- y se desea que cada práctica quede asociada a un programa.
-- Compatible con Oracle 10g.
-- ==========================================================

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = 'PRACTICA'
      AND COLUMN_NAME = 'ID_PROGRAMA';

    IF v_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE PRACTICA ADD ID_PROGRAMA NUMBER(10)';
    END IF;
END;
/

-- Asignar a prácticas existentes el primer programa activo si quedaron sin programa.
UPDATE PRACTICA
SET ID_PROGRAMA = (
    SELECT MIN(ID_PROGRAMA)
    FROM PROGRAMA
    WHERE NVL(UPPER(ESTADO), 'ACTIVO') IN ('ACTIVO', 'ACTIVA')
)
WHERE ID_PROGRAMA IS NULL
  AND EXISTS (SELECT 1 FROM PROGRAMA);

COMMIT;

-- Crear llave foránea si no existe. Si ya existe, no hace nada.
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM USER_CONSTRAINTS
    WHERE CONSTRAINT_NAME = 'FK_PRACTICA_PROGRAMA';

    IF v_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE PRACTICA ADD CONSTRAINT FK_PRACTICA_PROGRAMA FOREIGN KEY (ID_PROGRAMA) REFERENCES PROGRAMA(ID_PROGRAMA)';
    END IF;
END;
/

-- Consultas de verificación
SELECT ID_PROGRAMA, NOMBRE, CODIGO, FACULTAD, DURACION_SEMESTRES, ESTADO
FROM PROGRAMA
ORDER BY ID_PROGRAMA;

SELECT p.ID_PRACTICA, p.TITULO, p.DESCRIPCION, p.HORAS_REQUERIDAS, p.TIPO_PRACTICA,
       p.ID_PROGRAMA, pr.NOMBRE AS PROGRAMA
FROM PRACTICA p
LEFT JOIN PROGRAMA pr ON p.ID_PROGRAMA = pr.ID_PROGRAMA
ORDER BY p.ID_PRACTICA;
