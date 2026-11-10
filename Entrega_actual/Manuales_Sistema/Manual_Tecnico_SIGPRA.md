**UNIVERSIDAD DE INVESTIGACIÓN Y DESARROLLO – UDI**

Facultad de Ingenierías

Programa de Ingeniería de Sistemas

**MANUAL TÉCNICO**

SIGPRA – Software Integral de Gestión de Prácticas Académicas

*Arquitectura, instalación, configuración y operación – versión 1.0*

Proyecto Integrador V – Quinto semestre

Ingeniería del Software II · Programación III · Teoría General de Sistemas

Jesús Daniel Rueda Castillo

Alonso Andrés Henao Roa

Rafael Fabián Carreño Barrera

Bucaramanga, noviembre de 2026

**Contenido**

# 1. Propósito y alcance

Este manual describe cómo está construida la versión entregada de SIGPRA y cómo se instala, se configura, se despliega y se mantiene. Está dirigido a quien tenga que ejecutar la aplicación, revisar su código o continuar su desarrollo.

Todo lo que aparece aquí corresponde a la versión del repositorio https://github.com/rcarreno4-dot/Proyecto-integrador-5, rama main. Cuando el diseño del proyecto y la implementación no coinciden, el manual lo dice de forma explícita; el resumen de esas diferencias está en el capítulo 10.

# 2. Arquitectura de la versión entregada

El diseño de SIGPRA es orientado a servicios: cuatro servicios (S1 planeación y convenios, S2 asignación de prácticas, S3 ejecución y seguimiento, S4 informes y auditoría) que se comunican por REST con mensajes JSON. En la versión entregada esos servicios están implementados como **módulos de un mismo servidor**, cada uno con su grupo de rutas bajo /api. La Figura 1 muestra la arquitectura que realmente se ejecuta.

**Figura 1.** *Arquitectura de la versión entregada.*

## 2.1 Capa de presentación

Es una aplicación de una sola página (SPA) escrita en HTML5, JavaScript y Bootstrap 5.3.8. Está en tres archivos de la carpeta Aplicacion_SIGPRA/Primer servicio: index.html, app.js (unas 4.700 líneas, con todas las vistas por rol) y styles.css. El servidor entrega estos archivos sin caché para que el navegador siempre cargue la última versión.

La interfaz carga el estado con GET /api/state y guarda los cambios enviando el estado completo con PUT /api/state. Si el servidor no responde, entra en **Modo local** y trabaja con una copia guardada en el localStorage del navegador. Las reglas de negocio de los formularios (fechas, horas, cupos, pesos de la rúbrica, justificaciones) se validan en el navegador.

## 2.2 Capa de negocio

Es un servidor Node.js con Express 4 escrito en TypeScript 5.4 y organizado con el patrón MVC. El punto de entrada es src/server.ts, que crea la aplicación definida en src/app.ts. Esta registra CORS, el lector de JSON (límite de 2 MB), el enrutador de la API y los archivos estáticos. Según la variable SIGPRA_STORAGE, la API usa el enrutador del modo JSON (src/routes/index.ts) o el del modo PostgreSQL (src/postgres/routes.ts).

**Tabla 1.** *Componentes del servidor por servicio del diseño*

| **Servicio**               | **Controladores**                                 | **Servicios**                                                      |
|----------------------------|---------------------------------------------------|--------------------------------------------------------------------|
| S1 Planeación y convenios  | PeriodController, AgreementController             | PeriodService, AgreementService                                    |
| S2 Asignación de prácticas | AssignmentController, RequestController           | AssignmentService (RequestController usa SigpraStore directamente) |
| S3 Ejecución y seguimiento | TrackingController                                | TrackingService                                                    |
| S4 Informes y auditoría    | ReportController                                  | ReportService                                                      |
| Transversal                | AuthController, StateController, HealthController | AuthService, StateService                                          |

Todos los servicios leen y escriben por medio de la clase SigpraStore (src/models/SigpraStore.ts), que mantiene el estado en memoria y lo guarda en el archivo JSON. Los tipos del modelo están en src/models/types.ts y los datos de demostración en src/models/seedData.ts.

## 2.3 Capa de persistencia

La aplicación tiene dos modos de almacenamiento, que se eligen con SIGPRA_STORAGE:

- **Modo json (por defecto):** el estado completo se guarda en data/sigpra-store.json. El archivo se busca primero en la carpeta desde la que se ejecuta el servidor y luego junto a la carpeta compilada. Si no existe, se parte de los datos de demostración.

