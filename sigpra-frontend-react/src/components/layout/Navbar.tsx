import React from "react";
import { useSigpra } from "../../context/SigpraContext";
import { NotificationBell } from "./NotificationBell";

interface NavbarProps {
  onLogout: () => void;
  isNavidad?: boolean;
  onToggleNavidad?: () => void;
}

export const Navbar: React.FC<NavbarProps> = ({ onLogout, isNavidad, onToggleNavidad }) => {
  const { currentUser } = useSigpra();

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
          <div className="position-relative">
            <div
              className="rounded-3 d-flex align-items-center justify-content-center text-white fw-bold shadow-sm"
              style={{ width: 40, height: 40, background: "#0f766e", fontSize: "1.2rem" }}
            >
              S
            </div>
            {isNavidad && (
              <span
                style={{
                  position: "absolute",
                  top: -12,
                  left: -8,
                  fontSize: "1.2rem",
                  pointerEvents: "none",
                  transform: "rotate(-15deg)"
                }}
              >
                🎅
              </span>
            )}
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
          {onToggleNavidad && (
            <button
              className={`btn btn-sm ${isNavidad ? "btn-danger fw-bold shadow" : "btn-outline-info"} d-flex align-items-center gap-1 shadow-sm px-2 py-1`}
              onClick={onToggleNavidad}
              type="button"
              style={{ fontSize: "0.82rem", borderRadius: "20px" }}
              title={isNavidad ? "Desactivar Modo Navidad" : "Activar Modo Navidad"}
            >
              {isNavidad ? "🎄 Navidad ON (❄️)" : "❄️ Modo Navidad"}
            </button>
          )}
          <NotificationBell />
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

          <button className="btn btn-outline-light btn-sm" onClick={onLogout} type="button">
            Cerrar sesión
          </button>
        </div>
      </div>
    </header>
  );
};
