import type { Request, Response } from 'express';
import { pool } from '../config/postgres.js';

export class CertificationController {
  // Cierre de Práctica y Emisión de Certificado / Paz y Salvo
  public async generateCertificate(req: Request, res: Response): Promise<void> {
    try {
      const { id_asignacion, codigo_estudiante, horas_totales_cumplidas } = req.body;

      if (horas_totales_cumplidas < 140) {
        res.status(400).json({
          success: false,
          message: 'No se puede emitir el certificado. El estudiante no ha completado el número mínimo de horas requeridas.'
        });
        return;
      }

      const query = `
        INSERT INTO certificacion_practica (id_asignacion, codigo_estudiante, fecha_emision, estado, codigo_verificacion)
        VALUES ($1, $2, CURRENT_DATE, 'APROBADO', MD5(RANDOM()::text))
        RETURNING id_certificacion, codigo_verificacion;
      `;
      const result = await pool.query(query, [id_asignacion, codigo_estudiante]);

      res.status(201).json({
        success: true,
        message: 'Certificado de práctica y Paz y Salvo generado exitosamente.',
        data: result.rows[0]
      });
    } catch (error: any) {
      res.status(500).json({ success: false, message: 'Error al generar certificación', error: error.message });
    }
  }

  // Reporte General e Indicadores del Sistema
  public async getSummaryReport(req: Request, res: Response): Promise<void> {
    try {
      const queryStats = `
        SELECT 
          (SELECT COUNT(*) FROM asignacion) as total_asignaciones,
          (SELECT COUNT(*) FROM certificacion_practica WHERE estado = 'APROBADO') as total_certificados,
          (SELECT COUNT(*) FROM solicitud_practica WHERE estado = 'PENDIENTE') as solicitudes_pendientes;
      `;
      const result = await pool.query(queryStats);

      res.status(200).json({
        success: true,
        timestamp: new Date().toISOString(),
        metrics: result.rows[0]
      });
    } catch (error: any) {
      res.status(500).json({ success: false, message: 'Error al consultar reporte consolidado', error: error.message });
    }
  }
}
