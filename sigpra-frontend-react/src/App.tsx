import React, { useState } from "react";
import { SigpraProvider, useSigpra } from "./context/SigpraContext";
import { Navbar } from "./components/layout/Navbar";
import { Sidebar } from "./components/layout/Sidebar";
import { LoginView } from "./components/views/LoginView";
import { CoordinatorDashboard } from "./components/views/CoordinatorDashboard";
import { DirectorDashboard } from "./components/views/DirectorDashboard";
import { StudentDashboard } from "./components/views/StudentDashboard";
import { TeacherDashboard } from "./components/views/TeacherDashboard";
import { ReportsView } from "./components/views/ReportsView";
import { PeriodsView } from "./components/views/PeriodsView";
import { AuditView } from "./components/views/AuditView";
import { StudentRequestView } from "./components/views/StudentRequestView";
import { AgreementsView } from "./components/views/AgreementsView";
import { AssignmentsView } from "./components/views/AssignmentsView";
import { StudentActivitiesView } from "./components/views/StudentActivitiesView";
import { TeacherValidationsView } from "./components/views/TeacherValidationsView";
import { TeacherVisitsView } from "./components/views/TeacherVisitsView";
import { SnowOverlay } from "./components/common/SnowOverlay";

const MainContent: React.FC = () => {
  const { currentUser, currentView, toastMessage, setCurrentView } = useSigpra();
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isNavidad, setIsNavidad] = useState(() => {
    try {
      return localStorage.getItem("sigpra_modo_navidad") === "true";
    } catch {
      return false;
    }
  });

  const toggleNavidad = () => {
    setIsNavidad((prev) => {
      const next = !prev;
      try {
        localStorage.setItem("sigpra_modo_navidad", next ? "true" : "false");
      } catch {}
      return next;
    });
  };

  if (!isAuthenticated) {
    return (
      <>
        {isNavidad && <SnowOverlay />}
        <div style={{ position: "fixed", top: 16, right: 16, zIndex: 1080 }}>
          <button
            className={`btn btn-sm ${isNavidad ? "btn-danger fw-bold shadow" : "btn-outline-info"} d-flex align-items-center gap-1 shadow-sm px-2 py-1`}
            onClick={toggleNavidad}
            type="button"
            style={{ fontSize: "0.82rem", borderRadius: "20px" }}
          >
            {isNavidad ? "🎄 Navidad ON (❄️)" : "❄️ Modo Navidad"}
          </button>
        </div>
        <LoginView onLogin={() => {
          setCurrentView("dashboard");
          setIsAuthenticated(true);
        }} />
      </>
    );
  }

  const renderView = () => {
    switch (currentView) {
      case "dashboard":
        if (currentUser.role === "DIRECTOR") return <DirectorDashboard />;
        if (currentUser.role === "COORDINADOR") return <CoordinatorDashboard />;
        if (currentUser.role === "ESTUDIANTE") return <StudentDashboard />;
        if (currentUser.role === "DOCENTE_ASESOR") return <TeacherDashboard />;
        return <CoordinatorDashboard />;

      case "reports":
        return <ReportsView />;

      case "periods":
        return <PeriodsView />;

      case "agreements":
        return <AgreementsView />;

      case "assignments":
        return <AssignmentsView />;

      case "audit":
        return <AuditView />;

      case "student-request":
        return <StudentRequestView />;

      case "student-activities":
        return <StudentActivitiesView />;

      case "director-requests":
        return <DirectorDashboard />;

      case "director-approved":
        return <DirectorDashboard />;

      case "student-progress":
        return <StudentDashboard />;

      case "teacher-validations":
        return <TeacherValidationsView />;

      case "teacher-visits":
        return <TeacherVisitsView />;

      default:
        return <CoordinatorDashboard />;
    }
  };

  return (
    <div className="d-flex flex-column min-vh-100 bg-light">
      {isNavidad && <SnowOverlay />}
      <Navbar
        onLogout={() => {
          setCurrentView("dashboard");
          setIsAuthenticated(false);
        }}
        isNavidad={isNavidad}
        onToggleNavidad={toggleNavidad}
      />

      {/* Notificación flotante Toast */}
      {toastMessage && (
        <div
          className="position-fixed top-0 end-0 p-3"
          style={{ zIndex: 1090 }}
        >
          <div className="alert alert-dark shadow-lg border-0 text-white py-2 px-3 mb-0 rounded-3">
            {toastMessage}
          </div>
        </div>
      )}

      <div className="d-flex flex-grow-1">
        <Sidebar />
        <main className="flex-grow-1 p-4" style={{ maxWidth: 1400, margin: "0 auto", width: "100%" }}>
          {renderView()}
        </main>
      </div>
    </div>
  );
};

export const App: React.FC = () => {
  return (
    <SigpraProvider>
      <MainContent />
    </SigpraProvider>
  );
};

export default App;
