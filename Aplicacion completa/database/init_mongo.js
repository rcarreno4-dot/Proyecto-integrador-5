// ==============================================================================
// COLECCIÃ“N Y DOCUMENTOS MONGODB (SIGPRA - BITÃCORAS CU-04 Y CU-05)
// ==============================================================================

db = db.getSiblingDB('sigpra_nosql');
db.actividades_bitacora.drop();

db.createCollection('actividades_bitacora', {
   validator: {
      $jsonSchema: {
         bsonType: "object",
         required: [ "estudiante_id", "fecha_actividad", "horas_reportadas", "descripcion", "estado" ],
         properties: {
            estudiante_id: { bsonType: "int" },
            fecha_actividad: { bsonType: "string" },
            horas_reportadas: { bsonType: "int", minimum: 1, maximum: 12 },
            descripcion: { bsonType: "string" },
            soporte_adjunto: { bsonType: "string" },
            estado: { enum: [ "Pendiente", "Aprobado", "Rechazado" ] },
            observacion_docente: { bsonType: "string" },
            fecha_registro: { bsonType: "date" }
         }
      }
   }
});

db.actividades_bitacora.insertMany([
   {
      estudiante_id: 4,
      estudiante_nombre: "Juan Rueda",
      fecha_actividad: "2026-02-15",
      horas_reportadas: 8,
      descripcion: "AnÃ¡lisis de requerimientos y diseÃ±o de modelos relacionales en PostgreSQL y NoSQL en MongoDB.",
      soporte_adjunto: "diagrama_arquitectura.pdf",
      estado: "Aprobado",
      observacion_docente: "Excelente sustentaciÃ³n tÃ©cnica de esquemas.",
      fecha_registro: new Date()
   },
   {
      estudiante_id: 4,
      estudiante_nombre: "Juan Rueda",
      fecha_actividad: "2026-02-22",
      horas_reportadas: 8,
      descripcion: "Desarrollo de interfaces grÃ¡ficas interactivas con CSS3, animaciones y mÃ³dulos JS por rol.",
      soporte_adjunto: "pantallazos_interfaz.png",
      estado: "Pendiente",
      observacion_docente: "",
      fecha_registro: new Date()
   }
]);