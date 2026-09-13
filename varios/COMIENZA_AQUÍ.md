# 📌 COMIENZA AQUÍ

Bienvenido al repositorio de **PROYECTOJD** - Sistema de Gestión de Prácticas Académicas.

---

## ⚡ Inicio en 2 Minutos

### Opción 1: Entender Rápido (Si eres Profesor)
```
1. Lee: README.md (5 minutos)
2. Ve: GUÍA_INICIO.md (5 minutos) 
3. Mira: Diagramas en Software_Gestion_Practicas/Gestion_Practicas/diagramas/
```

### Opción 2: Explorar Fondo (Si eres Desarrollador)
```
1. Lee: README.md > Arquitectura MVC
2. Explora: Software_Gestion_Practicas/Gestion_Practicas/src/gestionpracticas/
3. Lee: docs/manual_tecnico.md
```

### Opción 3: Instalar y Ejecutar (Si quieres verlo funcionando)
```
1. Ve a: Software_Gestion_Practicas/Gestion_Practicas/
2. Sigue: docs/guia_despliegue.md
3. Usuario prueba: admin@proyectojd.com / 123456
```

---

## 📂 Estructura del Repositorio

```
📦 Proyecto/
│
├── 📄 README.md ........................ LÉEME PRIMERO (guía completa)
├── 📄 GUÍA_INICIO.md .................. Guía paso a paso
├── 📄 ÍNDICE.md ....................... Mapa de navegación
├── 📄 COMIENZA_AQUÍ.md ................ Este archivo
│
├── 📦 Software_Gestion_Practicas/
│   └── Gestion_Practicas/
│       ├── 📄 README.txt .............. Instrucciones iniciales
│       ├── 📄 README.md ............... Alternativo
│       ├── 📄 pom.xml ................. Dependencias Maven
│       ├── ejecutar.bat/sh ............ Scripts de ejecución
│       │
│       ├── 📁 src/gestionpracticas/   ⭐ CÓDIGO FUENTE
│       │   ├── modelo/ ............... Entidades del sistema
│       │   ├── dao/ .................. Acceso a datos (CRUD)
│       │   ├── logica/ ............... Lógica de negocio
│       │   ├── servicio/ ............. Servicios especiales
│       │   ├── vista/ ................ Interfaces gráficas
│       │   └── util/ ................. Utilidades y conexión BD
│       │
│       ├── 📁 lib/ ................... Librerías (Oracle, PDF)
│       │   ├── ojdbc14.jar
│       │   └── itextpdf-5.5.13.3.jar
│       │
│       ├── 📁 sql/ ................... Scripts Base de Datos
│       │   ├── instalar_todo.sql
│       │   ├── 00_limpiar_esquema.sql
│       │   ├── 01_creacion_tablas_oracle10g.sql
│       │   ├── 02_funciones_procedimientos_triggers.sql
│       │   └── ... (más scripts)
│       │
│       ├── 📁 config/ ................ Configuración
│       │   └── config.properties
│       │
│       ├── 📁 resources/ ............. Recursos
│       │   └── config.properties
│       │
│       ├── 📁 docs/ ⭐ DOCUMENTACIÓN
│       │   ├── manual_usuario.md
│       │   ├── manual_tecnico.md
│       │   ├── arquitectura.md
│       │   ├── diccionario_datos.md
│       │   ├── guia_despliegue.md
│       │   ├── plan_comercial.md
│       │   ├── video_demo_guion.md
│       │   └── CAMBIOS_V1_4_PRESENTACION.md
│       │
│       ├── 📁 documentacion/
│       │   ├── DOCUMENTACION_UML.md
│       │   └── MANUAL_TECNICO_RESUMIDO.md
│       │
│       ├── 📁 diagramas/ ⭐ DIAGRAMAS UML
│       │   ├── diagrama_componentes.puml
│       │   └── diagrama_despliegue.puml
│       │
│       └── 📁 dist/ .................. JAR compilado (después de build)
│
├── 📁 documentos_alternos/ ............ Documentación adicional
├── 📁 prototipos/ ..................... Prototipos UI
├── 📁 assets/ ......................... Recursos diversos
├── 📁 lib/ ............................ Librerías externas
│
├── 📄 LICENSE ......................... Licencia MIT
├── 📄 schema.sql ...................... Schema base de datos
└── .gitignore ......................... Configuración Git
```

---

## 🎯 ¿Qué Busco?

### Entender qué es el proyecto
→ Lee **[README.md](README.md)**

### Ver la arquitectura
→ Lee **[DOCUMENTACION_UML.md](Software_Gestion_Practicas/Gestion_Practicas/documentacion/DOCUMENTACION_UML.md)**

### Ver los diagramas UML
→ Abre **[diagramas_componentes.puml](Software_Gestion_Practicas/Gestion_Practicas/diagramas/diagrama_componentes.puml)** y **[diagrama_despliegue.puml](Software_Gestion_Practicas/Gestion_Practicas/diagramas/diagrama_despliegue.puml)**

