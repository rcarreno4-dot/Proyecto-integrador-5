// CU-04: Registrar actividad ejecutada y soportes
exports.registrarActividad = async (req, res) => {
  try {
    const { estudianteId, descripcion, horas, soporteUrl } = req.body;
    // Conectar con tu modelo de Mongoose real:
    res.json({ message: 'Actividad y soporte guardados en MongoDB (CU-04)' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// CU-07: Consultar estado consolidado propio
exports.consultarEstado = async (req, res) => {
  try {
    res.json({ message: 'Estado consolidado de prácticas del estudiante (CU-07)' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};
