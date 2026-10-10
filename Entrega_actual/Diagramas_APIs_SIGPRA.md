# Diagramas de funcionamiento de las APIs SIGPRA

Estos diagramas representan el funcionamiento actual de la aplicacion web.
El backend usa Node.js, Express y TypeScript con organizacion MVC. Actualmente
guarda el estado en un archivo JSON del servidor; la conexion directa con
PostgreSQL aun no esta implementada.

## Arquitectura de las APIs

```mermaid
flowchart LR
    N[Navegador] --> R[Express: rutas /api]
    R --> C[Controladores MVC]
    C --> S[Servicios]
    S --> M[SigpraStore]
    M --> J[(sigpra-store.json)]
```

## Carga inicial de la aplicacion

```mermaid
sequenceDiagram
    participant Web as Aplicacion web
    participant API as API Express
    participant Datos as SigpraStore

    Web->>API: GET /api/health
    API-->>Web: Servidor disponible
    par Estado
        Web->>API: GET /api/state
        API->>Datos: all()
        Datos-->>API: Estado completo
        API-->>Web: JSON
    and Periodos
        Web->>API: GET /api/periods
        API->>Datos: periods()
        Datos-->>API: Periodos y rubricas
        API-->>Web: JSON
    and Convenios
        Web->>API: GET /api/agreements
        API->>Datos: agreements()
        Datos-->>API: Instituciones, convenios y plazas
        API-->>Web: JSON
    and Asignaciones
        Web->>API: GET /api/assignments
        API->>Datos: assignments()
        Datos-->>API: Asignaciones
        API-->>Web: JSON
    end
    Web->>Web: Mostrar las pantallas
```

Las tres APIs de dominio consultadas al iniciar son:

| API | Informacion devuelta |
| --- | --- |
| `GET /api/periods` | Periodos academicos y rubricas. |
| `GET /api/agreements` | Instituciones, convenios y plazas. |
| `GET /api/assignments` | Asignaciones de practicantes. |

## Guardado desde un formulario

```mermaid
sequenceDiagram
    participant Usuario
    participant Web as Aplicacion web
    participant API as API Express
    participant Archivo as sigpra-store.json

    Usuario->>Web: Completa y guarda un formulario
    Web->>Web: Actualiza datos y localStorage
    Web->>API: PUT /api/state con el estado completo
    API->>Archivo: Guarda el estado en JSON
    API-->>Web: Confirma los datos guardados
```

Las rutas `/api/periods`, `/api/agreements` y `/api/assignments` tambien
exponen `POST` para crear registros. Los formularios actuales de la interfaz
sincronizan sus cambios mediante `PUT /api/state`, no mediante esos `POST`.
