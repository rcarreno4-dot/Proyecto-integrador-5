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

// AutenticaciÃ³n de Usuarios
app.post('/api/auth/login', async (req, res) => {
  const { email, password } = req.body;
  try {
    const result = await pool.query('SELECT id, documento, nombre, email, rol, estado FROM usuarios WHERE email = ', [email]);
    if (result.rows.length === 0) {
      return res.status(401).json({ success: false, message: 'Usuario no encontrado' });
    }
    const user = result.rows[0];
    res.json({ success: true, user, token: 'mock-jwt-token-' + user.id });
  } catch (err) {
    res.status(500).json({ success: false, error: err.message });
  }
});

// Listar Usuarios por Rol
app.get('/api/users', async (req, res) => {
  try {
    const { rol } = req.query;
    let query = 'SELECT id, documento, nombre, email, rol, estado FROM usuarios';
    const params = [];
    if (rol) {
      query += ' WHERE rol = ';
      params.push(rol);
    }
    const result = await pool.query(query, params);
    res.json(result.rows);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

const PORT = process.env.PORT || 3001;
app.listen(PORT, () => console.log(Auth Service ejecutÃ¡ndose en puerto ));
