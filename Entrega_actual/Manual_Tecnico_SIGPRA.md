# Manual Técnico - Sistema SIGPRA
## Software Integral de Gestión de Prácticas Académicas
**Arquitectura, Instalación, Operación y Mantenimiento**  
**Universidad de Investigación y Desarrollo (UDI) - Noviembre 2026**

---

## 1. Arquitectura del Sistema
SIGPRA implementa el patrón arquitectónico **Modelo-Vista-Controlador (MVC)** bajo una estrategia orientada a servicios desacoplados:
* **Frontend SPA:** Single Page Application basada en HTML5, Bootstrap 5 y JavaScript modular (ES6+), renderizada reactivamente en el cliente.
* **Backend MVC:** Node.js 20 LTS con framework Express 4.18 desarrollado en TypeScript 5.4, con validación estricta de esquemas y middleware de control de excepciones.
* **Persistencia Políglota:**
  - **PostgreSQL 16:** Motor relacional ACID en 3FN para entidades transaccionales (usuarios, periodos, convenios, plazas, asignaciones, solicitudes, evaluaciones).
  - **MongoDB 7:** Repositorio documental NoSQL para metadatos de evidencias, archivos adjuntos y bitácoras de auditoría.
  - **JSON Storage Engine:** Mecanismo de persistencia local autónomo para garantizar resiliencia y portabilidad inmediata.

---

## 2. Stack Tecnológico y Requisitos
* **Runtime:** Node.js 20 LTS / npm 10+
* **Lenguaje:** TypeScript 5.4.2
* **Bases de Datos:** PostgreSQL 16 y MongoDB 7.0
* **Contenedores:** Docker 26.0 & Docker Compose v2
* **PaaS Cloud:** Render Web Service (Ubuntu / Node environment)

---

## 3. Variables de Entorno (.env)
```env
PORT=3000
NODE_ENV=production
SIGPRA_STORAGE=json   # Opciones: 'json' o 'postgres'
DATABASE_URL=postgresql://sigpra_user:sigpra_pass@localhost:5432/sigpra_db
MONGO_URI=mongodb://localhost:27017/sigpra_mongo
```

---

## 4. Despliegue e Instalación

### 4.1 Despliegue en la Nube (Render PaaS)
Configuración declarativa en `render.yaml`:
* **URL:** `https://sigpra-udi.onrender.com`
* **Root Directory:** `Aplicacion_SIGPRA/Primer servicio`
* **Build Command:** `npm install && npm run build`
* **Start Command:** `npm start`

### 4.2 Despliegue Contenedorizado (Docker Compose)
Para levantar la suite completa en servidor o laboratorio:
```bash
docker-compose up -d --build
```
Servicios iniciados:
* `sigpra_app` en `http://localhost:3000`
* `sigpra_postgres` en `localhost:5432`
* `sigpra_mongodb` en `localhost:27017`

### 4.3 Ejecución Local Nativa
```bash
cd "Aplicacion_SIGPRA/Primer servicio"
npm install
npm run build
npm start
```

---

## 5. Catálogo de APIs REST
| Servicio | Método | Endpoint | Descripción |
|---|---|---|---|
| Autenticación | POST | `/api/auth/login` | Login y emisión de sesión |
| Autenticación | POST | `/api/auth/register` | Auto-registro de nuevo practicante |
| Solicitudes (CU-08) | GET | `/api/requests` | Consulta de solicitudes filtradas |
| Solicitudes (CU-08) | POST | `/api/requests` | Radicación de solicitud con soportes |
| Solicitudes (CU-08) | PUT | `/api/requests/:id/review` | Dictamen del Director (Aprobar/Devolver/Rechazar) |
| Periodos (CU-01) | GET / POST | `/api/periods` | Consulta y creación de periodos |
| Convenios (CU-02) | GET / POST | `/api/agreements` | Administración de convenios y plazas |
| Asignación (CU-03) | GET / POST | `/api/assignments` | Asignación de practicante habilitado |
| Bitácora (CU-04) | GET / POST | `/api/activities` | Registro de actividades y horas |
| Validación (CU-05) | PUT | `/api/activities/:id/validate` | Validación docente de horas |
| Visitas (CU-06) | GET / POST | `/api/visits` | Registro de visitas evaluativas |
| Consolidado (CU-07) | GET | `/api/reports/consolidated` | Reporte consolidado del periodo |
| Monitoreo | GET | `/api/health` | Estado del backend y servicios |

---

## 6. Procedimientos de Respaldo
* **PostgreSQL:** `pg_dump -U sigpra_user -h localhost -d sigpra_db -F c -b -v -f 'backup_pg.dump'`
* **MongoDB:** `mongodump --uri='mongodb://localhost:27017/sigpra_mongo' --out='./backup_mongo/'`