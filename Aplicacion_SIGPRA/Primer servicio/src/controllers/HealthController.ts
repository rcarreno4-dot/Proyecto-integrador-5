import { Request, Response } from "express";

export class HealthController {
  static show(_req: Request, res: Response) {
    res.json({
      ok: true,
      service: "SIGPRA API MVC",
      stack: ["Node.js", "Express", "TypeScript", "MVC"],
      apis: ["/api/auth/login", "/api/periods", "/api/agreements", "/api/assignments", "/api/reports/consolidated"]
    });
  }
}
