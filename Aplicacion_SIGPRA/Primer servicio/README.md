# Aplicacion SIGPRA

<<<<<<< HEAD
Espacio principal de la aplicacion web SIGPRA para el Proyecto Integrador.

## Ejecucion rapida

Puedes ejecutar la aplicacion directamente desde esta carpeta:
=======
Aplicacion web funcional tipo MVP para el producto final de SIGPRA.
Incluye frontend web y backend API REST con Node.js, Express, TypeScript y organizacion MVC orientada a los servicios del sistema (S1 a S4).

## Como abrir y ejecutar

### Opcion recomendada (con servidor API REST)

Desde la carpeta `Aplicacion_SIGPRA` o desde `Aplicacion_SIGPRA/Primer servicio`:
>>>>>>> origin/main

```bash
npm install
npm run dev
```

<<<<<<< HEAD
O ingresando a la carpeta de servicio:

```bash
cd "Primer servicio"
npm install
npm run dev
```

La aplicacion quedara disponible en: `http://localhost:3000`

## Estructura

- **`Primer servicio/`**: Contiene la implementacion funcional de la aplicacion (Frontend web, API REST MVC en Express/TypeScript, persistencia, adaptadores de PostgreSQL y Bootstrap).
  - **S1. Planeacion y convenios**: Periodos, instituciones, convenios y plazas (`/api/periods`, `/api/agreements`).
  - **S2. Asignacion de practicas**: Asignaciones y control de cupos (`/api/assignments`).
  - **S3. Ejecucion y seguimiento**: Registro de actividades/bitacora y visitas de acompanamiento (`/api/activities`, `/api/visits`).
  - **S4. Informes y auditoria**: Consolidado de practicas e historial (`/api/reports/consolidated`).
  - **Transversales**: Autenticacion, estado y salud (`/api/auth/login`, `/api/state`, `/api/health`).

Para mas detalles tecnicos y descripcion de los endpoints, consulta [`Primer servicio/README.md`](Primer%20servicio/README.md).
=======
Luego abrir en el navegador:

```text
http://localhost:3000
```

### Opcion local de respaldo

Abrir `index.html` en el navegador. En este modo la aplicacion funciona directamente con `localStorage`, sin requerir servidor activo.

## Bootstrap

La interfaz usa Bootstrap 5.3.8 en botones, formularios, tablas adaptables,
navegacion, insignias, alertas y barras de progreso. `styles.css` se carga despues
de Bootstrap para conservar los colores institucionales y la distribucion de SIGPRA.

Los archivos CSS y JavaScript (bundle con Popper), sus mapas y la licencia estan
en `vendor/bootstrap/`, de modo que tambien funcionan sin conexion al abrir
`index.html`. Express publica estos recursos en `/vendor/bootstrap`.

`npm install` y `npm run build` sincronizan los recursos desde la dependencia npm:

```bash
npm run assets:bootstrap
```

## Arquitectura de Servicios (Alineacion con la Propuesta S1 a S4)

De acuerdo con el documento formal de entrega (*Propuesta_SIGPRA_Segunda_Entrega*, Seccion 6.2), el sistema organiza su logica de negocio en cuatro servicios de dominio mas los servicios transversales:

| Servicio | Descripcion y Casos de Uso | Controladores y Servicios | APIs REST |
| --- | --- | --- | --- |
| **S1. Planeacion y convenios** | Configuracion de periodos academicos, administracion de instituciones, convenios y plazas (RF-01, RF-02, CU-01, CU-02). | `PeriodController`, `PeriodService`, `AgreementController`, `AgreementService` | `GET/POST /api/periods`<br>`GET/POST /api/agreements` |
| **S2. Asignacion de practicas** | Asignacion guiada de estudiantes a plazas y docentes con control de cupos (RF-03, CU-03). | `AssignmentController`, `AssignmentService` | `GET/POST /api/assignments` |
| **S3. Ejecucion y seguimiento** | Registro de actividades/bitacora, validacion de horas y visitas de acompanamiento con rubricas (RF-04, RF-05, CU-04, CU-05, CU-06). | `TrackingController`, `TrackingService` | `GET/POST/PUT /api/activities`<br>`GET/POST /api/visits` |
| **S4. Informes y auditoria** | Consolidado general de practicas, metricas, filtros e historial de auditoria (CU-07). | `ReportController`, `ReportService` | `GET /api/reports/consolidated` |
| **Transversal** | Autenticacion por roles, sincronizacion de estado global y verificacion del servidor. | `AuthController`, `AuthService`, `StateController`, `StateService`, `HealthController` | `POST /api/auth/login`<br>`GET/PUT /api/state`<br>`GET /api/health` |

## Estructura del Backend MVC

La capa backend se encuentra en `src/`:

| Carpeta | Responsabilidad |
| --- | --- |
| `src/controllers` | Recibe peticiones HTTP y responde JSON para cada servicio. |
| `src/services` | Ejecuta reglas de negocio de S1 (Periodos/Convenios), S2 (Asignaciones), S3 (Seguimiento/Visitas), S4 (Reportes) y Autenticacion. |
| `src/models` | Define tipos TypeScript (`types.ts`), datos demo (`seedData.ts`) y persistencia (`SigpraStore.ts`). |
| `src/routes` | Enrutamiento Express agrupado y documentado por servicio. |
| `src/postgres` | Adaptador y rutas para modo opcional con PostgreSQL y persistencia JSONB. |

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
- Navegacion agrupada por etapa y accesos directos a los modulos disponibles para cada rol.
- CU-01: crear periodos como borrador o publicarlos tras validar fechas, horas minimas y pesos de evaluacion.
- CU-02: registrar instituciones, convenios y plazas; convenios sin documento o fuera de vigencia quedan inactivos.
- CU-03: asignar estudiantes a plazas vigentes y docentes con disponibilidad mediante flujo guiado de cuatro pasos, con control de cupos y reasignacion.
- CU-04: registrar, guardar como borrador, corregir y reenviar actividades; valida fechas, horas y referencias de soportes.
- CU-05: aprobar, devolver o rechazar actividades; confirmacion de revision de soportes.
- CU-06: registrar visitas, asistencia y puntajes para los criterios de la rubrica del periodo.
- CU-07: consultar consolidado con filtros por periodo, programa, institucion y docente; exportar CSV o imprimir/guardar PDF.
- Persistencia local en archivo JSON del servidor (`data/sigpra-store.json`) con respaldo en `localStorage`.

## Alcance tecnico

Esta version funciona con un backend API REST en Node.js/Express/TypeScript y conserva modo local de respaldo para demostracion inmediata. Los datos se sincronizan contra la persistencia JSON local del servidor (`data/sigpra-store.json`) o contra PostgreSQL mediante `SIGPRA_STORAGE=postgres` en `.env`.
>>>>>>> origin/main
