# ==============================================================================
# SCRIPT COMPLETO SIGPRA - MULTI-PÁGINA INTERACTIVO, ANIMACIONES Y DBs
# Ruta: C:\Users\jrued\OneDrive\Documentos\Aplicacion completa
# Archivo: setup_sigpra.ps1
# ==============================================================================

$BasePath = "C:\Users\jrued\OneDrive\Documentos\Aplicacion completa"

Write-Host "========================================================================" -ForegroundColor Cyan
Write-Host " Generando SIGPRA (Vistas interactivas, animaciones CSS/JS y DBs)..." -ForegroundColor Green
Write-Host "========================================================================" -ForegroundColor Cyan

function Write-Utf8File {
    param (
        [string]$FilePath,
        [string]$Content
    )
    $dir = Split-Path -Path $FilePath -Parent
    if (-not (Test-Path $dir)) { New-Item -Path $dir -ItemType Directory -Force | Out-Null }
    $utf8NoBom = New-Object System.Text.UTF8Encoding $False
    [System.IO.File]::WriteAllText($FilePath, $Content, $utf8NoBom)
    Write-Host "[+] Archivo generado con éxito: $FilePath" -ForegroundColor Yellow
}

# ------------------------------------------------------------------------------
# 1. BASE DE DATOS POSTGRESQL (database\init_postgres.sql)
# ------------------------------------------------------------------------------
$PostgresSql = @'
-- ==============================================================================
-- ESQUEMA COMPLETO Y DATOS SEMILLA POSTGRESQL (ESTRUCTURA RELACIONAL SIGPRA)
-- ==============================================================================

DROP TABLE IF EXISTS evaluaciones_visita CASCADE;
DROP TABLE IF EXISTS asignaciones_practica CASCADE;
DROP TABLE IF EXISTS solicitudes_practica CASCADE;
DROP TABLE IF EXISTS plazas_practica CASCADE;
DROP TABLE IF EXISTS convenios CASCADE;
DROP TABLE IF EXISTS periodos_practica CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    documento VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE periodos_practica (
    id SERIAL PRIMARY KEY,
    codigo_periodo VARCHAR(20) UNIQUE NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    horas_requeridas INT NOT NULL DEFAULT 320,
    estado VARCHAR(20) DEFAULT 'En Curso'
);

CREATE TABLE convenios (
    id SERIAL PRIMARY KEY,
    nit_rut VARCHAR(20) UNIQUE NOT NULL,
    nombre_empresa VARCHAR(150) NOT NULL,
    numero_convenio VARCHAR(50) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    estado VARCHAR(20) DEFAULT 'Vigente'
);

CREATE TABLE plazas_practica (
    id SERIAL PRIMARY KEY,
    convenio_id INT REFERENCES convenios(id) ON DELETE CASCADE,
    titulo_plaza VARCHAR(150) NOT NULL,
    modalidad VARCHAR(30) CHECK (modalidad IN ('Presencial', 'Híbrida', 'Remota')),
    cupos INT NOT NULL DEFAULT 1,
    descripcion TEXT,
    estado VARCHAR(20) DEFAULT 'Disponible'
);

CREATE TABLE solicitudes_practica (
    id SERIAL PRIMARY KEY,
    estudiante_id INT REFERENCES usuarios(id),
    plaza_id INT REFERENCES plazas_practica(id),
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'Pendiente'
);

CREATE TABLE asignaciones_practica (
    id SERIAL PRIMARY KEY,
    solicitud_id INT REFERENCES solicitudes_practica(id),
    docente_id INT REFERENCES usuarios(id),
    periodo_id INT REFERENCES periodos_practica(id),
    fecha_asignacion DATE DEFAULT CURRENT_DATE,
    estado VARCHAR(20) DEFAULT 'Aprobada'
);

