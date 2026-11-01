import React from "react";
import { useSigpra } from "../../context/SigpraContext";

export const AuditView: React.FC = () => {
  const { currentUser, auditLogs } = useSigpra();

  if (currentUser.role !== "DIRECTOR") {
    return (
      <div className="alert alert-danger p-4 text-center">
        <h4>🔒 Acceso Restringido</h4>
        <p className="mb-0">El registro de auditoría del sistema es exclusivo para la Dirección de Programa.</p>
      </div>
    );
  }

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <div>
          <h2 className="fw-bold mb-1" style={{ color: "#004b87" }}>
            Trazabilidad y Registro de Auditoría
          </h2>
          <p className="text-muted small mb-0">
            Historial inmutable de eventos, dictámenes y acciones de usuarios en SIGPRA.
          </p>
        </div>
        <span className="badge bg-primary px-3 py-2">
          {auditLogs.length} eventos registrados
        </span>
      </div>

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
            {auditLogs.map(log => (
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
