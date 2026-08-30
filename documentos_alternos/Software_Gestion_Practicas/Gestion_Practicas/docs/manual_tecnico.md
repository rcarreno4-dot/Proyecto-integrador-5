# Manual técnico
## Sistema de Gestión de Prácticas Académicas v1.0

## 1. Arquitectura

El sistema usa una arquitectura por capas:

| Capa | Paquete | Responsabilidad |
|---|---|---|
| Vista | `com.gestionpracticas.vista` | Formularios Swing y navegación por rol. |
| Servicio/Lógica | `com.gestionpracticas.servicio` y `com.gestionpracticas.logica` | Reglas de negocio, trazabilidad, cambio de etapa, documentos y reportes PDF. |
| DAO | `com.gestionpracticas.dao` | Acceso a Oracle mediante JDBC y CRUD. |
| Modelo/DTO | `com.gestionpracticas.modelo` | Entidades simples usadas por formularios y servicios. |
| Utilidades | `com.gestionpracticas.util` | Conexión, helpers de BD, validaciones y utilidades de pantalla. |

Patrones aplicados:

- DAO: clases `UsuarioDAO`, `PracticaDAO`, `GenericCrudDAO`, entre otras.
- Service Layer: `EtapaProductivaService`, `ReportePdfService`, `CertificadoService`.
- Singleton: `ConexionBD` administra una única conexión JDBC reutilizable.
- DTO/Modelo: clases simples en `modelo`.

## 2. Configuración Oracle

Archivo principal: `resources/config.properties` o `config/config.properties`.

```properties
db.driver=oracle.jdbc.driver.OracleDriver
db.url=jdbc:oracle:thin:@localhost:1521:XE
db.user=PROYECTOJD
db.password=PROYECTOJD
```

La clase `ConexionBD` busca primero en classpath y luego en las carpetas `resources/` y `config/`. Si no encuentra configuración, usa Oracle XE local como valor por defecto.

## 3. Base de datos

Scripts principales:

1. `sql/00_limpiar_esquema.sql`: elimina objetos previos.
2. `sql/00_schema_oracle_10g_39_objetos.sql`: crea 34 tablas, 5 vistas, secuencias, índices y triggers de ID.
3. `sql/01_datos_prueba.sql`: inserta datos de prueba.
4. `sql/02_funciones_procedimientos_triggers.sql`: funciones, procedimientos y triggers de negocio/auditoría.
5. `sql/03_roles_privilegios.sql`: roles Oracle sugeridos.
6. `sql/04_consultas_validacion.sql`: consultas de prueba.
7. `sql/instalar_todo.sql`: instalación completa.

Ejecución en SQL*Plus:

```sql
CONNECT PROYECTOJD/PROYECTOJD@localhost:1521/XE
@sql/instalar_todo.sql
```

## 4. Funciones y procedimientos relevantes

Funciones:

- `FN_TOTAL_HORAS_ESTUDIANTE(id_estudiante)`.
- `FN_ESTADO_HORAS(id_matricula)`.
- `FN_PROMEDIO_EVALUACION_PRACTICA(id_practica)`.
- `FN_TOTAL_ESTUDIANTES_PRACTICA(id_practica)`.
- `FN_GENERAR_HASH_CERT(id_matricula)`.

Procedimientos:

- `SP_CREAR_USUARIO`.
- `SP_MATRICULAR_ESTUDIANTE`.
- `SP_REGISTRAR_ACTIVIDAD`.
- `SP_REGISTRAR_EVALUACION`.
- `SP_CAMBIAR_ETAPA_PRODUCTIVA`.
- `SP_EMITIR_CERTIFICADO`.

Triggers críticos:

- Validación de horas no negativas.
- Validación de nota entre 0 y 5.
- Auditoría automática de matrícula, evaluación, documentos, historial y certificados.

## 5. Compilación

Con Ant/NetBeans:

```bash
ant clean jar
```

Salida esperada:

```text
dist/GestionPracticas.jar
```

Con línea de comandos:

```bash
java -cp "dist/GestionPracticas.jar:lib/ojdbc14.jar:lib/itextpdf-5.5.13.3.jar" com.gestionpracticas.Main
```

En Windows cambie `:` por `;`.

## 6. Reportes PDF

La clase `ReportePdfService` usa iText 5.5.13.3 para generar:

- Avance de horas.
- Evaluación final.
- Certificado de práctica.

Cada documento incluye trazabilidad mediante hash y código textual verificable.

## 7. Seguridad

- Autenticación por correo y contraseña.
- Redirección por rol desde `LoginForm`.
- Registro de auditoría en `LOGIN_AUDITORIA` y `LOG_ACTIVIDAD`.
- Roles Oracle sugeridos en `03_roles_privilegios.sql`.

Nota técnica: para producción se recomienda reemplazar contraseñas de prueba por hash BCrypt o PBKDF2. La estructura de columnas permite migrarlo sin cambiar el resto del sistema.

## 8. Supuestos de diseño

El enunciado suministra nombres de tablas, pero no todos los campos. Se creó un diccionario compatible con las convenciones usadas por el código y con los requisitos funcionales: trazabilidad, documentos, horas, evaluación, matrícula y certificados.
