import { Router } from 'express';
import { AssignmentController } from '../controllers/assignment.controller.js';

const router = Router();
const controller = new AssignmentController();

router.post('/request', (req, res) => controller.createRequest(req, res));
router.post('/assign', (req, res) => controller.createAssignment(req, res));

export default router;
