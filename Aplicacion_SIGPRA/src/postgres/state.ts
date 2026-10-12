import fs from 'node:fs';
import path from 'node:path';
import { Pool, PoolClient } from 'pg';
import { seedData } from '../models/seedData';
import { SigpraData } from '../models/types';

const pool = new Pool({ connectionString: process.env.DATABASE_URL, max: 5 });
const storage = path.join(process.cwd(), 'data', 'sigpra-store.json');
const clone = <T>(x: T): T => JSON.parse(JSON.stringify(x)) as T;

function valid(state: unknown): state is SigpraData {
  if (!state || typeof state !== 'object') return false;
  const d = state as Record<string, unknown>;
  return ['programs', 'users', 'periods', 'institutions', 'agreements', 'places', 'assignments',
    'activities', 'evidences', 'visits', 'audit'].every(k => Array.isArray(d[k]));
}

export async function setupPostgres(): Promise<void> {
  if (!process.env.DATABASE_URL) throw new Error('DATABASE_URL no configurada');
  await pool.query('CREATE SCHEMA IF NOT EXISTS sigpra');
  await pool.query(`CREATE TABLE IF NOT EXISTS sigpra.estado_mvp (
    id INTEGER PRIMARY KEY CHECK (id = 1),
    contenido JSONB NOT NULL,
    actualizado_en TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
  )`);
  const existing = await pool.query('SELECT id FROM sigpra.estado_mvp WHERE id=1');
  if (existing.rowCount) return;
  // Nunca se importan datos sin consentimiento explicito.
  let start: SigpraData = clone(seedData);
  if (fs.existsSync(storage)) {
    if (process.env.SIGPRA_PG_IMPORT_JSON !== '1') {
      throw new Error('Existe data/sigpra-store.json: configura SIGPRA_PG_IMPORT_JSON=1 para importarlo sin reemplazarlo.');
    }
    const saved: unknown = JSON.parse(fs.readFileSync(storage, 'utf8'));
    if (!valid(saved)) throw new Error('JSON local invalido; se cancela importacion para proteger los datos');
    start = saved;
  }
  await pool.query('INSERT INTO sigpra.estado_mvp(id, contenido) VALUES(1, $1::jsonb) ON CONFLICT(id) DO NOTHING',
    [JSON.stringify(start)]);
}

export async function readState(): Promise<SigpraData> {
  const r = await pool.query<{ contenido: SigpraData }>('SELECT contenido FROM sigpra.estado_mvp WHERE id = 1');
  if (!r.rows.length || !valid(r.rows[0].contenido)) throw new Error('Estado PostgreSQL no inicializado o invalido');
  return r.rows[0].contenido;
}

// Todas las mutaciones usan bloqueo de fila en una transaccion.
export async function changeState<T>(fn: (state: SigpraData) => T): Promise<T> {
  const client: PoolClient = await pool.connect();
  try {
    await client.query('BEGIN');
    const result = await client.query<{ contenido: SigpraData }>('SELECT contenido FROM sigpra.estado_mvp WHERE id=1 FOR UPDATE');
    const state = result.rows[0]?.contenido;
    if (!valid(state)) throw new Error('Estado no valido en PostgreSQL');
    const outcome = fn(state);
    if (!valid(state)) throw new Error('La operacion genero un estado invalido');
    await client.query('UPDATE sigpra.estado_mvp SET contenido=$1::jsonb, actualizado_en=NOW() WHERE id=1', [JSON.stringify(state)]);
    await client.query('COMMIT');
    return outcome;
  } catch (error) {
    await client.query('ROLLBACK');
    throw error;
  } finally { client.release(); }
}

export async function replaceState(next: unknown): Promise<SigpraData> {
  if (!valid(next)) throw new Error('Estructura de estado invalida');
  return changeState(state => {
    // Conserva la referencia y reemplaza todo el estado de modo atomico.
    for (const key of Object.keys(state)) delete (state as unknown as Record<string, unknown>)[key];
    Object.assign(state, clone(next));
    return clone(state);
  });
}

export async function postgresReady(): Promise<boolean> {
  const r = await pool.query('SELECT 1');
  return r.rowCount === 1;
}
