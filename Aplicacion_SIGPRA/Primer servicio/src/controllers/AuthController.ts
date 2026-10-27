import { Request, Response } from "express";
import { AuthService } from "../services/AuthService";

export class AuthController {
  static login(req: Request, res: Response) {
    const user = AuthService.login(String(req.body.email || ""), String(req.body.password || ""));
    if (!user) return res.status(401).json({ message: "Credenciales invalidas" });
    return res.json({ user });
  }

  static register(req: Request, res: Response) {
    try {
      const { name, email, password, code, semester } = req.body;
      if (!name || !email || !password) {
        return res.status(400).json({ error: "Nombre, correo y contraseña son obligatorios." });
      }
      const user = AuthService.register({ name, email, password, code, semester });
      return res.status(201).json({ message: "Estudiante registrado exitosamente.", user });
    } catch (error: any) {
      return res.status(400).json({ error: error.message || "Error al registrar el estudiante." });
    }
  }
}
