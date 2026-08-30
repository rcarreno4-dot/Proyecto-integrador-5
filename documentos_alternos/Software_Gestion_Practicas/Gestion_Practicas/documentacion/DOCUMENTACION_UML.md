# Documentación UML - Software para la Gestión de Prácticas Académicas

## Diagrama de despliegue

El diagrama de despliegue representa la distribución física y lógica de los elementos que conforman el Software para la Gestión de Prácticas Académicas. Su propósito es mostrar cómo se ejecuta la aplicación, qué nodos participan en el funcionamiento del sistema, cómo se comunican entre sí y dónde se encuentran desplegados los principales artefactos de software.

El sistema se fundamenta en una arquitectura Modelo-Vista-Controlador (MVC), implementada en Java con NetBeans y conectada a una base de datos Oracle XE 10g. La aplicación ejecuta una interfaz Java Swing, procesa las operaciones por medio de controladores y lógica de negocio, accede a la información mediante clases DAO y centraliza la conexión en la clase `ConexionBD.java` usando el driver `ojdbc14.jar`.

### Nodos principales

**Cliente / Usuario:** representa el equipo desde el cual interactúan los actores del sistema: Director, Coordinador, Docente, Estudiante e Institución Receptora. Incluye la interfaz Swing, login por rol y los paneles principales.

**Equipo de Ejecución Java:** representa el ambiente de ejecución del sistema. Contiene el archivo `GestionPracticas.jar`, la JVM/JDK y los paquetes de la aplicación.

**Aplicación GestionPracticas:** representa el núcleo lógico del sistema. Contiene controladores MVC, lógica de negocio, DAO, modelo, `ConexionBD.java`, driver `ojdbc14` y generador de reportes.

**Servidor de Base de Datos:** representa Oracle Database XE 10g con el esquema `PROYECTOJD`, tablas, claves primarias, claves foráneas, secuencias, funciones, procedimientos y disparadores.

**Repositorio y Respaldo:** representa el control de versiones y respaldo mediante GitHub, scripts SQL y copias de seguridad.

**Salida:** representa la generación de reportes en PDF, Excel e impresión.

### Flujo de comunicación

El flujo principal del sistema es:

**Vista → Controlador → Lógica de Negocio → DAO → ConexionBD.java → Oracle**

Este flujo mantiene separadas las responsabilidades del sistema y evita que las vistas accedan directamente a la base de datos.

## Diagrama de componentes

El diagrama de componentes representa la organización interna del software. A diferencia del diagrama de despliegue, que muestra dónde se ejecuta el sistema, el diagrama de componentes muestra cómo está construido internamente.

### Componentes principales

**Vista:** agrupa las interfaces gráficas en Java Swing, como `LoginForm`, `DirectorDashboard`, `CoordinadorDashboard`, `DocenteDashboard`, `EstudianteDashboard`, `InstitucionDashboard` y los formularios CRUD.

**Controlador MVC:** coordina los eventos de los botones, la navegación entre ventanas, la validación inicial y la redirección según el rol autenticado.

**Lógica de Negocio:** contiene reglas del sistema como autenticación, validación de usuarios, registro de prácticas, control de grupos, evaluación, rúbricas y actividades.

**DAO:** contiene las clases de acceso a datos encargadas de ejecutar operaciones CRUD y consultas SQL contra Oracle.

**Modelo:** contiene las clases que representan las entidades del dominio, como Usuario, Programa, Curso, Practica, Grupo, Institucion, MatriculaPractica, Evaluacion y Rubrica.

**Utilidades:** contiene clases auxiliares como `ConexionBD.java` y `Utilidades.java`.

**Reportes:** genera salidas en PDF y Excel para el Director del Programa.

**Base de Datos Oracle:** almacena las tablas del sistema, secuencias, funciones, procedimientos y triggers.

### Relaciones entre componentes

- La Vista depende del Controlador.
- El Controlador depende de la Lógica de Negocio.
- La Lógica de Negocio depende de los DAO.
- Los DAO dependen de `ConexionBD.java`.
- `ConexionBD.java` se comunica con Oracle mediante JDBC.
- El Modelo es utilizado por Vista, Controlador, Lógica y DAO.
- Reportes consulta información mediante DAO y genera archivos PDF o Excel.

## Justificación

Ambos diagramas evidencian que el sistema fue diseñado de forma modular, con separación de responsabilidades y una estructura coherente con la arquitectura MVC. Esta organización facilita el mantenimiento del código, la validación de requerimientos, la conexión con Oracle y la ampliación futura de módulos como reportes, auditoría, evaluación con rúbrica y control de horas.
