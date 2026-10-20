// SCHEMA MONGODB CON REGISTRO DE RECHAZOS Y NOTIFICACIONES
db = db.getSiblingDB('sigpra_documental');

db.createCollection('auditoria_eventos');
db.createCollection('notificaciones_usuarios');

print("Colecciones e índices de MongoDB actualizados para SIGPRA.");