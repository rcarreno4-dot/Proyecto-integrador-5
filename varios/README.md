# SIGPRA — Software Integral de Gestión de Prácticas Académicas

Proyecto Integrador de quinto semestre, Programa de Ingeniería de Sistemas, Universidad de Investigación y Desarrollo (UDI). Período académico II-2026.

SIGPRA es una aplicación web para centralizar la planificación, asignación, seguimiento y evaluación de las prácticas pedagógicas de un programa de licenciatura, junto con la generación de reportes e indicadores para procesos de acreditación.

## Integrantes

- Jesús Daniel Rueda Castillo
- Alonso Andrés Henao Roa
- Rafael Fabián Carreño Barrera

## Cursos vinculados

- Ingeniería del Software II
- Programación III
- Teoría General de Sistemas

## Actores del sistema

| Actor | Responsabilidad principal |
|---|---|
| Gestor de Prácticas | Planifica el periodo de práctica, gestiona instituciones y convenios, asigna estudiantes a instituciones y docentes, y consulta los informes e indicadores de cumplimiento. |
| Estudiante | Registra las horas de práctica ejecutadas y carga las evidencias y soportes documentales exigidos. |
| Docente Asesor | Realiza el seguimiento y la evaluación pedagógica de los estudiantes asignados, y registra las observaciones de sus visitas. |

## Casos de uso

- **Planificar práctica académica** — configura la intensidad horaria reglamentaria y las reglas del periodo lectivo.
- **Gestionar asignación de estudiantes** — vincula al estudiante con una institución con convenio activo y le asigna un docente.
- **Gestionar instituciones y convenios** — administra el registro legal de instituciones receptoras y la vigencia de los convenios.
- **Gestionar informes e indicadores** — consolida métricas de cumplimiento para los informes de acreditación.
- **Gestionar evidencias y cumplimientos de horas** — el estudiante registra horas y carga soportes documentales.
- **Realizar seguimiento y evaluación** — el docente asesor registra visitas, evalúa y retroalimenta.

## Arquitectura y tecnología

- Arquitectura web en capas, con una API REST intermedia entre el cliente y las bases de datos.
- **PostgreSQL** para la información transaccional que requiere integridad referencial: usuarios, estudiantes, instituciones, convenios, asignaciones, seguimiento y evaluación.
- **MongoDB** para evidencias digitales y eventos de auditoría, por su naturaleza de esquema más flexible.
- Autenticación por rol resuelta desde el servidor (el rol no se elige al iniciar sesión).

## Estado actual del proyecto

**Primera entrega — 30 de agosto de 2026** (completada)
Propuesta del proyecto, arquitectura, análisis de requerimientos, diagrama de casos de uso, diagrama de dominio, modelo entidad-relación y prototipo de mediana fidelidad del panel del Estudiante.

**Segundo avance — 11 de octubre de 2026** (en curso)
Prototipo de alta fidelidad, modelo relacional, diccionario de datos, código documentado y CRUD funcional sobre al menos el 50% de los casos de uso.

**Entrega final — 15 de noviembre de 2026**
Aplicación funcional completa, reportes, auditoría, pruebas, documentación técnica y de usuario, artículo IEEE y video demostrativo.

## Estructura del repositorio

```
Proyecto-integrador-5/
├── README.md
└── Primera_entrega/
    └── Anexos/
        ├── Casos_de_uso.png
        ├── Diagrama_Dominio.png
        └── Modelo_Entidad_Relacion_SIGPRA.png
```

*(La carpeta `Software_Gestion_Practicas` y el código fuente se agregarán a partir del segundo avance.)*

## Anexos de la primera entrega

- [Casos de uso](https://github.com/rcarreno4-dot/Proyecto-integrador-5/blob/main/Primera_entrega/Anexos/Casos_de_uso.png)
- [Diagrama de dominio](https://github.com/rcarreno4-dot/Proyecto-integrador-5/blob/main/Primera_entrega/Anexos/Diagrama_Dominio.png)
- [Modelo entidad-relación](https://github.com/rcarreno4-dot/Proyecto-integrador-5/blob/main/Primera_entrega/Anexos/Modelo_Entidad_Relacion_SIGPRA.png)

---

**Última actualización:** agosto de 2026
