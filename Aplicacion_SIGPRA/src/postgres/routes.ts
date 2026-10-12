import { Router, Request, Response, NextFunction } from 'express';
import { changeState, readState, replaceState, postgresReady } from './state';
import { SigpraData } from '../models/types';

export const postgresRouter = Router();
const wrap = (fn: (req: Request, res: Response) => Promise<void>) =>
  (req: Request, res: Response, next: NextFunction) => { Promise.resolve(fn(req, res)).catch(next); };
const newId = (records: { id: number }[]) => Math.max(0, ...records.map(r => Number(r.id))) + 1;

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
postgresRouter.get('/periods', wrap(async (_req, res) => { res.json({ periods: (await readState()).periods }); }));
postgresRouter.get('/agreements', wrap(async (_req, res) => {
  const d = await readState(); res.json({ institutions: d.institutions, agreements: d.agreements, places: d.places });
}));
postgresRouter.get('/assignments', wrap(async (_req, res) => { res.json({ assignments: (await readState()).assignments }); }));
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