CREATE TABLE evaluaciones_visita (
    id SERIAL PRIMARY KEY,
    asignacion_id INT REFERENCES asignaciones_practica(id),
    numero_visita INT CHECK (numero_visita IN (1, 2, 3)),
    nota_tecnica NUMERIC(3,2) CHECK (nota_tecnica BETWEEN 0.0 AND 5.0),
    nota_etica NUMERIC(3,2) CHECK (nota_etica BETWEEN 0.0 AND 5.0),
    nota_comunicacion NUMERIC(3,2) CHECK (nota_comunicacion BETWEEN 0.0 AND 5.0),
    nota_promedio NUMERIC(3,2),
    observaciones TEXT,
    fecha_evaluacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO usuarios (documento, nombre, email, password_hash, rol) VALUES
('1001', 'Dr. Carlos Mendoza', 'director@udi.edu.co', 'director123', 'Director del Programa'),
('1002', 'Ing. Martha Gómez', 'coordinador@udi.edu.co', 'coordinador123', 'Coordinador de Práctica'),
('1003', 'Prof. Roberto Silva', 'docente@udi.edu.co', 'docente123', 'Docente Asesor'),
('1004', 'Juan Rueda', 'estudiante@udi.edu.co', 'estudiante123', 'Estudiante'),
('9001', 'Tech Solutions S.A.S', 'empresa@techsolutions.com', 'empresa123', 'Institución Receptora');

INSERT INTO periodos_practica (codigo_periodo, fecha_inicio, fecha_fin, horas_requeridas, estado) VALUES
('2026-1', '2026-02-01', '2026-06-30', 320, 'En Curso'),
('2025-2', '2025-08-01', '2025-12-15', 320, 'Finalizado');

INSERT INTO convenios (nit_rut, nombre_empresa, numero_convenio, fecha_inicio, fecha_vencimiento, estado) VALUES
('900123456-1', 'Tech Solutions S.A.S', 'CONV-2025-089', '2025-01-15', '2028-12-31', 'Vigente');

INSERT INTO plazas_practica (convenio_id, titulo_plaza, modalidad, cupos, descripcion, estado) VALUES
(1, 'Desarrollador Junior Fullstack', 'Híbrida', 2, 'Desarrollo de microservicios en Node.js y React con PostgreSQL y MongoDB.', 'Disponible');

INSERT INTO solicitudes_practica (estudiante_id, plaza_id, estado) VALUES
(4, 1, 'Aprobada');

INSERT INTO asignaciones_practica (solicitud_id, docente_id, periodo_id, estado) VALUES
(1, 3, 1, 'Aprobada');

INSERT INTO evaluaciones_visita (asignacion_id, numero_visita, nota_tecnica, nota_etica, nota_comunicacion, nota_promedio, observaciones) VALUES
(1, 1, 4.8, 5.0, 4.7, 4.83, 'El practicante demuestra un alto desempeño técnico y excelente compromiso.');
'@
Write-Utf8File -FilePath "$BasePath\database\init_postgres.sql" -Content $PostgresSql

# ------------------------------------------------------------------------------
# 2. BASE DE DATOS MONGODB (database\init_mongo.js)
# ------------------------------------------------------------------------------
$MongoJs = @'
// ==============================================================================
// COLECCIÓN Y DOCUMENTOS MONGODB (SIGPRA - BITÁCORAS CU-04 Y CU-05)
// ==============================================================================

db = db.getSiblingDB('sigpra_nosql');
db.actividades_bitacora.drop();

db.createCollection('actividades_bitacora', {
   validator: {
      $jsonSchema: {
         bsonType: "object",
         required: [ "estudiante_id", "fecha_actividad", "horas_reportadas", "descripcion", "estado" ],
         properties: {
            estudiante_id: { bsonType: "int" },
            fecha_actividad: { bsonType: "string" },
            horas_reportadas: { bsonType: "int", minimum: 1, maximum: 12 },
            descripcion: { bsonType: "string" },
            soporte_adjunto: { bsonType: "string" },
            estado: { enum: [ "Pendiente", "Aprobado", "Rechazado" ] },
            observacion_docente: { bsonType: "string" },
            fecha_registro: { bsonType: "date" }
         }
      }
   }
});

db.actividades_bitacora.insertMany([
   {
      estudiante_id: 4,
      estudiante_nombre: "Juan Rueda",
      fecha_actividad: "2026-02-15",
      horas_reportadas: 8,
      descripcion: "Análisis de requerimientos y diseño de modelos relacionales en PostgreSQL y NoSQL en MongoDB.",
      soporte_adjunto: "diagrama_arquitectura.pdf",
      estado: "Aprobado",
      observacion_docente: "Excelente sustentación técnica de esquemas.",
      fecha_registro: new Date()
   },
   {
      estudiante_id: 4,
      estudiante_nombre: "Juan Rueda",
      fecha_actividad: "2026-02-22",
      horas_reportadas: 8,
      descripcion: "Desarrollo de interfaces gráficas interactivas con CSS3, animaciones y módulos JS por rol.",
      soporte_adjunto: "pantallazos_interfaz.png",
      estado: "Pendiente",
      observacion_docente: "",
      fecha_registro: new Date()
   }
]);
'@
Write-Utf8File -FilePath "$BasePath\database\init_mongo.js" -Content $MongoJs

# ------------------------------------------------------------------------------
# 3. ESTILOS CSS CON ANIMACIONES MODERNAS (portal-web\public\css\styles.css)
# ------------------------------------------------------------------------------
$CssContent = @'
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');

:root {
  --primary: #2563eb;
  --primary-dark: #1d4ed8;
  --primary-light: #eff6ff;
  --dark-bg: #0f172a;
  --sidebar-bg: #1e293b;
  --card-bg: #ffffff;
  --body-bg: #f8fafc;
  --text-primary: #0f172a;
  --text-secondary: #64748b;
  --border: #e2e8f0;
  --shadow-sm: 0 1px 3px rgba(0,0,0,0.05);
  --shadow-md: 0 4px 6px -1px rgba(0,0,0,0.08);
  --shadow-lg: 0 10px 25px -5px rgba(0,0,0,0.1);
  --radius-lg: 16px;
  --radius-md: 10px;
}

body {
  font-family: 'Inter', sans-serif;
  background-color: var(--body-bg);
  color: var(--text-primary);
  margin: 0;
  padding: 0;
  overflow-x: hidden;
}

@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(18px); }
  to { opacity: 1; transform: translateY(0); }
}

.animated-view {
  animation: fadeInUp 0.4s cubic-bezier(0.16, 1, 0.3, 1) forwards;
}

.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 50%, #1e1b4b 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-card {
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(16px);
  border-radius: 20px;
  box-shadow: var(--shadow-lg);
  width: 100%;
  max-width: 450px;
  padding: 40px;
  animation: fadeInUp 0.5s ease-out;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.dashboard-wrapper {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 270px;
  background-color: var(--sidebar-bg);
  color: #ffffff;
  display: flex;
  flex-direction: column;
  position: fixed;
  height: 100vh;
  z-index: 100;
  box-shadow: 4px 0 15px rgba(0,0,0,0.05);
}

.sidebar-header {
  padding: 24px;
  border-bottom: 1px solid rgba(255,255,255,0.08);
  display: flex;
  align-items: center;
  gap: 12px;
}

.sidebar-menu {
  list-style: none;
  padding: 16px;
  margin: 0;
  flex-grow: 1;
}

.sidebar-menu li {
  margin-bottom: 6px;
}

.sidebar-menu a {
  color: #94a3b8;
  text-decoration: none;
  padding: 12px 16px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  gap: 12px;
  font-weight: 500;
  font-size: 0.93rem;
  transition: all 0.25s ease;
}

.sidebar-menu a:hover {
  color: #ffffff;
  background-color: rgba(255, 255, 255, 0.08);
  transform: translateX(4px);
}

.sidebar-menu a.active {
  color: #ffffff;
  background: linear-gradient(135deg, var(--primary), var(--primary-dark));
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.35);
}

.main-content {
  margin-left: 270px;
  flex-grow: 1;
  padding: 32px;
  min-height: 100vh;
}

.top-bar {
  background: #ffffff;
  padding: 18px 28px;
  border-radius: var(--radius-lg);
  border: 1px solid var(--border);
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 28px;
  box-shadow: var(--shadow-sm);
}

.card-custom {
  background: var(--card-bg);
  border-radius: var(--radius-lg);
  border: 1px solid var(--border);
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: var(--shadow-sm);
  transition: transform 0.25s ease, box-shadow 0.25s ease;
}

.card-custom:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
}

.metric-card {
  border-radius: var(--radius-lg);
  padding: 24px;
  color: white;
  transition: transform 0.3s ease;
}

.metric-card:hover {
  transform: translateY(-4px);
}

