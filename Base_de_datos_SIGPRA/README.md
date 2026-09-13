# Base de datos SIGPRA

Carpeta independiente para la documentacion y los artefactos de base de datos del proyecto SIGPRA.

No reemplaza la propuesta ni modifica los documentos existentes. Su objetivo es dejar organizada la decision tecnica de persistencia para la siguiente entrega.

## Estructura

- `Documento_Base_de_Datos_SIGPRA.md`: documento principal de base de datos.
- `scripts/postgresql_schema.sql`: script inicial para la base relacional PostgreSQL.
- `scripts/mongodb_collections.js`: script inicial para colecciones MongoDB.
- `documentos/Almacenamiento_Archivos_SIGPRA.md`: reglas para soportes y evidencias cargadas.
- `documentos/Tareas_Base_de_Datos_SIGPRA.md`: tareas sugeridas para construir y validar la persistencia.
- `imagenes/arquitectura_persistencia.svg`: diagrama de arquitectura de persistencia.
- `imagenes/modelo_relacional_resumen.svg`: diagrama resumen del modelo relacional.

## Decision de persistencia

SIGPRA utilizara tres componentes:

1. PostgreSQL para datos relacionales y transaccionales.
2. MongoDB para informacion documental/flexible y trazabilidad.
3. Almacenamiento de archivos para soportes cargados por usuarios.

