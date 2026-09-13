# Casos de uso - Gestor de Practicas

Este documento consolida los casos de uso definidos para SIGPRA, tomando como base el diagrama entregado y las fichas compartidas. El foco de esta version esta en el actor principal **Gestor de Practicas** y en los actores relacionados que completan el ciclo de practica academica.

## Alcance del diagrama

Actor principal:

- Gestor de Practicas

Actores relacionados:

- Estudiante
- Docente Asesor

Casos de uso principales:

- Planificar practica academica
- Gestionar asignacion de estudiantes
- Gestionar instituciones y convenios
- Gestionar informes e indicadores
- Gestionar evidencias y cumplimiento de horas
- Realizar seguimiento y evaluacion

Relaciones identificadas:

- Gestionar asignacion de estudiantes incluye Gestionar instituciones y convenios.
- Gestionar asignacion de estudiantes incluye Gestionar evidencias y cumplimiento de horas.
- Gestionar instituciones y convenios incluye Gestionar informes e indicadores.
- Gestionar informes e indicadores incluye Gestionar evidencias y cumplimiento de horas.
- Gestionar evidencias y cumplimiento de horas extiende Realizar seguimiento y evaluacion.

## Caso de uso 1: Planificar practica academica

| Campo | Descripcion |
| --- | --- |
| Actor | Gestor de Practicas |
| Proposito | Configurar los parametros que regulan las practicas academicas de un periodo lectivo. |
| Precondiciones | El periodo academico esta habilitado, el gestor tiene permisos administrativos y el sistema esta disponible. |
| Postcondiciones | La practica queda parametrizada y lista para la vinculacion de estudiantes. |

### Flujo principal

1. El Gestor accede al modulo de configuracion de practicas academicas.
2. Selecciona la licenciatura o programa y el nivel de practica.
3. Define la intensidad horaria minima reglamentaria.
4. Establece criterios de evaluacion y reglas de negocio.
5. Carga, selecciona o crea las rubricas de evaluacion.
6. El sistema valida coherencia y completitud de la configuracion.
7. El Gestor revisa la configuracion completa.
8. El sistema almacena y publica la configuracion del periodo.

### Flujos alternos

- El Gestor puede definir intensidades horarias especificas por institucion receptora.
- El Gestor puede configurar descuentos o bonificaciones de horas segun condiciones institucionales.
- El Gestor puede reutilizar plantillas de criterios de evaluacion existentes.
- El Gestor puede abrir un editor avanzado para reglas con multiples condiciones.

### Excepciones

- Si las horas son menores al minimo legal, el sistema bloquea el registro.
- Si las reglas de negocio son contradictorias, el sistema solicita correccion.
- Si faltan campos obligatorios, el sistema muestra los campos incompletos.
- Si no hay rubricas cargadas, el sistema no permite publicar la configuracion.

### Prototipo asociado

Pantalla: **Planificacion** en `prototipos/prototipos_casos_uso_gestor.html`.

## Caso de uso 2: Gestionar asignacion de estudiantes

| Campo | Descripcion |
| --- | --- |
| Actor | Gestor de Practicas |
| Proposito | Vincular estudiante, institucion receptora y docente asesor para iniciar la practica. |
| Precondiciones | El estudiante esta matriculado, existe al menos un convenio activo y la practica esta parametrizada. |
| Postcondiciones | El estudiante queda asignado, recibe credenciales y se habilitan evidencias, seguimiento y evaluacion. |

### Flujo principal

1. El Gestor ingresa al modulo de asignaciones.
2. Consulta estudiantes matriculados sin asignacion.
3. Selecciona un estudiante.
4. El sistema valida que no tenga asignacion vigente ni bloqueos criticos.
5. El Gestor consulta instituciones con convenio vigente y cupos disponibles.
6. Selecciona la institucion receptora.
7. El sistema valida cupos y convenio activo.
8. El Gestor selecciona un docente asesor disponible.
9. El sistema valida la carga maxima del docente.
10. El Gestor revisa el resumen de asignacion.
11. Confirma la asignacion.
12. El sistema genera credenciales y registra la asignacion.
13. El sistema actualiza el estado del estudiante a Asignado.
14. Se notifican docente e institucion.
15. Se registra trazabilidad en el historial del estudiante.