.metric-card.primary { background: linear-gradient(135deg, #2563eb, #1d4ed8); }
.metric-card.success { background: linear-gradient(135deg, #059669, #047857); }

.table-custom {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
}

.table-custom th {
  background: #f8fafc;
  text-align: left;
  padding: 14px 18px;
  font-size: 12px;
  font-weight: 700;
  text-transform: uppercase;
  color: var(--text-secondary);
  border-bottom: 1px solid var(--border);
}

.table-custom td {
  padding: 16px 18px;
  border-bottom: 1px solid var(--border);
  font-size: 0.93rem;
  vertical-align: middle;
}

.table-custom tr:hover td {
  background-color: #f1f5f9;
}

.badge-status {
  padding: 5px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.badge-active { background: #dcfce7; color: #15803d; }
.badge-pending { background: #fef3c7; color: #b45309; }

#toast-container {
  position: fixed;
  bottom: 24px;
  right: 24px;
  z-index: 9999;
}

.toast-message {
  background: #0f172a;
  color: #ffffff;
  padding: 14px 22px;
  border-radius: 12px;
  box-shadow: 0 10px 20px rgba(0,0,0,0.2);
  display: flex;
  align-items: center;
  gap: 12px;
  font-weight: 500;
  animation: fadeInUp 0.3s ease;
}
'@
Write-Utf8File -FilePath "$BasePath\portal-web\public\css\styles.css" -Content $CssContent

# ------------------------------------------------------------------------------
# 4. JS DE AUTENTICACIÓN (portal-web\public\js\auth.js)
# ------------------------------------------------------------------------------
$AuthJs = @'
const UserDatabase = [
    { email: 'director@udi.edu.co', pass: 'director123', nombre: 'Dr. Carlos Mendoza', rol: 'Director del Programa', redirect: 'director.html' },
    { email: 'coordinador@udi.edu.co', pass: 'coordinador123', nombre: 'Ing. Martha Gómez', rol: 'Coordinador de Práctica', redirect: 'coordinador.html' },
    { email: 'docente@udi.edu.co', pass: 'docente123', nombre: 'Prof. Roberto Silva', rol: 'Docente Asesor', redirect: 'docente.html' },
    { email: 'estudiante@udi.edu.co', pass: 'estudiante123', nombre: 'Juan Rueda', rol: 'Estudiante', redirect: 'estudiante.html' },
    { email: 'empresa@techsolutions.com', pass: 'empresa123', nombre: 'Tech Solutions S.A.S', rol: 'Institución Receptora', redirect: 'empresa.html' }
];

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const email = document.getElementById('email').value.trim();
            const password = document.getElementById('password').value.trim();
            const alertBox = document.getElementById('login-alert');

            const foundUser = UserDatabase.find(u => u.email.toLowerCase() === email.toLowerCase() && u.pass === password);

            if (foundUser) {
                localStorage.setItem('sigpra_user', JSON.stringify(foundUser));
                window.location.href = foundUser.redirect;
            } else {
                alertBox.classList.remove('d-none');
                alertBox.textContent = 'Correo electrónico o contraseña incorrectos.';
            }
        });
    }
});

function quickFill(email, pass) {
    document.getElementById('email').value = email;
    document.getElementById('password').value = pass;
}
'@
Write-Utf8File -FilePath "$BasePath\portal-web\public\js\auth.js" -Content $AuthJs

# ------------------------------------------------------------------------------
# 5. JS TIEMPO REAL Y MÓDULOS (portal-web\public\js\dashboard.js)
# ------------------------------------------------------------------------------
$DashboardJs = @'
const SigpraStore = {
    get: (key, defaultValue) => {
        const val = localStorage.getItem('sigpra_' + key);
        return val ? JSON.parse(val) : defaultValue;
    },
    set: (key, value) => {
        localStorage.setItem('sigpra_' + key, JSON.stringify(value));
    }
};

if (!localStorage.getItem('sigpra_periodos')) {
    SigpraStore.set('periodos', [
        { id: 1, codigo: '2026-1', inicio: '2026-02-01', fin: '2026-06-30', horas: 320, estado: 'En Curso' },
        { id: 2, codigo: '2025-2', inicio: '2025-08-01', fin: '2025-12-15', horas: 320, estado: 'Finalizado' }
    ]);
}

if (!localStorage.getItem('sigpra_plazas')) {
    SigpraStore.set('plazas', [
        { id: 1, empresa: 'Tech Solutions S.A.S', titulo: 'Desarrollador Junior Fullstack', modalidad: 'Híbrida', cupos: 2, estado: 'Disponible', descripcion: 'Desarrollo de microservicios en Node.js y React con PostgreSQL y MongoDB.' },
        { id: 2, empresa: 'Bancolombia S.A.', titulo: 'Analista de Ciberseguridad y Datos', modalidad: 'Presencial', cupos: 3, estado: 'Disponible', descripcion: 'Análisis de vulnerabilidades, auditoría de logs y gestión de seguridad informática.' }
    ]);
}

if (!localStorage.getItem('sigpra_solicitudes')) {
    SigpraStore.set('solicitudes', [
        { id: 1, estudiante: 'Juan Rueda', doc: '1004', plaza: 'Desarrollador Junior Fullstack', empresa: 'Tech Solutions S.A.S', docente: 'Prof. Roberto Silva', estado: 'Pendiente' }
    ]);
}

if (!localStorage.getItem('sigpra_bitacoras')) {
    SigpraStore.set('bitacoras', [
        { id: 'act1', estudiante: 'Juan Rueda', fecha: '2026-02-15', horas: 8, descripcion: 'Análisis de requerimientos y diseño de modelos relacionales en PostgreSQL y NoSQL en MongoDB.', soporte: 'diagrama_arquitectura.pdf', estado: 'Aprobado', observacion: 'Excelente diseño de esquemas.' },
        { id: 'act2', estudiante: 'Juan Rueda', fecha: '2026-02-22', horas: 8, descripcion: 'Desarrollo de interfaces gráficas interactivas con CSS3, animaciones y módulos JS por rol.', soporte: 'pantallazos_ui.png', estado: 'Pendiente', observacion: '' }
    ]);
}

if (!localStorage.getItem('sigpra_evaluaciones')) {
    SigpraStore.set('evaluaciones', [
        { id: 1, estudiante: 'Juan Rueda', visita: 'Visita 1 - Inicial', notaTecnica: 4.8, notaEtica: 5.0, notaComunicacion: 4.7, promedio: 4.83, observaciones: 'Excelente actitud y competencias técnicas.' }
    ]);
}

function checkSession(requiredRole) {
    const sessionData = localStorage.getItem('sigpra_user');
    if (!sessionData) {
        window.location.href = 'index.html';
        return null;
    }
    const user = JSON.parse(sessionData);
    if (requiredRole && user.rol !== requiredRole) {
        alert('Acceso no autorizado para su rol.');
        window.location.href = user.redirect;
        return null;
    }
    
    document.addEventListener('DOMContentLoaded', () => {
        const nameElem = document.getElementById('user-name');
        const roleElem = document.getElementById('user-role');
        if (nameElem) nameElem.textContent = user.nombre;
        if (roleElem) roleElem.textContent = user.rol;
    });
    return user;
}

function logout() {
    localStorage.removeItem('sigpra_user');
    window.location.href = 'index.html';
}

function showNotification(msg) {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        document.body.appendChild(container);
    }
    const toast = document.createElement('div');
    toast.className = 'toast-message';
    toast.innerHTML = `<i class="fa-solid fa-circle-check text-success"></i> <span>${msg}</span>`;
    container.appendChild(toast);
    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transition = 'opacity 0.4s ease';
        setTimeout(() => toast.remove(), 400);
    }, 3000);
}

