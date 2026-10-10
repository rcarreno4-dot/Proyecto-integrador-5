import { Request, Response } from "express";
import { AuthService } from "../services/AuthService";

export class AuthController {
  static login(req: Request, res: Response) {
    const user = AuthService.login(String(req.body.email || ""), String(req.body.password || ""));
    if (!user) return res.status(401).json({ message: "Credenciales invalidas" });
    return res.json({ user });
  }
}
