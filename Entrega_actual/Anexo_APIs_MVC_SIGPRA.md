# Anexo tecnico: aplicacion web y APIs SIGPRA

## Implementacion actual

La aplicacion web esta en `../Aplicacion_SIGPRA/`. El servidor usa Node.js,
Express y TypeScript. El codigo del servidor se organiza en rutas,
controladores, servicios y modelos bajo `src/` (arquitectura MVC). Express
entrega la interfaz y las rutas REST bajo `/api`.

La interfaz consulta estas tres APIs de dominio al iniciar y usa sus
respuestas para mostrar los datos:

| API | Metodo | Datos que usa la interfaz |
| --- | --- | --- |
| `/api/periods` | GET | Periodos academicos y rubricas. |
| `/api/agreements` | GET | Instituciones, convenios y plazas. |
| `/api/assignments` | GET | Asignaciones de practicantes. |

Tambien usa `/api/health` para comprobar la conexion, `/api/state` para
cargar y sincronizar los cambios, y `/api/auth/login` para validar el acceso
de las cuentas de demostracion. El servidor expone ademas
`/api/reports/consolidated` para consultar el consolidado calculado.

## Como ejecutar y verificar

Desde `Aplicacion_SIGPRA`:

```powershell
npm.cmd install
npm.cmd run dev
```

Abrir `http://localhost:3000` y consultar estas rutas en el navegador o con
`Invoke-RestMethod`:

```powershell
Invoke-RestMethod http://localhost:3000/api/periods
Invoke-RestMethod http://localhost:3000/api/agreements
Invoke-RestMethod http://localhost:3000/api/assignments
Invoke-RestMethod http://localhost:3000/api/reports/consolidated
```

Para comprobar las peticiones de la interfaz, abrir las herramientas de
desarrollo del navegador, pestaña Red, y recargar la pagina. Las tres
consultas de dominio deben responder con HTTP 200.

## Alcance de la persistencia

El servidor guarda el estado del prototipo en `data/sigpra-store.json`. El
navegador conserva una copia local de respaldo. El esquema PostgreSQL y las
colecciones MongoDB de `../Base_de_datos_SIGPRA/` son modelos y scripts de
base de datos; todavia no son la persistencia operativa de esta aplicacion.
Por ello, las pruebas de CRUD de PostgreSQL deben demostrarse por separado.
Las cuentas y contraseñas incluidas son solo para la demostracion.

## Relacion con los casos de uso

- CU-01 consulta periodos por `/api/periods`.
- CU-02 consulta instituciones, convenios y plazas por `/api/agreements`.
- CU-03 consulta asignaciones por `/api/assignments`.
- CU-07 dispone del consolidado en `/api/reports/consolidated`.

La interfaz sincroniza los cambios de sus formularios mediante `PUT
/api/state`. Las rutas de dominio tambien exponen `POST` para crear registros,
pero los formularios actuales no las usan de forma individual.
