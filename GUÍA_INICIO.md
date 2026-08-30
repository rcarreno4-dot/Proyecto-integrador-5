# 🚀 GUÍA DE INICIO RÁPIDO

Bienvenido al Sistema de Gestión de Prácticas Académicas **PROYECTOJD**.

Esta guía te llevará a través del proyecto en **5 minutos**.

---

## 📌 PRIMERO: ¿Qué es este proyecto?

Un **sistema Java + Oracle** que gestiona:
- ✅ Prácticas académicas de estudiantes
- ✅ Evaluación con rúbricas
- ✅ Control de horas
- ✅ Reportes PDF verificables
- ✅ Auditoría y trazabilidad

---

## 📂 SEGUNDO: Estructura Principal

```
c:\Users\Fabian\OneDrive\Desktop\Proyecto\
│
├── 📄 README.md                          ← LÉEME PRIMERO
├── 📄 ÍNDICE.md                          ← Mapa completo del proyecto
├── 📄 GUÍA_INICIO.md                     ← Este archivo
├── 📄 LICENSE                            ← Licencia MIT
│
├── 📦 Software_Gestion_Practicas/        ← CÓDIGO FUENTE PRINCIPAL
│   └── Gestion_Practicas/
│       ├── src/                          ← Código Java (lo que importa)
│       ├── lib/                          ← Librerías (Oracle, PDF)
│       ├── sql/                          ← Scripts de base de datos
│       ├── docs/                         ← Documentación
│       └── pom.xml / ejecutar.bat
│
├── 📚 documentos_alternos/               ← Documentación adicional
├── 🗂️ prototipos/                        ← Prototipos UI
├── 📊 assets/                            ← Recursos varios
└── schema.sql                            ← Schema base de datos

```

---

## 🎯 TERCERO: ¿Por dónde empiezo según mi rol?

### 👨‍🏫 Si eres PROFESOR/REVISOR

**Lectura recomendada (en este orden):**

1. **5 min** → Lee [README.md](README.md)
   - Entiende qué es el proyecto
   
