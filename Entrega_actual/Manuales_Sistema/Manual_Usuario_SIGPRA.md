**UNIVERSIDAD DE INVESTIGACIÓN Y DESARROLLO – UDI**

Facultad de Ingenierías

Programa de Ingeniería de Sistemas

**MANUAL DE USUARIO**

SIGPRA – Software Integral de Gestión de Prácticas Académicas

*Guía de uso por rol – versión 1.0*

Proyecto Integrador V – Quinto semestre

Ingeniería del Software II · Programación III · Teoría General de Sistemas

Jesús Daniel Rueda Castillo

Alonso Andrés Henao Roa

Rafael Fabián Carreño Barrera

Bucaramanga, noviembre de 2026

**Contenido**

# 1. Presentación

SIGPRA es la aplicación web con la que el programa gestiona las prácticas pedagógicas, desde que se programa el periodo hasta que se consulta el consolidado final. Este manual explica, paso a paso, lo que puede hacer cada persona dentro de la aplicación según su rol.

La aplicación tiene cuatro roles. Cada uno ve solo las opciones que le corresponden:

**Tabla 1.** *Roles de SIGPRA y lo que hace cada uno*

| **Rol**                  | **Qué hace en SIGPRA**                                                                                                                      |
|--------------------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| Estudiante practicante   | Solicita la práctica con sus documentos, registra sus actividades con soportes y consulta su avance de horas.                               |
| Director de programa     | Revisa las solicitudes de práctica y decide si las aprueba, las devuelve o las rechaza. Consulta el consolidado y el registro de auditoría. |
| Coordinador de prácticas | Administra programas, periodos, instituciones, convenios y plazas, asigna a los practicantes y consulta el consolidado.                     |
| Docente asesor           | Valida las actividades de sus practicantes y registra las visitas de acompañamiento con su evaluación.                                      |

Si usted solo necesita conocer su parte, vaya directamente al capítulo de su rol. Los capítulos 2 y 3 son comunes para todos.

# 2. Acceso a la aplicación

## 2.1 Dónde se abre

SIGPRA funciona desde el navegador, sin instalar nada. Puede usarse en computador, tableta o celular.

- Versión publicada en la nube: **https://sigpra-udi.onrender.com**

- Versión local, cuando se ejecuta en un equipo del laboratorio: **http://localhost:3000**

- Navegadores recomendados: Google Chrome, Microsoft Edge o Mozilla Firefox en una versión reciente.

|                                                                                                                                                                                                        |
|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Nota:** la versión publicada usa un plan gratuito que se suspende cuando nadie la usa por un rato. Si la página tarda en cargar la primera vez, espere alrededor de un minuto y vuelva a intentarlo. |

## 2.2 Cómo está organizada la pantalla

Después de iniciar sesión, todas las pantallas tienen la misma estructura:

- **Menú lateral (izquierda):** las opciones de su rol, agrupadas por etapa. La opción en la que usted está aparece resaltada en azul.

- **Encabezado (arriba):** el nombre de la pantalla, su nombre y el botón **Cerrar sesión**.

- **Contenido (centro):** el formulario de la tarea, organizado en etapas numeradas, y debajo la tabla con los registros.

- **Indicador de conexión (abajo a la izquierda):** "API Express conectada" significa que los cambios se están guardando en el servidor. Si aparece "Modo local", la aplicación no pudo comunicarse con el servidor y los cambios quedan solo en su navegador.

La pantalla de **Inicio** de cada rol muestra tarjetas con los números más importantes (por ejemplo, solicitudes por revisar o actividades pendientes). Al hacer clic en una tarjeta, la aplicación lo lleva a la pantalla donde se atiende esa tarea.

# 3. Inicio de sesión y registro

## 3.1 Iniciar sesión

1.  Abra la aplicación.

2.  Escriba su **correo institucional** y su **clave**.

3.  Haga clic en **Entrar**. La aplicación abre la pantalla de inicio de su rol.

**Figura 1.** *Pantalla de inicio de sesión.*

## 3.2 Cuentas de demostración

Para pruebas y sustentaciones, la aplicación trae cuentas de demostración. Están en la sección **Cuentas de prueba (desplegar para ingresar)**, debajo del formulario; al desplegarla se puede entrar con un clic. Todas usan la clave 123456.

**Tabla 2.** *Cuentas de demostración*

