
# ANALISIS.md

## Arquitectura propuesta

- modelo
- dao
- servicio
- vista
- util

## Objetivos

- Mantener ConexionBD.java intacto
- Compatibilidad Java 8 + Oracle 10g
- Uso de ojdbc8.jar
- Refactorización MVC
- ScrollPane en todas las vistas
- Responsive Swing usando GridBagLayout y BorderLayout

## Layouts seleccionados

- BorderLayout
- GridBagLayout
- JScrollPane

## Resoluciones soportadas

- 1024x768
- 1280x800
- 1920x1080

## Responsabilidades

### DAO
CRUD puro y acceso a datos.

### Servicio
Validaciones y reglas de negocio.

### Vista
Interacción con usuario y eventos Swing.

### Modelo
POJOs serializables.