### Flujos alternos

- Busqueda dinamica por nombre, codigo o documento del estudiante.
- Reasignacion cuando el estudiante ya tiene una asignacion previa.
- Continuacion justificada si existen bloqueos academicos no criticos.
- Acceso directo al modulo de instituciones si falta un convenio.
- Vista comparativa de cupos por institucion.
- Panel lateral con disponibilidad y carga docente.
- Estado En espera si no hay docentes disponibles.

### Excepciones

- Si el estudiante ya esta asignado, se bloquea la nueva asignacion y se ofrece reasignar.
- Si la institucion no tiene cupos, se bloquea la asignacion.
- Si el docente alcanzo su carga maxima, se bloquea la asignacion.
- Si el convenio vence en menos de 30 dias, se emite advertencia.
- Si el convenio tiene requisitos especiales incumplidos, se rechaza la asignacion.
- Si falla el envio de notificaciones, la asignacion se completa y queda evento pendiente.

### Prototipo asociado

Pantalla: **Asignaciones** en `prototipos/prototipos_casos_uso_gestor.html`.

## Caso de uso 3: Gestionar instituciones y convenios

| Campo | Descripcion |
| --- | --- |
| Actor | Gestor de Practicas |
| Proposito | Administrar instituciones receptoras, convenios, cupos, vigencia y documentos legales. |
| Precondiciones | El Gestor cuenta con informacion legal de la institucion y permisos administrativos. |
| Postcondiciones | La institucion queda registrada, el convenio queda visible y los cupos quedan disponibles para asignaciones. |

### Flujo principal

1. El Gestor accede al modulo de instituciones y convenios.
2. Selecciona registrar una institucion nueva o modificar una existente.
3. El sistema muestra el formulario de datos institucionales.
4. El Gestor ingresa o actualiza razon social, identificacion, contacto, direccion y ciudad.
5. El Gestor ingresa fechas, cupos y condiciones del convenio.
6. Carga el PDF del convenio firmado.
7. El sistema valida campos, formato del archivo y coherencia de fechas.
8. El Gestor revisa y confirma.
9. El sistema almacena la informacion.
10. El sistema registra auditoria.
11. El sistema actualiza indicadores del modulo de reportes.

### Flujos alternos

- Vista de instituciones existentes con estado de convenio, cupos totales y cupos usados.
- Desactivacion logica de instituciones conservando historial.
- Consulta de historico de convenios anteriores.
- Configuracion avanzada de cupos por nivel de practica.
- Guardado como borrador antes de publicar.
- Carga de anexos adicionales del convenio.

### Excepciones

- Si el RUC o identificacion ya existe, se bloquea el registro y se ofrece ver el existente.
- Si la fecha de vencimiento es anterior a la fecha de inicio, se rechaza el convenio.
- Si el archivo no es PDF o supera 10 MB, se rechaza la carga.
- Si faltan campos obligatorios, el sistema los senala.
- Si el convenio vence en menos de 30 dias, se emite advertencia.
- Si se reducen cupos por debajo de estudiantes asignados, se bloquea o advierte la modificacion.

### Prototipo asociado

Pantalla: **Instituciones** en `prototipos/prototipos_casos_uso_gestor.html`.

## Caso de uso 4: Gestionar informes e indicadores

| Campo | Descripcion |
| --- | --- |
| Actor | Gestor de Practicas |
| Proposito | Consolidar informacion de horas, evidencias, evaluaciones y visitas para generar indicadores academicos e informes institucionales. |
| Precondiciones | Existen datos de ejecucion de al menos un periodo academico, el Gestor tiene permisos de reportes y el modulo de reportes esta disponible. |
| Postcondiciones | El informe queda generado, almacenado y disponible para consulta o exportacion en PDF, Excel, presentacion o HTML interactivo. |

### Flujo principal

