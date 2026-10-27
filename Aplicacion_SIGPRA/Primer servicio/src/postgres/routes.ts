import { Router, Request, Response, NextFunction } from 'express';
import { changeState, readState, replaceState, postgresReady } from './state';
import { SigpraData } from '../models/types';

export const postgresRouter = Router();
const wrap = (fn: (req: Request, res: Response) => Promise<void>) =>
  (req: Request, res: Response, next: NextFunction) => { Promise.resolve(fn(req, res)).catch(next); };
const newId = (records: { id: number }[]) => Math.max(0, ...records.map(r => Number(r.id))) + 1;

// ==========================================
// Servicios Transversales
// ==========================================
postgresRouter.get('/health', wrap(async (_req, res) => {
  res.json({ ok: await postgresReady(), service: 'SIGPRA', persistence: 'PostgreSQL JSONB MVP' });
}));
postgresRouter.get('/state', wrap(async (_req, res) => { res.json({ data: await readState() }); }));
postgresRouter.put('/state', wrap(async (req, res) => {
  res.json({ data: await replaceState(req.body) });
}));
postgresRouter.post('/auth/login', wrap(async (req, res) => {
  const d = await readState();
  const u = d.users.find(x => x.email === String(req.body?.email || '') && x.password === String(req.body?.password || ''));
  if (!u) { res.status(401).json({ message: 'Credenciales invalidas' }); return; }
  const { password: _password, ...user } = u;
  res.json({ user });
}));

// ==========================================
// S1. Planeacion y convenios (RF-01, RF-02)
// ==========================================
postgresRouter.get('/periods', wrap(async (_req, res) => { res.json({ periods: (await readState()).periods }); }));
postgresRouter.get('/agreements', wrap(async (_req, res) => {
  const d = await readState(); res.json({ institutions: d.institutions, agreements: d.agreements, places: d.places });
}));
postgresRouter.post('/periods', wrap(async (req, res) => {
  const period = await changeState((d: SigpraData) => {
    const input = req.body;
    if (!input || !d.programs.some(p => p.active && p.name === input.program && p.levels.includes(input.level)))
      throw new Error('Programa o nivel invalido');
    const p = { ...input, id: newId(d.periods) };
    d.periods.push(p); return p;
  }); res.status(201).json({ period });
}));
postgresRouter.post('/agreements', wrap(async (req, res) => {
  const agreement = await changeState(d => {
    const input = req.body;
    if (!input || !d.institutions.some(i => i.id === Number(input.institutionId)) || input.end < input.start)
      throw new Error('Convenio invalido');
    const item = { ...input, id: newId(d.agreements) };
    d.agreements.push(item); return item;
  }); res.status(201).json({ agreement });
}));

// ==========================================
// S2. Asignacion de practicas (RF-03)
// ==========================================
postgresRouter.get('/assignments', wrap(async (_req, res) => { res.json({ assignments: (await readState()).assignments }); }));
postgresRouter.post('/assignments', wrap(async (req, res) => {
  const assignment = await changeState(d => {
    const input = req.body;
    const place = d.places.find(p => p.id === Number(input?.placeId));
    if (!place || place.occupied >= place.offered) throw new Error('Plaza sin cupos');
    if (!d.periods.some(p => p.id === Number(input.periodId)) ||
        !d.users.some(u => u.id === Number(input.studentId) && u.role === 'ESTUDIANTE') ||
        !d.users.some(u => u.id === Number(input.teacherId) && u.role === 'DOCENTE_ASESOR'))
      throw new Error('Asignacion invalida');
    if (d.assignments.some(a => a.studentId === Number(input.studentId) && a.periodId === Number(input.periodId) && a.status === 'ACTIVA'))
      throw new Error('Estudiante ya asignado en el periodo');
    const a = { ...input, id: newId(d.assignments) };
    d.assignments.push(a); place.occupied++; return a;
  }); res.status(201).json({ assignment });
}));

// ==========================================
// S3. Ejecucion y seguimiento (RF-04, RF-05)
// ==========================================
postgresRouter.get('/activities', wrap(async (req, res) => {
  const d = await readState();
  const assignmentId = req.query.assignmentId ? Number(req.query.assignmentId) : undefined;
  const activities = assignmentId ? d.activities.filter(a => a.assignmentId === assignmentId) : d.activities;
  res.json({ activities });
}));
postgresRouter.post('/activities', wrap(async (req, res) => {
  const activity = await changeState(d => {
    const input = req.body;
    if (!input?.assignmentId || !input?.description || !input?.hours) throw new Error('Datos de actividad incompletos');
    const a = { ...input, id: newId(d.activities), status: 'PENDIENTE', observation: input.observation || '' };
    d.activities.push(a); return a;
  }); res.status(201).json({ activity });
}));
postgresRouter.put('/activities/:id', wrap(async (req, res) => {
  const id = Number(req.params.id);
  const updated = await changeState(d => {
    const item = d.activities.find(a => a.id === id);
    if (!item) throw new Error('Actividad no encontrada');
    if (req.body?.status) item.status = req.body.status;
    if (req.body?.observation !== undefined) item.observation = req.body.observation;
    return item;
  }); res.json({ activity: updated });
}));
postgresRouter.get('/visits', wrap(async (req, res) => {
  const d = await readState();
  const assignmentId = req.query.assignmentId ? Number(req.query.assignmentId) : undefined;
  const visits = assignmentId ? d.visits.filter(v => v.assignmentId === assignmentId) : d.visits;
  res.json({ visits });
}));
postgresRouter.post('/visits', wrap(async (req, res) => {
  const visit = await changeState(d => {
    const input = req.body;
    if (!input?.assignmentId || !input?.teacherId || !input?.date) throw new Error('Datos de visita incompletos');
    const v = { ...input, id: newId(d.visits), mode: input.mode || 'PRESENCIAL', attendance: Boolean(input.attendance), score: Number(input.score), status: input.status || 'REGISTRADA' };
    d.visits.push(v); return v;
  }); res.status(201).json({ visit });
}));

// ==========================================
// S4. Informes y auditoria (CU-07)
// ==========================================
postgresRouter.get('/reports/consolidated', wrap(async (_req, res) => {
  const data = await readState();
  const rows = data.assignments.map(a => {
    const place = data.places.find(p => p.id === a.placeId);
    const agreement = data.agreements.find(c => c.id === place?.agreementId);
    const activities = data.activities.filter(x => x.assignmentId === a.id);
    return { assignmentId: a.id,
      student: data.users.find(u => u.id === a.studentId)?.name,
      teacher: data.users.find(u => u.id === a.teacherId)?.name,
      institution: data.institutions.find(i => i.id === agreement?.institutionId)?.name,
      approvedHours: activities.filter(x => x.status === 'APROBADA').reduce((sum, x) => sum + Number(x.hours), 0) || a.approvedHours,
      activities: activities.length, visits: data.visits.filter(x => x.assignmentId === a.id).length, status: a.status };
  });
  res.json({ rows });
}));
