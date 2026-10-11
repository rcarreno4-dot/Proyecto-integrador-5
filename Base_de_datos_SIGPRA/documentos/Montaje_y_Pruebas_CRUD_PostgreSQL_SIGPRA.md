# 9.4 Montaje y pruebas CRUD de la base de datos

## Objetivo

Esta seccion documenta el montaje de la base de datos relacional de SIGPRA y la prueba CRUD solicitada para evidenciar operaciones de crear, consultar, actualizar y eliminar registros sobre tablas basicas del sistema.

## Montaje de PostgreSQL

La base de datos se monto en PostgreSQL 17 sobre el servidor local `localhost`, puerto `5432`, con la base de datos `sigpra` y el esquema `sigpra`. El registro de instalacion y carga del esquema esta en `Base_de_datos_SIGPRA/documentos/Registro_PostgreSQL_SIGPRA.txt`.

Durante el montaje se ejecuto el archivo:

```text
Base_de_datos_SIGPRA/scripts/postgresql_schema.sql
```

El resultado registrado fue:

| Elemento verificado | Resultado |
| --- | --- |
| Base de datos | `sigpra` creada |
| Esquema | `sigpra` creado |
| Tablas | 16 tablas |
| Indices | 6 indices |
| Vista | 1 vista |
| Conexion | Verificada con usuario `postgres` |

Las tablas verificadas en el esquema son: `asignacion`, `convenio`, `coordinador_practicas`, `criterio_rubrica`, `detalle_evaluacion`, `docente_asesor`, `estudiante`, `evaluacion`, `evidencia`, `institucion`, `periodo_practica`, `plaza_practica`, `registro_actividad`, `rubrica`, `usuario` y `visita_seguimiento`.

## Pruebas CRUD

La prueba CRUD se preparo en el archivo:

```text
Base_de_datos_SIGPRA/scripts/pruebas_crud_basicas.sql
```

El script ejecuta operaciones sobre cuatro tablas basicas del dominio:

| Operacion | Evidencia dentro del script | Tablas involucradas |
| --- | --- | --- |
| CREATE | `INSERT INTO` | `institucion`, `usuario`, `convenio`, `plaza_practica` |
| READ | `SELECT` de registros creados | `institucion`, `usuario`, `convenio`, `plaza_practica` |
| UPDATE | `UPDATE` de campos de prueba | telefono/contacto, estado, fecha final, cupos |
| DELETE | `DELETE` en orden de dependencias | `plaza_practica`, `convenio`, `institucion`, `usuario` |

La prueba usa registros temporales identificados con `CRUD-TEST-2026`, `crud.sigpra@prueba.local` y `CONV-CRUD-2026-001`. Al final elimina los datos de prueba y ejecuta una verificacion de conteos. La prueba se considera correcta cuando los cuatro conteos finales son `0`, porque eso confirma que el CRUD se ejecuto sin dejar datos residuales.

## Comando de ejecucion

Desde la raiz del repositorio se puede ejecutar directamente:

```powershell
& "C:\Program Files\PostgreSQL\17\bin\psql.exe" -U postgres -d sigpra -v ON_ERROR_STOP=1 -f Base_de_datos_SIGPRA\scripts\pruebas_crud_basicas.sql 2>&1 | Tee-Object -FilePath Base_de_datos_SIGPRA\documentos\Evidencia_CRUD_PostgreSQL_SIGPRA.txt
```

Tambien se deja un ejecutor que solicita la clave del usuario PostgreSQL y guarda la evidencia automaticamente:

```powershell
powershell -ExecutionPolicy Bypass -File Base_de_datos_SIGPRA\scripts\ejecutar_pruebas_crud_postgresql.ps1
```

## Evidencia esperada

El archivo final de evidencia debe quedar en:

```text
Base_de_datos_SIGPRA/documentos/Evidencia_CRUD_PostgreSQL_SIGPRA.txt
```

Ese archivo debe contener la salida real de PostgreSQL con las operaciones `INSERT`, `SELECT`, `UPDATE`, `DELETE`, el `COMMIT` final y el bloque de verificacion con conteos en cero.

## Estado

El montaje de PostgreSQL, el script CRUD y la evidencia real de ejecucion ya estan completos en el repositorio. La salida se conserva en `Base_de_datos_SIGPRA/documentos/Evidencia_CRUD_PostgreSQL_SIGPRA.txt` y confirma que la prueba termino con `COMMIT` y conteos finales en cero.
