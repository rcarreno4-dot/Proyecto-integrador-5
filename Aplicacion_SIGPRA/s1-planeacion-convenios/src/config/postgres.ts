import { Pool } from 'pg';
import dotenv from 'dotenv';

dotenv.config();

export const pool = new Pool({
  connectionString: process.env.POSTGRES_URL || 'postgresql://postgres:postgres@localhost:5432/sigpra_db',
});

pool.on('connect', () => {
  console.log('PostgreSQL conectado exitosamente en S1');
});

pool.on('error', (err) => {
  console.error('Error inesperado en el cliente de PostgreSQL:', err);
});