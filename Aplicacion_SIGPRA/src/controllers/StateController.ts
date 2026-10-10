import { Request, Response } from "express";
import { StateService } from "../services/StateService";

export class StateController {
  static show(_req: Request, res: Response) {
    res.json({ data: StateService.show() });
  }

  static replace(req: Request, res: Response) {
    res.json({ data: StateService.replace(req.body) });
  }
}
