import { Router } from "express";
import { AuthController } from "../controllers/AuthController";
import { HealthController } from "../controllers/HealthController";
import { PeriodController } from "../controllers/PeriodController";
import { AgreementController } from "../controllers/AgreementController";
import { AssignmentController } from "../controllers/AssignmentController";
import { TrackingController } from "../controllers/TrackingController";
import { ReportController } from "../controllers/ReportController";
import { StateController } from "../controllers/StateController";

export const apiRouter = Router();

// ==========================================
// Servicios Transversales (Salud, Sesion, Estado)
// ==========================================
apiRouter.get("/health", HealthController.show);
apiRouter.get("/state", StateController.show);
apiRouter.put("/state", StateController.replace);
apiRouter.post("/auth/login", AuthController.login);

// ==========================================
// S1. Planeacion y convenios (RF-01, RF-02)
// ==========================================
apiRouter.get("/periods", PeriodController.index);
apiRouter.post("/periods", PeriodController.store);
apiRouter.get("/agreements", AgreementController.index);
apiRouter.post("/agreements", AgreementController.store);

// ==========================================
// S2. Asignacion de practicas (RF-03)
// ==========================================
apiRouter.get("/assignments", AssignmentController.index);
apiRouter.post("/assignments", AssignmentController.store);

// ==========================================
// S3. Ejecucion y seguimiento (RF-04, RF-05)
// ==========================================
apiRouter.get("/activities", TrackingController.listActivities);
apiRouter.post("/activities", TrackingController.createActivity);
apiRouter.put("/activities/:id", TrackingController.updateActivity);
apiRouter.get("/visits", TrackingController.listVisits);
apiRouter.post("/visits", TrackingController.createVisit);

// ==========================================
// S4. Informes y auditoria (CU-07)
// ==========================================
apiRouter.get("/reports/consolidated", ReportController.consolidated);
