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
- Modelo editable completo: `../Entrega_actual/Proyecto_astah_SIGPRA.asta`.
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

### `usuario` — cuentas, rol y estado

| Columna | Tipo | Reglas |
| --- | --- | --- |
| `id_usuario` | BIGSERIAL | PK |
| `nombres` | VARCHAR(120) | NN |
| `apellidos` | VARCHAR(120) | NN |
| `correo` | VARCHAR(180) | NN, UQ |
| `password_hash` | VARCHAR(255) | NN |
| `rol` | VARCHAR(30) | NN; ESTUDIANTE, DOCENTE_ASESOR, COORDINADOR o DIRECTOR |
| `estado` | VARCHAR(20) | NN, default ACTIVO; ACTIVO o INACTIVO |
| `creado_en` | TIMESTAMPTZ | NN, default CURRENT_TIMESTAMP |

### `estudiante` — datos academicos

| Columna | Tipo | Reglas |
| --- | --- | --- |
| `id_estudiante` | BIGSERIAL | PK |
| `id_usuario` | BIGINT | FK a `usuario`, NN, UQ |
| `codigo_estudiantil` | VARCHAR(40) | NN, UQ |
| `documento` | VARCHAR(40) | NN, UQ |
| `programa` | VARCHAR(160) | NN |
| `semestre` | INTEGER | NN, entre 1 y 12 |
| `estado_matricula` | VARCHAR(30) | NN, default HABILITADO; HABILITADO, NO_HABILITADO o EGRESADO |

### `docente_asesor` — capacidad de acompanamiento

| Columna | Tipo | Reglas |
| --- | --- | --- |
| `id_docente_asesor` | BIGSERIAL | PK |
| `id_usuario` | BIGINT | FK a `usuario`, NN, UQ |
| `profesion` | VARCHAR(120) | Nullable |
| `cupo_maximo` | INTEGER | NN, default 10, mayor que 0 |
| `activo` | BOOLEAN | NN, default TRUE |

### `coordinador_practicas` — coordinacion academica

| Columna | Tipo | Reglas |
| --- | --- | --- |
| `id_coordinador` | BIGSERIAL | PK |
| `id_usuario` | BIGINT | FK a `usuario`, NN, UQ |
| `programa` | VARCHAR(160) | NN |
| `activo` | BOOLEAN | NN, default TRUE |

### `institucion` — instituciones receptoras

| Columna | Tipo | Reglas |
| --- | --- | --- |
| `id_institucion` | BIGSERIAL | PK |
| `nombre` | VARCHAR(180) | NN |
| `nit` | VARCHAR(40) | Nullable, UQ |
| `direccion` | VARCHAR(250) | Nullable |
| `telefono` | VARCHAR(50) | Nullable |
| `correo_contacto` | VARCHAR(180) | Nullable |
| `responsable_contacto` | VARCHAR(160) | Nullable |
| `activa` | BOOLEAN | NN, default TRUE |

### `convenio` — vigencia con una institucion

| Columna | Tipo | Reglas |
| --- | --- | --- |
| `id_convenio` | BIGSERIAL | PK |
| `id_institucion` | BIGINT | FK a `institucion`, NN |
| `numero_convenio` | VARCHAR(80) | NN, UQ |
| `fecha_inicio` | DATE | NN |
| `fecha_fin` | DATE | NN, igual o posterior a `fecha_inicio` |
| `documento_url` | VARCHAR(400) | Nullable |
| `estado` | VARCHAR(20) | NN, default VIGENTE; VIGENTE, VENCIDO o SUSPENDIDO |

### `plaza_practica` — cupos asociados al convenio

| Columna | Tipo | Reglas |
| --- | --- | --- |
| `id_plaza` | BIGSERIAL | PK |
| `id_convenio` | BIGINT | FK a `convenio`, NN |
| `nivel_practica` | VARCHAR(80) | NN |
| `jornada` | VARCHAR(40) | NN; MANANA, TARDE, NOCHE o MIXTA |
| `cupos_ofrecidos` | INTEGER | NN, mayor o igual a 0 |
| `cupos_ocupados` | INTEGER | NN, default 0; entre 0 y `cupos_ofrecidos` |
| `docente_titular` | VARCHAR(160) | Nullable |
| `estado` | VARCHAR(20) | NN, default DISPONIBLE; DISPONIBLE, SIN_CUPOS o INACTIVA |

