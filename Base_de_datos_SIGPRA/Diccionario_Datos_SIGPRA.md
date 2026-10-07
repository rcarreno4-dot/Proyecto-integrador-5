# Modelado de datos de SIGPRA

## Alcance de persistencia

SIGPRA separa la persistencia en tres componentes: PostgreSQL para los datos
transaccionales, MongoDB para documentos flexibles y auditoria, y almacenamiento
de archivos para los binarios de evidencias. El almacenamiento de archivos es
un componente de persistencia, pero no un motor de base de datos.

El modelo relacional implementado como esquema SQL es la fuente de verdad para
las tablas y sus restricciones. Los diagramas ER y relacional representan ese
mismo esquema PostgreSQL. Las colecciones MongoDB y los objetos de archivo se
documentan en secciones independientes; no se les atribuyen relaciones
referenciales propias de PostgreSQL.

## Entregables del modelo

- Modelo Entidad-Relacion PostgreSQL: `imagenes/Modelo_Entidad_Relacion_SIGPRA.png`.
- Modelo relacional PostgreSQL: `imagenes/Modelo_Relacional_SIGPRA.png`.
- Modelo editable completo: `../Entrega_actual/Diagramas UML/Clases y modelos editables/Proyecto_astah_SIGPRA.asta`.
- DDL PostgreSQL: `scripts/postgresql_schema.sql`.
- Colecciones MongoDB: `scripts/mongodb_collections.js`.
- Convenciones de almacenamiento de archivos:
  `documentos/Almacenamiento_Archivos_SIGPRA.md`.

## Modelo Entidad-Relacion PostgreSQL

Las entidades y relaciones implementadas son:

| Entidad padre | Entidad relacionada | Cardinalidad |
| --- | --- | --- |
| `usuario` | `estudiante` | 1 a 0..1; FK `id_usuario` es unica |
| `usuario` | `docente_asesor` | 1 a 0..1; FK `id_usuario` es unica |
| `usuario` | `coordinador_practicas` | 1 a 0..1; FK `id_usuario` es unica |
| `institucion` | `convenio` | 1 a N |
| `convenio` | `plaza_practica` | 1 a N |
| `coordinador_practicas` | `periodo_practica` | 1 a N |
| `periodo_practica` | `rubrica` | 1 a N |
| `rubrica` | `criterio_rubrica` | 1 a N |
| `estudiante` | `asignacion` | 1 a N |
| `plaza_practica` | `asignacion` | 1 a N |
| `docente_asesor` | `asignacion` | 1 a N |
| `periodo_practica` | `asignacion` | 1 a N |
| `asignacion` | `registro_actividad` | 1 a N |
| `registro_actividad` | `evidencia` | 1 a N |
| `asignacion` | `visita_seguimiento` | 1 a N |
| `docente_asesor` | `visita_seguimiento` | 1 a N |
| `visita_seguimiento` | `evaluacion` | 1 a 0..1; `id_visita` es nullable y unico |
| `asignacion` | `evaluacion` | 1 a N |
| `rubrica` | `evaluacion` | 1 a N |
| `evaluacion` | `detalle_evaluacion` | 1 a N |
| `criterio_rubrica` | `detalle_evaluacion` | 1 a N |

Cada asignacion pertenece a un estudiante, una plaza, un docente asesor y un
periodo. La restriccion `UNIQUE (id_estudiante, id_periodo)` evita mas de una
asignacion del mismo estudiante dentro del mismo periodo.

## Modelo relacional y diccionario PostgreSQL

Notacion: `PK` clave primaria, `FK` clave foranea, `UQ` unica, `NN` no nulo.
`BIGSERIAL` representa un identificador autoincremental. Si no se indica un
valor por defecto, la columna no tiene uno en el DDL.

## 1. Tabla `usuario`
**Descripción:** Cuentas de usuario, roles y estado de acceso.

