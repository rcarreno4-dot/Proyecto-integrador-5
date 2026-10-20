const express = require('express');
const cors = require('cors');
const connectDB = require('./config/db');
require('dotenv').config({ path: '../../../.env' });

const estudianteRoutes = require('./routes/estudiante.routes');
const docenteRoutes = require('./routes/docente.routes');

const app = express();
app.use(cors());
app.use(express.json());

connectDB();

app.use('/estudiante', estudianteRoutes);
app.use('/docente', docenteRoutes);

const PORT = 5000;
app.listen(PORT, () => console.log(`[Data/Actividades Service - MongoDB] Escuchando en puerto ${PORT}`));
