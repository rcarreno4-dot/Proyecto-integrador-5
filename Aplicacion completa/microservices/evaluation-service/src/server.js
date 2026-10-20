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

// CU-06: Realizar visita de acompaÃ±amiento y evaluar (Docente Asesor)
app.post('/api/evaluations/visitas', async (req, res) => {
  const { asignacion_id, docente_id, numero_visita, fecha_visita, observaciones_pedagogicas, calificacion_final } = req.body;
  try {
    const result = await pool.query(
      INSERT INTO visitas_evaluaciones (asignacion_id, docente_id, numero_visita, fecha_visita, observaciones_pedagogicas, calificacion_final)
       VALUES (, , , , , ) RETURNING *,
      [asignacion_id, docente_id, numero_visita, fecha_visita, observaciones_pedagogicas, calificacion_final]
    );
    res.json({ message: 'Visita de acompaÃ±amiento registrada y evaluada', visita: result.rows[0] });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.get('/api/evaluations/visitas', async (req, res) => {
  try {
    const result = await pool.query(
      SELECT v.*, u_doc.nombre as docente_nombre, u_est.nombre as estudiante_nombre
      FROM visitas_evaluaciones v
      JOIN asignaciones_practica a ON v.asignacion_id = a.id
      JOIN usuarios u_est ON a.estudiante_id = u_est.id
      JOIN usuarios u_doc ON v.docente_id = u_doc.id
    );
    res.json(result.rows);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// CU-07: Consultar estado consolidado de prÃ¡cticas (Director / Coordinador)
app.get('/api/evaluations/consolidado', async (req, res) => {
  try {
    const result = await pool.query(
      SELECT 
        a.id as asignacion_id,
        u_est.nombre as estudiante,
        u_est.documento as documento,
        p.titulo_plaza as plaza,
        i.nombre_empresa as empresa,
        u_doc.nombre as docente_asesor,
        a.estado_solicitud as estado_practica,
        COALESCE(AVG(v.calificacion_final), 0) as promedio_nota_visitas
      FROM asignaciones_practica a
      JOIN usuarios u_est ON a.estudiante_id = u_est.id
      LEFT JOIN usuarios u_doc ON a.docente_id = u_doc.id
      JOIN plazas_practica p ON a.plaza_id = p.id
      JOIN instituciones_convenios i ON p.institucion_id = i.id
      LEFT JOIN visitas_evaluaciones v ON v.asignacion_id = a.id
      GROUP BY a.id, u_est.nombre, u_est.documento, p.titulo_plaza, i.nombre_empresa, u_doc.nombre, a.estado_solicitud
    );
    res.json({
      total_practicantes: result.rows.length,
      resumen: result.rows
    });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

const PORT = process.env.PORT || 3004;
app.listen(PORT, () => console.log(Evaluation Service ejecutÃ¡ndose en puerto ));
