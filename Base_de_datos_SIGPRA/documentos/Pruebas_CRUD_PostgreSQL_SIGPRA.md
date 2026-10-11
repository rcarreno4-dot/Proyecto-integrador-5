# Pruebas CRUD de PostgreSQL - SIGPRA

## Objetivo

Documentar la evidencia solicitada en el segundo avance para demostrar operaciones **CRUD (crear, leer, actualizar y borrar)** sobre tablas basicas de la base de datos SIGPRA.

La base PostgreSQL ya cuenta con el esquema `sigpra`, 16 tablas, indices y una vista. Este documento complementa esa evidencia con una prueba CRUD reproducible.

## Archivo de prueba

Ejecutar:

`Base_de_datos_SIGPRA/scripts/pruebas_crud_basicas.sql`

El script prueba las siguientes tablas:

- `institucion`
- `usuario`
- `convenio`
- `plaza_practica`

Las filas usan identificadores exclusivos de prueba y se eliminan al final, por lo que la ejecucion no debe dejar datos de prueba permanentes.

## Ejecucion recomendada

Desde la raiz del repositorio, en PowerShell:

```powershell
psql -U postgres -d sigpra -v ON_ERROR_STOP=1 -f Base_de_datos_SIGPRA/scripts/pruebas_crud_basicas.sql 2>&1 | Tee-Object -FilePath Base_de_datos_SIGPRA/documentos/Evidencia_CRUD_PostgreSQL_SIGPRA.txt
```

Si `psql` no esta en el `PATH`, usar la ruta completa de PostgreSQL 17.

Tambien se puede usar el ejecutor preparado, que solicita la clave del usuario
`postgres` y guarda la evidencia automaticamente:

```powershell
powershell -ExecutionPolicy Bypass -File Base_de_datos_SIGPRA/scripts/ejecutar_pruebas_crud_postgresql.ps1
```

## Criterios de aceptacion

La prueba se considera satisfactoria cuando:

1. **CREATE:** se insertan correctamente una institucion, un usuario, un convenio y una plaza.
2. **READ:** los `SELECT` muestran los registros creados.
3. **UPDATE:** se observan los valores actualizados de telefono/contacto, estado del usuario, fecha de fin del convenio y cupos de la plaza.
4. **DELETE:** se eliminan los registros en el orden correcto de dependencias.
5. **Verificacion final:** los cuatro conteos finales son `0`.
6. El proceso termina sin errores de restriccion, llaves foraneas o validacion.

## Evidencia para la entrega

Conservar el archivo generado:

`Base_de_datos_SIGPRA/documentos/Evidencia_CRUD_PostgreSQL_SIGPRA.txt`

Ademas, para la sustentacion o video se recomienda mostrar brevemente:

- `\dt sigpra.*`
- ejecucion del script CRUD;
- un `SELECT` de los registros creados;
- el bloque de verificacion final con conteos en cero.

## Relacion con la rubrica

Esta evidencia cubre el requisito:

> Montaje y pruebas de requisitos de la Base de Datos (CRUD crear, leer, actualizar y borrar de tablas basicas).

> **Importante:** este documento describe la prueba y el script. La evidencia queda completa solo despues de ejecutar el script y conservar la salida real de PostgreSQL.
