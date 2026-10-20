import { Request, Response } from "express";
import { ReportService } from "../services/ReportService";

export class ReportController {
  static consolidated(_req: Request, res: Response) {
    res.json({ rows: ReportService.consolidated() });
  }
}
