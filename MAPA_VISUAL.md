# 🗺️ MAPA VISUAL DEL PROYECTO

Navegación visual y rápida por toda la estructura del proyecto.

---

## 🎯 VISIÓN GENERAL EN 1 MINUTO

```
┌─────────────────────────────────────────────────────────────┐
│           PROYECTOJD - Gestión de Prácticas                │
│                   (Sistema Java + Oracle)                  │
│                                                             │
│  👨‍🏫 Profesor → Revisa: README.md + Diagramas + Docs    │
│  👨‍💻 Developer → Explora: src/ + docs/ + sql/             │
│  🚀 DevOps → Sigue: guia_despliegue.md                    │
└─────────────────────────────────────────────────────────────┘
```

---

## 📍 UBICACIÓN DE CADA ELEMENTO

### 1️⃣ DOCUMENTACIÓN DE INICIO

```
📦 Raíz del Repositorio
│
├── 🎯 COMIENZA_AQUÍ.md ........... ⭐ PRIMERO (2 minutos)
├── 📖 README.md .................. ⭐ LEE ESTO (5-10 minutos)
├── 🗺️ MAPA_VISUAL.md ............. ← TÚ ESTÁS AQUÍ
├── 📑 ÍNDICE.md .................. Mapa completo de navegación
└── ⚡ GUÍA_INICIO.md ............. Pasos detallados
```

**➜ Comienza por:** `COMIENZA_AQUÍ.md`  
**➜ Luego lee:** `README.md`  
**➜ Para explorar:** `ÍNDICE.md`

---

### 2️⃣ CÓDIGO FUENTE JAVA

```
📦 Software_Gestion_Practicas/Gestion_Practicas/src/
│
├── 🎨 gestionpracticas/
│   │
│   ├── modelo/ ..................... Entidades del sistema
│   │   ├── Usuario.java
│   │   ├── Practica.java
│   │   ├── Grupo.java
│   │   ├── Evaluacion.java
│   │   ├── Rubrica.java
│   │   ├── MatriculaPractica.java
│   │   ├── RegistroActividad.java
│   │   ├── DocumentoEstudiante.java
│   │   └── ... (más modelos)
│   │
│   ├── 🗂️ dao/ ....................... Acceso a Datos (CRUD)
│   │   ├── UsuarioDAO.java
│   │   ├── PracticaDAO.java
│   │   ├── GrupoDAO.java
│   │   ├── MatriculaPracticaDAO.java
│   │   ├── RegistroActividadDAO.java
│   │   ├── EvaluacionDAO.java
│   │   ├── RubricaDAO.java
│   │   ├── GenericCrudDAO.java
│   │   └── ... (más DAOs)
│   │
│   ├── ⚙️ logica/ ................... Lógica de Negocio
│   │   ├── LoginLogica.java
│   │   ├── MatriculaLogica.java
│   │   ├── ActividadLogica.java
│   │   ├── EvaluacionLogica.java
│   │   └── ... (más lógica)
│   │
│   ├── 🔧 servicio/ ................ Servicios Especializados
│   │   ├── EtapaProductivaService.java ← Cambio de etapa
│   │   ├── ReportePdfService.java ←--- Generación PDF
│   │   ├── CertificadoService.java
│   │   └── ... (más servicios)
│   │
│   ├── 🖥️ vista/ ................... Interfaces Gráficas (Swing)
│   │   │
│   │   ├── LoginForm.java ........... Pantalla de login
│   │   │
│   │   ├── director/
│   │   │   ├── DirectorDashboard.java
│   │   │   ├── DirectorForm.java
│   │   │   ├── MatriculaForm.java
│   │   │   ├── ReporteForm.java
│   │   │   └── ... (más formularios)
│   │   │
│   │   ├── coordinador/
│   │   │   ├── CoordinadorDashboard.java
│   │   │   ├── GrupoForm.java
│   │   │   └── ... (más formularios)
│   │   │
│   │   ├── docente/
│   │   │   ├── DocenteDashboard.java
│   │   │   ├── EvaluacionForm.java
│   │   │   ├── RubricaForm.java
│   │   │   └── ... (más formularios)
│   │   │
│   │   ├── estudiante/
│   │   │   ├── EstudianteDashboard.java
│   │   │   ├── RegistroActividadForm.java
│   │   │   └── ... (más formularios)
│   │   │
│   │   ├── institucion/
│   │   │   ├── InstitucionDashboard.java
│   │   │   └── ... (formularios institución)
│   │   │
│   │   └── simple/
│   │       ├── ReportesHistoricoSimpleForm.java
│   │       └── ... (más formularios)
│   │
│   └── 🔐 util/ ................... Utilidades y Configuración
│       ├── ConexionBD.java ........... Singleton conexión
│       ├── Utilidades.java
│       ├── PantallaUtil.java
│       ├── DBHelper.java
│       ├── SemillasProyecto.java .... Datos de prueba
│       └── ... (más utilidades)
```

