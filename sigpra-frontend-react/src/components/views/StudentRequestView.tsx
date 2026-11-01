import React, { useState } from "react";
import { useSigpra } from "../../context/SigpraContext";

export const StudentRequestView: React.FC = () => {
  const { currentUser, assignments, requests, periods, places, institutions, users, setCurrentView, showToast } = useSigpra();

  const activeAssignment = assignments.find(
    a => a.studentId === currentUser.id && a.status === "ACTIVA"
  );

  const existingRequest = requests.find(r => r.studentId === currentUser.id);

  const [level, setLevel] = useState("IX semestre");
  const [modality, setModality] = useState("Convenio institucional");
  const [comments, setComments] = useState("");

  if (activeAssignment) {
    const period = periods.find(p => p.id === activeAssignment.periodId);
    const place = places.find(p => p.id === activeAssignment.placeId);
    const institution = institutions.find(i => i.id === place?.agreementId);
    const teacher = users.find(u => u.id === activeAssignment.teacherId);

    return (
      <div className="card p-4 border-start border-4 border-success shadow-sm rounded-3">
        <div className="d-flex align-items-center gap-3 mb-3">
          <span className="fs-1">🎓</span>
          <div>
            <span className="badge bg-success px-3 py-1 fs-6 mb-1">PRÁCTICA ACADÉMICA EN CURSO</span>
            <h2 className="mb-0 text-primary fw-bold">Práctica Académica Asignada y Activa</h2>
            <small className="text-muted">Tu trámite de solicitud y habilitación se encuentra formalizado y en ejecución.</small>
          </div>
        </div>

        <div className="alert alert-info border-0 rounded-3">
          <strong>Información para el estudiante:</strong> Actualmente ya cuentas con una práctica académica activa y
          formalmente asignada. No requieres solicitar ni radicar una nueva práctica. Puedes reportar tus horas en tu
          bitácora diaria y adjuntar las evidencias de tus actividades.
        </div>

        <div className="card p-3 bg-light border-0 rounded-3 mb-3">
          <h5 className="fw-bold mb-3">Detalle de tu Vinculación Formativa</h5>
          <div className="row g-3">
            <div className="col-md-4">
              <span className="text-muted d-block small">Institución receptora:</span>
              <strong className="fs-6">{institution?.name || "Asignada"}</strong>
            </div>
            <div className="col-md-4">
              <span className="text-muted d-block small">Docente asesor UDI:</span>
              <strong className="fs-6">{teacher?.name || "Asignado"}</strong>
            </div>
            <div className="col-md-4">
              <span className="text-muted d-block small">Periodo académico:</span>
              <strong className="fs-6">{period?.program} ({period?.year}-{period?.semester})</strong>
            </div>
            <div className="col-md-4">
              <span className="text-muted d-block small">Horas requeridas:</span>
              <strong className="fs-6 text-success">{period?.minHours || 160} hrs (Decreto 1075)</strong>
            </div>
            <div className="col-md-4">
              <span className="text-muted d-block small">Jornada / Horario:</span>
              <strong className="fs-6">{place?.shift || "Diurna"}</strong>
            </div>
            <div className="col-md-4">
              <span className="text-muted d-block small">Tutor en centro de práctica:</span>
              <strong className="fs-6">{place?.tutor || "Designado"}</strong>
            </div>
          </div>
        </div>

        <div className="d-flex gap-2">
          <button onClick={() => setCurrentView("student-activities")} className="btn btn-primary fw-bold">
            📝 Ir a Mi Bitácora (Reportar Horas)
          </button>
          <button onClick={() => setCurrentView("student-progress")} className="btn btn-outline-secondary">
            ⏱️ Consultar Mi Avance de 160h
          </button>
        </div>
      </div>
    );
  }

  if (existingRequest && existingRequest.status === "APROBADA") {
    return (
      <div className="card p-4 border-start border-4 border-success shadow-sm rounded-3">
        <div className="d-flex align-items-center gap-3 mb-3">
          <span className="fs-1">🎉</span>
          <div>
            <span className="badge bg-success px-3 py-1 fs-6 mb-1">SOLICITUD APROBADA Y HABILITADA</span>
            <h2 className="mb-0 text-primary fw-bold">¡Estás Habilitado para Práctica!</h2>
            <small className="text-muted">Tu expediente fue evaluado y aprobado por la Dirección de Programa.</small>
          </div>
        </div>
        <div className="alert alert-success">
          Tu solicitud formal fue aprobada con éxito. En este momento la <strong>Coordinación de Prácticas</strong> se
          encuentra gestionando la asignación de tu plaza y docente asesor. No requieres volver a radicar.
        </div>
      </div>
    );
  }

  const handleRadicar = (e: React.FormEvent) => {
    e.preventDefault();
    showToast("✅ Solicitud formal de práctica radicada con éxito. Queda en revisión por Dirección.");
  };

  return (
    <div className="card p-4 border rounded-3 shadow-xs">
      <h3 className="fw-bold text-primary mb-2">Formulario de Solicitud de Práctica Académica</h3>
      <p className="text-muted small mb-4">
        Radica tus documentos de soporte digital (Matrícula, EPS/ARL y Paz y Salvo) para habilitación formal.
      </p>

      <form onSubmit={handleRadicar} className="row g-3">
        <div className="col-md-6">
          <label className="form-label small fw-bold">Nivel formativo *</label>
          <select className="form-select form-select-sm" value={level} onChange={e => setLevel(e.target.value)}>
            <option value="VIII semestre">VIII semestre</option>
            <option value="IX semestre">IX semestre</option>
          </select>
        </div>

        <div className="col-md-6">
          <label className="form-label small fw-bold">Modalidad de práctica *</label>
          <select className="form-select form-select-sm" value={modality} onChange={e => setModality(e.target.value)}>
            <option value="Convenio institucional">Convenio institucional</option>
            <option value="Autogestionada">Autogestionada (Empresa propia o vinculación laboral)</option>
          </select>
        </div>

        <div className="col-md-4">
          <label className="form-label small fw-bold">Certificado de Matrícula (PDF) *</label>
          <input type="file" className="form-control form-control-sm" required />
        </div>

        <div className="col-md-4">
          <label className="form-label small fw-bold">Afiliación EPS / ARL vigente (PDF) *</label>
          <input type="file" className="form-control form-control-sm" required />
        </div>

        <div className="col-md-4">
          <label className="form-label small fw-bold">Paz y Salvo Académico (PDF) *</label>
          <input type="file" className="form-control form-control-sm" required />
        </div>

        <div className="col-12">
          <label className="form-label small fw-bold">Observaciones adicionales</label>
          <textarea
            className="form-control form-control-sm"
            rows={2}
            placeholder="Información sobre tu centro de práctica o empresa de interés..."
            value={comments}
            onChange={e => setComments(e.target.value)}
          />
        </div>

        <div className="col-12 mt-4">
          <button type="submit" className="btn btn-primary px-4 fw-bold shadow-xs">
            📤 Radicar Solicitud Formal (CU-08)
          </button>
        </div>
      </form>
    </div>
  );
};
