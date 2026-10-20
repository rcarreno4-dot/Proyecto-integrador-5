import express from 'express';
import type { Application, Request, Response } from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import trackingRoutes from './routes/tracking.routes.js';

dotenv.config();

const app: Application = express();
const PORT = process.env.PORT || 3003;

app.use(cors());
app.use(express.json());

app.use('/api/tracking', trackingRoutes);

app.get('/health', (req: Request, res: Response) => {
  res.status(200).json({
    service: 'S3. Ejecución y Seguimiento',
    status: 'UP',
    port: PORT,
    timestamp: new Date().toISOString()
  });
});

app.listen(PORT, () => {
  console.log(`=================================================`);
  console.log(` Microservicio S3 activo en http://localhost:${PORT}`);
  console.log(` Endpoint Seguimiento: http://localhost:${PORT}/api/tracking`);
  console.log(`=================================================`);
});
