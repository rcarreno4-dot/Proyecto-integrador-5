const express = require('express');
const cors = require('cors');
const proxy = require('express-http-proxy');
require('dotenv').config({ path: '../.env' });

const app = express();
app.use(cors());
app.use(express.json());

app.use(express.static('../frontend'));

// Redirección de peticiones por Actor / Rol
app.use('/api/coordinador', proxy(process.env.AUTH_SERVICE_URL || 'http://localhost:4000', {
  proxyReqPathResolver: req => '/coordinador' + req.url
}));

app.use('/api/director', proxy(process.env.AUTH_SERVICE_URL || 'http://localhost:4000', {
  proxyReqPathResolver: req => '/director' + req.url
}));

app.use('/api/estudiante', proxy(process.env.DATA_SERVICE_URL || 'http://localhost:5000', {
  proxyReqPathResolver: req => '/estudiante' + req.url
}));

app.use('/api/docente', proxy(process.env.DATA_SERVICE_URL || 'http://localhost:5000', {
  proxyReqPathResolver: req => '/docente' + req.url
}));

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`[API Gateway] Enrutamiento por actores activo en http://localhost:${PORT}`);
});