| Clave primaria | Nombre de campo | Tipo de datos | Valor nulo | Único | Condición | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| PK | id_usuario | BIGSERIAL | NO | SI | NO | Identificador único de la cuenta de usuario |
| | nombres | VARCHAR(120) | NO | NO | NO | Nombres del usuario |
| | apellidos | VARCHAR(120) | NO | NO | NO | Apellidos del usuario |
| | correo | VARCHAR(180) | NO | SI | NO | Correo electrónico de acceso |
| | password_hash | VARCHAR(255) | NO | NO | NO | Hash de la contraseña de acceso |
| | rol | VARCHAR(30) | NO | NO | ESTUDIANTE, DOCENTE_ASESOR, COORDINADOR o DIRECTOR | Rol asignado al usuario |
| | estado | VARCHAR(20) | NO | NO | Default 'ACTIVO'; ACTIVO o INACTIVO | Estado actual del usuario |
| | creado_en | TIMESTAMPTZ | NO | NO | Default CURRENT_TIMESTAMP | Fecha y hora de creación del registro |

## 2. Tabla `estudiante`
**Descripción:** Datos académicos específicos de los estudiantes.

| Clave primaria | Nombre de campo | Tipo de datos | Valor nulo | Único | Condición | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| PK | id_estudiante | BIGSERIAL | NO | SI | NO | Identificador único del perfil estudiante |
| FK | id_usuario | BIGINT | NO | SI | NO | Clave foránea referenciando a la tabla usuario |
| | codigo_estudiantil | VARCHAR(40) | NO | SI | NO | Código institucional del estudiante |
| | documento | VARCHAR(40) | NO | SI | NO | Número de documento de identidad |
| | programa | VARCHAR(160) | NO | NO | NO | Programa académico al que pertenece |
| | semestre | INTEGER | NO | NO | Entre 1 y 12 | Semestre académico actual |
| | estado_matricula | VARCHAR(30) | NO | NO | Default 'HABILITADO'; HABILITADO, NO_HABILITADO o EGRESADO | Estado de matrícula académica |

## 3. Tabla `docente_asesor`
**Descripción:** Información sobre la capacidad y acompañamiento de docentes asesores.

| Clave primaria | Nombre de campo | Tipo de datos | Valor nulo | Único | Condición | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| PK | id_docente_asesor | BIGSERIAL | NO | SI | NO | Identificador único del perfil docente asesor |
| FK | id_usuario | BIGINT | NO | SI | NO | Clave foránea referenciando a la tabla usuario |
| | profesion | VARCHAR(120) | SI | NO | NO | Profesión o área de especialidad |
| | cupo_maximo | INTEGER | NO | NO | Default 10; Mayor que 0 | Capacidad máxima de estudiantes acompañados |
| | activo | BOOLEAN | NO | NO | Default TRUE | Indica si el docente se encuentra activo |

## 4. Tabla `coordinador_practicas`
**Descripción:** Datos sobre la coordinación académica de las prácticas.

| Clave primaria | Nombre de campo | Tipo de datos | Valor nulo | Único | Condición | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| PK | id_coordinador | BIGSERIAL | NO | SI | NO | Identificador único del coordinador |
| FK | id_usuario | BIGINT | NO | SI | NO | Clave foránea referenciando a la tabla usuario |
| | programa | VARCHAR(160) | NO | NO | NO | Programa académico que coordina |
| | activo | BOOLEAN | NO | NO | Default TRUE | Indica si el coordinador está activo |

## 5. Tabla `institucion`
**Descripción:** Información general de las instituciones receptoras.

| Clave primaria | Nombre de campo | Tipo de datos | Valor nulo | Único | Condición | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| PK | id_institucion | BIGSERIAL | NO | SI | NO | Identificador único de la institución receptora |
| | nombre | VARCHAR(180) | NO | NO | NO | Nombre de la institución |
| | nit | VARCHAR(40) | SI | SI | NO | Número de Identificación Tributaria (NIT) |
| | direccion | VARCHAR(250) | SI | NO | NO | Dirección de la sede |
| | telefono | VARCHAR(50) | SI | NO | NO | Teléfono de contacto |
| | correo_contacto | VARCHAR(180) | SI | NO | NO | Correo electrónico institucional de contacto |
| | responsable_contacto | VARCHAR(160) | SI | NO | NO | Nombre de la persona encargada en la institución |
| | activa | BOOLEAN | NO | NO | Default TRUE | Estado de disponibilidad de la institución |