| **Rol**                          | **Correo**                                                | **Para qué sirve**                               |
|----------------------------------|-----------------------------------------------------------|--------------------------------------------------|
| Coordinador de prácticas         | coordinador@udi.edu.co                                    | Periodos, convenios, asignaciones y consolidado  |
| Director de programa             | director@udi.edu.co                                       | Revisión de solicitudes, consolidado y auditoría |
| Docente asesor                   | docente@udi.edu.co                                        | Validación de actividades y visitas              |
| Estudiante con práctica asignada | estudiante@udi.edu.co                                     | Bitácora, evidencias y avance de horas           |
| Estudiantes sin asignación       | sofia@udi.edu.co, daniel@udi.edu.co, valentina@udi.edu.co | Solicitud de práctica en distintos estados       |

## 3.3 Registro de un estudiante nuevo

Un estudiante que todavía no tiene cuenta puede crearla desde la pantalla de inicio. Las cuentas creadas de esta forma siempre quedan con el rol de estudiante.

1.  En la pantalla de inicio de sesión, haga clic en **Registrar nueva cuenta de estudiante**.

2.  Complete los campos obligatorios: nombre y apellidos, correo institucional UDI, código estudiantil, semestre actual (de 7.° a 10.°), contraseña y confirmación de la contraseña.

3.  La contraseña debe tener al menos seis caracteres y las dos contraseñas deben coincidir.

4.  Haga clic en **Crear Cuenta y Comenzar Solicitud**. La aplicación crea la cuenta e inicia la sesión.

**Figura 2.** *Formulario de registro de estudiante.*

# 4. Estudiante practicante

El menú del estudiante tiene estas opciones: **Inicio**, **Solicitar práctica**, **Mi avance**, **Mi bitacora**, **Evidencias** y **Validaciones**. La opción **Solicitar práctica** desaparece del menú cuando el estudiante ya tiene una práctica asignada.

## 4.1 Solicitar la práctica

La solicitud es el primer paso: sin ella aprobada por el director, el estudiante no queda habilitado para que el coordinador le asigne una plaza. En la parte superior de la pantalla se ve la ruta del trámite en cuatro etapas: radicación, revisión de la dirección, asignación y ejecución.

La pantalla tiene dos pestañas: **Mi Estado de Radicación**, que muestra cómo va la solicitud, y **Diligenciar / Modificar Solicitud**, que contiene el formulario.

1.  Entre a **Solicitar práctica** y abra la pestaña **Diligenciar / Modificar Solicitud**.

2.  En **Periodo y Nivel Académico**, elija el periodo publicado y el nivel de práctica que va a cursar.

3.  En **Modalidad de Solicitud y Plaza de Interés**, elija la modalidad: convenio institucional (plazas UDI), práctica autogestionada o validación por experiencia laboral. Si quiere, sugiera una plaza.

4.  En **Carga de Documentos de Soporte y Prerrequisitos**, adjunte los tres documentos: certificado de matrícula, afiliación a EPS y ARL, y paz y salvo académico. Se aceptan archivos PDF, PNG o JPG.

5.  Si lo necesita, escriba comentarios para el director.

6.  Haga clic en **Guardar como borrador** si quiere terminar después, o en **Radicar Solicitud Formal** para enviarla.

**Figura 3.** *Formulario de solicitud de práctica.*

Al radicar, la aplicación verifica que estén los tres documentos. Si falta alguno, no deja radicar y le indica cuál falta. Cuando la radicación es correcta, la solicitud pasa a **Pendiente de revisión** y aparece un comprobante con el número de radicado (por ejemplo, RAD-2026-0003), la fecha y la hora y la lista de soportes. El comprobante se puede imprimir.

|                                                                                                                      |
|----------------------------------------------------------------------------------------------------------------------|
| **Nota:** un borrador se puede guardar sin los documentos. La exigencia de los tres soportes aplica solo al radicar. |

## 4.2 Corregir una solicitud devuelta

Si el director devuelve la solicitud, la pestaña **Mi Estado de Radicación** muestra el estado **DEVUELTA** y el motivo que escribió el director.

1.  Lea el motivo de la devolución.

2.  Haga clic en **Ir al formulario para corregir**.

3.  Reemplace el documento que le pidieron corregir. Los demás documentos se conservan.

4.  Haga clic en **Radicar Solicitud Formal** para enviarla de nuevo.

**Figura 4.** *Solicitud devuelta con el motivo del director.*

