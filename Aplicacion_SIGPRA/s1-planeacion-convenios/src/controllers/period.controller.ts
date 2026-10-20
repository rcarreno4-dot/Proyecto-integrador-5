import { Request, Response } from 'express';
import { pool } from '../config/postgres.js';

export class PeriodController {
  
  // GET /api/periods - Consultar todos los periodos académicos
  public async getPeriods(req: Request, res: Response): Promise<void> {
    try {
      const query = `
        SELECT p.id_periodo, p.programa, p.nivel_practica, p.anio, p.semestre, 
               p.fecha_inicio, p.fecha_fin, p.horas_minimas, p.estado
        FROM periodo_practica p
        ORDER BY p.anio DESC, p.semestre DESC;
      `;
      const result = await pool.query(query);
      res.status(200).json({ success: true, data: result.rows });
    } catch (error: any) {
      res.status(500).json({ success: false, message: 'Error al consultar periodos', error: error.message });
    }
  }

  // POST /api/periods - Publicar periodo y rúbrica (CU-01)[cite: 2]
  public async createPeriod(req: Request, res: Response): Promise<void> {
    const client = await pool.connect();
    try {
      const {
        id_coordinador, programa, nivel_practica, anio, semestre,
        fecha_inicio, fecha_fin, fecha_limite_reportes, horas_minimas,
        criterios // Arreglo con { nombre, descripcion, peso }
      } = req.body;

      // Validación de regla de negocio RN-03 / CU-01: pesos deben sumar 100%[cite: 2]
      if (criterios && Array.isArray(criterios)) {
        const totalPeso = criterios.reduce((sum: number, c: any) => sum + Number(c.peso), 0);
        if (totalPeso !== 100) {
          res.status(400).json({ 
            success: false, 
            message: 'Error de validación: Los pesos de los criterios de la rúbrica deben sumar exactamente 100%.' 
          });
          return;
        }
      }

      await client.query('BEGIN');

      // 1. Insertar Periodo
      const insertPeriodQuery = `
        INSERT INTO periodo_practica 
        (id_coordinador, programa, nivel_practica, anio, semestre, fecha_inicio, fecha_fin, fecha_limite_reportes, horas_minimas, estado)
        VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, 'PUBLICADO')
        RETURNING id_periodo;
      `;
      const periodResult = await client.query(insertPeriodQuery, [
        id_coordinador, programa, nivel_practica, anio, semestre,
        fecha_inicio, fecha_fin, fecha_limite_reportes, horas_minimas
      ]);
      const idPeriodo = periodResult.rows[0].id_periodo;

      // 2. Insertar Rúbrica asociada
      const insertRubricQuery = `
        INSERT INTO rubrica (id_periodo, nombre, activa)
        VALUES ($1, $2, true)
        RETURNING id_rubrica;
      `;
      const rubricResult = await client.query(insertRubricQuery, [idPeriodo, `Rúbrica Evaluativa ${anio}-${semestre}`]);
      const idRubrica = rubricResult.rows[0].id_rubrica;

      // 3. Insertar Criterios de la Rúbrica
      for (const criterio of criterios) {
        const insertCriterionQuery = `
          INSERT INTO criterio_rubrica (id_rubrica, nombre, descripcion, peso)
          VALUES ($1, $2, $3, $4);
        `;
        await client.query(insertCriterionQuery, [idRubrica, criterio.nombre, criterio.descripcion, criterio.peso]);
      }

      await client.query('COMMIT');
      res.status(201).json({ success: true, message: 'Periodo de práctica publicado con éxito.', id_periodo: idPeriodo });

    } catch (error: any) {
      await client.query('ROLLBACK');
      res.status(500).json({ success: false, message: 'Error al registrar el periodo', error: error.message });
    } finally {
      client.release();
    }
  }
}