const express = require('express');
const router = express.Router();
const ctrl = require('../controllers/coordinador.controller');

router.post('/periodo', ctrl.programarPeriodo);
router.post('/convenios', ctrl.gestionarConvenios);
router.post('/asignar', ctrl.asignarPracticante);

module.exports = router;
