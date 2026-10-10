# Aplicacion SIGPRA

Aplicacion web funcional tipo MVP para el producto final de SIGPRA.
Incluye frontend web y backend API REST con Node.js, Express, TypeScript y organizacion MVC.

## Como abrir

### Opcion recomendada con API

```bash
npm install
npm run dev
```

Luego abrir:

```text
http://localhost:3000
```

### Opcion local de respaldo

Abrir `index.html` en el navegador. En este modo la aplicacion funciona con `localStorage`, sin servidor.

## Backend MVC

La capa backend se encuentra en `src/`:

| Carpeta | Responsabilidad |
| --- | --- |
| `src/controllers` | Recibe peticiones HTTP y responde JSON. |
| `src/services` | Ejecuta reglas de negocio de autenticacion, periodos, convenios, asignaciones y reportes. |
| `src/models` | Define tipos TypeScript, datos demo y persistencia JSON local. |
| `src/routes` | Agrupa las rutas REST expuestas por Express. |

## APIs implementadas

| API | Metodo | Uso en la aplicacion |
| --- | --- | --- |
| `/api/health` | GET | Verifica conexion del frontend con Express. |
| `/api/state` | GET | Carga el estado inicial desde el backend. |
| `/api/state` | PUT | Sincroniza cambios realizados en la interfaz. |
| `/api/auth/login` | POST | Valida inicio de sesion por rol. |
| `/api/periods` | GET/POST | Expone periodos de practica. |
| `/api/agreements` | GET/POST | Expone instituciones, convenios y plazas. |
| `/api/assignments` | GET/POST | Expone asignaciones de practicantes. |
| `/api/reports/consolidated` | GET | Entrega consolidado de practicas. |

## Usuarios de prueba

| Rol | Correo | Clave |
| --- | --- | --- |
| Coordinador | coordinador@udi.edu.co | 123456 |
| Estudiante | estudiante@udi.edu.co | 123456 |
| Estudiante | sofia@udi.edu.co | 123456 |
| Estudiante | daniel@udi.edu.co | 123456 |
| Estudiante | valentina@udi.edu.co | 123456 |
| Docente asesor | docente@udi.edu.co | 123456 |
| Director | director@udi.edu.co | 123456 |

## Funcionalidades incluidas

- Inicio de sesion por rol.
- Navegacion agrupada por etapa y accesos directos a los modulos disponibles para cada rol, con resumen de tareas y registros.
- Formularios operativos organizados en etapas guiadas que se habilitan al completar la etapa anterior.
- CU-01: crear periodos como borrador o publicarlos tras validar fechas, horas minimas y pesos de evaluacion.
- CU-02: registrar instituciones, convenios y plazas; convenios sin documento o fuera de vigencia quedan inactivos.
- CU-03: asignar estudiantes a plazas vigentes y docentes con disponibilidad mediante un flujo guiado de cuatro pasos, con control de cupos, resumen previo y reasignacion con historial.
- Datos de demostracion: cinco estudiantes y cinco plazas disponibles para practicar el flujo de asignacion.
- CU-04: registrar, guardar como borrador, corregir y reenviar actividades; valida fechas, horas y referencias de soportes.
- CU-05: aprobar, devolver o rechazar actividades; la aprobacion exige confirmar la revision de soportes.
- CU-06: registrar visitas, asistencia y puntajes para todos los criterios de la rubrica del periodo.
- CU-07: consultar consolidado con filtros por periodo, programa, institucion y docente; exportar CSV o imprimir/guardar como PDF desde el navegador.
- Registro de evidencias como metadatos.
- Exportacion de datos en JSON.
- Consumo de APIs Express para carga, login, periodos, convenios, asignaciones y sincronizacion.
- Persistencia local en navegador con `localStorage` como respaldo.

## Alcance tecnico

Esta version funciona con un backend API REST en Node.js/Express/TypeScript y tambien conserva modo local de respaldo para demostracion inmediata. Los datos se sincronizan contra una persistencia JSON local del servidor; los archivos binarios y las notificaciones no se envian a terceros. El selector "Ver como" facilita la demostracion y no representa control de acceso de produccion. Para produccion, el siguiente paso es reemplazar la persistencia JSON por PostgreSQL, MongoDB y almacenamiento de archivos, usando los scripts incluidos en `Base_de_datos_SIGPRA`.
