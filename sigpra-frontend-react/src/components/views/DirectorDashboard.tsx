import React, { useState } from "react";
import { useSigpra } from "../../context/SigpraContext";
import { KPICard } from "../common/KPICard";

export const DirectorDashboard: React.FC = () => {
  const { requests, assignments, users, periods, updateRequestStatus, setCurrentView, currentView } = useSigpra();

  const pendingRequests = requests.filter(r => r.status === "PENDIENTE_REVISION");
  const approvedRequests = requests.filter(r => r.status === "APROBADA");
  const activeAssignments = assignments.filter(a => a.status === "ACTIVA");
  const showingApproved = currentView === "director-approved";
  const visibleRequests = showingApproved ? approvedRequests : pendingRequests;

  const [evaluatingReqId, setEvaluatingReqId] = useState<number | null>(null);
  const [justification, setJustification] = useState("");

  const selectedReq = requests.find(r => r.id === evaluatingReqId);
  const reqStudent = selectedReq ? users.find(u => u.id === selectedReq.studentId) : null;
  const reqPeriod = selectedReq ? periods.find(p => p.id === selectedReq.periodId) : null;

  const handleDecision = (status: "APROBADA" | "DEVUELTA" | "RECHAZADA") => {
    if (!evaluatingReqId) return;
    if (status !== "APROBADA" && justification.trim().length < 10) {
      alert("Según el Caso de Uso CU-08, la justificación es obligatoria y debe contener al menos 10 caracteres explicando el motivo al estudiante.");
      return;
    }
    updateRequestStatus(evaluatingReqId, status, justification);
    setEvaluatingReqId(null);
    setJustification("");
  };

  return (
    <div>
      <div className="row g-3 mb-4">
        <div className="col-md-3">
          <KPICard
            title="Por Dictaminar"
            value={pendingRequests.length}
            subtitle="Solicitudes pendientes"
            icon="⏳"
            variant="amber"
            onClick={() => setCurrentView("director-requests")}
            ctaText="Evaluar bandeja"
          />
        </div>
        <div className="col-md-3">
          <KPICard
            title="Habilitados"
            value={approvedRequests.length}
            subtitle="Listos para asignación"
            icon="✅"
            variant="green"
            onClick={() => setCurrentView("director-approved")}
            ctaText="Ver habilitados"
          />
        </div>
        <div className="col-md-3">
          <KPICard
            title="En Práctica"
            value={activeAssignments.length}
            subtitle="Prácticas activas en curso"
            icon="🎓"
            variant="blue"
            onClick={() => setCurrentView("reports")}
            ctaText="Ver consolidado"
          />
        </div>
        <div className="col-md-3">
          <KPICard
            title="Normativa"
            value="160h"
            subtitle="Decreto 1075 de 2015"
            icon="⚖️"
            variant="purple"
            onClick={() => setCurrentView("periods")}
            ctaText="Ver periodos"
          />
        </div>
      </div>

      <div className="card border rounded-3 shadow-xs">
        <div className="card-header bg-white py-3 d-flex justify-content-between align-items-center">
          <div>
            <h5 className="mb-0 fw-bold text-dark">
              {showingApproved ? "Estudiantes habilitados para asignación" : "Solicitudes Pendientes de Dictamen Formal"}
            </h5>
            <small className="text-muted">
              {showingApproved
                ? "Solicitudes aprobadas que pueden continuar con la asignación de plaza."
                : "Estudiantes que radicaron soportes documentales para inicio de práctica"}
            </small>
          </div>
          <div className="d-flex align-items-center gap-2">
            <button
              className={`btn btn-sm ${showingApproved ? "btn-outline-secondary" : "btn-warning"}`}
              onClick={() => setCurrentView("director-requests")}
              type="button"
            >
              Pendientes ({pendingRequests.length})
            </button>
            <button
              className={`btn btn-sm ${showingApproved ? "btn-success" : "btn-outline-secondary"}`}
              onClick={() => setCurrentView("director-approved")}
              type="button"
            >
              Habilitados ({approvedRequests.length})
            </button>
          </div>
        </div>
        <div className="table-responsive">
          <table className="table table-hover align-middle mb-0">
            <thead className="table-light">
              <tr>
                <th>Estudiante</th>
                <th>Periodo</th>
                <th>Modalidad</th>
                {showingApproved ? <th>Estado de asignación</th> : <th>Soportes Anexos</th>}
                {!showingApproved && <th className="text-end">Acción</th>}
              </tr>
            </thead>
            <tbody>
              {visibleRequests.length === 0 ? (
                <tr>
                  <td colSpan={showingApproved ? 4 : 5} className="text-center py-4 text-muted">
                    {showingApproved
                      ? "No hay estudiantes habilitados pendientes de asignación."
                      : "✅ No hay solicitudes pendientes de revisión. Todas han sido tramitadas."}
                  </td>
                </tr>
              ) : (
                visibleRequests.map(req => {
                  const student = users.find(u => u.id === req.studentId);
                  const period = periods.find(p => p.id === req.periodId);
                  const hasAssignment = assignments.some(assignment =>
                    assignment.studentId === req.studentId && assignment.status === "ACTIVA"
                  );
                  return (
                    <tr key={req.id}>
                      <td>
                        <strong>{student?.name}</strong>
                        <div className="text-muted small">{student?.code || student?.email}</div>
                      </td>
                      <td>{period?.program} ({period?.year}-{period?.semester})</td>
                      <td>{req.modality}</td>
                      {showingApproved ? (
                        <td>
                          <span className={`badge ${hasAssignment ? "bg-success" : "bg-info text-dark"}`}>
                            {hasAssignment ? "Asignado" : "Listo para asignación"}
                          </span>
                        </td>
                      ) : (
                        <>
                          <td>
                            {(req.documentNames?.length ? req.documentNames : ["Matrícula.pdf (demo)", "EPS_ARL.pdf (demo)", "PazSalvo.pdf (demo)"]).map(document => (
                              <span key={document} className="badge bg-secondary-subtle text-dark border me-1">{document}</span>
                            ))}
                          </td>
                          <td className="text-end">
                            <button
                              onClick={() => {
                                setEvaluatingReqId(req.id);
                                setJustification("Cumple con totalidad de prerrequisitos documentales y académicos.");
                              }}
                              className="btn btn-primary btn-sm fw-bold shadow-xs"
                            >
                              🔍 Evaluar Solicitud
                            </button>
                          </td>
                        </>
                      )}
                    </tr>
                  );
                })
              )}
            </tbody>
          </table>
        </div>
      </div>

      {evaluatingReqId && selectedReq && (
        <div className="modal show d-block" style={{ backgroundColor: "rgba(15,23,42,0.6)" }}>
          <div className="modal-dialog modal-lg">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title fw-bold">
                  Evaluación y Dictamen de Solicitud #{selectedReq.id}
                </h5>
                <button type="button" className="btn-close" onClick={() => setEvaluatingReqId(null)} />
              </div>
              <div className="modal-body">
                <div className="card p-3 bg-light border-0 rounded-3 mb-3">
                  <div className="row g-2">
                    <div className="col-md-6">
                      <span className="text-muted small d-block">Estudiante:</span>
                      <strong>{reqStudent?.name} ({reqStudent?.code})</strong>
                    </div>
                    <div className="col-md-6">
                      <span className="text-muted small d-block">Convocatoria:</span>
                      <strong>{reqPeriod?.program} ({reqPeriod?.year}-{reqPeriod?.semester})</strong>
                    </div>
                  </div>
                </div>

                <div className="mb-3">
                  <label className="form-label small fw-bold">Soportes Digitales Radicados:</label>
                  <div className="d-flex gap-2 flex-wrap">
                    {(selectedReq.documentNames?.length ? selectedReq.documentNames : ["Matrícula.pdf (demo)", "EPS_ARL.pdf (demo)", "PazSalvo.pdf (demo)"]).map(document => (
                      <span key={document} className="badge bg-secondary-subtle text-dark border p-2">📄 {document}</span>
                    ))}
                  </div>
                </div>

                <div className="mb-3">
                  <label className="form-label small fw-bold">Justificación / Observaciones de la Dirección *</label>
                  <textarea
                    className="form-control"
                    rows={3}
                    value={justification}
                    onChange={e => setJustification(e.target.value)}
                    placeholder="Registra el dictamen o las observaciones detalladas..."
                  />
                </div>
              </div>
              <div className="modal-footer d-flex justify-content-between">
                <button type="button" className="btn btn-secondary btn-sm" onClick={() => setEvaluatingReqId(null)}>
                  Cerrar
                </button>
                <div className="d-flex gap-2">
                  <button type="button" onClick={() => handleDecision("RECHAZADA")} className="btn btn-outline-danger btn-sm">
                    Rechazar
                  </button>
                  <button type="button" onClick={() => handleDecision("DEVUELTA")} className="btn btn-warning btn-sm">
                    Devolver para corrección
                  </button>
                  <button type="button" onClick={() => handleDecision("APROBADA")} className="btn btn-success btn-sm fw-bold">
                    ✅ Aprobar y Habilitar
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
