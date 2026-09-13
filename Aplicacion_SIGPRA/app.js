const STORAGE_KEY = "sigpra_mvp_data_v1";
const SESSION_KEY = "sigpra_mvp_session_v1";

const seedData = {
  users: [
    { id: 1, name: "Rafael Carreno", email: "coordinador@sigpra.edu.co", password: "123456", role: "COORDINADOR", initials: "RC" },
    { id: 2, name: "Laura Gomez", email: "estudiante@sigpra.edu.co", password: "123456", role: "ESTUDIANTE", initials: "LG", code: "UDI-2026-001", semester: 8 },
    { id: 3, name: "Martha Rodriguez", email: "docente@sigpra.edu.co", password: "123456", role: "DOCENTE_ASESOR", initials: "MR" },
    { id: 4, name: "Director Programa", email: "director@sigpra.edu.co", password: "123456", role: "DIRECTOR", initials: "DP" },
    { id: 5, name: "Miguel Rios", email: "miguel@sigpra.edu.co", password: "123456", role: "ESTUDIANTE", initials: "MR", code: "UDI-2026-002", semester: 8 },
    { id: 6, name: "Carlos Mendoza", email: "docente2@sigpra.edu.co", password: "123456", role: "DOCENTE_ASESOR", initials: "CM" }
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

const app = document.querySelector("#app");

function loadData() {
  const saved = localStorage.getItem(STORAGE_KEY);
  if (!saved) return structuredClone(seedData);
  try {
    return JSON.parse(saved);
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
        <nav class="nav" aria-label="Navegacion de ${escapeHtml(roleLabel(user.role))}">
          ${nav.map((item) => `<button class="${item.id === currentView ? "active" : ""}" data-view="${item.id}">${item.label}</button>`).join("")}
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
            <h1>${escapeHtml(viewTitle(currentView))}</h1>
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
  const shared = [{ id: "dashboard", label: "Inicio" }];
  const map = {
    COORDINADOR: [
      ...shared,
      { id: "periods", label: "Periodos" },
      { id: "agreements", label: "Convenios y plazas" },
      { id: "assignments", label: "Asignaciones" },
      { id: "reports", label: "Consolidado" },
      { id: "audit", label: "Auditoria" }
    ],
    ESTUDIANTE: [
      ...shared,
      { id: "student-activities", label: "Actividades" },
      { id: "student-evidence", label: "Evidencias" },
      { id: "student-progress", label: "Mi avance" }
    ],
    DOCENTE_ASESOR: [
      ...shared,
      { id: "teacher-validations", label: "Validar actividades" },
      { id: "teacher-visits", label: "Visitas y evaluacion" },
      { id: "reports", label: "Consolidado" }
    ],
    DIRECTOR: [
      ...shared,
      { id: "reports", label: "Consolidado" },
      { id: "audit", label: "Auditoria" }
    ]
  };
  return map[role] || shared;
}

function viewTitle(view) {
  return {
    dashboard: "Panel principal",
    periods: "Programar periodo de practica",
    agreements: "Gestionar convenios y plazas",
    assignments: "Asignar practicante",
    reports: "Estado consolidado",
    audit: "Auditoria",
    "student-activities": "Registrar actividades",
    "student-evidence": "Evidencias y soportes",
    "student-progress": "Avance de practica",
    "teacher-validations": "Validar actividades reportadas",
    "teacher-visits": "Visitas y evaluacion"
  }[view] || "SIGPRA";
}

function viewSubtitle(view, role) {
  return {
    dashboard: `Sesion activa como ${roleLabel(role)}.`,
    periods: "Define fechas, horas minimas y criterios de evaluacion.",
    agreements: "Administra instituciones receptoras, convenios y cupos.",
    assignments: "Vincula estudiante, plaza, docente asesor y periodo.",
    reports: "Consulta horas, estados, evidencias y visitas.",
    audit: "Revisa acciones importantes registradas por el sistema.",
    "student-activities": "Registra actividades ejecutadas y horas reportadas.",
    "student-evidence": "Asocia soportes a las actividades reportadas.",
    "student-progress": "Consulta tu avance frente al periodo asignado.",
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
        <h2>Iniciar sesion</h2>
        <p class="muted">Usa una cuenta de prueba o ingresa correo y clave.</p>
        <form id="login-form" class="form-grid">
          <label class="full-field">Correo
            <input name="email" type="email" value="coordinador@sigpra.edu.co" required />
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
  const totalAssignments = data.assignments.length;
  const pending = data.activities.filter((activity) => activity.status === "PENDIENTE").length;
  const approved = data.activities.filter((activity) => activity.status === "APROBADA").reduce((sum, item) => sum + Number(item.hours), 0);
  const places = data.places.reduce((sum, place) => sum + (place.offered - place.occupied), 0);

  if (user.role === "ESTUDIANTE") return renderStudentProgress(user, true);
  if (user.role === "DOCENTE_ASESOR") return renderTeacherHome(user);

  return `
    <section class="grid">
      ${metricCard("Practicas activas", totalAssignments, "Asignaciones registradas")}
      ${metricCard("Horas aprobadas", approved, "Horas validadas por docentes")}
      ${metricCard("Pendientes", pending, "Actividades por validar")}
      ${metricCard("Cupos disponibles", places, "Plazas abiertas")}
      <article class="card wide">
        <h2>Ultimas asignaciones</h2>
        ${assignmentsTable(data.assignments.slice(0, 5))}
      </article>
      <article class="card">
        <h2>Acciones rapidas</h2>
        <div class="toolbar">
          <button class="button primary" data-view="periods">Crear periodo</button>
          <button class="button" data-view="agreements">Ver convenios</button>
          <button class="button" data-view="reports">Consolidado</button>
        </div>
      </article>
    </section>
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
    <section class="grid">
      <article class="card full">
        <div class="toolbar">
          <button class="button primary" data-modal="period">Nuevo periodo</button>
          <button class="button" data-action="reset-demo">Restaurar datos demo</button>
        </div>
        <div class="table-wrap">
          <table>
            <thead><tr><th>Programa</th><th>Nivel</th><th>Periodo</th><th>Fechas</th><th>Horas</th><th>Estado</th></tr></thead>
            <tbody>
              ${data.periods.map((period) => `
                <tr>
                  <td>${escapeHtml(period.program)}</td>
                  <td>${escapeHtml(period.level)}</td>
                  <td>${period.year}-${period.semester}</td>
                  <td>${moneyDate(period.start)} - ${moneyDate(period.end)}</td>
                  <td>${period.minHours}</td>
                  <td>${statusTag(period.status)}</td>
                </tr>
              `).join("")}
            </tbody>
          </table>
        </div>
      </article>
    </section>
  `;
}

function renderAgreements() {
  return `
    <section class="grid">
      <article class="card full">
        <div class="toolbar">
          <button class="button primary" data-modal="institution">Nueva institucion</button>
          <button class="button" data-modal="agreement">Nuevo convenio</button>
          <button class="button" data-modal="place">Nueva plaza</button>
        </div>
        <h2>Instituciones y convenios</h2>
        <div class="table-wrap">
          <table>
            <thead><tr><th>Institucion</th><th>Convenio</th><th>Vigencia</th><th>Plazas</th><th>Cupos</th><th>Estado</th></tr></thead>
            <tbody>
              ${data.agreements.map((agreement) => {
                const institution = data.institutions.find((item) => item.id === agreement.institutionId);
                const places = data.places.filter((place) => place.agreementId === agreement.id);
                const cupos = places.reduce((sum, place) => sum + (place.offered - place.occupied), 0);
                return `
                  <tr>
                    <td>${escapeHtml(institution?.name)}</td>
                    <td>${escapeHtml(agreement.number)}</td>
                    <td>${moneyDate(agreement.start)} - ${moneyDate(agreement.end)}</td>
                    <td>${places.length}</td>
                    <td>${cupos}</td>
                    <td>${statusTag(agreement.status)}</td>
                  </tr>
                `;
              }).join("")}
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
                    <td>${statusTag(place.status)}</td>
                  </tr>
                `;
              }).join("")}
            </tbody>
          </table>
        </div>
      </article>
    </section>
  `;
}

function renderAssignments() {
  return `
    <section class="grid">
      <article class="card full">
        <div class="toolbar">
          <button class="button primary" data-modal="assignment">Nueva asignacion</button>
        </div>
        ${assignmentsTable(data.assignments)}
      </article>
    </section>
  `;
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
    <section class="grid">
      <article class="card full">
        <div class="toolbar">
          <button class="button primary" data-modal="activity">Nueva actividad</button>
        </div>
        ${activitiesTable(activities)}
      </article>
    </section>
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
    <section class="grid">
      <article class="card full">
        <div class="toolbar">
          <button class="button primary" data-modal="evidence">Registrar evidencia</button>
        </div>
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

function renderStudentProgress(user, compact = false) {
  const assignment = data.assignments.find((item) => item.studentId === user.id && item.status === "ACTIVA");
  if (!assignment) return `<div class="empty">No tienes una asignacion activa.</div>`;
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
  `;
}

function renderTeacherValidations(user) {
  const assignments = data.assignments.filter((assignment) => assignment.teacherId === user.id).map((assignment) => assignment.id);
  const activities = data.activities.filter((activity) => assignments.includes(activity.assignmentId));
  return `<section class="grid"><article class="card full">${activitiesTable(activities, true)}</article></section>`;
}

function renderTeacherVisits(user) {
  const assignments = data.assignments.filter((assignment) => assignment.teacherId === user.id);
  return `
    <section class="grid">
      <article class="card full">
        <div class="toolbar">
          <button class="button primary" data-modal="visit">Registrar visita</button>
        </div>
        <div class="table-wrap">
          <table>
            <thead><tr><th>Fecha</th><th>Estudiante</th><th>Modalidad</th><th>Puntaje</th><th>Observacion</th><th>Estado</th></tr></thead>
            <tbody>
              ${data.visits.filter((visit) => visit.teacherId === user.id).map((visit) => {
                const assignment = assignments.find((item) => item.id === visit.assignmentId);
                const d = assignmentDetails(assignment || {});
                return `<tr><td>${moneyDate(visit.date)}</td><td>${escapeHtml(d.student?.name)}</td><td>${escapeHtml(visit.mode)}</td><td>${visit.score}</td><td>${escapeHtml(visit.observation)}</td><td>${statusTag(visit.status)}</td></tr>`;
              }).join("") || `<tr><td colspan="6">No hay visitas registradas.</td></tr>`}
            </tbody>
          </table>
        </div>
      </article>
    </section>
  `;
}

function renderReports(user) {
  const rows = data.assignments.map((assignment) => {
    const d = assignmentDetails(assignment);
    const hours = approvedHours(assignment.id) || assignment.approvedHours;
    const required = d.period?.minHours || 320;
    return { assignment, d, hours, required, percent: Math.round((hours / required) * 100) };
  });
  return `
    <section class="grid">
      <article class="card full">
        <div class="toolbar">
          <button class="button primary" data-action="export-json">Exportar JSON</button>
          <button class="button" data-action="export-csv">Exportar CSV</button>
        </div>
        <div class="report-box">
          <article><strong>${rows.length}</strong><br><span class="muted">Practicas</span></article>
          <article><strong>${rows.reduce((sum, row) => sum + row.hours, 0)}</strong><br><span class="muted">Horas aprobadas</span></article>
          <article><strong>${data.activities.filter((item) => item.status === "PENDIENTE").length}</strong><br><span class="muted">Pendientes</span></article>
          <article><strong>${data.visits.length}</strong><br><span class="muted">Visitas</span></article>
        </div>
        <div class="table-wrap">
          <table>
            <thead><tr><th>Estudiante</th><th>Institucion</th><th>Docente</th><th>Horas</th><th>Avance</th><th>Estado</th></tr></thead>
            <tbody>
              ${rows.map((row) => `
                <tr>
                  <td>${escapeHtml(row.d.student?.name)}</td>
                  <td>${escapeHtml(row.d.institution?.name)}</td>
                  <td>${escapeHtml(row.d.teacher?.name)}</td>
                  <td>${row.hours} / ${row.required}</td>
                  <td><div class="progress"><span style="width:${Math.min(100, row.percent)}%"></span></div>${row.percent}%</td>
                  <td>${statusTag(row.assignment.status)}</td>
                </tr>
              `).join("")}
            </tbody>
          </table>
        </div>
      </article>
    </section>
  `;
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

function optionList(items, labelFn) {
  return items.map((item) => `<option value="${item.id}">${escapeHtml(labelFn(item))}</option>`).join("");
}

function modalForm(type, id) {
  const user = currentUser();
  if (type === "period") {
    return `
      <form class="form-grid" data-submit="period">
        <label>Programa<input name="program" value="Licenciatura en Educacion Infantil" required></label>
        <label>Nivel<input name="level" value="VIII semestre" required></label>
        <label>Anio<input name="year" type="number" value="2026" required></label>
        <label>Semestre<select name="semester"><option value="1">1</option><option value="2" selected>2</option></select></label>
        <label>Fecha inicio<input name="start" type="date" required></label>
        <label>Fecha fin<input name="end" type="date" required></label>
        <label>Limite reportes<input name="reportLimit" type="date" required></label>
        <label>Horas minimas<input name="minHours" type="number" value="320" required></label>
        <button class="button primary full-field" type="submit">Guardar periodo</button>
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
    return `
      <form class="form-grid" data-submit="assignment">
        <label>Estudiante<select name="studentId">${optionList(studentUsers(), (item) => `${item.name} - ${item.code || item.email}`)}</select></label>
        <label>Docente asesor<select name="teacherId">${optionList(teacherUsers(), (item) => item.name)}</select></label>
        <label>Plaza<select name="placeId">${optionList(data.places, (item) => `${item.level} / cupos ${item.offered - item.occupied}`)}</select></label>
        <label>Periodo<select name="periodId">${optionList(data.periods, (item) => `${item.program} ${item.year}-${item.semester}`)}</select></label>
        <button class="button primary full-field" type="submit">Confirmar asignacion</button>
      </form>`;
  }
  if (type === "activity") {
    return `
      <form class="form-grid" data-submit="activity">
        <label>Fecha<input name="date" type="date" required></label>
        <label>Horas<input name="hours" type="number" min="0.5" max="24" step="0.5" required></label>
        <label class="full-field">Tipo de actividad<input name="type" required></label>
        <label class="full-field">Descripcion<textarea name="description" required></textarea></label>
        <button class="button primary full-field" type="submit">Enviar a validacion</button>
      </form>`;
  }
  if (type === "evidence") {
    const assignment = data.assignments.find((item) => item.studentId === user.id && item.status === "ACTIVA");
    const activities = assignment ? data.activities.filter((item) => item.assignmentId === assignment.id) : [];
    return `
      <form class="form-grid" data-submit="evidence">
        <label>Actividad<select name="activityId">${optionList(activities, (item) => `${item.date} - ${item.type}`)}</select></label>
        <label>Tipo<select name="type"><option>PDF</option><option>JPG</option><option>PNG</option><option>DOCX</option></select></label>
        <label class="full-field">Nombre del archivo<input name="fileName" placeholder="planeacion_clase.pdf" required></label>
        <button class="button primary full-field" type="submit">Registrar evidencia</button>
      </form>`;
  }
  if (type === "validate") {
    const activity = data.activities.find((item) => item.id === Number(id));
    return `
      <form class="form-grid" data-submit="validate" data-id="${id}">
        <p class="full-field"><strong>${escapeHtml(activity?.type)}</strong><br>${escapeHtml(activity?.description)}</p>
        <label>Resultado<select name="status"><option value="APROBADA">Aprobar</option><option value="DEVUELTA">Devolver</option><option value="RECHAZADA">Rechazar</option></select></label>
        <label>Observacion<input name="observation" placeholder="Observacion para el estudiante"></label>
        <button class="button primary full-field" type="submit">Guardar validacion</button>
      </form>`;
  }
  if (type === "visit") {
    const assignments = data.assignments.filter((item) => item.teacherId === user.id);
    return `
      <form class="form-grid" data-submit="visit">
        <label>Asignacion<select name="assignmentId">${optionList(assignments, (item) => assignmentDetails(item).student?.name || item.id)}</select></label>
        <label>Fecha<input name="date" type="date" required></label>
        <label>Modalidad<select name="mode"><option>PRESENCIAL</option><option>VIRTUAL</option><option>MIXTA</option></select></label>
        <label>Puntaje<input name="score" type="number" min="0" max="5" step="0.1" required></label>
        <label class="full-field">Observacion<textarea name="observation" required></textarea></label>
        <button class="button primary full-field" type="submit">Guardar visita y evaluacion</button>
      </form>`;
  }
  return `<div class="empty">Formulario no disponible.</div>`;
}

function handleSubmit(form) {
  const type = form.dataset.submit;
  const formData = Object.fromEntries(new FormData(form).entries());
  const user = currentUser();
  if (type === "period") {
    data.periods.push({ id: nextId(data.periods), ...formData, year: Number(formData.year), semester: Number(formData.semester), minHours: Number(formData.minHours), status: "PUBLICADO", rubric: [] });
    audit("Creo un periodo de practica", "PERIODO_PRACTICA");
  }
  if (type === "institution") {
    data.institutions.push({ id: nextId(data.institutions), ...formData, active: true });
    audit("Registro una institucion", "INSTITUCION");
  }
  if (type === "agreement") {
    data.agreements.push({ id: nextId(data.agreements), institutionId: Number(formData.institutionId), number: formData.number, start: formData.start, end: formData.end, document: formData.document, status: "VIGENTE" });
    audit("Registro un convenio", "CONVENIO");
  }
  if (type === "place") {
    data.places.push({ id: nextId(data.places), agreementId: Number(formData.agreementId), level: formData.level, shift: formData.shift, offered: Number(formData.offered), occupied: 0, tutor: formData.tutor, status: "DISPONIBLE" });
    audit("Registro una plaza de practica", "PLAZA_PRACTICA");
  }
  if (type === "assignment") {
    const id = nextId(data.assignments);
    const place = data.places.find((item) => item.id === Number(formData.placeId));
    if (place && place.occupied >= place.offered) {
      toast("La plaza seleccionada no tiene cupos disponibles.");
      return;
    }
    data.assignments.push({ id, studentId: Number(formData.studentId), teacherId: Number(formData.teacherId), placeId: Number(formData.placeId), periodId: Number(formData.periodId), date: new Date().toISOString().slice(0, 10), status: "ACTIVA", approvedHours: 0 });
    if (place) place.occupied += 1;
    audit("Creo una asignacion de practica", "ASIGNACION");
  }
  if (type === "activity") {
    const assignment = data.assignments.find((item) => item.studentId === user.id && item.status === "ACTIVA");
    data.activities.push({ id: nextId(data.activities), assignmentId: assignment.id, date: formData.date, type: formData.type, description: formData.description, hours: Number(formData.hours), status: "PENDIENTE", observation: "" });
    audit("Registro una actividad", "REGISTRO_ACTIVIDAD");
  }
  if (type === "evidence") {
    const id = nextId(data.evidences);
    data.evidences.push({ id, activityId: Number(formData.activityId), fileName: formData.fileName, type: formData.type, url: `storage/sigpra/evidencia-${id}-${formData.fileName}`, status: "CARGADA", mongoId: `ev-local-${String(id).padStart(3, "0")}` });
    audit("Registro una evidencia", "EVIDENCIA");
  }
  if (type === "validate") {
    const activity = data.activities.find((item) => item.id === Number(form.dataset.id));
    activity.status = formData.status;
    activity.observation = formData.observation || (formData.status === "APROBADA" ? "Actividad aprobada." : "Requiere ajuste.");
    const assignment = data.assignments.find((item) => item.id === activity.assignmentId);
    assignment.approvedHours = approvedHours(assignment.id);
    audit(`Valido actividad como ${formData.status}`, "REGISTRO_ACTIVIDAD");
  }
  if (type === "visit") {
    data.visits.push({ id: nextId(data.visits), assignmentId: Number(formData.assignmentId), teacherId: user.id, date: formData.date, mode: formData.mode, attendance: true, observation: formData.observation, score: Number(formData.score), status: "REGISTRADA" });
    audit("Registro visita y evaluacion", "VISITA_SEGUIMIENTO");
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
  const rows = data.assignments.map((assignment) => {
    const d = assignmentDetails(assignment);
    return [d.student?.name, d.institution?.name, d.teacher?.name, approvedHours(assignment.id) || assignment.approvedHours, assignment.status];
  });
  const csv = [header, ...rows].map((row) => row.map((cell) => `"${String(cell ?? "").replaceAll('"', '""')}"`).join(",")).join("\n");
  downloadFile("sigpra-consolidado.csv", csv, "text/csv");
  audit("Exporto consolidado CSV", "REPORTE");
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
    handleSubmit(event.target);
  }
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
});

document.addEventListener("keydown", (event) => {
  if (event.key === "Escape") document.querySelector(".modal-backdrop")?.remove();
});

render();