## 4.3 Consultar el avance

En **Mi avance** (y también en **Inicio**) el estudiante ve las horas aprobadas frente a la intensidad horaria exigida por el periodo, el porcentaje de avance, las horas que están pendientes de validación y las que le faltan. También ve los datos de su práctica activa: institución, docente asesor y periodo. Solo las horas que aprueba el docente asesor cuentan para el avance.

**Figura 5.** *Avance de horas del estudiante.*

## 4.4 Registrar una actividad en la bitácora

Cada jornada de práctica se registra como una actividad con sus soportes.

1.  Entre a **Mi bitacora**.

2.  En **Datos de la actividad**, escriba la fecha, las horas ejecutadas, el tipo de actividad (la aplicación sugiere planeación de clase, refuerzo pedagógico o reunión con el docente titular, pero puede escribir otro) y la descripción.

3.  En **Soportes y envío**, adjunte uno o varios archivos.

4.  Haga clic en **Guardar como borrador** para terminar después, o en **Enviar a validacion** para mandarla al docente asesor.

**Tabla 3.** *Reglas para registrar una actividad*

| **Dato** | **Regla**                                                                                                                 |
|----------|---------------------------------------------------------------------------------------------------------------------------|
| Fecha    | Debe estar dentro de las fechas del periodo y no puede ser posterior a la fecha límite de reporte.                        |
| Horas    | Entre 0,5 y 24 horas por actividad.                                                                                       |
| Soportes | Para enviar a validación se necesita al menos un soporte. Formatos: PDF, JPG, PNG, DOC o DOCX, de hasta 10 MB cada uno.   |
| Edición  | Solo se pueden modificar las actividades en borrador o devueltas. Las pendientes, aprobadas o rechazadas ya no se editan. |

**Figura 6.** *Bitácora de actividades del estudiante.*

Cuando el docente devuelve una actividad, en la tabla de **Actividades registradas** aparece la opción para corregirla y reenviarla. La actividad vuelve con todo lo que ya se había cargado y con la observación del docente.

## 4.5 Agregar evidencias

En **Evidencias** se puede agregar un soporte a una actividad que ya existe: elija la actividad, adjunte el archivo y haga clic en **Registrar evidencia**. Aplican los mismos formatos y el mismo límite de 10 MB. La tabla inferior muestra cada archivo con su actividad y su estado.

## 4.6 Ver las validaciones

En **Validaciones** el estudiante ve el resultado que dio el docente a cada actividad (pendiente, aprobada, devuelta o rechazada) y la observación que escribió.

# 5. Director de programa

El menú del director tiene **Inicio**, **Solicitudes de práctica**, **Consolidado** y **Auditoria**. En **Inicio** aparecen las solicitudes que esperan dictamen, los estudiantes habilitados y los que ya están en práctica.

**Figura 7.** *Pantalla de inicio del director.*

## 5.1 Revisar las solicitudes de práctica

La bandeja de **Solicitudes de práctica** muestra todas las solicitudes con el estudiante, el periodo, la modalidad, los soportes, la fecha de radicación y el estado. Arriba hay contadores y botones para filtrar por estado: pendientes, aprobadas, devueltas y rechazadas. Cada soporte se puede abrir desde la misma tabla.

**Figura 8.** *Bandeja de solicitudes del director.*

1.  En la fila de la solicitud, haga clic en **Evaluar Solicitud**.

2.  Revise los datos del estudiante y abra cada soporte con **Ver Soporte**.

3.  Escriba su observación en el campo **Observación / Justificación de la Dirección de Programa**.

4.  Elija la decisión: **Aprobar y Habilitar**, **Devolver para corrección** o **Rechazar**.

**Figura 9.** *Ventana de evaluación de una solicitud.*

**Tabla 4.** *Decisiones sobre una solicitud*

| **Decisión**             | **Qué pasa**                                                                                          | **Justificación**                                           |
|--------------------------|-------------------------------------------------------------------------------------------------------|-------------------------------------------------------------|
| Aprobar y Habilitar      | La solicitud queda aprobada y el estudiante queda habilitado para que el coordinador le asigne plaza. | Opcional. Si la escribe, debe tener al menos 10 caracteres. |
| Devolver para corrección | El estudiante ve el motivo y puede corregir los soportes y volver a radicar.                          | Obligatoria, de al menos 10 caracteres.                     |
| Rechazar                 | La solicitud se cierra y el estudiante no queda habilitado.                                           | Obligatoria, de al menos 10 caracteres.                     |

