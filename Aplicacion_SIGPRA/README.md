# Aplicacion SIGPRA

Espacio principal de la aplicacion web SIGPRA para el Proyecto Integrador.

## Ejecucion rapida

Puedes ejecutar la aplicacion directamente desde esta carpeta:

```bash
npm install
npm run dev
```

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
