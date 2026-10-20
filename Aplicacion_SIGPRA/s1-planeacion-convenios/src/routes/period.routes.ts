import { Router } from 'express';
import { PeriodController } from '../controllers/period.controller.js';

const router = Router();
const periodController = new PeriodController();

router.get('/', (req, res) => periodController.getPeriods(req, res));
router.post('/', (req, res) => periodController.createPeriod(req, res));

export default router;