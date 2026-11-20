# SIGPRA: una aplicación web para gestionar las prácticas pedagógicas de las licenciaturas de la Universidad de Investigación y Desarrollo[^1]

# SIGPRA: a web application for managing teaching practicums in the education programs of Universidad de Investigación y Desarrollo

Jesús Daniel Rueda Castillo[^2], Alonso Andrés Henao Roa[^3], Rafael Fabián Carreño Barrera[^4]

Artículo entregado el 22 de noviembre de 2026

Este artículo puede compartirse bajo la [Licencia Creative Commons Atribución-NoComercial-CompartirIgual 4.0 Internacional](https://creativecommons.org/licenses/by-nc-sa/4.0/deed.es) y se referencia usando el siguiente formato: Rueda Castillo, J. D., Henao Roa, A. A. & Carreño Barrera, R. F. (2026). SIGPRA: una aplicación web para gestionar las prácticas pedagógicas de las licenciaturas de la Universidad de Investigación y Desarrollo. *I+D Revista de Investigaciones*, vol. (núm.), pp-pp. DOI:

---
# Resumen

En los programas de licenciatura, la práctica pedagógica obliga a coordinar a personas que no comparten oficina ni horario: el coordinador que programa el periodo y consigue las plazas, los colegios que firman los convenios, los estudiantes que reportan sus horas y los docentes asesores que visitan y evalúan. En la Universidad de Investigación y Desarrollo (UDI) esa información suele quedar repartida entre correos, archivos personales y formatos en papel. Este artículo presenta SIGPRA, una aplicación web que construimos durante el Proyecto Integrador V para apoyar ese proceso desde la programación del periodo hasta el consolidado final. Seguimos un modelo en cascada de cinco fases, definimos siete requerimientos funcionales y ocho casos de uso, y diseñamos una arquitectura orientada a servicios con cuatro servicios que se comunican por REST. La versión entregada está hecha con Node.js, Express y TypeScript bajo el patrón MVC, ofrece una interfaz para cada rol y está publicada en la nube. El modelo de datos se montó en PostgreSQL, con 16 tablas, y en MongoDB, con tres colecciones, y se verificó con pruebas CRUD. La aplicación cubre los ocho casos de uso del alcance. Algunas decisiones del diseño, como desplegar cada servicio por separado o trabajar directamente sobre el esquema relacional, quedaron como trabajo futuro.

**Palabras clave:** práctica pedagógica, ingeniería de software, aplicación web, arquitectura orientada a servicios, gestión académica.

---
# Abstract

In teacher education programs, the teaching practicum requires coordinating people who do not share an office or a schedule: the coordinator who plans the term and secures placements, the schools that sign the agreements, the students who report their hours, and the faculty advisors who visit and assess them. At Universidad de Investigación y Desarrollo (UDI), this information tends to be spread across e-mail, personal files and paper forms. This article presents SIGPRA, a web application we built during the Integrative Project V course to support the process from term planning to the final consolidated report. We followed a five-phase waterfall model, defined seven functional requirements and eight use cases, and designed a service-oriented architecture with four services that communicate through REST. The delivered version is built with Node.js, Express and TypeScript following the MVC pattern, offers a separate interface for each role and is deployed in the cloud. The data model was set up in PostgreSQL, with 16 tables, and in MongoDB, with three collections, and it was verified with CRUD tests. The application covers the eight use cases in scope. Some design decisions, such as deploying each service independently or working directly on the relational schema, were left as future work.

**Keywords:** teaching practicum, software engineering, web application, service-oriented architecture, academic management.

---
# Introducción

La práctica pedagógica es el momento en que el estudiante de licenciatura sale de la universidad y empieza a dar clase en un colegio real, acompañado por un docente asesor y por la institución que lo recibe. No es una asignatura como las demás. Las horas que cumple tienen valor normativo, porque con ellas el programa demuestra las condiciones de calidad que recoge el Decreto Único Reglamentario del Sector Educación (Ministerio de Educación Nacional \[MEN\], 2015), y esa misma información se revisa después en la autoevaluación del programa y en los procesos de acreditación (Consejo Nacional de Acreditación \[CNA\], 2013). Por eso no basta con que el practicante asista. El programa tiene que poder mostrar dónde se hizo la práctica, bajo qué convenio, cuántas horas se aprobaron y quién evaluó al estudiante.

Cuando empezamos a describir el proceso nos llamó la atención la cantidad de personas que intervienen y lo poco que coinciden en tiempo y lugar. El coordinador define el periodo y consigue las plazas; los colegios firman o renuevan los convenios y dicen cuántos practicantes pueden recibir; el estudiante reporta lo que hizo y guarda sus soportes; el docente asesor visita el aula, aprueba las horas y aplica una rúbrica; y al final el director del programa necesita un consolidado para responder ante la decanatura y los pares académicos. Cada uno necesita algo que produce otro, y lo necesita en un momento distinto del semestre.

No queremos decir que las herramientas que se usan hoy estén mal usadas. El correo, las hojas de cálculo y los formatos firmados funcionan bien cuando son pocos los practicantes. La dificultad aparece cuando crecen los estudiantes y los convenios: la información de un mismo practicante queda repartida entre los archivos del coordinador, el correo del docente y lo que guarda el propio estudiante, no hay un solo lugar donde ver qué convenios siguen vigentes y cuántos cupos quedan, y los soportes terminan en computadores personales. Las plataformas de cursos que ya tienen las universidades sirven para entregar trabajos y calificar, pero no están pensadas para manejar convenios con colegios, cupos por plaza o la validación de horas por parte de un docente asesor.

Con ese punto de partida, el objetivo del proyecto fue desarrollar una aplicación web para gestionar las prácticas pedagógicas de las licenciaturas de la UDI, que permitiera programar el periodo, asignar a los practicantes a las instituciones receptoras, registrar y validar las actividades, y hacer el seguimiento y la evaluación por parte del docente asesor. Para lograrlo nos propusimos cuatro objetivos específicos: caracterizar el proceso con sus actores y reglas, diseñar la arquitectura y el modelo de datos, construir la aplicación sobre los requerimientos priorizados y verificarla, junto con su documentación técnica y de usuario. El proyecto reunió tres cursos del quinto semestre: Ingeniería del Software II, Programación III y Teoría General de Sistemas.

En lo que sigue contamos cómo lo hicimos. Primero describimos el tipo de estudio, el equipo, las herramientas y los requerimientos; luego el procedimiento que seguimos en cada entrega; y al final los resultados, con lo que la aplicación hace, lo que se probó y lo que quedó pendiente.

# Metodología

## Tipo de estudio

Se trata de un proyecto de desarrollo tecnológico, de carácter aplicado y descriptivo. No hubo manipulación experimental ni medición con usuarios reales: el producto es un sistema de software, y su verificación se hizo con pruebas funcionales sobre los requerimientos y con pruebas CRUD sobre la base de datos. Como proceso de desarrollo elegimos el modelo en cascada (Royce, 1970; Sommerville, 2016). Lo escogimos por dos razones. El alcance estaba limitado por el calendario académico y no esperábamos que cambiara mucho, y cada entrega del Proyecto Integrador coincidía con el cierre de una fase y tenía productos que el docente podía revisar. Trabajamos en cinco fases: análisis de requerimientos, diseño, construcción, pruebas y documentación y cierre. Cuando una revisión mostraba algo que faltaba, volvíamos a la fase anterior para corregirlo, como admite la versión con realimentación del modelo (Pressman & Maxim, 2020).

## Participantes

No trabajamos con una muestra de personas. El equipo lo formamos tres estudiantes de quinto semestre de Ingeniería de Sistemas de la UDI. Todos participamos en el análisis; después, la documentación quedó sobre todo a cargo de un integrante y el desarrollo y la base de datos a cargo de los otros dos. Los usuarios del sistema se definieron como actores. Cuatro usan directamente la aplicación: el coordinador de prácticas, el director de programa, el docente asesor y el estudiante practicante. Otros dos son externos: la institución receptora y el sistema de información académica de la universidad, que aporta los datos de matrícula.

Para el análisis partimos del enunciado del proyecto entregado por los docentes, de la normativa y de la descripción del proceso que construimos como equipo. Las decisiones se revisaron en seis reuniones de seguimiento entre septiembre y octubre de 2026, que quedaron registradas en actas. Las pruebas se hicieron con una cuenta por rol y con datos ficticios; no se usó información personal de estudiantes reales.

## Herramientas tecnológicas

La Tabla 1 resume las herramientas que usamos. La aplicación entregada tiene un servidor en Node.js con Express y TypeScript, organizado en rutas, controladores, servicios y modelos, y una interfaz de una sola página hecha con HTML5, JavaScript y Bootstrap 5. Para el modelo de datos usamos PostgreSQL 16 y MongoDB 7: la primera para la información que debe mantener relaciones entre sí y la segunda para la que cambia de forma de un caso a otro, como los metadatos de los soportes y las bitácoras de visita (MongoDB, 2024; The PostgreSQL Global Development Group, 2024). En la última etapa se hizo además una segunda interfaz en React 18 y Vite que consume la misma API, pero la versión publicada es la primera.

*Tabla 1.* Herramientas utilizadas en el desarrollo de SIGPRA

| **Herramienta**                   | **Uso en el proyecto**                                   |
|-----------------------------------|----------------------------------------------------------|
| Node.js 20 y Express 4            | Servidor web y API REST bajo /api                        |
| TypeScript 5.4                    | Lenguaje del servidor (patrón MVC)                       |
| HTML5, JavaScript y Bootstrap 5.3 | Interfaz de una sola página con vistas por rol           |
| PostgreSQL 16                     | Esquema relacional y modo de almacenamiento postgres     |
| MongoDB 7                         | Colecciones de soportes, bitácoras y auditoría           |
| Docker Compose                    | Entorno local con la aplicación y las dos bases de datos |
| Render                            | Publicación de la aplicación con HTTPS                   |
| Astah, Graphviz y Mermaid         | Diagramas UML y de arquitectura                          |
| Git, GitHub y GitKraken           | Control de versiones del código y de la documentación    |

## Requerimientos/Requisitos

En la primera versión teníamos requerimientos como “registrar estudiante” o “registrar institución”. En la revisión de la primera entrega entendimos que eso es mantenimiento de datos y no lo que el sistema tiene que resolver, así que replanteamos los requerimientos sobre la gestión de la práctica. Quedaron siete requerimientos funcionales, que se convirtieron en ocho casos de uso modelados con UML (Object Management Group \[OMG\], 2017). El RF-04 se dividió en dos casos de uso porque el registro lo hace el estudiante y la validación el docente asesor, en momentos distintos. La Tabla 2 muestra la relación entre requerimientos, casos de uso y servicios.

*Tabla 2.* Requerimientos funcionales, casos de uso y servicio que los atiende

| **RF** | **Requerimiento**                                   | **CU**       | **Serv.** |
|--------|-----------------------------------------------------|--------------|-----------|
| RF-01  | Programación del periodo de práctica                | CU-01        | S1        |
| RF-02  | Gestión de convenios y plazas                       | CU-02        | S1        |
| RF-03  | Asignación del practicante a plaza y docente asesor | CU-03        | S2        |
| RF-04  | Registro y validación de la práctica ejecutada      | CU-04, CU-05 | S3        |
| RF-05  | Seguimiento y evaluación del practicante            | CU-06        | S3        |
| RF-06  | Consolidado del estado de las prácticas             | CU-07        | S4        |
| RF-07  | Solicitud de práctica y revisión del director       | CU-08        | S2        |

Los requerimientos no funcionales los pensamos a partir de las características de calidad de la norma ISO/IEC 25010 (International Organization for Standardization \[ISO\], 2011), pero solo dejamos los que nacen de una necesidad concreta del proceso. Son cinco: disponibilidad en los días previos al cierre de reportes, capacidad para que al menos 60 practicantes registren actividades al mismo tiempo, registro de visitas desde el celular aunque se pierda la conexión, un registro de auditoría que no se pueda modificar y que se conserve al menos cinco años, y la recuperación de los soportes durante ese mismo tiempo. Además definimos once reglas de negocio; por ejemplo, un estudiante no puede tener dos asignaciones en el mismo periodo, solo las horas aprobadas por el docente asesor suman al avance y no se aceptan actividades con fecha posterior al límite de reporte.

Cada requerimiento se tradujo en requisitos concretos de la aplicación. Al publicar un periodo, los pesos de la rúbrica deben sumar 100 % y la intensidad horaria debe estar entre 1 y 320 horas. Una plaza solo se ofrece si su convenio tiene documento y no ha vencido. Una actividad se acepta si sus horas están entre 0,5 y 24, si su fecha cae dentro del periodo y si tiene al menos un soporte en PDF, JPG, PNG, DOC o DOCX de hasta 10 MB. La nota de cada criterio de la rúbrica va de 0,0 a 5,0. Para devolver o rechazar una solicitud de práctica, el director debe escribir una justificación de al menos diez caracteres.

## Procedimiento

El trabajo se ordenó alrededor de las tres entregas del semestre. Hasta el 30 de agosto de 2026 hicimos el análisis: describimos el proceso en seis momentos, identificamos los actores, revisamos la normativa, levantamos los requerimientos y las reglas de negocio, y modelamos en Astah el diagrama de casos de uso con sus fichas y el diagrama de dominio. También entregamos un primer modelo entidad-relación y un prototipo de mediana fidelidad. Las observaciones del docente en esa entrega nos llevaron a escribir los objetivos sin nombres de productos y a plantear la arquitectura como orientada a servicios.

Entre el 31 de agosto y el 11 de octubre trabajamos en el diseño. Dividimos el sistema en cuatro servicios según la información que maneja cada uno y la frecuencia con que cambia: S1, planeación y convenios; S2, asignación de prácticas; S3, ejecución y seguimiento; y S4, informes y auditoría. Delante de ellos ubicamos una capa intermedia que recibe todas las solicitudes, revisa el rol del usuario y las envía al servicio que corresponde. Tomamos la orientación a servicios como forma de organizar el sistema (Erl, 2016) y REST con mensajes JSON como la manera en que esos servicios se comunican (Fielding, 2000). En esa misma fase elaboramos los diagramas de secuencia y de clases, el modelo relacional y el diccionario de datos, el prototipo de alta fidelidad y los scripts de la base de datos. El 11 de octubre ejecutamos las pruebas CRUD y entregamos un prototipo funcional con la mitad de los casos de uso.

Del 12 de octubre al 22 de noviembre completamos la construcción, las pruebas y la documentación. Aquí tomamos una decisión práctica: por el tiempo disponible, los cuatro servicios quedaron como módulos dentro de un mismo servidor, cada uno con su grupo de rutas, en lugar de desplegarse por separado. El caso de uso CU-08, la solicitud de práctica que revisa el director, se agregó en esta etapa porque sin él no quedaba claro quién habilita al estudiante para ser asignado. Configuramos dos modos de almacenamiento: uno en un archivo JSON y otro en PostgreSQL, donde el estado se guarda en una columna JSONB y cada cambio se hace dentro de una transacción. Publicamos la aplicación en Render, preparamos un entorno local con Docker Compose y elaboramos los diagramas de componentes y de despliegue y los manuales técnico y de usuario. Todo el código y la documentación se manejaron en un repositorio público de GitHub, con más de 180 commits a lo largo del semestre.

# Resultados

## La aplicación entregada

SIGPRA cubre los ocho casos de uso del alcance (Figura 1). El coordinador programa el periodo con su rúbrica, registra instituciones, convenios y plazas, y asigna a cada practicante una plaza y un docente asesor. El estudiante radica su solicitud de práctica con la matrícula, la afiliación a EPS y ARL y el paz y salvo, y recibe un número de radicado. El director revisa esos soportes y aprueba, devuelve o rechaza la solicitud. Una vez asignado, el estudiante registra sus actividades con soportes y puede ver en todo momento cuántas horas lleva aprobadas y cuántas le faltan (Figura 2). El docente asesor valida las actividades y registra las visitas con su evaluación. Al final, el coordinador y el director consultan el consolidado por periodo, programa, institución y docente, lo exportan a CSV o lo imprimen; si quedan actividades sin revisar, el sistema lo advierte y muestra el resultado como parcial.

![Diagrama de casos de uso](../Diagramas%20UML/Clases%20y%20modelos%20editables/Diagrama_De_Casos_de_uso.png)

*Figura 1.* Diagrama de casos de uso de SIGPRA.

![Avance de horas del estudiante](../Vistas_actuales_SIGPRA/Progreso_160_Horas.png)

*Figura 2.* Vista del estudiante con el avance de horas aprobadas frente a la intensidad exigida en el periodo.

Cada rol ve solo las opciones que le corresponden. Los formularios largos se dividieron en etapas numeradas para que el usuario llene una parte a la vez, y debajo de cada formulario aparece la tabla con los registros. La aplicación está publicada en https://sigpra-udi.onrender.com y el código está en el repositorio del proyecto.

## Arquitectura implementada

La Figura 3 muestra cómo quedó la versión entregada. El servidor expone 21 rutas bajo /api, atendidas por nueve controladores y siete servicios, y el acceso a los datos pasa siempre por una misma clase, SigpraStore. Cada grupo de rutas corresponde a uno de los servicios del diseño, lo que nos permitió conservar la separación de responsabilidades aunque el despliegue sea uno solo. La versión pública usa el modo JSON; con una variable de entorno, el mismo código guarda el estado en PostgreSQL.

![Arquitectura de la versión entregada](../Manuales_Sistema/imagenes_manual_tecnico/arquitectura_version_entregada.png)

*Figura 3.* Arquitectura de la versión entregada de SIGPRA.

## Base de datos

El esquema relacional tiene 16 tablas, desde usuario y convenio hasta evaluación y su detalle por criterio. Varias reglas de negocio quedaron como restricciones de la propia base de datos: los cupos ocupados no pueden superar los ofrecidos, las horas de una actividad deben ser mayores que cero y no pasar de 24, la nota de cada criterio va de 0 a 5 y la fecha límite de reporte no puede ser anterior al fin del periodo. Un índice único parcial impide que un estudiante tenga dos asignaciones activas en el mismo periodo, y una vista reúne el estado de cada práctica para los informes. En MongoDB creamos tres colecciones, para evidencias, bitácoras de visita y eventos de auditoría, cada una con un validador de esquema que exige sus campos obligatorios.

Las pruebas CRUD se ejecutaron sobre cuatro tablas básicas: institución, usuario, convenio y plaza de práctica. El script crea un registro en cada una, lo consulta, lo actualiza y lo borra en el orden de sus dependencias, todo dentro de una transacción. La prueba terminó sin errores de restricciones ni de llaves foráneas, y los cuatro conteos finales quedaron en cero, lo que confirma que no se dejaron datos de prueba en la base.

## Aporte de cada curso

De Ingeniería del Software II usamos el análisis y el modelado: la especificación de requerimientos, los casos de uso con sus fichas, los diagramas de secuencia, clases, componentes y despliegue, y la planeación por fases. Fue también el curso que más nos obligó a revisar la trazabilidad entre lo que pedía cada requerimiento y lo que hacía cada pantalla.

De Programación III salió la construcción: el servidor MVC en TypeScript, las rutas de la API, la interfaz por roles, los dos modos de almacenamiento y los scripts de PostgreSQL y MongoDB con sus pruebas.

De Teoría General de Sistemas tomamos la forma de mirar el proceso. Antes de pensar en pantallas identificamos los actores, las entradas y salidas de cada momento, los límites del sistema y lo que queda por fuera, como la institución receptora o el sistema académico (Bertalanffy, 1976). El registro de auditoría y el consolidado parcial cumplen el papel de realimentación: le devuelven al coordinador y al director información sobre el estado real del proceso para que puedan corregirlo a tiempo.

## Limitaciones

La versión entregada no implementa todo lo que se diseñó. Los servicios funcionan como módulos de un solo servidor y no como despliegues independientes. La aplicación guarda su estado en JSON o en una tabla JSONB y todavía no trabaja sobre las 16 tablas ni sobre MongoDB, que están montadas y probadas por separado. Las reglas de negocio se validan en el navegador y las rutas de la API aún no comprueban el rol de quien las llama. Los soportes se registran por su nombre y su ruta prevista, sin subir el archivo al servidor, y las contraseñas no están cifradas. Tampoco hay un sistema de notificaciones: cada usuario ve sus pendientes cuando entra, pero no se envían correos ni avisos a los demás. El registro de auditoría guarda quién hizo cada cambio y cuándo, pero no el valor anterior. En Render, además, el archivo JSON no es permanente: cuando el servicio se reinicia, los datos vuelven a los iniciales. Por último, al revisar el código frente al documento vimos que el coordinador puede asignar a un estudiante cuya solicitud todavía no ha aprobado el director; la lista lo marca como pendiente, pero no lo bloquea.

# Conclusiones

SIGPRA muestra que el proceso de prácticas pedagógicas de la UDI se puede apoyar con una aplicación web en la que el coordinador, el director, los docentes asesores y los estudiantes trabajan sobre la misma información. Los ocho casos de uso del alcance funcionan en la versión publicada, y cada acción importante queda registrada con quién la hizo y cuándo.

Modelar el proceso completo antes de recortar lo que íbamos a construir nos ahorró trabajo. Al tener claros los seis momentos de la práctica, fue fácil ver dónde encajaba cada requerimiento y por qué faltaba el CU-08 cuando apareció la pregunta de quién habilita al estudiante. También aprendimos que la trazabilidad entre el documento y el código no se puede dejar para el final. Revisando, uno por uno, los requerimientos contra lo que hacía la aplicación, encontramos que el cierre del periodo no verificaba las actividades pendientes, cosa que alcanzamos a corregir, y que la asignación no exige la solicitud aprobada, que quedó pendiente.

Separar la arquitectura en servicios desde el diseño, aunque al final quedaran en un solo servidor, dejó el camino listo para el trabajo futuro: desplegar cada servicio por separado, conectar la aplicación al esquema relacional y a MongoDB, mover las validaciones al servidor, cifrar las contraseñas y conectarse con las credenciales institucionales, subir los soportes a un almacenamiento propio, enviar notificaciones y exigir la solicitud aprobada antes de asignar.

# Agradecimientos

Agradecemos a los profesores Jonathan Arley Monsalve Salazar (Ingeniería del Software II), John William Ruiz Zapata (Programación III) y Ricardo Vicente Jaimes Vivas (Teoría General de Sistemas) por las revisiones de cada entrega, que nos hicieron cambiar varias decisiones del proyecto. También agradecemos al programa de Ingeniería de Sistemas de la Universidad de Investigación y Desarrollo (UDI).

# Referencias

Bertalanffy, L. von (1976). *Teoría general de los sistemas: fundamentos, desarrollo, aplicaciones*. México: Fondo de Cultura Económica.

Consejo Nacional de Acreditación. (2013). *Lineamientos para la acreditación de programas de pregrado*. Bogotá: CNA.

Erl, T. (2016). *Service-oriented architecture: Analysis and design for services and microservices* (2.ª ed.). Boston: Prentice Hall.

Fielding, R. T. (2000). *Architectural styles and the design of network-based software architectures* (Tesis doctoral). University of California, Irvine.

International Organization for Standardization. (2011). *ISO/IEC 25010:2011. Systems and software engineering: Systems and software Quality Requirements and Evaluation (SQuaRE). System and software quality models*. Ginebra: ISO.

Ministerio de Educación Nacional. (2015). *Decreto 1075 de 2015, por medio del cual se expide el Decreto Único Reglamentario del Sector Educación*. Bogotá: MEN.

MongoDB. (2024). *Schema validation*. Recuperado de https://www.mongodb.com/docs/manual/core/schema-validation/

Object Management Group. (2017). *OMG Unified Modeling Language (OMG UML), version 2.5.1*. Recuperado de https://www.omg.org/spec/UML/2.5.1/

Pressman, R. S. & Maxim, B. R. (2020). *Software engineering: A practitioner’s approach* (9.ª ed.). Nueva York: McGraw-Hill Education.

Royce, W. W. (1970). Managing the development of large software systems. *Proceedings of IEEE WESCON*, 26, 1-9.

Sommerville, I. (2016). *Software engineering* (10.ª ed.). Boston: Pearson.

The PostgreSQL Global Development Group. (2024). *PostgreSQL 16 documentation*. Recuperado de https://www.postgresql.org/docs/16/

[^1]: Artículo de desarrollo tecnológico, enfoque cualitativo, resultado de un proyecto de aula culminado (Proyecto Integrador V), perteneciente al área de Ingeniería de Sistemas, subárea de Ingeniería de Software, desarrollado en el programa de Ingeniería de Sistemas de la Universidad de Investigación y Desarrollo (Bucaramanga, Colombia) con los cursos Ingeniería del Software II, Programación III y Teoría General de Sistemas. Fecha de inicio: agosto de 2026. Fecha de terminación: noviembre de 2026.

[^2]: Estudiante de quinto semestre de Ingeniería de Sistemas, Universidad de Investigación y Desarrollo (Bucaramanga, Colombia). Correo electrónico institucional: jrueda51@udi.edu.co.

[^3]: Estudiante de quinto semestre de Ingeniería de Sistemas, Universidad de Investigación y Desarrollo (Bucaramanga, Colombia). Correo electrónico institucional: ahenao2@udi.edu.co.

[^4]: Estudiante de quinto semestre de Ingeniería de Sistemas, Universidad de Investigación y Desarrollo (Bucaramanga, Colombia). Correo electrónico institucional: rcarreno4@udi.edu.co.
