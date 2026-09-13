// SIGPRA - Colecciones iniciales MongoDB
// Componente documental/flexible.

use("sigpra_documental");

db.createCollection("evidencias_documentales", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["actividad_id", "nombre_archivo", "archivo_url", "tipo_soporte", "creado_en"],
      properties: {
        actividad_id: { bsonType: "long" },
        evidencia_relacional_id: { bsonType: ["long", "null"] },
        nombre_archivo: { bsonType: "string" },
        archivo_url: { bsonType: "string" },
        tipo_soporte: { bsonType: "string" },
        mime_type: { bsonType: "string" },
        hash_archivo: { bsonType: ["string", "null"] },
        metadatos: { bsonType: "object" },
        creado_por_usuario_id: { bsonType: ["long", "null"] },
        creado_en: { bsonType: "date" }
      }
    }
  }
});

db.createCollection("bitacoras_visita", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["visita_id", "asignacion_id", "observaciones", "creado_en"],
      properties: {
        visita_id: { bsonType: "long" },
        asignacion_id: { bsonType: "long" },
        docente_asesor_id: { bsonType: ["long", "null"] },
        observaciones: { bsonType: "string" },
        compromisos: { bsonType: "array" },
        anexos: { bsonType: "array" },
        creado_en: { bsonType: "date" }
      }
    }
  }
});

db.createCollection("auditoria_eventos", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["evento", "usuario_id", "fecha_evento", "origen"],
      properties: {
        evento: { bsonType: "string" },
        usuario_id: { bsonType: "long" },
        rol: { bsonType: ["string", "null"] },
        entidad: { bsonType: ["string", "null"] },
        entidad_id: { bsonType: ["long", "null"] },
        origen: { bsonType: "string" },
        detalle: { bsonType: "object" },
        ip: { bsonType: ["string", "null"] },
        fecha_evento: { bsonType: "date" }
      }
    }
  }
});

db.evidencias_documentales.createIndex({ actividad_id: 1 });
db.evidencias_documentales.createIndex({ evidencia_relacional_id: 1 });
db.evidencias_documentales.createIndex({ tipo_soporte: 1, creado_en: -1 });

db.bitacoras_visita.createIndex({ visita_id: 1 }, { unique: true });
db.bitacoras_visita.createIndex({ asignacion_id: 1, creado_en: -1 });

db.auditoria_eventos.createIndex({ usuario_id: 1, fecha_evento: -1 });
db.auditoria_eventos.createIndex({ entidad: 1, entidad_id: 1 });
db.auditoria_eventos.createIndex({ fecha_evento: -1 });

db.evidencias_documentales.insertOne({
  actividad_id: NumberLong(1),
  evidencia_relacional_id: NumberLong(1),
  nombre_archivo: "planeacion_clase.pdf",
  archivo_url: "storage/sigpra/2026-2/asignacion-1/actividad-1/planeacion_clase.pdf",
  tipo_soporte: "PLANEACION",
  mime_type: "application/pdf",
  hash_archivo: "sha256-demo",
  metadatos: {
    descripcion: "Planeacion de clase cargada como ejemplo",
    version: 1
  },
  creado_por_usuario_id: NumberLong(1),
  creado_en: new Date()
});