function switchTab(viewId) {
    document.querySelectorAll('.view-panel').forEach(el => el.classList.add('d-none'));
    const target = document.getElementById(viewId);
    if (target) {
        target.classList.remove('d-none');
        target.classList.add('animated-view');
    }
    document.querySelectorAll('.sidebar-menu a').forEach(el => el.classList.remove('active'));
    if (event && event.currentTarget) {
        event.currentTarget.classList.add('active');
    }
}
'@
Write-Utf8File -FilePath "$BasePath\portal-web\public\js\dashboard.js" -Content $DashboardJs

# ------------------------------------------------------------------------------
# 6. INDEX.HTML - LOGIN PRINCIPAL
# ------------------------------------------------------------------------------
$IndexHtml = @'
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SIGPRA - Inicio de Sesión</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="css/styles.css" rel="stylesheet">
</head>
<body>

<div class="login-container">
    <div class="login-card">
        <div class="login-header text-center">
            <i class="fa-solid fa-graduation-cap"></i>
            <h3 class="fw-bold text-dark mt-2 mb-1">Portal SIGPRA</h3>
            <p class="text-muted small">Sistema de Gestión de Prácticas Académicas</p>
        </div>

        <div id="login-alert" class="alert alert-danger d-none small mb-3"></div>

        <form id="login-form">
            <div class="mb-3">
                <label class="form-label fw-semibold small text-secondary">Correo Electrónico Personal</label>
                <div class="input-group">
                    <span class="input-group-text bg-light"><i class="fa-solid fa-envelope text-muted"></i></span>
                    <input type="email" id="email" class="form-control" placeholder="ejemplo@udi.edu.co" required>
                </div>
            </div>

            <div class="mb-4">
                <label class="form-label fw-semibold small text-secondary">Contraseña</label>
                <div class="input-group">
                    <span class="input-group-text bg-light"><i class="fa-solid fa-lock text-muted"></i></span>
                    <input type="password" id="password" class="form-control" placeholder="••••••••" required>
                </div>
            </div>

            <button type="submit" class="btn btn-primary w-100 py-2.5 fw-bold text-uppercase"><i class="fa-solid fa-right-to-bracket me-2"></i> Iniciar Sesión</button>
        </form>

        <hr class="my-4">

        <div class="small">
            <p class="fw-bold text-secondary mb-2">Credenciales Registradas (Clic para autocompletar):</p>
            <div class="list-group list-group-flush border rounded overflow-hidden">
                <button type="button" class="list-group-item list-group-item-action p-2 text-start small" onclick="quickFill('director@udi.edu.co', 'director123')">
                    <strong>Director:</strong> director@udi.edu.co
                </button>
                <button type="button" class="list-group-item list-group-item-action p-2 text-start small" onclick="quickFill('coordinador@udi.edu.co', 'coordinador123')">
                    <strong>Coordinador:</strong> coordinador@udi.edu.co
                </button>
                <button type="button" class="list-group-item list-group-item-action p-2 text-start small" onclick="quickFill('docente@udi.edu.co', 'docente123')">
                    <strong>Docente Asesor:</strong> docente@udi.edu.co
                </button>
                <button type="button" class="list-group-item list-group-item-action p-2 text-start small" onclick="quickFill('estudiante@udi.edu.co', 'estudiante123')">
                    <strong>Estudiante:</strong> estudiante@udi.edu.co
                </button>
                <button type="button" class="list-group-item list-group-item-action p-2 text-start small" onclick="quickFill('empresa@techsolutions.com', 'empresa123')">
                    <strong>Empresa:</strong> empresa@techsolutions.com
                </button>
            </div>
        </div>
    </div>
</div>

<script src="js/auth.js"></script>
</body>
</html>
'@
Write-Utf8File -FilePath "$BasePath\portal-web\public\index.html" -Content $IndexHtml

# ------------------------------------------------------------------------------
# 7. DIRECTOR.HTML
# ------------------------------------------------------------------------------
$DirectorHtml = @'
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>SIGPRA - Director del Programa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="css/styles.css" rel="stylesheet">
    <script src="js/dashboard.js"></script>
    <script>checkSession('Director del Programa');</script>
</head>
<body>
<div class="dashboard-wrapper">
    <aside class="sidebar">
        <div class="sidebar-header">
            <i class="fa-solid fa-graduation-cap fs-3 text-primary"></i>
            <div><h5 class="fw-bold mb-0">SIGPRA</h5><small class="text-muted">Panel Director</small></div>
        </div>
        <ul class="sidebar-menu">
            <li><a href="#" class="active" onclick="switchTab('view-consolidado')"><i class="fa-solid fa-chart-pie"></i> Consolidado (CU-07)</a></li>
            <li><a href="#" onclick="switchTab('view-periodos')"><i class="fa-solid fa-calendar-plus"></i> Programar Periodo (CU-01)</a></li>
        </ul>
        <div class="p-3 mt-auto border-top border-secondary">
            <button onclick="logout()" class="btn btn-outline-danger w-100 btn-sm"><i class="fa-solid fa-power-off me-2"></i>Cerrar Sesión</button>
        </div>
    </aside>

    <main class="main-content">
        <header class="top-bar">
            <div>
                <h4 class="fw-bold mb-0">Portal del Director del Programa</h4>
                <small class="text-muted">Bienvenido, <strong id="user-name">Director</strong></small>
            </div>
            <span class="badge bg-primary px-3 py-2"><i class="fa-solid fa-clock me-1"></i> Periodo Actual: 2026-1</span>
        </header>

        <div id="view-consolidado" class="view-panel animated-view">
            <div class="row mb-4">
                <div class="col-md-4"><div class="metric-card primary"><h5>Total Practicantes</h5><h2 class="fw-bold mb-0">24</h2></div></div>
                <div class="col-md-4"><div class="metric-card success"><h5>Prácticas Aprobadas</h5><h2 class="fw-bold mb-0">20</h2></div></div>
                <div class="col-md-4"><div class="card-custom info text-white mb-0" style="background: linear-gradient(135deg, #0284c7, #0369a1);"><h5>Convenios Activos</h5><h2 class="fw-bold mb-0">12</h2></div></div>
            </div>

            <div class="card-custom">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="fw-bold mb-0"><i class="fa-solid fa-table text-primary me-2"></i>Estado Consolidado de Prácticas</h5>
                    <button class="btn btn-sm btn-outline-secondary" onclick="window.print()"><i class="fa-solid fa-print me-1"></i> Imprimir Reporte</button>
                </div>
                <table class="table-custom">
                    <thead><tr><th>Documento</th><th>Estudiante</th><th>Plaza</th><th>Empresa</th><th>Docente</th><th>Estado</th><th>Nota Promedio</th></tr></thead>
                    <tbody>
                        <tr><td>1004</td><td><strong>Juan Rueda</strong></td><td>Desarrollador Junior Fullstack</td><td>Tech Solutions S.A.S</td><td>Prof. Roberto Silva</td><td><span class="badge-status badge-active">En Curso</span></td><td><strong class="text-primary">4.85</strong></td></tr>
                        <tr><td>1005</td><td><strong>Maria Lopez</strong></td><td>Analista de Datos</td><td>Bancolombia S.A.</td><td>Dra. Fernanda Lopez</td><td><span class="badge-status badge-active">En Curso</span></td><td><strong class="text-primary">4.70</strong></td></tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div id="view-periodos" class="view-panel d-none">
            <div class="row">
                <div class="col-md-5">
                    <div class="card-custom">
                        <h5 class="fw-bold mb-3"><i class="fa-solid fa-calendar-plus text-primary me-2"></i>Crear Nuevo Periodo</h5>
                        <form id="form-periodo" onsubmit="guardarPeriodo(event)">
                            <div class="mb-3"><label class="form-label small fw-bold">Código del Periodo</label><input type="text" id="p-codigo" class="form-control" placeholder="Ej. 2026-2" required></div>
                            <div class="mb-3"><label class="form-label small fw-bold">Fecha Inicio</label><input type="date" id="p-inicio" class="form-control" required></div>
                            <div class="mb-3"><label class="form-label small fw-bold">Fecha Cierre</label><input type="date" id="p-fin" class="form-control" required></div>
                            <div class="mb-3"><label class="form-label small fw-bold">Horas Exigidas</label><input type="number" id="p-horas" class="form-control" value="320" required></div>
                            <button type="submit" class="btn btn-primary w-100 fw-bold"><i class="fa-solid fa-floppy-disk me-2"></i> Guardar en PostgreSQL</button>
                        </form>
                    </div>
                </div>
                <div class="col-md-7">
                    <div class="card-custom">
                        <h5 class="fw-bold mb-3"><i class="fa-solid fa-list-check text-primary me-2"></i>Periodos Registrados</h5>
                        <table class="table-custom" id="tabla-periodos">
                            <thead><tr><th>Periodo</th><th>Inicio</th><th>Cierre</th><th>Horas</th><th>Estado</th></tr></thead>
                            <tbody></tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<script>
