const { Pool } = require('pg');
require('dotenv').config({ path: '../../../.env' });

const pool = new Pool({
  host: process.env.PG_HOST || 'localhost',
  port: process.env.PG_PORT || 5432,
  user: process.env.PG_USER || 'postgres',
  password: process.env.PG_PASSWORD || 'postgres',
  database: process.env.PG_DATABASE || 'sigpra_pg_db'
});

pool.on('connect', () => console.log('[PostgreSQL] Conectado exitosamente.'));
module.exports = { query: (text, params) => pool.query(text, params) };