## 6. Tabla `convenio`
**Descripción:** Vigencia y términos de convenios con las instituciones.

| Clave primaria | Nombre de campo | Tipo de datos | Valor nulo | Único | Condición | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| PK | id_convenio | BIGSERIAL | NO | SI | NO | Identificador único del convenio |
| FK | id_institucion | BIGINT | NO | NO | NO | Clave foránea referenciando a la tabla institucion |
| | numero_convenio | VARCHAR(80) | NO | SI | NO | Código o número oficial del convenio |
| | fecha_inicio | DATE | NO | NO | NO | Fecha de inicio de vigencia |
| | fecha_fin | DATE | NO | NO | Igual o posterior a fecha_inicio | Fecha de vencimiento del convenio |
| | documento_url | VARCHAR(400) | SI | NO | NO | Ruta o enlace al documento escaneado |
| | estado | VARCHAR(20) | NO | NO | Default 'VIGENTE'; VIGENTE, VENCIDO o SUSPENDIDO | Estado actual del convenio |

## 7. Tabla `plaza_practica`
**Descripción:** Definición de cupos y horarios asociados a cada convenio.

| Clave primaria | Nombre de campo | Tipo de datos | Valor nulo | Único | Condición | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| PK | id_plaza | BIGSERIAL | NO | SI | NO | Identificador único de la plaza de práctica |
| FK | id_convenio | BIGINT | NO | NO | NO | Clave foránea referenciando a la tabla convenio |
| | nivel_practica | VARCHAR(80) | NO | NO | NO | Nivel o módulo de la práctica |
| | jornada | VARCHAR(40) | NO | NO | MANANA, TARDE, NOCHE o MIXTA | Jornada horaria ofrecida |
| | cupos_ofrecidos | INTEGER | NO | NO | Mayor o igual a 0 | Total de plazas disponibles inicialmente |
| | cupos_ocupados | INTEGER | NO | NO | Default 0; Entre 0 y cupos_ofrecidos | Plazas actualmente asignadas |
| | docente_titular | VARCHAR(160) | SI | NO | NO | Docente titular asignado en la institución |
| | estado | VARCHAR(20) | NO | NO | Default 'DISPONIBLE'; DISPONIBLE, SIN_CUPOS o INACTIVA | Disponibilidad de la plaza |

## 8. Tabla `periodo_practica`
**Descripción:** Configuración académica de los períodos lectivos de prácticas.

| Clave primaria | Nombre de campo | Tipo de datos | Valor nulo | Único | Condición | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| PK | id_periodo | BIGSERIAL | NO | SI | NO | Identificador único del período académico |
| FK | id_coordinador | BIGINT | NO | NO | NO | Clave foránea referenciando a coordinador_practicas |
| | programa | VARCHAR(160) | NO | NO | NO | Programa asociado al período |
| | nivel_practica | VARCHAR(80) | NO | NO | NO | Nivel académico de la práctica |
| | anio | INTEGER | NO | NO | Mínimo 2026 | Año lectivo |
| | semestre | INTEGER | NO | NO | 1 o 2 | Semestre académico del año |
| | fecha_inicio | DATE | NO | NO | NO | Fecha inicial del período |
| | fecha_fin | DATE | NO | NO | Igual o posterior a fecha_inicio | Fecha final del período |
| | fecha_limite_reportes | DATE | NO | NO | No posterior a fecha_fin | Fecha límite para entrega de reportes |
| | horas_minimas | INTEGER | NO | NO | Mayor que 0 | Horas requeridas para aprobar el período |
| | estado | VARCHAR(20) | NO | NO | Default 'CONFIGURACION'; CONFIGURACION, PUBLICADO o CERRADO | Estado del proceso de prácticas |

