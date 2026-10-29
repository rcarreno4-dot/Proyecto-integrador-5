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
| **Director de Programa** | `director@udi.edu.co` | `123456` | Bandeja de radicación CU-08, verificación de soportes con firma SHA-256 y dictamen. |
| **Docente Asesor** | `docente@udi.edu.co` | `123456` | Validación pedagógica de actividades, rúbricas y visitas de seguimiento. |
| **Estudiante Practicante** | `estudiante@udi.edu.co` | `123456` | Radicación con Stepper de 4 pasos, bitácora diaria y barra de 160 horas obligatorias. |

---

## 📂 Organización del Repositorio

El repositorio se encuentra estructurado en módulos claros y desacoplados:

* 📋 [**`Guia_Validacion_Cliente_SIGPRA.md`**](./Guia_Validacion_Cliente_SIGPRA.md): **Guía rápida de sustentación y validación funcional por roles para el cliente y evaluadores**.
* 📁 [**`Entrega_actual/`**](./Entrega_actual/): **Todos los entregables académicos de la sustentación final** organizados por subcarpetas:
  * 📄 [**`Documento_Final/`**](./Entrega_actual/Documento_Final/): Documento de propuesta final de 17 puntos según la rúbrica oficial.
  * 🔬 [**`Articulo_IEEE/`**](./Entrega_actual/Articulo_IEEE/): Artículo científico en formato oficial IEEE (2 columnas).
  * 📊 [**`Poster_Sustentacion/`**](./Entrega_actual/Poster_Sustentacion/): Póster de defensa en alta resolución (90x120 cm).
  * 🎬 [**`Guion_Video/`**](./Entrega_actual/Guion_Video/): Guión estructurado y libreto para el video demostrativo funcional.
  * 📖 [**`Manuales_Sistema/`**](./Entrega_actual/Manuales_Sistema/): Manual de Usuario por roles y Manual Técnico de arquitectura y APIs.
  * 📝 [**`Actas de seguimiento/`**](./Entrega_actual/Actas%20de%20seguimiento/): Registro formal de las reuniones de seguimiento y avances.
  * 📸 [**`Vistas_actuales_SIGPRA/`**](./Entrega_actual/Vistas_actuales_SIGPRA/): Prototipos de interfaz de alta fidelidad (CU-01 a CU-08).
  * 📐 [**`Diagramas UML/`**](./Entrega_actual/Diagramas%20UML/): Modelos de Arquitectura, Casos de Uso, Secuencia, Clases y Dominio.
* 💻 [**`Aplicacion_SIGPRA/`**](./Aplicacion_SIGPRA/): Código fuente del aplicativo web SPA con backend Node.js, Express, TypeScript y arquitectura MVC desplegado en Render.
* 🏗️ [**`Aplicacion completa/`**](./Aplicacion%20completa/): Arquitectura distribuida contenerizada en Docker (Microservicios + bases de datos).
* 🗄️ [**`Base_de_datos_SIGPRA/`**](./Base_de_datos_SIGPRA/): Scripts de creación de esquemas, tablas, triggers y diagramas Entidad-Relación de PostgreSQL.
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
