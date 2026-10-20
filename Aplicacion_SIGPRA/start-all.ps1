$basePath = "C:\Users\jrued\OneDrive\Documentos\INTEGRADOR\Proyecto-integrador-5\Aplicacion_SIGPRA"

Write-Host "=================================================" -ForegroundColor Cyan
Write-Host "   INICIANDO ARQUITECTURA SIGPRA INTEGRADA       " -ForegroundColor Cyan
Write-Host "=================================================" -ForegroundColor Cyan

# Iniciar Microservicios independientes en consolas separadas
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$basePath\s1-planeacion-convenios'; npm run dev"
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$basePath\s2-asignacion-practicas'; npm run dev"
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$basePath\s3-ejecucion-seguimiento'; npm run dev"
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$basePath\s4-cierre-certificacion'; npm run dev"

Start-Sleep -Seconds 3

# Abrir el Frontend unificado
Start-Process "$basePath\frontend\index.html"
