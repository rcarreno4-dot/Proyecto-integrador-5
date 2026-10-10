# Base de datos SIGPRA

Carpeta independiente para la documentacion y los artefactos de base de datos del proyecto SIGPRA.

No reemplaza la propuesta ni modifica los documentos existentes. Su objetivo es dejar organizada la decision tecnica de persistencia para la siguiente entrega.

## Estructura

- `Documento_Base_de_Datos_SIGPRA.md`: documento principal de base de datos.
- `Diccionario_Datos_SIGPRA.md`: diccionario relacional PostgreSQL y diccionarios de MongoDB y almacenamiento de archivos.
- `scripts/postgresql_schema.sql`: script inicial para la base relacional PostgreSQL.
- `scripts/migracion_2026_10_reglas_asignacion_periodo.sql`: adapta una base ya creada a las reglas de plazo de reporte y reasignacion.
- `scripts/mongodb_collections.js`: script inicial para colecciones MongoDB.
- `documentos/Almacenamiento_Archivos_SIGPRA.md`: reglas para soportes y evidencias cargadas.
- `documentos/Tareas_Base_de_Datos_SIGPRA.md`: tareas sugeridas para construir y validar la persistencia.
- `imagenes/arquitectura_persistencia.svg`: diagrama de arquitectura de persistencia.
- `imagenes/Modelo_Entidad_Relacion_SIGPRA.png`: modelo ER alineado con el DDL PostgreSQL.
- `imagenes/Modelo_Relacional_SIGPRA.png`: vista relacional de las 16 tablas PostgreSQL.
- `../Entrega_actual/Diagramas UML/Clases y modelos editables/Proyecto_astah_SIGPRA.asta`: proyecto editable que conserva los diagramas UML y las vistas del modelo PostgreSQL.

## Decision de persistencia

SIGPRA utilizara tres componentes:

1. PostgreSQL para datos relacionales y transaccionales.
2. MongoDB para informacion documental/flexible y trazabilidad.
3. Almacenamiento de archivos para soportes cargados por usuarios.

El almacenamiento de archivos es un componente de persistencia y no un motor
de base de datos. El diccionario documenta por separado las
tablas PostgreSQL, las colecciones MongoDB y los metadatos/rutas de los archivos.

La migracion se ejecuta una sola vez sobre bases creadas con el esquema
anterior. Verifica que los periodos existentes cumplan la nueva regla antes
de cambiar restricciones; si encuentra fechas incompatibles, cancela la
transaccion e informa que deben corregirse. La aplicacion web todavia usa
persistencia JSON local, no estas bases de datos.

Desde la raiz del repositorio, con acceso a PostgreSQL y una copia de
seguridad de la base existente:

```powershell
psql -U postgres -d sigpra -v ON_ERROR_STOP=1 -f Base_de_datos_SIGPRA/scripts/migracion_2026_10_reglas_asignacion_periodo.sql
```
