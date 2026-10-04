# Aplicacion SIGPRA

Aplicacion web funcional tipo MVP para el producto final de SIGPRA.

## Como abrir

Abrir `index.html` en el navegador.

## Usuarios de prueba

| Rol | Correo | Clave |
| --- | --- | --- |
| Coordinador | coordinador@udi.edu.co | 123456 |
| Estudiante | estudiante@udi.edu.co | 123456 |
| Docente asesor | docente@udi.edu.co | 123456 |
| Director | director@udi.edu.co | 123456 |

## Funcionalidades incluidas

- Inicio de sesion por rol.
- CU-01: crear periodos como borrador o publicarlos tras validar fechas, horas minimas y pesos de evaluacion.
- CU-02: registrar instituciones, convenios y plazas; convenios sin documento o fuera de vigencia quedan inactivos.
- CU-03: asignar estudiantes a plazas vigentes y docentes con disponibilidad, con control de cupos y reasignacion con historial.
- CU-04: registrar, guardar como borrador, corregir y reenviar actividades; valida fechas, horas y referencias de soportes.
- CU-05: aprobar, devolver o rechazar actividades; la aprobacion exige confirmar la revision de soportes.
- CU-06: registrar visitas, asistencia y puntajes para todos los criterios de la rubrica del periodo.
- CU-07: consultar consolidado con filtros por periodo, programa, institucion y docente; exportar CSV o imprimir/guardar como PDF desde el navegador.
- Registro de evidencias como metadatos.
- Exportacion de datos en JSON.
- Persistencia local en navegador con `localStorage`.

## Alcance tecnico

Esta version funciona sin servidor para que pueda demostrarse de inmediato. Los datos y el nombre/tipo de los archivos se guardan localmente en el navegador; los archivos binarios y las notificaciones no se envian a terceros. El selector "Ver como" facilita la demostracion y no representa control de acceso de produccion. Para produccion, el siguiente paso es conectar la interfaz a una API REST con PostgreSQL, MongoDB y almacenamiento de archivos, usando los scripts incluidos en `Base_de_datos_SIGPRA`.