### `periodo_practica` — configuracion de practica

| Columna | Tipo | Reglas |
| --- | --- | --- |
| `id_periodo` | BIGSERIAL | PK |
| `id_coordinador` | BIGINT | FK a `coordinador_practicas`, NN |
| `programa` | VARCHAR(160) | NN |
| `nivel_practica` | VARCHAR(80) | NN |
| `anio` | INTEGER | NN, default no definido, minimo 2026 |
| `semestre` | INTEGER | NN; 1 o 2 |
| `fecha_inicio` | DATE | NN |
| `fecha_fin` | DATE | NN, igual o posterior a `fecha_inicio` |
| `fecha_limite_reportes` | DATE | NN, no posterior a `fecha_fin` |
| `horas_minimas` | INTEGER | NN, mayor que 0 |
| `estado` | VARCHAR(20) | NN, default CONFIGURACION; CONFIGURACION, PUBLICADO o CERRADO |

### `rubrica` — instrumento de evaluacion del periodo

| Columna | Tipo | Reglas |
| --- | --- | --- |
| `id_rubrica` | BIGSERIAL | PK |
| `id_periodo` | BIGINT | FK a `periodo_practica`, NN |
| `nombre` | VARCHAR(160) | NN |
| `descripcion` | TEXT | Nullable |
| `activa` | BOOLEAN | NN, default TRUE |

### `criterio_rubrica` — criterios y ponderaciones

| Columna | Tipo | Reglas |
| --- | --- | --- |
| `id_criterio` | BIGSERIAL | PK |
| `id_rubrica` | BIGINT | FK a `rubrica`, NN, borrado en cascada |
| `nombre` | VARCHAR(160) | NN |
| `descripcion` | TEXT | Nullable |
| `peso` | NUMERIC(5,2) | NN, mayor que 0 y menor o igual a 100 |

### `asignacion` — vinculo de practica

| Columna | Tipo | Reglas |
| --- | --- | --- |
| `id_asignacion` | BIGSERIAL | PK |
| `id_estudiante` | BIGINT | FK a `estudiante`, NN |
| `id_plaza` | BIGINT | FK a `plaza_practica`, NN |
| `id_docente_asesor` | BIGINT | FK a `docente_asesor`, NN |
| `id_periodo` | BIGINT | FK a `periodo_practica`, NN |
| `fecha_asignacion` | DATE | NN, default CURRENT_DATE |
| `estado` | VARCHAR(20) | NN, default ACTIVA; ACTIVA, REASIGNADA, CERRADA o CANCELADA |
| `horas_aprobadas` | NUMERIC(7,2) | NN, default 0, mayor o igual a 0 |
| Restriccion | — | UQ compuesta sobre (`id_estudiante`, `id_periodo`) |

### `registro_actividad` — bitacora de horas

| Columna | Tipo | Reglas |
| --- | --- | --- |
| `id_actividad` | BIGSERIAL | PK |
| `id_asignacion` | BIGINT | FK a `asignacion`, NN, borrado en cascada |
| `fecha_actividad` | DATE | NN |
| `tipo_actividad` | VARCHAR(120) | NN |
| `descripcion` | TEXT | NN |
| `horas_reportadas` | NUMERIC(5,2) | NN, mayor que 0 y menor o igual a 24 |
| `estado` | VARCHAR(20) | NN, default PENDIENTE; BORRADOR, PENDIENTE, APROBADA, DEVUELTA o RECHAZADA |
| `observacion_validacion` | TEXT | Nullable |
| `creado_en` | TIMESTAMPTZ | NN, default CURRENT_TIMESTAMP |

### `evidencia` — referencia al archivo de soporte

