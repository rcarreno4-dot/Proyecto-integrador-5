const express = require('express');
const cors = require('cors');
const { Pool } = require('pg');

const app = express();
app.use(cors());
app.use(express.json());

const pool = new Pool({
  host: process.env.DB_HOST || 'localhost',
  database: process.env.DB_NAME || 'sigpra_relational_db',
  user: process.env.DB_USER || 'postgres',
  password: process.env.DB_PASS || 'postgrespassword',
  port: 5432
});

// CU-01: Programar periodo de prÃ¡ctica
app.post('/api/academic/periodos', async (req, res) => {
  const { codigo_periodo, fecha_inicio, fecha_fin, horas_requeridas } = req.body;
  try {
    const result = await pool.query(
      'INSERT INTO periodos_practica (codigo_periodo, fecha_inicio, fecha_fin, horas_requeridas) VALUES (, , , ) RETURNING *',
      [codigo_periodo, fecha_inicio, fecha_fin, horas_requeridas]
    );
    res.json({ message: 'Periodo programado con Ã©xito', periodo: result.rows[0] });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.get('/api/academic/periodos', async (req, res) => {
  try {
    const result = await pool.query('SELECT * FROM periodos_practica ORDER BY id DESC');
    res.json(result.rows);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// CU-02: Gestionar convenios y plazas
app.get('/api/academic/convenios', async (req, res) => {
  try {
    const result = await pool.query('SELECT * FROM instituciones_convenios');
    res.json(result.rows);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/academic/plazas', async (req, res) => {
  const { institucion_id, titulo_plaza, descripcion, cupos_disponibles, modalidad } = req.body;
  try {
    const result = await pool.query(
      'INSERT INTO plazas_practica (institucion_id, titulo_plaza, descripcion, cupos_disponibles, modalidad) VALUES (, , , , ) RETURNING *',
      [institucion_id, titulo_plaza, descripcion, cupos_disponibles, modalidad]
    );
    res.json({ message: 'Plaza creada con Ã©xito', plaza: result.rows[0] });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.get('/api/academic/plazas', async (req, res) => {
  try {
    const result = await pool.query(
      SELECT p.*, i.nombre_empresa 
      FROM plazas_practica p
      JOIN instituciones_convenios i ON p.institucion_id = i.id
    );
    res.json(result.rows);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// CU-08: Solicitar PrÃ¡ctica AcadÃ©mica (Estudiante)
app.post('/api/academic/solicitudes', async (req, res) => {
  const { estudiante_id, plaza_id, periodo_id } = req.body;
  try {
    const result = await pool.query(
      'INSERT INTO asignaciones_practica (estudiante_id, plaza_id, periodo_id, estado_solicitud) VALUES (, , , ) RETURNING *',
      [estudiante_id, plaza_id, periodo_id, 'Pendiente']
    );
    res.json({ message: 'Solicitud de prÃ¡ctica realizada', solicitud: result.rows[0] });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// CU-03: Asignar practicante a plaza y docente asesor (Coordinador)
app.put('/api/academic/asignaciones/:id', async (req, res) => {
  const { id } = req.params;
  const { docente_id, estado_solicitud } = req.body;
  try {
    const result = await pool.query(
      'UPDATE asignaciones_practica SET docente_id = , estado_solicitud =  WHERE id =  RETURNING *',
      [docente_id, estado_solicitud, id]
    );
    res.json({ message: 'AsignaciÃ³n actualizada', asignacion: result.rows[0] });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.get('/api/academic/asignaciones', async (req, res) => {
  try {
    const result = await pool.query(
      SELECT a.*, 
             u_est.nombre as estudiante_nombre, 
             u_doc.nombre as docente_nombre,
             p.titulo_plaza,
             i.nombre_empresa
      FROM asignaciones_practica a
      JOIN usuarios u_est ON a.estudiante_id = u_est.id
      LEFT JOIN usuarios u_doc ON a.docente_id = u_doc.id
      JOIN plazas_practica p ON a.plaza_id = p.id
      JOIN instituciones_convenios i ON p.institucion_id = i.id
    );
    res.json(result.rows);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

const PORT = process.env.PORT || 3002;
app.listen(PORT, () => console.log(Academic Service ejecutÃ¡ndose en puerto ));
