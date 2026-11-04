import React, { useState } from "react";
import { Activity } from "../../types";
import { useSigpra } from "../../context/SigpraContext";

export const TeacherValidationsView: React.FC = () => {
  const { currentUser, assignments, activities, users, updateActivityStatus, showToast } = useSigpra();
  const [feedbackByActivity, setFeedbackByActivity] = useState<Record<number, string>>({});
  const myAssignmentIds = assignments
    .filter(assignment => assignment.teacherId === currentUser.id && assignment.status === "ACTIVA")
    .map(assignment => assignment.id);
  const myActivities = activities
    .filter(activity => myAssignmentIds.includes(activity.assignmentId))
    .sort((a, b) => b.date.localeCompare(a.date));

  const validate = (activity: Activity, status: Activity["status"]) => {
    const feedback = feedbackByActivity[activity.id] || "";
    if (status !== "APROBADA" && !feedback.trim()) {
      showToast("❌ Escribe una observación para devolver o rechazar la actividad.");
      return;
    }
    updateActivityStatus(activity.id, status, feedback);
  };

  return (
    <div>
      <div className="mb-3">
        <h2 className="fw-bold mb-1 text-primary">Validación de bitácoras</h2>
        <p className="text-muted small mb-0">Revisa las actividades reportadas, sus evidencias y deja observaciones.</p>
      </div>
      {myActivities.length === 0 ? (
        <div className="alert alert-info">No hay actividades registradas para tus estudiantes asignados.</div>
      ) : (
        <div className="d-flex flex-column gap-3">
          {myActivities.map(activity => {
            const assignment = assignments.find(item => item.id === activity.assignmentId);
            const student = users.find(user => user.id === assignment?.studentId);
            const isPending = activity.status === "PENDIENTE";
            return (
              <article className="card border-0 shadow-xs" key={activity.id}>
                <div className="card-body">
                  <div className="d-flex flex-wrap justify-content-between gap-2 mb-2">
                    <div><h5 className="fw-bold mb-0">{student?.name || "Estudiante"}</h5><small className="text-muted">{activity.date} · {activity.type} · {activity.hours} horas</small></div>
                    <span className={`badge align-self-start ${activity.status === "APROBADA" ? "bg-success" : isPending ? "bg-warning text-dark" : "bg-danger"}`}>{activity.status}</span>
                  </div>
                  <p className="mb-2">{activity.description}</p>
                  <div className="small text-muted mb-3"><strong>Evidencias:</strong> {activity.evidenceNames?.join(", ") || "Sin evidencias adjuntas"}</div>
                  {activity.feedback && !isPending && <div className="alert alert-light border py-2"><strong>Observaciones registradas:</strong> {activity.feedback}</div>}
                  {isPending && (
                    <>
                      <label className="form-label small fw-bold" htmlFor={`feedback-${activity.id}`}>Observaciones (obligatorias si se devuelve o rechaza)</label>
                      <textarea
                        className="form-control mb-3"
                        id={`feedback-${activity.id}`}
                        onChange={event => setFeedbackByActivity(previous => ({ ...previous, [activity.id]: event.target.value }))}
                        rows={2}
                        value={feedbackByActivity[activity.id] || ""}
                      />
                      <div className="d-flex justify-content-end gap-2">
                        <button className="btn btn-outline-danger btn-sm" onClick={() => validate(activity, "RECHAZADA")} type="button">Rechazar</button>
                        <button className="btn btn-warning btn-sm" onClick={() => validate(activity, "DEVUELTA")} type="button">Devolver para corrección</button>
                        <button className="btn btn-success btn-sm" onClick={() => validate(activity, "APROBADA")} type="button">Aprobar horas</button>
                      </div>
                    </>
                  )}
                </div>
              </article>
            );
          })}
        </div>
      )}
    </div>
  );
};
