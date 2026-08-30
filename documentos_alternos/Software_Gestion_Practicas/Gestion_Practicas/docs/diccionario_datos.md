# Diccionario de datos resumido

El sistema incluye los 39 objetos solicitados: 34 tablas operativas y 5 vistas de consulta.

## Tablas principales

| Tabla | Propósito |
|---|---|
| USUARIO | Usuarios del sistema con rol académico y credenciales. |
| ROL | Catálogo de roles: Director, Coordinador, Docente, Estudiante e Institución. |
| PROGRAMA | Programas académicos. |
| CURSO | Cursos asociados al programa. |
| PRACTICA | Prácticas académicas con horas reglamentarias y modalidad. |
| GRUPO | Grupos de práctica con docente, institución y cupo. |
| INSTITUCION | Instituciones receptoras. |
| MATRICULA_PRACTICA | Vinculación estudiante-práctica-grupo-institución y etapa académica. |
| REGISTRO_ACTIVIDAD | Actividades y horas reportadas por estudiantes. |
| HORAS_PRACTICA | Horas confirmadas por institución. |
| EVALUACION | Evaluaciones cuantitativas y cualitativas. |
| RUBRICA | Rúbricas parametrizables. |
| CRITERIO_RUBRICA | Criterios de evaluación. |
| NIVEL_DESEMPENO | Niveles de logro por criterio. |
| DOCUMENTO_ESTUDIANTE | Evidencias y documentos cargados. |
| HISTORIAL_REVISION | Cambios de etapa y revisiones académicas. |
| LOG_ACTIVIDAD | Bitácora de operaciones críticas. |
| LOGIN_AUDITORIA | Auditoría de inicio de sesión. |
| CERTIFICADO_PRACTICA | Certificados emitidos con hash verificable. |

## Vistas

| Vista | Propósito |
|---|---|
| VW_PRACTICAS_ACTIVAS | Prácticas activas por programa. |
| VW_HORAS_ESTUDIANTE | Acumulado de horas por estudiante y estado de cumplimiento. |
| VW_ESTUDIANTES_MATRICULADOS | Listado integral de estudiantes matriculados. |
| VW_EVALUACIONES_COMPLETAS | Evaluaciones con estudiante, práctica y rúbrica. |
| VW_PRACTICAS_INSTITUCION | Prácticas y estudiantes por institución receptora. |

## Campos de trazabilidad clave

- `LOG_ACTIVIDAD.ID_USUARIO`: responsable de la acción.
- `LOG_ACTIVIDAD.TABLA_AFECTADA`: tabla modificada.
- `LOG_ACTIVIDAD.TIPO_OPERACION`: INSERT, UPDATE, DELETE o evento del sistema.
- `LOG_ACTIVIDAD.VALOR_ANTERIOR`: estado anterior.
- `LOG_ACTIVIDAD.VALOR_NUEVO`: estado nuevo.
- `HISTORIAL_REVISION.ESTADO_ANTERIOR` y `ESTADO_NUEVO`: cambio de etapa.
- `CERTIFICADO_PRACTICA.HASH_VERIFICACION`: código verificable del certificado.