2. **5 min** → Lee la sección de **"Flujos Principales"** en [README.md](README.md#-flujos-principales)
   - Entiende cómo funciona
   
3. **10 min** → Abre y revisa [Software_Gestion_Practicas/Gestion_Practicas/documentacion/DOCUMENTACION_UML.md](Software_Gestion_Practicas/Gestion_Practicas/documentacion/DOCUMENTACION_UML.md)
   - Ve la arquitectura en diagramas
   
4. **5 min** → Mira los diagramas visuales:
   - [Componentes](Software_Gestion_Practicas/Gestion_Practicas/diagramas/diagrama_componentes.puml)
   - [Despliegue](Software_Gestion_Practicas/Gestion_Practicas/diagramas/diagrama_despliegue.puml)

5. **10 min** → Lee [Software_Gestion_Practicas/Gestion_Practicas/docs/manual_usuario.md](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_usuario.md)
   - Entiende cómo los usuarios lo usan

**Total: ~35 minutos para entender TODO**

---

### 👨‍💻 Si eres DESARROLLADOR

**Lectura recomendada (en este orden):**

1. **5 min** → Lee [README.md](README.md#-arquitectura-mvc)
   - Entiende las capas
   
2. **10 min** → Lee [Software_Gestion_Practicas/Gestion_Practicas/docs/arquitectura.md](Software_Gestion_Practicas/Gestion_Practicas/docs/arquitectura.md)
   - Entiende los flujos técnicos
   
3. **15 min** → Explora el código en:
   ```
   Software_Gestion_Practicas/Gestion_Practicas/src/gestionpracticas/
   ├── modelo/      ← Entidades
   ├── dao/         ← Base de datos
   ├── logica/      ← Reglas de negocio
   ├── servicio/    ← Servicios especiales
   ├── vista/       ← Interfaces (por rol)
   └── util/        ← Utilidades
   ```

4. **10 min** → Lee [Software_Gestion_Practicas/Gestion_Practicas/docs/manual_tecnico.md](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_tecnico.md)
   - Detalles técnicos profundos

5. **10 min** → Revisa [Software_Gestion_Practicas/Gestion_Practicas/docs/diccionario_datos.md](Software_Gestion_Practicas/Gestion_Practicas/docs/diccionario_datos.md)
   - Entiende la BD

**Total: ~50 minutos para entender la arquitectura**

---

### 🚀 Si quieres INSTALAR Y EJECUTAR

**Pasos rápidos:**

1. **Asegúrate de tener:**
   - ✅ Java JDK 8+ instalado
   - ✅ Oracle Database 10g+ disponible
   - ✅ NetBeans IDE (opcional pero recomendado)

2. **Navega a:**
   ```
   cd Software_Gestion_Practicas/Gestion_Practicas
   ```

3. **Instala la base de datos:**
   ```
   Abre SQL*Plus y conecta como usuario PROYECTOJD
   @sql/instalar_todo.sql
   ```

4. **Compila el proyecto:**
   ```
   ant clean jar
   ```

5. **Ejecuta (Windows):**
   ```
   ejecutar.bat
   ```
   O manualmente:
   ```
   java -cp "dist/GestionPracticas.jar;lib/ojdbc14.jar;lib/itextpdf-5.5.13.3.jar" com.gestionpracticas.Main
   ```

6. **Ingresa con usuario de prueba:**
   ```
   Email: admin@proyectojd.com
   Password: 123456
   ```

**Ver detalles completos en:** [Software_Gestion_Practicas/Gestion_Practicas/docs/guia_despliegue.md](Software_Gestion_Practicas/Gestion_Practicas/docs/guia_despliegue.md)

---

## 🔑 CUARTO: Datos de Prueba

```
DIRECTOR:        admin@proyectojd.com      / 123456
COORDINADOR:     carlos@proyectojd.com     / 123456
DOCENTE:         maria@proyectojd.com      / 123456
ESTUDIANTE:      juan@proyectojd.com       / 123456
INSTITUCIÓN:     empresa@proyectojd.com    / 123456
```

---

## 📚 QUINTO: Documentación Organizada

### Conceptos
- [`README.md`](README.md) - Visión general
- [`ÍNDICE.md`](ÍNDICE.md) - Mapa completo

### Usuarios
- [`docs/manual_usuario.md`](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_usuario.md) - Cómo usar el sistema
- [`docs/video_demo_guion.md`](Software_Gestion_Practicas/Gestion_Practicas/docs/video_demo_guion.md) - Guión de demostración 5 min

### Técnico
- [`docs/manual_tecnico.md`](Software_Gestion_Practicas/Gestion_Practicas/docs/manual_tecnico.md) - Detalles profundos
- [`docs/arquitectura.md`](Software_Gestion_Practicas/Gestion_Practicas/docs/arquitectura.md) - Flujos técnicos
- [`documentacion/DOCUMENTACION_UML.md`](Software_Gestion_Practicas/Gestion_Practicas/documentacion/DOCUMENTACION_UML.md) - Análisis UML

### Base de Datos
- [`docs/diccionario_datos.md`](Software_Gestion_Practicas/Gestion_Practicas/docs/diccionario_datos.md) - Esquema de BD
- [`sql/`](Software_Gestion_Practicas/Gestion_Practicas/sql/) - Scripts SQL

### Despliegue
- [`docs/guia_despliegue.md`](Software_Gestion_Practicas/Gestion_Practicas/docs/guia_despliegue.md) - Instalación en producción

### Comercial/Presentación
- [`docs/plan_comercial.md`](Software_Gestion_Practicas/Gestion_Practicas/docs/plan_comercial.md) - Propuesta de valor
- [`docs/CAMBIOS_V1_4_PRESENTACION.md`](Software_Gestion_Practicas/Gestion_Practicas/docs/CAMBIOS_V1_4_PRESENTACION.md) - Últimas mejoras

---

## ✨ SEXTO: Características Principales

| Característica | Ubicación en Código |
|---|---|
| **Autenticación por rol** | `src/gestionpracticas/vista/LoginForm.java` |
| **Dashboard por rol** | `src/gestionpracticas/vista/{director,coordinador,docente,estudiante,institucion}/` |
| **Matrícula de estudiantes** | `src/gestionpracticas/dao/MatriculaPracticaDAO.java` |
| **Registro de actividades** | `src/gestionpracticas/vista/estudiante/RegistroActividadForm.java` |
| **Confirmación de horas** | `src/gestionpracticas/vista/institucion/` |
| **Evaluación con rúbrica** | `src/gestionpracticas/dao/EvaluacionDAO.java` |
| **Cambio de etapa** | `src/gestionpracticas/servicio/EtapaProductivaService.java` |
| **Generación de PDF** | `src/gestionpracticas/servicio/ReportePdfService.java` |
| **Auditoría/Trazabilidad** | `src/gestionpracticas/util/` |

---

## 🎯 CHECKLIST PARA PROFESOR

- [ ] Revisé `README.md`
- [ ] Entendí la arquitectura (MVC, 5 capas)
- [ ] Vi los diagramas UML (componentes y despliegue)
- [ ] Leí `manual_usuario.md` (flujos de usuario)
- [ ] Revisé `diccionario_datos.md` (BD)
- [ ] Entendí los 5 roles del sistema
- [ ] Validé que el código está organizado por capas
- [ ] Verifiqué trazabilidad y auditoría

---

## 🆘 Preguntas Frecuentes

**P: ¿Dónde está el código?**  
R: En `Software_Gestion_Practicas/Gestion_Practicas/src/gestionpracticas/`

**P: ¿Dónde está la documentación de BD?**  
R: En `Software_Gestion_Practicas/Gestion_Practicas/docs/diccionario_datos.md`

**P: ¿Cómo lo instalo?**  
R: Sigue `Software_Gestion_Practicas/Gestion_Practicas/docs/guia_despliegue.md`

**P: ¿Dónde veo los diagramas?**  
R: En `Software_Gestion_Practicas/Gestion_Practicas/diagramas/`

**P: ¿Qué versión es?**  
R: v1.4 (última versión con mejoras)

**P: ¿Necesito hacer algo más?**  
R: NO. El proyecto está listo para revisar y ejecutar.

---

## 🔗 Enlaces Rápidos

📍 **Navegación completa:** [ÍNDICE.md](ÍNDICE.md)

📍 **README principal:** [README.md](README.md)

📍 **Código fuente:** [Software_Gestion_Practicas/Gestion_Practicas/src/gestionpracticas/](Software_Gestion_Practicas/Gestion_Practicas/src/gestionpracticas/)

📍 **Documentación:** [Software_Gestion_Practicas/Gestion_Practicas/docs/](Software_Gestion_Practicas/Gestion_Practicas/docs/)

---

**¿Listo para revisar?** ✅

**Próximos pasos:**
1. Lee [README.md](README.md)
2. Ve a [ÍNDICE.md](ÍNDICE.md) para navegación completa
3. Explora el código en `Software_Gestion_Practicas/Gestion_Practicas/src/`

---

*Última actualización: Agosto 2026*
