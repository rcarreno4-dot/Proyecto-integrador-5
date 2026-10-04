const STORAGE_KEY = "sigpra_mvp_data_v1";
const SESSION_KEY = "sigpra_mvp_session_v1";

const seedData = {
  users: [
    { id: 1, name: "Rafael Carreno", email: "coordinador@udi.edu.co", password: "123456", role: "COORDINADOR", initials: "RC" },
    { id: 2, name: "Laura Gomez", email: "estudiante@udi.edu.co", password: "123456", role: "ESTUDIANTE", initials: "LG", code: "UDI-2026-001", semester: 8 },
    { id: 3, name: "Martha Rodriguez", email: "docente@udi.edu.co", password: "123456", role: "DOCENTE_ASESOR", initials: "MR" },
    { id: 4, name: "Director Programa", email: "director@udi.edu.co", password: "123456", role: "DIRECTOR", initials: "DP" },
    { id: 5, name: "Miguel Rios", email: "miguel@udi.edu.co", password: "123456", role: "ESTUDIANTE", initials: "MR", code: "UDI-2026-002", semester: 8 },
    { id: 6, name: "Carlos Mendoza", email: "docente2@udi.edu.co", password: "123456", role: "DOCENTE_ASESOR", initials: "CM" }
  ],
  periods: [
    {
      id: 1,
      program: "Licenciatura en Educacion Infantil",
      level: "VIII semestre",
      year: 2026,
      semester: 2,
      start: "2026-08-17",
      end: "2026-11-28",
      reportLimit: "2026-11-30",
      minHours: 320,
      status: "PUBLICADO",
      rubric: [
        { name: "Cumplimiento de horas", weight: 35 },
        { name: "Calidad del soporte", weight: 25 },
        { name: "Desempeno institucional", weight: 25 },
        { name: "Informe final", weight: 15 }
      ]
    }
  ],
  institutions: [
    {
      id: 1,
      name: "Centro Educativo La Esperanza",
      nit: "900123456-1",
      contact: "Sandra Perez",
      phone: "607 555 1234",
      email: "contacto@laesperanza.edu.co",
      active: true
    },
    {
      id: 2,
      name: "Fundacion Aprender",
      nit: "901222333-4",
      contact: "Paula Vargas",
      phone: "607 555 7788",
      email: "practicas@aprender.org",
      active: true
    }
  ],
  agreements: [
    { id: 1, institutionId: 1, number: "CV-2026-013", start: "2026-01-15", end: "2027-06-30", status: "VIGENTE", document: "convenio-la-esperanza.pdf" },
    { id: 2, institutionId: 2, number: "CV-2025-044", start: "2025-03-01", end: "2026-12-15", status: "VIGENTE", document: "convenio-aprender.pdf" }
  ],
  places: [
    { id: 1, agreementId: 1, level: "Practica pedagogica VIII", shift: "MANANA", offered: 12, occupied: 1, tutor: "Sandra Perez", status: "DISPONIBLE" },
    { id: 2, agreementId: 2, level: "Practica pedagogica VIII", shift: "TARDE", offered: 6, occupied: 1, tutor: "Paula Vargas", status: "DISPONIBLE" }
  ],
  assignments: [
    { id: 1, studentId: 2, teacherId: 3, placeId: 1, periodId: 1, date: "2026-08-20", status: "ACTIVA", approvedHours: 96 },
    { id: 2, studentId: 5, teacherId: 6, placeId: 2, periodId: 1, date: "2026-08-21", status: "ACTIVA", approvedHours: 42 }
  ],
  activities: [
    {
      id: 1,
      assignmentId: 1,
      date: "2026-09-13",
      type: "Acompanamiento pedagogico",
      description: "Apoyo en planeacion y ejecucion de actividad de lectura guiada con grupo de transicion.",
      hours: 4,
      status: "PENDIENTE",
      observation: ""
    },
    {
      id: 2,
      assignmentId: 1,
      date: "2026-09-06",
      type: "Planeacion de clase",
      description: "Preparacion de material didactico y guia de clase.",
      hours: 6,
      status: "APROBADA",
      observation: "Soporte completo."
    },
    {
      id: 3,
      assignmentId: 2,
      date: "2026-09-10",
      type: "Refuerzo pedagogico",
      description: "Acompanamiento a estudiantes con dificultad lectora.",
      hours: 3,
      status: "DEVUELTA",
      observation: "Adjuntar soporte firmado por la institucion."
    }
  ],
  evidences: [
    { id: 1, activityId: 1, fileName: "evidencia_foto.pdf", type: "PDF", url: "storage/sigpra/2026-2/asignacion-1/actividad-1/evidencia_foto.pdf", status: "CARGADA", mongoId: "ev-demo-001" },
    { id: 2, activityId: 2, fileName: "planeacion_clase.docx", type: "DOCX", url: "storage/sigpra/2026-2/asignacion-1/actividad-2/planeacion_clase.docx", status: "VALIDADA", mongoId: "ev-demo-002" }
  ],
  visits: [
    { id: 1, assignmentId: 1, teacherId: 3, date: "2026-09-20", mode: "PRESENCIAL", attendance: true, observation: "Buen manejo del grupo. Se recomienda fortalecer el cierre de la actividad.", score: 4.4, status: "REGISTRADA" }
  ],
  audit: [
    { id: 1, date: "2026-09-13 10:00", user: "Sistema", event: "Carga inicial de datos demo", entity: "APP" }
  ]
};

let data = loadData();
let session = loadSession();
let currentView = "dashboard";
let reportFilters = { periodId: "", program: "", institutionId: "", teacherId: "" };

const app = document.querySelector("#app");

function loadData() {
  const saved = localStorage.getItem(STORAGE_KEY);
  if (!saved) return structuredClone(seedData);
  try {
    const stored = JSON.parse(saved);
    if (Array.isArray(stored.users)) {
      let migrated = false;
      stored.users = stored.users.map((user) => {
        if (typeof user.email === "string" && user.email.endsWith("@sigpra.edu.co")) {
          migrated = true;
          return { ...user, email: user.email.replace(/@sigpra\.edu\.co$/, "@udi.edu.co") };
        }
        return user;
      });
      if (migrated) localStorage.setItem(STORAGE_KEY, JSON.stringify(stored));
    }
    return stored;
  } catch {
    return structuredClone(seedData);
  }
}

function saveData() {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(data));
}

function loadSession() {
  const saved = localStorage.getItem(SESSION_KEY);
  if (!saved) return null;
  try {
    return JSON.parse(saved);
  } catch {
    return null;
  }
}

function saveSession(user) {
  session = user ? { userId: user.id } : null;
  if (session) localStorage.setItem(SESSION_KEY, JSON.stringify(session));
  else localStorage.removeItem(SESSION_KEY);
}

function currentUser() {
  return data.users.find((user) => user.id === session?.userId) || null;
}

function nextId(collection) {
  return Math.max(0, ...collection.map((item) => Number(item.id))) + 1;
}

