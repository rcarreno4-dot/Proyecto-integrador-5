// ==============================================================================
// BASE DE DATOS MONGODB - SIGPRA (MÃ³dulo Documental y Evidencias)
// ==============================================================================

db = db.getSiblingDB('sigpra_nosql_db');

db.createCollection('registros_actividades');
db.createCollection('cuestionarios_respuestas');
db.createCollection('auditoria_bitacora');

// Documentos Semilla de Ejemplo
db.registros_actividades.insertMany([
  {
    asignacion_id: 1,
    estudiante_id: 4,
    estudiante_nombre: "Juan Rueda",
    fecha_actividad: new Date("2026-02-15"),
    horas_ejecutadas: 8,
    descripcion_actividad: "AnÃ¡lisis de requerimientos y diseÃ±o del esquema NoSQL en MongoDB para el mÃ³dulo de bitÃ¡coras.",
    soportes_archivos: [
      { nombre: "diagrama_arquitectura.png", url: "/uploads/diagrama_arquitectura.png" },
      { nombre: "acta_reunion.pdf", url: "/uploads/acta_reunion.pdf" }
    ],
    estado_validacion: "Aprobado", // Pendiente, Aprobado, Rechazado
    observaciones_docente: "Excelente trabajo en la delimitaciÃ³n de atributos NoSQL.",
    validado_por_docente_id: 3,
    fecha_validacion: new Date("2026-02-16")
  },
  {
    asignacion_id: 1,
    estudiante_id: 4,
    estudiante_nombre: "Juan Rueda",
    fecha_actividad: new Date("2026-02-22"),
    horas_ejecutadas: 8,
    descripcion_actividad: "ImplementaciÃ³n de endpoints REST en microservicio de actividades.",
    soportes_archivos: [
      { nombre: "evidencia_pruebas.pdf", url: "/uploads/evidencia_pruebas.pdf" }
    ],
    estado_validacion: "Pendiente",
    observaciones_docente: "",
    validado_por_docente_id: null,
    fecha_validacion: null
  }
]);

db.cuestionarios_respuestas.insertMany([
  {
    asignacion_id: 1,
    fase: "DiagnÃ³stico Inicial",
    preguntas_respuestas: [
      { pregunta: "Â¿Cuenta con equipo de cÃ³mputo asignado?", respuesta: "SÃ­, la empresa proporcionÃ³ estaciÃ³n de trabajo." },
      { pregunta: "Â¿Tiene plan de trabajo aprobado?", respuesta: "SÃ­, avalado por el tutor empresarial." }
    ],
    fecha_respuesta: new Date("2026-02-10")
  }
]);

print("Base de datos sigpra_nosql_db inicializada con colecciones y datos de prueba.");
