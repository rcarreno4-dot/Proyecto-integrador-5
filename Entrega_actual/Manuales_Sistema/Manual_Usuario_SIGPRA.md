# Manual de Usuario - Sistema SIGPRA
## Software Integral de Gestión de Prácticas Académicas
**Universidad de Investigación y Desarrollo (UDI)**  
**Facultad de Ingenierías y Ciencias Básicas**  
**Programa de Ingeniería de Sistemas**  
**Proyecto Integrador V - Noviembre 2026**

---

### Integrantes del Proyecto
* **Jesús Daniel Rueda Castillo**
* **Alonso Andrés Henao Roa**
* **Rafael Fabián Carreño Barrera**

---

## 1. Enlaces de Acceso y Requisitos
* **Enlace Público en la Nube (Producción):** [https://sigpra-udi.onrender.com](https://sigpra-udi.onrender.com)
* **Servidor Local (Laboratorios UDI):** `http://localhost:3000`
* **Navegadores Soportados:** Google Chrome, Microsoft Edge, Mozilla Firefox, Apple Safari (100% web responsive, compatible con PC, laptops y móviles).

---

## 2. Acceso y Autenticación
La pantalla de inicio ofrece tres mecanismos de acceso:
1. **Inicio de Sesión Estándar:** Ingrese su correo institucional (@udi.edu.co) y clave para acceder a su perfil asignado.
2. **Registro de Nuevos Estudiantes (Autoservicio):** Haga clic en `📝 Registrar nueva cuenta de estudiante`, complete el nombre, correo, código estudiantil y semestre (7º a 10º) para crear su cuenta y radicar su práctica inmediatamente.
3. **Cuentas de Demostración (Acceso Rápido para Auditoría):**
   * **Coordinador:** `coordinador@udi.edu.co` / `123456`
   * **Director de Programa:** `director@udi.edu.co` / `123456`
   * **Docente Asesor:** `docente@udi.edu.co` / `123456`
   * **Estudiante Demo:** `estudiante@udi.edu.co` / `123456`
   * **Estudiante Creada:** `jobixiita@gmail.com` / `123456` (Ana Francisca)

---

## 3. Guía Operativa por Rol

### 3.1 Estudiante Practicante
* **Radicación de Solicitud (CU-08):**
  1. Ingrese al menú **"Solicitar práctica"**.
  2. Seleccione modalidad: *Convenio Institucional* (elija la entidad receptora de la lista) o *Práctica Autogestionada* (digite el nombre de la empresa).
  3. Adjunte en formato PDF los documentos obligatorios:
     - Matrícula académica y financiera vigente.
     - Certificación de afiliación a EPS y ARL (vigencia menor a 30 días).
     - Paz y salvo académico de la institución.
  4. Presione **"Radicar Solicitud Oficial"** para enviarla a revisión de la Dirección (estado `PENDIENTE_REVISION`).
* **Subsanación de Devoluciones:**
  - Si la Dirección devuelve la solicitud, la pestaña *Mi Estado* muestra la observación en naranja. El estudiante puede sustituir el soporte observado y volver a enviar.
* **Registro de Bitácora (CU-04):**
  - Registre la fecha, tipo de labor (clase, tutoría, planeación), horas cronológicas ejecutadas y adjunte soporte digital.

### 3.2 Director de Programa
* **Evaluación de Solicitudes (CU-08):**
  1. Ingrese al módulo **"Solicitudes de práctica"**.
  2. En la tabla de postulaciones, presione **"Evaluar"** en la fila del estudiante.
  3. Examine los documentos cargados en el modal de dictamen.
  4. Emita su resolución:
     - **Aprobar:** Cambia el estado a `APROBADA` y marca al estudiante como `Habilitado` para ser asignado por la Coordinación.
     - **Devolver:** Exige ingresar la **Justificación Obligatoria** de los documentos a corregir.
     - **Rechazar:** Requiere justificación obligatoria por incumplimiento estatutario.

### 3.3 Coordinador de Prácticas
* **Periodos (CU-01):** Apertura, programación y publicación del semestre académico con sus semanas y horas requeridas.
* **Convenios y Plazas (CU-02):** Registro de instituciones receptoras aliadas, vigencia legal y cupos disponibles.
* **Asignar Practicantes (CU-03):** Conecta a estudiantes con dictamen `APROBADA` a una plaza con cupo disponible y les designa su docente tutor.
* **Consolidado (CU-07):** Monitoreo global de horas, porcentaje de avance por institución y exportación oficial en CSV, JSON y PDF.

### 3.4 Docente Asesor
* **Validación de Bitácoras (CU-05):** Inspecciona las horas reportadas por sus estudiantes asignados y las aprueba o devuelve con recomendaciones.
* **Visitas de Seguimiento (CU-06):** Registra visitas presenciales o virtuales en el colegio/empresa, asistencia del estudiante, observaciones formativas y calificación según rúbrica.

---

## 4. Preguntas Frecuentes
* **¿Por qué un estudiante no puede ser asignado?** Porque debe contar con solicitud aprobada por el Director de Programa.
* **¿Qué sucede al guardar como borrador?** La información se guarda pero no pasa a revisión hasta presionar *Radicar Solicitud Oficial*.