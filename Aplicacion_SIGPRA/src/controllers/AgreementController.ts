import { Request, Response } from "express";
import { AgreementService } from "../services/AgreementService";

export class AgreementController {
  static index(_req: Request, res: Response) {
    res.json(AgreementService.list());
  }

  static store(req: Request, res: Response) {
    res.status(201).json({ agreement: AgreementService.create(req.body) });
  }
}
