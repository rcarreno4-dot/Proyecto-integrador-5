@echo off
title SIGPRA Frontend React - Servidor de Desarrollo
echo ========================================================
echo   Iniciando SIGPRA Frontend React (Vite + TypeScript)
echo ========================================================
echo.

cd /d "%~dp0sigpra-frontend-react"

if not exist node_modules (
  echo [1/2] Instalando dependencias necesarias...
  call npm install --strict-ssl=false
) else (
  echo [1/2] Dependencias verificadas correctamente.
)

echo.
echo [2/2] Iniciando servidor Vite en http://localhost:5173 ...
echo Presiona Ctrl + C para detener el servidor cuando termines.
echo.

start http://localhost:5173

call npm run dev -- --host
pause
