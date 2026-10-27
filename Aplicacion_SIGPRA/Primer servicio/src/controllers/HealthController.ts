import { Request, Response } from "express";

export class HealthController {
  static show(_req: Request, res: Response) {
    res.json({
      ok: true,
      service: "SIGPRA API MVC",
      stack: ["Node.js", "Express", "TypeScript", "MVC"],
      services: {
        S1_Planeacion_y_convenios: ["/api/periods", "/api/agreements"],
        S2_Asignacion_y_solicitudes: ["/api/requests", "/api/assignments"],
        S3_Ejecucion_y_seguimiento: ["/api/activities", "/api/visits"],
        S4_Informes_y_auditoria: ["/api/reports/consolidated"],
        Transversales: ["/api/auth/login", "/api/state", "/api/health"]
      },
      apis: [
        "/api/auth/login",
        "/api/periods",
        "/api/agreements",
        "/api/requests",
        "/api/assignments",
        "/api/activities",
        "/api/visits",
        "/api/reports/consolidated"
      ]
    });
  }
}
