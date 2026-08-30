-- Instalador completo del Sistema de Gestión de Prácticas Académicas
-- Uso en SQL*Plus:
-- CONNECT PROYECTOJD/PROYECTOJD@localhost:1521/XE
-- @instalar_todo.sql

@00_limpiar_esquema.sql
@00_schema_oracle_10g_39_objetos.sql
@01_datos_prueba.sql
@02_funciones_procedimientos_triggers.sql
@04_consultas_validacion.sql