1. El Gestor accede al modulo de reportes e indicadores.
2. Selecciona el tipo de informe: cobertura, desempeno docente, cumplimiento de horas, competencias o comparativo.
3. Define el periodo academico o rango de fechas.
4. Aplica filtros por institucion, nivel de practica, programa, docente o estado.
5. El sistema consulta evidencias, horas, evaluaciones, visitas y calificaciones.
6. El sistema calcula indicadores de cumplimiento, desempeno, aprobacion, asistencia y tendencias.
7. El sistema estructura graficos, tablas y estadisticas.
8. El sistema muestra un informe preliminar en pantalla.
9. El Gestor revisa consistencia y detalle de los calculos.
10. El Gestor selecciona formato de exportacion.
11. El sistema genera el archivo.
12. El archivo queda disponible para descarga.
13. El sistema registra historial del informe, parametros, filtros y usuario generador.

### Flujos alternos

- El Gestor puede usar plantillas de reportes predisenadas.
- El Gestor puede construir un informe personalizado desde cero.
- El Gestor puede aplicar filtros avanzados con operadores logicos y comparadores.
- El Gestor puede reutilizar filtros guardados de informes anteriores.
- El Gestor puede modificar tipos de graficos, escalas y secciones narrativas.
- El Gestor puede abrir el detalle de cualquier indicador para revisar los registros fuente.
- La exportacion PDF puede generarse como resumen ejecutivo, informe completo o seleccion personalizada.
- La exportacion Excel puede incluir hojas por resumen, metrica y datos crudos.

### Excepciones

- Si el periodo esta fuera del rango tipico de analisis, el sistema advierte antes de continuar.
- Si la base de datos esta en mantenimiento o lenta, el sistema informa tiempos de respuesta extendidos.
- Si existen datos inconsistentes, el reporte se genera con bandera de alerta y detalle del registro afectado.
- Si hay informacion incompleta, el reporte se marca como parcial.
- Si no hay datos suficientes, el sistema recomienda ampliar filtros o seleccionar otro periodo.
- Si el servidor de reportes esta saturado, el informe queda en cola.
- Si falla la descarga, el archivo queda disponible mediante enlace temporal.
- Si falla el registro de historial, se genera alerta tecnica sin impedir la descarga.

### Prototipo asociado

Pantalla: **Informes** en `prototipos/prototipos_casos_uso_gestor.html`.

## Caso de uso 5: Gestionar evidencias y cumplimientos de horas

| Campo | Descripcion |
| --- | --- |
| Actor | Estudiante |
| Proposito | Registrar horas de practica y cargar evidencias que respalden las actividades realizadas. |
| Precondiciones | El estudiante esta asignado a una institucion, tiene credenciales activas y la practica esta parametrizada en un periodo vigente. |
| Postcondiciones | Las horas se acumulan en el historial, las evidencias quedan almacenadas y disponibles para revision del docente asesor. |

### Flujo principal

1. El estudiante accede al sistema y selecciona Cargar evidencias.
2. Inicia una nueva sesion de carga.
3. Selecciona la actividad realizada desde un catalogo.
4. Ingresa la cantidad de horas trabajadas.
5. Describe la actividad realizada.
6. Selecciona los archivos de evidencia.
7. El sistema valida formatos permitidos: PDF, JPG, PNG y DOCX.
8. El sistema valida que cada archivo no supere 5 MB.
9. El sistema calcula el total de horas de la sesion.
10. El estudiante revisa horas, descripcion y archivos.
11. Confirma el envio.
12. El sistema valida que no se superen 8 horas en un dia.
13. El sistema registra la carga con fecha y hora.
14. El sistema actualiza el acumulado de horas.
15. El sistema notifica al docente asesor.

### Flujos alternos

- El estudiante puede seleccionar Otra actividad si no encuentra una opcion del catalogo.
- El estudiante puede hacer carga por lotes de multiples archivos.
- El estudiante puede usar camara del dispositivo para generar evidencia fotografica.
- El estudiante puede guardar como borrador antes de enviar al docente.
- El estudiante puede agregar notas visibles para el docente.

### Excepciones

- Si el archivo tiene formato no permitido, el sistema rechaza la carga.
- Si un archivo supera 5 MB, el sistema solicita reemplazarlo.
- Si se registran mas de 8 horas en un dia, el sistema bloquea el registro.
- Si el acumulado supera las horas reglamentarias, el sistema advierte y deja constancia.
- Si hay problemas de conectividad, el sistema conserva temporalmente los datos y permite reintentar.
- Si la notificacion al docente no puede entregarse, queda en cola de mensajes.

