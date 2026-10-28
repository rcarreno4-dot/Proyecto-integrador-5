import cors from "cors";
import express from "express";
import fs from "fs";
import path from "path";
import { postgresRouter } from "./postgres/routes";
import { apiRouter } from "./routes";

export function createApp() {
  const app = express();
  const publicDir = fs.existsSync(path.join(process.cwd(), "index.html"))
    ? process.cwd()
    : path.resolve(__dirname, "..");

  app.use(cors());
  app.use(express.json({ limit: "2mb" }));
  app.use("/api", process.env.SIGPRA_STORAGE === "postgres" ? postgresRouter : apiRouter);
  app.use("/vendor/bootstrap", express.static(path.join(publicDir, "vendor/bootstrap")));
  app.use("/assets", express.static(path.join(publicDir, "assets")));
  app.get("/", (_req, res) => {
    res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    res.sendFile(path.join(publicDir, "index.html"));
  });
  app.get("/app.js", (_req, res) => {
    res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    res.sendFile(path.join(publicDir, "app.js"));
  });
  app.get("/styles.css", (_req, res) => {
    res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    res.sendFile(path.join(publicDir, "styles.css"));
  });

  return app;
}