**➜ Para revisar:** `Software_Gestion_Practicas/Gestion_Practicas/src/gestionpracticas/`

---

### 3️⃣ DOCUMENTACIÓN

```
📚 Software_Gestion_Practicas/Gestion_Practicas/docs/
│
├── 📖 manual_usuario.md ........... Cómo usar (paso a paso)
│   └── ¿Qué leen?: Usuarios finales, profesores
│   └── Contenido: Flujos por rol, screenshots, guías
│
├── 📘 manual_tecnico.md ........... Detalles técnicos profundos
│   └── ¿Qué lee?: Desarrolladores, arquitectos
│   └── Contenido: Código, patrones, solución técnica
│
├── 🏗️ arquitectura.md ............. Descripción arquitectónica
│   └── Contenido: Capas, flujos, componentes, patrones
│
├── 📊 diccionario_datos.md ........ Esquema de base de datos
│   └── Contenido: Tablas, columnas, relaciones
│
├── 🚀 guia_despliegue.md ......... Instalación paso a paso
│   └── Contenido: Pre-requisitos, pasos, validación
│
├── 💼 plan_comercial.md .......... Propuesta de valor
│   └── Contenido: Módulos, diferenciadores, modelo comercial
│
├── 🎬 video_demo_guion.md ........ Guión de demostración
│   └── Contenido: Timeline 5 minutos, flujos a mostrar
│
├── ✨ CAMBIOS_V1_4_PRESENTACION.md  Release notes
│   └── Contenido: Últimas mejoras, nuevas funciones
│
└── 📁 img/ ....................... Imágenes para documentación
```

**➜ Para entender cómo se usa:** `docs/manual_usuario.md`  
**➜ Para detalles técnicos:** `docs/manual_tecnico.md`  
**➜ Para instalar:** `docs/guia_despliegue.md`

---

### 4️⃣ DOCUMENTACIÓN ADICIONAL

```
📚 Software_Gestion_Practicas/Gestion_Practicas/documentacion/
│
├── 📋 DOCUMENTACION_UML.md ........ Análisis UML completo
│   └── Diagramas de componentes y despliegue analizados
│
└── 📖 MANUAL_TECNICO_RESUMIDO.md .. Versión resumida
    └── Quick reference técnico
```

**➜ Para entender UML:** `documentacion/DOCUMENTACION_UML.md`

---

### 5️⃣ DIAGRAMAS UML

```
📊 Software_Gestion_Practicas/Gestion_Practicas/diagramas/
│
├── 🔷 diagrama_componentes.puml .... Arquitectura interna
│   └── Muestra: Vistas, Controladores, Servicios, DAO, BD
│
└── 🔹 diagrama_despliegue.puml .... Distribución física
    └── Muestra: Cliente, Servidor Java, Base de datos, Red
```

**➜ Para ver arquitectura:** Abre con PlantUML viewer o VS Code

---

### 6️⃣ BASE DE DATOS (SCRIPTS SQL)

```
🗄️ Software_Gestion_Practicas/Gestion_Practicas/sql/
│
├── 🔧 instalar_todo.sql ........... ⭐ EJECUTAR PRIMERO
│   └── Instala todo el esquema en orden
│
├── 🧹 00_limpiar_esquema.sql ..... Borra esquema anterior
├── 🏗️ 01_creacion_tablas_oracle10g.sql ... Crea tablas
├── ⚡ 02_funciones_procedimientos_triggers.sql ... Lógica BD
├── 🔐 03_roles_privilegios.sql ... Permisos Oracle
├── ✔️ 04_consultas_validacion.sql  Validación después de instalar
├── 📊 05_roles_privilegios.sql ... Más permisos
├── 📝 06_datos_base_para_pruebas_universidad.sql
├── 🔗 07_fix_relacion_programa_practica.sql
├── 🎨 08_actualizacion_ui_estados_perfil.sql
├── 🆕 99_patch_presentacion_v13.sql ... Patch v1.3
└── 🆕 99_patch_presentacion_v14.sql ... Patch v1.4
```

**➜ Para instalar BD:** Ejecuta `instalar_todo.sql` en SQL*Plus

---

### 7️⃣ CONFIGURACIÓN