## 9. Tabla `rubrica`
**Descripción:** Instrumentos de evaluación asignados a cada período.

| Clave primaria | Nombre de campo | Tipo de datos | Valor nulo | Único | Condición | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| PK | id_rubrica | BIGSERIAL | NO | SI | NO | Identificador único de la rúbrica |
| FK | id_periodo | BIGINT | NO | NO | NO | Clave foránea referenciando a periodo_practica |
| | nombre | VARCHAR(160) | NO | NO | NO | Nombre del instrumento de evaluación |
| | descripcion | TEXT | SI | NO | NO | Detalle u objetivos de la rúbrica |
| | activa | BOOLEAN | NO | NO | Default TRUE | Indica si está activa para ser usada |

## 10. Tabla `criterio_rubrica`
**Descripción:** Criterios de evaluación y sus ponderaciones dentro de una rúbrica.

| Clave primaria | Nombre de campo | Tipo de datos | Valor nulo | Único | Condición | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| PK | id_criterio | BIGSERIAL | NO | SI | NO | Identificador único del criterio |
| FK | id_rubrica | BIGINT | NO | NO | Borrado en cascada | Clave foránea referenciando a la tabla rubrica |
| | nombre | VARCHAR(160) | NO | NO | NO | Nombre del criterio a evaluar |
| | descripcion | TEXT | SI | NO | NO | Detalle del criterio |
| | peso | NUMERIC(5,2) | NO | NO | Mayor que 0 y menor o igual a 100 | Porcentaje de ponderación en la nota |


## 11. Tabla `asignacion`
**Descripción:** Registro del vínculo de práctica entre estudiante, plaza, docente y período.

| Clave primaria | Nombre de campo | Tipo de datos | Valor nulo | Único | Condición | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| PK | id_asignacion | BIGSERIAL | NO | SI | UQ Compuesta (id_estudiante, id_periodo) | Identificador único de la asignación |
| FK | id_estudiante | BIGINT | NO | NO | UQ Compuesta (id_estudiante, id_periodo) | Clave foránea referenciando a estudiante |
| FK | id_plaza | BIGINT | NO | NO | NO | Clave foránea referenciando a plaza_practica |
| FK | id_docente_asesor | BIGINT | NO | NO | NO | Clave foránea referenciando a docente_asesor |
| FK | id_periodo | BIGINT | NO | NO | UQ Compuesta (id_estudiante, id_periodo) | Clave foránea referenciando a periodo_practica |
| | fecha_asignacion | DATE | NO | NO | Default CURRENT_DATE | Fecha de registro de la asignación |
| | estado | VARCHAR(20) | NO | NO | Default 'ACTIVA'; ACTIVA, REASIGNADA, CERRADA o CANCELADA | Estado del vínculo de práctica |
| | horas_aprobadas | NUMERIC(7,2) | NO | NO | Default 0; Mayor o igual a 0 | Acumulado de horas validadas |

## 12. Tabla `registro_actividad`
**Descripción:** Bitácora de horas y actividades realizadas por el estudiante.

| Clave primaria | Nombre de campo | Tipo de datos | Valor nulo | Único | Condición | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| PK | id_actividad | BIGSERIAL | NO | SI | NO | Identificador único del registro de actividad |
| FK | id_asignacion | BIGINT | NO | NO | Borrado en cascada | Clave foránea referenciando a asignacion |
| | fecha_actividad | DATE | NO | NO | NO | Fecha en que se realizó la actividad |
| | tipo_actividad | VARCHAR(120) | NO | NO | NO | Categoría o tipo de tarea |
| | descripcion | TEXT | NO | NO | NO | Descripción detallada de las tareas |
| | horas_reportadas | NUMERIC(5,2) | NO | NO | Mayor que 0 y menor o igual a 24 | Horas invertidas en la actividad |
| | estado | VARCHAR(20) | NO | NO | Default 'PENDIENTE'; BORRADOR, PENDIENTE, APROBADA, DEVUELTA o RECHAZADA | Estado de revisión de la bitácora |
| | observacion_validacion | TEXT | SI | NO | NO | Retroalimentación dada por el evaluador |
| | creado_en | TIMESTAMPTZ | NO | NO | Default CURRENT_TIMESTAMP | Fecha y hora de creación del registro |

