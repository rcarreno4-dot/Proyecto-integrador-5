import express, { Application, Request, Response } from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import path from 'path';
import { connectMongo } from './config/mongodb.js';
import periodRoutes from './routes/period.routes.js';
import agreementRoutes from './routes/agreement.routes.js';

dotenv.config();

const app: Application = express();
const PORT = process.env.PORT || 3001;

// Middlewares
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Servir estáticos
app.use(express.static(path.join(process.cwd(), 'public')));

// Inicializar conexión a MongoDB para logs de auditoría
connectMongo();

// Rutas de Microservicio S1[cite: 2, 3]
app.use('/api/periods', periodRoutes);      // CU-01: Programar periodo y rúbricas[cite: 2]
app.use('/api/agreements', agreementRoutes); // CU-02: Gestionar convenios y plazas[cite: 2]

// Ruta de Salud
app.get('/health', (req: Request, res: Response) => {
  res.status(200).json({
    service: 'S1. Planeación y convenios',
    status: 'UP',
    timestamp: new Date().toISOString()
  });
});

app.listen(PORT, () => {
  console.log(`=================================================`);
  console.log(` Microservicio S1 activo en http://localhost:${PORT}`);
  console.log(` Endpoint Periodos:   http://localhost:${PORT}/api/periods`);
  console.log(` Endpoint Convenios:  http://localhost:${PORT}/api/agreements`);
  console.log(`=================================================`);
});