```
⚙️ Software_Gestion_Practicas/Gestion_Practicas/config/
│
└── config.properties .............. Parámetros de conexión
    ├── URL Oracle: jdbc:oracle:thin:@localhost:1521:XE
    ├── Usuario: PROYECTOJD
    └── Contraseña: PROYECTOJD

📁 Software_Gestion_Practicas/Gestion_Practicas/resources/
│
└── config.properties .............. Configuración alternativa
```

**➜ Para configurar:** Edita `config/config.properties` con tu BD

---

### 8️⃣ LIBRERÍAS

```
📦 Software_Gestion_Practicas/Gestion_Practicas/lib/
│
├── ojdbc14.jar ................... Oracle JDBC driver
├── itextpdf-5.5.13.3.jar ......... Generador de PDF
└── ojdbc8-full/ .................. Oracle JDBC extendido (opcional)
```

**➜ Incluidas en:** `pom.xml` y build automático

---

### 9️⃣ EJECUCIÓN

```
▶️ Software_Gestion_Practicas/Gestion_Practicas/
│
├── ejecutar.bat .................. Script Windows
├── ejecutar.sh ................... Script Linux/Mac
├── pom.xml ....................... Dependencias Maven
└── Comando alternativo:
    java -cp "dist/GestionPracticas.jar;lib/ojdbc14.jar;lib/itextpdf-5.5.13.3.jar" com.gestionpracticas.Main
```

**➜ Para ejecutar:** `ejecutar.bat` (Windows) o `ejecutar.sh` (Linux)

---

### 🔟 COMPILACIÓN

```
🔨 Software_Gestion_Practicas/Gestion_Practicas/
│
├── build.xml ..................... Script Ant
└── Comando: ant clean jar
```

**➜ Para compilar:** `ant clean jar` (genera `dist/GestionPracticas.jar`)

---

## 🎯 FLUJOS PRINCIPALES (DÓNDE ENCONTRAR CADA PARTE)

### Login 🔐
```
Vista:   LoginForm.java
Lógica:  LoginLogica.java
DAO:     UsuarioDAO.java
BD:      USUARIO, ROL, LOGIN_AUDITORIA
```

### Matrícula de Estudiante 👤
```
Vista:   MatriculaForm.java (Director)
Lógica:  MatriculaLogica.java
DAO:     MatriculaPracticaDAO.java
BD:      MATRICULA_PRACTICA, USUARIO, PRACTICA, GRUPO
```

### Registro de Actividades 📝
```
Vista:   RegistroActividadForm.java (Estudiante)
Lógica:  ActividadLogica.java
DAO:     RegistroActividadDAO.java
BD:      REGISTRO_ACTIVIDAD, DOCUMENTO_ESTUDIANTE
```

### Confirmación de Horas ✅
```
Vista:   InstitucionDashboard.java (Institución)
Lógica:  ActividadLogica.java
DAO:     HorasPracticaDAO.java
BD:      HORAS_PRACTICA, REGISTRO_ACTIVIDAD
```

### Evaluación 📊
```
Vista:   EvaluacionForm.java (Docente)
Lógica:  EvaluacionLogica.java
DAO:     EvaluacionDAO.java, RubricaDAO.java
BD:      EVALUACION, RUBRICA, CRITERIO_RUBRICA, NIVEL_DESEMPENO
```

### Cambio de Etapa 🔄
```
Servicio: EtapaProductivaService.java
Lógica:   Validaciones, trazabilidad
DAO:      MatriculaPracticaDAO.java, HorasPracticaDAO.java
BD:       MATRICULA_PRACTICA, HISTORIAL_REVISION, LOG_ACTIVIDAD
```

### Generación de PDF 📄
```
Servicio: ReportePdfService.java
Lógica:   iTextPDF 5.5.13.3
DAO:      Múltiples DAOs
BD:       REPORTE, CERTIFICADO_PRACTICA
```

---

## ⏱️ TIEMPOS DE LECTURA RECOMENDADOS

### Para Profesor (Revisión académica)
```
COMIENZA_AQUÍ.md ............... 2 min
README.md ...................... 5 min
DOCUMENTACION_UML.md ........... 10 min
Diagramas ...................... 5 min
manual_usuario.md .............. 10 min
diccionario_datos.md ........... 5 min
───────────────────────────────────
TOTAL ....................... ~37 min
```

### Para Desarrollador (Implementación)
```
README.md ...................... 10 min
arquitectura.md ................ 15 min
Revisar src/ ................... 30 min
manual_tecnico.md .............. 20 min
diccionario_datos.md ........... 10 min
───────────────────────────────────
TOTAL ....................... ~85 min
```

