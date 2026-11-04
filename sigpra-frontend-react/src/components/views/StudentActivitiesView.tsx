import React, { useState } from "react";
import { useSigpra } from "../../context/SigpraContext";

export const StudentActivitiesView: React.FC = () => {
  const { currentUser, assignments, activities, periods, addActivity, resubmitActivity, showToast } = useSigpra();
  const assignment = assignments.find(item => item.studentId === currentUser.id && item.status === "ACTIVA");
  const period = periods.find(item => item.id === assignment?.periodId);
  const myActivities = assignment ? activities.filter(item => item.assignmentId === assignment.id) : [];
  const [date, setDate] = useState(new Date().toISOString().slice(0, 10));
  const [type, setType] = useState("");
  const [description, setDescription] = useState("");
  const [hours, setHours] = useState(1);
  const [evidenceNames, setEvidenceNames] = useState<string[]>([]);
  const [editingActivityId, setEditingActivityId] = useState<number | null>(null);

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!assignment) return;
    if (date < (period?.start || date) || date > (period?.end || date)) {
      showToast("❌ La fecha debe estar dentro de la vigencia del periodo de práctica.");
      return;
    }
    if (hours > 12) {
      showToast("❌ Registra máximo 12 horas por actividad diaria.");
      return;
    }
    if (evidenceNames.length === 0) {
      showToast("❌ Adjunta al menos una evidencia de la actividad.");
      return;
    }
    const activityData = {
      assignmentId: assignment.id,
      date,
      type: type.trim(),
      description: description.trim(),
      hours: Number(hours),
      evidenceNames
    };
    if (editingActivityId !== null) {
      resubmitActivity(editingActivityId, activityData);
    } else {
      addActivity({ ...activityData, status: "PENDIENTE" });
    }
    setType("");
    setDescription("");
    setHours(1);
    setEvidenceNames([]);
    setEditingActivityId(null);
    const input = document.getElementById("activity-evidence") as HTMLInputElement | null;
    if (input) input.value = "";
  };

  if (!assignment) {
    return <div className="alert alert-info">No tienes una asignación de práctica activa para registrar actividades.</div>;
  }

  const approvedHours = myActivities.filter(item => item.status === "APROBADA").reduce((sum, item) => sum + item.hours, 0);

  return (
    <div>
      <div className="mb-3">
        <h2 className="fw-bold mb-1 text-primary">Mi bitácora de actividades</h2>
        <p className="text-muted small mb-0">Registra actividades y evidencias para la revisión de tu docente asesor.</p>
      </div>
      <div className="alert alert-primary d-flex justify-content-between align-items-center">
        <span>Horas aprobadas: <strong>{approvedHours}</strong> / {period?.minHours || 160} h</span>
        <span>Los reportes nuevos quedan pendientes de validación.</span>
      </div>
      <form className="card card-body border-0 shadow-xs mb-4" onSubmit={handleSubmit}>
        <h5 className="fw-bold">{editingActivityId !== null ? "Corregir actividad devuelta" : "Registrar actividad ejecutada"}</h5>
        <div className="row g-3">
          <div className="col-md-2"><label className="form-label">Fecha</label><input className="form-control" type="date" min={period?.start} max={period?.end} required value={date} onChange={event => setDate(event.target.value)} /></div>
          <div className="col-md-3"><label className="form-label">Tipo de actividad</label><input className="form-control" required value={type} onChange={event => setType(event.target.value)} placeholder="Ej. Desarrollo de software" /></div>
          <div className="col-md-2"><label className="form-label">Horas (máx. 12)</label><input className="form-control" type="number" min={1} max={12} required value={hours} onChange={event => setHours(Number(event.target.value))} /></div>
          <div className="col-md-5"><label className="form-label">Descripción</label><input className="form-control" required value={description} onChange={event => setDescription(event.target.value)} /></div>
          <div className="col-md-9"><label className="form-label">Evidencias (PDF o imagen)</label><input id="activity-evidence" className="form-control" type="file" accept=".pdf,image/*" multiple required={editingActivityId === null || evidenceNames.length === 0} onChange={event => setEvidenceNames(Array.from(event.target.files || []).map(file => file.name))} /><small className="text-muted">En esta demostración se guardan los nombres de archivo localmente, no se cargan a un servidor.</small></div>
          <div className="col-md-3 d-flex align-items-end gap-2">
            {editingActivityId !== null && <button className="btn btn-outline-secondary w-50" onClick={() => { setEditingActivityId(null); setType(""); setDescription(""); setHours(1); setEvidenceNames([]); const input = document.getElementById("activity-evidence") as HTMLInputElement | null; if (input) input.value = ""; }} type="button">Cancelar</button>}
            <button className="btn btn-primary w-100" type="submit">{editingActivityId !== null ? "Reenviar" : "Enviar a validación"}</button>
          </div>
        </div>
      </form>
      <div className="table-responsive card border-0 shadow-xs">
        <table className="table table-hover align-middle mb-0">
          <thead className="table-light"><tr><th>Fecha</th><th>Actividad</th><th>Descripción / evidencias</th><th>Horas</th><th>Estado</th><th>Observaciones docente</th></tr></thead>
          <tbody>{myActivities.length === 0 ? <tr><td className="text-center text-muted py-4" colSpan={6}>Aún no has registrado actividades.</td></tr> : myActivities.map(item => (
            <tr key={item.id}>
              <td>{item.date}</td><td>{item.type}</td>
              <td>{item.description}<div className="small text-muted">{item.evidenceNames?.join(", ") || "Sin evidencias adjuntas"}</div></td>
              <td>{item.hours} h</td>
              <td><span className={`badge ${item.status === "APROBADA" ? "bg-success" : item.status === "PENDIENTE" ? "bg-warning text-dark" : "bg-danger"}`}>{item.status}</span></td>
              <td>{item.feedback || "—"}{item.status === "DEVUELTA" && <button className="btn btn-link btn-sm d-block px-0" onClick={() => { setEditingActivityId(item.id); setDate(item.date); setType(item.type); setDescription(item.description); setHours(item.hours); setEvidenceNames(item.evidenceNames || []); window.scrollTo({ top: 0, behavior: "smooth" }); }} type="button">Corregir y reenviar</button>}</td>
            </tr>
          ))}</tbody>
        </table>
      </div>
    </div>
  );
};
