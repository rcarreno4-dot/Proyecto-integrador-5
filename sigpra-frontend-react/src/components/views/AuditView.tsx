import React, { useMemo, useState } from "react";
import { useSigpra } from "../../context/SigpraContext";

export const AuditView: React.FC = () => {
  const { currentUser, auditLogs } = useSigpra();
  const [search, setSearch] = useState("");
  const [selectedUser, setSelectedUser] = useState("");
  const [selectedEntity, setSelectedEntity] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");

  if (currentUser.role !== "DIRECTOR" && currentUser.role !== "COORDINADOR") {
    return (
      <div className="alert alert-danger p-4 text-center">
        <h4>🔒 Acceso Restringido</h4>
        <p className="mb-0">El registro de auditoría del sistema es exclusivo para la Coordinación y Dirección de Programa.</p>
      </div>
    );
  }

  const users = useMemo(
    () => Array.from(new Set(auditLogs.map(log => log.user))).sort((a, b) => a.localeCompare(b, "es")),
    [auditLogs]
  );
  const entities = useMemo(
    () => Array.from(new Set(auditLogs.map(log => log.entity))).sort((a, b) => a.localeCompare(b, "es")),
    [auditLogs]
  );
  const filteredLogs = useMemo(() => {
    const term = search.trim().toLocaleLowerCase("es");
    return auditLogs.filter(log => {
      const logDate = log.date.slice(0, 10);
      const matchesSearch = !term
        || log.user.toLocaleLowerCase("es").includes(term)
        || log.event.toLocaleLowerCase("es").includes(term)
        || log.entity.toLocaleLowerCase("es").includes(term);
      return matchesSearch
        && (!selectedUser || log.user === selectedUser)
        && (!selectedEntity || log.entity === selectedEntity)
        && (!startDate || logDate >= startDate)
        && (!endDate || logDate <= endDate);
    });
  }, [auditLogs, search, selectedUser, selectedEntity, startDate, endDate]);

  const resetFilters = () => {
    setSearch("");
    setSelectedUser("");
    setSelectedEntity("");
    setStartDate("");
    setEndDate("");
  };

  const exportCsv = () => {
    const rows = [
      ["Fecha y hora", "Usuario responsable", "Acción realizada", "Módulo / entidad"],
      ...filteredLogs.map(log => [log.date, log.user, log.event, log.entity])
    ];
    const csv = rows
      .map(row => row.map(value => `"${value.replace(/"/g, '""')}"`).join(";"))
      .join("\r\n");
    const blob = new Blob(["\ufeff", csv], { type: "text/csv;charset=utf-8" });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = `SIGPRA_Auditoria_${new Date().toISOString().slice(0, 10)}.csv`;
    link.click();
    window.setTimeout(() => URL.revokeObjectURL(url), 1000);
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <div>
          <h2 className="fw-bold mb-1" style={{ color: "#004b87" }}>
            Trazabilidad y Registro de Auditoría
          </h2>
          <p className="text-muted small mb-0">
            Consulta y filtra los eventos, dictámenes y acciones de usuarios registrados en SIGPRA.
          </p>
        </div>
        <span className="badge bg-primary px-3 py-2">
          {filteredLogs.length} de {auditLogs.length} eventos
        </span>
      </div>

      <section className="card border-0 shadow-xs p-3 mb-3">
        <div className="row g-2 align-items-end">
          <div className="col-lg-3 col-md-6">
            <label className="form-label small fw-bold" htmlFor="audit-search">Buscar</label>
            <input
              className="form-control form-control-sm"
              id="audit-search"
              onChange={event => setSearch(event.target.value)}
              placeholder="Acción, usuario o entidad"
              value={search}
            />
          </div>
          <div className="col-lg-2 col-md-6">
            <label className="form-label small fw-bold" htmlFor="audit-user">Usuario</label>
            <select className="form-select form-select-sm" id="audit-user" onChange={event => setSelectedUser(event.target.value)} value={selectedUser}>
              <option value="">Todos</option>
              {users.map(user => <option key={user} value={user}>{user}</option>)}
            </select>
          </div>
          <div className="col-lg-2 col-md-6">
            <label className="form-label small fw-bold" htmlFor="audit-entity">Módulo / entidad</label>
            <select className="form-select form-select-sm" id="audit-entity" onChange={event => setSelectedEntity(event.target.value)} value={selectedEntity}>
              <option value="">Todos</option>
              {entities.map(entity => <option key={entity} value={entity}>{entity}</option>)}
            </select>
          </div>
          <div className="col-lg-2 col-md-6">
            <label className="form-label small fw-bold" htmlFor="audit-start">Desde</label>
            <input className="form-control form-control-sm" id="audit-start" type="date" onChange={event => setStartDate(event.target.value)} value={startDate} />
          </div>
          <div className="col-lg-2 col-md-6">
            <label className="form-label small fw-bold" htmlFor="audit-end">Hasta</label>
            <input className="form-control form-control-sm" id="audit-end" type="date" min={startDate || undefined} onChange={event => setEndDate(event.target.value)} value={endDate} />
          </div>
          <div className="col-lg-1 col-md-6 d-flex gap-2">
            <button aria-label="Limpiar filtros" className="btn btn-outline-secondary btn-sm flex-grow-1" onClick={resetFilters} type="button">Limpiar</button>
          </div>
        </div>
        <div className="d-flex justify-content-end mt-3">
          <button className="btn btn-success btn-sm" disabled={!filteredLogs.length} onClick={exportCsv} type="button">
            Exportar CSV
          </button>
        </div>
      </section>

      <div className="table-responsive border rounded-3 bg-white shadow-xs">
        <table className="table table-hover align-middle mb-0">
          <thead className="table-light">
            <tr>
              <th>Fecha y Hora</th>
              <th>Usuario Responsable</th>
              <th>Acción Realizada</th>
              <th>Módulo / Entidad</th>
            </tr>
          </thead>
          <tbody>
            {filteredLogs.length === 0 ? (
              <tr><td className="text-center text-muted py-4" colSpan={4}>No hay eventos que coincidan con los filtros seleccionados.</td></tr>
            ) : filteredLogs.map(log => (
              <tr key={log.id}>
                <td className="text-muted small">{log.date}</td>
                <td><strong>{log.user}</strong></td>
                <td>{log.event}</td>
                <td>
                  <span className="badge bg-secondary-subtle text-dark border">
                    {log.entity}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
