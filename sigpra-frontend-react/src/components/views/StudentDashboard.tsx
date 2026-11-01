import React from "react";
import { useSigpra } from "../../context/SigpraContext";

export const StudentDashboard: React.FC = () => {
  const { currentUser, assignments, activities, periods, places, institutions, users, requests, setCurrentView } = useSigpra();

  const activeAssignment = assignments.find(
    a => a.studentId === currentUser.id && a.status === "ACTIVA"
  );

  const studentReq = requests.find(r => r.studentId === currentUser.id);

  // Caso 1: Estudiante con práctica activa en curso
  if (activeAssignment) {
    const period = periods.find(p => p.id === activeAssignment.periodId);
    const place = places.find(p => p.id === activeAssignment.placeId);
    const institution = institutions.find(i => i.id === place?.agreementId);
    const teacher = users.find(u => u.id === activeAssignment.teacherId);

    const studentActivities = activities.filter(a => a.assignmentId === activeAssignment.id);
    const approvedHours = studentActivities
      .filter(a => a.status === "APROBADA")
      .reduce((sum, a) => sum + a.hours, 0) || activeAssignment.approvedHours;

    const requiredHours = period?.minHours || 160;
    const progressPercent = Math.min(100, Math.round((approvedHours / requiredHours) * 100));

    return (
      <div>
        {/* Banner de Práctica en Curso (Sin botón de solicitar práctica) */}
        <div className="card p-4 border-start border-4 border-primary rounded-3 shadow-xs mb-4 bg-white">
          <div className="d-flex justify-content-between align-items-center flex-wrap gap-2">
            <div>
              <span className="badge bg-success mb-2 px-3 py-1">PRÁCTICA EN EJECUCIÓN</span>
              <h3 className="fw-bold mb-1" style={{ color: "#004b87" }}>
                ¡Bienvenido a tu panel de práctica, {currentUser.name}!
              </h3>
              <p className="text-muted small mb-0">
                Centro: <strong>{institution?.name}</strong> · Tutor: <strong>{place?.tutor}</strong> · Asesor UDI: <strong>{teacher?.name}</strong>
              </p>
            </div>
            <button onClick={() => setCurrentView("student-activities")} className="btn btn-primary fw-bold shadow-xs">
              📝 Registrar en Bitácora ➔
            </button>
          </div>
        </div>

        {/* Barra de Progreso de 160 Horas (Decreto 1075) */}
        <div className="card p-4 border rounded-3 shadow-xs mb-4 bg-white">
          <div className="d-flex justify-content-between align-items-center mb-2">
            <div>
              <h5 className="fw-bold mb-0">Avance Reglamentario de Práctica (Decreto 1075)</h5>
              <small className="text-muted">Meta obligatoria: {requiredHours} horas de formación</small>
            </div>
            <div className="text-end">
              <span className="fs-4 fw-bold text-primary">{approvedHours}</span> / {requiredHours} hrs
            </div>
          </div>
          <div className="progress" style={{ height: 16 }}>
            <div
              className={`progress-bar ${progressPercent >= 100 ? "bg-success" : "bg-primary"}`}
              style={{ width: `${progressPercent}%` }}
            />
          </div>
          <div className="d-flex justify-content-between align-items-center mt-2 small text-muted">
            <span>{progressPercent}% completado</span>
            <span>{Math.max(0, requiredHours - approvedHours)} horas restantes para culminar</span>
          </div>
        </div>

        {/* Últimas Actividades de Bitácora */}
        <div className="card border rounded-3 shadow-xs bg-white">
          <div className="card-header bg-white py-3 d-flex justify-content-between align-items-center">
            <h6 className="mb-0 fw-bold">Tus Reportes de Bitácora Recientes</h6>
            <button onClick={() => setCurrentView("student-activities")} className="btn btn-sm btn-outline-primary">
              Ver bitácora completa ➔
            </button>
          </div>
          <div className="table-responsive">
            <table className="table table-hover align-middle mb-0">
              <thead className="table-light">
                <tr>
                  <th>Fecha</th>
                  <th>Tipo de Actividad</th>
                  <th>Descripción</th>
                  <th>Horas</th>
                  <th>Estado</th>
                </tr>
              </thead>
              <tbody>
                {studentActivities.length === 0 ? (
                  <tr>
                    <td colSpan={5} className="text-center py-3 text-muted">
                      No has registrado actividades aún. Haz clic en "Registrar en Bitácora" para reportar tus horas.
                    </td>
                  </tr>
                ) : (
                  studentActivities.slice(0, 5).map(act => (
                    <tr key={act.id}>
                      <td className="text-muted small">{act.date}</td>
                      <td><strong>{act.type}</strong></td>
                      <td className="small">{act.description}</td>
                      <td><span className="fw-bold">{act.hours}h</span></td>
                      <td>
                        <span className={`badge ${act.status === "APROBADA" ? "bg-success" : "bg-warning text-dark"}`}>
                          {act.status}
                        </span>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    );
  }

  // Caso 2: Estudiante sin práctica pero con solicitud radicada
  if (studentReq) {
    const isApproved = studentReq.status === "APROBADA";
    const isPending = studentReq.status === "PENDIENTE_REVISION";
    return (
      <div className="card p-4 border rounded-3 shadow-xs bg-white">
        <h4 className="fw-bold text-primary mb-3">Estado de tu Trámite de Solicitud</h4>
        {isApproved ? (
          <div className="alert alert-success d-flex align-items-center gap-3">
            <span className="fs-2">🎉</span>
            <div>
              <h5 className="alert-heading mb-1">¡Solicitud Aprobada y Estudiante Habilitado!</h5>
              <p className="mb-0">
                Cumples con todos los prerrequisitos. La Coordinación de Prácticas se encuentra asignando tu plaza.
              </p>
            </div>
          </div>
        ) : isPending ? (
          <div className="alert alert-info d-flex align-items-center gap-3">
            <span className="fs-2">⏳</span>
            <div>
              <h5 className="alert-heading mb-1">Solicitud en Revisión por la Dirección de Programa</h5>
              <p className="mb-0">
                Tus documentos digitales están siendo auditados. Recibirás respuesta formal en esta misma bandeja.
              </p>
            </div>
          </div>
        ) : (
          <div className="alert alert-secondary">
            Estado de tu solicitud: <strong>{studentReq.status}</strong>
          </div>
        )}
      </div>
    );
  }

  // Caso 3: Estudiante nuevo sin práctica ni solicitud
  return (
    <div className="card p-5 text-center border-0 rounded-4 shadow-sm bg-light">
      <div className="fs-1 mb-2">🎓</div>
      <h2 className="fw-bold mb-2" style={{ color: "#004b87" }}>
        ¡Bienvenido a SIGPRA, {currentUser.name}!
      </h2>
      <p className="lead text-muted mx-auto mb-4" style={{ maxWidth: 650 }}>
        Sistema Integral de Gestión de Prácticas Académicas de la Universidad de Investigación y Desarrollo (UDI).
      </p>

      <div className="row g-3 justify-content-center text-start mb-4" style={{ maxWidth: 800, margin: "0 auto" }}>
        <div className="col-md-4">
          <div className="card p-3 h-100 bg-white border shadow-xs">
            <div className="fw-bold text-primary mb-1">1. Radicación (CU-08)</div>
            <small className="text-muted">Carga tu certificado de matrícula, afiliación a EPS/ARL y paz y salvo.</small>
          </div>
        </div>
        <div className="col-md-4">
          <div className="card p-3 h-100 bg-white border shadow-xs">
            <div className="fw-bold text-success mb-1">2. Habilitación</div>
            <small className="text-muted">La Dirección de Programa verifica tus soportes y emite el dictamen formal.</small>
          </div>
        </div>
        <div className="col-md-4">
          <div className="card p-3 h-100 bg-white border shadow-xs">
            <div className="fw-bold text-purple mb-1">3. Ejecución de 160h</div>
            <small className="text-muted">Coordinación asigna tu plaza y comienzas a reportar tus bitácoras diarias.</small>
          </div>
        </div>
      </div>

      <div>
        <button onClick={() => setCurrentView("student-request")} className="btn btn-primary btn-lg px-4 py-2 fw-bold shadow">
          📝 Radicar mi Solicitud de Práctica Ahora ➔
        </button>
      </div>
    </div>
  );
};
