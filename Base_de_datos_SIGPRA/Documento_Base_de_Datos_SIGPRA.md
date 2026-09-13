# Documento de base de datos - SIGPRA

## 1. Proposito

Este documento define la propuesta de persistencia para SIGPRA, separada de la propuesta general del proyecto. La base de datos se organiza en tres componentes para responder a necesidades distintas:

- Integridad transaccional.
- Flexibilidad documental.
- Conservacion de archivos de soporte.

## 2. Componentes de persistencia

| Componente | Tecnologia propuesta | Uso principal |
| --- | --- | --- |
| Base relacional | PostgreSQL | Usuarios, roles, periodos, convenios, plazas, asignaciones, actividades, horas y evaluaciones. |
| Base documental | MongoDB | Metadatos variables de evidencias, bitacoras de visita y eventos de auditoria. |
| Almacenamiento de archivos | Sistema de archivos o servicio tipo bucket | PDF, imagenes, documentos firmados, planeaciones y anexos. |

## 3. Criterio de separacion

PostgreSQL se utiliza cuando la informacion requiere llaves primarias, llaves foraneas, consistencia referencial y consultas estructuradas.

MongoDB se utiliza cuando el contenido puede cambiar entre casos, por ejemplo detalles de auditoria, observaciones extendidas o metadatos de archivos que no tienen siempre la misma estructura.

El almacenamiento de archivos conserva el binario real. La base relacional y la base documental solo guardan referencias, metadatos, hash y ubicacion del archivo.

## 4. Base relacional PostgreSQL

### 4.1 Tablas principales

| Tabla | Funcion |
| --- | --- |
| `usuario` | Autenticacion, correo, rol y estado general. |
| `estudiante` | Datos academicos del practicante. |
| `docente_asesor` | Datos del docente que acompana y evalua. |
| `coordinador_practicas` | Coordinador responsable del proceso. |
| `institucion` | Instituciones receptoras. |
| `convenio` | Convenios suscritos con instituciones. |
| `plaza_practica` | Cupos ofrecidos por institucion y convenio. |
| `periodo_practica` | Configuracion del periodo academico de practica. |
| `rubrica` | Instrumento de evaluacion asociado al periodo. |
| `criterio_rubrica` | Criterios y pesos de la rubrica. |
| `asignacion` | Vinculo entre estudiante, plaza, docente asesor y periodo. |
| `registro_actividad` | Actividades y horas reportadas por el estudiante. |
| `evidencia` | Referencias relacionales a soportes cargados. |
| `visita_seguimiento` | Visitas de acompanamiento del docente asesor. |
| `evaluacion` | Evaluacion general de una asignacion. |
| `detalle_evaluacion` | Puntaje por criterio de rubrica. |

### 4.2 Reglas principales

- Un usuario puede especializarse como estudiante, docente asesor o coordinador.
- Una institucion puede tener varios convenios.
- Un convenio puede tener varias plazas.
- Una asignacion vincula estudiante, plaza, docente asesor y periodo.
- Una asignacion puede tener muchos registros de actividad.
- Una actividad puede tener varias evidencias.
- Una asignacion puede tener varias visitas.
- Una visita puede generar una evaluacion.
- Una evaluacion se detalla por criterios de rubrica.

## 5. Base documental MongoDB

### 5.1 Colecciones propuestas

| Coleccion | Funcion |
| --- | --- |
| `evidencias_documentales` | Metadatos flexibles de soportes cargados. |
| `bitacoras_visita` | Observaciones extendidas y anexos de visitas. |
| `auditoria_eventos` | Trazabilidad de acciones relevantes del sistema. |

### 5.2 Relacion con PostgreSQL

MongoDB no reemplaza el modelo relacional. Cada documento conserva referencias hacia identificadores de PostgreSQL, por ejemplo:

- `actividad_id`
- `asignacion_id`
- `visita_id`
- `usuario_id`

## 6. Almacenamiento de archivos

Los archivos no se guardan directamente dentro de PostgreSQL ni MongoDB. Se conservan en almacenamiento de archivos y se referencian desde las bases de datos.

Ejemplo:

```text
storage/sigpra/2026-2/asignacion-15/actividad-80/evidencia-planeacion.pdf
```

## 7. Flujo de carga de evidencia

1. El estudiante registra una actividad en PostgreSQL.
2. El sistema recibe uno o varios archivos.
3. El archivo se guarda en almacenamiento.
4. Se calcula hash del archivo.
5. Se crea el registro `evidencia` en PostgreSQL.
6. Se crea el documento `evidencias_documentales` en MongoDB.
7. Se registra un evento en `auditoria_eventos`.

## 8. Diagramas

- Arquitectura de persistencia: `imagenes/arquitectura_persistencia.svg`
- Modelo relacional resumen: `imagenes/modelo_relacional_resumen.svg`

## 9. Scripts

- PostgreSQL: `scripts/postgresql_schema.sql`
- MongoDB: `scripts/mongodb_collections.js`

## 10. Recomendacion de implementacion

Para el proyecto academico se recomienda iniciar con PostgreSQL y el almacenamiento de archivos. MongoDB se puede integrar despues para auditoria y metadatos flexibles, manteniendo desde el inicio los campos de referencia necesarios.

