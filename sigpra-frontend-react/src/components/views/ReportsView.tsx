import React, { useState, useMemo } from "react";
import { useSigpra } from "../../context/SigpraContext";

export const ReportsView: React.FC = () => {
  const { periods, institutions, users, assignments, activities, places, auditAction } = useSigpra();

  const [periodId, setPeriodId] = useState<string>("");
  const [program, setProgram] = useState<string>("");
  const [institutionId, setInstitutionId] = useState<string>("");
  const [teacherId, setTeacherId] = useState<string>("");
  const [hasSearched, setHasSearched] = useState<boolean>(false);
  const [searchTerm, setSearchTerm] = useState<string>("");

  const programs: string[] = Array.from(new Set(periods.map(p => p.program)));
  const teachers = users.filter(u => u.role === "DOCENTE_ASESOR");

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    setHasSearched(true);
    auditAction(`Generó búsqueda y aplicó filtros en consolidado (Periodo: ${periodId || "Todos"}, Programa: ${program || "Todos"})`, "REPORTE");
  };

  const handleReset = () => {
    setPeriodId("");
    setProgram("");
    setInstitutionId("");
    setTeacherId("");
    setSearchTerm("");
    setHasSearched(false);
  };

  const filteredAssignments = useMemo(() => {
    if (!hasSearched) return [];
    return assignments.filter(a => {
      if (a.status !== "ACTIVA") return false;
      const period = periods.find(p => p.id === a.periodId);
      const place = places.find(p => p.id === a.placeId);
      const institution = institutions.find(i => i.id === place?.agreementId);

      if (periodId && a.periodId !== Number(periodId)) return false;
      if (program && period?.program !== program) return false;
      if (institutionId && institution?.id !== Number(institutionId)) return false;
      if (teacherId && a.teacherId !== Number(teacherId)) return false;

      return true;
    });
  }, [assignments, periods, places, institutions, periodId, program, institutionId, teacherId, hasSearched]);

  const visibleRows = useMemo(() => {
    if (!searchTerm.trim()) return filteredAssignments;
    const term = searchTerm.toLowerCase();
    return filteredAssignments.filter(a => {
      const student = users.find(u => u.id === a.studentId);
      const teacher = users.find(u => u.id === a.teacherId);
      const place = places.find(p => p.id === a.placeId);
      const institution = institutions.find(i => i.id === place?.agreementId);

      return (
        student?.name.toLowerCase().includes(term) ||
        teacher?.name.toLowerCase().includes(term) ||
        institution?.name.toLowerCase().includes(term)
      );
    });
  }, [filteredAssignments, searchTerm, users, places, institutions]);

  const handleExportCsv = () => {
    const headers = [
      "Periodo", "Programa", "Código", "Estudiante", "Institución", "Docente", "Horas", "Meta", "Avance", "Estado"
    ];

    const rows = visibleRows.map(a => {
      const student = users.find(u => u.id === a.studentId);
      const teacher = users.find(u => u.id === a.teacherId);
      const period = periods.find(p => p.id === a.periodId);
      const place = places.find(p => p.id === a.placeId);
      const institution = institutions.find(i => i.id === place?.agreementId);

      const approvedHours = activities
        .filter(act => act.assignmentId === a.id && act.status === "APROBADA")
        .reduce((sum, act) => sum + act.hours, 0) || a.approvedHours;

      const required = period?.minHours || 160;
      const percent = Math.min(100, Math.round((approvedHours / required) * 100));

      return [
        `${period?.year || 2026}-${period?.semester || 2}`,
        period?.program || "N/A",
        student?.code || `UDI-${student?.id}`,
        student?.name || "N/A",
        institution?.name || "N/A",
        teacher?.name || "N/A",
        `${approvedHours} hrs`,
        `${required} hrs`,
        `${percent}%`,
        a.status
      ];
    });

    const csvContent = [headers, ...rows]
      .map(row => row.map(cell => `"${String(cell).replace(/"/g, '""')}"`).join(";"))
      .join("\r\n");

    const bom = "\ufeff";
    const blob = new Blob([bom + csvContent], { type: "text/csv;charset=utf-8" });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = `SIGPRA_Consolidado_Practicas_${new Date().toISOString().split("T")[0]}.csv`;
    link.click();
    auditAction(`Exportó reporte tabular CSV (${rows.length} registros)`, "REPORTE");
  };

  const handlePrint = () => {
    auditAction("Generó impresión oficial UDI del reporte consolidado", "REPORTE");
    window.print();
  };

  const totalApprovedHours = visibleRows.reduce((acc, a) => {
    const h = activities
      .filter(act => act.assignmentId === a.id && act.status === "APROBADA")
      .reduce((s, act) => s + act.hours, 0) || a.approvedHours;
    return acc + h;
  }, 0);

  return (
    <div className="space-y-4">
      <div className="d-none d-print-block border-bottom pb-3 mb-4">
        <div className="d-flex align-items-center justify-content-between">
          <div>
            <h2 className="text-primary fw-bold mb-0" style={{ fontSize: "1.2rem" }}>
              UNIVERSIDAD DE INVESTIGACIÓN Y DESARROLLO — UDI
            </h2>
            <div className="text-muted small">Facultad de Ingenierías · Programa de Ingeniería de Sistemas</div>
            <div className="text-secondary small">SIGPRA — Sistema Integral de Gestión de Prácticas Académicas</div>
          </div>
          <div className="text-end small text-muted">
            <div className="fw-bold text-dark">INFORME OFICIAL CONSOLIDADO</div>
            <div>Decreto 1075 de 2015</div>
            <div>Fecha: {new Date().toLocaleDateString("es-CO")}</div>
          </div>
        </div>
      </div>

      <div className="d-flex justify-content-between align-items-center mb-3 d-print-none">
        <div>
          <h2 className="fw-bold mb-1" style={{ color: "#004b87" }}>
            Informes y Consolidado de Prácticas
          </h2>
          <p className="text-muted small mb-0">
            Aplica los filtros de búsqueda para consultar el reporte detallado y habilitar opciones de exportación.
          </p>
        </div>
      </div>

      <div className="card p-3 border rounded-3 mb-4 shadow-xs d-print-none">
        <form onSubmit={handleSearch} className="row g-3 align-items-end">
          <div className="col-md-3">
            <label className="form-label small fw-bold">Periodo académico</label>
            <select className="form-select form-select-sm" value={periodId} onChange={e => setPeriodId(e.target.value)}>
              <option value="">Todos los periodos</option>
              {periods.map(p => (
                <option key={p.id} value={p.id}>
                  {p.year}-{p.semester} — {p.program} ({p.level})
                </option>
              ))}
            </select>
          </div>

          <div className="col-md-3">
            <label className="form-label small fw-bold">Programa académico</label>
            <select className="form-select form-select-sm" value={program} onChange={e => setProgram(e.target.value)}>
              <option value="">Todos los programas</option>
              {programs.map(prg => (
                <option key={prg} value={prg}>
                  {prg}
                </option>
              ))}
            </select>
          </div>

          <div className="col-md-3">
            <label className="form-label small fw-bold">Institución receptora</label>
            <select className="form-select form-select-sm" value={institutionId} onChange={e => setInstitutionId(e.target.value)}>
              <option value="">Todas las instituciones</option>
              {institutions.map(i => (
                <option key={i.id} value={i.id}>
                  {i.name}
                </option>
              ))}
            </select>
          </div>

          <div className="col-md-3">
            <label className="form-label small fw-bold">Docente asesor</label>
            <select className="form-select form-select-sm" value={teacherId} onChange={e => setTeacherId(e.target.value)}>
              <option value="">Todos los docentes</option>
              {teachers.map(t => (
                <option key={t.id} value={t.id}>
                  {t.name}
                </option>
              ))}
            </select>
          </div>

          <div className="col-12 d-flex gap-2">
            <button type="submit" className="btn btn-primary btn-sm px-4 fw-bold shadow-xs">
              🔍 Buscar / Generar Reporte
            </button>
            {hasSearched && (
              <button type="button" onClick={handleReset} className="btn btn-outline-secondary btn-sm">
                🔄 Limpiar Filtros
              </button>
            )}
          </div>
        </form>
      </div>

      {!hasSearched ? (
        <div className="card p-5 text-center border-0 rounded-4 shadow-sm bg-light my-4">
          <div className="fs-1 mb-2">📊</div>
          <h3 className="fw-bold text-primary mb-2">Módulo de Informes y Consolidado de Prácticas</h3>
          <p className="text-muted mx-auto mb-3" style={{ maxWidth: 560 }}>
            Configura los filtros de búsqueda en el formulario superior y haz clic en{" "}
            <strong>"Buscar / Generar Reporte"</strong> para desplegar la información consolidada, la tabla de avance y
            las opciones de exportación e impresión oficial.
          </p>
          <div>
            <button
              type="button"
              onClick={() => setHasSearched(true)}
              className="btn btn-primary px-4 py-2 fw-bold shadow-xs"
            >
              🔍 Ver Consolidado General de Prácticas
            </button>
          </div>
        </div>
      ) : (
        <>
          <div className="card p-3 bg-light border rounded-3 mb-3 d-flex flex-row justify-content-between align-items-center flex-wrap gap-2 d-print-none">
            <div className="d-flex align-items-center gap-2">
              <span className="badge bg-primary fs-6">{visibleRows.length} práctica(s) encontrada(s)</span>
              <span className="text-muted small">| Filtros aplicados</span>
            </div>
            <div className="d-flex align-items-center gap-2">
              <button onClick={handleExportCsv} className="btn btn-success btn-sm fw-bold shadow-xs">
                📊 Exportar a Excel (CSV)
              </button>
              <button onClick={handlePrint} className="btn btn-primary btn-sm fw-bold shadow-xs">
                🖨️ Imprimir / Guardar PDF Oficial
              </button>
            </div>
          </div>

          <div className="row g-3 mb-4 d-print-none">
            <div className="col-md-3">
              <div className="card p-3 border bg-white rounded-3 shadow-xs text-center">
                <span className="text-muted small text-uppercase fw-bold">Prácticas Activas</span>
                <h3 className="mb-0 fw-bold text-primary">{visibleRows.length}</h3>
              </div>
            </div>
            <div className="col-md-3">
              <div className="card p-3 border bg-white rounded-3 shadow-xs text-center">
                <span className="text-muted small text-uppercase fw-bold">Horas Aprobadas</span>
                <h3 className="mb-0 fw-bold text-success">{totalApprovedHours} hrs</h3>
              </div>
            </div>
            <div className="col-md-3">
              <div className="card p-3 border bg-white rounded-3 shadow-xs text-center">
                <span className="text-muted small text-uppercase fw-bold">Meta Normativa</span>
                <h3 className="mb-0 fw-bold text-secondary">160h MEN</h3>
              </div>
            </div>
            <div className="col-md-3">
              <div className="card p-3 border bg-white rounded-3 shadow-xs text-center">
                <span className="text-muted small text-uppercase fw-bold">Tope Máximo</span>
                <h3 className="mb-0 fw-bold text-dark">320h</h3>
              </div>
            </div>
          </div>

          <div className="d-flex justify-content-between align-items-center mb-2 d-print-none">
            <h5 className="fw-bold mb-0">Detalle de Prácticas Académicas</h5>
            <div className="input-group input-group-sm" style={{ maxWidth: 320 }}>
              <span className="input-group-text bg-white">🔍</span>
              <input
                type="text"
                className="form-control"
                placeholder="Filtrar por estudiante, empresa o tutor..."
                value={searchTerm}
                onChange={e => setSearchTerm(e.target.value)}
              />
            </div>
          </div>

          <div className="table-responsive border rounded-3 bg-white shadow-xs">
            <table className="table table-hover align-middle mb-0" id="reports-table">
              <thead className="table-light">
                <tr>
                  <th>Periodo</th>
                  <th>Programa</th>
                  <th>Estudiante</th>
                  <th>Institución Receptora</th>
                  <th>Docente Asesor</th>
                  <th>Horas</th>
                  <th>Avance</th>
                  <th>Estado</th>
                </tr>
              </thead>
              <tbody>
                {visibleRows.length === 0 ? (
                  <tr>
                    <td colSpan={8} className="text-center py-4 text-muted">
                      ⚠️ No se encontraron prácticas para los criterios seleccionados.
                    </td>
                  </tr>
                ) : (
                  visibleRows.map(a => {
                    const student = users.find(u => u.id === a.studentId);
                    const teacher = users.find(u => u.id === a.teacherId);
                    const period = periods.find(p => p.id === a.periodId);
                    const place = places.find(p => p.id === a.placeId);
                    const institution = institutions.find(i => i.id === place?.agreementId);

                    const approvedHours = activities
                      .filter(act => act.assignmentId === a.id && act.status === "APROBADA")
                      .reduce((sum, act) => sum + act.hours, 0) || a.approvedHours;

                    const required = period?.minHours || 160;
                    const percent = Math.min(100, Math.round((approvedHours / required) * 100));

                    return (
                      <tr key={a.id}>
                        <td>{period?.year}-{period?.semester}</td>
                        <td>{period?.program}</td>
                        <td>
                          <strong>{student?.name}</strong>
                          <div className="text-muted small">{student?.code || student?.email}</div>
                        </td>
                        <td>{institution?.name}</td>
                        <td>{teacher?.name}</td>
                        <td>
                          <span className="fw-bold">{approvedHours}</span> / {required} hrs
                        </td>
                        <td style={{ minWidth: 120 }}>
                          <div className="progress" style={{ height: 6 }}>
                            <div className="progress-bar bg-success" style={{ width: `${percent}%` }} />
                          </div>
                          <span className="small text-muted">{percent}%</span>
                        </td>
                        <td>
                          <span className="badge bg-success-subtle text-success border border-success">
                            {a.status}
                          </span>
                        </td>
                      </tr>
                    );
                  })
                )}
              </tbody>
            </table>
          </div>

          <div className="d-none d-print-block mt-5 pt-4">
            <div className="row text-center">
              <div className="col-6">
                <div style={{ borderTop: "1px solid #475569", width: "75%", margin: "45px auto 4px" }} />
                <strong style={{ fontSize: "0.88rem" }}>Coordinación de Prácticas Académicas</strong>
                <div className="small text-muted">Facultad de Ingenierías — UDI</div>
              </div>
              <div className="col-6">
                <div style={{ borderTop: "1px solid #475569", width: "75%", margin: "45px auto 4px" }} />
                <strong style={{ fontSize: "0.88rem" }}>Dirección de Programa</strong>
                <div className="small text-muted">Ingeniería de Sistemas — UDI</div>
              </div>
            </div>
          </div>
        </>
      )}
    </div>
  );
};
