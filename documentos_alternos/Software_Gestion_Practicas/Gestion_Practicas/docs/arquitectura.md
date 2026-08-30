# Arquitectura técnica

```text
Vista Swing
   ↓
Controlador/Formularios
   ↓
Servicios de negocio
   ↓
DAO / JDBC
   ↓
Oracle 10g+ / SQL*Plus
```

## Paquetes

- `com.gestionpracticas.vista`: pantallas por rol.
- `com.gestionpracticas.modelo`: entidades/DTO.
- `com.gestionpracticas.dao`: persistencia.
- `com.gestionpracticas.logica`: lógica existente del prototipo.
- `com.gestionpracticas.servicio`: reglas nuevas de trazabilidad, documentos, etapas y PDF.
- `com.gestionpracticas.util`: conexión, utilidades y helpers.

## Flujo de cambio de etapa

1. El servicio consulta horas aprobadas.
2. Verifica evaluación y documentos.
3. Actualiza `MATRICULA_PRACTICA`.
4. Inserta `HISTORIAL_REVISION`.
5. Registra auditoría en `LOG_ACTIVIDAD`.

## Flujo de reporte PDF

1. El usuario solicita reporte.
2. `ReportePdfService` consulta Oracle.
3. iTextPDF crea el documento.
4. Se agrega hash verificable.
5. Se registra salida en `REPORTE` o `CERTIFICADO_PRACTICA`.