## 13. Tabla `evidencia`
**Descripción:** Archivos de soporte cargados por el estudiante para justificar actividades.

| Clave primaria | Nombre de campo | Tipo de datos | Valor nulo | Único | Condición | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| PK | id_evidencia | BIGSERIAL | NO | SI | NO | Identificador único de la evidencia |
| FK | id_actividad | BIGINT | NO | NO | Borrado en cascada | Clave foránea referenciando a registro_actividad |
| | mongo_document_id | VARCHAR(80) | SI | NO | NO | Identificador de documento en MongoDB |
| | nombre_archivo | VARCHAR(220) | NO | NO | NO | Nombre del archivo subido |
| | tipo_archivo | VARCHAR(80) | NO | NO | NO | Tipo MIME o extensión del archivo |
| | archivo_url | VARCHAR(500) | NO | NO | NO | Ruta completa o enlace de almacenamiento |
| | hash_archivo | VARCHAR(128) | SI | NO | NO | Hash de verificación del archivo |
| | estado | VARCHAR(20) | NO | NO | Default 'CARGADA'; CARGADA, VALIDADA o RECHAZADA | Estado de validación del archivo |
| | cargado_en | TIMESTAMPTZ | NO | NO | Default CURRENT_TIMESTAMP | Fecha y hora de carga de la evidencia |

## 14. Tabla `visita_seguimiento`
**Descripción:** Bitácora de acompañamiento y visitas realizadas por el docente asesor.

| Clave primaria | Nombre de campo | Tipo de datos | Valor nulo | Único | Condición | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| PK | id_visita | BIGSERIAL | NO | SI | NO | Identificador único de la visita |
| FK | id_asignacion | BIGINT | NO | NO | Borrado en cascada | Clave foránea referenciando a asignacion |
| FK | id_docente_asesor | BIGINT | NO | NO | NO | Clave foránea referenciando a docente_asesor |
| | mongo_bitacora_id | VARCHAR(80) | SI | NO | NO | Identificador de bitácora en MongoDB |
| | fecha_visita | DATE | NO | NO | NO | Fecha en que se llevó a cabo la visita |
| | modalidad | VARCHAR(30) | NO | NO | PRESENCIAL, VIRTUAL o MIXTA | Modalidad de acompañamiento |
| | hubo_asistencia | BOOLEAN | NO | NO | Default TRUE | Indica si las partes asistieron a la reunión |
| | observacion_resumen | TEXT | SI | NO | NO | Resumen y compromisos de la visita |
| | creada_en | TIMESTAMPTZ | NO | NO | Default CURRENT_TIMESTAMP | Fecha y hora de registro |

## 15. Tabla `evaluacion`
**Descripción:** Evaluación final y concepto general de la práctica o seguimiento.

| Clave primaria | Nombre de campo | Tipo de datos | Valor nulo | Único | Condición | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| PK | id_evaluacion | BIGSERIAL | NO | SI | NO | Identificador único de la evaluación |
| FK | id_visita | BIGINT | SI | SI | NO | Clave foránea referenciando a visita_seguimiento |
| FK | id_asignacion | BIGINT | NO | NO | NO | Clave foránea referenciando a asignacion |
| FK | id_rubrica | BIGINT | NO | NO | NO | Clave foránea referenciando a rubrica |
| | puntaje_total | NUMERIC(5,2) | SI | NO | NO | Calificación final calculada |
| | concepto_final | VARCHAR(40) | SI | NO | APROBADO, APROBADO_CON_RECOMENDACIONES o NO_APROBADO | Dictamen cuantitativo/cualitativo final |
| | observacion_general | TEXT | SI | NO | NO | Comentarios generales sobre la evaluación |
| | evaluada_en | TIMESTAMPTZ | NO | NO | Default CURRENT_TIMESTAMP | Fecha y hora del registro de evaluación |