### Prototipo asociado

Pantalla: **Evidencias** en `prototipos/prototipos_casos_uso_gestor.html`.

## Caso de uso 6: Realizar seguimiento y evaluacion

| Campo | Descripcion |
| --- | --- |
| Actor | Docente Asesor |
| Proposito | Revisar evidencias, registrar visitas de acompanamiento, evaluar competencias y entregar retroalimentacion. |
| Precondiciones | El estudiante esta asignado al docente, existen evidencias o una visita planificada y estan disponibles las rubricas de evaluacion. |
| Postcondiciones | Las observaciones, calificaciones y retroalimentacion quedan consolidadas; el estudiante y el Gestor pueden consultar el avance. |

### Flujo principal

1. El docente accede al listado de estudiantes asignados.
2. Selecciona un estudiante y consulta su expediente.
3. Revisa evidencias cargadas y su coherencia con lo declarado.
4. Aprueba, rechaza o solicita aclaracion de evidencias.
5. Registra observacion y calificacion de seguimiento.
6. El sistema calcula promedio ponderado parcial.
7. El docente programa visita presencial o remota.
8. Durante la visita verifica desempeno, conducta profesional y cumplimiento.
9. Registra reporte de visita con fecha, institucion, horas y observaciones.
10. Carga o selecciona la rubrica correspondiente.
11. Califica cada criterio de la rubrica.
12. Registra comentarios por criterio.
13. El sistema consolida evidencia, visita y rubrica en una calificacion integral.
14. El docente envia retroalimentacion al estudiante.
15. El sistema actualiza el estado del estudiante.
16. El sistema notifica al estudiante.

### Flujos alternos

- El docente puede comparar el estudiante con el promedio de sus demas asignados.
- El expediente puede mostrar contactos de la institucion y jefe inmediato.
- Una evidencia dudosa puede marcarse como Requiere aclaracion.
- El docente puede consultar una linea de tiempo historica de evidencias.
- En visita remota puede adjuntar captura o grabacion como soporte.
- El docente puede usar rubrica personalizada o reutilizar una anterior.
- La retroalimentacion puede incluir documentos, videos o reunion de seguimiento.

### Excepciones

- Si no hay evidencias, el sistema sugiere visita o mensaje al estudiante.
- Si se rechaza una evidencia, el estudiante recibe solicitud de reemplazo.
- Si la institucion reporta conflicto de agenda, el sistema sugiere reprogramar.
- Si el estudiante no asistio, se genera alerta al coordinador.
- Si un criterio queda en nivel deficiente, el sistema recomienda plan de mejora.
- Si hay evidencias contradictorias, el docente debe registrar una observacion explicativa.
- Si falla la notificacion, queda disponible en mensajes internos.

### Prototipo asociado

Pantalla: **Seguimiento** en `prototipos/prototipos_casos_uso_gestor.html`.

## Trazabilidad hacia prototipo

| Caso de uso | Pantalla | Elementos visibles |
| --- | --- | --- |
| Planificar practica academica | Planificacion | Periodo, programa, nivel, horas, reglas, rubricas, validacion y publicacion. |
| Gestionar asignacion de estudiantes | Asignaciones | Filtros, lista de estudiantes, cupos por institucion, carga docente, resumen y confirmacion. |
| Gestionar instituciones y convenios | Instituciones | Registro legal, convenio, cupos, carga de PDF, anexos, estados y auditoria. |
| Gestionar informes e indicadores | Informes | Tipo de informe, filtros, indicadores, graficos, advertencias y exportacion. |
| Gestionar evidencias y cumplimientos de horas | Evidencias | Actividad, horas, descripcion, carga de archivos, validaciones y envio a docente. |
| Realizar seguimiento y evaluacion | Seguimiento | Expediente, evidencias, visita, rubrica, calificacion integral y retroalimentacion. |

## Observaciones para la entrega

- Las pantallas son prototipos de media fidelidad: muestran estructura, jerarquia visual, campos, validaciones y acciones esperadas, sin conectarse a base de datos.
- El flujo esta orientado al Gestor de Practicas como actor administrativo central.
- Los modulos de evidencias, informes y seguimiento quedan descritos como parte del ciclo completo de practica.
