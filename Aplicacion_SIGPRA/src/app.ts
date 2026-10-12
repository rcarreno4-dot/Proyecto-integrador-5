import cors from "cors";
import express from "express";
import path from "path";
import { postgresRouter } from "./postgres/routes";
import { apiRouter } from "./routes";

export function createApp() {
  const app = express();
  const publicDir = process.cwd();

  app.use(cors());
  app.use(express.json({ limit: "2mb" }));
  app.use("/api", process.env.SIGPRA_STORAGE === "postgres" ? postgresRouter : apiRouter);
  app.get("/", (_req, res) => res.sendFile(path.join(publicDir, "index.html")));
  app.get("/app.js", (_req, res) => res.sendFile(path.join(publicDir, "app.js")));
  app.get("/styles.css", (_req, res) => res.sendFile(path.join(publicDir, "styles.css")));

  return app;
}
