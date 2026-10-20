const express = require('express');
const router = express.Router();
const ctrl = require('../controllers/director.controller');

router.post('/solicitud', ctrl.gestionarSolicitud);

module.exports = router;
