GESTION PRACTICAS - COMPATIBILIDAD JAVA 1.4

CONFIGURACION ACTUAL:
- Java: 1.4.2_03
- JDBC Oracle recomendado: ojdbc14.jar
- Usuario Oracle: PROYECTOJD
- Contraseña Oracle: PROYECTOJD

IMPORTANTE:
1. Agregar ojdbc14.jar en:
   Proyecto -> Properties -> Libraries -> Add JAR/Folder

2. Conexion Oracle:
   jdbc:oracle:thin:@localhost:1521:XE

3. Si usa Java 1.4:
   - No usar FlatLaf
   - No usar lambdas
   - No usar módulos modernos

4. Recomendación:
   Oracle 10g + ojdbc14.jar es la combinación más estable.