### Entender cómo se usa
→ Lee **[manual_usuario.md](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_usuario.md)**

### Revisar el código
→ Explora **[src/gestionpracticas/](Software_Gestion_Practicas/Gestion_Practicas/src/gestionpracticas/)**

### Aspectos técnicos profundos
→ Lee **[manual_tecnico.md](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_tecnico.md)**

### Estructura de base de datos
→ Lee **[diccionario_datos.md](Software_Gestion_Practicas/Gestion_Practicas/docs/diccionario_datos.md)**

### Instalar y ejecutar
→ Sigue **[guia_despliegue.md](Software_Gestion_Practicas/Gestion_Practicas/docs/guia_despliegue.md)**

### Navegar completo
→ Consulta **[ÍNDICE.md](ÍNDICE.md)**

---

## 🚀 Instalación Rápida

```bash
# 1. Navegar al proyecto
cd Software_Gestion_Practicas/Gestion_Practicas

# 2. Crear base de datos (SQL*Plus)
@sql/instalar_todo.sql

# 3. Compilar
ant clean jar

# 4. Ejecutar
ejecutar.bat    # Windows
# o
java -cp "dist/GestionPracticas.jar;lib/ojdbc14.jar;lib/itextpdf-5.5.13.3.jar" com.gestionpracticas.Main
```

### Usuario de Prueba
```
Email: admin@proyectojd.com
Pass:  123456
```

---

## 📊 Resumen Ejecutivo

| Aspecto | Detalles |
|--------|----------|
| **Tipo** | Aplicación Java Swing + Oracle |
| **Versión** | 1.4 (Producción) |
| **Líneas de código** | ~8,000+ |
| **Clases** | 50+ |
| **Tablas Oracle** | 20+ |
| **Roles** | 5 (Director, Coordinador, Docente, Estudiante, Institución) |
| **Documentación** | 10+ archivos |
| **Licencia** | MIT |

---

## ✅ Qué Encontrarás Aquí

✔️ Código fuente Java completo  
✔️ Arquitectura MVC bien organizada  
✔️ Base de datos Oracle 10g+  
✔️ Documentación técnica detallada  
✔️ Manuales de usuario  
✔️ Diagramas UML  
✔️ Scripts SQL listos para instalar  
✔️ JAR compilado y ejecutable  
✔️ Configuración portable  
✔️ Auditoría y trazabilidad  

---

## 🎓 Para Estudiantes de Ingeniería

Este proyecto integra:
- ✅ Ingeniería de Software I (Diseño MVC, patrones)
- ✅ Programación II (Java Swing)
- ✅ Bases de Datos II (Oracle SQL)
- ✅ UML (Diagramas de componentes y despliegue)

Perfectos para aprender:
- Arquitectura por capas
- JDBC y DAO pattern
- GUI con Swing
- Diseño de BD relacional
- Auditoría y trazabilidad

---

## 🔗 Navegación Principal

| Enlace | Para |
|--------|------|
| [README.md](README.md) | Guía completa |
| [GUÍA_INICIO.md](GUÍA_INICIO.md) | Paso a paso |
| [ÍNDICE.md](ÍNDICE.md) | Mapa completo |
| [Software_Gestion_Practicas/Gestion_Practicas/src/](Software_Gestion_Practicas/Gestion_Practicas/src/gestionpracticas/) | Código fuente |
| [Software_Gestion_Practicas/Gestion_Practicas/docs/](Software_Gestion_Practicas/Gestion_Practicas/docs/) | Documentación |
| [Software_Gestion_Practicas/Gestion_Practicas/diagramas/](Software_Gestion_Practicas/Gestion_Practicas/diagramas/) | Diagramas UML |

---

## ⏱️ Tiempo Estimado de Revisión

| Audiencia | Tiempo |
|-----------|--------|
| Profesor (revisión rápida) | 30-45 minutos |
| Desarrollador (entender código) | 1-2 horas |
| Instalación completa | 20-30 minutos |

---

## 📞 Preguntas?

Cada sección tiene su documentación:
- **¿Qué es?** → [README.md](README.md)
- **¿Cómo inicio?** → [GUÍA_INICIO.md](GUÍA_INICIO.md)
- **¿Dónde está todo?** → [ÍNDICE.md](ÍNDICE.md)
- **¿Cómo lo instalo?** → [guia_despliegue.md](Software_Gestion_Practicas/Gestion_Practicas/docs/guia_despliegue.md)
- **¿Cómo funciona?** → [manual_usuario.md](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_usuario.md)
- **¿Cómo se hace?** → [manual_tecnico.md](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_tecnico.md)

---

**¡Listo para empezar!** ✨

**Próximo paso:** Lee [README.md](README.md)

---

*Última actualización: Agosto 2026*