### Para DevOps (Instalación)
```
guia_despliegue.md ............. 5 min
config.properties .............. 5 min
Ejecutar sql/instalar_todo.sql . 10 min
Compilar: ant clean jar ........ 5 min
Ejecutar: ejecutar.bat ......... 2 min
Probar con usuarios ............ 5 min
───────────────────────────────────
TOTAL ....................... ~32 min
```

---

## 🔍 BÚSQUEDA RÁPIDA

| Busco | Ubicación | Archivo |
|-------|-----------|---------|
| **¿Qué es esto?** | Raíz | `README.md` |
| **¿Por dónde empiezo?** | Raíz | `COMIENZA_AQUÍ.md` |
| **¿Dónde está todo?** | Raíz | `ÍNDICE.md` o `MAPA_VISUAL.md` |
| **Clase Usuario** | Código | `src/gestionpracticas/modelo/Usuario.java` |
| **DAO Usuario** | Código | `src/gestionpracticas/dao/UsuarioDAO.java` |
| **Vista Director** | Código | `src/gestionpracticas/vista/director/` |
| **Tabla USUARIO** | Docs | `docs/diccionario_datos.md` |
| **Conexión BD** | Código | `src/gestionpracticas/util/ConexionBD.java` |
| **PDF Service** | Código | `src/gestionpracticas/servicio/ReportePdfService.java` |
| **Diagramas UML** | Docs | `documentacion/DOCUMENTACION_UML.md` |
| **Cómo instalar** | Docs | `docs/guia_despliegue.md` |
| **Cómo usar** | Docs | `docs/manual_usuario.md` |
| **Detalles técnicos** | Docs | `docs/manual_tecnico.md` |
| **Scripts SQL** | BD | `sql/instalar_todo.sql` |

---

## ✅ CHECKLIST RÁPIDO

### Para Profesor
- [ ] Leí `COMIENZA_AQUÍ.md`
- [ ] Leí `README.md`
- [ ] Revisé `DOCUMENTACION_UML.md`
- [ ] Vi los diagramas (componentes y despliegue)
- [ ] Leí `manual_usuario.md`
- [ ] Entiendo los 5 roles
- [ ] Entiendo la arquitectura MVC
- [ ] Sé dónde está el código

### Para Desarrollador
- [ ] Entiendo la arquitectura por capas
- [ ] Conozco la estructura de paquetes
- [ ] Identifiqué los patrones (DAO, Singleton, MVC)
- [ ] Sé cómo agregar una nueva funcionalidad
- [ ] Entiendo el flujo de datos (Vista → Lógica → DAO → BD)
- [ ] Puedo navegar el código fácilmente

### Para Instalar
- [ ] Tengo JDK 8+ instalado
- [ ] Tengo Oracle 10g+ disponible
- [ ] Ejecuté `instalar_todo.sql`
- [ ] Configuré `config.properties`
- [ ] Compilé con `ant clean jar`
- [ ] El JAR se ejecuta correctamente

---

## 🎯 PRÓXIMOS PASOS

### Opción 1: Entender Rápido (5-10 min)
1. Lee `README.md`
2. Abre los diagramas
3. Lee `manual_usuario.md`

### Opción 2: Aprender Profundo (1-2 horas)
1. Lee `arquitectura.md`
2. Explora `src/gestionpracticas/`
3. Lee `manual_tecnico.md`
4. Revisa BD en `diccionario_datos.md`

### Opción 3: Instalar y Ejecutar (30 min)
1. Sigue `guia_despliegue.md`
2. Ejecuta `instalar_todo.sql`
3. Compila con `ant clean jar`
4. Ejecuta `ejecutar.bat`

---

## 📞 DUDAS FRECUENTES

**P: ¿Por dónde empiezo?**  
A: `COMIENZA_AQUÍ.md` → `README.md` → `ÍNDICE.md`

**P: ¿Dónde está el código?**  
A: `Software_Gestion_Practicas/Gestion_Practicas/src/gestionpracticas/`

**P: ¿Cómo instalo?**  
A: `Software_Gestion_Practicas/Gestion_Practicas/docs/guia_despliegue.md`

**P: ¿Cuáles son los usuarios?**  
A: Ver tabla en `README.md`

**P: ¿Dónde están los diagramas?**  
A: `Software_Gestion_Practicas/Gestion_Practicas/diagramas/`

---

**🎯 Comienza por:** [`COMIENZA_AQUÍ.md`](COMIENZA_AQUÍ.md)

*Última actualización: Agosto 2026*
