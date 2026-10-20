import { Request, Response } from "express";
import { PeriodService } from "../services/PeriodService";

export class PeriodController {
  static index(_req: Request, res: Response) {
    res.json({ periods: PeriodService.list() });
  }

  static store(req: Request, res: Response) {
    res.status(201).json({ period: PeriodService.create(req.body) });
  }
}
