@echo off
echo Cerrando procesos Java que puedan bloquear la carpeta build...
taskkill /F /IM java.exe 2>NUL
taskkill /F /IM javaw.exe 2>NUL
echo Eliminando carpeta build...
rmdir /S /Q build
echo Listo. Abra NetBeans y ejecute Clean and Build nuevamente.
pause