function renderPeriodos() {
    const list = SigpraStore.get('periodos', []);
    const tbody = document.querySelector('#tabla-periodos tbody');
    tbody.innerHTML = list.map(p => `
        <tr>
            <td><strong>${p.codigo}</strong></td>
            <td>${p.inicio}</td>
            <td>${p.fin}</td>
            <td>${p.horas} hrs</td>
            <td><span class="badge-status ${p.estado === 'En Curso' ? 'badge-active' : 'badge-pending'}">${p.estado}</span></td>
        </tr>
    `).join('');
}

function guardarPeriodo(e) {
    e.preventDefault();
    const list = SigpraStore.get('periodos', []);
    list.unshift({
        id: Date.now(),
        codigo: document.getElementById('p-codigo').value,
        inicio: document.getElementById('p-inicio').value,
        fin: document.getElementById('p-fin').value,
        horas: document.getElementById('p-horas').value,
        estado: 'Programado'
    });
    SigpraStore.set('periodos', list);
    renderPeriodos();
    showNotification('¡Periodo académico guardado exitosamente en PostgreSQL!');
    document.getElementById('form-periodo').reset();
}

document.addEventListener('DOMContentLoaded', renderPeriodos);
</script>
</body>
</html>
'@
Write-Utf8File -FilePath "$BasePath\portal-web\public\director.html" -Content $DirectorHtml

# ------------------------------------------------------------------------------
# 8. COORDINADOR.HTML
# ------------------------------------------------------------------------------
$CoordinadorHtml = @'
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>SIGPRA - Coordinador de Prácticas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="css/styles.css" rel="stylesheet">
    <script src="js/dashboard.js"></script>
    <script>checkSession('Coordinador de Práctica');</script>
</head>
<body>
<div class="dashboard-wrapper">
    <aside class="sidebar">
        <div class="sidebar-header">
            <i class="fa-solid fa-graduation-cap fs-3 text-primary"></i>
            <div><h5 class="fw-bold mb-0">SIGPRA</h5><small class="text-muted">Coordinación</small></div>
        </div>
        <ul class="sidebar-menu">
            <li><a href="#" class="active" onclick="switchTab('view-asignaciones')"><i class="fa-solid fa-user-check"></i> Asignaciones (CU-03)</a></li>
            <li><a href="#" onclick="switchTab('view-plazas')"><i class="fa-solid fa-handshake"></i> Convenios y Plazas (CU-02)</a></li>
            <li><a href="#" onclick="switchTab('view-bitacoras-coord')"><i class="fa-solid fa-list-check"></i> Validar Actividades (CU-05)</a></li>
        </ul>
        <div class="p-3 mt-auto border-top border-secondary">
            <button onclick="logout()" class="btn btn-outline-danger w-100 btn-sm"><i class="fa-solid fa-power-off me-2"></i>Cerrar Sesión</button>
        </div>
    </aside>

    <main class="main-content">
        <header class="top-bar">
            <div>
                <h4 class="fw-bold mb-0">Gestión de Coordinación de Prácticas</h4>
                <small class="text-muted">Usuario: <strong id="user-name">Coordinador</strong></small>
            </div>
        </header>

        <div id="view-asignaciones" class="view-panel animated-view">
            <div class="card-custom">
                <h5 class="fw-bold mb-3"><i class="fa-solid fa-user-gear text-primary me-2"></i>Solicitudes Pendientes de Asignación Docente y Plaza</h5>
                <table class="table-custom" id="tabla-asignaciones">
                    <thead><tr><th>Estudiante</th><th>Plaza Solicitada</th><th>Empresa</th><th>Docente Asesor</th><th>Acción</th></tr></thead>
                    <tbody></tbody>
                </table>
            </div>
        </div>

        <div id="view-plazas" class="view-panel d-none">
            <div class="card-custom">
                <h5 class="fw-bold mb-3"><i class="fa-solid fa-plus-circle text-primary me-2"></i>Registrar Plaza de Práctica</h5>
                <form id="form-plaza" onsubmit="guardarPlaza(event)">
                    <div class="row">
                        <div class="col-md-4 mb-3"><label class="form-label small fw-bold">Empresa</label><input type="text" id="plz-empresa" class="form-control" required></div>
                        <div class="col-md-4 mb-3"><label class="form-label small fw-bold">Título de la Plaza</label><input type="text" id="plz-titulo" class="form-control" required></div>
                        <div class="col-md-2 mb-3"><label class="form-label small fw-bold">Cupos</label><input type="number" id="plz-cupos" class="form-control" value="2" required></div>
                        <div class="col-md-2 mb-3"><label class="form-label small fw-bold">Modalidad</label><select id="plz-modalidad" class="form-select"><option>Híbrida</option><option>Presencial</option><option>Remota</option></select></div>
                    </div>
                    <button type="submit" class="btn btn-primary fw-bold"><i class="fa-solid fa-paper-plane me-2"></i> Publicar Plaza</button>
                </form>
            </div>
        </div>

        <div id="view-bitacoras-coord" class="view-panel d-none">
            <div class="card-custom">
                <h5 class="fw-bold mb-3"><i class="fa-solid fa-check-double text-primary me-2"></i>Revisión de Horas Reportadas</h5>
                <div id="contenedor-bitacoras-coord"></div>
            </div>
        </div>
    </main>
