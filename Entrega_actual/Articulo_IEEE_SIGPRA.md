# SIGPRA: Plataforma Web para la Gestión Integral, Supervisión Pedagógica y Trazabilidad Transaccional de Prácticas Académicas Universitarias

**Autores:** Jesús Daniel Rueda Castillo, Alonso Andrés Henao Roa, Rafael Fabián Carreño Barrera  
**Institución:** Programa de Ingeniería de Sistemas — Facultad de Ingenierías, Universidad de Investigación y Desarrollo (UDI), Bucaramanga, Colombia  
**Contacto:** `{jrueda, ahenao, rcarreno4}@udi.edu.co`  
**Artículo Oficial Formato IEEE (Quinto Semestre - Proyecto Integrador V)**

---

## Resumen
Las prácticas académicas en la educación superior constituyen un pilar formativo regulado por normativas nacionales (Decreto 1075 de 2015 del Ministerio de Educación Nacional de Colombia) y requerimientos de acreditación del Consejo Nacional de Acreditación (CNA). No obstante, su administración operativa suele realizarse de forma fragmentada mediante hojas de cálculo aisladas y formatos físicos dispersos. Este artículo presenta el diseño, desarrollo y validación de SIGPRA (Software Integral de Gestión de Prácticas Académicas), una plataforma web fundamentada en el patrón arquitectónico Modelo-Vista-Controlador (MVC) y persistencia políglota distribuida (PostgreSQL 16 y MongoDB 7) con despliegue cloud en Render.

**Palabras clave:** Prácticas académicas, arquitectura MVC, persistencia políglota, Node.js, TypeScript, PostgreSQL, MongoDB, trazabilidad transaccional, ingeniería de software.

---

## I. INTRODUCCIÓN
En los programas universitarios de pregrado, las prácticas académicas constituyen el puente metodológico que articula el aprendizaje teórico con el ejercicio profesional. En Colombia, el Decreto 1075 de 2015 exige convenios formalizados, afiliación a riesgos laborales (ARL), supervisión pedagógica continua y evidencia fehaciente de horas. SIGPRA resuelve la desarticulación operativa formalizando 8 casos de uso desacoplados, radicación digital con validación documental obligatoria (CU-08) y persistencia políglota distribuida.

---

## II. TRABAJOS RELACIONADOS Y ESTADO DEL ARTE
Comparativa con plataformas LMS (Moodle, Canvas) y ERPs universitarios (SAP Campus, Ellucian Banner). Las soluciones existentes carecen de soporte nativo para convenios tripartitas, rúbricas de supervisión in situ y radicación documental ágil para estudiantes.

---

## III. METODOLOGÍA DE DESARROLLO Y REQUERIMIENTOS
Metodología híbrida RUP + Scrum en 4 fases canónicas. Cinco actores: Estudiante Practicante, Director de Programa, Coordinador de Prácticas, Docente Asesor UDI y Tutor Institucional. Especificación de 8 Requisitos Funcionales (RF-01 a RF-08) y 5 Requisitos No Funcionales (RNF-01 a RNF-05).

---

## IV. ARQUITECTURA DEL SISTEMA Y PERSISTENCIA POLÍGLOTA
Patrón MVC desacoplado en tres capas:
1. **Presentación:** SPA reactiva modular en TypeScript y TailwindCSS.
2. **Negocio:** Backend REST en Node.js 20 LTS / Express 4.18 con controladores especializados.
3. **Persistencia Políglota:** PostgreSQL 16 (16 tablas relacionales ACID) + MongoDB 7 (evidencias documentales y auditoría BSON) + Storage Engine JSON autónomo.

### TABLA I. DESGLOSE TÉCNICO DE LA ARQUITECTURA POLÍGLOTA SIGPRA
| Capa Arquitectónica | Tecnologías y Estándares | Propósito en SIGPRA |
|---|---|---|
| Presentación | TypeScript, TailwindCSS, HTML5 | SPA interactiva reactiva multiplataforma |
| Lógica de Negocio | Node.js 20, TypeScript, Express | Controladores MVC, validación y APIs REST |
| Datos Transaccionales | PostgreSQL 16 (Relacional ACID) | Integridad de convenios, cupos y notas |
| Datos Documentales | MongoDB 7 (NoSQL BSON) | Evidencias digitales y bitácoras de auditoría |

