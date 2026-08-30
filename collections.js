// SIGPRA - Colecciones MongoDB para primera entrega
// Base sugerida: sigpra

use('sigpra');

// Evidencias asociadas a una bitacora de PostgreSQL.
db.createCollection('evidencias', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['bitacoraId', 'estudianteId', 'nombreArchivo', 'tipoMime', 'url', 'subidoEn'],
      properties: {
        bitacoraId: { bsonType: 'long' },
        estudianteId: { bsonType: 'long' },
        nombreArchivo: { bsonType: 'string' },
        tipoMime: { bsonType: 'string' },
        tamanoBytes: { bsonType: 'long', minimum: 1 },
        url: { bsonType: 'string' },
        descripcion: { bsonType: 'string' },
        subidoEn: { bsonType: 'date' }
      }
    }
  }
});

db.evidencias.createIndex({ bitacoraId: 1 });
db.evidencias.createIndex({ estudianteId: 1, subidoEn: -1 });

// Auditoria flexible para trazabilidad de operaciones.
db.createCollection('auditoria');
db.auditoria.createIndex({ entidad: 1, entidadId: 1, ocurridoEn: -1 });
db.auditoria.createIndex({ usuarioId: 1, ocurridoEn: -1 });

// Documento de ejemplo para pruebas de insercion y consulta.
db.evidencias.insertOne({
  bitacoraId: NumberLong(1),
  estudianteId: NumberLong(1),
  nombreArchivo: 'planeacion_semana_01.pdf',
  tipoMime: 'application/pdf',
  tamanoBytes: NumberLong(245760),
  url: '/uploads/evidencias/planeacion_semana_01.pdf',
  descripcion: 'Planeacion de actividades de la primera semana',
  subidoEn: new Date()
});

// Consultas CRUD de verificacion.
db.evidencias.find({ bitacoraId: NumberLong(1) });
db.evidencias.updateOne(
  { nombreArchivo: 'planeacion_semana_01.pdf' },
  { $set: { descripcion: 'Planeacion actualizada' } }
);
db.evidencias.deleteOne({ nombreArchivo: 'planeacion_semana_01.pdf' });
