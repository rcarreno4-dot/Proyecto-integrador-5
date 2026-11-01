# ⚛️ SIGPRA - Módulo Frontend en React 18 & TypeScript
### Proyecto Integrador V — Universidad de Investigación y Desarrollo (UDI)

Este directorio contiene la implementación moderna y paralela del frontend de **SIGPRA** utilizando **React 18**, **TypeScript**, **Vite** y **Bootstrap 5**.

---

## 🛡️ Principio de Seguridad de la Entrega

> [!IMPORTANT]
> **Cero afectación a la versión en producción:**
> Esta carpeta es completamente independiente y paralela. La versión principal y oficial de SIGPRA ubicada en `Aplicacion_SIGPRA/Primer servicio/` permanece **100% intacta, funcional y desplegada en la nube con Render**.

---

## 🚀 Cómo Ejecutar la Versión React

Para iniciar el servidor de desarrollo local:

```bash
cd sigpra-frontend-react
npm run dev
```

La aplicación se abrirá en `http://localhost:5173/`.

Para compilar para producción:

```bash
npm run build
```

---

## 🏛️ Arquitectura por Componentes

```
sigpra-frontend-react/
├── src/
│   ├── components/
│   │   ├── common/
│   │   │   └── KPICard.tsx              # Tarjetas métricas interactivas (Amber, Green, Blue, Purple)
│   │   ├── layout/
│   │   │   ├── Navbar.tsx               # Barra superior UDI con selector dinámico de roles
│   │   │   └── Sidebar.tsx              # Navegación reactiva con filtrado estricto por permisos
│   │   └── views/
│   │       ├── CoordinatorDashboard.tsx # Panel de Coordinación con accesos directos
│   │       ├── DirectorDashboard.tsx    # Panel de Dirección con dictamen (CU-08) y modales
│   │       ├── StudentDashboard.tsx     # Panel de Estudiante con velocímetro de 160h
│   │       ├── StudentRequestView.tsx   # Radicación con protección de práctica asignada
│   │       ├── TeacherDashboard.tsx     # Panel de Docente Asesor y seguimiento
│   │       ├── PeriodsView.tsx          # Gestión de periodos con año dinámico y tope 320h
│   │       ├── ReportsView.tsx          # Búsqueda bajo demanda, exportador CSV y print oficial
│   │       └── AuditView.tsx            # Registro inmutable exclusivo para Director
│   ├── context/
│   │   └── SigpraContext.tsx            # Estado global con React Context y sincronización LocalStorage
│   ├── data/
│   │   └── seedData.ts                  # Datos institucionales UDI
│   ├── types/
│   │   └── index.ts                     # Modelos TypeScript fuertemente tipados
│   ├── App.tsx                          # Enrutador principal de vistas
│   ├── index.css                        # Estilos globales y reglas @media print
│   └── main.tsx                         # Punto de entrada React 18
├── index.html
├── package.json
├── tsconfig.json
└── vite.config.ts
```

---

## ✨ Requerimientos Funcionales y Normativos Cumplidos

1. **Exportación JavaScript Directa de la Tabla Visible:** La función `handleExportCsv` en `ReportsView.tsx` genera un CSV con formato oficial (UTF-8 con BOM y `;`) leyendo directamente las filas visibles tras aplicar búsquedas en vivo.
2. **Información y Reportes Bajo Demanda:** La vista de consolidado inicializa limpia con el formulario de filtros; las tablas y los botones de exportación/impresión se despliegan **únicamente cuando se ejecuta la búsqueda**.
3. **Impresión Oficial UDI Focalizada:** La hoja de estilo `@media print` oculta elementos auxiliares y filas descartadas, imprimiendo únicamente los registros buscados con membrete UDI y firmas de Coordinación y Dirección.
4. **Auditoría Exclusiva para el Director:** El módulo y la vista `AuditView` están estrictamente restringidos al rol `DIRECTOR`, bloqueando accesos no autorizados.
5. **Configuración de Periodos (Año Dinámico y Validaciones):** Carga dinámica de `new Date().getFullYear()`, calendario con bloqueo de fechas pasadas (`min="today"`) y validación estricta de máximo 320 horas (Decreto 1075).
6. **Protección de Estudiante con Práctica Asignada:** Si el practicante ya tiene una práctica activa (`status === "ACTIVA"`), se oculta la opción de solicitar práctica en el menú lateral y la vista muestra su asignación formal orientándolo a registrar su bitácora.
