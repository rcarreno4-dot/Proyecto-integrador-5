import mongoose from 'mongoose';
import dotenv from 'dotenv';

dotenv.config();

export const connectMongo = async (): Promise<void> => {
  try {
    const mongoUrl = process.env.MONGO_URL || 'mongodb://localhost:27017/sigpra_documental';
    await mongoose.connect(mongoUrl);
    console.log('MongoDB conectado para eventos de auditoría en S1');
  } catch (error) {
    console.error('Error conectando a MongoDB:', error);
  }
};