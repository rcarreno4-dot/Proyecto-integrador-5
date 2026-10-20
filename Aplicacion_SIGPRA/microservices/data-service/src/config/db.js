const mongoose = require('mongoose');
require('dotenv').config({ path: '../../../.env' });

const connectDB = async () => {
  try {
    const uri = process.env.MONGO_URI || 'mongodb://127.0.0.1:27017/sigpra_mongo_db';
    await mongoose.connect(uri);
    console.log('[MongoDB] Conectado exitosamente.');
  } catch (err) {
    console.error('[MongoDB Error]:', err.message);
  }
};
module.exports = connectDB;
