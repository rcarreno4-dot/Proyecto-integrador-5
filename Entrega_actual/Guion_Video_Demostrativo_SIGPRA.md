# Guion Técnico y de Locución - Vídeo Demostrativo SIGPRA
## Sustentación Práctica al 100% de Casos de Uso y Base de Datos
**Duración Máxima:** 5 minutos exactos (00:00 a 05:00)  
**URL de Demostración:** [https://sigpra-udi.onrender.com](https://sigpra-udi.onrender.com)  
**Autores:** Jesús Daniel Rueda Castillo, Alonso Andrés Henao Roa, Rafael Fabián Carreño Barrera  

---

### Resumen de Cuentas de Acceso Rápido
| Rol | Correo | Clave | Momento en el Vídeo |
|---|---|---|---|
| **Estudiante** | Registro autoservicio o `jobixiita@gmail.com` | `123456` | 00:35 - 01:25 |
| **Director** | `director@udi.edu.co` | `123456` | 01:25 - 02:10 |
| **Coordinador** | `coordinador@udi.edu.co` | `123456` | 02:10 - 03:00 y 04:05 - 04:45 |
| **Docente Asesor** | `docente@udi.edu.co` | `123456` | 03:30 - 04:05 |

---

## Cronograma Segundo a Segundo (00:00 - 05:00)

### Bloque 1: Presentación y Arquitectura (00:00 - 00:35 | 35 seg)
* **Pantalla:** Mostrar URL `https://sigpra-udi.onrender.com`, pantalla de inicio corporativa UDI.
* **Locución:** *"Cordial saludo a los docentes evaluadores. Presentamos la sustentación funcional de SIGPRA, Software Integral de Gestión de Prácticas Académicas de la UDI. Desarrollado por Daniel Rueda, Alonso Henao y Fabián Carreño. La aplicación está construida sobre arquitectura MVC en Node.js, TypeScript, PostgreSQL y MongoDB, y se encuentra desplegada y funcionando en vivo en la nube."*

### Bloque 2: Auto-Registro y Radicación de Práctica CU-08 (00:35 - 01:25 | 50 seg)
* **Pantalla:** Clic en `📝 Registrar nueva cuenta de estudiante`. Registrar estudiante, entrar a `Solicitar práctica`, cargar los 3 PDF (matrícula, EPS, paz y salvo) y pulsar `Radicar Solicitud Oficial`.
* **Locución:** *"Comenzamos con el nuevo caso de uso CU-08. Cualquier practicante nuevo puede autorregistrarse desde el botón de la página de inicio. Una vez dentro de su perfil, accede a 'Solicitar práctica', selecciona su modalidad institucional, anexa sus soportes digitales obligatorios en PDF y radica la solicitud formal. El sistema valida la integridad de los archivos y fija el estado en Pendiente de Revisión."*

### Bloque 3: Evaluación Académica del Director CU-08 (01:25 - 02:10 | 45 seg)
* **Pantalla:** Entrar con `director@udi.edu.co`. Ir a `Solicitudes de práctica`. Abrir modal de dictamen con botón `Evaluar`. Mostrar regla de justificación en devolución y presionar `Aprobar Solicitud`.
* **Locución:** *"Cambiamos al rol del Director de Programa, quien ejerce la autoridad académica. En su panel de Solicitudes ve las postulaciones radicadas. Al hacer clic en 'Evaluar', inspecciona los soportes. El sistema implementa una regla estricta: si se devuelve o rechaza, exige justificación obligatoria. En este caso dictamina favorablemente y aprueba. Al aprobar, el estudiante queda inmediatamente habilitado para ser asignado."*

### Bloque 4: Periodos, Convenios y Asignación CU-01, CU-02, CU-03 (02:10 - 03:00 | 50 seg)
* **Pantalla:** Entrar con `coordinador@udi.edu.co`. Mostrar `Periodos` (CU-01), `Convenios y Plazas` (CU-02) con cupos, y en `Asignaciones` (CU-03) asignar al estudiante habilitado a su plaza y tutor.
* **Locución:** *"Ingresamos ahora como Coordinador de Prácticas. En CU-01 verificamos la programación del periodo activo. En CU-02 controlamos la vigencia jurídica de los convenios y el saldo de cupos en tiempo real. En CU-03 vinculamos al estudiante previamente habilitado por el Director con una plaza disponible y le asignamos su docente asesor. El sistema valida automáticamente que existan cupos antes de confirmar."*

### Bloque 5: Bitácora de Campo del Estudiante CU-04 (03:00 - 03:30 | 30 seg)
* **Pantalla:** Entrar como practicante. Ir a `Registrar actividad` (CU-04). Registrar fecha, planeación, 4 horas y cargar evidencia.
* **Locución:** *"El estudiante inicia sus labores en la institución y reporta periódicamente su bitácora en CU-04. Registra fecha, tipo de labor pedagógica, número de horas y carga su evidencia digital. La actividad queda registrada con trazabilidad inmediata para revisión docente."*

### Bloque 6: Validación y Visita Evaluativa CU-05 y CU-06 (03:30 - 04:05 | 35 seg)
* **Pantalla:** Entrar con `docente@udi.edu.co`. En `Validar bitácoras` (CU-05) aprobar horas. En `Visitas de seguimiento` (CU-06) registrar visita in situ presencial/virtual con rúbrica cuantitativa.
* **Locución:** *"El Docente Asesor ingresa para hacer la supervisión académica. En CU-05 revisa las actividades y aprueba las horas válidas. En CU-06 registra la visita de acompañamiento presencial o virtual, dejando constancia de la asistencia, observaciones pedagógicas y la calificación cuantitativa mediante rúbrica institucional."*

### Bloque 7: Consolidado de Base de Datos y Auditoría CU-07 (04:05 - 04:45 | 40 seg)
* **Pantalla:** Entrar a `Consolidado` (CU-07). Mostrar indicadores de horas, filtrar por institución, presionar `Exportar CSV` y mostrar la tabla de auditoría.
* **Locución:** *"Finalmente, en CU-07 tanto el Coordinador como el Director acceden al reporte consolidado de base de datos. Se visualizan indicadores en tiempo real: horas aprobadas, cumplimiento de metas y visitas realizadas. La información puede filtrarse y exportarse en CSV, JSON o imprimirse en PDF para procesos de autoevaluación ante el CNA. Además, el módulo de auditoría garantiza la inmutabilidad de cada cambio de estado."*

### Bloque 8: Cierre y Despedida (04:45 - 05:00 | 15 seg)
* **Pantalla:** Créditos finales y agradecimiento.
* **Locución:** *"Con esto demostramos el 100% de los 8 casos de uso y la operatividad de nuestra arquitectura web y base de datos relacional y documental. Muchas gracias por su atención."*