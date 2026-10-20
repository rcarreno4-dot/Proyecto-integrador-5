const express = require('express');
const cors = require('cors');
require('dotenv').config({ path: '../../../.env' });

const coordinadorRoutes = require('./routes/coordinador.routes');
const directorRoutes = require('./routes/director.routes');

const app = express();
app.use(cors());
app.use(express.json());

app.use('/coordinador', coordinadorRoutes);
app.use('/director', directorRoutes);

const PORT = 4000;
app.listen(PORT, () => console.log(`[Auth/Practicas Service - PostgreSQL] Escuchando en puerto ${PORT}`));
