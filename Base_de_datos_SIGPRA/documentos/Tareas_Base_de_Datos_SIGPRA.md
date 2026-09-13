# Tareas de base de datos - SIGPRA

## 1. Tareas de diseno

| Codigo | Tarea | Responsable sugerido | Resultado esperado |
| --- | --- | --- | --- |
| BD-01 | Revisar entidades de la propuesta y confirmar campos obligatorios. | Equipo completo | Lista validada de tablas y campos. |
| BD-02 | Ajustar el modelo relacional segun observaciones del docente. | Responsable BD | Modelo actualizado. |
| BD-03 | Definir convenciones de nombres para tablas, columnas e indices. | Responsable BD | Convenciones documentadas. |
| BD-04 | Definir separacion final entre PostgreSQL, MongoDB y archivos. | Equipo completo | Documento de persistencia aprobado. |

## 2. Tareas PostgreSQL

| Codigo | Tarea | Resultado esperado |
| --- | --- | --- |
| PG-01 | Crear esquema `sigpra`. | Esquema creado. |
| PG-02 | Ejecutar `scripts/postgresql_schema.sql`. | Tablas, llaves e indices creados. |
| PG-03 | Crear datos de prueba. | Usuarios, instituciones, periodo y asignaciones de ejemplo. |
| PG-04 | Crear vistas de consulta. | Vista de estado consolidado funcionando. |
| PG-05 | Probar restricciones. | Validacion de llaves, estados y horas. |

## 3. Tareas MongoDB

| Codigo | Tarea | Resultado esperado |
| --- | --- | --- |
| MG-01 | Crear base `sigpra_documental`. | Base documental creada. |
| MG-02 | Ejecutar `scripts/mongodb_collections.js`. | Colecciones, validadores e indices creados. |
| MG-03 | Registrar evidencia de prueba. | Documento de evidencia guardado. |
| MG-04 | Registrar evento de auditoria. | Evento consultable por usuario y entidad. |

## 4. Tareas de almacenamiento

| Codigo | Tarea | Resultado esperado |
| --- | --- | --- |
| FS-01 | Crear estructura base de carpetas. | Carpeta `storage/sigpra` creada. |
| FS-02 | Definir tamano maximo de archivos. | Regla documentada. |
| FS-03 | Validar extensiones permitidas. | Lista blanca definida. |
| FS-04 | Guardar ruta en PostgreSQL. | Campo `archivo_url` con referencia valida. |
| FS-05 | Guardar metadatos en MongoDB. | Documento enlazado con evidencia relacional. |

## 5. Tareas de verificacion

| Codigo | Tarea | Resultado esperado |
| --- | --- | --- |
| QA-BD-01 | Insertar estudiante, institucion, convenio, plaza y periodo. | Datos base creados sin error. |
| QA-BD-02 | Crear asignacion de practica. | Relacion estudiante-plaza-docente-periodo creada. |
| QA-BD-03 | Registrar actividad y evidencia. | Actividad en PostgreSQL y documento en MongoDB. |
| QA-BD-04 | Validar actividad. | Estado actualizado y auditoria registrada. |
| QA-BD-05 | Consultar consolidado. | Vista o consulta devuelve horas aprobadas. |

