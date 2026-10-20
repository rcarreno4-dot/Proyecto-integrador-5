import express from 'express';
import type { Application, Request, Response } from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import certificationRoutes from './routes/certification.routes.js';

dotenv.config();

const app: Application = express();
const PORT = process.env.PORT || 3004;

app.use(cors());
app.use(express.json());

app.use('/api/certificates', certificationRoutes);

app.get('/health', (req: Request, res: Response) => {
  res.status(200).json({
    service: 'S4. Cierre, Certificación y Reportes',
    status: 'UP',
    port: PORT,
    timestamp: new Date().toISOString()
  });
});

app.listen(PORT, () => {
  console.log(`=================================================`);
  console.log(` Microservicio S4 activo en http://localhost:${PORT}`);
  console.log(` Endpoint Certificados: http://localhost:${PORT}/api/certificates`);
  console.log(`=================================================`);
});