|                                                                                                                              |
|------------------------------------------------------------------------------------------------------------------------------|
| **Nota:** solo el director puede aprobar, devolver o rechazar. El coordinador ve la misma bandeja, pero en modo de consulta. |

## 5.2 Consolidado

El director consulta el consolidado igual que el coordinador. El procedimiento está en la sección 6.6.

## 5.3 Registro de auditoría

La opción **Auditoria** es exclusiva del director. Muestra, en orden, las acciones importantes registradas en la aplicación: fecha, usuario, evento y entidad afectada (por ejemplo, la radicación o la aprobación de una solicitud, la creación o el cierre de un periodo, o la suspensión de un convenio). Es una consulta: los registros no se pueden editar ni borrar desde la aplicación.

**Figura 10.** *Registro de auditoría.*

# 6. Coordinador de prácticas

El menú del coordinador tiene **Inicio**, **Periodos de practica**, **Convenios y plazas**, **Asignaciones**, **Solicitudes de práctica** y **Consolidado**.

**Figura 11.** *Pantalla de inicio del coordinador.*

## 6.1 Programas académicos

En la parte superior de **Periodos de practica** está la **Gestión de programas académicos**. Allí se registra cada programa con sus niveles de práctica, separados por coma, y su estado (activo o inactivo). Un programa que ya tiene periodos no se puede eliminar, y a sus niveles en uso no se les puede cambiar el nombre.

## 6.2 Programar un periodo de práctica

El formulario **Programar periodo de practica** se llena en tres etapas:

1.  **Programa y configuración previa:** elija el programa académico y el nivel de práctica, e indique el año y el semestre.

2.  **Fechas e intensidad horaria:** escriba la fecha de inicio, la fecha de fin, la fecha límite de reporte y la intensidad horaria mínima.

3.  **Criterios de evaluación y publicación:** escriba el nombre y el peso de cada uno de los tres criterios de la rúbrica.

4.  Haga clic en **Guardar como borrador** o en **Publicar periodo**.

**Tabla 5.** *Validaciones al guardar o publicar un periodo*

| **Regla**                                                                                 | **Cuándo aplica**                                  |
|-------------------------------------------------------------------------------------------|----------------------------------------------------|
| La fecha de inicio no puede ser anterior a hoy y debe ser anterior a la fecha de fin.     | Borrador y publicación (al crear un periodo nuevo) |
| La fecha límite de reporte no puede ser anterior a la fecha de fin.                       | Borrador y publicación                             |
| No puede existir otro periodo para el mismo programa, año y semestre.                     | Borrador y publicación                             |
| La intensidad horaria debe estar entre 1 y 320 horas.                                     | Borrador y publicación                             |
| La intensidad no puede ser menor que la de los periodos ya publicados del mismo programa. | Solo publicación                                   |
| Los pesos de los criterios deben sumar exactamente 100 %.                                 | Solo publicación                                   |

**Figura 12.** *Programas académicos y programación del periodo.*

En la tabla **Periodos registrados**, un borrador muestra el botón **Continuar borrador** para terminarlo. Un periodo publicado ya no se edita desde el formulario.

### Cerrar un periodo

Un periodo publicado muestra el botón **Cerrar periodo**. Al hacer clic, la aplicación pide confirmar. Después del cierre, el periodo queda en estado **CERRADO**, sigue visible en la tabla y deja de ofrecerse para nuevas solicitudes de práctica y nuevas asignaciones. El cierre queda en el registro de auditoría.

## 6.3 Convenios y plazas

Para registrar una institución nueva, haga clic en **Nueva institucion** y escriba nombre, NIT, contacto, teléfono y correo. Luego registre el convenio y su primera plaza en tres etapas:

1.  **Institución receptora:** elija la institución.

2.  **Convenio:** escriba el número del convenio, las fechas de vigencia (desde y hasta) y adjunte el documento firmado.

3.  **Plaza ofrecida:** elija el programa afín, escriba el nivel o área formativa, elija la jornada (mañana, tarde, noche o mixta) e indique los cupos y el docente titular.

4.  Haga clic en **Guardar convenio y plaza**.

La aplicación no permite guardar si la fecha de inicio es posterior a la de vencimiento o si ya existe un convenio con el mismo número.

**Tabla 6.** *Estados de un convenio*

