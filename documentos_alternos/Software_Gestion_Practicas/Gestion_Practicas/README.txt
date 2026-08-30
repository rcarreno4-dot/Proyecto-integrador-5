Sistema_Gestion_Practicas_v1.0
================================

Proyecto Java Swing + Oracle para la Gestión de Prácticas Académicas.

Tecnologías especificadas:
- JDK recomendado: 26. El código mantiene compatibilidad de compilación con JDK 21+ y nivel fuente 11 para facilitar ejecución en NetBeans.
- IDE recomendado: NetBeans 17 o superior.
- Base de datos: Oracle 10g o superior / SQL*Plus.
- Driver incluido: lib/ojdbc14.jar.
- PDF incluido: lib/itextpdf-5.5.13.3.jar.
- Arquitectura: Vista, Controlador/Formularios, Servicio, DAO, Modelo y Util.

Ejecución rápida en NetBeans:
1. Descomprima este ZIP.
2. Abra NetBeans > File > Open Project y seleccione la carpeta Sistema_Gestion_Practicas_v1.0.
3. Verifique que lib/ojdbc14.jar y lib/itextpdf-5.5.13.3.jar estén en Libraries.
4. Edite resources/config.properties o config/config.properties con su conexión Oracle.
   Ejemplo: jdbc:oracle:thin:@localhost:1521:XE
5. En SQL*Plus conecte el esquema del proyecto y ejecute:
   cd sql
   @instalar_todo.sql
6. En NetBeans ejecute Clean and Build y luego Run.

Usuarios de prueba:
- Director: admin@proyectojd.com / 123456
- Coordinador: carlos@proyectojd.com / 123456
- Docente: maria@proyectojd.com / 123456
- Estudiante: juan@proyectojd.com / 123456
- Institución: empresa@proyectojd.com / 123456

Estructura principal:
- src/          Código fuente Java.
- lib/          Librerías JDBC Oracle e iTextPDF.
- resources/    Configuración portable del sistema.
- sql/          Scripts completos: esquema 39 objetos, datos, funciones, procedimientos, triggers y validación.
- docs/         Manual de usuario, manual técnico, guía de despliegue, diccionario y material comercial.
- diagramas/    Diagramas de componentes y despliegue en PlantUML/PNG.
- dist/         JAR generado por Ant.

Supuestos documentados:
La base fue solicitada con nombres de tablas existentes, pero sin diccionario completo de columnas. Por eso el script crea un esquema Oracle compatible con las convenciones indicadas y con alias de columnas usados por el código, por ejemplo EMAIL/CORREO, PASSWORD/CONTRASENA, TITULO/NOMBRE y HORAS_REQUERIDAS/HORAS_REGLAMENTARIAS.
