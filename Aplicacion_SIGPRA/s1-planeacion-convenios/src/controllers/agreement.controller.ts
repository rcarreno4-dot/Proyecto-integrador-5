import { Request, Response } from 'express';
import { pool } from '../config/postgres.js';

export class AgreementController {

  // GET /api/agreements - Consultar convenios vigentes, instituciones y plazas (CU-02)[cite: 2]
  public async getAgreements(req: Request, res: Response): Promise<void> {
    try {
      const query = `
        SELECT 
          i.id_institucion, i.nombre AS institucion_nombre, i.nit,
          c.id_convenio, c.numero_convenio, c.fecha_inicio, c.fecha_fin, c.estado AS estado_convenio,
          p.id_plaza, p.nivel_practica, p.jornada, p.cupos_ofrecidos, p.cupos_ocupados, p.estado AS estado_plaza
        FROM institucion i
        INNER JOIN convenio c ON i.id_institucion = c.id_institucion
        LEFT JOIN plaza_practica p ON c.id_convenio = p.id_convenio
        ORDER BY c.fecha_fin DESC;
      `;
      const result = await pool.query(query);
      res.status(200).json({ success: true, data: result.rows });
    } catch (error: any) {
      res.status(500).json({ success: false, message: 'Error al consultar convenios', error: error.message });
    }
  }

  // POST /api/agreements - Registrar institucion, convenio y plaza (CU-02)[cite: 2]
  public async createAgreement(req: Request, res: Response): Promise<void> {
    const client = await pool.connect();
    try {
      const {
        id_institucion, // Si ya existe la institución
        nombre_institucion, nit, direccion, telefono, correo_contacto, responsable_contacto, // Si es nueva
        numero_convenio, fecha_inicio, fecha_fin, documento_url,
        nivel_practica, jornada, cupos_ofrecidos, docente_titular
      } = req.body;

      // Validacion de fechas del convenio[cite: 2]
      if (new Date(fecha_inicio) >= new Date(fecha_fin)) {
        res.status(400).json({ 
          success: false, 
          message: 'Error de validación: La fecha de inicio debe ser anterior a la fecha de vencimiento.' 
        });
        return;
      }

      await client.query('BEGIN');

      let targetInstitucionId = id_institucion;

      // 1. Crear institución si no se envió id_institucion[cite: 2]
      if (!targetInstitucionId) {
        const insertInstQuery = `
          INSERT INTO institucion (nombre, nit, direccion, telefono, correo_contacto, responsable_contacto, activa)
          VALUES ($1, $2, $3, $4, $5, $6, true)
          RETURNING id_institucion;
        `;
        const instResult = await client.query(insertInstQuery, [
          nombre_institucion, nit, direccion, telefono, correo_contacto, responsable_contacto
        ]);
        targetInstitucionId = instResult.rows[0].id_institucion;
      }

      // 2. Crear Convenio[cite: 2]
      const insertConvQuery = `
        INSERT INTO convenio (id_institucion, numero_convenio, fecha_inicio, fecha_fin, documento_url, estado)
        VALUES ($1, $2, $3, $4, $5, 'VIGENTE')
        RETURNING id_convenio;
      `;
      const convResult = await client.query(insertConvQuery, [
        targetInstitucionId, numero_convenio, fecha_inicio, fecha_fin, documento_url || null
      ]);
      const idConvenio = convResult.rows[0].id_convenio;

      // 3. Crear Plaza de Práctica asociada[cite: 2]
      const insertPlazaQuery = `
        INSERT INTO plaza_practica (id_convenio, nivel_practica, jornada, cupos_ofrecidos, cupos_ocupados, docente_titular, estado)
        VALUES ($1, $2, $3, $4, 0, $5, 'DISPONIBLE');
      `;
      await client.query(insertPlazaQuery, [
        idConvenio, nivel_practica, jornada, cupos_ofrecidos, docente_titular || null
      ]);

      await client.query('COMMIT');
      res.status(201).json({ 
        success: true, 
        message: 'Convenio y plaza registrados exitosamente.', 
        id_convenio: idConvenio 
      });

    } catch (error: any) {
      await client.query('ROLLBACK');
      res.status(500).json({ success: false, message: 'Error al registrar convenio', error: error.message });
    } finally {
      client.release();
    }
  }
}