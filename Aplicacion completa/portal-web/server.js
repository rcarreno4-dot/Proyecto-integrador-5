const express = require('express');
const path = require('path');
const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());
app.use(express.static(path.join(__dirname, 'public')));

// MICROSERVICIO 1: Usuarios y Autenticación
app.get('/api/usuarios/me', (req, res) => {
    res.json({ usuario: 'Ing. Martha Gómez', rol: 'DOCENTE_ASESOR' });
});

// MICROSERVICIO 2: Prácticas, Convenios y Asignaciones
app.get('/api/practicas/periodo-activo', (req, res) => {
    res.json({ periodo: '2026-2', estado: 'PUBLICADO', estudiantes: 2 });
});

// MICROSERVICIO 3: Bitácoras, Horas y Soportes (PostgreSQL + MongoDB)
app.put('/api/bitacoras/:id/validar', (req, res) => {
    const { id } = req.params;
    const { estado, motivoRechazo } = req.body;
    console.log(`[MICROSERVICIO BITÁCORAS] Registro #${id} actualizado a ${estado}. Motivo: ${motivoRechazo || 'N/A'}`);
    res.json({ success: true, id, estado, motivoRechazo });
});

// MICROSERVICIO 4: Notificaciones y Auditoría de Eventos
app.get('/api/notificaciones', (req, res) => {
    res.json([
        { id: 1, mensaje: 'Laura Gómez envió bitácora de 4 horas para validación.', tareaId: 101 },
        { id: 2, mensaje: 'Juan Rueda envió bitácora de 8 horas para validación.', tareaId: 102 }
    ]);
});

app.listen(PORT, () => {
    console.log(`Servidor SIGPRA de 4 Microservicios iniciado en http://localhost:${PORT}`);
});