function escapeHtml(value) {
  return String(value ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function moneyDate(value) {
  if (!value) return "Sin fecha";
  return new Date(`${value}T00:00:00`).toLocaleDateString("es-CO", { year: "numeric", month: "short", day: "2-digit" });
}

function toast(message) {
  document.querySelector(".toast")?.remove();
  const node = document.createElement("div");
  node.className = "toast";
  node.textContent = message;
  document.body.appendChild(node);
  setTimeout(() => node.remove(), 3200);
}

function audit(event, entity) {
  const user = currentUser();
  data.audit.unshift({
    id: nextId(data.audit),
    date: new Date().toLocaleString("es-CO"),
    user: user?.name || "Sistema",
    event,
    entity
  });
}

function roleLabel(role) {
  return {
    COORDINADOR: "Coordinador de practicas",
    ESTUDIANTE: "Estudiante practicante",
    DOCENTE_ASESOR: "Docente asesor",
    DIRECTOR: "Director de programa"
  }[role] || role;
}

function statusTag(status) {
  const ok = ["APROBADA", "PUBLICADO", "VIGENTE", "ACTIVA", "VALIDADA", "REGISTRADA"];
  const warn = ["PENDIENTE", "CONFIGURACION", "DEVUELTA", "BORRADOR"];
  const bad = ["RECHAZADA", "VENCIDO", "CANCELADA", "INACTIVA"];
  const cls = ok.includes(status) ? "ok" : warn.includes(status) ? "warn" : bad.includes(status) ? "bad" : "";
  return `<span class="tag ${cls}">${escapeHtml(status)}</span>`;
}

function agreementStatus(agreement) {
  const today = new Date().toISOString().slice(0, 10);
  return agreement.status === "VIGENTE" && agreement.document && agreement.start <= today && agreement.end >= today
    ? "VIGENTE"
    : "INACTIVA";
}

function placeStatus(place, agreement) {
  return agreement && agreementStatus(agreement) === "VIGENTE" && place.occupied < place.offered
    ? "DISPONIBLE"
    : "INACTIVA";
}

function assignmentPlaceLabel(place) {
  const agreement = data.agreements.find((item) => item.id === place.agreementId);
  const institution = data.institutions.find((item) => item.id === agreement?.institutionId);
  return `${institution?.name || "Institucion"} - ${place.level} (${place.shift})`;
}

function studentUsers() {
  return data.users.filter((user) => user.role === "ESTUDIANTE");
}

function teacherUsers() {
  return data.users.filter((user) => user.role === "DOCENTE_ASESOR");
}

function assignmentDetails(assignment) {
  const student = data.users.find((user) => user.id === assignment.studentId);
  const teacher = data.users.find((user) => user.id === assignment.teacherId);
  const place = data.places.find((item) => item.id === assignment.placeId);
  const agreement = data.agreements.find((item) => item.id === place?.agreementId);
  const institution = data.institutions.find((item) => item.id === agreement?.institutionId);
  const period = data.periods.find((item) => item.id === assignment.periodId);
  return { student, teacher, place, agreement, institution, period };
}

function approvedHours(assignmentId) {
  return data.activities
    .filter((activity) => activity.assignmentId === assignmentId && activity.status === "APROBADA")
    .reduce((sum, activity) => sum + Number(activity.hours), 0);
}

function pendingActivitiesForTeacher(teacherId) {
  const teacherAssignments = data.assignments.filter((assignment) => assignment.teacherId === teacherId).map((assignment) => assignment.id);
  return data.activities.filter((activity) => teacherAssignments.includes(activity.assignmentId) && activity.status === "PENDIENTE");
}

function render() {
  const user = currentUser();
  if (!user) {
    renderLogin();
    return;
  }

  const nav = navForRole(user.role);
  if (!nav.some((item) => item.id === currentView)) currentView = nav[0].id;

  app.innerHTML = `
    <div class="shell">
      <aside class="sidebar">
        <div class="brand">
          <strong>SIGPRA</strong>
          <span>Gestion de practicas academicas</span>
        </div>
        <div class="role-switch">
          <label for="role-switch">Ver como</label>
          <select id="role-switch">
            ${[
              ["COORDINADOR", "Coordinador de Practicas"],
              ["ESTUDIANTE", "Estudiante"],
              ["DOCENTE_ASESOR", "Docente Asesor"],
              ["DIRECTOR", "Director del programa"]
            ].map(([role, label]) => `<option value="${role}" ${user.role === role ? "selected" : ""}>${label}</option>`).join("")}
          </select>
        </div>
        <nav class="nav" aria-label="Navegacion de ${escapeHtml(roleLabel(user.role))}">
          ${renderNavigation(nav)}
        </nav>
        <div class="sidebar-footer">
          <strong>${escapeHtml(user.name)}</strong>
          <p class="muted">${escapeHtml(roleLabel(user.role))}</p>
          <button class="button" data-action="logout">Cerrar sesion</button>
        </div>
      </aside>
      <main class="content">
        <header class="topbar">
          <div>
            <h1>${escapeHtml(viewTitle(currentView, user.role))}</h1>
            <p class="subtitle">${escapeHtml(viewSubtitle(currentView, user.role))}</p>
          </div>
          <div class="userbox">
            <div>
              <strong>${escapeHtml(user.name)}</strong>
              <div class="muted">${escapeHtml(user.email)}</div>
            </div>
            <span class="avatar">${escapeHtml(user.initials)}</span>
          </div>
        </header>
        ${renderView(user)}
      </main>
    </div>
  `;
}

function navForRole(role) {
  const shared = [{ id: "dashboard", label: "Inicio", group: "Principal", description: "Resumen de tus tareas y accesos frecuentes." }];
  const map = {
    COORDINADOR: [
      ...shared,
      { id: "periods", label: "Periodos de practica", group: "Preparar practicas", description: "Configura fechas, horas y criterios para cada periodo." },
      { id: "agreements", label: "Convenios y plazas", group: "Preparar practicas", description: "Organiza instituciones, convenios y cupos disponibles." },
      { id: "assignments", label: "Asignaciones", group: "Seguimiento", description: "Consulta y organiza las practicas de los estudiantes." },
      { id: "reports", label: "Consolidado", group: "Seguimiento", description: "Filtra el avance y consulta resultados de las practicas." },
      { id: "audit", label: "Auditoria", group: "Seguimiento", description: "Revisa el historial de acciones registradas." }
    ],
    ESTUDIANTE: [
      ...shared,
      { id: "student-progress", label: "Mi avance", group: "Mi practica", description: "Consulta tus horas aprobadas y el estado de tu practica." },
      { id: "student-activities", label: "Mi bitacora", group: "Mi practica", description: "Registra el trabajo realizado y reporta tus horas." },
      { id: "student-evidence", label: "Evidencias", group: "Mi practica", description: "Adjunta y consulta los soportes de tus actividades." },
      { id: "student-validations", label: "Validaciones", group: "Mi practica", description: "Consulta las respuestas y observaciones de tu docente." }
    ],
    DOCENTE_ASESOR: [
      ...shared,
      { id: "teacher-validations", label: "Validar actividades", group: "Seguimiento", description: "Revisa soportes y responde a los reportes pendientes." },
      { id: "teacher-visits", label: "Visitas y evaluacion", group: "Seguimiento", description: "Registra acompanamientos y evaluaciones." }
    ],
    DIRECTOR: [
      ...shared,
      { id: "reports", label: "Consolidado", group: "Consulta", description: "Consulta el avance general de las practicas." },
      { id: "audit", label: "Auditoria", group: "Consulta", description: "Revisa las acciones registradas en el sistema." }
    ]
  };
  return map[role] || shared;
}

function renderNavigation(nav) {
  let currentGroup = "";
  return nav.map((item) => {
    const heading = item.group !== currentGroup
      ? `<span class="nav-heading">${escapeHtml(item.group)}</span>`
      : "";
    currentGroup = item.group;
    return `${heading}<button class="${item.id === currentView ? "active" : ""}" data-view="${item.id}" ${item.id === currentView ? 'aria-current="page"' : ""}>${escapeHtml(item.label)}</button>`;
  }).join("");
}

function countLabel(count, singular, plural = `${singular}s`) {
  return `${count} ${count === 1 ? singular : plural}`;
}

function moduleSummary(view, user) {
  if (view === "periods") return countLabel(data.periods.length, "periodo", "periodos");
  if (view === "agreements") return `${countLabel(data.institutions.length, "institucion", "instituciones")} · ${countLabel(data.places.length, "plaza", "plazas")}`;
  if (view === "assignments") return countLabel(data.assignments.filter((item) => item.status === "ACTIVA").length, "practica activa", "practicas activas");
  if (view === "reports") return countLabel(data.assignments.filter((item) => item.status === "ACTIVA").length, "practica para consultar", "practicas para consultar");
  if (view === "audit") return countLabel(data.audit.length, "evento registrado", "eventos registrados");
  if (view === "student-activities" || view === "student-evidence" || view === "student-validations" || view === "student-progress") {
    const assignmentIds = data.assignments.filter((item) => item.studentId === user.id).map((item) => item.id);
    const activities = data.activities.filter((item) => assignmentIds.includes(item.assignmentId));
    if (view === "student-activities") return countLabel(activities.length, "actividad", "actividades");
    if (view === "student-validations") return countLabel(activities.filter((item) => item.status !== "BORRADOR").length, "actividad enviada", "actividades enviadas");
    if (view === "student-evidence") {
      const activityIds = activities.map((item) => item.id);
      return countLabel(data.evidences.filter((item) => activityIds.includes(item.activityId)).length, "evidencia cargada", "evidencias cargadas");
    }
    const assignment = data.assignments.find((item) => item.studentId === user.id && item.status === "ACTIVA");
    const period = data.periods.find((item) => item.id === assignment?.periodId);
    const hours = assignment ? approvedHours(assignment.id) || assignment.approvedHours : 0;
    return assignment ? `${Math.min(100, Math.round((hours / (period?.minHours || 320)) * 100))}% de avance` : "Sin practica activa";
  }
  if (view === "teacher-validations") return countLabel(pendingActivitiesForTeacher(user.id).length, "actividad por revisar", "actividades por revisar");
  if (view === "teacher-visits") return countLabel(data.visits.filter((item) => item.teacherId === user.id).length, "visita registrada", "visitas registradas");
  return "";
}

function renderModuleCards(user) {
  const modules = navForRole(user.role).filter((item) => item.id !== "dashboard");
  return `
    <section class="module-section" aria-labelledby="modules-heading">
      <div class="module-section-heading">
        <h2 id="modules-heading">Modulos de trabajo</h2>
        <p>Selecciona una opcion para ir directamente a la tarea que necesitas.</p>
      </div>
      <div class="module-grid">
        ${modules.map((item) => `
          <button class="module-card" type="button" data-view="${item.id}">
            <span class="module-card-heading">
              <strong>${escapeHtml(item.label)}</strong>
              <span aria-hidden="true">→</span>
            </span>
            <span class="module-card-description">${escapeHtml(item.description)}</span>
            <span class="module-card-summary">${escapeHtml(moduleSummary(item.id, user))}</span>
          </button>
        `).join("")}
      </div>
    </section>
  `;
}

function viewTitle(view, role) {
  if (view === "dashboard" && role !== "COORDINADOR") {
    return {
      ESTUDIANTE: "Resumen de practica",
      DOCENTE_ASESOR: "Resumen de seguimiento",
      DIRECTOR: "Resumen del programa"
    }[role] || "Resumen";
  }
  return {
    dashboard: "Resumen de coordinacion",
    periods: "CU-01 Programar periodo de practica",
    agreements: "CU-02 Gestionar convenios y plazas",
    assignments: "CU-03 Asignar practicante a plaza y docente asesor",
    reports: "CU-07 Consultar estado consolidado de practicas",
    audit: "Auditoria",
    "student-activities": "CU-04 Registrar actividad ejecutada y soportes",
    "student-evidence": "Evidencias y soportes",
    "student-validations": "Estado de validaciones",
    "student-progress": "Avance de practica",
    "teacher-validations": "CU-05 Validar actividades reportadas",
    "teacher-visits": "CU-06 Realizar visita de acompanamiento y evaluar"
  }[view] || "SIGPRA";
}

function viewSubtitle(view, role) {
  return {
    dashboard: `Panel de ${role === "COORDINADOR" ? "coordinacion" : role === "DOCENTE_ASESOR" ? "seguimiento" : role === "DIRECTOR" ? "direccion" : "estudiante"}`,
    periods: "Define fechas, horas minimas y criterios de evaluacion.",
    agreements: "Administra instituciones receptoras, convenios y cupos.",
    assignments: "Vincula estudiante, plaza, docente asesor y periodo.",
    reports: "Consulta horas, estados, evidencias y visitas.",
    audit: "Revisa acciones importantes registradas por el sistema.",
    "student-activities": "Registra actividades ejecutadas y horas reportadas.",
    "student-evidence": "Asocia soportes a las actividades reportadas.",
    "student-progress": "Consulta tu avance frente al periodo asignado.",
    "student-validations": "Consulta el resultado de las actividades revisadas por tu docente asesor.",
    "teacher-validations": "Aprueba, devuelve o rechaza actividades de estudiantes asignados.",
    "teacher-visits": "Registra visitas de acompanamiento y evaluaciones."
  }[view] || "";
}

function renderView(user) {
  const renderers = {
    dashboard: () => renderDashboard(user),
    periods: renderPeriods,
    agreements: renderAgreements,
    assignments: renderAssignments,
    reports: () => renderReports(user),
    audit: renderAudit,
    "student-activities": () => renderStudentActivities(user),
    "student-evidence": () => renderStudentEvidence(user),
    "student-validations": () => renderStudentValidations(user),
    "student-progress": () => renderStudentProgress(user),
    "teacher-validations": () => renderTeacherValidations(user),
    "teacher-visits": () => renderTeacherVisits(user)
  };
  return renderers[currentView]?.() || renderDashboard(user);
}

function renderLogin() {
  app.innerHTML = `
    <main class="login-page">
      <section class="login-aside">
        <div>
          <h1>SIGPRA</h1>
          <p>Aplicacion web para gestionar practicas academicas: periodos, convenios, asignaciones, evidencias, validacion, visitas y reportes.</p>
        </div>
        <p>Version funcional de demostracion con persistencia local en navegador.</p>
      </section>
      <section class="login-card">
        <div class="brand login-brand">
          <strong>SIGPRA</strong>
          <span>Gestion de practicas academicas</span>
        </div>
        <h2>Iniciar sesion</h2>
        <p class="muted">Usa una cuenta de prueba o ingresa correo y clave.</p>
        <form id="login-form" class="form-grid">
          <label class="full-field">Correo
            <input name="email" type="email" value="coordinador@udi.edu.co" required />
          </label>
          <label class="full-field">Clave
            <input name="password" type="password" value="123456" required />
          </label>
          <button class="button primary full-field" type="submit">Entrar</button>
        </form>
        <div class="demo-users">
          ${data.users
            .filter((user) => ["COORDINADOR", "ESTUDIANTE", "DOCENTE_ASESOR", "DIRECTOR"].includes(user.role))
            .slice(0, 4)
            .map((user) => `<button data-login="${user.email}"><strong>${escapeHtml(roleLabel(user.role))}</strong><br><span class="muted">${escapeHtml(user.email)}</span></button>`)
            .join("")}
        </div>
      </section>
    </main>
  `;
}

function renderDashboard(user) {
  if (user.role === "ESTUDIANTE") return renderStudentProgress(user, true);
  if (user.role === "DOCENTE_ASESOR") return renderTeacherHome(user);
  if (user.role === "DIRECTOR") return renderDirectorHome(user);

  const publishedPeriods = data.periods.filter((period) => period.status === "PUBLICADO");
  const activeAgreements = data.agreements.filter((agreement) => agreementStatus(agreement) === "VIGENTE");
  const activePeriod = [...publishedPeriods].sort((left, right) =>
    `${right.year}-${right.semester}`.localeCompare(`${left.year}-${left.semester}`)
  )[0];
  const unassignedStudents = activePeriod
    ? studentUsers().filter((student) => !data.assignments.some((assignment) =>
      assignment.studentId === student.id && assignment.periodId === activePeriod.id && assignment.status === "ACTIVA"
    )).length
    : studentUsers().length;

  return `
    <section class="grid">
      ${metricCard("Periodos publicados", publishedPeriods.length, activePeriod ? `${activePeriod.year}-${activePeriod.semester} activo` : "No hay periodos activos")}
      ${metricCard("Convenios vigentes", activeAgreements.length, `${activeAgreements.filter((agreement) => {
        const daysUntilEnd = (new Date(`${agreement.end}T00:00:00`) - new Date()) / 86400000;
        return daysUntilEnd >= 0 && daysUntilEnd <= 30;
      }).length} por vencer este mes`)}
      ${metricCard("Asignaciones pendientes", unassignedStudents, "Estudiantes por asignar")}
      <article class="card wide">
        <h2>Periodo activo</h2>
        <div class="table-wrap">
          <table>
            <thead><tr><th>Programa</th><th>Nivel</th><th>Vigencia</th><th>Estado</th></tr></thead>
            <tbody>
              ${activePeriod
                ? `<tr><td>${escapeHtml(activePeriod.program)}</td><td>${escapeHtml(activePeriod.level)}</td><td>${moneyDate(activePeriod.start)} - ${moneyDate(activePeriod.end)}</td><td>${statusTag(activePeriod.status)}</td></tr>`
                : `<tr><td colspan="4">No hay periodos publicados.</td></tr>`}
            </tbody>
          </table>
        </div>
      </article>
      <article class="card">
        <h2>Acciones</h2>
        <p class="muted">Continua la gestion del periodo.</p>
        <div class="toolbar">
          <button class="button primary" data-view="assignments">Asignar practicantes</button>
        </div>
      </article>
    </section>
    ${renderModuleCards(user)}
  `;
}

function renderDirectorHome(user) {
  const activeAssignments = data.assignments.filter((assignment) => assignment.status === "ACTIVA");
  const averageProgress = activeAssignments.length
    ? Math.round(activeAssignments.reduce((sum, assignment) => {
      const period = data.periods.find((item) => item.id === assignment.periodId);
      const hours = approvedHours(assignment.id) || assignment.approvedHours;
      return sum + Math.min(100, (hours / (period?.minHours || 1)) * 100);
    }, 0) / activeAssignments.length)
    : 0;
  const activeTeachers = new Set(activeAssignments.map((assignment) => assignment.teacherId)).size;

  return `
    <section class="grid">
      ${metricCard("Programas activos", data.periods.filter((period) => period.status === "PUBLICADO").length, "Periodos publicados")}
      ${metricCard("Estudiantes en practica", activeAssignments.length, `${activeTeachers} docentes asesores`)}
      ${metricCard("Avance promedio", `${averageProgress}%`, "Sobre horas requeridas")}
      <article class="card full">
        <h2>Acciones</h2>
        <p class="muted">Revisa el estado consolidado del periodo.</p>
        <div class="toolbar">
          <button class="button primary" data-view="reports">Ver consolidado de practicas</button>
        </div>
      </article>
    </section>
    ${renderModuleCards(user)}
  `;
}

function renderTeacherHome(user) {
  const pending = pendingActivitiesForTeacher(user.id);
  const teacherAssignments = data.assignments.filter((assignment) => assignment.teacherId === user.id);
  return `
    <section class="grid">
      ${metricCard("Estudiantes asignados", teacherAssignments.length, "Practicas activas")}
      ${metricCard("Pendientes", pending.length, "Actividades por revisar")}
      ${metricCard("Visitas registradas", data.visits.filter((visit) => visit.teacherId === user.id).length, "Acompanamiento")}
      <article class="card full">
        <h2>Actividades pendientes</h2>
        ${activitiesTable(pending, true)}
      </article>
    </section>
    ${renderModuleCards(user)}
  `;
}

function metricCard(label, value, helper) {
  return `
    <article class="card">
      <p class="muted">${escapeHtml(label)}</p>
      <span class="metric">${escapeHtml(value)}</span>
      <p class="muted">${escapeHtml(helper)}</p>
    </article>
  `;
}

function renderPeriods() {
  return `
    <section class="stack">
      <article class="card full">
        <h2>Paso 1-2: Programa y configuracion previa</h2>
        <p class="muted">Selecciona el programa y el nivel de practica que se van a programar.</p>
        <h2>Paso 3-4: Fechas, intensidad horaria y criterios de evaluacion</h2>
        ${modalForm("period")}
      </article>
      <article class="card full">
        <h2>Periodos registrados</h2>
        <div class="table-wrap">
          <table>
            <thead><tr><th>Programa</th><th>Nivel</th><th>Periodo</th><th>Vigencia</th><th>Reporte hasta</th><th>Horas</th><th>Evaluacion</th><th>Estado</th></tr></thead>
            <tbody>
              ${data.periods.map((period) => `
                <tr>
                  <td>${escapeHtml(period.program)}</td>
                  <td>${escapeHtml(period.level)}</td>
                  <td>${period.year}-${period.semester}</td>
                  <td>${moneyDate(period.start)} - ${moneyDate(period.end)}</td>
                  <td>${moneyDate(period.reportLimit)}</td>
                  <td>${period.minHours}</td>
                  <td>${(period.rubric || []).map((criterion) => `${escapeHtml(criterion.name)} (${criterion.weight}%)`).join("<br>") || "Sin criterios configurados"}</td>
                  <td>${statusTag(period.status)}${period.status === "BORRADOR" ? `<br><button class="button" data-modal="period" data-id="${period.id}">Continuar borrador</button>` : ""}</td>
                </tr>
              `).join("") || `<tr><td colspan="8">No hay periodos registrados.</td></tr>`}
            </tbody>
          </table>
        </div>
      </article>
    </section>
  `;
}

function renderAgreements() {
  return `
    <section class="stack">
      <article class="card full">
        <div class="toolbar">
          <button class="button primary" data-modal="institution">Nueva institucion</button>
        </div>
        <h2>Paso 1: Institucion receptora</h2>
        <p class="muted">Selecciona una institucion registrada o crea una nueva.</p>
        <h2>Paso 2-3: Convenio y plazas ofrecidas</h2>
        <p class="muted">Los convenios vencidos o sin documento quedan inactivos; sus plazas no se ofrecen para asignacion.</p>
        ${modalForm("agreement-place")}
      </article>
      <article class="card full">
        <h2>Convenios y plazas registrados</h2>
        <div class="table-wrap">
          <table>
            <thead><tr><th>Institucion</th><th>Convenio</th><th>Vigencia</th><th>Plazas</th><th>Cupos</th><th>Estado</th></tr></thead>
            <tbody>
              ${data.agreements.map((agreement) => {
                const institution = data.institutions.find((item) => item.id === agreement.institutionId);
                const places = data.places.filter((place) => place.agreementId === agreement.id);
                const cupos = agreementStatus(agreement) === "VIGENTE"
                  ? places.reduce((sum, place) => sum + (place.offered - place.occupied), 0)
                  : 0;
                return `
                  <tr>
                    <td>${escapeHtml(institution?.name)}</td>
                    <td>${escapeHtml(agreement.number)}</td>
                    <td>${moneyDate(agreement.start)} - ${moneyDate(agreement.end)}</td>
                    <td>${places.length}</td>
                    <td>${cupos}</td>
                    <td>${statusTag(agreementStatus(agreement))}</td>
                  </tr>
                `;
              }).join("") || `<tr><td colspan="6">No hay convenios registrados.</td></tr>`}
            </tbody>
          </table>
        </div>
      </article>
      <article class="card full">
        <h2>Plazas disponibles</h2>
        <div class="table-wrap">
          <table>
            <thead><tr><th>Institucion</th><th>Nivel</th><th>Jornada</th><th>Cupos</th><th>Docente titular</th><th>Estado</th></tr></thead>
            <tbody>
              ${data.places.map((place) => {
                const agreement = data.agreements.find((item) => item.id === place.agreementId);
                const institution = data.institutions.find((item) => item.id === agreement?.institutionId);
                return `
                  <tr>
                    <td>${escapeHtml(institution?.name)}</td>
                    <td>${escapeHtml(place.level)}</td>
                    <td>${escapeHtml(place.shift)}</td>
                    <td>${place.occupied} / ${place.offered}</td>
                    <td>${escapeHtml(place.tutor)}</td>
                    <td>${statusTag(placeStatus(place, agreement))}</td>
                  </tr>
                `;
              }).join("") || `<tr><td colspan="6">No hay plazas registradas.</td></tr>`}
            </tbody>
          </table>
        </div>
      </article>
    </section>
  `;
}

function renderAssignments() {
  return `
    <section class="stack">
      <article class="card full">
        <h2>1. Estudiante practicante</h2>
        <p class="muted">Periodo vigente: selecciona al estudiante habilitado para iniciar la asignacion.</p>
        <h2>2. Plaza de practica</h2>
        <h2>3. Docente asesor</h2>
        <h2>4. Resumen de la asignacion</h2>
        ${assignmentWorkflowForm()}
      </article>
      <article class="card full">
        <h2>Asignaciones registradas</h2>
        ${assignmentsTable(data.assignments)}
      </article>
    </section>
  `;
}

function assignmentWorkflowForm() {
  const publishedPeriods = data.periods.filter((period) => period.status === "PUBLICADO");
  const availablePlaces = data.places.filter((place) => {
    const agreement = data.agreements.find((item) => item.id === place.agreementId);
    return placeStatus(place, agreement) === "DISPONIBLE";
  });
  const availableTeachers = teacherUsers().filter((teacher) =>
    data.assignments.filter((assignment) =>
      assignment.teacherId === teacher.id && assignment.status === "ACTIVA"
    ).length < 6
  );
  const activePeriod = [...publishedPeriods].sort((left, right) =>
    `${right.year}-${right.semester}`.localeCompare(`${left.year}-${left.semester}`)
  )[0];

  if (!publishedPeriods.length) {
    return `<div class="empty">Publica un periodo antes de crear asignaciones.</div>`;
  }

  return `
    <form class="form-grid workflow-form" data-submit="assignment">
      <label class="full-field">Estudiante habilitado
        <select name="studentId" required>
          <option value="">-- Seleccione un estudiante --</option>
          ${studentUsers().map((student) => `<option value="${student.id}">${escapeHtml(student.name)} - ${escapeHtml(student.code || student.email)}</option>`).join("")}
        </select>
      </label>
      <div class="full-field" id="assignment-student-details" hidden></div>
      <label class="full-field">Periodo vigente
        <select name="periodId" required>
          ${publishedPeriods.map((period) => `<option value="${period.id}" ${period.id === activePeriod?.id ? "selected" : ""}>${escapeHtml(period.year)}-${escapeHtml(period.semester)} (${escapeHtml(period.program)} - ${escapeHtml(period.level)})</option>`).join("")}
        </select>
      </label>
      <div class="full-field notice warning" id="assignment-reassignment" hidden>
        <strong>Asignacion activa encontrada.</strong> Para cambiar la plaza o el docente, selecciona el motivo de la reasignacion.
        <label>Motivo de la reasignacion
          <select name="reassignmentReason">
            <option value="">-- Seleccione un motivo --</option>
            <option>Solicitud de la institucion receptora</option>
            <option>Solicitud del estudiante</option>
            <option>Cambio de disponibilidad del docente asesor</option>
            <option>Otro</option>
          </select>
        </label>
      </div>
      <label class="full-field">Plaza de practica disponible
        <select name="placeId" required>
          <option value="">-- Seleccione una plaza --</option>
          ${availablePlaces.map((place) => {
            const agreement = data.agreements.find((item) => item.id === place.agreementId);
            const institution = data.institutions.find((item) => item.id === agreement?.institutionId);
            return `<option value="${place.id}">${escapeHtml(institution?.name)} - ${escapeHtml(place.level)} (${place.offered - place.occupied} cupos)</option>`;
          }).join("")}
        </select>
      </label>
      <div class="full-field" id="assignment-place-details" hidden></div>
      <label class="full-field">Docente asesor disponible
        <select name="teacherId" required>
          <option value="">-- Seleccione un docente --</option>
          ${availableTeachers.map((teacher) => {
            const load = data.assignments.filter((assignment) =>
              assignment.teacherId === teacher.id && assignment.status === "ACTIVA"
            ).length;
            return `<option value="${teacher.id}">${escapeHtml(teacher.name)} - ${load} de 6 estudiantes asignados</option>`;
          }).join("")}
        </select>
      </label>
      <article class="card full">
        <h3>Resumen de la asignacion</h3>
        <div id="assignment-summary" class="empty">Selecciona estudiante, plaza y docente para revisar la asignacion.</div>
      </article>
      <button class="button primary full-field" type="submit">Confirmar asignacion</button>
    </form>
  `;
}

function updateAssignmentWorkflow(form) {
  const studentId = Number(form.elements.studentId.value);
  const periodId = Number(form.elements.periodId.value);
  const placeId = Number(form.elements.placeId.value);
  const teacherId = Number(form.elements.teacherId.value);
  const student = data.users.find((item) => item.id === studentId && item.role === "ESTUDIANTE");
  const period = data.periods.find((item) => item.id === periodId);
  const place = data.places.find((item) => item.id === placeId);
  const agreement = data.agreements.find((item) => item.id === place?.agreementId);
  const institution = data.institutions.find((item) => item.id === agreement?.institutionId);
  const teacher = data.users.find((item) => item.id === teacherId && item.role === "DOCENTE_ASESOR");
  const activeAssignment = data.assignments.find((item) =>
    item.studentId === studentId && item.periodId === periodId && item.status === "ACTIVA"
  );
  const studentDetails = form.querySelector("#assignment-student-details");
  const placeDetails = form.querySelector("#assignment-place-details");
  const summary = form.querySelector("#assignment-summary");
  const reassignment = form.querySelector("#assignment-reassignment");
  const reason = form.elements.reassignmentReason;

  studentDetails.hidden = !student;
  studentDetails.innerHTML = student
    ? `<table><tbody><tr><th>Programa</th><td>${escapeHtml(period?.program || "No especificado")}</td><th>Semestre</th><td>${escapeHtml(student.semester || "No especificado")}</td></tr><tr><th>Estado</th><td><span class="tag ok">Habilitado</span></td><th>Horas requeridas</th><td>${period?.minHours || "No definidas"}</td></tr></tbody></table>`
    : "";

  reassignment.hidden = !activeAssignment;
  reason.required = Boolean(activeAssignment);
  if (!activeAssignment) reason.value = "";
  if (activeAssignment) {
    const previous = assignmentDetails(activeAssignment);
    reassignment.querySelector("strong").nextSibling.textContent =
      ` Este estudiante ya esta asignado en el periodo ${period?.year}-${period?.semester} a ${previous.institution?.name || "una institucion"} con ${previous.teacher?.name || "un docente asesor"}.`;
  }

  placeDetails.hidden = !place;
  placeDetails.innerHTML = place
    ? `<table><tbody><tr><th>Institucion</th><td>${escapeHtml(institution?.name || "")}</td><th>Convenio</th><td>${escapeHtml(agreement?.number || "")}</td></tr><tr><th>Nivel / jornada</th><td>${escapeHtml(place.level)} - ${escapeHtml(place.shift)}</td><th>Cupos</th><td>${place.offered - place.occupied} disponibles de ${place.offered}</td></tr></tbody></table>`
    : "";

  const selectedStudent = form.elements.studentId.selectedOptions[0]?.textContent;
  const selectedPlace = form.elements.placeId.selectedOptions[0]?.textContent;
  const selectedTeacher = form.elements.teacherId.selectedOptions[0]?.textContent;
  const selectedPeriod = form.elements.periodId.selectedOptions[0]?.textContent;
  const hasSelection = student && period && place && teacher;
  summary.classList.toggle("empty", !hasSelection);
  summary.innerHTML = hasSelection
    ? `<table><tbody><tr><th>Estudiante</th><td>${escapeHtml(selectedStudent)}</td></tr><tr><th>Plaza / institucion</th><td>${escapeHtml(selectedPlace)}</td></tr><tr><th>Docente asesor</th><td>${escapeHtml(selectedTeacher)}</td></tr><tr><th>Periodo</th><td>${escapeHtml(selectedPeriod)}</td></tr><tr><th>Tipo</th><td>${activeAssignment ? "Reasignacion" : "Nueva asignacion"}</td></tr></tbody></table>`
    : "Selecciona estudiante, plaza y docente para revisar la asignacion.";
}

function assignmentsTable(assignments) {
  if (!assignments.length) return `<div class="empty">No hay asignaciones registradas.</div>`;
  return `
    <div class="table-wrap">
      <table>
        <thead><tr><th>Estudiante</th><th>Institucion</th><th>Docente asesor</th><th>Periodo</th><th>Horas</th><th>Estado</th></tr></thead>
        <tbody>
          ${assignments.map((assignment) => {
            const d = assignmentDetails(assignment);
            const hours = approvedHours(assignment.id) || assignment.approvedHours;
            const min = d.period?.minHours || 1;
            return `
              <tr>
                <td>${escapeHtml(d.student?.name)}</td>
                <td>${escapeHtml(d.institution?.name)}</td>
                <td>${escapeHtml(d.teacher?.name)}</td>
                <td>${d.period ? `${d.period.year}-${d.period.semester}` : ""}</td>
                <td>${hours} / ${min}</td>
                <td>${statusTag(assignment.status)}</td>
              </tr>
            `;
          }).join("")}
        </tbody>
      </table>
    </div>
  `;
}

function renderStudentActivities(user) {
  const assignment = data.assignments.find((item) => item.studentId === user.id && item.status === "ACTIVA");
  const activities = assignment ? data.activities.filter((activity) => activity.assignmentId === assignment.id) : [];
  return `
    <section class="stack">
      <article class="card full">
        <h2>Nueva bitacora (CU-04)</h2>
        <p class="muted">Registra la actividad realizada, las horas y sus soportes. Puedes guardar un borrador o enviarlo al docente asesor.</p>
        ${assignment ? modalForm("activity") : `<div class="empty">Necesitas una asignacion activa para registrar actividades.</div>`}
      </article>
      <article class="card full">
        <h2>Actividades registradas</h2>
        ${studentActivitiesTable(activities)}
      </article>
    </section>
  `;
}

function studentActivitiesTable(activities) {
  if (!activities.length) return `<div class="empty">No hay actividades registradas.</div>`;
  return `
    <div class="table-wrap">
      <table>
        <thead><tr><th>Fecha</th><th>Actividad</th><th>Horas</th><th>Soportes</th><th>Estado</th><th>Observacion</th><th>Accion</th></tr></thead>
        <tbody>
          ${activities.map((activity) => {
            const evidences = data.evidences.filter((evidence) => evidence.activityId === activity.id);
            const canEdit = ["DEVUELTA", "BORRADOR"].includes(activity.status);
            return `<tr>
              <td>${moneyDate(activity.date)}</td>
              <td><strong>${escapeHtml(activity.type)}</strong><br><span class="muted">${escapeHtml(activity.description)}</span></td>
              <td>${activity.hours}</td>
              <td>${evidences.map((evidence) => escapeHtml(evidence.fileName)).join("<br>") || "Sin soportes"}</td>
              <td>${statusTag(activity.status)}</td>
              <td>${escapeHtml(activity.observation || "Sin observacion")}</td>
              <td>${canEdit ? `<button class="button" data-modal="activity" data-id="${activity.id}">${activity.status === "DEVUELTA" ? "Corregir y reenviar" : "Completar borrador"}</button>` : "—"}</td>
            </tr>`;
          }).join("")}
        </tbody>
      </table>
    </div>
  `;
}

function activitiesTable(activities, withActions = false) {
  if (!activities.length) return `<div class="empty">No hay actividades registradas.</div>`;
  return `
    <div class="table-wrap">
      <table>
        <thead><tr><th>Fecha</th><th>Estudiante</th><th>Actividad</th><th>Horas</th><th>Estado</th><th>Observacion</th>${withActions ? "<th>Accion</th>" : ""}</tr></thead>
        <tbody>
          ${activities.map((activity) => {
            const assignment = data.assignments.find((item) => item.id === activity.assignmentId);
            const d = assignmentDetails(assignment || {});
            return `
              <tr>
                <td>${moneyDate(activity.date)}</td>
                <td>${escapeHtml(d.student?.name || "")}</td>
                <td><strong>${escapeHtml(activity.type)}</strong><br><span class="muted">${escapeHtml(activity.description)}</span></td>
                <td>${activity.hours}</td>
                <td>${statusTag(activity.status)}</td>
                <td>${escapeHtml(activity.observation || "Sin observacion")}</td>
                ${withActions ? `<td><button class="button" data-modal="validate" data-id="${activity.id}">Revisar</button></td>` : ""}
              </tr>
            `;
          }).join("")}
        </tbody>
      </table>
    </div>
  `;
}

function renderStudentEvidence(user) {
  const assignment = data.assignments.find((item) => item.studentId === user.id && item.status === "ACTIVA");
  const activities = assignment ? data.activities.filter((activity) => activity.assignmentId === assignment.id) : [];
  const activityIds = activities.map((activity) => activity.id);
  const evidences = data.evidences.filter((evidence) => activityIds.includes(evidence.activityId));
  return `
    <section class="stack">
      <article class="card full">
        <h2>Registrar soporte o evidencia</h2>
        <p class="muted">Selecciona la actividad y adjunta el archivo que la respalda.</p>
        ${assignment ? modalForm("evidence") : `<div class="empty">Necesitas una asignacion activa para adjuntar evidencias.</div>`}
      </article>
      <article class="card full">
        <div class="table-wrap">
          <table>
            <thead><tr><th>Archivo</th><th>Actividad</th><th>Tipo</th><th>Referencia</th><th>Estado</th></tr></thead>
            <tbody>
              ${evidences.map((evidence) => {
                const activity = data.activities.find((item) => item.id === evidence.activityId);
                return `<tr><td>${escapeHtml(evidence.fileName)}</td><td>${escapeHtml(activity?.type)}</td><td>${escapeHtml(evidence.type)}</td><td>${escapeHtml(evidence.mongoId)}</td><td>${statusTag(evidence.status)}</td></tr>`;
              }).join("") || `<tr><td colspan="5">No hay evidencias registradas.</td></tr>`}
            </tbody>
          </table>
        </div>
      </article>
    </section>
  `;
}

function renderStudentValidations(user) {
  const assignmentIds = data.assignments
    .filter((assignment) => assignment.studentId === user.id)
    .map((assignment) => assignment.id);
  const activities = data.activities.filter((activity) =>
    assignmentIds.includes(activity.assignmentId) && activity.status !== "BORRADOR"
  );
  return `
    <section class="grid">
      <article class="card full">
        <h2>Estado de validaciones</h2>
        <div class="table-wrap">
          <table>
            <thead><tr><th>Actividad</th><th>Fecha</th><th>Horas</th><th>Resultado</th><th>Observacion</th></tr></thead>
            <tbody>
              ${activities.map((activity) => `
                <tr>
                  <td>${escapeHtml(activity.type)}</td>
                  <td>${moneyDate(activity.date)}</td>
                  <td>${activity.hours}</td>
                  <td>${statusTag(activity.status)}</td>
                  <td>${escapeHtml(activity.observation || "Sin observacion")}</td>
                </tr>
              `).join("") || `<tr><td colspan="5">Aun no hay actividades enviadas a validacion.</td></tr>`}
            </tbody>
          </table>
        </div>
      </article>
    </section>
  `;
}

function renderStudentProgress(user, compact = false) {
  const assignment = data.assignments.find((item) => item.studentId === user.id && item.status === "ACTIVA");
  if (!assignment) return `<div class="empty">No tienes una asignacion activa.</div>${compact ? renderModuleCards(user) : ""}`;
  const d = assignmentDetails(assignment);
  const hours = approvedHours(assignment.id) || assignment.approvedHours;
  const required = d.period?.minHours || 320;
  const percent = Math.min(100, Math.round((hours / required) * 100));
  const activities = data.activities.filter((activity) => activity.assignmentId === assignment.id);
  return `
    <section class="grid">
      ${metricCard("Horas aprobadas", `${hours} / ${required}`, `${percent}% de avance`)}
      ${metricCard("Actividades", activities.length, "Registros creados")}
      ${metricCard("Pendientes", activities.filter((item) => item.status === "PENDIENTE").length, "Por validar")}
      <article class="card wide">
        <h2>Practica activa</h2>
        <p><strong>Institucion:</strong> ${escapeHtml(d.institution?.name)}</p>
        <p><strong>Docente asesor:</strong> ${escapeHtml(d.teacher?.name)}</p>
        <p><strong>Periodo:</strong> ${d.period?.year}-${d.period?.semester}</p>
        <div class="progress" aria-label="Avance de horas"><span style="width:${percent}%"></span></div>
      </article>
      <article class="card">
        <h2>Acciones</h2>
        <div class="toolbar">
          <button class="button primary" data-view="student-activities">Registrar actividad</button>
          <button class="button" data-view="student-evidence">Registrar evidencia</button>
        </div>
      </article>
      ${compact ? "" : `<article class="card full"><h2>Historial</h2>${activitiesTable(activities)}</article>`}
    </section>
    ${compact ? renderModuleCards(user) : ""}
  `;
}

function renderTeacherValidations(user) {
  const assignments = data.assignments.filter((assignment) => assignment.teacherId === user.id).map((assignment) => assignment.id);
  const activities = data.activities.filter((activity) => assignments.includes(activity.assignmentId) && activity.status === "PENDIENTE");
  return `<section class="stack"><article class="card full"><h2>Paso 1-2: Actividades pendientes</h2><p class="muted">Selecciona una actividad para revisar su descripcion, horas y soportes.</p>${activitiesTable(activities, true)}</article></section>`;
}

function renderTeacherVisits(user) {
  const assignments = data.assignments.filter((assignment) => assignment.teacherId === user.id);
  return `
    <section class="stack">
      <article class="card full">
        <h2>Paso 2-4: Estudiante a evaluar</h2>
        <h2>Paso 5: Registro de la visita</h2>
        <h2>Paso 6-8: Rubrica de evaluacion</h2>
        ${assignments.length ? modalForm("visit") : `<div class="empty">No tienes estudiantes asignados para realizar visitas.</div>`}
      </article>
      <article class="card full">
        <h2>Visitas registradas</h2>
        <div class="table-wrap">
          <table>
            <thead><tr><th>Fecha</th><th>Estudiante</th><th>Modalidad</th><th>Asistencia</th><th>Concepto</th><th>Puntaje</th><th>Observacion</th><th>Estado</th></tr></thead>
            <tbody>
              ${data.visits.filter((visit) => visit.teacherId === user.id).map((visit) => {
                const assignment = assignments.find((item) => item.id === visit.assignmentId);
                const d = assignmentDetails(assignment || {});
                return `<tr><td>${moneyDate(visit.date)}</td><td>${escapeHtml(d.student?.name)}</td><td>${escapeHtml(visit.mode)}</td><td>${visit.attendance ? "Asistio" : "No asistio"}</td><td>${escapeHtml(visit.concept || "Sin evaluacion")}</td><td>${visit.score}</td><td>${escapeHtml(visit.observation)}${visit.supportFileName ? `<br><span class="muted">Soporte: ${escapeHtml(visit.supportFileName)}</span>` : ""}</td><td>${statusTag(visit.status)}</td></tr>`;
              }).join("") || `<tr><td colspan="8">No hay visitas registradas.</td></tr>`}
            </tbody>
          </table>
        </div>
      </article>
    </section>
  `;
}

function renderReports(user) {
  const visibleAssignments = data.assignments.filter((assignment) =>
    assignment.status === "ACTIVA" && (user.role !== "DOCENTE_ASESOR" || assignment.teacherId === user.id)
  );
  const rows = visibleAssignments.map((assignment) => {
    const d = assignmentDetails(assignment);
    const hours = approvedHours(assignment.id) || assignment.approvedHours;
    const required = d.period?.minHours || 320;
    return { assignment, d, hours, required, percent: Math.round((hours / required) * 100) };
  }).filter((row) =>
    (!reportFilters.periodId || row.assignment.periodId === Number(reportFilters.periodId)) &&
    (!reportFilters.program || row.d.period?.program === reportFilters.program) &&
    (!reportFilters.institutionId || row.d.institution?.id === Number(reportFilters.institutionId)) &&
    (!reportFilters.teacherId || row.assignment.teacherId === Number(reportFilters.teacherId))
  );
  const assignmentIds = rows.map((row) => row.assignment.id);
  const reportActivities = data.activities.filter((item) => assignmentIds.includes(item.assignmentId));
  const reportVisits = data.visits.filter((item) => assignmentIds.includes(item.assignmentId));
  const pendingCount = reportActivities.filter((item) => item.status === "PENDIENTE").length;
  const programs = [...new Set(data.periods.map((period) => period.program))];
  const selectedFilter = (value, selected) => String(value) === String(selected) ? "selected" : "";
  const institutionGroups = [...rows.reduce((groups, row) => {
    const key = row.d.institution?.id || 0;
    const group = groups.get(key) || {
      name: row.d.institution?.name || "Sin institucion",
      students: 0,
      hours: 0,
      required: 0
    };
    group.students += row.assignment.status === "ACTIVA" ? 1 : 0;
    group.hours += row.hours;
    group.required += row.required;
    groups.set(key, group);
    return groups;
  }, new Map()).values()];
  return `
    <section class="grid">
      <article class="card full">
        <div class="toolbar">
          <button class="button primary" data-action="export-json">Exportar JSON</button>
          <button class="button" data-action="export-csv">Exportar CSV</button>
          <button class="button" data-action="export-print">Imprimir / guardar como PDF</button>
        </div>
        <h2>Periodo y filtros</h2>
        <form class="form-grid report-filters" data-submit="report-filters">
          <label>Periodo academico<select name="periodId">
            <option value="">Todos los periodos</option>
            ${data.periods.map((period) => `<option value="${period.id}" ${selectedFilter(period.id, reportFilters.periodId)}>${period.year}-${period.semester}</option>`).join("")}
          </select></label>
          <label>Programa<select name="program">
            <option value="">Todos</option>
            ${programs.map((program) => `<option value="${escapeHtml(program)}" ${selectedFilter(program, reportFilters.program)}>${escapeHtml(program)}</option>`).join("")}
          </select></label>
          <label>Institucion receptora<select name="institutionId">
            <option value="">Todas</option>
            ${optionList(data.institutions, (item) => item.name, reportFilters.institutionId)}
          </select></label>
          <label>Docente asesor<select name="teacherId">
            <option value="">Todos</option>
            ${optionList(teacherUsers(), (item) => item.name, reportFilters.teacherId)}
          </select></label>
          <button class="button primary full-field" type="submit">Aplicar filtros</button>
        </form>
        ${pendingCount ? `<div class="notice warning">Consolidado parcial: ${pendingCount} actividad(es) esperan validacion.</div>` : ""}
        <div class="report-box ${rows.length ? "" : "hidden"}">
          <article><strong>${rows.length}</strong><br><span class="muted">Practicas</span></article>
          <article><strong>${rows.reduce((sum, row) => sum + row.hours, 0)}</strong><br><span class="muted">Horas aprobadas</span></article>
          <article><strong>${reportActivities.filter((item) => item.status === "APROBADA").length}</strong><br><span class="muted">Actividades validadas</span></article>
          <article><strong>${reportVisits.length}</strong><br><span class="muted">Visitas realizadas</span></article>
        </div>
        ${rows.length ? "" : `<div class="empty">No hay informacion para los filtros seleccionados. Ajusta los filtros para generar el consolidado.</div>`}
        <div class="report-data ${rows.length ? "" : "hidden"}">
        <h2>Consolidado por institucion</h2>
        <div class="table-wrap">
          <table>
            <thead><tr><th>Institucion</th><th>Estudiantes activos</th><th>Horas aprobadas</th><th>Avance</th></tr></thead>
            <tbody>
              ${institutionGroups.map((group) => {
                const percent = group.required ? Math.round(group.hours / group.required * 100) : 0;
                return `<tr><td>${escapeHtml(group.name)}</td><td>${group.students}</td><td>${group.hours}</td><td>${percent}%</td></tr>`;
              }).join("") || `<tr><td colspan="4">No hay instituciones para los filtros seleccionados.</td></tr>`}
            </tbody>
          </table>
        </div>
        <h2>Detalle de practicas</h2>
        <div class="table-wrap">
          <table>
            <thead><tr><th>Periodo</th><th>Programa</th><th>Estudiante</th><th>Institucion</th><th>Docente</th><th>Horas</th><th>Avance</th><th>Estado</th></tr></thead>
            <tbody>
              ${rows.map((row) => `
                <tr>
                  <td>${row.d.period ? `${row.d.period.year}-${row.d.period.semester}` : ""}</td>
                  <td>${escapeHtml(row.d.period?.program)}</td>
                  <td>${escapeHtml(row.d.student?.name)}</td>
                  <td>${escapeHtml(row.d.institution?.name)}</td>
                  <td>${escapeHtml(row.d.teacher?.name)}</td>
                  <td>${row.hours} / ${row.required}</td>
                  <td><div class="progress"><span style="width:${Math.min(100, row.percent)}%"></span></div>${row.percent}%</td>
                  <td>${statusTag(row.assignment.status)}</td>
                </tr>
              `).join("") || `<tr><td colspan="8">No hay informacion para los filtros seleccionados.</td></tr>`}
            </tbody>
          </table>
        </div>
        </div>
        <p class="muted">El reporte se calcula con los datos registrados para el periodo y los filtros aplicados. PDF se genera con la opcion Imprimir del navegador.</p>
      </article>
    </section>
  `;
}

function filteredReportAssignments() {
  const user = currentUser();
  return data.assignments.filter((assignment) => {
    if (assignment.status !== "ACTIVA") return false;
    if (user?.role === "DOCENTE_ASESOR" && assignment.teacherId !== user.id) return false;
    const details = assignmentDetails(assignment);
    return (!reportFilters.periodId || assignment.periodId === Number(reportFilters.periodId)) &&
      (!reportFilters.program || details.period?.program === reportFilters.program) &&
      (!reportFilters.institutionId || details.institution?.id === Number(reportFilters.institutionId)) &&
      (!reportFilters.teacherId || assignment.teacherId === Number(reportFilters.teacherId));
  });
}

function renderAudit() {
  return `
    <section class="grid">
      <article class="card full">
        <div class="table-wrap">
          <table>
            <thead><tr><th>Fecha</th><th>Usuario</th><th>Evento</th><th>Entidad</th></tr></thead>
            <tbody>
              ${data.audit.map((item) => `<tr><td>${escapeHtml(item.date)}</td><td>${escapeHtml(item.user)}</td><td>${escapeHtml(item.event)}</td><td>${escapeHtml(item.entity)}</td></tr>`).join("")}
            </tbody>
          </table>
        </div>
      </article>
    </section>
  `;
}

function openModal(type, id = null) {
  const modal = document.createElement("div");
  modal.className = "modal-backdrop";
  modal.innerHTML = `<section class="modal">${modalContent(type, id)}</section>`;
  document.body.appendChild(modal);
}

function modalContent(type, id) {
  const title = {
    period: "Nuevo periodo",
    institution: "Nueva institucion",
    agreement: "Nuevo convenio",
    "agreement-place": "Registrar convenio y plazas",
    place: "Nueva plaza",
    assignment: "Nueva asignacion",
    activity: "Nueva actividad",
    evidence: "Registrar evidencia",
    validate: "Validar actividad",
    visit: "Registrar visita"
  }[type];
  return `
    <div class="modal-header">
      <h2>${escapeHtml(title)}</h2>
      <button class="close" data-action="close-modal" aria-label="Cerrar">×</button>
    </div>
    ${modalForm(type, id)}
  `;
}

function optionList(items, labelFn, selectedId = "") {
  return items.map((item) => `<option value="${item.id}" ${Number(item.id) === Number(selectedId) && selectedId !== "" ? "selected" : ""}>${escapeHtml(labelFn(item))}</option>`).join("");
}

function modalForm(type, id) {
  const user = currentUser();
  if (type === "period") {
    const period = id ? data.periods.find((item) => item.id === Number(id)) : null;
    const criterion = (index, field, fallback) => escapeHtml(period?.rubric?.[index]?.[field] ?? fallback);
    const programs = [...new Set([
      "Ingenieria de Sistemas",
      "Licenciatura en Pedagogia",
      ...data.periods.map((item) => item.program)
    ])];
    const levels = [...new Set([
      "Practica I",
      "Practica II",
      ...data.periods.map((item) => item.level)
    ])];
    return `
      <form class="form-grid workflow-form" data-submit="period" data-id="${period?.id || ""}">
        <label>Programa academico<select name="program" required>${programs.map((program) => `<option ${program === (period?.program || programs[0]) ? "selected" : ""}>${escapeHtml(program)}</option>`).join("")}</select></label>
        <label>Nivel de practica<select name="level" required>${levels.map((level) => `<option ${level === (period?.level || levels[0]) ? "selected" : ""}>${escapeHtml(level)}</option>`).join("")}</select></label>
        <label>Anio<input name="year" type="number" value="${period?.year || 2026}" required></label>
        <label>Semestre<select name="semester"><option value="1" ${period?.semester === 1 ? "selected" : ""}>1</option><option value="2" ${!period || period.semester === 2 ? "selected" : ""}>2</option></select></label>
        <label>Fecha inicio<input name="start" type="date" value="${escapeHtml(period?.start || "")}" required></label>
        <label>Fecha fin<input name="end" type="date" value="${escapeHtml(period?.end || "")}" required></label>
        <label>Fecha limite de reporte<input name="reportLimit" type="date" value="${escapeHtml(period?.reportLimit || "")}" required></label>
        <label>Intensidad horaria minima<input name="minHours" type="number" min="1" value="${period?.minHours || 320}" required></label>
        <div class="full-field">
          <h3>Criterios de evaluacion y pesos</h3>
          <div class="form-grid">
            <label>Criterio 1<input name="criterion1" value="${criterion(0, "name", "Cumplimiento de horas")}" required></label>
            <label>Peso (%)<input name="weight1" type="number" min="0" max="100" value="${criterion(0, "weight", 40)}" required></label>
            <label>Criterio 2<input name="criterion2" value="${criterion(1, "name", "Validacion de actividades")}" required></label>
            <label>Peso (%)<input name="weight2" type="number" min="0" max="100" value="${criterion(1, "weight", 30)}" required></label>
            <label>Criterio 3<input name="criterion3" value="${criterion(2, "name", "Visita de acompanamiento")}" required></label>
            <label>Peso (%)<input name="weight3" type="number" min="0" max="100" value="${criterion(2, "weight", 30)}" required></label>
          </div>
          <p class="muted">Los pesos deben sumar exactamente 100% para publicar el periodo.</p>
        </div>
        <div class="full-field notice warning">No se podra publicar si las fechas no son coherentes, las horas son inferiores al minimo previo del programa o los pesos no suman 100%.</div>
        <button class="button full-field" type="submit" name="status" value="BORRADOR">Guardar como borrador</button>
        <button class="button primary full-field" type="submit" name="status" value="PUBLICADO">Publicar periodo</button>
      </form>`;
  }
  if (type === "institution") {
    return `
      <form class="form-grid" data-submit="institution">
        <label>Nombre<input name="name" required></label>
        <label>NIT<input name="nit" required></label>
        <label>Contacto<input name="contact" required></label>
        <label>Telefono<input name="phone" required></label>
        <label class="full-field">Correo<input name="email" type="email" required></label>
        <button class="button primary full-field" type="submit">Guardar institucion</button>
      </form>`;
  }
  if (type === "agreement") {
    return `
      <form class="form-grid" data-submit="agreement">
        <label>Institucion<select name="institutionId">${optionList(data.institutions, (item) => item.name)}</select></label>
        <label>Numero convenio<input name="number" required></label>
        <label>Fecha inicio<input name="start" type="date" required></label>
        <label>Fecha fin<input name="end" type="date" required></label>
        <label class="full-field">Documento<input name="document" placeholder="convenio.pdf"></label>
        <button class="button primary full-field" type="submit">Guardar convenio</button>
      </form>`;
  }
  if (type === "agreement-place") {
    const institutions = data.institutions.filter((institution) => institution.active);
    return `
      <form class="form-grid" data-submit="agreement-place">
        <label>Institucion receptora<select name="institutionId" required>${optionList(institutions, (item) => `${item.name} - ${item.contact}`)}</select></label>
        <label>Numero de convenio<input name="number" required></label>
        <label>Vigencia desde<input name="start" type="date" required></label>
        <label>Vigencia hasta<input name="end" type="date" required></label>
        <label class="full-field">Documento suscrito del convenio<input name="document" type="file" accept=".pdf,.doc,.docx" /></label>
        <div class="full-field notice warning">El prototipo registra el nombre y los datos del archivo localmente; no carga documentos a un servidor.</div>
        <h3 class="full-field">Plaza ofrecida</h3>
        <label>Nivel educativo<input name="level" required></label>
        <label>Jornada<select name="shift"><option value="MANANA">Manana</option><option value="TARDE">Tarde</option><option value="NOCHE">Noche</option><option value="MIXTA">Mixta</option></select></label>
        <label>Cupos<input name="offered" type="number" min="1" value="2" required></label>
        <label>Docente titular<input name="tutor" required></label>
        <button class="button primary full-field" type="submit">Guardar convenio y plaza</button>
      </form>`;
  }
  if (type === "place") {
    return `
      <form class="form-grid" data-submit="place">
        <label>Convenio<select name="agreementId">${optionList(data.agreements, (item) => item.number)}</select></label>
        <label>Nivel<input name="level" required></label>
        <label>Jornada<select name="shift"><option>MANANA</option><option>TARDE</option><option>NOCHE</option><option>MIXTA</option></select></label>
        <label>Cupos<input name="offered" type="number" min="1" value="5" required></label>
        <label class="full-field">Docente titular<input name="tutor" required></label>
        <button class="button primary full-field" type="submit">Guardar plaza</button>
      </form>`;
  }
  if (type === "assignment") {
    const availablePlaces = data.places.filter((place) => {
      const agreement = data.agreements.find((item) => item.id === place.agreementId);
      return placeStatus(place, agreement) === "DISPONIBLE";
    });
    const availableTeachers = teacherUsers().filter((teacher) =>
      data.assignments.filter((assignment) => assignment.teacherId === teacher.id && assignment.status === "ACTIVA").length < 6
    );
    return `
      <form class="form-grid" data-submit="assignment">
        <label>Estudiante habilitado<select name="studentId" required><option value="">Seleccione un estudiante</option>${optionList(studentUsers(), (item) => `${item.name} - ${item.code || item.email}`)}</select></label>
        <label>Periodo vigente<select name="periodId" required><option value="">Seleccione periodo</option>${optionList(data.periods.filter((period) => period.status === "PUBLICADO"), (item) => `${item.program} ${item.year}-${item.semester}`)}</select></label>
        <label>Plaza disponible<select name="placeId" required><option value="">Seleccione plaza</option>${optionList(availablePlaces, (item) => `${assignmentPlaceLabel(item)} - ${item.offered - item.occupied} cupos`)}</select></label>
        <label>Docente asesor disponible<select name="teacherId" required><option value="">Seleccione docente</option>${optionList(availableTeachers, (item) => `${item.name} - ${data.assignments.filter((assignment) => assignment.teacherId === item.id && assignment.status === "ACTIVA").length}/6 estudiantes`)}</select></label>
        <label class="full-field">Motivo de reasignacion <select name="reassignmentReason"><option value="">No es una reasignacion</option><option>Solicitud de la institucion receptora</option><option>Solicitud del estudiante</option><option>Cambio de disponibilidad del docente asesor</option><option>Otro</option></select></label>
        <div class="full-field notice warning">Si el estudiante ya tiene asignacion activa en este periodo, indica un motivo para reasignarlo. Se conserva el historial anterior.</div>
        <button class="button primary full-field" type="submit">Confirmar asignacion</button>
      </form>`;
  }
  if (type === "activity") {
    const activity = id ? data.activities.find((item) => item.id === Number(id)) : null;
    return `
      <form class="form-grid workflow-form" data-submit="activity" data-id="${id || ""}">
        ${activity?.observation ? `<div class="notice warning full-field">Observacion del docente: ${escapeHtml(activity.observation)}</div>` : ""}
        <label>Fecha de la actividad<input name="date" type="date" value="${escapeHtml(activity?.date || "")}" required></label>
        <label>Horas ejecutadas<input name="hours" type="number" min="0.5" max="24" step="0.5" value="${escapeHtml(activity?.hours || "")}" required></label>
        <label class="full-field">Tipo de actividad
          <input name="type" list="activity-types" value="${escapeHtml(activity?.type || "")}" required>
          <datalist id="activity-types"><option>Planeacion de clase</option><option>Refuerzo pedagogico</option><option>Reunion con docente titular</option></datalist>
        </label>
        <label class="full-field">Descripcion<textarea name="description" required>${escapeHtml(activity?.description || "")}</textarea></label>
        <label class="full-field">Soportes o evidencias<input name="files" type="file" accept=".pdf,.jpg,.jpeg,.png,.doc,.docx" multiple></label>
        <div class="notice warning full-field">El envio requiere al menos un soporte. Se permiten PDF, imagenes y documentos Office de hasta 10 MB cada uno. En este prototipo se persiste la referencia y el nombre de archivo, no el contenido binario.</div>
        <button class="button full-field" type="submit" name="status" value="BORRADOR">Guardar como borrador</button>
        <button class="button primary full-field" type="submit" name="status" value="PENDIENTE">Enviar a validacion</button>
      </form>`;
  }
  if (type === "evidence") {
    const assignment = data.assignments.find((item) => item.studentId === user.id && item.status === "ACTIVA");
    const activities = assignment ? data.activities.filter((item) => item.assignmentId === assignment.id) : [];
    return `
      <form class="form-grid workflow-form" data-submit="evidence">
        <label class="full-field">Actividad<select name="activityId" required>${optionList(activities, (item) => `${item.date} - ${item.type}`)}</select></label>
        <label class="full-field">Soporte o evidencia<input name="file" type="file" accept=".pdf,.jpg,.jpeg,.png,.doc,.docx" required></label>
        <div class="notice warning full-field">En esta version se guarda el nombre y la referencia del archivo en el navegador, no el contenido binario.</div>
        <button class="button primary full-field" type="submit">Registrar evidencia</button>
      </form>`;
  }
  if (type === "validate") {
    const activity = data.activities.find((item) => item.id === Number(id));
    const assignment = activity && data.assignments.find((item) => item.id === activity.assignmentId);
    const evidence = data.evidences.filter((item) => item.activityId === activity?.id);
    return `
      <form class="form-grid" data-submit="validate" data-id="${id}">
        <div class="full-field">
          <p><strong>${escapeHtml(activity?.type)}</strong><br>${escapeHtml(activity?.description)}</p>
          <p class="muted">Estudiante: ${escapeHtml(assignment && assignmentDetails(assignment).student?.name)} · Fecha: ${moneyDate(activity?.date)} · Horas: ${activity?.hours}</p>
          <h3>Soportes</h3>
          ${evidence.length ? `<ul>${evidence.map((item) => `<li>${escapeHtml(item.fileName)} (${escapeHtml(item.type)})</li>`).join("")}</ul>` : `<div class="empty">No se adjuntaron soportes.</div>`}
        </div>
        <label class="full-field"><span><input name="evidenceReviewed" type="checkbox" value="yes" style="width:auto"> He revisado los soportes obligatorios de esta actividad</span></label>
        <label>Resultado<select name="status"><option value="APROBADA">Aprobar</option><option value="DEVUELTA">Devolver</option><option value="RECHAZADA">Rechazar</option></select></label>
        <label>Observacion / justificacion<textarea name="observation" placeholder="Obligatoria al devolver o rechazar"></textarea></label>
        <div class="full-field notice warning">Para aprobar debes confirmar la revision de los soportes. Al devolver o rechazar, registra la justificacion para el estudiante.</div>
        <button class="button primary full-field" type="submit">Guardar validacion</button>
      </form>`;
  }
  if (type === "visit") {
    const assignments = data.assignments.filter((item) => item.teacherId === user.id && item.status === "ACTIVA");
    return `
      <form class="form-grid workflow-form" data-submit="visit">
        <label>Estudiante a evaluar<select name="assignmentId" required><option value="">Seleccione estudiante</option>${optionList(assignments, (item) => `${assignmentDetails(item).student?.name} - ${assignmentDetails(item).institution?.name}`, assignments[0]?.id)}</select></label>
        <div class="full-field" id="visit-assignment-details">${visitAssignmentDetails(assignments[0]?.id)}</div>
        <label>Fecha<input name="date" type="date" required></label>
        <label>Modalidad<select name="mode"><option value="PRESENCIAL">Presencial</option><option value="VIRTUAL">Remota</option><option value="MIXTA">Mixta</option></select></label>
        <label class="full-field">Observaciones de la visita<textarea name="observation" required></textarea></label>
        <label class="full-field"><span><input name="attendance" type="checkbox" checked style="width:auto"> El estudiante asistio a la visita programada</span></label>
        <label class="full-field">Soporte de visita remota (si aplica)<input name="support" type="file" accept=".pdf,.jpg,.jpeg,.png,.doc,.docx"></label>
        <div class="full-field notice warning">Si la visita es remota, adjunta un soporte. Si el estudiante no asistio, se registra la inasistencia para coordinar una reprogramacion.</div>
        <h3 class="full-field">Rubrica de evaluacion</h3>
        <div class="form-grid full-field" id="visit-rubric">${visitRubricFields(assignments[0]?.id)}</div>
        <button class="button primary full-field" type="submit">Guardar visita y evaluacion</button>
      </form>`;
  }
  return `<div class="empty">Formulario no disponible.</div>`;
}

function visitAssignmentDetails(assignmentId) {
  const assignment = data.assignments.find((item) => item.id === Number(assignmentId));
  if (!assignment) return `<div class="empty">Selecciona un estudiante asignado para ver la informacion de su practica.</div>`;
  const details = assignmentDetails(assignment);
  const activities = data.activities.filter((activity) => activity.assignmentId === assignment.id);
  const evidence = data.evidences.filter((item) => activities.some((activity) => activity.id === item.activityId));
  return `
    <div class="notice">
      <strong>Institucion receptora:</strong> ${escapeHtml(details.institution?.name || "No registrada")}<br>
      <strong>Actividades reportadas:</strong> ${activities.length}<br>
      <strong>Evidencias disponibles:</strong> ${evidence.map((item) => escapeHtml(item.fileName)).join(", ") || "Ninguna"}
    </div>
  `;
}

function visitRubricFields(assignmentId) {
  const assignment = data.assignments.find((item) => item.id === Number(assignmentId));
  const period = assignment && data.periods.find((item) => item.id === assignment.periodId);
  const rubric = period?.rubric || [];
  if (!rubric.length) return `<div class="notice warning full-field">No existe una rubrica configurada para el periodo. El coordinador debe configurar criterios antes de registrar la evaluacion.</div>`;
  return rubric.map((criterion, index) => `
    <label>${escapeHtml(criterion.name)} (${criterion.weight}%)
      <select name="score-${index}" required>
        <option value="">Seleccione puntaje</option>
        <option value="4">Excelente</option>
        <option value="3">Bueno</option>
        <option value="2">Aceptable</option>
        <option value="1">Insuficiente</option>
      </select>
    </label>
    <label>Comentario<input name="comment-${index}" placeholder="Comentario del criterio"></label>
  `).join("");
}

function handleSubmit(form, submitter) {
  const type = form.dataset.submit;
  const formData = Object.fromEntries(new FormData(form).entries());
  const user = currentUser();
  if (type === "period") {
    const rubric = [1, 2, 3].map((index) => ({
      name: String(formData[`criterion${index}`]).trim(),
      weight: Number(formData[`weight${index}`])
    }));
    const minimumForProgram = Math.max(0, ...data.periods
      .filter((period) => period.id !== Number(form.dataset.id) && period.status === "PUBLICADO" &&
        period.program.toLowerCase() === String(formData.program).trim().toLowerCase())
      .map((period) => Number(period.minHours)));
    const periodStatus = submitter?.value || "PUBLICADO";
    const existingPeriod = form.dataset.id ? data.periods.find((period) => period.id === Number(form.dataset.id)) : null;
    if (new Date(`${formData.start}T00:00:00`) > new Date(`${formData.end}T00:00:00`) ||
      new Date(`${formData.reportLimit}T00:00:00`) < new Date(`${formData.end}T00:00:00`)) {
      toast("Verifica las fechas: el inicio debe preceder al fin y el limite de reporte no puede ser anterior al fin.");
      return;
    }
    if (data.periods.some((period) => period.id !== existingPeriod?.id &&
      period.program.toLowerCase() === String(formData.program).trim().toLowerCase() &&
      period.year === Number(formData.year) && period.semester === Number(formData.semester))) {
      toast("Ya existe un periodo para ese programa, ano y semestre.");
      return;
    }
    if (existingPeriod && existingPeriod.status !== "BORRADOR") {
      toast("Solo se pueden editar periodos guardados como borrador.");
      return;
    }
    if (periodStatus === "PUBLICADO" && Number(formData.minHours) < minimumForProgram) {
      toast(`Las horas minimas no pueden ser inferiores al minimo anterior del programa (${minimumForProgram}).`);
      return;
    }
    if (periodStatus === "PUBLICADO" && rubric.reduce((sum, criterion) => sum + criterion.weight, 0) !== 100) {
      toast("No se puede publicar: los pesos de evaluacion deben sumar 100%.");
      return;
    }
    const periodRecord = existingPeriod || { id: nextId(data.periods) };
    Object.assign(periodRecord, {
      program: String(formData.program).trim(),
      level: String(formData.level).trim(),
      year: Number(formData.year),
      semester: Number(formData.semester),
      start: formData.start,
      end: formData.end,
      reportLimit: formData.reportLimit,
      minHours: Number(formData.minHours),
      status: periodStatus,
      rubric
    });
    if (!existingPeriod) data.periods.push(periodRecord);
    audit(existingPeriod ? `${periodStatus === "PUBLICADO" ? "Publico" : "Actualizo"} un periodo en borrador` : "Creo un periodo de practica", "PERIODO_PRACTICA");
  }
  if (type === "institution") {
    data.institutions.push({ id: nextId(data.institutions), ...formData, active: true });
    audit("Registro una institucion", "INSTITUCION");
  }
  if (type === "agreement") {
    if (formData.start > formData.end) {
      toast("La fecha de inicio del convenio debe ser anterior a su fecha de vencimiento.");
      return;
    }
    const agreement = {
      id: nextId(data.agreements),
      institutionId: Number(formData.institutionId),
      number: String(formData.number).trim(),
      start: formData.start,
      end: formData.end,
      document: String(formData.document).trim(),
      status: formData.document && formData.end >= new Date().toISOString().slice(0, 10) ? "VIGENTE" : "INACTIVA"
    };
    data.agreements.push(agreement);
    audit("Registro un convenio", "CONVENIO");
  }
  if (type === "agreement-place") {
    const document = form.querySelector('[name="document"]').files[0];
    if (formData.start > formData.end) {
      toast("La fecha de inicio del convenio debe ser anterior a su fecha de vencimiento.");
      return;
    }
    if (data.agreements.some((agreement) => agreement.number.toLowerCase() === String(formData.number).trim().toLowerCase())) {
      toast("Ya existe un convenio con ese numero.");
      return;
    }
    const agreementId = nextId(data.agreements);
    const agreement = {
      id: agreementId,
      institutionId: Number(formData.institutionId),
      number: String(formData.number).trim(),
      start: formData.start,
      end: formData.end,
      document: document?.name || "",
      status: document && formData.start <= new Date().toISOString().slice(0, 10) &&
        formData.end >= new Date().toISOString().slice(0, 10) ? "VIGENTE" : "INACTIVA"
    };
    data.agreements.push(agreement);
    data.places.push({
      id: nextId(data.places),
      agreementId,
      level: String(formData.level).trim(),
      shift: formData.shift,
      offered: Number(formData.offered),
      occupied: 0,
      tutor: String(formData.tutor).trim(),
      status: agreement.status === "VIGENTE" ? "DISPONIBLE" : "INACTIVA"
    });
    audit("Registro un convenio y sus plazas", "CONVENIO_PLAZA");
  }
  if (type === "place") {
    data.places.push({ id: nextId(data.places), agreementId: Number(formData.agreementId), level: formData.level, shift: formData.shift, offered: Number(formData.offered), occupied: 0, tutor: formData.tutor, status: "DISPONIBLE" });
    audit("Registro una plaza de practica", "PLAZA_PRACTICA");
  }
  if (type === "assignment") {
    const studentId = Number(formData.studentId);
    const teacherId = Number(formData.teacherId);
    const placeId = Number(formData.placeId);
    const periodId = Number(formData.periodId);
    const activeAssignment = data.assignments.find((assignment) =>
      assignment.studentId === studentId && assignment.periodId === periodId && assignment.status === "ACTIVA"
    );
    const id = nextId(data.assignments);
    const place = data.places.find((item) => item.id === placeId);
    const agreement = data.agreements.find((item) => item.id === place?.agreementId);
    const teacherLoad = data.assignments.filter((assignment) =>
      assignment.teacherId === teacherId && assignment.status === "ACTIVA" && assignment.id !== activeAssignment?.id
    ).length;
    if (!data.users.some((item) => item.id === studentId && item.role === "ESTUDIANTE") ||
      !data.users.some((item) => item.id === teacherId && item.role === "DOCENTE_ASESOR") ||
      !data.periods.some((item) => item.id === periodId && item.status === "PUBLICADO")) {
      toast("Selecciona un estudiante, docente asesor y periodo publicado validos.");
      return;
    }
    if (!place || placeStatus(place, agreement) !== "DISPONIBLE") {
      toast("La plaza no tiene cupos o el convenio no esta vigente y documentado.");
      return;
    }
    if (teacherLoad >= 6) {
      toast("El docente asesor ya alcanzo el maximo de seis estudiantes asignados.");
      return;
    }
    if (activeAssignment && !formData.reassignmentReason) {
      toast("El estudiante ya tiene una asignacion activa en este periodo. Selecciona un motivo para reasignarlo.");
      return;
    }
    if (activeAssignment) {
      const previousPlace = data.places.find((item) => item.id === activeAssignment.placeId);
      activeAssignment.status = "CANCELADA";
      activeAssignment.reassignmentReason = String(formData.reassignmentReason);
      if (previousPlace) previousPlace.occupied = Math.max(0, previousPlace.occupied - 1);
    }
    data.assignments.push({
      id,
      studentId,
      teacherId,
      placeId,
      periodId,
      date: new Date().toISOString().slice(0, 10),
      status: "ACTIVA",
      approvedHours: 0,
      reassignedFrom: activeAssignment?.id || null,
      reassignmentReason: activeAssignment ? String(formData.reassignmentReason) : ""
    });
    place.occupied += 1;
    audit(activeAssignment ? "Reasigno un estudiante conservando el historial" : "Creo una asignacion de practica", "ASIGNACION");
    const details = assignmentDetails(data.assignments[data.assignments.length - 1]);
    audit(`Asignacion preparada para ${details.student?.name}, docente ${details.teacher?.name} e institucion ${details.institution?.name}; verificar notificacion externa`, "NOTIFICACION");
  }
  if (type === "activity") {
    const assignment = data.assignments.find((item) => item.studentId === user.id && item.status === "ACTIVA");
    const submitStatus = submitter?.value || "PENDIENTE";
    const date = String(formData.date);
    const hours = Number(formData.hours);
    const period = assignment && data.periods.find((item) => item.id === assignment.periodId);
    const existing = form.dataset.id ? data.activities.find((item) => item.id === Number(form.dataset.id)) : null;
    const files = Array.from(form.querySelector('[name="files"]').files);
    const allowedExtensions = new Set(["pdf", "jpg", "jpeg", "png", "doc", "docx"]);
    if (!assignment || !period) {
      toast("No hay una asignacion activa para registrar esta actividad.");
      return;
    }
    if (existing && (existing.assignmentId !== assignment.id || !["DEVUELTA", "BORRADOR"].includes(existing.status))) {
      toast("Solo puedes corregir tus actividades devueltas o completar borradores.");
      return;
    }
    if (date < period.start || date > period.end || date > period.reportLimit) {
      toast(`La fecha debe estar dentro del periodo ${moneyDate(period.start)} - ${moneyDate(period.end)} y antes del cierre de reportes.`);
      return;
    }
    if (!Number.isFinite(hours) || hours < 0.5 || hours > 24) {
      toast("Las horas deben ser un valor entre 0.5 y 24.");
      return;
    }
    if (submitStatus === "PENDIENTE" && files.length === 0 && !data.evidences.some((evidence) => evidence.activityId === existing?.id)) {
      toast("Adjunta al menos un soporte antes de enviar la actividad a validacion.");
      return;
    }
    const invalidFile = files.find((file) => {
      const extension = file.name.split(".").pop()?.toLowerCase() || "";
      return !allowedExtensions.has(extension) || file.size > 10 * 1024 * 1024;
    });
    if (invalidFile) {
      toast(`El archivo ${invalidFile.name} no es valido. Usa PDF, JPG, PNG, DOC o DOCX de hasta 10 MB.`);
      return;
    }
    const activity = existing || {
      id: nextId(data.activities),
      assignmentId: assignment.id,
      observation: ""
    };
    activity.date = date;
    activity.type = String(formData.type).trim();
    activity.description = String(formData.description).trim();
    activity.hours = hours;
    activity.status = submitStatus;
    if (existing) activity.observation = "";
    else data.activities.push(activity);
    for (const file of files) {
      const extension = file.name.split(".").pop()?.toUpperCase() || "ARCHIVO";
      const evidenceId = nextId(data.evidences);
      data.evidences.push({
        id: evidenceId,
        activityId: activity.id,
        fileName: file.name,
        type: extension,
        url: `storage/sigpra/assignment-${assignment.id}/activity-${activity.id}/${encodeURIComponent(file.name)}`,
        status: "CARGADA",
        mongoId: `ev-local-${String(evidenceId).padStart(3, "0")}`
      });
    }
    audit(submitStatus === "BORRADOR" ? "Guardo actividad como borrador" : existing ? "Reenvio actividad corregida" : "Registro una actividad", "REGISTRO_ACTIVIDAD");
  }
  if (type === "evidence") {
    const file = form.querySelector('[name="file"]').files[0];
    const activity = data.activities.find((item) => item.id === Number(formData.activityId));
    const assignment = activity && data.assignments.find((item) =>
      item.id === activity.assignmentId && item.studentId === user.id && item.status === "ACTIVA"
    );
    const extension = file?.name.split(".").pop()?.toLowerCase() || "";
    if (!file || !assignment || !["pdf", "jpg", "jpeg", "png", "doc", "docx"].includes(extension) ||
      file.size > 10 * 1024 * 1024) {
      toast("Selecciona una actividad propia y un archivo permitido de hasta 10 MB.");
      return;
    }
    const id = nextId(data.evidences);
    data.evidences.push({ id, activityId: activity.id, fileName: file.name, type: extension.toUpperCase(), url: `storage/sigpra/evidencia-${id}-${encodeURIComponent(file.name)}`, status: "CARGADA", mongoId: `ev-local-${String(id).padStart(3, "0")}` });
    audit("Registro una evidencia", "EVIDENCIA");
  }
  if (type === "validate") {
    const activity = data.activities.find((item) => item.id === Number(form.dataset.id));
    const assignment = activity && data.assignments.find((item) => item.id === activity.assignmentId);
    const evidence = data.evidences.filter((item) => item.activityId === activity?.id);
    if (!activity || !assignment || assignment.teacherId !== user.id || activity.status !== "PENDIENTE") {
      toast("La actividad ya no esta pendiente o no pertenece a tus estudiantes asignados.");
      return;
    }
    if (formData.status === "APROBADA" && (!formData.evidenceReviewed || evidence.length === 0)) {
      toast("Para aprobar, confirma la revision y verifica que la actividad tenga al menos un soporte.");
      return;
    }
    if (["DEVUELTA", "RECHAZADA"].includes(String(formData.status)) && !String(formData.observation).trim()) {
      toast("Escribe la observacion obligatoria al devolver o rechazar una actividad.");
      return;
    }
    activity.status = formData.status;
    activity.observation = String(formData.observation || (formData.status === "APROBADA" ? "Actividad aprobada." : "")).trim();
    activity.validatedBy = user.id;
    activity.validatedAt = new Date().toISOString();
    evidence.forEach((item) => { item.status = formData.status === "APROBADA" ? "VALIDADA" : item.status; });
    assignment.approvedHours = approvedHours(assignment.id);
    audit(`Valido actividad como ${formData.status}`, "REGISTRO_ACTIVIDAD");
  }
  if (type === "visit") {
    const assignment = data.assignments.find((item) => item.id === Number(formData.assignmentId) &&
      item.teacherId === user.id && item.status === "ACTIVA");
    const period = assignment && data.periods.find((item) => item.id === assignment.periodId);
    const rubric = period?.rubric || [];
    const support = form.querySelector('[name="support"]').files[0];
    if (!assignment || !period) {
      toast("Selecciona una asignacion activa propia para registrar la visita.");
      return;
    }
    if (formData.date < period.start || formData.date > period.end) {
      toast("La fecha de la visita debe estar dentro del periodo de practica.");
      return;
    }
    if (!rubric.length) {
      toast("No se puede guardar la evaluacion porque el periodo no tiene una rubrica configurada.");
      return;
    }
    const ratings = rubric.map((criterion, index) => ({
      criterion: criterion.name,
      weight: Number(criterion.weight),
      score: Number(formData[`score-${index}`]),
      comment: String(formData[`comment-${index}`] || "").trim()
    }));
    if (ratings.some((rating) => !Number.isFinite(rating.score) || rating.score < 1 || rating.score > 4) ||
      rubric.reduce((sum, criterion) => sum + Number(criterion.weight), 0) !== 100) {
      toast("Completa todos los criterios y verifica que la rubrica del periodo sume 100%.");
      return;
    }
    if (formData.mode === "VIRTUAL" && !support) {
      toast("Adjunta el soporte requerido para una visita remota.");
      return;
    }
    if (support && (support.size > 10 * 1024 * 1024 ||
      !["pdf", "jpg", "jpeg", "png", "doc", "docx"].includes(support.name.split(".").pop()?.toLowerCase() || ""))) {
      toast("El soporte debe ser PDF, imagen, DOC o DOCX de hasta 10 MB.");
      return;
    }
    const weightedScore = ratings.reduce((sum, rating) => sum + rating.score * rating.weight, 0) / 100;
    const concept = weightedScore >= 3.5 ? "Excelente" : weightedScore >= 2.5 ? "Bueno" : weightedScore >= 1.5 ? "Aceptable" : "Insuficiente";
    data.visits.push({
      id: nextId(data.visits),
      assignmentId: assignment.id,
      teacherId: user.id,
      date: formData.date,
      mode: formData.mode,
      attendance: form.querySelector('[name="attendance"]').checked,
      observation: String(formData.observation).trim(),
      score: Math.round(weightedScore * 100) / 100,
      concept,
      rubric: ratings,
      supportFileName: support?.name || "",
      status: "REGISTRADA"
    });
    audit("Registro visita y evaluacion", "VISITA_SEGUIMIENTO");
    if (!form.querySelector('[name="attendance"]').checked) {
      audit("Registro inasistencia del estudiante y requiere reprogramacion", "VISITA_SEGUIMIENTO");
    }
  }
  if (type === "report-filters") {
    reportFilters = {
      periodId: String(formData.periodId || ""),
      program: String(formData.program || ""),
      institutionId: String(formData.institutionId || ""),
      teacherId: String(formData.teacherId || "")
    };
    audit(`Aplico filtros al consolidado (${JSON.stringify(reportFilters)})`, "CONSULTA_CONSOLIDADA");
    saveData();
    toast("Filtros aplicados; consolidado actualizado.");
    render();
    return;
  }
  saveData();
  document.querySelector(".modal-backdrop")?.remove();
  toast("Operacion guardada correctamente.");
  render();
}

function exportJson() {
  downloadFile("sigpra-datos.json", JSON.stringify(data, null, 2), "application/json");
  audit("Exporto datos JSON", "REPORTE");
  saveData();
}

function exportCsv() {
  const header = ["estudiante", "institucion", "docente", "horas_aprobadas", "estado"];
  const assignments = filteredReportAssignments();
  const rows = assignments.map((assignment) => {
    const d = assignmentDetails(assignment);
    return [d.student?.name, d.institution?.name, d.teacher?.name, approvedHours(assignment.id) || assignment.approvedHours, assignment.status];
  });
  const csv = [header, ...rows].map((row) => row.map((cell) => `"${String(cell ?? "").replaceAll('"', '""')}"`).join(",")).join("\n");
  downloadFile("sigpra-consolidado.csv", csv, "text/csv");
  audit(`Exporto consolidado CSV (${JSON.stringify(reportFilters)})`, "REPORTE");
  saveData();
}

function downloadFile(name, content, type) {
  const blob = new Blob([content], { type });
  const url = URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = url;
  link.download = name;
  link.click();
  URL.revokeObjectURL(url);
}

document.addEventListener("submit", (event) => {
  if (event.target.id === "login-form") {
    event.preventDefault();
    const formData = Object.fromEntries(new FormData(event.target).entries());
    const user = data.users.find((item) => item.email === formData.email && item.password === formData.password);
    if (!user) {
      toast("Correo o clave incorrectos.");
      return;
    }
    currentView = "dashboard";
    saveSession(user);
    render();
    return;
  }
  if (event.target.dataset.submit) {
    event.preventDefault();
    handleSubmit(event.target, event.submitter);
  }
});

document.addEventListener("change", (event) => {
  if (event.target.matches('[data-submit="visit"] [name="assignmentId"]')) {
    const form = event.target.closest("form");
    const rubric = form.querySelector("#visit-rubric");
    const details = form.querySelector("#visit-assignment-details");
    rubric.innerHTML = visitRubricFields(event.target.value);
    details.innerHTML = visitAssignmentDetails(event.target.value);
    return;
  }
  const assignmentForm = event.target.closest('[data-submit="assignment"]');
  if (assignmentForm) {
    updateAssignmentWorkflow(assignmentForm);
    return;
  }
  if (event.target.id !== "role-switch") return;
  const role = event.target.value;
  const user = data.users.find((item) => item.role === role);
  if (!user) {
    toast("No hay una cuenta de demostracion disponible para ese rol.");
    render();
    return;
  }
  currentView = "dashboard";
  saveSession(user);
  render();
});

document.addEventListener("click", (event) => {
  const login = event.target.closest("[data-login]");
  if (login) {
    const user = data.users.find((item) => item.email === login.dataset.login);
    saveSession(user);
    currentView = "dashboard";
    render();
    return;
  }

  const view = event.target.closest("[data-view]");
  if (view) {
    currentView = view.dataset.view;
    render();
    return;
  }

  const modal = event.target.closest("[data-modal]");
  if (modal) {
    openModal(modal.dataset.modal, modal.dataset.id);
    return;
  }

  const action = event.target.closest("[data-action]");
  if (!action) return;

  if (action.dataset.action === "logout") {
    saveSession(null);
    render();
  }
  if (action.dataset.action === "close-modal") {
    document.querySelector(".modal-backdrop")?.remove();
  }
  if (action.dataset.action === "reset-demo") {
    data = structuredClone(seedData);
    saveData();
    toast("Datos demo restaurados.");
    render();
  }
  if (action.dataset.action === "export-json") exportJson();
  if (action.dataset.action === "export-csv") exportCsv();
  if (action.dataset.action === "export-print") {
    audit(`Preparo impresion PDF del consolidado (${JSON.stringify(reportFilters)})`, "REPORTE");
    saveData();
    window.print();
  }
});

document.addEventListener("keydown", (event) => {
  if (event.key === "Escape") document.querySelector(".modal-backdrop")?.remove();
});

render();
