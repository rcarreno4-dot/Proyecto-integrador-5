import React from "react";
import { useSigpra } from "../../context/SigpraContext";

export const Navbar: React.FC = () => {
  const { currentUser, setCurrentUser, users, setCurrentView } = useSigpra();

  const handleUserChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const selected = users.find(u => u.id === Number(e.target.value));
    if (selected) {
      setCurrentUser(selected);
      setCurrentView("dashboard");
    }
  };

  const getRoleBadge = (role: string) => {
    switch (role) {
      case "DIRECTOR": return "bg-primary";
      case "COORDINADOR": return "bg-success";
      case "DOCENTE_ASESOR": return "bg-warning text-dark";
      case "ESTUDIANTE": return "bg-info text-dark";
      default: return "bg-secondary";
    }
  };

  return (
    <header className="navbar navbar-expand-lg navbar-dark bg-dark px-4 py-2 shadow-sm d-print-none">
      <div className="container-fluid p-0">
        <div className="d-flex align-items-center gap-3">
          <div
            className="rounded-3 d-flex align-items-center justify-content-center text-white fw-bold shadow-sm"
            style={{ width: 40, height: 40, background: "#0f766e", fontSize: "1.2rem" }}
          >
            S
          </div>
          <div>
            <span className="navbar-brand mb-0 fw-bold fs-5 d-block text-white" style={{ letterSpacing: "-0.5px" }}>
              SIGPRA <span className="badge bg-primary text-uppercase" style={{ fontSize: "0.65rem", verticalAlign: "middle" }}>React 18</span>
            </span>
            <small className="text-secondary" style={{ fontSize: "0.75rem" }}>
              Universidad de Investigación y Desarrollo — UDI
            </small>
          </div>
        </div>

        <div className="d-flex align-items-center gap-3">
          <div className="d-flex align-items-center gap-2">
            <span className="text-light small d-none d-md-inline">Cambiar rol / cuenta:</span>
            <select
              className="form-select form-select-sm bg-secondary text-white border-0"
              style={{ width: "auto", minWidth: 230 }}
              value={currentUser.id}
              onChange={handleUserChange}
            >
              <optgroup label="Cuentas Institucionales UDI">
                {users.map(u => (
                  <option key={u.id} value={u.id}>
                    {u.name} ({u.role})
                  </option>
                ))}
              </optgroup>
            </select>
          </div>

          <div className="vr bg-secondary d-none d-md-block" style={{ height: 28 }} />

          <div className="d-flex align-items-center gap-2">
            <div
              className="rounded-circle bg-primary text-white d-flex align-items-center justify-content-center fw-bold"
              style={{ width: 34, height: 34, fontSize: "0.85rem" }}
            >
              {currentUser.initials}
            </div>
            <div className="d-none d-lg-block text-start">
              <div className="text-white small fw-bold leading-none">{currentUser.name}</div>
              <span className={`badge ${getRoleBadge(currentUser.role)}`} style={{ fontSize: "0.68rem" }}>
                {currentUser.role}
              </span>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
};
