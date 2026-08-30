# Diagramas - Primera entrega SIGPRA

## Diagrama de casos de uso

```mermaid
flowchart LR
  E[Estudiante] --> UC1((Autenticarse))
  E --> UC2((Autoregistrarse))
  E --> UC3((Registrar bitacora))
  E --> UC4((Cargar evidencia))
  E --> UC5((Consultar avance))
  D[Docente asesor] --> UC1
  D --> UC6((Consultar bitacoras))
  D --> UC7((Aprobar o rechazar bitacora))
  X[Director] --> UC1
  X --> UC8((Asignar practica))
  X --> UC9((Gestionar docentes))
  X --> UC10((Consultar reportes))
  X --> UC11((Cerrar o reabrir practica))
```

## Diagrama de dominio

```mermaid
classDiagram
  class Usuario { +bigint id +string correo +Rol rol +Estado estado }
  class Estudiante { +string codigo }
  class Docente { +string especialidad }
  class Director
  class Practica { +bigint id +string periodo +EstadoPractica estado +decimal horasRequeridas }
  class EntidadReceptora { +bigint id +string nombre }
  class Bitacora { +bigint id +date fecha +decimal horas +EstadoBitacora estado }
  class Validacion { +bigint id +Resultado resultado +string observacion }
  class Evidencia { +string nombreArchivo +string tipoMime +string url }
  Usuario <|-- Estudiante
  Usuario <|-- Docente
  Usuario <|-- Director
  Estudiante "1" --> "0..*" Practica : realiza
  Docente "1" --> "0..*" Practica : asesora
  EntidadReceptora "1" --> "0..*" Practica : recibe
  Practica "1" --> "0..*" Bitacora : contiene
  Bitacora "1" --> "0..*" Validacion : recibe
  Bitacora "1" --> "0..*" Evidencia : referencia
```

## Modelo entidad-relacion

```mermaid
erDiagram
  USUARIO ||--o| ESTUDIANTE : representa
  USUARIO ||--o| DOCENTE : representa
  ESTUDIANTE ||--o{ PRACTICA : realiza
  DOCENTE ||--o{ PRACTICA : asesora
  PROGRAMA ||--o{ PRACTICA : pertenece
  ENTIDAD_RECEPTORA ||--o{ PRACTICA : recibe
  PRACTICA ||--o{ BITACORA : contiene
  BITACORA ||--o{ VALIDACION : recibe
  BITACORA ||--o{ EVIDENCIA : referencia
```

## Arquitectura de componentes

```mermaid
flowchart TB
  Cliente[Cliente web] --> Router[Router REST]
  Router --> Auth[Servicio de autenticacion]
  Router --> Practicas[Servicio de practicas]
  Router --> Bitacoras[Servicio de bitacoras]
  Router --> Reportes[Servicio de reportes]
  Auth --> RepoSQL[Repositorio SQL]
  Practicas --> RepoSQL
  Bitacoras --> RepoSQL
  Bitacoras --> RepoMongo[Repositorio MongoDB]
  Reportes --> RepoSQL
  RepoSQL[(PostgreSQL)]
  RepoMongo[(MongoDB)]
```

## Modelo entidad-relacion visual

Archivo: `modelo_entidad_relacion.svg`.

![Modelo entidad-relacion](modelo_entidad_relacion.svg)

## Notas de entrega

- Estos diagramas cubren el alcance de la primera entrega: casos de uso y dominio/ER.
- Componentes, secuencias y despliegue se ampliaran en el segundo avance y la entrega final.
- El prototipo navegable esta en `prototipo_mediana_fidelidad.html`.
