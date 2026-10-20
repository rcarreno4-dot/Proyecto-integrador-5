const db = require('../config/db');

// CU-01: Programar periodo de práctica
exports.programarPeriodo = async (req, res) => {
  try {
    const { nombre, fechaInicio, fechaFin } = req.body;
    // Sustituir con tu consulta SQL real:
    // const result = await db.query('INSERT INTO periodos (nombre, fecha_inicio, fecha_fin) VALUES ($1, $2, $3) RETURNING *', [nombre, fechaInicio, fechaFin]);
    res.json({ message: 'Periodo de práctica programado con éxito (CU-01)' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// CU-02: Gestionar convenios y plazas
exports.gestionarConvenios = async (req, res) => {
  try {
    res.json({ message: 'Convenio / Plaza registrado correctamente (CU-02)' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// CU-03: Asignar practicante a plaza y docente asesor
exports.asignarPracticante = async (req, res) => {
  try {
    res.json({ message: 'Practicante asignado exitosamente (CU-03)' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};
