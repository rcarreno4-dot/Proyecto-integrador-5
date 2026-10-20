const express = require('express');
const router = express.Router();
const ctrl = require('../controllers/estudiante.controller');

router.post('/actividad', ctrl.registrarActividad);
router.get('/estado/:id', ctrl.consultarEstado);

module.exports = router;
