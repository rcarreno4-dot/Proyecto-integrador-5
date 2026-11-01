import React from "react";
import { useSigpra } from "../../context/SigpraContext";
import { KPICard } from "../common/KPICard";

export const CoordinatorDashboard: React.FC = () => {
  const { periods, agreements, assignments, activities, users, setCurrentView } = useSigpra();

  const publishedPeriods = periods.filter(p => p.status === "PUBLICADO");
  const activeAgreements = agreements.filter(a => a.status === "VIGENTE");
  const totalApprovedHours = activities
    .filter(a => a.status === "APROBADA")
    .reduce((sum, a) => sum + a.hours, 0);

  const studentsCount = users.filter(u => u.role === "ESTUDIANTE").length;

  return (
    <div>
      <div className="row g-3 mb-4">
        <div className="col-md-3">
          <KPICard
            title="Periodos Activos"
            value={publishedPeriods.length}
            subtitle="Convocatorias en curso"
            icon="📅"
            variant="blue"
            onClick={() => setCurrentView("periods")}
            ctaText="Gestionar periodos"
          />
        </div>
        <div className="col-md-3">
          <KPICard
            title="Convenios Vigentes"
            value={activeAgreements.length}
            subtitle="Instituciones aliadas"
            icon="🏫"
            variant="green"
            onClick={() => setCurrentView("agreements")}
            ctaText="Ver convenios"
          />
        </div>
        <div className="col-md-3">
          <KPICard
            title="Practicantes"
            value={studentsCount}
            subtitle={`${assignments.length} con plaza asignada`}
            icon="🎓"
            variant="amber"
            onClick={() => setCurrentView("assignments")}
            ctaText="Gestionar asignaciones"
          />
        </div>
        <div className="col-md-3">
          <KPICard
            title="Horas Validadas"
            value={`${totalApprovedHours}h`}
            subtitle="Trazabilidad Decreto 1075"
            icon="⏱️"
            variant="purple"
            onClick={() => setCurrentView("reports")}
            ctaText="Ver consolidado"
          />
        </div>
      </div>

      <div className="row g-4">
        <div className="col-md-8">
          <div className="card border rounded-3 shadow-xs h-100">
            <div className="card-header bg-white py-3 d-flex justify-content-between align-items-center">
              <h5 className="mb-0 fw-bold">Periodos Académicos Activos</h5>
              <button onClick={() => setCurrentView("periods")} className="btn btn-outline-primary btn-sm">
                Gestionar todos ➔
              </button>
            </div>
            <div className="table-responsive">
              <table className="table table-hover align-middle mb-0">
                <thead className="table-light">
                  <tr>
                    <th>Programa</th>
                    <th>Nivel</th>
                    <th>Vigencia</th>
                    <th>Horas</th>
                    <th>Estado</th>
                  </tr>
                </thead>
                <tbody>
                  {publishedPeriods.map(p => (
                    <tr key={p.id}>
                      <td><strong>{p.program}</strong></td>
                      <td>{p.level}</td>
                      <td>{p.start} a {p.end}</td>
                      <td><span className="badge bg-secondary-subtle text-dark">{p.minHours} hrs</span></td>
                      <td><span className="badge bg-success">{p.status}</span></td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <div className="col-md-4">
          <div className="card border rounded-3 shadow-xs p-3">
            <h5 className="fw-bold mb-2">Acciones Rápidas de Coordinación</h5>
            <p className="text-muted small mb-3">Accede directamente a los flujos operativos clave.</p>
            <div className="d-flex flex-column gap-2">
              <button onClick={() => setCurrentView("periods")} className="btn btn-primary text-start fw-bold">
                ➕ Nuevo Periodo Académico
              </button>
              <button onClick={() => setCurrentView("assignments")} className="btn btn-outline-primary text-start">
                👥 Asignar Practicantes (CU-03)
              </button>
              <button onClick={() => setCurrentView("reports")} className="btn btn-outline-secondary text-start">
                📊 Consolidado e Informes Oficiales
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
