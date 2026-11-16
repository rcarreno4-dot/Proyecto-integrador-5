**UNIVERSIDAD DE INVESTIGACIÓN Y DESARROLLO – UDI**

Facultad de Ingenierías

Programa de Ingeniería de Sistemas

**MANUAL TÉCNICO**

SIGPRA – Software Integral de Gestión de Prácticas Académicas

*Arquitectura, instalación, configuración y operación – versión 1.0*

Proyecto Integrador V – Quinto semestre

Ingeniería del Software II · Programación III · Teoría General de Sistemas

Jesús Daniel Rueda Castillo

Alonso Andrés Henao Roa

Rafael Fabián Carreño Barrera

Bucaramanga, noviembre de 2026

**Contenido**

# 1. Propósito y alcance

Este manual describe cómo está construida la versión entregada de SIGPRA y cómo se instala, se configura, se despliega y se mantiene. Está dirigido a quien tenga que ejecutar la aplicación, revisar su código o continuar su desarrollo.

## 6. Procedimientos de Respaldo
* **PostgreSQL:** `pg_dump -U sigpra_user -h localhost -d sigpra_db -F c -b -v -f 'backup_pg.dump'`
* **MongoDB:** `mongodump --uri='mongodb://localhost:27017/sigpra_mongo' --out='./backup_mongo/'`