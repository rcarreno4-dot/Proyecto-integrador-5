# SIGPRA — Informe Semanal de Seguimiento
**Proyecto Integrador — UDI — II — 2026**

---

| **Semana Nº** | 47 (Sesión de Cierre Definitivo, Validación de Rúbrica, Seguridad de Autenticación, Depuración de Datos y Release v2.0.0) | **Fechas** | Domingo 22 de noviembre de 2026 |
|---|---|---|---|
| **Elaborado por** | Rafael Carreño, Alonso Henao, Jesús Rueda | | |

---

## Lo que se hizo esta jornada

| # | Actividad | Responsable | Estado |
|---|---|---|---|
| **1** | **Auditoría Integral y Validación de Rúbrica Final (17 Aspectos):** Verificación punto a punto de los 17 capítulos del Documento Final (`Propuesta_SIGPRA_Entrega_Final.docx`), Artículo IEEE (6 páginas en formato formal a 2 columnas), Póster Académico de Sustentación (PDF y PPTX 90×120 cm rediseñado), Manual Técnico y de Usuario, Scripts CRUD de Base de Datos y Modelo UML Astah (`.asta`). | Alonso Henao y Rafael Carreño | Completado |
| **2** | **Implementación de Reglas de Negocio en la Aplicación Web (Producción y React):** Restricción y bloqueo automático del cierre de periodo académico mientras existan actividades de bitácora en estado "PENDIENTE" de revisión por docentes tutores (Regla RN-11 / RF-01). Unificación del dictamen de evaluación dual (RF-05), vinculando la calificación cuantitativa (0.0 a 5.0) con los conceptos oficiales del reglamento institucional (*Aprobado*, *Aprobado con recomendaciones*, *No aprobado*). | Rafael Carreño y Jesús Rueda | Completado |
| **3** | **Refuerzo de Seguridad y Autenticación en la Aplicación Web:** Retiro completo de atajos de inicio de sesión con 1 clic ("Cuentas de prueba") y remoción del manejador de bypass `data-login`. Limpieza de credenciales precargadas en el formulario de login, exigiendo autenticación estricta obligatoria mediante correo institucional UDI y contraseña para todos los roles (`Coordinador`, `Estudiante`, `Docente Asesor`, `Director de Programa`). Retiro del distintivo de desarrollo *"API Express conectada"* de la barra lateral, otorgando un acabado profesional de producción. | Rafael Carreño | Completado |
| **4** | **Depuración de Base de Datos y Datos de Prueba:** Eliminación del programa y periodo de prueba informal (`sebarcar` / `PRACTICA VII`). Depuración de la tabla de plazas disponibles, retirando registros de prueba y consolidando 3 plazas oficiales y vigentes con el *Centro Educativo La Esperanza* y la *Fundación Aprender*. Purga de alumnos de prueba duplicados, manteniendo una lista limpia de 5 practicantes oficiales con código UDI para demostración fluida en vivo. Migración del almacenamiento local del navegador a `sigpra_mvp_data_v2` para auto-limpieza de caché en clientes. | Rafael Carreño y Alonso Henao | Completado |
| **5** | **Depuración y Optimización del Repositorio en GitHub:** Eliminación de más de 150.000 líneas de código y archivos residuales obsoletos: carpeta huérfana de respaldo de octubre (`sigpra_pg_respaldo_...`), duplicado suelto de la guía de validación en raíz y la carpeta histórica `documentos_alternos/` (dumps de Oracle y código antiguo Java). Consolidación del repositorio a 10 elementos esenciales, limpios y ordenados. | Alonso Henao y Rafael Carreño | Completado |
| **6** | **Publicación y Oficialización de la Release v2.0.0 en GitHub:** Creación de la etiqueta anotada y publicación de la Release oficial **`v2.0.0-entrega-final`** catalogada con el distintivo verde `Latest` en GitHub Releases, empaquetando el código final descargable (`.zip` y `.tar.gz`). | Rafael Carreño | Completado |

---

## Pendiente para la entrega final y sustentación

| # | Tarea | Responsable |
|---|---|---|
| **1** | Subir los enlaces de GitHub, Render, YouTube y los archivos oficiales consolidados a la plataforma Google Classroom antes de las 11:30 pm de hoy, domingo 22 de noviembre de 2026. | Todo el equipo |
| **2** | Sustentación final del proyecto integrador ante el comité evaluador (martes 24 / miércoles 25 de noviembre de 2026). | Todo el equipo |

---

## Dificultades u observaciones
El proyecto SIGPRA cumple al 100% con los requerimientos funcionales, no funcionales y la rúbrica institucional de evaluación. La aplicación se encuentra desplegada y operativa en Render PaaS con SSL, el repositorio de GitHub está completamente depurado y cuenta con su release formal v2.0.0 con distintivo Latest, y toda la documentación académica está consolidada y verificada para la entrega formal y sustentación ante el jurado.

---

## Integrantes
* Jesús Daniel Rueda Castillo
* Alonso Andrés Henao Roa
* Rafael Fabián Carreño Barrera
