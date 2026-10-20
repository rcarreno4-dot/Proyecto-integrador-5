const express = require('express');
const router = express.Router();
const ctrl = require('../controllers/docente.controller');

router.put('/validar/:actividadId', ctrl.validarActividad);
router.post('/visita', ctrl.registrarVisita);

module.exports = router;
