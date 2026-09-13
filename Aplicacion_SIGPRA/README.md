# Aplicacion SIGPRA

Aplicacion web funcional tipo MVP para el producto final de SIGPRA.

## Como abrir

Abrir `index.html` en el navegador.

## Usuarios de prueba

| Rol | Correo | Clave |
| --- | --- | --- |
| Coordinador | coordinador@sigpra.edu.co | 123456 |
| Estudiante | estudiante@sigpra.edu.co | 123456 |
| Docente asesor | docente@sigpra.edu.co | 123456 |
| Director | director@sigpra.edu.co | 123456 |

## Funcionalidades incluidas

- Inicio de sesion por rol.
- Panel de coordinador.
- Programacion de periodos.
- Gestion de instituciones, convenios y plazas.
- Asignacion de practicantes a plaza y docente asesor.
- Registro de actividades por estudiante.
- Registro de evidencias como metadatos.
- Validacion de actividades por docente asesor.
- Registro de visitas y evaluacion.
- Consulta consolidada para direccion/coordinacion.
- Exportacion de datos en JSON.
- Persistencia local en navegador con `localStorage`.

## Alcance tecnico

Esta version funciona sin servidor para que pueda demostrarse de inmediato. Para produccion, el siguiente paso es conectar la interfaz a una API REST con PostgreSQL, MongoDB y almacenamiento de archivos, usando los scripts incluidos en `Base_de_datos_SIGPRA`.
