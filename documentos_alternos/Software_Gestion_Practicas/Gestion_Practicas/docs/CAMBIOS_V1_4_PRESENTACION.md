# Cambios v1.4 - Presentación final

Esta versión corrige el flujo solicitado para sustentación:

## Estudiante
- El estudiante solo registra actividades y horas.
- Ya no ve botones de aprobar, rechazar, actualizar o eliminar horas.
- El histórico muestra colores:
  - Naranja: pendiente o registrado.
  - Verde: aprobado o finalizado.
  - Rojo: rechazado.
- El estudiante puede abrir **Mi matrícula / proceso** para ver o solicitar matrícula a una práctica/grupo.
- El estudiante puede registrar **Reportes / Incidencias** y consultar la respuesta cuando director o coordinador la atiendan.

## Coordinador
- En **Grupos de Práctica** se agregó el botón **Vincular estudiante**.
- Desde **Seguimiento integral** puede seleccionar un grupo y consultar:
  - estudiantes del grupo,
  - práctica,
  - institución,
  - docente asignado,
  - etapa,
  - horas requeridas,
  - horas reportadas,
  - horas aprobadas,
  - actividades,
  - evaluaciones.
- Se corrigió la consulta que fallaba con `ORA-00904: A.ID_USUARIO identificador no válido`.

## Horas y actividades
- El sistema prioriza `REGISTRO_ACTIVIDAD` para guardar la actividad real del estudiante.
- Si la tabla no es compatible, usa `HORAS_PRACTICA` como respaldo.
- La institución, docente o coordinador pueden validar horas desde **Control de horas**.

## Reportes e incidencias
- Nuevo módulo **Reportes / Incidencias** para todos los roles.
- Todos los usuarios pueden crear reportes o incidencias.
- Solo director y coordinador pueden responder y descargar PDF.
- El usuario que creó el reporte ve si está pendiente o respondido.

## Script recomendado
Ejecutar en SQL*Plus con el usuario `PROYECTOJD`:

```sql
@sql/99_patch_presentacion_v14.sql
```

El script no borra datos; solo agrega columnas auxiliares si faltan.
