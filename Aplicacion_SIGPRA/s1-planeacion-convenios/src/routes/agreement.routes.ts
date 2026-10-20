import { Router } from 'express';
import { AgreementController } from '../controllers/agreement.controller.js';

const router = Router();
const agreementController = new AgreementController();

router.get('/', (req, res) => agreementController.getAgreements(req, res));
router.post('/', (req, res) => agreementController.createAgreement(req, res));

export default router;