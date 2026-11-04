import React, { useMemo, useState } from "react";
import { useSigpra } from "../../context/SigpraContext";

export const AssignmentsView: React.FC = () => {
  const { assignments, requests, users, periods, places, agreements, institutions, createAssignment } = useSigpra();
  const [studentId, setStudentId] = useState("");
  const [placeId, setPlaceId] = useState("");
  const [teacherId, setTeacherId] = useState("");
  const [periodId, setPeriodId] = useState("");

  const eligibleStudents = useMemo(() => requests
    .filter(request => request.status === "APROBADA")
    .map(request => users.find(user => user.id === request.studentId))
    .filter((user): user is NonNullable<typeof user> => Boolean(user))
    .filter(user => !assignments.some(assignment => assignment.studentId === user.id && assignment.status === "ACTIVA")),
  [requests, users, assignments]);

  const availablePlaces = places.filter(place => place.occupied < place.offered
    && agreements.some(agreement => agreement.id === place.agreementId && agreement.status === "VIGENTE"));
  const teachers = users.filter(user => user.role === "DOCENTE_ASESOR");
  const publishedPeriods = periods.filter(period => period.status === "PUBLICADO");

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    createAssignment(Number(studentId), Number(placeId), Number(teacherId), Number(periodId));
    setStudentId("");
    setPlaceId("");
    setTeacherId("");
    setPeriodId("");
  };

  const assignmentRows = assignments.map(assignment => {
    const student = users.find(user => user.id === assignment.studentId);
    const teacher = users.find(user => user.id === assignment.teacherId);
    const place = places.find(item => item.id === assignment.placeId);
    const agreement = agreements.find(item => item.id === place?.agreementId);
    const institution = institutions.find(item => item.id === agreement?.institutionId);
    const period = periods.find(item => item.id === assignment.periodId);
    return { assignment, student, teacher, place, institution, period };
  });

  return (
    <div>
      <div className="mb-3">
        <h2 className="fw-bold mb-1 text-primary">Asignación de practicantes</h2>
        <p className="text-muted small mb-0">Asigna estudiantes habilitados a una plaza disponible, periodo y docente asesor.</p>
      </div>

      <form className="card card-body border-0 shadow-xs mb-4" onSubmit={handleSubmit}>
        <h5 className="fw-bold">Nueva asignación</h5>
        {eligibleStudents.length === 0 && (
          <div className="alert alert-info mb-3">No hay estudiantes habilitados sin plaza asignada. Primero aprueba una solicitud desde la bandeja de Dirección.</div>
        )}
        <div className="row g-3">
          <div className="col-md-3">
            <label className="form-label">Estudiante habilitado</label>
            <select className="form-select" required value={studentId} onChange={event => setStudentId(event.target.value)} disabled={!eligibleStudents.length}>
              <option value="">Selecciona estudiante</option>
              {eligibleStudents.map(student => <option key={student.id} value={student.id}>{student.name} — {student.code}</option>)}
            </select>
          </div>
          <div className="col-md-3">
            <label className="form-label">Plaza disponible</label>
            <select className="form-select" required value={placeId} onChange={event => setPlaceId(event.target.value)} disabled={!availablePlaces.length}>
              <option value="">Selecciona plaza</option>
              {availablePlaces.map(place => {
                const agreement = agreements.find(item => item.id === place.agreementId);
                const institution = institutions.find(item => item.id === agreement?.institutionId);
                return <option key={place.id} value={place.id}>{institution?.name} — {place.program} ({place.offered - place.occupied} cupos)</option>;
              })}
            </select>
          </div>
          <div className="col-md-2">
            <label className="form-label">Docente asesor</label>
            <select className="form-select" required value={teacherId} onChange={event => setTeacherId(event.target.value)}>
              <option value="">Selecciona docente</option>
              {teachers.map(teacher => <option key={teacher.id} value={teacher.id}>{teacher.name}</option>)}
            </select>
          </div>
          <div className="col-md-2">
            <label className="form-label">Periodo</label>
            <select className="form-select" required value={periodId} onChange={event => setPeriodId(event.target.value)}>
              <option value="">Selecciona periodo</option>
              {publishedPeriods.map(period => <option key={period.id} value={period.id}>{period.year}-{period.semester} — {period.program}</option>)}
            </select>
          </div>
          <div className="col-md-2 d-flex align-items-end">
            <button className="btn btn-primary w-100" type="submit" disabled={!eligibleStudents.length || !availablePlaces.length}>Confirmar asignación</button>
          </div>
        </div>
      </form>

      <div className="table-responsive card border-0 shadow-xs">
        <table className="table table-hover align-middle mb-0">
          <thead className="table-light"><tr><th>Estudiante</th><th>Plaza / institución</th><th>Docente asesor</th><th>Periodo</th><th>Fecha</th><th>Estado</th></tr></thead>
          <tbody>{assignmentRows.map(({ assignment, student, teacher, place, institution, period }) => (
            <tr key={assignment.id}>
              <td><strong>{student?.name}</strong><div className="text-muted small">{student?.code}</div></td>
              <td>{place?.program}<div className="text-muted small">{institution?.name}</div></td>
              <td>{teacher?.name}</td>
              <td>{period?.year}-{period?.semester} — {period?.program}</td>
              <td>{assignment.date}</td>
              <td><span className={`badge ${assignment.status === "ACTIVA" ? "bg-success" : "bg-secondary"}`}>{assignment.status}</span></td>
            </tr>
          ))}</tbody>
        </table>
      </div>
    </div>
  );
};