- **Modo postgres:** el mismo estado se guarda en una sola fila de la tabla sigpra.estado_mvp, en una columna JSONB. Cada cambio se hace dentro de una transacción con bloqueo de fila. Al arrancar, el servidor crea el esquema y la tabla si no existen.

El esquema relacional de 16 tablas y las tres colecciones de MongoDB (capítulo 7) están diseñados, montados y probados, pero la aplicación todavía no lee ni escribe en ellos tabla por tabla.

## 2.4 Diagramas de clases

La Figura 2 muestra el modelo de datos que maneja el servidor y la Figura 3 la organización de controladores, servicios y almacenamiento. Las fuentes editables de ambos diagramas están en Entrega_actual/Diagramas UML/Clases y modelos editables (archivos .mmd, formato Mermaid).

**Figura 2.** *Diagrama de clases: modelo de datos (src/models/types.ts).*

**Figura 3.** *Diagrama de clases: controladores, servicios y SigpraStore.*

# 3. Estructura del repositorio

**Tabla 2.** *Carpetas principales del repositorio*

| **Carpeta o archivo**                                                                    | **Contenido**                                                                                                                                        |
|------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------|
| Aplicacion_SIGPRA/Primer servicio/                                                       | Aplicación entregada: servidor (src/), interfaz (index.html, app.js, styles.css), recursos (assets/, vendor/bootstrap/), datos (data/) y Dockerfile. |
| Base_de_datos_SIGPRA/                                                                    | Scripts de PostgreSQL y MongoDB, pruebas CRUD, diccionario de datos e imágenes de los modelos.                                                       |
| sigpra-frontend-react/                                                                   | Segunda interfaz en React 18, TypeScript y Vite que consume la misma API. Solo se ejecuta en local.                                                  |
| docker-compose.yml                                                                       | Entorno local con la aplicación, PostgreSQL 16 y MongoDB 7.                                                                                          |
| render.yaml                                                                              | Configuración del servicio publicado en Render.                                                                                                      |
| Entrega_actual/                                                                          | Entregables académicos: documento final, diagramas UML, manuales, artículo, póster, actas y capturas.                                                |
| Aplicacion_SIGPRA/s1-…, s2-…, s3-…, s4-…, gateway/, microservices/; Aplicacion completa/ | Versiones iniciales de servicios separados y un prototipo con contenedores. No hacen parte de la versión entregada.                                  |
| documentos_alternos/                                                                     | Respaldos de entregas anteriores y de un prototipo de escritorio en Java y Oracle.                                                                   |

## 3.1 Estructura de la aplicación entregada

> Aplicacion_SIGPRA/Primer servicio/
>
> ├── src/
>
> │ ├── server.ts punto de entrada
>
> │ ├── app.ts configuración de Express
>
> │ ├── routes/index.ts rutas del modo json
>
> │ ├── postgres/ rutas y estado del modo postgres
>
> │ ├── controllers/ 9 controladores
>
> │ ├── services/ 7 servicios
>
> │ └── models/ types.ts, seedData.ts, SigpraStore.ts
>
> ├── index.html, app.js, styles.css interfaz (SPA)
>
> ├── assets/ logo e ilustraciones
>
> ├── vendor/bootstrap/ Bootstrap 5.3.8 local
>
> ├── scripts/copy-bootstrap.cjs
>
> ├── data/sigpra-store.json estado en modo json
>
> ├── .env.example
>
> ├── Dockerfile
>
> └── package.json

# 4. Tecnologías y dependencias

**Tabla 3.** *Tecnologías de la versión entregada*

| **Tecnología** | **Versión**   | **Uso**                                                                |
|----------------|---------------|------------------------------------------------------------------------|
| Node.js        | 20            | Entorno de ejecución del servidor (imagen node:20-alpine en Docker).   |
| Express        | 4.18          | Servidor HTTP y enrutamiento de la API.                                |
| TypeScript     | 5.4           | Lenguaje del servidor; se compila a dist/ con tsc.                     |
| pg             | 8.13          | Cliente de PostgreSQL para el modo postgres.                           |
| cors, dotenv   | 2.8 / 16.4    | CORS y lectura del archivo .env.                                       |
| tsx            | 4.7           | Ejecución en desarrollo con recarga automática.                        |
| Bootstrap      | 5.3.8         | Estilos y componentes de la interfaz, servidos desde vendor/bootstrap. |
| PostgreSQL     | 16            | Esquema relacional y modo de almacenamiento postgres.                  |
| MongoDB        | 7             | Colecciones documentales (montadas y probadas aparte).                 |
| Docker Compose | —             | Entorno local con la aplicación y las dos bases de datos.              |
| Render         | plan gratuito | Publicación de la aplicación con HTTPS.                                |

