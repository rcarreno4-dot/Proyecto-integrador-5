const express = require('express');
const cors = require('cors');
const mongoose = require('mongoose');

const app = express();
app.use(cors());
app.use(express.json());

const mongoURI = process.env.MONGO_URI || 'mongodb://localhost:27017/sigpra_nosql_db';
mongoose.connect(mongoURI, { useNewUrlParser: true, useUnifiedTopology: true })
  .then(() => console.log('MongoDB Conectado para MÃ³dulo de Actividades'))
  .catch(err => console.error('Error MongoDB:', err));

const ActividadSchema = new mongoose.Schema({
  asignacion_id: Number,
  estudiante_id: Number,
  estudiante_nombre: String,
  fecha_actividad: Date,
  horas_ejecutadas: Number,
  descripcion_actividad: String,
  soportes_archivos: Array,
  estado_validacion: { type: String, default: 'Pendiente' },
  observaciones_docente: String,
  validado_por_docente_id: Number,
  fecha_validacion: Date
});

const Actividad = mongoose.model('RegistroActividad', ActividadSchema, 'registros_actividades');

// CU-04: Registrar actividad ejecutada y soportes (Estudiante)
app.post('/api/activities', async (req, res) => {
  try {
    const nuevaActividad = new Actividad({
      ...req.body,
      fecha_actividad: new Date(req.body.fecha_actividad || Date.now())
    });
    await nuevaActividad.save();
    res.json({ message: 'Actividad registrada correctamente', actividad: nuevaActividad });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Consultar actividades
app.get('/api/activities', async (req, res) => {
  try {
    const { estudiante_id, asignacion_id } = req.query;
    let filter = {};
    if (estudiante_id) filter.estudiante_id = Number(estudiante_id);
    if (asignacion_id) filter.asignacion_id = Number(asignacion_id);
    
    const actividades = await Actividad.find(filter).sort({ fecha_actividad: -1 });
    res.json(actividades);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// CU-05: Validar actividades reportadas (Docente / Coordinador)
app.put('/api/activities/:id/validate', async (req, res) => {
  try {
    const { id } = req.params;
    const { estado_validacion, observaciones_docente, validado_por_docente_id } = req.body;
    
    const actualizada = await Actividad.findByIdAndUpdate(id, {
      estado_validacion,
      observaciones_docente,
      validado_por_docente_id,
      fecha_validacion: new Date()
    }, { new: true });
    
    res.json({ message: 'ValidaciÃ³n de actividad actualizada', actividad: actualizada });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

const PORT = process.env.PORT || 3003;
app.listen(PORT, () => console.log(Activity Service ejecutÃ¡ndose en puerto ));
