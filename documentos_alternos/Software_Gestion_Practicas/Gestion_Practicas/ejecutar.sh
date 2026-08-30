#!/usr/bin/env bash
cd "$(dirname "$0")"
echo "Iniciando sistema PROYECTOJD..."
java -cp "dist/GestionPracticas.jar:lib/ojdbc14.jar:lib/itextpdf-5.5.13.3.jar" com.gestionpracticas.Main