## 16. Tabla `detalle_evaluacion`
**Descripción:** Desglose del puntaje asignado a cada criterio dentro de una evaluación.

| Clave primaria | Nombre de campo | Tipo de datos | Valor nulo | Único | Condición | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| PK | id_detalle | BIGSERIAL | NO | SI | UQ Compuesta (id_evaluacion, id_criterio) | Identificador único del detalle |
| FK | id_evaluacion | BIGINT | NO | NO | UQ Compuesta (id_evaluacion, id_criterio) / Borrado en cascada | Clave foránea referenciando a evaluacion |
| FK | id_criterio | BIGINT | NO | NO | UQ Compuesta (id_evaluacion, id_criterio) | Clave foránea referenciando a criterio_rubrica |
| | puntaje | NUMERIC(5,2) | NO | NO | Entre 0 y 5 | Nota asignada para el criterio |
| | observacion | TEXT | SI | NO | NO | Comentario puntual sobre el criterio |

El DDL tambien define indices para convenios por institucion, plazas por
convenio/estado, asignaciones por periodo/estado, actividades por
asignacion/estado, evidencias por actividad y visitas por asignacion. La vista
`v_estado_practicas` consolida estudiante, periodo, institucion y horas
reportadas aprobadas.

## Diccionario documental MongoDB

Base: `sigpra_documental`. MongoDB conserva documentos JSON/BSON y sus
referencias a identificadores PostgreSQL; dichas referencias no son claves
foraneas validadas automaticamente entre motores.

## 17. Colección `evidencias_documentales` (MongoDB — `sigpra_documental`)

**Descripción:** Almacenamiento no relacional de metadatos y referencias documentales de evidencias.

| Clave primaria | Nombre de campo | Tipo BSON | Valor nulo | Único | Condición | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| PK | _id | ObjectId | NO | SI | NO | Identificador único generado por MongoDB |
| FK | actividad_id | long | NO | NO | Índice simple aplicados | Referencia a `registro_actividad.id_actividad` |
| FK | evidencia_relacional_id | long | SI | NO | Índice simple aplicados | Referencia a `evidencia.id_evidencia` |
| | nombre_archivo | string | NO | NO | NO | Nombre del archivo subido |
| | archivo_url | string | NO | NO | NO | Ruta o enlace al almacenamiento de archivos |
| | tipo_soporte | string | NO | NO | Índice compuesto con `creado_en` (desc) | Clasificación funcional de la evidencia |
| | mime_type | string | SI | NO | NO | Tipo MIME del archivo |
| | hash_archivo | string | SI | NO | NO | Hash de integridad del archivo |
| | metadatos | object | SI | NO | NO | Documento con datos flexibles de la evidencia |
| FK | creado_por_usuario_id | long | SI | NO | NO | Referencia a `usuario.id_usuario` |
| | creado_en | date | NO | NO | Índice compuesto con `tipo_soporte` | Fecha de creación del registro |

## 18. Colección `bitacoras_visita` (MongoDB — `sigpra_documental`)

**Descripción:** Contenido extendido, compromisos y anexos de las visitas de seguimiento.

| Clave primaria | Nombre de campo | Tipo BSON | Valor nulo | Único | Condición | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| PK | _id | ObjectId | NO | SI | NO | Identificador único generado por MongoDB |
| FK | visita_id | long | NO | SI | Índice único | Referencia a `visita_seguimiento.id_visita` |
| FK | asignacion_id | long | NO | NO | Índice compuesto con `creado_en` (desc) | Referencia a `asignacion.id_asignacion` |
| FK | docente_asesor_id | long | SI | NO | NO | Referencia a `docente_asesor.id_docente_asesor` |
| | observaciones | string | NO | NO | NO | Detalle extendido de la visita |
| | compromisos | array | SI | NO | NO | Arreglo con los compromisos acordados |
| | anexos | array | SI | NO | NO | Arreglo de referencias a anexos en almacenamiento |
| | creado_en | date | NO | NO | Índice compuesto con `asignacion_id` | Fecha de creación del registro |

