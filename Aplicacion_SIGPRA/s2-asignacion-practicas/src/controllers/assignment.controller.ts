import type { Request, Response } from 'express';
import { pool } from '../config/postgres.js';
import { S1ClientService } from '../services/s1-client.service.js';

export class AssignmentController {
  private s1Client = new S1ClientService();

  public async createRequest(req: Request, res: Response): Promise<void> {
    try {
      const { id_estudiante, id_periodo, nivel_practica, observaciones } = req.body;
      const query = `
        INSERT INTO solicitud_practica (id_estudiante, id_periodo, nivel_practica, observaciones, estado, fecha_solicitud)
        VALUES ($1, $2, $3, $4, 'PENDIENTE', CURRENT_DATE)
        RETURNING id_solicitud;
      `;
      const result = await pool.query(query, [id_estudiante, id_periodo, nivel_practica, observaciones]);
      res.status(201).json({ success: true, id_solicitud: result.rows[0].id_solicitud });
    } catch (error: any) {
      res.status(500).json({ success: false, error: error.message });
    }
  }

  public async createAssignment(req: Request, res: Response): Promise<void> {
    const client = await pool.connect();
    try {
      const { id_estudiante, id_plaza, id_docente_asesor, id_periodo } = req.body;

      const isAvailable = await this.s1Client.verifyPlazaAvailability(Number(id_plaza));
      if (!isAvailable) {
        res.status(400).json({ success: false, message: 'Plaza no disponible o convenio no vigente en S1.' });
        return;
      }

      await client.query('BEGIN');
      const insertQuery = `
        INSERT INTO asignacion (id_estudiante, id_plaza, id_docente_asesor, id_periodo, fecha_asignacion, estado, horas_aprobadas)
        VALUES ($1, $2, $3, $4, CURRENT_DATE, 'ACTIVA', 0) RETURNING id_asignacion;
      `;
      const result = await client.query(insertQuery, [id_estudiante, id_plaza, id_docente_asesor, id_periodo]);

      await client.query('UPDATE plaza_practica SET cupos_ocupados = cupos_ocupados + 1 WHERE id_plaza = $1;', [id_plaza]);
      await client.query('COMMIT');

      res.status(201).json({ success: true, id_asignacion: result.rows[0].id_asignacion });
    } catch (error: any) {
      await client.query('ROLLBACK');
      res.status(500).json({ success: false, error: error.message });
    } finally {
      client.release();
    }
  }
}
