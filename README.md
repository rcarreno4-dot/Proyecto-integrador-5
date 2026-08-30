# PROYECTOJD - Sistema de Gestión de Prácticas Académicas

![Licencia](https://img.shields.io/badge/Licencia-MIT-blue.svg)
![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)
![Oracle](https://img.shields.io/badge/Oracle-10g%2B-red.svg)
![Estado](https://img.shields.io/badge/Estado-Producción-green.svg)

---

## 📋 Descripción General

**PROYECTOJD** es un sistema integral desarrollado en **Java Swing** para la gestión centralizada de prácticas académicas universitarias. Facilita la coordinación entre estudiantes, docentes, coordinadores, directores e instituciones receptoras mediante:

- ✅ Control de matrícula y grupos de práctica
- ✅ Registro y confirmación de horas
- ✅ Evaluación con rúbricas parametrizables
- ✅ Gestión de evidencias y documentos
- ✅ Trazabilidad y auditoría completa
- ✅ Generación de reportes y certificados PDF verificables
- ✅ Arquitectura MVC por capas

---

## 📁 Estructura del Repositorio

```
Proyecto/
├── 📄 README.md                      ← Estás aquí (guía principal)
├── 📄 ÍNDICE.md                      ← Navegación completa
├── 📄 LICENSE                        ← Licencia MIT
│
├── 📦 Software_Gestion_Practicas/    ← CÓDIGO FUENTE PRINCIPAL
│   └── Gestion_Practicas/
│       ├── src/                      ← Código Java (capas MVC)
│       ├── lib/                      ← Librerías (Oracle JDBC, iTextPDF)
│       ├── sql/                      ← Scripts de base de datos
│       ├── config/                   ← Configuración
│       ├── pom.xml                   ← Dependencias Maven
│       └── ejecutar.bat/.sh          ← Scripts de ejecución
│
├── 📚 documentos_alternos/           ← DOCUMENTACIÓN COMPLEMENTARIA
│   ├── diagramas_primera_entrega.md
│   ├── Primer_Entrega_Proyecto_Integrador/
│   └── astah_export/
│
├── 🗂️ prototipos/                    ← Prototipos de UI
│   └── prototipo_mediana_fidelidad.html
│
└── 📊 schema.sql                     ← Esquema base de datos
└── 📄 assets/                        ← Recursos adicionales
```

---

## 🚀 Inicio Rápido

### Prerequisitos
- **Java JDK 8+** (recomendado JDK 21+)
- **Oracle Database 10g XE** o superior
- **NetBeans IDE** 17+ (recomendado)
- Puertos: 1521 (Oracle), acceso a red interna

### Instalación en 5 pasos

```bash
# 1. Navegar a la carpeta del proyecto
cd Software_Gestion_Practicas/Gestion_Practicas

# 2. Verificar librerías en lib/
# - ojdbc14.jar o ojdbc8.jar
# - itextpdf-5.5.13.3.jar

# 3. Crear esquema en Oracle (SQL*Plus)
@sql/instalar_todo.sql

# 4. Compilar con Ant
ant clean jar

# 5. Ejecutar (Windows)
ejecutar.bat
```

### Usuarios de Prueba

| Rol | Email | Contraseña |
|-----|-------|-----------|
| **Director** | admin@proyectojd.com | 123456 |
| **Coordinador** | carlos@proyectojd.com | 123456 |
| **Docente** | maria@proyectojd.com | 123456 |
| **Estudiante** | juan@proyectojd.com | 123456 |
| **Institución** | empresa@proyectojd.com | 123456 |

---

## 📖 Guía de Navegación

### Para el Profesor/Revisor

| Necesito... | Debo revisar... | Archivo |
|-------------|-----------------|---------|
| **Entender la arquitectura** | Diagrama de componentes | `Software_Gestion_Practicas/Gestion_Practicas/diagramas/diagrama_componentes.puml` |
| **Ver flujo de despliegue** | Diagrama de despliegue | `Software_Gestion_Practicas/Gestion_Practicas/diagramas/diagrama_despliegue.puml` |
| **Revisar documentación UML** | Análisis completo | `Software_Gestion_Practicas/Gestion_Practicas/documentacion/DOCUMENTACION_UML.md` |
| **Entender manualmente cómo usar** | Manual de usuario | `Software_Gestion_Practicas/Gestion_Practicas/docs/manual_usuario.md` |
| **Revisar detalles técnicos** | Manual técnico | `Software_Gestion_Practicas/Gestion_Practicas/docs/manual_tecnico.md` |
| **Ver estructura base de datos** | Diccionario de datos | `Software_Gestion_Practicas/Gestion_Practicas/docs/diccionario_datos.md` |
| **Desplegar en producción** | Guía de despliegue | `Software_Gestion_Practicas/Gestion_Practicas/docs/guia_despliegue.md` |
| **Cambios en v1.4** | Release notes | `Software_Gestion_Practicas/Gestion_Practicas/docs/CAMBIOS_V1_4_PRESENTACION.md` |

### Para Desarrolladores

| Necesito... | Ubicación |
|-------------|-----------|
| **Código fuente** | `src/gestionpracticas/` |
| **Modelos/Entidades** | `src/gestionpracticas/modelo/` |
| **Acceso a datos (DAO)** | `src/gestionpracticas/dao/` |
| **Lógica de negocio** | `src/gestionpracticas/logica/` |
| **Vistas/Interfaces** | `src/gestionpracticas/vista/` |
| **Utilidades** | `src/gestionpracticas/util/` |
| **Scripts SQL** | `sql/` |
| **Configuración** | `config/` o `resources/` |

---

## 🏗️ Arquitectura MVC

```
VISTA (Java Swing)
    ↓
CONTROLADOR (Eventos, navegación)
    ↓
LÓGICA DE NEGOCIO (Servicios, validaciones)
    ↓
DAO (Acceso a datos CRUD)
    ↓
ORACLE DATABASE (Persistencia)
```

### Capas del Sistema

| Capa | Paquete | Responsabilidad |
|------|---------|-----------------|
| **Vista** | `com.gestionpracticas.vista` | Interfaces gráficas por rol (Director, Coordinador, Docente, Estudiante, Institución) |
| **Controlador** | `com.gestionpracticas.vista` | Gestión de eventos, validación inicial, redirección por rol |
| **Lógica** | `com.gestionpracticas.logica`<br>`com.gestionpracticas.servicio` | Reglas de negocio, trazabilidad, cambio de etapas, generación de reportes |
| **DAO** | `com.gestionpracticas.dao` | Operaciones CRUD, consultas SQL, acceso a Oracle mediante JDBC |
| **Modelo** | `com.gestionpracticas.modelo` | Entidades del dominio (Usuario, Practica, Grupo, Evaluacion, etc.) |
| **Utilidades** | `com.gestionpracticas.util` | Conexión singleton a BD, validadores, helpers de pantalla |

---

## 🗄️ Base de Datos

**Motor:** Oracle 10g XE o superior  
**Esquema:** `PROYECTOJD`  
**Objetos:** 39 componentes (tablas, secuencias, funciones, procedimientos, triggers)

### Tablas Principales

- **USUARIO** → Usuarios del sistema con rol y credenciales
- **PROGRAMA** → Programas académicos
- **CURSO** → Cursos asociados a programas
- **PRACTICA** → Prácticas académicas
- **GRUPO** → Grupos de práctica con docente e institución
- **MATRICULA_PRACTICA** → Vinculación estudiante-práctica-grupo
- **REGISTRO_ACTIVIDAD** → Actividades registradas por estudiantes
- **EVALUACION** → Evaluaciones cuantitativas y cualitativas
- **RUBRICA** → Rúbricas parametrizables
- **DOCUMENTO_ESTUDIANTE** → Evidencias y documentos
- **HORAS_PRACTICA** → Horas confirmadas por institución
- **LOG_ACTIVIDAD** → Bitácora de auditoría
- **CERTIFICADO_PRACTICA** → Certificados con hash verificable

Ver detalles completos en: [`docs/diccionario_datos.md`](Software_Gestion_Practicas/Gestion_Practicas/docs/diccionario_datos.md)

---

## 📋 Flujos Principales

### 1️⃣ Flujo de Estudiante
1. **Ingreso** → Autenticación por correo/contraseña
2. **Matrícula** → Director asigna estudiante a práctica y grupo
3. **Registro de Actividades** → Estudiante carga horas y evidencias
4. **Evaluación** → Docente evalúa con rúbrica
5. **Cambio de Etapa** → Sistema transiciona ELECTIVA → PRODUCTIVA
6. **Certificado** → Descarga PDF con hash verificable

### 2️⃣ Flujo de Confirmación de Horas
1. **Registro** → Estudiante registra actividades
2. **Confirmación** → Institución receptora valida y confirma horas
3. **Actualización** → Base de datos refleja horas ejecutadas
4. **Auditoría** → LOG_ACTIVIDAD registra cambios

### 3️⃣ Flujo de Evaluación
1. **Rúbrica** → Docente define criterios y niveles de desempeño
2. **Evaluación** → Docente califica estudiante
3. **Retroalimentación** → Docente registra observaciones
4. **Validación** → Sistema verifica consistencia datos/etapa

---

## 📚 Documentación Completa

### Orientado a Profesores/Revisores
- [`DOCUMENTACION_UML.md`](Software_Gestion_Practicas/Gestion_Practicas/documentacion/DOCUMENTACION_UML.md) - Análisis de diagramas UML
- [`manual_usuario.md`](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_usuario.md) - Cómo usar el sistema
- [`CAMBIOS_V1_4_PRESENTACION.md`](Software_Gestion_Practicas/Gestion_Practicas/docs/CAMBIOS_V1_4_PRESENTACION.md) - Últimas mejoras

### Orientado a Desarrolladores
- [`manual_tecnico.md`](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_tecnico.md) - Detalles técnicos y arquitectura
- [`arquitectura.md`](Software_Gestion_Practicas/Gestion_Practicas/docs/arquitectura.md) - Flujos y componentes
- [`diccionario_datos.md`](Software_Gestion_Practicas/Gestion_Practicas/docs/diccionario_datos.md) - Esquema de BD

### Orientado a Despliegue
- [`guia_despliegue.md`](Software_Gestion_Practicas/Gestion_Practicas/docs/guia_despliegue.md) - Instalación en producción
- [`plan_comercial.md`](Software_Gestion_Practicas/Gestion_Practicas/docs/plan_comercial.md) - Propuesta de valor

### Otros
- [`video_demo_guion.md`](Software_Gestion_Practicas/Gestion_Practicas/docs/video_demo_guion.md) - Guión demostrativo de 5 min

---

## 🔍 Revisión Técnica Rápida

### Validación del Código
```bash
cd Software_Gestion_Practicas/Gestion_Practicas
ant clean jar  # Debe compilar sin errores
```

### Validación de Base de Datos
```sql
-- Conectar como usuario PROYECTOJD en Oracle SQL*Plus
@sql/04_consultas_validacion.sql
```

### Validación de Ejecución
```bash
java -cp "dist/GestionPracticas.jar;lib/ojdbc14.jar;lib/itextpdf-5.5.13.3.jar" com.gestionpracticas.Main
```

---

## 📊 Estadísticas del Proyecto

| Métrica | Valor |
|---------|-------|
| **Líneas de código Java** | ~8,000+ |
| **Clases** | 50+ |
| **Tablas Oracle** | 20+ |
| **Funciones/Procedimientos** | 10+ |
| **Roles/Módulos** | 5 |
| **Documentación** | 10 archivos |
| **Diagramas UML** | 2 (componentes, despliegue) |

---

## ✅ Checklist para el Profesor

- [ ] Revisar `DOCUMENTACION_UML.md` para entender arquitectura
- [ ] Abrir diagramas en `diagramas/diagrama_componentes.puml`
- [ ] Revisar estructura de carpetas en `src/gestionpracticas/`
- [ ] Ver scripts SQL en `sql/`
- [ ] Ejecutar instalación y probar con usuarios de prueba
- [ ] Revisar `manual_usuario.md` para flujos
- [ ] Consultar `manual_tecnico.md` para aspectos técnicos
- [ ] Verificar seguridad en `docs/manual_tecnico.md#7-seguridad`

---

## 🔗 Enlaces Importantes

| Sección | Enlace |
|---------|--------|
| **Índice Completo** | [ÍNDICE.md](ÍNDICE.md) |
| **Licencia** | [LICENSE](LICENSE) |
| **Código Fuente** | [Software_Gestion_Practicas/Gestion_Practicas/src](Software_Gestion_Practicas/Gestion_Practicas/src) |
| **Scripts SQL** | [Software_Gestion_Practicas/Gestion_Practicas/sql](Software_Gestion_Practicas/Gestion_Practicas/sql) |
| **Diagramas** | [Software_Gestion_Practicas/Gestion_Practicas/diagramas](Software_Gestion_Practicas/Gestion_Practicas/diagramas) |

---

## 📞 Soporte

Para consultas sobre:
- **Arquitectura** → Ver `docs/arquitectura.md`
- **Instalación** → Ver `docs/guia_despliegue.md`
- **Uso del sistema** → Ver `docs/manual_usuario.md`
- **Código técnico** → Ver `docs/manual_tecnico.md`

---

## 📝 Información de la Licencia

Este proyecto se distribuye bajo licencia **MIT**. Ver [`LICENSE`](LICENSE) para detalles completos.

---

**Última actualización:** Agosto 2026  
**Versión:** 1.4  
**Estado:** Producción  
**Autor:** PROYECTOJD Team