## 4.1 Scripts de npm

**Tabla 4.** *Scripts definidos en package.json*

| **Comando**   | **Qué hace**                                                                                    |
|---------------|-------------------------------------------------------------------------------------------------|
| npm install   | Instala dependencias. Al terminar, ejecuta postinstall, que copia Bootstrap a vendor/bootstrap. |
| npm run dev   | Ejecuta src/server.ts con tsx y recarga al guardar cambios.                                     |
| npm run build | Copia Bootstrap y compila TypeScript a dist/.                                                   |
| npm start     | Ejecuta la versión compilada (node dist/server.js).                                             |

# 5. Configuración

La configuración se hace con variables de entorno. En local se pueden poner en un archivo .env dentro de Aplicacion_SIGPRA/Primer servicio (hay un ejemplo en .env.example).

**Tabla 5.** *Variables de entorno*

| **Variable**          | **Valor por defecto** | **Descripción**                                                                                                                                                                             |
|-----------------------|-----------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| PORT                  | 3000                  | Puerto del servidor HTTP.                                                                                                                                                                   |
| SIGPRA_STORAGE        | json                  | Modo de almacenamiento: json o postgres.                                                                                                                                                    |
| DATABASE_URL          | —                     | Cadena de conexión a PostgreSQL. Obligatoria en modo postgres. Ejemplo: postgresql://postgres:CLAVE@localhost:5432/sigpra.                                                                  |
| SIGPRA_PG_IMPORT_JSON | sin definir           | En modo postgres, si existe data/sigpra-store.json y la tabla todavía está vacía, el servidor se niega a arrancar para no reemplazar datos. Con 1, importa ese archivo como estado inicial. |
| NODE_ENV              | —                     | Render y Docker la ponen en production. La aplicación no cambia su comportamiento por esta variable.                                                                                        |

|                                                                                                                          |
|--------------------------------------------------------------------------------------------------------------------------|
| **Nota:** la aplicación no usa ninguna variable de conexión a MongoDB, porque todavía no se conecta a esa base de datos. |

# 6. Instalación y despliegue

## 6.1 Ejecución local con Node.js

Requisitos: Node.js 20 y npm.

> git clone https://github.com/rcarreno4-dot/Proyecto-integrador-5.git
>
> cd "Proyecto-integrador-5/Aplicacion_SIGPRA/Primer servicio"
>
> npm install
>
> npm run build
>
> npm start \# o, en desarrollo: npm run dev

La aplicación queda en http://localhost:3000 en modo json. Para usar PostgreSQL, cree la base de datos, defina SIGPRA_STORAGE=postgres y DATABASE_URL en el archivo .env y arranque de nuevo. Al conectarse, el servidor muestra el mensaje "SIGPRA: PostgreSQL conectado".

## 6.2 Entorno local con Docker Compose

Desde la raíz del repositorio:

> docker compose up -d --build

**Tabla 6.** *Contenedores definidos en docker-compose.yml*

| **Contenedor**  | **Imagen**                                      | **Puerto** | **Detalles**                                                                                                   |
|-----------------|-------------------------------------------------|------------|----------------------------------------------------------------------------------------------------------------|
| sigpra_app      | Construida con el Dockerfile de Primer servicio | 3000       | Modo json por defecto. Monta Primer servicio/data en /app/data.                                                |
| sigpra_postgres | postgres:16-alpine                              | 5432       | Base sigpra, usuario postgres. Carga postgresql_schema.sql al crearse el volumen. Tiene verificación de salud. |
| sigpra_mongodb  | mongo:7.0                                       | 27017      | Base sigpra. Carga mongodb_collections.js al crearse el volumen.                                               |

|                                                                                                                                                                                                                                                                                                                                                                               |
|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Nota:** para que el contenedor de la aplicación use PostgreSQL, cambie SIGPRA_STORAGE a postgres en docker-compose.yml. Como la carpeta data ya contiene sigpra-store.json, agregue también SIGPRA_PG_IMPORT_JSON=1 la primera vez, o el servidor no arrancará. La contraseña de PostgreSQL del archivo es solo para desarrollo y debe cambiarse en cualquier otro entorno. |

## 6.3 Despliegue en Render

