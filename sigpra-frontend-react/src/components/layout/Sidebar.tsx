import React from "react";
import { useSigpra } from "../../context/SigpraContext";

interface NavItem {
  id: string;
  label: string;
  icon: string;
  group: string;
}

export const Sidebar: React.FC = () => {
  const { currentUser, currentView, setCurrentView, assignments } = useSigpra();

  const hasActiveAssignment = assignments.some(
    a => a.studentId === currentUser.id && a.status === "ACTIVA"
  );

  const getNavItems = (): NavItem[] => {
    const shared: NavItem[] = [
      { id: "dashboard", label: "Inicio", icon: "🏠", group: "Principal" }
    ];

    switch (currentUser.role) {
      case "COORDINADOR":
        return [
          ...shared,
          { id: "periods", label: "Periodos de práctica", icon: "📅", group: "Gestión Formativa" },
          { id: "agreements", label: "Convenios y plazas", icon: "🏫", group: "Gestión Formativa" },
          { id: "assignments", label: "Asignaciones", icon: "🎓", group: "Seguimiento" },
          { id: "director-requests", label: "Solicitudes radicadas", icon: "📑", group: "Seguimiento" },
          { id: "reports", label: "Consolidado de horas", icon: "📊", group: "Seguimiento" }
        ];

      case "DIRECTOR":
        return [
          ...shared,
          { id: "director-requests", label: "Dictamen de solicitudes", icon: "⚖️", group: "Gestión y Habilitación" },
          { id: "reports", label: "Consolidado general", icon: "📊", group: "Auditoría e Informes" },
          { id: "audit", label: "Registro de auditoría", icon: "🔒", group: "Auditoría e Informes" }
        ];

      case "ESTUDIANTE":
        return [
          ...shared,
          ...(!hasActiveAssignment
            ? [{ id: "student-request", label: "Solicitar práctica", icon: "📝", group: "Mi Práctica" }]
            : []),
          { id: "student-progress", label: "Mi avance (160h)", icon: "⏱️", group: "Mi Práctica" },
          { id: "student-activities", label: "Mi bitácora diaria", icon: "📋", group: "Mi Práctica" }
        ];

      case "DOCENTE_ASESOR":
        return [
          ...shared,
          { id: "teacher-validations", label: "Validar bitácoras", icon: "✅", group: "Acompañamiento" },
          { id: "teacher-visits", label: "Visitas y evaluación", icon: "📍", group: "Acompañamiento" }
        ];

      default:
        return shared;
    }
  };

  const navItems = getNavItems();
  const groups = Array.from(new Set(navItems.map(item => item.group)));

  return (
    <aside className="bg-white border-end d-flex flex-column p-3 d-print-none" style={{ width: 260, minHeight: "calc(100vh - 60px)" }}>
      <div className="flex-grow-1">
        {groups.map(groupName => (
          <div key={groupName} className="mb-3">
            <div className="text-uppercase text-muted fw-bold mb-2" style={{ fontSize: "0.68rem", letterSpacing: "0.05em" }}>
              {groupName}
            </div>
            <ul className="nav nav-pills flex-column gap-1">
              {navItems
                .filter(item => item.group === groupName)
                .map(item => {
                  const isActive = currentView === item.id;
                  return (
                    <li key={item.id} className="nav-item">
                      <button
                        className={`nav-link text-start w-100 d-flex align-items-center gap-2 py-2 px-3 rounded-3 border-0 transition-all ${
                          isActive
                            ? "bg-primary text-white fw-bold shadow-sm"
                            : "text-dark bg-transparent hover-bg-light"
                        }`}
                        style={{ fontSize: "0.88rem", cursor: "pointer" }}
                        onClick={() => setCurrentView(item.id)}
                      >
                        <span>{item.icon}</span>
                        <span>{item.label}</span>
                      </button>
                    </li>
                  );
                })}
            </ul>
          </div>
        ))}
      </div>

      <div className="p-3 bg-light rounded-3 border mt-auto text-center">
        <span className="badge bg-secondary-subtle text-dark border mb-1">Decreto 1075 de 2015</span>
        <div className="text-muted" style={{ fontSize: "0.72rem" }}>
          Meta: 160h mínimas · Tope: 320h
        </div>
      </div>
    </aside>
  );
};