| Columna | Tipo | Reglas |
| --- | --- | --- |
| `id_evidencia` | BIGSERIAL | PK |
| `id_actividad` | BIGINT | FK a `registro_actividad`, NN, borrado en cascada |
| `mongo_document_id` | VARCHAR(80) | Nullable; identificador documental MongoDB |
| `nombre_archivo` | VARCHAR(220) | NN |
| `tipo_archivo` | VARCHAR(80) | NN |
| `archivo_url` | VARCHAR(500) | NN; ruta o URL del binario |
| `hash_archivo` | VARCHAR(128) | Nullable |
| `estado` | VARCHAR(20) | NN, default CARGADA; CARGADA, VALIDADA o RECHAZADA |
| `cargado_en` | TIMESTAMPTZ | NN, default CURRENT_TIMESTAMP |

### `visita_seguimiento` — acompanamiento

| Columna | Tipo | Reglas |
| --- | --- | --- |
| `id_visita` | BIGSERIAL | PK |
| `id_asignacion` | BIGINT | FK a `asignacion`, NN, borrado en cascada |
| `id_docente_asesor` | BIGINT | FK a `docente_asesor`, NN |
| `mongo_bitacora_id` | VARCHAR(80) | Nullable; identificador documental MongoDB |
| `fecha_visita` | DATE | NN |
| `modalidad` | VARCHAR(30) | NN; PRESENCIAL, VIRTUAL o MIXTA |
| `hubo_asistencia` | BOOLEAN | NN, default TRUE |
| `observacion_resumen` | TEXT | Nullable |
| `creada_en` | TIMESTAMPTZ | NN, default CURRENT_TIMESTAMP |

### `evaluacion` — resultado general

| Columna | Tipo | Reglas |
| --- | --- | --- |
| `id_evaluacion` | BIGSERIAL | PK |
| `id_visita` | BIGINT | FK a `visita_seguimiento`, Nullable, UQ |
| `id_asignacion` | BIGINT | FK a `asignacion`, NN |
| `id_rubrica` | BIGINT | FK a `rubrica`, NN |
| `puntaje_total` | NUMERIC(5,2) | Nullable |
| `concepto_final` | VARCHAR(40) | Nullable; APROBADO, APROBADO_CON_RECOMENDACIONES o NO_APROBADO |
| `observacion_general` | TEXT | Nullable |
| `evaluada_en` | TIMESTAMPTZ | NN, default CURRENT_TIMESTAMP |

### `detalle_evaluacion` — puntaje por criterio

| Columna | Tipo | Reglas |
| --- | --- | --- |
| `id_detalle` | BIGSERIAL | PK |
| `id_evaluacion` | BIGINT | FK a `evaluacion`, NN, borrado en cascada |
| `id_criterio` | BIGINT | FK a `criterio_rubrica`, NN |
| `puntaje` | NUMERIC(5,2) | NN, entre 0 y 5 |
| `observacion` | TEXT | Nullable |
| Restriccion | — | UQ compuesta sobre (`id_evaluacion`, `id_criterio`) |

El DDL tambien define indices para convenios por institucion, plazas por
convenio/estado, asignaciones por periodo/estado, actividades por
asignacion/estado, evidencias por actividad y visitas por asignacion. La vista
`v_estado_practicas` consolida estudiante, periodo, institucion y horas
reportadas aprobadas.

## Diccionario documental MongoDB

Base: `sigpra_documental`. MongoDB conserva documentos JSON/BSON y sus
referencias a identificadores PostgreSQL; dichas referencias no son claves
foraneas validadas automaticamente entre motores.

### Coleccion `evidencias_documentales`

| Campo | Tipo BSON | Reglas / uso |
| --- | --- | --- |
| `_id` | ObjectId | Identificador generado por MongoDB |
| `actividad_id` | long | Requerido; referencia a `registro_actividad.id_actividad` |
| `evidencia_relacional_id` | long o null | Referencia a `evidencia.id_evidencia` |
| `nombre_archivo` | string | Requerido |
| `archivo_url` | string | Requerido; ruta al almacenamiento de archivos |
| `tipo_soporte` | string | Requerido; clasificacion funcional |
| `mime_type` | string | Tipo MIME |
| `hash_archivo` | string o null | Hash de integridad |
| `metadatos` | object | Datos flexibles de la evidencia |
| `creado_por_usuario_id` | long o null | Referencia a `usuario.id_usuario` |
| `creado_en` | date | Requerido |

