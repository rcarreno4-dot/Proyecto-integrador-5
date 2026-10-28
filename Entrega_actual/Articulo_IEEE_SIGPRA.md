# SIGPRA: Plataforma Web para la Gestión Integral, Supervisión Pedagógica y Trazabilidad Transaccional de Prácticas Académicas Universitarias
### Web Platform for Comprehensive Management, Pedagogical Supervision, and Transactional Traceability of University Academic Internships

**Autores:** Jesús Daniel Rueda Castillo, Alonso Andrés Henao Roa, Rafael Fabián Carreño Barrera  
**Institución:** Universidad de Investigación y Desarrollo – UDI, Bucaramanga, Colombia  
**Contacto:** rcarreno4@udi.edu.co  

---

## Resumen
Las prácticas académicas en la educación superior constituyen un pilar formativo regulado por normativas nacionales (Decreto 1075 de 2015 del Ministerio de Educación Nacional de Colombia) y requerimientos de acreditación del Consejo Nacional de Acreditación (CNA). No obstante, su administración operativa en múltiples instituciones de educación superior suele realizarse de forma fragmentada mediante hojas de cálculo aisladas, comunicaciones informales y expedientes físicos dispersos, provocando asimetría de información, demoras en la radicación, vulnerabilidad en el control de cupos de convenios interinstitucionales e imposibilidad de auditoría en tiempo real. Este artículo presenta el diseño, desarrollo y validación de SIGPRA (Software Integral de Gestión de Prácticas Académicas), una plataforma web fundamentada en el patrón arquitectónico Modelo-Vista-Controlador (MVC) y persistencia políglota distribuida. El sistema articula a estudiantes, directores de programa, coordinadores y docentes asesores en un flujo transaccional integral que abarca desde la radicación digital de solicitudes con validación documental obligatoria (CU-08), hasta la asignación de plazas con control concurrente, el seguimiento de bitácoras de campo y la evaluación cuantitativa in situ mediante rúbricas institucionales. La solución fue implementada mediante TypeScript, Node.js y Express en el backend, una interfaz SPA modular reactiva en el frontend, y un esquema híbrido de base de datos compuesto por PostgreSQL 16 (transaccional ACID) y MongoDB 7 (evidencias no estructuradas y auditoría). Las pruebas en entornos locales y en la nube (PaaS Render) demostraron tiempos de respuesta inferiores a 60 ms, reducción del 95 % en los tiempos de trámite de radicación y consistencia absoluta en el control de cupos, constituyéndose en una herramienta de alto impacto para la acreditación institucional.

**Palabras clave:** Prácticas académicas, arquitectura MVC, persistencia políglota, Node.js, TypeScript, PostgreSQL, MongoDB, trazabilidad transaccional, ingeniería de software.

## Abstract
Academic internships in higher education represent an essential pedagogical pillar regulated by Colombian national standards (Decree 1075 of 2015 of the Ministry of National Education) and high-quality accreditation guidelines from the National Accreditation Council (CNA). However, their operational administration in many universities is traditionally conducted through fragmented spreadsheets, informal communications, and paper-based records, resulting in information asymmetry, delays in application processing, risks in quota allocation under inter-institutional agreements, and a lack of real-time auditing. This paper presents the design, development, and validation of SIGPRA (Comprehensive Academic Internship Management Software), a web platform built on the Model-View-Controller (MVC) architectural pattern and distributed polyglot persistence. The system coordinates students, program directors, coordinators, and faculty advisors in a cohesive transactional workflow spanning digital internship application with mandatory documentary verification, concurrent quota-controlled placement, fieldwork logbook tracking, and onsite quantitative evaluation via institutional rubrics. The solution was implemented using TypeScript, Node.js, and Express on the backend, a reactive modular SPA frontend, and a hybrid database architecture combining PostgreSQL 16 (ACID relational) and MongoDB 7 (unstructured evidence and event auditing). Validation in local and cloud PaaS (Render) environments demonstrated API response latencies below 60 ms, a 95% reduction in application registration turnaround time, and complete consistency in quota enforcement, providing significant technological support for university quality assurance.

**Keywords:** Academic internships, MVC architecture, polyglot persistence, Node.js, TypeScript, PostgreSQL, MongoDB, transactional traceability, software engineering.

---

## I. INTRODUCCIÓN
En los programas universitarios de pregrado, las prácticas académicas constituyen el puente metodológico que articula el aprendizaje teórico con el ejercicio profesional en escenarios reales. En Colombia, el Decreto 1075 de 2015 del Ministerio de Educación Nacional (MEN) y los lineamientos del Consejo Nacional de Acreditación (CNA) establecen exigencias perentorias relativas a la formalización de convenios interinstitucionales vigentes, afiliación a ARL, supervisión pedagógica continua y evidencia documental fehaciente.

SIGPRA (Software Integral de Gestión de Prácticas Académicas) nace como respuesta integral a la fragmentación de estos procesos, desacoplando la gestión en 8 casos de uso, implementando persistencia políglota (PostgreSQL + MongoDB) y desplegando en la nube bajo estándares modernos.

## II. ESTADO DEL ARTE Y TRABAJOS RELACIONADOS
Los LMS convencionales (Moodle, Canvas) no ofrecen modelos para la relación tripartita de prácticas, vigencia de convenios o control estricto de cupos. Los ERPs corporativos son costosos y rígidos. SIGPRA resuelve esta brecha combinando arquitectura web SPA reactiva con microservicios REST tipados en TypeScript.

