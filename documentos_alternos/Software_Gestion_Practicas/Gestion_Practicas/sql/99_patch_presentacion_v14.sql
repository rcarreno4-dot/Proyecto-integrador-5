-- ==========================================================
-- PATCH v1.4 - Presentación final: horas, incidencias y seguimiento
-- Ejecutar con el usuario del proyecto (ej: PROYECTOJD) en SQL*Plus.
-- Este script NO borra datos. Solo agrega columnas auxiliares si faltan.
-- ==========================================================
SET SERVEROUTPUT ON;

CREATE OR REPLACE PROCEDURE ADD_COL_IF_NOT_EXISTS(
    P_TABLE VARCHAR2,
    P_COLUMN VARCHAR2,
    P_DEF VARCHAR2
) AS
    V_COUNT NUMBER;
BEGIN
    SELECT COUNT(*) INTO V_COUNT
    FROM USER_TAB_COLUMNS
    WHERE TABLE_NAME = UPPER(P_TABLE)
      AND COLUMN_NAME = UPPER(P_COLUMN);

    IF V_COUNT = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE ' || P_TABLE || ' ADD (' || P_COLUMN || ' ' || P_DEF || ')';
        DBMS_OUTPUT.PUT_LINE('Columna agregada: ' || P_TABLE || '.' || P_COLUMN);
    ELSE
        DBMS_OUTPUT.PUT_LINE('Ya existe: ' || P_TABLE || '.' || P_COLUMN);
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('No se pudo agregar ' || P_TABLE || '.' || P_COLUMN || ': ' || SQLERRM);
END;
/

BEGIN
    -- REGISTRO_ACTIVIDAD: se usa como fuente principal de horas y actividades del estudiante.
    ADD_COL_IF_NOT_EXISTS('REGISTRO_ACTIVIDAD','ID_MATRICULA_PRACTICA','NUMBER');
    ADD_COL_IF_NOT_EXISTS('REGISTRO_ACTIVIDAD','HORAS','NUMBER');
    ADD_COL_IF_NOT_EXISTS('REGISTRO_ACTIVIDAD','HORAS_APROBADAS','NUMBER');
    ADD_COL_IF_NOT_EXISTS('REGISTRO_ACTIVIDAD','OBSERVACIONES','VARCHAR2(1000)');
    ADD_COL_IF_NOT_EXISTS('REGISTRO_ACTIVIDAD','ESTADO','VARCHAR2(30) DEFAULT ''PENDIENTE''');

    -- REPORTE: se usa también como módulo de incidencias/solicitudes por usuario.
    ADD_COL_IF_NOT_EXISTS('REPORTE','ID_USUARIO_GENERA','NUMBER');
    ADD_COL_IF_NOT_EXISTS('REPORTE','TIPO_REPORTE','VARCHAR2(80)');
    ADD_COL_IF_NOT_EXISTS('REPORTE','TITULO','VARCHAR2(200)');
    ADD_COL_IF_NOT_EXISTS('REPORTE','DESCRIPCION','VARCHAR2(1000)');
    ADD_COL_IF_NOT_EXISTS('REPORTE','RESPUESTA','VARCHAR2(1000)');
    ADD_COL_IF_NOT_EXISTS('REPORTE','ID_RESPONSABLE','NUMBER');
    ADD_COL_IF_NOT_EXISTS('REPORTE','FECHA_RESPUESTA','DATE');
    ADD_COL_IF_NOT_EXISTS('REPORTE','ESTADO','VARCHAR2(30) DEFAULT ''PENDIENTE''');

    -- MATRÍCULA: facilita mostrar etapa productiva y seguimiento.
    ADD_COL_IF_NOT_EXISTS('MATRICULA_PRACTICA','ETAPA_ACADEMICA','VARCHAR2(30) DEFAULT ''PRODUCTIVA''');
END;
/

DROP PROCEDURE ADD_COL_IF_NOT_EXISTS;

COMMIT;
PROMPT Patch v1.4 aplicado. Abra nuevamente el proyecto para refrescar columnas.
