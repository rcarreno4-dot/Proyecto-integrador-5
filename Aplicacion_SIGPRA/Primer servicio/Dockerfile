# Etapa 1: Compilacion de TypeScript y preparacion de recursos
FROM node:20-alpine AS builder

WORKDIR /app

# Copiar archivos de dependencias
COPY package*.json ./
RUN npm install

# Copiar codigo fuente
COPY . .

# Compilar proyecto y asegurar recursos de Bootstrap
RUN npm run build

# Etapa 2: Imagen final optimizada para ejecucion
FROM node:20-alpine AS runner

WORKDIR /app
ENV NODE_ENV=production

COPY package*.json ./
RUN npm install --omit=dev

# Copiar distribucion compilada y recursos estaticos
COPY --from=builder /app/dist ./dist
COPY --from=builder /app/vendor ./vendor
COPY --from=builder /app/assets ./assets
COPY --from=builder /app/data ./data
COPY --from=builder /app/index.html ./index.html
COPY --from=builder /app/app.js ./app.js
COPY --from=builder /app/styles.css ./styles.css

EXPOSE 3000

CMD ["node", "dist/server.js"]
