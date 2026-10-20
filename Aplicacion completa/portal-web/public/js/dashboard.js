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
        { id: 1, empresa: 'Tech Solutions S.A.S', titulo: 'Desarrollador Junior Fullstack', modalidad: 'HÃ­brida', cupos: 2, estado: 'Disponible', descripcion: 'Desarrollo de microservicios en Node.js y React con PostgreSQL y MongoDB.' },
        { id: 2, empresa: 'Bancolombia S.A.', titulo: 'Analista de Ciberseguridad y Datos', modalidad: 'Presencial', cupos: 3, estado: 'Disponible', descripcion: 'AnÃ¡lisis de vulnerabilidades, auditorÃ­a de logs y gestiÃ³n de seguridad informÃ¡tica.' }
    ]);
}

if (!localStorage.getItem('sigpra_solicitudes')) {
    SigpraStore.set('solicitudes', [
        { id: 1, estudiante: 'Juan Rueda', doc: '1004', plaza: 'Desarrollador Junior Fullstack', empresa: 'Tech Solutions S.A.S', docente: 'Prof. Roberto Silva', estado: 'Pendiente' }
    ]);
}

if (!localStorage.getItem('sigpra_bitacoras')) {
    SigpraStore.set('bitacoras', [
        { id: 'act1', estudiante: 'Juan Rueda', fecha: '2026-02-15', horas: 8, descripcion: 'AnÃ¡lisis de requerimientos y diseÃ±o de modelos relacionales en PostgreSQL y NoSQL en MongoDB.', soporte: 'diagrama_arquitectura.pdf', estado: 'Aprobado', observacion: 'Excelente diseÃ±o de esquemas.' },
        { id: 'act2', estudiante: 'Juan Rueda', fecha: '2026-02-22', horas: 8, descripcion: 'Desarrollo de interfaces grÃ¡ficas interactivas con CSS3, animaciones y mÃ³dulos JS por rol.', soporte: 'pantallazos_ui.png', estado: 'Pendiente', observacion: '' }
    ]);
}

if (!localStorage.getItem('sigpra_evaluaciones')) {
    SigpraStore.set('evaluaciones', [
        { id: 1, estudiante: 'Juan Rueda', visita: 'Visita 1 - Inicial', notaTecnica: 4.8, notaEtica: 5.0, notaComunicacion: 4.7, promedio: 4.83, observaciones: 'Excelente actitud y competencias tÃ©cnicas.' }
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