import { Request, Response } from "express";
import { AssignmentService } from "../services/AssignmentService";

export class AssignmentController {
  static index(_req: Request, res: Response) {
    res.json({ assignments: AssignmentService.list() });
  }

  static store(req: Request, res: Response) {
    res.status(201).json({ assignment: AssignmentService.create(req.body) });
  }
}
