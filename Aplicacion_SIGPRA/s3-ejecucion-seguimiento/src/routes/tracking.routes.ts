import { Router } from 'express';
import { TrackingController } from '../controllers/tracking.controller.js';

const router = Router();
const controller = new TrackingController();

router.post('/logbook', (req, res) => controller.addLogbook(req, res));
router.post('/tutoring', (req, res) => controller.addTutoring(req, res));
router.post('/evaluation', (req, res) => controller.createEvaluation(req, res));

export default router;
