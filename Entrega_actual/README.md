# Entrega actual - SIGPRA

## Documento principal

- `Propuesta_SIGPRA_Segunda_Entrega.docx`: propuesta, análisis de requerimientos, fichas de casos de uso, modelos de datos, prototipo y referencias.

## Modelos y diagramas

- `Diagramas UML/Arquitectura/`: diagrama de arquitectura.
- `Diagramas UML/Casos de uso/`: diagrama de casos de uso.
- `Diagramas UML/Dominio/`: diagrama conceptual de dominio.
- `Diagramas UML/Secuencia/`: exportaciones de los diagramas de secuencia CU-01 a CU-07.
- `Diagramas UML/Clases y modelos editables/Proyecto_astah_SIGPRA.asta`: archivo editable de Astah con los diagramas de casos de uso, clases, dominio y secuencia para CU-01 a CU-07.
- `../Base_de_datos_SIGPRA/imagenes/Modelo_Entidad_Relacion_SIGPRA.png`: modelo ER alineado con el esquema PostgreSQL.
- `../Base_de_datos_SIGPRA/imagenes/Modelo_Relacional_SIGPRA.png`: modelo relacional de las 16 tablas PostgreSQL.
- `../Base_de_datos_SIGPRA/Diccionario_Datos_SIGPRA.md`: diccionario de tablas/columnas, colecciones MongoDB y objetos de almacenamiento de archivos.

## Persistencia SIGPRA

El modelado abarca los tres componentes de persistencia documentados para el
sistema: PostgreSQL (base relacional), MongoDB (base documental) y
almacenamiento de archivos (binarios de evidencias; no es un motor de base de
datos). Los scripts fuente se encuentran en `../Base_de_datos_SIGPRA/`.

## Prototipo y seguimiento

- `Prototipos_Figma_SIGPRA.md`: enlace al prototipo de mediana fidelidad en Figma.
- `Vistas_actuales_SIGPRA/`: capturas de las vistas actuales de la aplicacion para CU-01 a CU-07.
- `Acta_segumiento_blank.docx` y `Actas de seguimiento/`: formatos y actas de seguimiento.
