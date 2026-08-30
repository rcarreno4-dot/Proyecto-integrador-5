# PROYECTOJD - Gestión de Prácticas Académicas

## Versión incluida

Esta versión del proyecto incluye código fuente Java Swing, arquitectura por capas, librerías necesarias, scripts Oracle 10g, diagramas UML y documentación base para entrega final.

## Requisitos

- NetBeans IDE.
- JDK compatible con Java 8 o superior.
- Oracle Database 10g/XE o conexión a la base de datos de la universidad.
- Usuario de base de datos: `PROYECTOJD`.
- Contraseña de base de datos: `PROYECTOJD`.
- Librerías incluidas en carpeta `lib`:
  - `ojdbc14.jar`
  - `itextpdf-5.5.13.3.jar`

## Conexión configurada

El archivo `src/gestionpracticas/util/ConexionBD.java` quedó configurado para:

```java
jdbc:oracle:thin:@192.168.254.215:1521:orcl
```

Usuario y contraseña:

```text
PROYECTOJD / PROYECTOJD
```

Si se trabaja en Oracle XE local, revisar la carpeta `config` y cambiar la URL por:

```java
jdbc:oracle:thin:@localhost:1521:XE
```

## Cómo abrir en NetBeans

1. Abrir NetBeans.
2. Seleccionar **File > Open Project**.
3. Elegir la carpeta `GestionPracticas_Refactorizado_Completo`.
4. Verificar que las librerías `lib/ojdbc14.jar` y `lib/itextpdf-5.5.13.3.jar` estén cargadas.
5. Ejecutar el proyecto con `Run`.

## Cómo compilar

Desde consola dentro de la carpeta del proyecto:

```bash
ant clean jar
```

El proyecto fue compilado correctamente con las librerías relativas en `lib`.

## Cómo ejecutar el JAR

En Windows:

```bat
ejecutar.bat
```

O manualmente:

```bat
java -cp "dist/GestionPracticas.jar;lib/ojdbc14.jar;lib/itextpdf-5.5.13.3.jar" com.gestionpracticas.Main
```

## Orden de ejecución SQL

En SQL Developer o SQL Plus, conectado como `PROYECTOJD`:

1. `database/00_limpiar_esquema.sql` opcional, solo si se va a borrar todo.
2. `database/01_creacion_tablas_oracle10g.sql`.
3. `database/02_datos_prueba.sql`.
4. `database/03_funciones_procedimientos_triggers.sql`.
5. `database/04_consultas_prueba_crud.sql` para validar.
6. `database/05_roles_privilegios.sql` opcional, si se requiere DCL/roles.

## Usuarios de prueba

| Rol | Correo | Contraseña |
|---|---|---|
| Director | admin@proyectojd.com | 123456 |
| Coordinador | carlos@proyectojd.com | 123456 |
| Docente | maria@proyectojd.com | 123456 |
| Estudiante | juan@proyectojd.com | 123456 |
| Institución | empresa@proyectojd.com | 123456 |

## Estructura del proyecto

```text
src/gestionpracticas/modelo     Entidades del sistema
src/gestionpracticas/dao        Acceso a datos y CRUD
src/gestionpracticas/logica     Reglas de negocio
src/gestionpracticas/vista      Interfaces por rol
src/gestionpracticas/util       Conexión BD y utilidades
database                        Scripts Oracle 10g
diagramas                       Diagramas de componentes y despliegue
documentacion                   Documentación UML y técnica
lib                             Librerías del proyecto
```

## Nota importante

La compilación fue validada a nivel de código fuente y librerías. La conexión real depende de que el equipo esté en la red de la universidad y que el servicio Oracle esté disponible.
