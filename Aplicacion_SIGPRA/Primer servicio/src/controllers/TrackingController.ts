import { Request, Response } from "express";
import { TrackingService } from "../services/TrackingService";

/**
 * Controlador para Servicio S3: Ejecucion y seguimiento
 */
export class TrackingController {
  static listActivities(req: Request, res: Response) {
    const assignmentId = req.query.assignmentId ? Number(req.query.assignmentId) : undefined;
    const activities = TrackingService.listActivities(assignmentId);
    return res.json({ activities });
  }

  static createActivity(req: Request, res: Response) {
    try {
      const activity = TrackingService.createActivity(req.body);
      return res.status(201).json({ activity });
    } catch (error) {
      return res.status(400).json({ message: (error as Error).message });
    }
  }

  static updateActivity(req: Request, res: Response) {
    try {
      const id = Number(req.params.id);
      const { status, observation } = req.body;
      const activity = TrackingService.updateActivityStatus(id, status, observation);
      return res.json({ activity });
    } catch (error) {
      return res.status(400).json({ message: (error as Error).message });
    }
  }

  static listVisits(req: Request, res: Response) {
    const assignmentId = req.query.assignmentId ? Number(req.query.assignmentId) : undefined;
    const visits = TrackingService.listVisits(assignmentId);
    return res.json({ visits });
  }

  static createVisit(req: Request, res: Response) {
    try {
      const visit = TrackingService.createVisit(req.body);
      return res.status(201).json({ visit });
    } catch (error) {
      return res.status(400).json({ message: (error as Error).message });
    }
  }
}
