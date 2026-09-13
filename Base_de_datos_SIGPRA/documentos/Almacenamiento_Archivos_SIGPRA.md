# Almacenamiento de archivos - SIGPRA

## 1. Objetivo

Definir como se conservaran los soportes documentales cargados por estudiantes, docentes asesores y coordinadores.

## 2. Tipos de archivo esperados

- PDF.
- Imagenes JPG o PNG.
- Documentos DOCX.
- Hojas XLSX, cuando se requiera.
- Formatos firmados digitalizados.

## 3. Estructura sugerida

```text
storage/
  sigpra/
    2026-2/
      asignacion-15/
        actividad-80/
          evidencia-planeacion.pdf
          evidencia-fotografia.jpg
        visita-4/
          acta-visita.pdf
```

## 4. Reglas de nombrado

- Usar minusculas.
- Evitar espacios.
- Usar guion medio.
- Conservar extension original.
- Incluir contexto funcional: actividad, visita o evaluacion.

Ejemplo:

```text
evidencia-planeacion-clase-2026-09-13.pdf
```

## 5. Datos que deben guardarse en PostgreSQL

En la tabla `evidencia`:

- Identificador de actividad.
- Nombre original del archivo.
- Tipo de archivo.
- Ruta o URL.
- Hash.
- Estado.
- Fecha de carga.
- Identificador del documento en MongoDB, si aplica.

## 6. Datos que deben guardarse en MongoDB

En la coleccion `evidencias_documentales`:

- Tipo de soporte.
- Metadatos variables.
- Usuario que cargo el archivo.
- Relacion con actividad o visita.
- Versiones o historial documental, si aplica.

## 7. Seguridad

- Validar extension y tipo MIME.
- Rechazar archivos ejecutables.
- Calcular hash para detectar alteraciones.
- Restringir lectura segun rol.
- Mantener copia de respaldo.
- Conservar soportes por el periodo definido por la institucion.

