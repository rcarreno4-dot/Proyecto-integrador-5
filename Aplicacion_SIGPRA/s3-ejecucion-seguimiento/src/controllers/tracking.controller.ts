import type { Request, Response } from 'express';
import { pool } from '../config/postgres.js';

export class TrackingController {
  // CU-04: Registrar Bitácora de Actividades / Horas
  public async addLogbook(req: Request, res: Response): Promise<void> {
    try {
      const { id_asignacion, horas, descripcion, fecha_actividad } = req.body;
      const query = `
        INSERT INTO bitacora (id_asignacion, horas, descripcion, fecha_actividad, estado)
        VALUES ($1, $2, $3, $4, 'REGISTRADO')
        RETURNING id_bitacora;
      `;
      const result = await pool.query(query, [id_asignacion, horas, descripcion, fecha_actividad]);
      res.status(201).json({ success: true, message: 'Bitácora registrada correctamente.', id_bitacora: result.rows[0].id_bitacora });
    } catch (error: any) {
      res.status(500).json({ success: false, message: 'Error al registrar bitácora', error: error.message });
    }
  }

  // CU-05: Registrar Tutoría o Visita de Seguimiento
  public async addTutoring(req: Request, res: Response): Promise<void> {
    try {
      const { id_asignacion, id_docente, observaciones, compromisos } = req.body;
      const query = `
        INSERT INTO seguimiento_tutoria (id_asignacion, id_docente, fecha, observaciones, compromisos)
        VALUES ($1, $2, CURRENT_DATE, $3, $4)
        RETURNING id_seguimiento;
      `;
      const result = await pool.query(query, [id_asignacion, id_docente, observaciones, compromisos]);
      res.status(201).json({ success: true, message: 'Visita de seguimiento registrada.', id_seguimiento: result.rows[0].id_seguimiento });
    } catch (error: any) {
      res.status(500).json({ success: false, message: 'Error al registrar seguimiento', error: error.message });
    }
  }

  // CU-06: Evaluacion Final de la Práctica
  public async createEvaluation(req: Request, res: Response): Promise<void> {
    try {
      const { id_asignacion, calificacion_final, observaciones } = req.body;
      const query = `
        INSERT INTO evaluacion_practica (id_asignacion, calificacion_final, observaciones, fecha_evaluacion)
        VALUES ($1, $2, $3, CURRENT_DATE)
        RETURNING id_evaluacion;
      `;
      const result = await pool.query(query, [id_asignacion, calificacion_final, observaciones]);
      res.status(201).json({ success: true, message: 'Evaluación final guardada.', id_evaluacion: result.rows[0].id_evaluacion });
    } catch (error: any) {
      res.status(500).json({ success: false, message: 'Error al evaluar práctica', error: error.message });
    }
  }
}