Indices: `actividad_id`, `evidencia_relacional_id` y (`tipo_soporte`,
`creado_en` descendente).

### Coleccion `bitacoras_visita`

| Campo | Tipo BSON | Reglas / uso |
| --- | --- | --- |
| `_id` | ObjectId | Identificador generado por MongoDB |
| `visita_id` | long | Requerido, unico; referencia a `visita_seguimiento.id_visita` |
| `asignacion_id` | long | Requerido; referencia a `asignacion.id_asignacion` |
| `docente_asesor_id` | long o null | Referencia a `docente_asesor.id_docente_asesor` |
| `observaciones` | string | Requerido; detalle extendido de la visita |
| `compromisos` | array | Compromisos acordados |
| `anexos` | array | Referencias a anexos en almacenamiento |
| `creado_en` | date | Requerido |

Indices: `visita_id` unico y (`asignacion_id`, `creado_en` descendente).

### Coleccion `auditoria_eventos`

| Campo | Tipo BSON | Reglas / uso |
| --- | --- | --- |
| `_id` | ObjectId | Identificador generado por MongoDB |
| `evento` | string | Requerido; accion auditada |
| `usuario_id` | long | Requerido; referencia a `usuario.id_usuario` |
| `rol` | string o null | Rol al momento del evento |
| `entidad` | string o null | Tipo de objeto afectado |
| `entidad_id` | long o null | Identificador del objeto afectado |
| `origen` | string | Requerido; origen funcional del evento |
| `detalle` | object | Datos flexibles del evento |
| `ip` | string o null | Direccion de origen, cuando este disponible |
| `fecha_evento` | date | Requerido |

Indices: (`usuario_id`, `fecha_evento` descendente), (`entidad`, `entidad_id`)
y `fecha_evento` descendente.

## Diccionario del almacenamiento de archivos

El almacenamiento de archivos contiene los bytes originales; no es una cuarta
tabla ni otro motor de base de datos. El sistema mantiene en PostgreSQL la
referencia principal y puede mantener en MongoDB metadatos variables.

| Dato del objeto | Representacion | Regla |
| --- | --- | --- |
| Identificador funcional | IDs de asignacion, actividad o visita en la ruta | Relaciona el archivo con su contexto SIGPRA |
| Ruta/clave | `storage/sigpra/{periodo}/asignacion-{id}/actividad-{id}/{nombre}` | Unica por objeto dentro del almacenamiento |
| Nombre original | Metadato del objeto y columna `evidencia.nombre_archivo` | Conservar para mostrar al usuario |
| Contenido | Binario PDF, JPG, PNG, DOCX u otro formato autorizado | El binario no se guarda dentro de las filas de PostgreSQL |
| Tipo MIME y extension | Metadatos del objeto y MongoDB | Validar en el servidor y rechazar ejecutables |
| Tamano | Metadato del objeto | Limite configurable por politica institucional |
| Hash | SHA-256 recomendado; `evidencia.hash_archivo` | Verificar integridad y detectar duplicados |
| Fecha y usuario de carga | MongoDB y/o metadatos de almacenamiento | Mantener trazabilidad de la carga |
| Control de acceso | Politicas del servicio de archivos | Restringir la lectura por rol y por asignacion |

Las rutas son referencias ilustrativas, no prueba de que el servicio de archivos
ya este desplegado. La aplicacion demostrativa actual persiste datos y
referencias en `localStorage`; no almacena el binario ni se conecta aun a
PostgreSQL o MongoDB.

## Trazabilidad con los otros componentes

- PostgreSQL: `scripts/postgresql_schema.sql`.
- MongoDB: `scripts/mongodb_collections.js`.
- Archivos y seguridad: `documentos/Almacenamiento_Archivos_SIGPRA.md`.
- Arquitectura de persistencia: `imagenes/arquitectura_persistencia.svg`.
