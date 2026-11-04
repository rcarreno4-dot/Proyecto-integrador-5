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

const MainContent: React.FC = () => {
  const { currentUser, currentView, toastMessage, setCurrentView } = useSigpra();
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  if (!isAuthenticated) {
    return <LoginView onLogin={() => {
      setCurrentView("dashboard");
      setIsAuthenticated(true);
    }} />;
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
      <Navbar onLogout={() => {
        setCurrentView("dashboard");
        setIsAuthenticated(false);
      }} />

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
