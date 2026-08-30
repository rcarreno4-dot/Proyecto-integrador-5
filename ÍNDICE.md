# 📑 ÍNDICE COMPLETO - PROYECTOJD

Guía de navegación para acceder rápidamente a cualquier parte del proyecto.

---

## 🎯 Por Objetivo

### Si quiero...

#### Entender el Sistema
- 📖 **Descripción general** → [README.md](README.md)
- 🏗️ **Arquitectura técnica** → [Software_Gestion_Practicas/Gestion_Practicas/docs/arquitectura.md](Software_Gestion_Practicas/Gestion_Practicas/docs/arquitectura.md)
- 📊 **Diagramas UML** → [Software_Gestion_Practicas/Gestion_Practicas/documentacion/DOCUMENTACION_UML.md](Software_Gestion_Practicas/Gestion_Practicas/documentacion/DOCUMENTACION_UML.md)
- 🎨 **Diagramas visuales** → 
  - [Componentes](Software_Gestion_Practicas/Gestion_Practicas/diagramas/diagrama_componentes.puml)
  - [Despliegue](Software_Gestion_Practicas/Gestion_Practicas/diagramas/diagrama_despliegue.puml)

#### Usar el Sistema
- 👤 **Manual de usuario** → [Software_Gestion_Practicas/Gestion_Practicas/docs/manual_usuario.md](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_usuario.md)
- 🔐 **Usuarios de prueba** → Ver tabla en [README.md](README.md#usuarios-de-prueba)
- 🎬 **Video demostrativo** (guión) → [Software_Gestion_Practicas/Gestion_Practicas/docs/video_demo_guion.md](Software_Gestion_Practicas/Gestion_Practicas/docs/video_demo_guion.md)

#### Desplegar/Instalar
- ⚙️ **Instalación rápida** → Ver [README.md](README.md#inicio-rápido)
- 📋 **Guía de despliegue** → [Software_Gestion_Practicas/Gestion_Practicas/docs/guia_despliegue.md](Software_Gestion_Practicas/Gestion_Practicas/docs/guia_despliegue.md)
- 🗄️ **Scripts de base de datos** → [Software_Gestion_Practicas/Gestion_Practicas/sql/](Software_Gestion_Practicas/Gestion_Practicas/sql/)

#### Entender la Base de Datos
- 📚 **Diccionario de datos** → [Software_Gestion_Practicas/Gestion_Practicas/docs/diccionario_datos.md](Software_Gestion_Practicas/Gestion_Practicas/docs/diccionario_datos.md)
- 📊 **Schema SQL** → [schema.sql](schema.sql)
- 🔧 **Scripts de instalación** → [Software_Gestion_Practicas/Gestion_Practicas/sql/instalar_todo.sql](Software_Gestion_Practicas/Gestion_Practicas/sql/instalar_todo.sql)

#### Revisar Código Fuente
- 📂 **Estructura del código** → [Software_Gestion_Practicas/Gestion_Practicas/src/gestionpracticas/](Software_Gestion_Practicas/Gestion_Practicas/src/gestionpracticas/)
  - Modelos → `src/gestionpracticas/modelo/`
  - DAO → `src/gestionpracticas/dao/`
  - Lógica → `src/gestionpracticas/logica/`
  - Vistas → `src/gestionpracticas/vista/`
  - Utilidades → `src/gestionpracticas/util/`

#### Aspectos Técnicos Profundos
- 🔬 **Manual técnico completo** → [Software_Gestion_Practicas/Gestion_Practicas/docs/manual_tecnico.md](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_tecnico.md)
- 📖 **Manual técnico resumido** → [Software_Gestion_Practicas/Gestion_Practicas/documentacion/MANUAL_TECNICO_RESUMIDO.md](Software_Gestion_Practicas/Gestion_Practicas/documentacion/MANUAL_TECNICO_RESUMIDO.md)
- 🔐 **Seguridad** → Ver sección 7 en [manual_tecnico.md](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_tecnico.md#7-seguridad)

#### Información Comercial
- 💼 **Plan comercial** → [Software_Gestion_Practicas/Gestion_Practicas/docs/plan_comercial.md](Software_Gestion_Practicas/Gestion_Practicas/docs/plan_comercial.md)
- 📈 **Propuesta de valor** → Ver sección 1 en [plan_comercial.md](Software_Gestion_Practicas/Gestion_Practicas/docs/plan_comercial.md)

#### Últimas Actualizaciones
- ✨ **Cambios v1.4** → [Software_Gestion_Practicas/Gestion_Practicas/docs/CAMBIOS_V1_4_PRESENTACION.md](Software_Gestion_Practicas/Gestion_Practicas/docs/CAMBIOS_V1_4_PRESENTACION.md)
- 📝 **Cambios fix UI** → [Software_Gestion_Practicas/Gestion_Practicas/CAMBIOS_FIX_2_UI_CRUD.txt](Software_Gestion_Practicas/Gestion_Practicas/CAMBIOS_FIX_2_UI_CRUD.txt)

---

## 📂 Por Ubicación

### Raíz del Repositorio
```
/
├── README.md                          ← Guía principal (EMPIEZA AQUÍ)
├── ÍNDICE.md                          ← Este archivo
├── LICENSE                            ← Licencia MIT
├── schema.sql                         ← Esquema base de datos
└── assets/                            ← Recursos adicionales
```

### Código Fuente Principal
```
Software_Gestion_Practicas/Gestion_Practicas/
├── src/gestionpracticas/              ← Código Java
│   ├── modelo/                        ← Entidades (Usuario, Practica, etc.)
│   ├── dao/                           ← Acceso a datos (CRUD)
│   ├── logica/                        ← Lógica de negocio
│   ├── servicio/                      ← Servicios especializados
│   ├── vista/                         ← Interfaces gráficas (por rol)
│   └── util/                          ← Utilidades y helpers
│
├── lib/                               ← Librerías
│   ├── ojdbc14.jar                    ← Driver Oracle JDBC
│   └── itextpdf-5.5.13.3.jar         ← Generador PDF
│
├── sql/                               ← Scripts de base de datos
│   ├── instalar_todo.sql              ← Instalación completa
│   ├── 00_limpiar_esquema.sql
│   ├── 01_creacion_tablas_oracle10g.sql
│   ├── 02_funciones_procedimientos_triggers.sql
│   ├── 03_roles_privilegios.sql
│   ├── 04_consultas_validacion.sql
│   ├── 99_patch_presentacion_v13.sql
│   └── 99_patch_presentacion_v14.sql
│
├── config/                            ← Configuración
│   └── config.properties              ← Parámetros de conexión
│
├── resources/                         ← Recursos (propiedades, assets)
│   └── config.properties              ← Configuración alternativa
│
├── docs/                              ← Documentación oficial
│   ├── manual_usuario.md              ← Guía de uso
│   ├── manual_tecnico.md              ← Aspectos técnicos
│   ├── arquitectura.md                ← Descripción arquitectónica
│   ├── diccionario_datos.md           ← Esquema de BD
│   ├── guia_despliegue.md             ← Instalación en producción
│   ├── plan_comercial.md              ← Propuesta comercial
│   ├── video_demo_guion.md            ← Guión de demostración
│   ├── CAMBIOS_V1_4_PRESENTACION.md   ← Release notes v1.4
│   └── img/                           ← Imágenes para documentación
│
├── documentacion/                     ← Documentación complementaria
│   ├── DOCUMENTACION_UML.md           ← Análisis UML
│   └── MANUAL_TECNICO_RESUMIDO.md     ← Versión resumida técnica
│
├── diagramas/                         ← Diagramas UML
│   ├── diagrama_componentes.puml      ← Componentes del sistema
│   └── diagrama_despliegue.puml       ← Despliegue físico
│
├── pom.xml                            ← Dependencias Maven
├── README.txt                         ← Instrucciones iniciales
├── README.md                          ← README alternativo
├── README_EJECUCION_UNIVERSIDAD.md    ← Guía específica universidad
└── ejecutar.bat / ejecutar.sh         ← Scripts de ejecución
```

### Documentación Alternativa
```
documentos_alternos/
├── diagramas_primera_entrega.md       ← Diagramas iniciales
├── Primer_Entrega_Proyecto_Integrador/
├── astah_export/                      ← Exportación de diagramas Astah
└── Software_Gestion_Practicas/        ← Copias alternativas
```

### Prototipos
```
prototipos/
├── prototipo_mediana_fidelidad.html   ← Prototipo HTML
└── SIGPRA-Web-Prototype.make          ← Makefile prototipo web
```

### Librerías Externas
```
lib/ojdbc8-full/                       ← Oracle JDBC completo
├── LICENSE.txt
├── Readme.txt
└── Javadoc-Readmes/
```

---

## 👥 Por Perfil

### 👨‍🏫 Para el Profesor

**Lectura recomendada en orden:**

1. [README.md](README.md) - Visión general
2. [DOCUMENTACION_UML.md](Software_Gestion_Practicas/Gestion_Practicas/documentacion/DOCUMENTACION_UML.md) - Entender la arquitectura
3. [diagramas/diagrama_componentes.puml](Software_Gestion_Practicas/Gestion_Practicas/diagramas/diagrama_componentes.puml) - Ver componentes
4. [diagramas/diagrama_despliegue.puml](Software_Gestion_Practicas/Gestion_Practicas/diagramas/diagrama_despliegue.puml) - Ver despliegue
5. [manual_usuario.md](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_usuario.md) - Cómo funciona para usuarios
6. [diccionario_datos.md](Software_Gestion_Practicas/Gestion_Practicas/docs/diccionario_datos.md) - Estructura de datos
7. [CAMBIOS_V1_4_PRESENTACION.md](Software_Gestion_Practicas/Gestion_Practicas/docs/CAMBIOS_V1_4_PRESENTACION.md) - Mejoras finales

**Checklist de revisión:**
- [ ] ¿Entiende la arquitectura MVC?
- [ ] ¿Identifica los 5 roles principales?
- [ ] ¿Comprende el flujo de prácticas?
- [ ] ¿Ve la trazabilidad y auditoría?
- [ ] ¿Valida que la base de datos está bien estructurada?

### 👨‍💻 Para Desarrolladores

**Lectura recomendada en orden:**

1. [README.md](README.md#-arquitectura-mvc) - Visión de capas
2. [arquitectura.md](Software_Gestion_Practicas/Gestion_Practicas/docs/arquitectura.md) - Flujos técnicos
3. [manual_tecnico.md](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_tecnico.md) - Detalles de implementación
4. [src/gestionpracticas/](Software_Gestion_Practicas/Gestion_Practicas/src/gestionpracticas/) - Revisar código
5. [diccionario_datos.md](Software_Gestion_Practicas/Gestion_Practicas/docs/diccionario_datos.md) - Entender BD
6. [sql/instalar_todo.sql](Software_Gestion_Practicas/Gestion_Practicas/sql/instalar_todo.sql) - Ver scripts

**Rutas de aprendizaje:**
- **Vistas/UI** → `src/gestionpracticas/vista/`
- **Base de datos** → `src/gestionpracticas/dao/` + `sql/`
- **Lógica de negocio** → `src/gestionpracticas/logica/` + `src/gestionpracticas/servicio/`
- **Entidades** → `src/gestionpracticas/modelo/`
- **Configuración** → `src/gestionpracticas/util/ConexionBD.java`

### 🚀 Para Desplegar

**Lectura recomendada en orden:**

1. [guia_despliegue.md](Software_Gestion_Practicas/Gestion_Practicas/docs/guia_despliegue.md) - Procedimiento paso a paso
2. [README_EJECUCION_UNIVERSIDAD.md](Software_Gestion_Practicas/Gestion_Practicas/README_EJECUCION_UNIVERSIDAD.md) - Específico para universidades
3. [sql/](Software_Gestion_Practicas/Gestion_Practicas/sql/) - Ejecutar scripts en orden
4. [config/config.properties](Software_Gestion_Practicas/Gestion_Practicas/config/config.properties) - Configurar conexión

**Checklist de despliegue:**
- [ ] ¿Tengo JDK 8+ instalado?
- [ ] ¿Tengo Oracle 10g+ funcionando?
- [ ] ¿Ejecuté los scripts SQL?
- [ ] ¿Configuré config.properties?
- [ ] ¿Compiló sin errores con `ant clean jar`?
- [ ] ¿Se ejecuta el JAR correctamente?

---

## 🔍 Búsqueda Rápida

| Busco | Ubicación |
|-------|-----------|
| Clase `LoginForm` | [src/gestionpracticas/vista/](Software_Gestion_Practicas/Gestion_Practicas/src/gestionpracticas/vista/) |
| Tabla `USUARIO` | [docs/diccionario_datos.md](Software_Gestion_Practicas/Gestion_Practicas/docs/diccionario_datos.md) |
| DAO de Prácticas | `src/gestionpracticas/dao/PracticaDAO.java` |
| Servicio de reportes | `src/gestionpracticas/servicio/ReportePdfService.java` |
| Conexión BD | `src/gestionpracticas/util/ConexionBD.java` |
| Panel Director | [src/gestionpracticas/vista/director/DirectorDashboard.java](Software_Gestion_Practicas/Gestion_Practicas/src/gestionpracticas/vista/director/DirectorDashboard.java) |
| Panel Coordinador | `src/gestionpracticas/vista/coordinador/CoordinadorDashboard.java` |
| Panel Docente | `src/gestionpracticas/vista/docente/DocenteDashboard.java` |
| Panel Estudiante | `src/gestionpracticas/vista/estudiante/EstudianteDashboard.java` |
| Panel Institución | `src/gestionpracticas/vista/institucion/InstitucionDashboard.java` |

---

## 📊 Estadísticas Rápidas

- **Líneas de código Java:** ~8,000+
- **Clases Java:** 50+
- **Tablas Oracle:** 20+
- **Documentos:** 10 archivos markdown
- **Diagramas UML:** 2 (componentes, despliegue)
- **Scripts SQL:** 10+
- **Roles del sistema:** 5

---

## 🎯 Flujos Principales (Ubicaciones)

### Flujo de Login
- **Código:** `src/gestionpracticas/vista/LoginForm.java`
- **Lógica:** `src/gestionpracticas/logica/LoginLogica.java`
- **DAO:** `src/gestionpracticas/dao/UsuarioDAO.java`
- **Documentación:** [manual_usuario.md - Sección 1](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_usuario.md#1-iniciar-sesión)

### Flujo de Matrícula
- **Código:** `src/gestionpracticas/vista/director/MatriculaForm.java`
- **Lógica:** `src/gestionpracticas/logica/MatriculaLogica.java`
- **DAO:** `src/gestionpracticas/dao/MatriculaPracticaDAO.java`
- **Documentación:** [manual_usuario.md - Sección 3](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_usuario.md#3-matricular-estudiante-en-práctica)

### Flujo de Registro de Actividades
- **Código:** `src/gestionpracticas/vista/estudiante/RegistroActividadForm.java`
- **Lógica:** `src/gestionpracticas/logica/ActividadLogica.java`
- **DAO:** `src/gestionpracticas/dao/RegistroActividadDAO.java`
- **Documentación:** [manual_usuario.md - Sección 4](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_usuario.md#4-registrar-horas-y-actividades)

### Flujo de Evaluación
- **Código:** `src/gestionpracticas/vista/docente/EvaluacionForm.java`
- **Lógica:** `src/gestionpracticas/logica/EvaluacionLogica.java`
- **DAO:** `src/gestionpracticas/dao/EvaluacionDAO.java`
- **Documentación:** [manual_usuario.md - Sección 5](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_usuario.md#5-evaluar-estudiante)

### Flujo de Cambio de Etapa
- **Código:** `src/gestionpracticas/servicio/EtapaProductivaService.java`
- **Lógica:** Validaciones, trazabilidad, auditoría
- **DAO:** Múltiples DAOs (Matrícula, Horas, Evaluación)
- **Documentación:** [arquitectura.md - Sección flujo de cambio de etapa](Software_Gestion_Practicas/Gestion_Practicas/docs/arquitectura.md)

### Flujo de Reportes PDF
- **Código:** `src/gestionpracticas/servicio/ReportePdfService.java`
- **Lógica:** Generación con iTextPDF 5.5.13.3
- **DAO:** Consultas de datos
- **Documentación:** [manual_tecnico.md - Sección 6](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_tecnico.md#6-reportes-pdf)

---

## 📞 Preguntas Frecuentes

**P: ¿Por dónde empiezo?**  
A: Lee [README.md](README.md) primero, luego ve a la sección "📖 Guía de Navegación"

**P: ¿Dónde está el código fuente?**  
A: En `Software_Gestion_Practicas/Gestion_Practicas/src/gestionpracticas/`

**P: ¿Cómo instalo la base de datos?**  
A: Sigue [guia_despliegue.md](Software_Gestion_Practicas/Gestion_Practicas/docs/guia_despliegue.md)

**P: ¿Cuáles son los usuarios de prueba?**  
A: Ver tabla en [README.md](README.md#usuarios-de-prueba)

**P: ¿Dónde está la documentación técnica?**  
A: En [docs/](Software_Gestion_Practicas/Gestion_Practicas/docs/) - ver [manual_tecnico.md](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_tecnico.md)

**P: ¿Cómo comprendo la arquitectura?**  
A: 1) Lee [arquitectura.md](Software_Gestion_Practicas/Gestion_Practicas/docs/arquitectura.md) 2) Ve diagramas en [diagramas/](Software_Gestion_Practicas/Gestion_Practicas/diagramas/) 3) Lee [DOCUMENTACION_UML.md](Software_Gestion_Practicas/Gestion_Practicas/documentacion/DOCUMENTACION_UML.md)

---

**Última actualización:** Agosto 2026  
**Versión:** 1.0 (Índice)
