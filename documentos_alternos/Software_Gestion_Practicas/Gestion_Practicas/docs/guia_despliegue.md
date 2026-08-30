# Guía de despliegue para cliente

## Requisitos

- Windows 10/11 o Linux.
- JDK 26 recomendado. También funciona con JDK 21+.
- NetBeans 17 o superior.
- Oracle Database XE 10g o superior.
- SQL*Plus o SQL Developer.

## Instalación de Java

1. Instale JDK 26.
2. Configure `JAVA_HOME`.
3. Agregue `%JAVA_HOME%\bin` o `$JAVA_HOME/bin` al PATH.
4. Valide con:

```bash
java -version
javac -version
```

## Instalación Oracle

1. Instale Oracle XE.
2. Cree el usuario del esquema:

```sql
CREATE USER PROYECTOJD IDENTIFIED BY PROYECTOJD;
GRANT CONNECT, RESOURCE TO PROYECTOJD;
GRANT CREATE VIEW, CREATE PROCEDURE, CREATE TRIGGER TO PROYECTOJD;
ALTER USER PROYECTOJD QUOTA UNLIMITED ON USERS;
```

3. Conéctese:

```sql
CONNECT PROYECTOJD/PROYECTOJD@localhost:1521/XE
```

4. Ejecute:

```sql
@sql/instalar_todo.sql
```

## Configuración del sistema

Edite `resources/config.properties`:

```properties
db.url=jdbc:oracle:thin:@localhost:1521:XE
db.user=PROYECTOJD
db.password=PROYECTOJD
```

## Ejecución desde NetBeans

1. Abra el proyecto.
2. Revise que `lib/ojdbc14.jar` y `lib/itextpdf-5.5.13.3.jar` estén agregados.
3. Presione Clean and Build.
4. Presione Run.

## Ejecución desde JAR

Linux/macOS:

```bash
./ejecutar.sh
```

Windows:

```bat
ejecutar.bat
```

## Validación rápida

Ingrese con `admin@proyectojd.com / 123456`. Revise que se abra el panel del Director. Luego ejecute en SQL*Plus:

```sql
SELECT COUNT(*) FROM USUARIO;
SELECT * FROM VW_ESTUDIANTES_MATRICULADOS;
SELECT * FROM LOG_ACTIVIDAD;
```
