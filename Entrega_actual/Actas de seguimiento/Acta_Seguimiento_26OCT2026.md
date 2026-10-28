# SIGPRA — Informe Semanal de Seguimiento
**Proyecto Integrador — UDI — II — 2026**

---

| **Semana Nº** | 44 (Sesión 1 - Desarrollo Fullstack y Cloud) | **Fechas** | Lunes 26 de octubre de 2026 |
|---|---|---|---|
| **Elaborado por** | Alonso Henao, Jesús Rueda, Rafael Carreño | | |

---

## Lo que se hizo esta jornada

| # | Actividad | Responsable | Estado |
|---|---|---|---|
| **1** | **Implementación del Caso de Uso CU-08 (Solicitud y Registro de Practicante):** Construcción del formulario web para que el estudiante formalice su postulación, seleccione la modalidad y cargue los 3 soportes obligatorios en PDF (matrícula, EPS/ARL vigente y paz y salvo) conectados al endpoint `/api/requests`. | Rafael Carreño | Completado |
| **2** | **Desarrollo del panel de evaluación exclusiva para Director de Programa:** Implementación de la vista y lógica para emitir dictamen vinculante sobre solicitudes (Aprobado, Devuelto o Rechazado con justificación obligatoria) y configuración de vista de consulta para Coordinador. | Rafael Carreño y Alonso Henao | Completado |
| **3** | **Módulo de autorregistro de nuevos estudiantes:** Creación del formulario de registro en la pantalla inicial del sistema con endpoint `/api/auth/register`, validación de campos obligatorios y pruebas exitosas con el usuario de prueba `jobixiita@gmail.com`. | Rafael Carreño | Completado |
| **4** | **Garantía de persistencia total y resiliencia de datos:** Configuración del almacenamiento estructurado en `data/sigpra-store.json`, sincronización bidireccional de datos en caliente y pruebas de persistencia ante reinicios del servidor. | Rafael Carreño | Completado |
| **5** | **Contenerización con Docker y despliegue en la nube (PaaS Render):** Creación y ajuste de `Dockerfile`, `docker-compose.yml` y archivo de configuración declarativo `render.yaml`. Despliegue exitoso en producción con enlace web público HTTPS (`https://sigpra-udi.onrender.com`). | Rafael Carreño y Daniel Rueda | Completado |
| **6** | **Depuración de la interfaz de usuario:** Remoción de códigos técnicos 'CU-XX' de los menús para presentación formal de usuario final e integración del logo corporativo institucional y diseño visual en la pantalla de autenticación. | Alonso Henao y Daniel Rueda | Completado |

---

## Pendiente para la próxima jornada

| # | Tarea | Responsable |
|---|---|---|
| **1** | Actualizar el documento final de la propuesta incorporando formalmente el caso de uso CU-08, diagrama de secuencia y diagrama de casos de uso general. | Alonso Henao |
| **2** | Sustituir los manuales obsoletos de escritorio Java por manuales web actualizados para la SPA y la arquitectura Node.js/PostgreSQL/MongoDB. | Daniel Rueda y Rafael Carreño |
| **3** | Redactar el artículo científico en formato IEEE oficial de dos columnas y elaborar el guión técnico para el video demostrativo. | Alonso Henao y Rafael Carreño |
| **4** | Diseñar el póster académico para la sustentación final y preparar la versión release en el repositorio de GitHub. | Rafael Carreño y Daniel Rueda |

---

## Dificultades u observaciones
Se logró con éxito el despliegue de SIGPRA en la plataforma PaaS Render con certificado SSL activo y disponibilidad pública 24/7. El flujo completo del CU-08 quedó funcional, garantizando que el estudiante no puede radicar sin soportes y que el Director de Programa emite el dictamen vinculante. La persistencia en el archivo `sigpra-store.json` asegura la continuidad de los datos en el entorno cloud.

---

## Integrantes
* Jesús Daniel Rueda Castillo
* Alonso Andrés Henao Roa
* Rafael Fabián Carreño Barrera