El servicio publicado se define en render.yaml, en la raíz del repositorio:

**Tabla 7.** *Configuración del servicio en Render*

| **Parámetro** | **Valor**                                    |
|---------------|----------------------------------------------|
| Nombre        | sigpra-udi (https://sigpra-udi.onrender.com) |
| Tipo y plan   | Servicio web Node, plan gratuito             |
| Carpeta raíz  | Aplicacion_SIGPRA/Primer servicio            |
| Compilación   | npm install && npm run build                 |
| Inicio        | npm start                                    |
| Variables     | NODE_ENV=production, SIGPRA_STORAGE=json     |

Cada cambio en la rama conectada vuelve a desplegar el servicio. En el plan gratuito el disco es efímero: los cambios hechos en la aplicación publicada se pierden cuando el servicio se reinicia o se vuelve a desplegar, y el estado vuelve a ser el de data/sigpra-store.json del repositorio. Además, el servicio se suspende cuando no se usa por un tiempo y tarda cerca de un minuto en responder la primera solicitud.

**Figura 4.** *Despliegue en Render y en el entorno local con Docker Compose. El contenedor de MongoDB se levanta, pero la aplicación todavía no se conecta a él.*

## 6.4 Interfaz en React (opcional)

La carpeta sigpra-frontend-react tiene una segunda interfaz en React 18, TypeScript y Vite. Con el servidor corriendo en el puerto 3000, se ejecuta así (en Windows también con iniciar_react.bat):

> cd sigpra-frontend-react
>
> npm install
>
> npm run dev \# abre http://localhost:5173

Vite redirige las solicitudes de /api al puerto 3000. Esta interfaz no está publicada en Render.

# 7. Modelo de datos

## 7.1 Estado de la aplicación

El estado que maneja la aplicación (Figura 2) es un objeto JSON con estas colecciones: programs, users, periods, institutions, agreements, places, assignments, activities, evidences, visits, requests y audit. Las relaciones se hacen por identificadores numéricos (institutionId, agreementId, placeId, assignmentId, studentId, teacherId, periodId). La rúbrica de cada periodo va dentro del periodo, como una lista de criterios con nombre y peso.

## 7.2 Esquema relacional en PostgreSQL

El script Base_de_datos_SIGPRA/scripts/postgresql_schema.sql crea el esquema sigpra con 16 tablas: usuario, estudiante, docente_asesor, coordinador_practicas, institucion, convenio, plaza_practica, periodo_practica, rubrica, criterio_rubrica, asignacion, registro_actividad, evidencia, visita_seguimiento, evaluacion y detalle_evaluacion. El diccionario completo está en Base_de_datos_SIGPRA/Diccionario_Datos_SIGPRA.md y en el documento final del proyecto.

Varias reglas de negocio quedaron como restricciones de la base de datos:

- Los cupos ocupados de una plaza no pueden superar los ofrecidos.

- Las horas de una actividad deben ser mayores que cero y no pasar de 24.

- La nota de cada criterio va de 0 a 5.

- La fecha límite de reporte no puede ser anterior al fin del periodo.

- Un índice único parcial impide que un estudiante tenga dos asignaciones activas en el mismo periodo.

El esquema tiene además otros seis índices de consulta y la vista v_estado_practicas, que reúne el estado de cada práctica para los informes. El script migracion_2026_10_reglas_asignacion_periodo.sql aplica las reglas de octubre de 2026 sobre una base ya creada.

## 7.3 Colecciones en MongoDB

El script mongodb_collections.js crea tres colecciones con validación de esquema (\$jsonSchema) e índices: evidencias_documentales, bitacoras_visita y auditoria_eventos. Los campos que apuntan a registros de PostgreSQL se guardan como identificadores, sin verificación automática.

## 7.4 Pruebas CRUD

El script pruebas_crud_basicas.sql crea, consulta, actualiza y borra un registro en las tablas institucion, usuario, convenio y plaza_practica, todo dentro de una transacción, y al final verifica que no queden datos de prueba. En Windows se puede ejecutar con ejecutar_pruebas_crud_postgresql.ps1, que guarda la salida como evidencia. La ejecución del 11 de octubre de 2026 terminó sin errores y con los cuatro conteos finales en cero (archivo Evidencia_CRUD_PostgreSQL_SIGPRA.txt).

# 8. API REST

Todas las rutas están bajo /api, reciben y devuelven JSON y usan códigos HTTP estándar: 200 y 201 para operaciones correctas, 400 para datos inválidos, 401 para credenciales incorrectas, 404 para registros no encontrados y 500 para errores internos. La columna "Modo" indica si la ruta existe en el modo json, en el modo postgres o en ambos.

**Tabla 8.** *Rutas de la API*

| **Método y ruta**             | **Descripción**                                                                                                          | **Modo** |
|-------------------------------|--------------------------------------------------------------------------------------------------------------------------|----------|
| GET /api/health               | Estado del servidor y lista de servicios.                                                                                | Ambos    |
| GET /api/state                | Devuelve el estado completo.                                                                                             | Ambos    |
| PUT /api/state                | Reemplaza el estado completo (lo usa la interfaz para guardar).                                                          | Ambos    |
| POST /api/auth/login          | Recibe email y password; devuelve el usuario sin la contraseña o 401.                                                    | Ambos    |
| POST /api/auth/register       | Recibe name, email, password, code y semester; crea un estudiante.                                                       | json     |
| GET / POST /api/periods       | Lista o crea periodos de práctica (S1).                                                                                  | Ambos    |
| GET / POST /api/agreements    | Lista o crea convenios con sus instituciones y plazas (S1).                                                              | Ambos    |
| GET /api/requests             | Lista solicitudes; filtros opcionales studentId y status (S2).                                                           | json     |
| GET /api/requests/:id         | Devuelve una solicitud o 404 (S2).                                                                                       | json     |
| POST /api/requests            | Crea o actualiza una solicitud con sus documentos (S2).                                                                  | json     |
| PUT /api/requests/:id/review  | Dictamen del director: status (APROBADA, DEVUELTA o RECHAZADA) y justification, obligatoria al devolver o rechazar (S2). | json     |
| GET / POST /api/assignments   | Lista o crea asignaciones (S2).                                                                                          | Ambos    |
| GET /api/activities           | Lista actividades; filtro opcional assignmentId (S3).                                                                    | Ambos    |
| POST /api/activities          | Registra una actividad (S3).                                                                                             | Ambos    |
| PUT /api/activities/:id       | Cambia el estado de una actividad con status y observation (S3).                                                         | Ambos    |
| GET / POST /api/visits        | Lista o registra visitas; filtro opcional assignmentId (S3).                                                             | Ambos    |
| GET /api/reports/consolidated | Consolidado por asignación: estudiante, docente, institución, horas aprobadas, actividades y visitas (S4).               | Ambos    |

En total son 21 rutas en modo json y 16 en modo postgres. La interfaz entregada lee con las rutas GET de cada servicio, pero guarda los cambios con PUT /api/state; las rutas POST y PUT por recurso existen y se pueden probar con un cliente HTTP, pero los formularios todavía no las usan.

### Ejemplo

> curl -X POST http://localhost:3000/api/auth/login \\
>
> -H "Content-Type: application/json" \\
>
> -d '{"email":"coordinador@udi.edu.co","password":"123456"}'

# 9. Seguridad, roles y auditoría

## 9.1 Roles

**Tabla 9.** *Roles y permisos en la interfaz*

| **Rol (valor en datos)** | **Opciones del menú**                                                                                                |
|--------------------------|----------------------------------------------------------------------------------------------------------------------|
| ESTUDIANTE               | Inicio, Solicitar práctica (mientras no tenga práctica asignada), Mi avance, Mi bitácora, Evidencias, Validaciones   |
| DIRECTOR                 | Inicio, Solicitudes de práctica (con dictamen), Consolidado, Auditoría                                               |
| COORDINADOR              | Inicio, Periodos de práctica, Convenios y plazas, Asignaciones, Solicitudes de práctica (solo consulta), Consolidado |
| DOCENTE_ASESOR           | Inicio, Validar actividades, Visitas y evaluación                                                                    |

El rol se toma de la cuenta al iniciar sesión; el usuario no lo elige. Las cuentas creadas con el registro siempre son de estudiante.

## 9.2 Auditoría

La interfaz agrega un evento a la colección audit del estado cada vez que se hace una acción importante: crear o publicar un periodo, cerrarlo, registrar un convenio, suspenderlo o reactivarlo, asignar o reasignar, radicar o revisar una solicitud, validar una actividad o registrar una visita. Cada evento guarda la fecha, el usuario, la descripción y la entidad. Solo el director ve el registro, y la interfaz no ofrece ninguna opción para editarlo o borrarlo.

## 9.3 Limitaciones de seguridad conocidas

La versión entregada es un prototipo académico y tiene limitaciones que deben resolverse antes de usarla con datos reales:

- Las rutas de la API no tienen autenticación ni comprueban el rol de quien las llama. Los permisos por rol se aplican solo en la interfaz.

- GET /api/state devuelve el estado completo, incluidas las contraseñas de las cuentas, y PUT /api/state permite reemplazarlo sin restricción.

- Las contraseñas se guardan en texto plano. Debe aplicarse una función de hash.

- Los datos de la versión publicada son públicos para quien conozca la dirección de la API, así que no deben cargarse datos personales reales.

- CORS está abierto para cualquier origen.

# 10. Diferencias entre el diseño y la implementación

**Tabla 10.** *Diferencias conocidas y trabajo futuro*

| **Diseño**                                                               | **Versión entregada**                                                              |
|--------------------------------------------------------------------------|------------------------------------------------------------------------------------|
| Cuatro servicios desplegados por separado detrás de una capa intermedia. | Cuatro módulos en un solo servidor.                                                |
| Persistencia en el esquema relacional y en MongoDB.                      | Estado en un archivo JSON o en una tabla JSONB.                                    |
| Validación de reglas y roles en el servidor.                             | Validación en el navegador; la API no verifica roles.                              |
| Soportes guardados en un almacenamiento de archivos.                     | Se guardan el nombre y la ruta prevista, sin subir el archivo.                     |
| Credenciales institucionales y contraseñas con hash.                     | Cuentas de demostración y registro con contraseñas en texto plano.                 |
| Cierre formal de periodos académicos (RN-11 / RF-01).                    | Verificado e implementado: el sistema bloquea el cierre si existen actividades pendientes de revisión y deshabilita nuevos registros una vez cerrado. |
| Solo se asignan estudiantes habilitados por el director (RN-01).         | La lista marca a los no habilitados, pero no impide asignarlos.                    |
| Notificaciones a los participantes.                                      | Avisos dentro de la aplicación y eventos de auditoría; no se envían correos.       |

# 11. Respaldo y mantenimiento

## 11.1 Modo json

Basta con copiar el archivo data/sigpra-store.json con el servidor detenido. Para restaurar, se reemplaza el archivo y se arranca de nuevo.

## 11.2 PostgreSQL (entorno Docker Compose)

> \# Respaldo
>
> docker exec sigpra_postgres pg_dump -U postgres -d sigpra -F c -f /tmp/sigpra.dump
>
> docker cp sigpra_postgres:/tmp/sigpra.dump ./sigpra.dump
>
> \# Restauración
>
> docker cp ./sigpra.dump sigpra_postgres:/tmp/sigpra.dump
>
> docker exec sigpra_postgres pg_restore -U postgres -d sigpra --clean /tmp/sigpra.dump

## 11.3 MongoDB (entorno Docker Compose)

> docker exec sigpra_mongodb mongodump --db sigpra --out /tmp/respaldo
>
> docker cp sigpra_mongodb:/tmp/respaldo ./respaldo_mongo
>
> docker cp ./respaldo_mongo sigpra_mongodb:/tmp/respaldo
>
> docker exec sigpra_mongodb mongorestore --db sigpra /tmp/respaldo/sigpra

## 11.4 Restablecer los datos de demostración

En modo json, detenga el servidor y borre o renombre data/sigpra-store.json; al arrancar, la aplicación parte de seedData.ts. En modo postgres, borre la fila de sigpra.estado_mvp. Tenga en cuenta que la interfaz también guarda una copia en el localStorage del navegador, que conviene limpiar.

# 12. Solución de problemas

### El servidor no arranca en modo postgres y dice "DATABASE_URL no configurada".

Defina DATABASE_URL en el archivo .env o en el entorno.

### El servidor dice que existe data/sigpra-store.json y pide SIGPRA_PG_IMPORT_JSON=1.

Es una protección para no reemplazar datos. Defina SIGPRA_PG_IMPORT_JSON=1 para importar el archivo, o retírelo si quiere partir de los datos de demostración.

### La interfaz muestra "Modo local".

La interfaz no se pudo comunicar con la API. Verifique que el servidor esté corriendo y responda en /api/health.

### La interfaz se ve sin estilos.

Falta la copia local de Bootstrap. Ejecute npm install o npm run assets:bootstrap.

### La versión publicada perdió los cambios.

Es el comportamiento del disco efímero del plan gratuito de Render (sección 6.3).

### El puerto 3000 está ocupado.

Cambie PORT en el archivo .env o detenga el otro proceso.