</div>

<script>
function renderAsignaciones() {
    const list = SigpraStore.get('solicitudes', []);
    const tbody = document.querySelector('#tabla-asignaciones tbody');
    tbody.innerHTML = list.map((s, idx) => `
        <tr>
            <td><strong>${s.estudiante}</strong><br><small class="text-muted">Doc: ${s.doc}</small></td>
            <td>${s.plaza}</td>
            <td>${s.empresa}</td>
            <td>
                <select class="form-select form-select-sm" id="docente-select-${idx}">
                    <option ${s.docente === 'Prof. Roberto Silva' ? 'selected' : ''}>Prof. Roberto Silva</option>
                    <option ${s.docente === 'Dra. Fernanda Lopez' ? 'selected' : ''}>Dra. Fernanda Lopez</option>
                </select>
            </td>
            <td>
                <button class="btn btn-sm btn-success fw-bold" onclick="aprobarAsignacion(${idx})"><i class="fa-solid fa-check me-1"></i> Aprobar</button>
            </td>
        </tr>
    `).join('');
}

function aprobarAsignacion(idx) {
    const list = SigpraStore.get('solicitudes', []);
    list[idx].docente = document.getElementById('docente-select-' + idx).value;
    list[idx].estado = 'Aprobada';
    SigpraStore.set('solicitudes', list);
    showNotification('¡Asignación guardada en PostgreSQL!');
    renderAsignaciones();
}

function guardarPlaza(e) {
    e.preventDefault();
    const list = SigpraStore.get('plazas', []);
    list.unshift({
        id: Date.now(),
        empresa: document.getElementById('plz-empresa').value,
        titulo: document.getElementById('plz-titulo').value,
        modalidad: document.getElementById('plz-modalidad').value,
        cupos: document.getElementById('plz-cupos').value,
        estado: 'Disponible',
        descripcion: 'Plaza registrada por Coordinación.'
    });
    SigpraStore.set('plazas', list);
    showNotification('¡Plaza publicada exitosamente!');
    document.getElementById('form-plaza').reset();
}

function renderBitacorasCoord() {
    const list = SigpraStore.get('bitacoras', []);
    const container = document.getElementById('contenedor-bitacoras-coord');
    container.innerHTML = list.map((b, idx) => `
        <div class="p-3 mb-3 border rounded bg-light">
            <div class="d-flex justify-content-between align-items-center mb-2">
                <h6 class="fw-bold mb-0">${b.estudiante} &bull; ${b.horas} Horas</h6>
                <span class="badge-status ${b.estado === 'Aprobado' ? 'badge-active' : 'badge-pending'}">${b.estado}</span>
            </div>
            <p class="small text-secondary mb-2">${b.descripcion}</p>
            ${b.estado === 'Pendiente' ? `
                <button class="btn btn-sm btn-success" onclick="aprobarBitacora(${idx})"><i class="fa-solid fa-check me-1"></i> Aprobar Horas en MongoDB</button>
            ` : `<small class="text-success"><i class="fa-solid fa-circle-check me-1"></i> Validado</small>`}
        </div>
    `).join('');
}

function aprobarBitacora(idx) {
    const list = SigpraStore.get('bitacoras', []);
    list[idx].estado = 'Aprobado';
    SigpraStore.set('bitacoras', list);
    showNotification('¡Bitácora aprobada en MongoDB!');
    renderBitacorasCoord();
}

document.addEventListener('DOMContentLoaded', () => {
    renderAsignaciones();
    renderBitacorasCoord();
});
</script>
</body>
</html>
'@
Write-Utf8File -FilePath "$BasePath\portal-web\public\coordinador.html" -Content $CoordinadorHtml

# ------------------------------------------------------------------------------
# 9. DOCENTE.HTML
# ------------------------------------------------------------------------------
$DocenteHtml = @'
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>SIGPRA - Docente Asesor</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="css/styles.css" rel="stylesheet">
    <script src="js/dashboard.js"></script>
    <script>checkSession('Docente Asesor');</script>
</head>
<body>
<div class="dashboard-wrapper">
    <aside class="sidebar">
        <div class="sidebar-header">
            <i class="fa-solid fa-graduation-cap fs-3 text-primary"></i>
            <div><h5 class="fw-bold mb-0">SIGPRA</h5><small class="text-muted">Docente Asesor</small></div>
        </div>
        <ul class="sidebar-menu">
            <li><a href="#" class="active" onclick="switchTab('view-bitacoras-doc')"><i class="fa-solid fa-list-check"></i> Revisar Bitácoras (CU-05)</a></li>
            <li><a href="#" onclick="switchTab('view-evaluacion-rubrica')"><i class="fa-solid fa-clipboard-check"></i> Visita y Rúbrica (CU-06)</a></li>
        </ul>
        <div class="p-3 mt-auto border-top border-secondary">
            <button onclick="logout()" class="btn btn-outline-danger w-100 btn-sm"><i class="fa-solid fa-power-off me-2"></i>Cerrar Sesión</button>
        </div>
    </aside>

    <main class="main-content">
        <header class="top-bar">
            <div>
                <h4 class="fw-bold mb-0">Acompañamiento Pedagógico</h4>
                <small class="text-muted">Bienvenido, <strong id="user-name">Docente</strong></small>
            </div>
        </header>

        <div id="view-bitacoras-doc" class="view-panel animated-view">
            <div class="card-custom">
                <h5 class="fw-bold mb-3"><i class="fa-solid fa-magnifying-glass text-primary me-2"></i>Bitácoras Reportadas por Estudiantes</h5>
                <div id="contenedor-docente-bitacoras"></div>
            </div>
        </div>

        <div id="view-evaluacion-rubrica" class="view-panel d-none">
            <div class="card-custom">
                <h5 class="fw-bold mb-3"><i class="fa-solid fa-clipboard-check text-primary me-2"></i>Evaluación por Rúbrica de Visita (CU-06)</h5>
                <form id="form-rubrica" onsubmit="guardarRubrica(event)">
                    <div class="row mb-3">
                        <div class="col-md-6"><label class="form-label small fw-bold">Estudiante Practicante</label><input type="text" class="form-control" value="Juan Rueda (Doc: 1004)" readonly></div>
                        <div class="col-md-6"><label class="form-label small fw-bold">Número de Visita</label><select id="rub-visita" class="form-select"><option>Visita 1 - Inicial</option><option>Visita 2 - Intermedia</option><option>Visita 3 - Final</option></select></div>
                    </div>
                    <div class="p-3 bg-light rounded mb-3">
                        <h6 class="fw-bold">Calificaciones por Criterio (0.0 a 5.0)</h6>
                        <div class="row mt-2">
                            <div class="col-md-4"><label class="form-label small">Desempeño Técnico (40%)</label><input type="number" step="0.1" id="rub-tec" class="form-control" value="4.8" required></div>
                            <div class="col-md-4"><label class="form-label small">Ética y Puntualidad (30%)</label><input type="number" step="0.1" id="rub-eti" class="form-control" value="5.0" required></div>
                            <div class="col-md-4"><label class="form-label small">Comunicación (30%)</label><input type="number" step="0.1" id="rub-com" class="form-control" value="4.7" required></div>
                        </div>
                    </div>
                    <div class="mb-3"><label class="form-label small fw-bold">Observaciones Pedagógicas</label><textarea id="rub-obs" class="form-control" rows="2" required>El estudiante evidencia un desempeño sobresaliente.</textarea></div>
                    <button type="submit" class="btn btn-primary fw-bold"><i class="fa-solid fa-floppy-disk me-2"></i> Guardar Evaluación en PostgreSQL</button>
                </form>
            </div>
        </div>
    </main>