| **Estado** | **Significado**                                         | **Sus plazas se ofrecen** |
|------------|---------------------------------------------------------|---------------------------|
| VIGENTE    | Tiene documento y su fecha de vencimiento no ha pasado. | Sí, mientras tengan cupos |
| VENCIDO    | Su fecha de vencimiento ya pasó.                        | No                        |
| INACTIVA   | No tiene documento adjunto.                             | No                        |
| SUSPENDIDO | El coordinador lo suspendió con el botón Suspender.     | No, hasta que se reactive |

En la tabla **Convenios y plazas registrados**, cada convenio vigente tiene el botón **Suspender** y cada convenio suspendido tiene el botón **Reactivar**. Las prácticas que ya estaban asignadas no cambian al suspender un convenio. Si algún convenio vence en los próximos 60 días, aparece un aviso encima de la tabla con los días que le faltan, para que pueda tramitar la renovación a tiempo.

**Figura 13.** *Convenios y plazas con el aviso de vencimiento.*

## 6.4 Asignar un practicante

La asignación se hace en cuatro pasos:

1.  **Estudiante practicante:** elija el periodo y el estudiante. La lista indica si cada estudiante está habilitado (su solicitud fue aprobada) o pendiente de habilitación.

2.  **Plaza de práctica:** elija una plaza. Solo aparecen plazas con cupo disponible y convenio vigente.

3.  **Docente asesor:** elija el docente. La lista muestra cuántos estudiantes tiene cada uno; el máximo es seis.

4.  **Resumen de la asignación:** revise el resumen y haga clic en **Confirmar asignacion**. Con **Limpiar seleccion** se borra todo sin guardar.

**Figura 14.** *Asignación de un practicante.*

Al confirmar, la aplicación descuenta un cupo de la plaza y deja la asignación activa. Si el estudiante ya tenía una asignación activa en el periodo, la aplicación lo avisa y pide el **motivo de la reasignación**; al confirmar, la asignación anterior queda cancelada, se libera su cupo y se conserva el historial.

|                                                                                                                                                               |
|---------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Importante:** asigne solo estudiantes marcados como habilitados. La aplicación todavía no bloquea la asignación de un estudiante pendiente de habilitación. |

## 6.5 Solicitudes de práctica (consulta)

El coordinador ve la misma bandeja de solicitudes que el director, con sus filtros y sus soportes, pero solo puede consultarla. La decisión le corresponde al director.

## 6.6 Consolidado

1.  Entre a **Consolidado**.

2.  Elija los filtros que necesite: periodo académico, programa, institución receptora y docente asesor. Si los deja en "Todos", la consulta es general.

3.  Haga clic en **Buscar / Generar Reporte** (o en **Ver Consolidado General de Prácticas** para verlo sin filtros).

4.  Revise los indicadores (prácticas, horas aprobadas, actividades validadas y visitas realizadas), el detalle de cada práctica con su barra de avance y el consolidado por institución.

5.  Si lo necesita, haga clic en **Exportar a Excel (CSV)** o en **Imprimir / Guardar PDF Oficial**.

**Figura 15.** *Consolidado con filtros, indicadores y opciones de exportación.*

Si en el momento de la consulta hay actividades sin validar, la aplicación lo advierte con el mensaje **Consolidado parcial** y dice cuántas son, para que el dato no se tome como definitivo. Solo se cuentan las horas aprobadas por los docentes asesores.

# 7. Docente asesor

El menú del docente asesor tiene **Inicio**, **Validar actividades** y **Visitas y evaluacion**. En **Inicio** aparecen sus estudiantes, las actividades que esperan validación y las visitas registradas.

## 7.1 Validar actividades

1.  Entre a **Validar actividades**. La tabla muestra las actividades pendientes de sus estudiantes, con fecha, estudiante, actividad y horas.

2.  Haga clic en **Revisar** en la actividad que quiere validar.

3.  Revise la descripción y los soportes adjuntos y marque **Confirmo que revise los soportes disponibles para esta actividad**.

4.  En **Resultado**, elija **Aprobar**, **Devolver** o **Rechazar** y escriba la observación.

5.  Haga clic en **Guardar validacion**.

**Figura 16.** *Revisión de una actividad.*

**Tabla 7.** *Resultados de la validación*