---

## V. MODELADO DEL CICLO DE PRÁCTICAS Y CU-08
Formalización de los 8 Casos de Uso del ciclo de vida. Flujo detallado de radicación digital (CU-08): carga obligatoria de 3 soportes PDF (matrícula, EPS/ARL, paz y salvo), evaluación y dictamen vinculante por el Director de Programa.

### TABLA II. MATRIZ DE CASOS DE USO DEL SISTEMA SIGPRA
| Caso de Uso | Actor(es) Principal(es) | Requisito / Impacto |
|---|---|---|
| CU-01 Programar periodo | Coordinador | RF-01: Apertura oficial de ciclo |
| CU-02 Gestionar convenios | Coordinador | RF-02: Control de vigencia y cupos |
| CU-08 Solicitar práctica | Estudiante, Director | RF-08: Radicación y validación de requisitos |
| CU-03 Asignar plaza | Coordinador | RF-03: Emparejamiento estudiante-empresa |
| CU-04 Registrar bitácora | Estudiante | RF-04: Bitácora periódica de horas |
| CU-05 Validar bitácora | Docente Asesor / Tutor | RF-04: Aprobación formal de horas |
| CU-06 Realizar visita | Docente Asesor | RF-05: Calificación rúbrica in situ |
| CU-07 Consolidar notas | Director, Coordinador | RF-06: Métricas y reportes MEN/CNA |

---

## VI. PRUEBAS, VALIDACIÓN Y DESPLIEGUE CLOUD
Pruebas unitarias de endpoints REST, pruebas transaccionales ACID en PostgreSQL y despliegue continuo contenerizado en la plataforma PaaS Render (https://sigpra-udi.onrender.com) con certificación SSL/TLS.

### TABLA III. RESULTADOS DE RENDIMIENTO Y PRUEBAS EN PRODUCCIÓN
| Métrica / Indicador | Valor Obtenido | Estándar de Aceptación |
|---|---|---|
| Latencia API REST (Promedio) | 38 ms | < 150 ms (Excelente) |
| Carga inicial de SPA en Render | 1.2 segundos | < 3.0 segundos |
| Concurrencia de usuarios | 60 sesiones activas | >= 50 usuarios concurrentes |
| Consumo de memoria RAM backend | 148 MB | < 512 MB (Plan Free Cloud) |
| Tasa de éxito transaccional | 99.8 % | >= 99.0 % |

---

## VII. DISCUSIÓN Y EVALUACIÓN DE IMPACTO
Reducción del 95 % en los tiempos de radicación de solicitudes (de 8 días a 10 minutos). Eliminación total de contingencias legales por control automático de convenios y cobertura ARL.

---

## VIII. CONCLUSIONES Y TRABAJO FUTURO
El enfoque de arquitectura desacoplada y persistencia políglota demostró alta robustez para la gestión de prácticas pedagógicas universitarias. Trabajo futuro: firma digital con certificados PKI/X.509, integración OAuth2 con Microsoft 365 institucional y emparejamiento con IA.

---

## REFERENCIAS
[1] MEN Colombia, Decreto 1075 de 2015.  
[2] CNA, Lineamientos de Acreditación de Alta Calidad, 2021.  
[3] Pressman & Maxim, Software Engineering, 9th ed., McGraw-Hill, 2020.  
[4] Sommerville, Software Engineering, 10th ed., Pearson, 2016.  
[5] Cavus, Procedia - Social and Behavioral Sciences, 2015.  
[6] Martin et al., IEEE Access, 2020.  
[7] Lwoga, Information Technology & People, 2014.  
[8] Fowler, Patterns of Enterprise Application Architecture, 2002.  
[9] Pramod & Sadalage, NoSQL Distilled, 2012.  
[10] Schwaber & Sutherland, Guía Definitiva de Scrum, 2020.  
[11] Gamma et al., Design Patterns, 1994.  
[12] Tilkov & Vinoski, IEEE Internet Computing, 2010.  
[13] PostgreSQL Documentation v16, 2023.  
[14] MongoDB Manual v7.0, 2023.  
[15] Render Cloud Platform Documentation, 2024.  
[16] Hevery & Green, IEEE Software, 2014.