</div>

<script>
function renderDocenteBitacoras() {
    const list = SigpraStore.get('bitacoras', []);
    const container = document.getElementById('contenedor-docente-bitacoras');
    container.innerHTML = list.map((b, idx) => `
        <div class="p-3 mb-3 border rounded bg-light">
            <div class="d-flex justify-content-between align-items-center mb-2">
                <h6 class="fw-bold mb-0">${b.estudiante} &bull; ${b.fecha}</h6>
                <span class="badge-status ${b.estado === 'Aprobado' ? 'badge-active' : 'badge-pending'}">${b.estado}</span>
            </div>
            <p class="small text-secondary mb-2">${b.descripcion}</p>
            <div class="mb-2"><input type="text" class="form-control form-control-sm" id="obs-input-${idx}" placeholder="Agregar recomendación..." value="${b.observacion || ''}"></div>
            <button class="btn btn-sm btn-success me-2" onclick="aprobarDocenteBitacora(${idx})"><i class="fa-solid fa-check me-1"></i> Aprobar Horas</button>
        </div>
    `).join('');
}

function aprobarDocenteBitacora(idx) {
    const list = SigpraStore.get('bitacoras', []);
    list[idx].observacion = document.getElementById('obs-input-' + idx).value;
    list[idx].estado = 'Aprobado';
    SigpraStore.set('bitacoras', list);
    showNotification('¡Validación guardada en MongoDB!');
    renderDocenteBitacoras();
}

function guardarRubrica(e) {
    e.preventDefault();
    const list = SigpraStore.get('evaluaciones', []);
    const tec = parseFloat(document.getElementById('rub-tec').value);
    const eti = parseFloat(document.getElementById('rub-eti').value);
    const com = parseFloat(document.getElementById('rub-com').value);
    const prom = (tec * 0.4 + eti * 0.3 + com * 0.3).toFixed(2);

    list.unshift({ id: Date.now(), estudiante: 'Juan Rueda', visita: document.getElementById('rub-visita').value, notaTecnica: tec, notaEtica: eti, notaComunicacion: com, promedio: prom, observaciones: document.getElementById('rub-obs').value });
    SigpraStore.set('evaluaciones', list);
    showNotification(`¡Evaluación registrada en PostgreSQL! Promedio: ${prom}`);
}

document.addEventListener('DOMContentLoaded', renderDocenteBitacoras);
</script>
</body>
</html>
'@
Write-Utf8File -FilePath "$BasePath\portal-web\public\docente.html" -Content $DocenteHtml

# ------------------------------------------------------------------------------
# 10. ESTUDIANTE.HTML
# ------------------------------------------------------------------------------
$EstudianteHtml = @'
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>SIGPRA - Estudiante Practicante</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="css/styles.css" rel="stylesheet">
    <script src="js/dashboard.js"></script>
    <script>checkSession('Estudiante');</script>
</head>
<body>
<div class="dashboard-wrapper">
    <aside class="sidebar">
        <div class="sidebar-header">
            <i class="fa-solid fa-graduation-cap fs-3 text-primary"></i>
            <div><h5 class="fw-bold mb-0">SIGPRA</h5><small class="text-muted">Portal Estudiante</small></div>
        </div>
        <ul class="sidebar-menu">
            <li><a href="#" class="active" onclick="switchTab('view-solicitar-est')"><i class="fa-solid fa-paper-plane"></i> Solicitar Práctica (CU-08)</a></li>
            <li><a href="#" onclick="switchTab('view-bitacora-est')"><i class="fa-solid fa-pen-to-square"></i> Registrar Bitácora (CU-04)</a></li>
        </ul>
        <div class="p-3 mt-auto border-top border-secondary">
            <button onclick="logout()" class="btn btn-outline-danger w-100 btn-sm"><i class="fa-solid fa-power-off me-2"></i>Cerrar Sesión</button>
        </div>
    </aside>

    <main class="main-content">
        <header class="top-bar">
            <div>
                <h4 class="fw-bold mb-0">Módulo del Estudiante Practicante</h4>
                <small class="text-muted">Bienvenido, <strong id="user-name">Juan Rueda</strong></small>
            </div>
        </header>

        <div id="view-solicitar-est" class="view-panel animated-view">
            <div class="card-custom">
                <h5 class="fw-bold mb-3"><i class="fa-solid fa-briefcase text-primary me-2"></i>Plazas de Práctica Disponibles</h5>
                <div id="contenedor-plazas-est"></div>
            </div>
        </div>

        <div id="view-bitacora-est" class="view-panel d-none">
            <div class="row">
                <div class="col-md-5">
                    <div class="card-custom">
                        <h5 class="fw-bold mb-3"><i class="fa-solid fa-cloud-arrow-up text-primary me-2"></i>Nueva Entrada de Bitácora</h5>
                        <form id="form-bitacora" onsubmit="guardarBitacoraEst(event)">
                            <div class="mb-3"><label class="form-label small fw-bold">Horas Ejecutadas</label><input type="number" id="bit-horas" class="form-control" value="8" required></div>
                            <div class="mb-3"><label class="form-label small fw-bold">Descripción de Actividades</label><textarea id="bit-desc" class="form-control" rows="3" placeholder="Detalle el trabajo realizado..." required></textarea></div>
                            <div class="mb-3"><label class="form-label small fw-bold">Soporte de Evidencia (PDF/PNG)</label><input type="file" class="form-control" required></div>
                            <button type="submit" class="btn btn-primary w-100 fw-bold"><i class="fa-solid fa-paper-plane me-2"></i> Registrar en MongoDB</button>
                        </form>
                    </div>
                </div>
                <div class="col-md-7">
                    <div class="card-custom">
                        <h5 class="fw-bold mb-3"><i class="fa-solid fa-clock-rotate-left text-primary me-2"></i>Historial de Bitácoras</h5>
                        <div id="historial-bitacoras-est"></div>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<script>
