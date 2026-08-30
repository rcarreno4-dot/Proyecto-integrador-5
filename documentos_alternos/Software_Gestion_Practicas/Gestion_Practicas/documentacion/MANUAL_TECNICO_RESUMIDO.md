# Manual Técnico Resumido

## Arquitectura

El sistema está construido en Java Swing bajo una estructura por capas basada en MVC:

- **Vista:** interfaces gráficas por rol.
- **Controlador/Eventos:** gestión de acciones del usuario.
- **Lógica:** validaciones y reglas de negocio.
- **DAO:** operaciones SQL y CRUD.
- **Modelo:** entidades del dominio.
- **Util:** conexión a BD, constantes y validaciones generales.

## Base de datos

Motor: Oracle Database XE 10g.

Esquema: `PROYECTOJD`.

Objetos incluidos:

- Tablas principales del sistema.
- Claves primarias y foráneas.
- Secuencias para llaves primarias.
- Funciones para indicadores.
- Procedimientos para operaciones críticas.
- Triggers para validación y auditoría.

## Librerías

- `ojdbc14.jar`: conexión JDBC con Oracle 10g.
- `itextpdf-5.5.13.3.jar`: generación de reportes PDF.

## Clase de conexión

Ruta:

```text
src/gestionpracticas/util/ConexionBD.java
```

Esta clase centraliza la conexión a Oracle mediante `DriverManager` y es utilizada por los DAO.

## Compilación

```bash
ant clean jar
```

## Ejecución

```bash
java -cp "dist/GestionPracticas.jar;lib/ojdbc14.jar;lib/itextpdf-5.5.13.3.jar" com.gestionpracticas.Main
```
