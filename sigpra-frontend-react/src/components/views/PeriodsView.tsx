import React, { useState } from "react";
import { useSigpra } from "../../context/SigpraContext";

export const PeriodsView: React.FC = () => {
  const { periods, addPeriod, closePeriod } = useSigpra();

  const [showModal, setShowModal] = useState(false);
  const [program, setProgram] = useState("Ingeniería de Sistemas");
  const [level, setLevel] = useState("IX semestre");
  const [year, setYear] = useState(new Date().getFullYear());
  const [semester, setSemester] = useState(2);
  const [start, setStart] = useState("");
  const [end, setEnd] = useState("");
  const [reportLimit, setReportLimit] = useState("");
  const [minHours, setMinHours] = useState(160);

  const todayStr = new Date().toISOString().split("T")[0];

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const success = addPeriod({
      program,
      level,
      year: Number(year),
      semester: Number(semester),
      start,
      end,
      reportLimit,
      minHours: Number(minHours),
      status: "PUBLICADO",
      rubric: [
        { name: "Cumplimiento de horas", weight: 40 },
        { name: "Validación de bitácoras", weight: 30 },
        { name: "Visitas de tutor", weight: 30 }
      ]
    });

    if (success) {
      setShowModal(false);
      setStart("");
      setEnd("");
      setReportLimit("");
    }
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <div>
          <h2 className="fw-bold mb-1" style={{ color: "#004b87" }}>
            Periodos Académicos de Práctica
          </h2>
          <p className="text-muted small mb-0">
            Define fechas de vigencia, intensidad horaria reglamentaria y convocatorias activas.
          </p>
        </div>
        <button onClick={() => setShowModal(true)} className="btn btn-primary btn-sm px-3 fw-bold shadow-xs">
          ➕ Nuevo Periodo Académico
        </button>
      </div>

      <div className="table-responsive border rounded-3 bg-white shadow-xs">
        <table className="table table-hover align-middle mb-0">
          <thead className="table-light">
            <tr>
              <th>Programa</th>
              <th>Nivel</th>
              <th>Periodo</th>
              <th>Vigencia</th>
              <th>Límite Reporte</th>
              <th>Horas Mínimas</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {periods.map(p => (
              <tr key={p.id}>
                <td><strong>{p.program}</strong></td>
                <td>{p.level}</td>
                <td><span className="badge bg-light text-dark border">{p.year}-{p.semester}</span></td>
                <td>{p.start} a {p.end}</td>
                <td>{p.reportLimit}</td>
                <td><span className="badge bg-secondary-subtle text-dark">{p.minHours} hrs</span></td>
                <td>
                  <span className={`badge ${p.status === "PUBLICADO" ? "bg-success" : p.status === "CERRADO" ? "bg-secondary" : "bg-warning text-dark"}`}>
                    {p.status}
                  </span>
                </td>
                <td>
                  {p.status === "PUBLICADO" ? (
                    <button
                      onClick={() => {
                        if (confirm(`¿Está seguro de cerrar formalmente el periodo de ${p.program} (${p.year}-${p.semester})?`)) {
                          closePeriod(p.id);
                        }
                      }}
                      className="btn btn-outline-danger btn-sm py-0 px-2"
                      style={{ fontSize: "0.78rem" }}
                      title="Cerrar formalmente este periodo académico"
                    >
                      🔒 Cerrar
                    </button>
                  ) : (
                    <span className="text-muted small">—</span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {showModal && (
        <div className="modal show d-block" style={{ backgroundColor: "rgba(15,23,42,0.6)" }}>
          <div className="modal-dialog modal-lg">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title fw-bold">Configuración de Nuevo Periodo Académico</h5>
                <button type="button" className="btn-close" onClick={() => setShowModal(false)} />
              </div>
              <form onSubmit={handleSubmit}>
                <div className="modal-body row g-3">
                  <div className="col-md-6">
                    <label className="form-label small fw-bold">Programa académico *</label>
                    <select className="form-select form-select-sm" value={program} onChange={e => setProgram(e.target.value)}>
                      <option value="Ingeniería de Sistemas">Ingeniería de Sistemas</option>
                      <option value="Licenciatura en Educación Infantil">Licenciatura en Educación Infantil</option>
                      <option value="Ingeniería Industrial">Ingeniería Industrial</option>
                    </select>
                  </div>

                  <div className="col-md-6">
                    <label className="form-label small fw-bold">Nivel formativo *</label>
                    <select className="form-select form-select-sm" value={level} onChange={e => setLevel(e.target.value)}>
                      <option value="VIII semestre">VIII semestre</option>
                      <option value="IX semestre">IX semestre</option>
                      <option value="X semestre">X semestre</option>
                    </select>
                  </div>

                  <div className="col-md-6">
                    <label className="form-label small fw-bold">Año académico *</label>
                    <input
                      type="number"
                      className="form-control form-control-sm"
                      min={2020}
                      max={2035}
                      value={year}
                      onChange={e => setYear(Number(e.target.value))}
                      required
                    />
                  </div>

                  <div className="col-md-6">
                    <label className="form-label small fw-bold">Semestre académico *</label>
                    <select className="form-select form-select-sm" value={semester} onChange={e => setSemester(Number(e.target.value))}>
                      <option value={1}>1 (Primer semestre)</option>
                      <option value={2}>2 (Segundo semestre)</option>
                    </select>
                  </div>

                  <div className="col-md-4">
                    <label className="form-label small fw-bold">Fecha inicio *</label>
                    <input
                      type="date"
                      className="form-control form-control-sm"
                      min={todayStr}
                      value={start}
                      onChange={e => setStart(e.target.value)}
                      required
                    />
                    <small className="text-muted" style={{ fontSize: "0.7rem" }}>No permite fechas pasadas</small>
                  </div>

                  <div className="col-md-4">
                    <label className="form-label small fw-bold">Fecha fin *</label>
                    <input
                      type="date"
                      className="form-control form-control-sm"
                      min={todayStr}
                      value={end}
                      onChange={e => setEnd(e.target.value)}
                      required
                    />
                  </div>

                  <div className="col-md-4">
                    <label className="form-label small fw-bold">Fecha límite reporte *</label>
                    <input
                      type="date"
                      className="form-control form-control-sm"
                      min={todayStr}
                      value={reportLimit}
                      onChange={e => setReportLimit(e.target.value)}
                      required
                    />
                  </div>

                  <div className="col-md-12">
                    <label className="form-label small fw-bold">Intensidad horaria mínima reglamentaria (máx. 320 hrs) *</label>
                    <input
                      type="number"
                      className="form-control form-control-sm"
                      min={1}
                      max={320}
                      value={minHours}
                      onChange={e => setMinHours(Number(e.target.value))}
                      required
                    />
                    <small className="text-muted" style={{ fontSize: "0.75rem" }}>
                      Decreto 1075 de 2015: mínimo 160h, tope máximo configurable hasta 320h.
                    </small>
                  </div>
                </div>

                <div className="modal-footer">
                  <button type="button" className="btn btn-secondary btn-sm" onClick={() => setShowModal(false)}>
                    Cancelar
                  </button>
                  <button type="submit" className="btn btn-primary btn-sm fw-bold">
                    🚀 Publicar Periodo Académico
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