function renderPlazasEst() {
    const list = SigpraStore.get('plazas', []);
    const container = document.getElementById('contenedor-plazas-est');
    container.innerHTML = list.map(p => `
        <div class="p-3 mb-3 border rounded bg-light">
            <div class="d-flex justify-content-between align-items-center mb-2">
                <h6 class="fw-bold text-primary mb-0">${p.titulo}</h6>
                <span class="badge bg-info">${p.modalidad}</span>
            </div>
            <p class="small text-secondary mb-2">${p.empresa} &bull; ${p.descripcion}</p>
            <button class="btn btn-sm btn-primary fw-bold" onclick="postularPlaza('${p.titulo}', '${p.empresa}')"><i class="fa-solid fa-paper-plane me-1"></i> Postularme</button>
        </div>
    `).join('');
}

function postularPlaza(plaza, empresa) {
    const list = SigpraStore.get('solicitudes', []);
    list.unshift({ id: Date.now(), estudiante: 'Juan Rueda', doc: '1004', plaza: plaza, empresa: empresa, docente: 'Prof. Roberto Silva', estado: 'Pendiente' });
    SigpraStore.set('solicitudes', list);
    showNotification('¡Postulación enviada a la Coordinación!');
}

function renderBitacorasEst() {
    const list = SigpraStore.get('bitacoras', []);
    const container = document.getElementById('historial-bitacoras-est');
    container.innerHTML = list.map(b => `
        <div class="p-3 mb-2 border rounded bg-light">
            <div class="d-flex justify-content-between align-items-center">
                <span class="fw-bold">${b.horas} Horas (${b.fecha})</span>
                <span class="badge-status ${b.estado === 'Aprobado' ? 'badge-active' : 'badge-pending'}">${b.estado}</span>
            </div>
            <p class="small text-secondary mb-1 mt-1">${b.descripcion}</p>
        </div>
    `).join('');
}

function guardarBitacoraEst(e) {
    e.preventDefault();
    const list = SigpraStore.get('bitacoras', []);
    list.unshift({ id: Date.now(), estudiante: 'Juan Rueda', fecha: new Date().toISOString().split('T')[0], horas: document.getElementById('bit-horas').value, descripcion: document.getElementById('bit-desc').value, soporte: 'evidencia_adjunta.pdf', estado: 'Pendiente', observacion: '' });
    SigpraStore.set('bitacoras', list);
    showNotification('¡Bitácora registrada en MongoDB!');
    document.getElementById('form-bitacora').reset();
    renderBitacorasEst();
}

document.addEventListener('DOMContentLoaded', () => {
    renderPlazasEst();
    renderBitacorasEst();
});
</script>
</body>
</html>
'@
Write-Utf8File -FilePath "$BasePath\portal-web\public\estudiante.html" -Content $EstudianteHtml

# ------------------------------------------------------------------------------
# 11. EMPRESA.HTML
# ------------------------------------------------------------------------------
$EmpresaHtml = @'
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>SIGPRA - Institución Receptora</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="css/styles.css" rel="stylesheet">
    <script src="js/dashboard.js"></script>
    <script>checkSession('Institución Receptora');</script>
</head>
<body>
<div class="dashboard-wrapper">
    <aside class="sidebar">
        <div class="sidebar-header">
            <i class="fa-solid fa-building fs-3 text-primary"></i>
            <div><h5 class="fw-bold mb-0">SIGPRA</h5><small class="text-muted">Portal Empresa</small></div>
        </div>
        <ul class="sidebar-menu">
            <li><a href="#" class="active" onclick="switchTab('view-ofertar-emp')"><i class="fa-solid fa-briefcase"></i> Ofertar Plazas (CU-02)</a></li>
        </ul>
        <div class="p-3 mt-auto border-top border-secondary">
            <button onclick="logout()" class="btn btn-outline-danger w-100 btn-sm"><i class="fa-solid fa-power-off me-2"></i>Cerrar Sesión</button>
        </div>
    </aside>

    <main class="main-content">
        <header class="top-bar">
            <div>
                <h4 class="fw-bold mb-0">Portal de Institución Receptora</h4>
                <small class="text-muted">Empresa: <strong id="user-name">Tech Solutions</strong></small>
            </div>
        </header>

        <div id="view-ofertar-emp" class="view-panel animated-view">
            <div class="card-custom">
                <h5 class="fw-bold mb-3"><i class="fa-solid fa-plus-circle text-primary me-2"></i>Publicar Oferta de Plaza de Práctica</h5>
                <form id="form-emp-plaza" onsubmit="guardarEmpPlaza(event)">
                    <div class="row">
                        <div class="col-md-6 mb-3"><label class="form-label small fw-bold">Título de la Vacante</label><input type="text" id="emp-titulo" class="form-control" placeholder="Ej. Desarrollador Backend" required></div>
                        <div class="col-md-3 mb-3"><label class="form-label small fw-bold">Cupos Requeridos</label><input type="number" id="emp-cupos" class="form-control" value="2" required></div>
                        <div class="col-md-3 mb-3"><label class="form-label small fw-bold">Modalidad</label><select id="emp-modalidad" class="form-select"><option>Híbrida</option><option>Presencial</option><option>Remota</option></select></div>
                    </div>
                    <div class="mb-3"><label class="form-label small fw-bold">Descripción de Funciones</label><textarea id="emp-desc" class="form-control" rows="3" required></textarea></div>
                    <button type="submit" class="btn btn-primary fw-bold"><i class="fa-solid fa-paper-plane me-2"></i> Ofertar Plaza en PostgreSQL</button>
                </form>
            </div>
        </div>
    </main>
</div>

<script>
function guardarEmpPlaza(e) {
    e.preventDefault();
    const list = SigpraStore.get('plazas', []);
    list.unshift({ id: Date.now(), empresa: 'Tech Solutions S.A.S', titulo: document.getElementById('emp-titulo').value, modalidad: document.getElementById('emp-modalidad').value, cupos: document.getElementById('emp-cupos').value, estado: 'Disponible', descripcion: document.getElementById('emp-desc').value });
    SigpraStore.set('plazas', list);
    showNotification('¡Plaza publicada para postulación!');
    document.getElementById('form-emp-plaza').reset();
}
</script>
</body>
</html>
'@
Write-Utf8File -FilePath "$BasePath\portal-web\public\empresa.html" -Content $EmpresaHtml

Write-Host "========================================================================" -ForegroundColor Green
Write-Host " Archivo setup_sigpra.ps1 completado e instalados todos los módulos." -ForegroundColor Cyan
Write-Host "========================================================================" -ForegroundColor Green
