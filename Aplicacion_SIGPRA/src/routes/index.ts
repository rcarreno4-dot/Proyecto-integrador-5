import { Router } from "express";
import { AuthController } from "../controllers/AuthController";
import { HealthController } from "../controllers/HealthController";
import { PeriodController } from "../controllers/PeriodController";
import { AgreementController } from "../controllers/AgreementController";
import { AssignmentController } from "../controllers/AssignmentController";
import { ReportController } from "../controllers/ReportController";
import { StateController } from "../controllers/StateController";

export const apiRouter = Router();

apiRouter.get("/health", HealthController.show);
apiRouter.get("/state", StateController.show);
apiRouter.put("/state", StateController.replace);

apiRouter.post("/auth/login", AuthController.login);

apiRouter.get("/periods", PeriodController.index);
apiRouter.post("/periods", PeriodController.store);

apiRouter.get("/agreements", AgreementController.index);
apiRouter.post("/agreements", AgreementController.store);

apiRouter.get("/assignments", AssignmentController.index);
apiRouter.post("/assignments", AssignmentController.store);

apiRouter.get("/reports/consolidated", ReportController.consolidated);