| **Resultado** | **Qué pasa**                                                                           | **Observación** |
|---------------|----------------------------------------------------------------------------------------|-----------------|
| Aprobar       | Las horas se suman al avance del estudiante y los soportes quedan validados.           | Opcional        |
| Devolver      | La actividad vuelve al estudiante, con sus soportes, para que la corrija y la reenvíe. | Obligatoria     |
| Rechazar      | La actividad se cierra sin sumar horas. Sus datos se conservan.                        | Obligatoria     |

Para aprobar es necesario marcar la confirmación de revisión y que la actividad tenga al menos un soporte. Solo se pueden validar actividades pendientes de sus propios estudiantes.

## 7.2 Registrar una visita y evaluar

1.  **Seleccionar practicante:** elija el estudiante. La aplicación muestra la institución y la información de su práctica.

2.  **Completar datos de la visita:** escriba la fecha, elija la modalidad (presencial, remota o mixta), escriba las observaciones e indique si el estudiante asistió. Si la visita es remota, adjunte el soporte (PDF, imagen, DOC o DOCX de hasta 10 MB).

3.  **Evaluar con la rúbrica:** escriba la nota de cada criterio del periodo, de 0,0 a 5,0 con un decimal, y si quiere una observación por criterio.

4.  Haga clic en **Guardar visita y evaluacion**.

**Figura 17.** *Evaluación con la rúbrica del periodo.*

La fecha de la visita debe estar dentro del periodo. La aplicación calcula la nota final con los pesos de la rúbrica, la redondea a un decimal y asigna el concepto:

**Tabla 8.** *Concepto según la nota final*

| **Nota final** | **Concepto** |
|----------------|--------------|
| 4,5 a 5,0      | Excelente    |
| 3,8 a 4,4      | Bueno        |
| 3,0 a 3,7      | Aceptable    |
| Menos de 3,0   | Insuficiente |

Si el estudiante no asistió, la inasistencia queda en el registro de auditoría para coordinar una nueva visita.

# 8. Estados que maneja la aplicación

**Tabla 9.** *Estados por tipo de registro*

| **Registro**             | **Estados posibles**                                               |
|--------------------------|--------------------------------------------------------------------|
| Solicitud de práctica    | Borrador · Pendiente de revisión · Aprobada · Devuelta · Rechazada |
| Periodo de práctica      | Borrador · Publicado · Cerrado                                     |
| Convenio                 | Vigente · Vencido · Inactiva (sin documento) · Suspendido          |
| Plaza                    | Disponible · Inactiva (sin cupos o con convenio no vigente)        |
| Asignación               | Activa · Cancelada (cuando se reasigna)                            |
| Actividad de la bitácora | Borrador · Pendiente · Aprobada · Devuelta · Rechazada             |
| Evidencia                | Cargada · Validada                                                 |

# 9. Preguntas frecuentes

### No me deja radicar la solicitud de práctica.

Revise que haya adjuntado los tres documentos (matrícula, EPS/ARL y paz y salvo) y que exista un periodo publicado. Si le falta un documento, puede guardar la solicitud como borrador y radicarla después.

### Ya no veo la opción "Solicitar práctica" en el menú.

Es normal: la opción desaparece cuando usted ya tiene una práctica asignada. Desde ese momento trabaja con Mi bitacora y Mi avance.

### La aplicación no acepta la fecha de mi actividad.

La fecha debe estar dentro del periodo y no puede ser posterior a la fecha límite de reporte. El mensaje de error muestra las fechas permitidas.

### Registré horas pero mi avance no cambió.

Solo cuentan las horas que aprueba el docente asesor. Mientras la actividad esté pendiente, aparece como horas por validar.

### Como docente, no puedo aprobar una actividad.

Marque la casilla de confirmación de revisión de soportes y verifique que la actividad tenga al menos un soporte.

### Como director, no me deja devolver o rechazar.

La justificación es obligatoria y debe tener al menos 10 caracteres.

### Una plaza no aparece para asignar.

Solo aparecen plazas con cupo disponible y con un convenio vigente. Revise en Convenios y plazas si el convenio venció, está suspendido o no tiene documento.

### El consolidado dice "parcial".

Hay actividades sin validar en ese momento. El aviso dice cuántas son; cuando los docentes las validen, el consolidado será definitivo.

### Aparece "Modo local" abajo a la izquierda.

La aplicación no se pudo comunicar con el servidor. Recargue la página; si sigue igual, espere un minuto (el servidor gratuito puede estar arrancando) antes de seguir trabajando.