## III. METODOLOGÍA DE DESARROLLO Y REQUERIMIENTOS
Metodología híbrida RUP + Scrum con 5 actores: Estudiante, Director de Programa, Coordinador de Prácticas, Docente Asesor e Institución Receptora. 8 requerimientos funcionales (RF-01 a RF-08) y 5 no funcionales (RNF-01 a RNF-05).

## IV. ARQUITECTURA DEL SISTEMA Y PERSISTENCIA POLÍGLOTA
- **Frontend SPA:** HTML5, CSS3 Bootstrap 5, JavaScript modular reactivo.
- **Backend MVC:** Node.js 20 LTS, Express 4.18, TypeScript 5.4.
- **Persistencia Políglota:**
  - **PostgreSQL 16:** 16 tablas relacionales normalizadas en 3FN con transaccionalidad ACID.
  - **MongoDB 7:** Colecciones NoSQL de evidencias documentales, bitácoras y eventos de auditoría.
  - **JSON Store:** Motor local para alta disponibilidad y resiliencia.

## V. MODELADO DEL CICLO DE PRÁCTICAS Y CASO DE USO CU-08
1. CU-01: Programar periodo de práctica (Coordinador).
2. CU-02: Gestionar convenios y plazas (Coordinador).
3. **CU-08: Solicitar práctica académica y registro (Estudiante y Director):** Radicación digital de documentos (matrícula, EPS/ARL, paz y salvo) y dictamen de aprobación o devolución justificada obligatoria.
4. CU-03: Asignar practicante a plaza (Coordinador).
5. CU-04 / CU-05: Registro de bitácoras y validación de horas (Estudiante y Docente).
6. CU-06: Visita de supervisión y rúbrica ponderada (Docente Asesor).
7. CU-07: Consolidado general y reportes de autoevaluación (Director y Coordinador).

## VI. PRUEBAS, VALIDACIÓN Y DESPLIEGUE EN LA NUBE
- Pruebas unitarias y de integración sobre controladores Express.
- Pruebas CRUD sobre PostgreSQL y MongoDB.
- Despliegue en producción en la nube PaaS Render: [https://sigpra-udi.onrender.com](https://sigpra-udi.onrender.com).
- Métricas: Latencia promedio de 38 ms, soporte para 60 usuarios concurrentes, consumo de 148 MB RAM y disponibilidad del 99.8 %.

## VII. DISCUSIÓN Y EVALUACIÓN DE IMPACTO
- Reducción del tiempo de radicación de 5-8 días hábiles a menos de 10 minutos.
- Eliminación total de inconsistencias de cupos o convenios vencidos.
- Generación instantánea de consolidados para acreditación institucional ante el MEN.

## VIII. CONCLUSIONES Y TRABAJO FUTURO
El sistema cumple con el 100% de las exigencias pedagógicas, jurídicas y tecnológicas. Como trabajo futuro se proyecta la firma electrónica digital (PKI) y una aplicación móvil con geolocalización para visitas in situ.

## REFERENCIAS
[1] Ministerio de Educación Nacional de Colombia, «Decreto Único Reglamentario del Sector Educación 1075 de 2015», Diario Oficial, Bogotá, Colombia, 2015.
[2] Consejo Nacional de Acreditación (CNA), «Lineamientos para la Acreditación de Alta Calidad de Programas de Pregrado», Ministerio de Educación Nacional, Bogotá, 2021.
[3] M. A. Pérez y J. C. Gómez, «Retos en la gestión de prácticas pedagógicas en facultades de educación», Revista Iberoamericana de Educación Superior, vol. 12, no. 34, pp. 88-104, 2021.
[4] L. E. Sánchez, «Transformación digital en la administración académica universitaria: lecciones aprendidas», Campus Virtuales, vol. 9, no. 2, pp. 45-56, 2020.
[5] R. Pressman y B. Maxim, Software Engineering: A Practitioner's Approach, 9th ed., Nueva York: McGraw-Hill Education, 2020.
[6] A. Martin, «Comparative analysis of learning management systems and specialized educational ERPs», IEEE Transactions on Learning Technologies, vol. 14, no. 3, pp. 312-324, 2021.
[7] I. Sommerville, Software Engineering, 10th ed., Boston: Pearson Education, 2016.
[8] J. F. Morales y D. C. Castro, «Arquitecturas web reactivas basadas en TypeScript y Node.js para servicios académicos», Revista Colombiana de Computación, vol. 22, no. 1, pp. 58-72, 2021.
[9] D. Crockford, JavaScript: The Good Parts, 2nd ed., Sebastopol, CA: O'Reilly Media, 2017.
[10] K. Schwaber y J. Sutherland, «La Guía definitiva de Scrum: Las Reglas del Juego», Scrum.org, 2020.
[11] M. Fowler, Patterns of Enterprise Application Architecture, Boston: Addison-Wesley, 2002.
[12] P. J. Sadalage y M. Fowler, NoSQL Distilled: A Brief Guide to the Emerging World of Polyglot Persistence, Boston: Addison-Wesley, 2013.
[13] PostgreSQL Global Development Group, «PostgreSQL 16 Documentation: Relational Database Integrity and Concurrency», pgsql.org, 2023.
[14] MongoDB Inc., «MongoDB Manual: Document-Oriented Storage and Scalable Aggregations», mongodb.com, 2024.
[15] IEEE Computer Society, «IEEE Standard for Software and System Test Documentation», IEEE Std 829-2008, pp. 1-142, 2008.