import React from "react";
import { SigpraProvider, useSigpra } from "./context/SigpraContext";
import { Navbar } from "./components/layout/Navbar";
import { Sidebar } from "./components/layout/Sidebar";
import { CoordinatorDashboard } from "./components/views/CoordinatorDashboard";
import { DirectorDashboard } from "./components/views/DirectorDashboard";
import { StudentDashboard } from "./components/views/StudentDashboard";
import { TeacherDashboard } from "./components/views/TeacherDashboard";
import { ReportsView } from "./components/views/ReportsView";
import { PeriodsView } from "./components/views/PeriodsView";
import { AuditView } from "./components/views/AuditView";
import { StudentRequestView } from "./components/views/StudentRequestView";

const MainContent: React.FC = () => {
  const { currentUser, currentView, toastMessage } = useSigpra();

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

      case "audit":
        return <AuditView />;

      case "student-request":
        return <StudentRequestView />;

      case "director-requests":
        return <DirectorDashboard />;

      case "student-progress":
      case "student-activities":
        return <StudentDashboard />;

      case "teacher-validations":
      case "teacher-visits":
        return <TeacherDashboard />;

      default:
        return <CoordinatorDashboard />;
    }
  };

  return (
    <div className="d-flex flex-column min-vh-100 bg-light">
      <Navbar />

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
