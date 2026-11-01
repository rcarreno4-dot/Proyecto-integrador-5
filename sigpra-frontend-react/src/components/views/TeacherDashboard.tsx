import React from "react";
import { useSigpra } from "../../context/SigpraContext";
import { KPICard } from "../common/KPICard";

export const TeacherDashboard: React.FC = () => {
  const { currentUser, assignments, activities, users, periods, institutions, places } = useSigpra();

  const myAssignments = assignments.filter(a => a.teacherId === currentUser.id && a.status === "ACTIVA");
  const myAssignmentIds = myAssignments.map(a => a.id);

  const pendingActivities = activities.filter(
    a => myAssignmentIds.includes(a.assignmentId) && a.status === "PENDIENTE"
  );

  const approvedActivities = activities.filter(
    a => myAssignmentIds.includes(a.assignmentId) && a.status === "APROBADA"
  );

  const totalValidatedHours = approvedActivities.reduce((sum, a) => sum + a.hours, 0);

  return (
    <div>
      {/* Tarjetas KPI del Docente Asesor */}
      <div className="row g-3 mb-4">
        <div className="col-md-3">
          <KPICard
            title="Estudiantes"
            value={myAssignments.length}
            subtitle="Practicantes a cargo"
            icon="🎓"
            variant="blue"
          />
        </div>
        <div className="col-md-3">
          <KPICard
            title="Por Validar"
            value={pendingActivities.length}
            subtitle="Bitácoras pendientes"
            icon="📝"
            variant="amber"
          />
        </div>
        <div className="col-md-3">
          <KPICard
            title="Horas Validadas"
            value={`${totalValidatedHours}h`}
            subtitle="Acompañamiento docente"
            icon="⏱️"
            variant="green"
          />
        </div>
        <div className="col-md-3">
          <KPICard
            title="Visitas"
            value="3"
            subtitle="Seguimiento institucional"
            icon="📍"
            variant="purple"
          />
        </div>
      </div>

      {/* Lista de Estudiantes Asignados */}
      <div className="card border rounded-3 shadow-xs bg-white mb-4">
        <div className="card-header bg-white py-3">
          <h5 className="mb-0 fw-bold">Estudiantes Asignados para Tutoría y Seguimiento</h5>
        </div>
        <div className="table-responsive">
          <table className="table table-hover align-middle mb-0">
            <thead className="table-light">
              <tr>
                <th>Estudiante</th>
                <th>Periodo</th>
                <th>Institución / Empresa</th>
                <th>Tutor Institucional</th>
                <th>Horas Validadas</th>
                <th>Acción</th>
              </tr>
            </thead>
            <tbody>
              {myAssignments.length === 0 ? (
                <tr>
                  <td colSpan={6} className="text-center py-4 text-muted">
                    No tienes estudiantes asignados actualmente.
                  </td>
                </tr>
              ) : (
                myAssignments.map(a => {
                  const student = users.find(u => u.id === a.studentId);
                  const period = periods.find(p => p.id === a.periodId);
                  const place = places.find(p => p.id === a.placeId);
                  const institution = institutions.find(i => i.id === place?.agreementId);
                  const studentHours = activities
                    .filter(act => act.assignmentId === a.id && act.status === "APROBADA")
                    .reduce((sum, act) => sum + act.hours, 0) || a.approvedHours;

                  return (
                    <tr key={a.id}>
                      <td>
                        <strong>{student?.name}</strong>
                        <div className="text-muted small">{student?.code}</div>
                      </td>
                      <td>{period?.program} ({period?.year}-{period?.semester})</td>
                      <td>{institution?.name}</td>
                      <td>{place?.tutor}</td>
                      <td>
                        <span className="fw-bold text-success">{studentHours}</span> / {period?.minHours || 160} hrs
                      </td>
                      <td>
                        <button className="btn btn-outline-primary btn-sm">
                          👁️ Ver Bitácoras
                        </button>
                      </td>
                    </tr>
                  );
                })
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
