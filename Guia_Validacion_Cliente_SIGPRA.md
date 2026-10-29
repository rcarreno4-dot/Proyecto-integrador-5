# 📋 Guía Ejecutiva de Validación y Sustentación para el Cliente
### Sistema Integral de Gestión de Prácticas Académicas — SIGPRA
**Universidad de Investigación y Desarrollo (UDI) · Facultad de Ingenierías · Proyecto Integrador V**

---

## 🌐 1. Enlace de Acceso y Estado del Despliegue

* **URL en la Nube (Producción con SSL HTTPS)**: 👉 **[https://sigpra-udi.onrender.com](https://sigpra-udi.onrender.com)**
* **Repositorio Oficial en GitHub**: [https://github.com/rcarreno4-dot/Proyecto-integrador-5](https://github.com/rcarreno4-dot/Proyecto-integrador-5)
* **Versión del Sistema**: `v1.0.0-release` (Build `v20261028_v7`)
* **Marco Normativo Vigente**: **Decreto 1075 de 2015** del Ministerio de Educación Nacional (MEN) — Mínimo de 160 horas obligatorias de práctica pedagógica y profesional.

> **💡 Consejo para la demostración:** Si abres la aplicación en el navegador web y deseas asegurar que se cargue la versión más reciente del diseño, presiona **`Ctrl + F5`** (o `Shift + F5`) para una recarga limpia de caché.

---

## 👥 2. Cuentas Institucionales para Demostración

En la pantalla de inicio de sesión de SIGPRA se encuentra disponible la lista desplegable interactiva **"Cuentas de prueba"**, la cual permite ingresar a cualquiera de los cuatro roles institucionales con **un solo clic**, o digitando las credenciales:

| Rol Institucional | Correo Institucional | Clave | Misión y Casos de Uso Principales |
|---|---|:---:|---|
| **Director de Programa** | `director@udi.edu.co` | `123456` | Dictamen y habilitación formal (CU-08), verificación de soportes documentales con firma SHA-256. |
| **Estudiante Practicante** | `estudiante@udi.edu.co` | `123456` | Radicación con Stepper de 4 pasos, seguimiento en tiempo real y bitácora de 160 horas. |
| **Coordinador de Prácticas** | `coordinador@udi.edu.co` | `123456` | Periodos académicos, convenios, plazas formativas, asignación tripartita (CU-03) y reportes ejecutivos. |
| **Docente Asesor** | `docente@udi.edu.co` | `123456` | Validación formativa de actividades, rúbricas y visitas de acompañamiento en centro de práctica. |

---

## 🛠️ 3. Resumen de Mejoras y Correcciones Aplicadas

1. **Persistencia y Coherencia Formativa de Plazas**:
   - Al registrar un nuevo convenio y sus plazas (`agreement-place`), el sistema vincula y preserva fielmente el **Programa Académico Afín** (`Ingeniería de Sistemas`, `Licenciatura en Educación Infantil`, etc.).
2. **Dinamismo Reactivo en Asignaciones (CU-03)**:
   - Al cambiar el periodo académico en el paso 1, el listado de practicantes se actualiza automáticamente mostrando quién se encuentra habilitado para esa convocatoria.
   - Las plazas disponibles indican explícitamente su programa entre corchetes: `[Ingeniería de Sistemas]`.
3. **Onboarding y Seguimiento en Vivo para Estudiantes**:
   - La pantalla de inicio del estudiante ya no muestra cuadros vacíos. Si tiene una solicitud en curso, visualiza el Stepper interactivo y el estado dictaminado por la Dirección; si es un estudiante nuevo, visualiza una guía clara de 3 pasos para radicar.
4. **Dashboard Modernizado para Dirección y Docencia**:
   - Indicadores KPI dinámicos con microinteracciones de navegación.
   - En el Home del Director se resalta la tarjeta de advertencia **"Por Dictaminar"** y una tabla prioritaria con acceso directo al botón `"🔍 Evaluar"`.
5. **Visor de Soportes Apilable sin Pérdida de Datos**:
   - Dentro de la ventana de evaluación de solicitudes, los botones `"👁️ Ver Soporte"` abren los documentos digitales en una capa superior (`z-index: 1060`), permitiendo auditar la Matrícula, EPS/ARL y Paz y Salvo sin cerrar la ventana de decisión ni borrar las justificaciones redactadas.
6. **Consolidado Tabular Profesional y Buscadores en Tiempo Real**:
   - Filtro de periodos sin ambigüedad (`2026-2 — [Programa] ([Nivel])`).
   - Búsqueda instantánea con icono `🔍` en el historial de asignaciones y en los reportes de prácticas.
   - Exportación a **Excel (CSV con BOM UTF-8 y `;`)** e **Impresión Oficial UDI** con membrete y bloques de firma.

---

## 🧪 4. Guión de Prueba Paso a Paso por Roles

```
┌────────────────────────────────────────────────────────────────────────┐
│                        FLUJO COMPLETO DEL TRÁMITE                      │
├────────────────────────────────────────────────────────────────────────┤
│ 1. Estudiante radica solicitud con soportes digitales (CU-08)         │
│ 2. Director audita documentos y emite aprobación (Habilitación)        │
│ 3. Coordinador asigna plaza y docente según programa afín (CU-03)      │
│ 4. Estudiante reporta horas en bitácora hacia la meta de 160h          │
│ 5. Docente tutor valida actividades y registra acompañamiento          │
│ 6. Coordinación y Dirección generan el informe consolidado oficial    │
└────────────────────────────────────────────────────────────────────────┘
```

### Paso 1: Prueba del Rol Director de Programa
1. Ingresar con `director@udi.edu.co` / `123456`.
2. Observar las **Tarjetas KPI interactivas** (`Por Dictaminar`, `Habilitados`, `En Práctica`, `Avance Reglamentario`).
3. En la tabla del Home **"Solicitudes pendientes de dictamen"**, hacer clic en el botón azul **`"🔍 Evaluar"`** de un estudiante pendiente (ej: Ana Francisca o Camila).
4. Dentro de la ventana emergente:
   - Hacer clic en el botón **`"👁️ Ver Soporte"`** al lado de cada documento (Certificado de Matrícula, Afiliación EPS/ARL, Paz y Salvo Académico).
   - Constatar que se abre el visor con membrete oficial, marca de agua y firma criptográfica SHA-256.
   - Cerrar el visor con el botón `"Cerrar"` y verificar que la ventana de evaluación permanece abierta intacta.
5. Probar el dictamen:
   - Si se selecciona **"Aprobar y Habilitar"**, el practicante queda habilitado formalmente en el sistema.
   - Si se selecciona **"Devolver para corrección"**, el sistema exige la observación obligatoria y notifica al estudiante.

---

### Paso 2: Prueba del Rol Estudiante Practicante
1. Ingresar con `estudiante@udi.edu.co` / `123456` (o registrar una cuenta nueva en el botón *"Registrar nueva cuenta de estudiante"*).
2. **Pantalla de Inicio Inteligente**:
   - Si el estudiante cuenta con trámite radicado, visualiza el **Stepper de 4 fases** (Radicación ➔ Dirección ➔ Asignación ➔ En Ejecución) y el estado emitido por el Director.
   - Si es nuevo, observa el banner de bienvenida institucional con los requisitos del Decreto 1075.
3. Ir al módulo **"Solicitar práctica"**:
   - En la **Etapa 1**, verificar el selector desplegable de periodos y convocatorias activas.
   - En la **Etapa 2**, seleccionar modalidad institucional o autogestionada.
   - En la **Etapa 3**, adjuntar los archivos de soporte.
   - En la **Etapa 4**, hacer clic en *"Radicar Solicitud Formal"*.
4. Ir al módulo **"Mi avance"**:
   - Observar el velocímetro y la barra de progreso hacia las **160 horas obligatorias**.
   - Identificar las horas aprobadas, horas en bitácoras pendientes por validar y horas faltantes para cumplir la meta.

---

### Paso 3: Prueba del Rol Coordinador de Prácticas
1. Ingresar con `coordinador@udi.edu.co` / `123456`.
2. En el Dashboard, hacer clic en cualquier tarjeta KPI para navegar directamente a dicho módulo.
3. Ir a **"Asignaciones"**:
   - En el Paso 1, cambiar el **Periodo Académico a Gestionar** y constatar cómo se actualiza la lista de estudiantes con su etiqueta `[✅ Habilitado]` o `[⏳ Pendiente]`.
   - En el Paso 2 (Plazas), verificar que cada cupo exhibe el programa formativo afín: `[Ingeniería de Sistemas]`.
   - En el historial de asignaciones, escribir en el buscador `🔍` para filtrar estudiantes o empresas en tiempo real.
4. Ir a **"Consolidado" (Reportes)**:
   - Probar el botón verde **`"📊 Exportar a Excel (CSV)"`**: el archivo descargado se abre automáticamente en columnas limpias con tildes en Excel y WPS Office.
   - Probar el botón azul **`"🖨️ Imprimir / Guardar PDF Oficial"`**: se genera una vista ejecutiva con membrete formal de la UDI, métricas en una fila y firmas de la Coordinación y Dirección de Programa.

---

### Paso 4: Prueba del Rol Docente Asesor
1. Ingresar con `docente@udi.edu.co` / `123456`.
2. En el Dashboard, comprobar las tarjetas KPI de estudiantes a cargo y horas validadas.
3. Ir a **"Validar actividades"**: revisar las actividades pendientes enviadas por los estudiantes y aprobarlas o devolverlas con observaciones pedagógicas.
4. Ir a **"Visitas y evaluación"**: registrar una visita de acompañamiento institucional con rúbrica ponderada al 100%.

---

## 🏛️ 5. Cumplimiento de Criterios de Evaluación del Proyecto Integrador V

| Requisito Académico | Implementación en SIGPRA | Evidencia en la Aplicación |
|---|---|---|
| **Arquitectura de Software** | MVC desacoplado con backend en Node.js, Express, TypeScript y frontend reactivo SPA. | Código en `Aplicacion_SIGPRA/Primer servicio/` y despliegue en Render. |
| **Seguridad y Trazabilidad** | Firmas SHA-256 para documentos digitales y módulo de auditoría de eventos. | Módulo "Auditoría" y visor de soportes. |
| **Cumplimiento Normativo MEN** | Decreto 1075 de 2015 (Reglamentación de Prácticas Pedagógicas y Horas Mínimas). | Semáforo de avance, reportes oficiales y barra de 160h. |
| **Usabilidad y Accesibilidad** | Stepper guiado, microinteracciones en KPIs, modales apilables y exportación oficial. | Interfaz Bootstrap 5 responsive móvil/escritorio. |

---
*Documento elaborado para la sustentación final de Proyecto Integrador V — Universidad de Investigación y Desarrollo (UDI), 2026.*
