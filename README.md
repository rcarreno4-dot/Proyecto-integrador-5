# 🎓 SIGPRA — Software Integral de Gestión de Prácticas Académicas
### Universidad de Investigación y Desarrollo (UDI) · Facultad de Ingenierías · Programa de Ingeniería de Sistemas
**Proyecto Integrador V · Quinto Semestre (2026-2)**

[![Render Status](https://img.shields.io/badge/Render-Live%20Deploy-success?style=flat&logo=render)](https://sigpra-udi.onrender.com)
[![Release](https://img.shields.io/badge/Release-v1.0.0--release-blue?style=flat&logo=github)](https://github.com/rcarreno4-dot/Proyecto-integrador-5/releases/tag/v1.0.0-release)
[![Node.js](https://img.shields.io/badge/Node.js-20+-green?style=flat&logo=node.js)](https://nodejs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.4-blue?style=flat&logo=typescript)](https://www.typescriptlang.org/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-purple?style=flat&logo=bootstrap)](https://getbootstrap.com/)
[![Decreto 1075](https://img.shields.io/badge/Normativa-Decreto%201075%20de%202015-orange)](https://www.mineducacion.gov.co/)

---

## 🚀 Despliegue en la Nube (Producción)

La plataforma funcional está desplegada con certificado SSL HTTPS en:  
👉 **[https://sigpra-udi.onrender.com](https://sigpra-udi.onrender.com)**

### 👥 Cuentas de Acceso para Demostración:
| Rol Institucional | Correo Electrónico | Contraseña | Capacidades y Casos de Uso |
|---|---|---|---|
| **Coordinador** | `coordinador@udi.edu.co` | `123456` | Programar periodos, gestionar convenios/plazas, asignaciones y analítica. |
| **Director de Programa** | `director@udi.edu.co` | `123456` | Revisión de solicitudes de práctica (CU-08): ver soportes, aprobar, devolver o rechazar. Consulta de la auditoría. |
| **Docente Asesor** | `docente@udi.edu.co` | `123456` | Validación pedagógica de actividades, rúbricas y visitas de seguimiento. |
| **Estudiante Practicante** | `estudiante@udi.edu.co` | `123456` | Solicitud de práctica en 4 pasos, bitácora de actividades y barra de avance frente a las horas mínimas del periodo. |

---

## 📂 Organización del Repositorio

Así está organizado el repositorio:

* 📋 [**`Guia_Validacion_Cliente_SIGPRA.md`**](./Guia_Validacion_Cliente_SIGPRA.md): **Guía rápida de sustentación y validación funcional por roles para el cliente y evaluadores**.
* 📁 [**`Entrega_actual/`**](./Entrega_actual/): **Todos los entregables académicos de la sustentación final** organizados por subcarpetas:
  * 📄 [**`Documento_Final/`**](./Entrega_actual/Documento_Final/): Documento final de la propuesta: análisis, diseño, implementación y anexos.
  * 🔬 [**`Articulo_IEEE/`**](./Entrega_actual/Articulo_IEEE/): Artículo científico en formato oficial IEEE (2 columnas).
  * 📊 [**`Poster_Sustentacion/`**](./Entrega_actual/Poster_Sustentacion/): Póster de defensa en alta resolución (90x120 cm).
  * 🎬 **Video demostrativo**: [https://youtu.be/H5jdoqycu90](https://youtu.be/H5jdoqycu90)
  * 📖 [**`Manuales_Sistema/`**](./Entrega_actual/Manuales_Sistema/): Manual de Usuario por roles y Manual Técnico de arquitectura y APIs.
  * 📝 [**`Actas de seguimiento/`**](./Entrega_actual/Actas%20de%20seguimiento/): Registro formal de las reuniones de seguimiento y avances.
  * 📸 [**`Vistas_actuales_SIGPRA/`**](./Entrega_actual/Vistas_actuales_SIGPRA/): Prototipos de interfaz de alta fidelidad (CU-01 a CU-08).
  * 📐 [**`Diagramas UML/`**](./Entrega_actual/Diagramas%20UML/): Modelos de Arquitectura, Casos de Uso, Secuencia, Clases y Dominio.
* 💻 [**`Aplicacion_SIGPRA/`**](./Aplicacion_SIGPRA/): Código de la aplicación. La versión entregada y publicada en Render está en [`Primer servicio/`](./Aplicacion_SIGPRA/Primer%20servicio/): una SPA con Bootstrap y un servidor Node.js, Express y TypeScript con patrón MVC. Las carpetas `s1-…` a `s4-…`, `gateway/` y `microservices/` son una versión inicial de los servicios separados y no hacen parte de la versión entregada.
* ⚛️ [**`sigpra-frontend-react/`**](./sigpra-frontend-react/): Segunda versión de la interfaz en React 18, TypeScript y Vite. Usa la misma API y solo se ejecuta en local (`iniciar_react.bat`).
* 🏗️ [**`Aplicacion completa/`**](./Aplicacion%20completa/): Prototipo anterior con servicios separados en contenedores Docker. No es la versión entregada (ver la sección 6.5 del documento final).
* 🐳 [**`docker-compose.yml`**](./docker-compose.yml): Entorno local con la aplicación, PostgreSQL 16 y MongoDB 7.
* 🗄️ [**`Base_de_datos_SIGPRA/`**](./Base_de_datos_SIGPRA/): Scripts de PostgreSQL (esquema de 16 tablas) y de MongoDB, pruebas CRUD, diccionario de datos y diagramas entidad-relación y relacional.
* 📦 [**`documentos_alternos/`**](./documentos_alternos/): Respaldos históricos de la primera entrega y prototipo Java/Oracle.

---

## 💻 Ejecución Local del Proyecto

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/rcarreno4-dot/Proyecto-integrador-5.git
   cd Proyecto-integrador-5/"Aplicacion_SIGPRA/Primer servicio"
   ```
2. Instalar dependencias y compilar:
   ```bash
   npm install
   npm run build
   ```
3. Iniciar el servidor local:
   ```bash
   npm start
   ```
4. Abrir en el navegador: `http://localhost:3000`

---
*Desarrollado para el Proyecto Integrador V — Universidad de Investigación y Desarrollo (UDI), 2026.*