## 19. Colección `auditoria_eventos` (MongoDB — `sigpra_documental`)

**Descripción:** Registro unificado de auditoría de eventos y trazabilidad del sistema.

| Clave primaria | Nombre de campo | Tipo BSON | Valor nulo | Único | Condición | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| PK | _id | ObjectId | NO | SI | NO | Identificador único generado por MongoDB |
| | evento | string | NO | NO | NO | Acción o evento auditado |
| FK | usuario_id | long | NO | NO | Índice compuesto con `fecha_evento` (desc) | Referencia a `usuario.id_usuario` |
| | rol | string | SI | NO | NO | Rol que poseía el usuario al momento del evento |
| | entidad | string | SI | NO | Índice compuesto con `entidad_id` | Tipo de objeto o entidad afectada |
| | entidad_id | long | SI | NO | Índice compuesto con `entidad` | Identificador del objeto o entidad afectada |
| | origen | string | NO | NO | NO | Origen funcional donde se generó el evento |
| | detalle | object | SI | NO | NO | Documento flexible con datos del evento |
| | ip | string | SI | NO | NO | Dirección IP de origen si está disponible |
| | fecha_evento | date | NO | NO | Índice simple (desc) y compuestos | Fecha y hora exacta del evento |

## Diccionario del almacenamiento de archivos

El almacenamiento de archivos contiene los bytes originales; no es una cuarta
tabla ni otro motor de base de datos. El sistema mantiene en PostgreSQL la
referencia principal y puede mantener en MongoDB metadatos variables.

## 20. Almacenamiento de archivos (Metadatos y Reglas físicas)

**Descripción:** Definición del almacenamiento de binarios originales en el servidor de archivos.

| Nombre de dato | Representación | Valor nulo | Único | Regla / Condición | Descripción |
| :--- | :--- | :--- | :--- | :--- | :--- |
| Identificador funcional | IDs en ruta | NO | NO | Basado en asignación, actividad o visita | Relaciona el archivo con su contexto dentro de SIGPRA |
| Ruta / Clave | String (`storage/sigpra/...`) | NO | SI | Única por objeto dentro del almacenamiento | Ruta física: `{periodo}/asignacion-{id}/actividad-{id}/{nombre}` |
| Nombre original | String | NO | NO | Coincide con `evidencia.nombre_archivo` | Nombre original para mostrar al usuario |
| Contenido | Binario (PDF, JPG, PNG, DOCX, etc.) | NO | NO | Formatos autorizados únicamente | No se almacena dentro de las filas de PostgreSQL |
| Tipo MIME y extensión | String | NO | NO | Validar en servidor; rechazar ejecutables | Tipo de contenido y extensión del archivo |
| Tamaño | Integer / Long | NO | NO | Límite según política institucional | Peso total en bytes del archivo binario |
| Hash | String (SHA-256) | SI | NO | Coincide con `evidencia.hash_archivo` | Verifica la integridad y detecta duplicados |
| Fecha y usuario de carga | Date / Long | NO | NO | Registrado en MongoDB / Storage | Mantiene la trazabilidad de la carga del archivo |
| Control de acceso | Políticas de servicio | NO | NO | Restringido por rol y por asignación | Permisos de lectura/escritura del objeto |

Las rutas son referencias ilustrativas, no prueba de que el servicio de archivos
ya este desplegado. La aplicacion demostrativa actual persiste datos y
referencias en `localStorage`; no almacena el binario ni se conecta aun a
PostgreSQL o MongoDB.

## Trazabilidad con los otros componentes

- PostgreSQL: `scripts/postgresql_schema.sql`.
- MongoDB: `scripts/mongodb_collections.js`.
- Archivos y seguridad: `documentos/Almacenamiento_Archivos_SIGPRA.md`.
- Arquitectura de persistencia: `imagenes/arquitectura_persistencia.svg`.
