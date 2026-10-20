import { Router } from 'express';
import { CertificationController } from '../controllers/certification.controller.js';

const router = Router();
const controller = new CertificationController();

router.post('/generate', (req, res) => controller.generateCertificate(req, res));
router.get('/report-summary', (req, res) => controller.getSummaryReport(req, res));

export default router;
