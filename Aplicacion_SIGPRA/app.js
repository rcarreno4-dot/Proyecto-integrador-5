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
      { id: "teacher-visits", label: "Visitas y evaluacion" }
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
    dashboard: "Resumen",
    periods: "CU-01 Programar periodo de practica",
    agreements: "CU-02 Gestionar convenios y plazas",
    assignments: "CU-03 Asignar practicante a plaza y docente asesor",
    reports: "CU-07 Consultar estado consolidado de practicas",
    audit: "Auditoria",
    "student-activities": "CU-04 Registrar actividad ejecutada y soportes",
    "student-evidence": "Evidencias y soportes",
    "student-progress": "Avance de practica",
    "teacher-validations": "CU-05 Validar actividades reportadas",
    "teacher-visits": "CU-06 Realizar visita de acompanamiento y evaluar"
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
  const totalAssignments = data.assignments.length;
  const pending = data.activities.filter((activity) => activity.status === "PENDIENTE").length;
  const approved = data.activities.filter((activity) => activity.status === "APROBADA").reduce((sum, item) => sum + Number(item.hours), 0);
  const places = data.places.reduce((sum, place) => sum + (place.offered - place.occupied), 0);

  if (user.role === "ESTUDIANTE") return renderStudentProgress(user, true);
  if (user.role === "DOCENTE_ASESOR") return renderTeacherHome(user);
  if (user.role === "DIRECTOR") return renderDirectorHome();

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

function renderDirectorHome() {
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
    <section class="grid">
      <article class="card full">
        <div class="toolbar">
          <button class="button primary" data-modal="institution">Nueva institucion</button>
          <button class="button" data-modal="agreement-place">Registrar convenio y plazas</button>
        </div>
        <h2>Instituciones y convenios</h2>
        <p class="muted">Los convenios vencidos o sin documento quedan inactivos; sus plazas no se ofrecen para asignacion.</p>
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
  const activities = data.activities.filter((activity) => assignments.includes(activity.assignmentId) && activity.status === "PENDIENTE");
  return `<section class="grid"><article class="card full"><h2>Actividades pendientes</h2>${activitiesTable(activities, true)}</article></section>`;
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
    return `
      <form class="form-grid" data-submit="period" data-id="${period?.id || ""}">
        <label>Programa academico<input name="program" value="${escapeHtml(period?.program || "Licenciatura en Educacion Infantil")}" required></label>
        <label>Nivel de practica<input name="level" value="${escapeHtml(period?.level || "VIII semestre")}" required></label>
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
      <form class="form-grid" data-submit="activity" data-id="${id || ""}">
        ${activity?.observation ? `<div class="notice warning full-field">Observacion del docente: ${escapeHtml(activity.observation)}</div>` : ""}
        <label>Fecha<input name="date" type="date" value="${escapeHtml(activity?.date || "")}" required></label>
        <label>Horas<input name="hours" type="number" min="0.5" max="24" step="0.5" value="${escapeHtml(activity?.hours || "")}" required></label>
        <label class="full-field">Tipo de actividad<input name="type" value="${escapeHtml(activity?.type || "")}" required></label>
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
      <form class="form-grid" data-submit="evidence">
        <label>Actividad<select name="activityId">${optionList(activities, (item) => `${item.date} - ${item.type}`)}</select></label>
        <label>Tipo<select name="type"><option>PDF</option><option>JPG</option><option>PNG</option><option>DOCX</option></select></label>
        <label class="full-field">Nombre del archivo<input name="fileName" placeholder="planeacion_clase.pdf" required></label>
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
      <form class="form-grid" data-submit="visit">
        <label>Estudiante a evaluar<select name="assignmentId" required><option value="">Seleccione estudiante</option>${optionList(assignments, (item) => `${assignmentDetails(item).student?.name} - ${assignmentDetails(item).institution?.name}`, assignments[0]?.id)}</select></label>
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
    const id = nextId(data.evidences);
    data.evidences.push({ id, activityId: Number(formData.activityId), fileName: formData.fileName, type: formData.type, url: `storage/sigpra/evidencia-${id}-${formData.fileName}`, status: "CARGADA", mongoId: `ev-local-${String(id).padStart(3, "0")}` });
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
    const rubric = event.target.closest("form").querySelector("#visit-rubric");
    rubric.innerHTML = visitRubricFields(event.target.value);
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
