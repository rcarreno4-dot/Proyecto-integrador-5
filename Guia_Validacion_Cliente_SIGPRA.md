# 📋 Guía Ejecutiva de Validación y Sustentación para el Cliente
### Sistema Integral de Gestión de Prácticas Académicas — SIGPRA
**Universidad de Investigación y Desarrollo (UDI) · Facultad de Ingenierías · Proyecto Integrador V**

---

## 🌐 1. Enlace de Acceso y Estado del Despliegue

* **URL en la Nube (Producción con SSL HTTPS)**: 👉 **[https://sigpra-udi.onrender.com](https://sigpra-udi.onrender.com)**
* **Repositorio Oficial en GitHub**: [https://github.com/rcarreno4-dot/Proyecto-integrador-5](https://github.com/rcarreno4-dot/Proyecto-integrador-5)
* **Versión del Sistema**: `v1.0.0-release` (Build `v20261101_v8`)
* **Marco Normativo Vigente**: **Decreto 1075 de 2015** del Ministerio de Educación Nacional (MEN) — Mínimo de 160 horas obligatorias de práctica pedagógica y profesional (tope máximo configurable de 320 horas).

> **💡 Consejo para la demostración:** Si abres la aplicación en el navegador web y deseas asegurar que se cargue la versión más reciente del diseño, presiona **`Ctrl + F5`** (o `Shift + F5`) para una recarga limpia de caché.

---

## 👥 2. Cuentas Institucionales para Demostración

En la pantalla de inicio de sesión de SIGPRA se encuentra disponible la lista desplegable interactiva **"Cuentas de prueba"**, la cual permite ingresar a cualquiera de los cuatro roles institucionales con **un solo clic**, o digitando las credenciales:

| Rol Institucional | Correo Institucional | Clave | Misión y Casos de Uso Principales |
|---|---|:---:|---|
| **Director de Programa** | `director@udi.edu.co` | `123456` | Dictamen y habilitación formal (CU-08), verificación de soportes documentales con firma SHA-256 y **Auditoría exclusiva**. |
| **Estudiante Practicante** | `estudiante@udi.edu.co` | `123456` | Radicación con Stepper de 4 pasos, seguimiento en tiempo real, protección de práctica activa y bitácora de 160 horas. |
| **Coordinador de Prácticas** | `coordinador@udi.edu.co` | `123456` | Periodos académicos (con año dinámico, validación de fechas y tope 320h), convenios, asignación tripartita (CU-03) y reportes. |
| **Docente Asesor** | `docente@udi.edu.co` | `123456` | Validación formativa de actividades, rúbricas y visitas de acompañamiento en centro de práctica. |

---

## 🛠️ 3. Resumen de Mejoras y Correcciones Aplicadas

1. **Persistencia y Coherencia Formativa de Plazas**:
   - Al registrar un nuevo convenio y sus plazas (`agreement-place`), el sistema vincula y preserva fielmente el **Programa Académico Afín** (`Ingeniería de Sistemas`, `Licenciatura en Educación Infantil`, etc.).
2. **Dinamismo Reactivo en Asignaciones (CU-03)**:
   - Al cambiar el periodo académico en el paso 1, el listado de practicantes se actualiza automáticamente mostrando quién se encuentra habilitado para esa convocatoria con distintivos `[✅ Habilitado]` o `[⏳ Pendiente]`.
   - Las plazas disponibles indican explícitamente su programa afín entre corchetes: `[Ingeniería de Sistemas]`.
3. **Onboarding y Seguimiento en Vivo para Estudiantes**:
   - La pantalla de inicio del estudiante ya no muestra cuadros vacíos. Si tiene una solicitud en curso, visualiza el Stepper interactivo y el estado dictaminado por la Dirección; si es un estudiante nuevo, visualiza una guía clara de 3 pasos para radicar.
4. **Protección de Estudiante con Práctica Asignada**:
   - Si un practicante ya cuenta con una práctica académica activa y asignada, el sistema **oculta automáticamente la opción de "Solicitar práctica"** en el menú de navegación lateral.
   - En caso de acceder al módulo, el sistema bloquea los formularios de radicación/modificación y presenta una tarjeta formal indicando que su práctica ya está en curso, orientándolo a registrar sus horas en **"Mi bitácora"**.
5. **Dashboard Modernizado para Dirección y Docencia**:
   - Indicadores KPI dinámicos con microinteracciones de navegación.
   - En el Home del Director se resalta la tarjeta de advertencia **"Por Dictaminar"** y una tabla prioritaria con acceso directo al botón `"🔍 Evaluar"`.
6. **Visor de Soportes Apilable sin Pérdida de Datos**:
   - Dentro de la ventana de evaluación de solicitudes, los botones `"👁️ Ver Soporte"` abren los documentos digitales en una capa superior (`z-index: 1060`), permitiendo auditar la Matrícula, EPS/ARL y Paz y Salvo sin cerrar la ventana de decisión ni borrar las justificaciones redactadas.
7. **Control de Acceso: Auditoría Exclusiva del Director**:
   - El módulo de **Auditoría** queda reservado exclusivamente para la Dirección de Programa, eliminándose de la navegación del Coordinador y bloqueando accesos no autorizados.
8. **Configuración de Periodos con Año Dinámico y Validaciones Rigurosas**:
   - Etiqueta corregida a **"Año académico"** con precarga dinámica del año actual (`new Date().getFullYear()`).
   - **Bloqueo de fechas antiguas**: el selector de calendario (`min="today"`) y la validación en JavaScript impiden radicar periodos con fechas de inicio en el pasado.
   - **Límite reglamentario de 320 horas**: control por atributo HTML (`max="320"`) y validación programática que rechaza intensidades superiores al máximo institucional.
9. **Generación de Reportes Bajo Demanda**:
   - Al entrar al módulo de consolidado, se presenta el formulario de filtros limpio. La información consolidada, las métricas KPI y la tabla de prácticas se despliegan **únicamente cuando el usuario ejecuta la búsqueda**.
10. **Botones de Exportación e Impresión Condicionados**:
    - Las opciones **"📊 Exportar a Excel (CSV)"** y **"🖨️ Imprimir / Guardar PDF Oficial"** aparecen integradas en la tabla de resultados solo cuando se han aplicado los filtros y existen registros encontrados.
11. **Exportación JavaScript Directa de la Tabla Visible**:
    - La nueva función `exportTableToCsv` recorre dinámicamente las filas visibles del DOM de la tabla `#reports-table`, extrayendo con total fidelidad lo que el usuario está viendo en pantalla (respetando filtros de búsqueda en vivo, con BOM UTF-8 y `;` para Excel/WPS).
12. **Impresión Enfocada Exclusiva de los Datos Buscados**:
    - La hoja de estilo `@media print` oculta estrictamente cualquier fila descartada por el buscador (`display: none !important`), ocultando elementos auxiliares para imprimir **únicamente la tabla filtrada** con membrete oficial y firmas de Coordinación y Dirección.

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
3. En el menú lateral, verificar que el Director **SÍ tiene acceso al módulo "Auditoría"**.
4. En la tabla del Home **"Solicitudes pendientes de dictamen"**, hacer clic en el botón azul **`"🔍 Evaluar"`** de un estudiante pendiente (ej: Ana Francisca o Camila).
5. Dentro de la ventana emergente:
   - Hacer clic en el botón **`"📄 Ver Soporte"`** al lado de cada documento (Certificado de Matrícula, Afiliación EPS/ARL, Paz y Salvo Académico).
   - Constatar que se abre el visor con membrete oficial, marca de agua y firma criptográfica SHA-256.
   - Cerrar el visor con el botón `"Cerrar"` y verificar que la ventana de evaluación permanece abierta intacta sin borrar el formulario.
6. Probar el dictamen:
   - Si se selecciona **"Aprobar y Habilitar"**, el practicante queda habilitado formalmente en el sistema.
   - Si se selecciona **"Devolver para corrección"**, el sistema exige la observación obligatoria y notifica al estudiante.

---

### Paso 2: Prueba del Rol Estudiante Practicante
1. Ingresar con `estudiante@udi.edu.co` / `123456` (o un estudiante con práctica activa como Laura Gómez):
   - **Comprobar la regla de protección**: Como Laura Gómez ya tiene una práctica activa asignada, en el menú lateral **NO aparece la opción "Solicitar práctica"**, evitando radicaciones duplicadas.
   - Ir a **"Mi avance"**: Observar el velocímetro y la barra de progreso hacia las **160 horas obligatorias**.
2. Ingresar con un estudiante nuevo o sin práctica asignada (o registrar uno nuevo):
   - Verificar que **SÍ aparece la opción "Solicitar práctica"** en el menú.
   - En la **Etapa 1**, verificar el selector desplegable de periodos y convocatorias activas.
   - En la **Etapa 2**, seleccionar modalidad institucional o autogestionada.
   - En la **Etapa 3**, adjuntar los archivos de soporte.
   - En la **Etapa 4**, hacer clic en *"Radicar Solicitud Formal"*.

---

### Paso 3: Prueba del Rol Coordinador de Prácticas
1. Ingresar con `coordinador@udi.edu.co` / `123456`.
2. En el menú lateral, constatar que **"Auditoría" ya NO aparece** (es exclusiva de Dirección).
3. Ir a **"Periodos de práctica"** ➔ **"Nuevo periodo"**:
   - En el Paso 1, comprobar la etiqueta **"Año académico"** con el año actual precargado por defecto.
   - En el Paso 2, intentar colocar una fecha de inicio en el pasado: el calendario y la validación bloquearán el guardado.
   - Intentar ingresar más de 320 horas (ej: 400 hrs): el sistema restringe el valor a un máximo de 320 horas reglamentarias.
4. Ir a **"Asignaciones"**:
   - En el Paso 1, cambiar el **Periodo Académico a Gestionar** y constatar cómo se actualiza la lista de estudiantes con su etiqueta `[✅ Habilitado]` o `[⏳ Pendiente]`.
   - En el Paso 2 (Plazas), verificar que cada cupo exhibe el programa formativo afín: `[Ingeniería de Sistemas]`.
   - En el historial de asignaciones, escribir en el buscador `🔍` para filtrar estudiantes o empresas en tiempo real.
5. Ir a **"Consolidado" (Reportes)**:
   - Constatar que al ingresar, la vista se presenta limpia con el panel de filtros y la invitación a buscar.
   - Seleccionar un filtro o hacer clic en **`"🔍 Buscar / Generar Reporte"`**.
   - Al generarse la búsqueda, observar cómo emergen inmediatamente los contadores KPI, la tabla de resultados y la barra con los botones **"📊 Exportar a Excel (CSV)"** y **"🖨️ Imprimir / Guardar PDF Oficial"**.
   - En el buscador de la tabla, filtrar por un estudiante (ej: "Laura") y hacer clic en **"📊 Exportar a Excel (CSV)"**: el archivo descargado exportará exactamente los registros visibles con codificación UTF-8 con BOM y separador `;`.
   - Hacer clic en **"🖨️ Imprimir / Guardar PDF Oficial"**: comprobar que la vista previa de impresión muestra únicamente los datos filtrados con membrete UDI y firmas, sin filas ocultas.

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
| **Seguridad y Control de Roles** | Auditoría exclusiva para Director, firmas SHA-256 para documentos y protección contra solicitudes duplicadas. | Menú dinámico por rol y visor de soportes con sello digital. |
| **Cumplimiento Normativo MEN** | Decreto 1075 de 2015 (Reglamentación de Prácticas Pedagógicas, Horas Mínimas 160h y tope máximo de 320h). | Semáforo de avance, reportes oficiales y límites en formulario de periodos. |
| **Usabilidad y Accesibilidad** | Búsqueda bajo demanda, exportación fiel de tablas HTML, impresión oficial focalizada y modales apilables. | Interfaz Bootstrap 5 responsive móvil/escritorio. |

---
*Documento elaborado para la sustentación final de Proyecto Integrador V — Universidad de Investigación y Desarrollo (UDI), 2026.*
