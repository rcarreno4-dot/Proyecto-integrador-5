import express from 'express';
import type { Application, Request, Response } from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import assignmentRoutes from './routes/assignment.routes.js';

dotenv.config();

const app: Application = express();
const PORT = process.env.PORT || 3002;

app.use(cors());
app.use(express.json());

app.use('/api/assignments', assignmentRoutes);

app.get('/health', (req: Request, res: Response) => {
  res.status(200).json({ service: 'S2. Asignacion', status: 'UP', port: PORT });
});

app.listen(PORT, () => {
  console.log(`=================================================`);
  console.log(` Microservicio S2 corriendo en http://localhost:${PORT}`);
  console.log(`=================================================`);
});
