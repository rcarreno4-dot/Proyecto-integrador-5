import React, { useState } from "react";
import { useSigpra } from "../../context/SigpraContext";

export const TeacherVisitsView: React.FC = () => {
  const { currentUser, assignments, visits, users, places, agreements, institutions, addVisit } = useSigpra();
  const myAssignments = assignments.filter(item => item.teacherId === currentUser.id && item.status === "ACTIVA");
  const [assignmentId, setAssignmentId] = useState("");
  const [date, setDate] = useState(new Date().toISOString().slice(0, 10));
  const [institutionContact, setInstitutionContact] = useState("");
  const [observations, setObservations] = useState("");
  const [assessment, setAssessment] = useState(4);
  const myVisits = visits.filter(visit => visit.teacherId === currentUser.id);

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    addVisit({ assignmentId: Number(assignmentId), teacherId: currentUser.id, date, institutionContact: institutionContact.trim(), observations: observations.trim(), assessment: Number(assessment) });
    setAssignmentId("");
    setInstitutionContact("");
    setObservations("");
    setAssessment(4);
  };

  return (
    <div>
      <div className="mb-3">
        <h2 className="fw-bold mb-1 text-primary">Visitas y evaluación de acompañamiento</h2>
        <p className="text-muted small mb-0">Registra la visita al centro de práctica, contacto, observaciones y evaluación.</p>
      </div>
      <form className="card card-body border-0 shadow-xs mb-4" onSubmit={handleSubmit}>
        <h5 className="fw-bold">Registrar visita</h5>
        {myAssignments.length === 0 && <div className="alert alert-info">No tienes estudiantes asignados para programar una visita.</div>}
        <div className="row g-3">
          <div className="col-md-4"><label className="form-label">Estudiante / asignación</label><select className="form-select" required value={assignmentId} onChange={event => setAssignmentId(event.target.value)} disabled={!myAssignments.length}><option value="">Selecciona estudiante</option>{myAssignments.map(assignment => <option key={assignment.id} value={assignment.id}>{users.find(user => user.id === assignment.studentId)?.name}</option>)}</select></div>
          <div className="col-md-2"><label className="form-label">Fecha de visita</label><input className="form-control" type="date" required value={date} onChange={event => setDate(event.target.value)} /></div>
          <div className="col-md-3"><label className="form-label">Contacto institucional</label><input className="form-control" required value={institutionContact} onChange={event => setInstitutionContact(event.target.value)} /></div>
          <div className="col-md-3"><label className="form-label">Evaluación (1 a 5)</label><select className="form-select" value={assessment} onChange={event => setAssessment(Number(event.target.value))}>{[1, 2, 3, 4, 5].map(score => <option key={score} value={score}>{score} — {score === 5 ? "Excelente" : score === 1 ? "Insuficiente" : "Satisfactorio"}</option>)}</select></div>
          <div className="col-md-10"><label className="form-label">Observaciones y compromisos</label><textarea className="form-control" required rows={2} value={observations} onChange={event => setObservations(event.target.value)} /></div>
          <div className="col-md-2 d-flex align-items-end"><button className="btn btn-primary w-100" type="submit" disabled={!myAssignments.length}>Guardar visita</button></div>
        </div>
      </form>

      <div className="table-responsive card border-0 shadow-xs">
        <table className="table table-hover align-middle mb-0">
          <thead className="table-light"><tr><th>Fecha</th><th>Estudiante</th><th>Institución</th><th>Contacto</th><th>Evaluación</th><th>Observaciones</th></tr></thead>
          <tbody>{myVisits.length === 0 ? <tr><td className="text-center text-muted py-4" colSpan={6}>Aún no se han registrado visitas.</td></tr> : myVisits.map(visit => {
            const assignment = assignments.find(item => item.id === visit.assignmentId);
            const place = places.find(item => item.id === assignment?.placeId);
            const agreement = agreements.find(item => item.id === place?.agreementId);
            return <tr key={visit.id}><td>{visit.date}</td><td>{users.find(user => user.id === assignment?.studentId)?.name}</td><td>{institutions.find(item => item.id === agreement?.institutionId)?.name}</td><td>{visit.institutionContact}</td><td><span className="badge bg-primary">{visit.assessment} / 5</span></td><td>{visit.observations}</td></tr>;
          })}</tbody>
        </table>
      </div>
    </div>
  );
};
