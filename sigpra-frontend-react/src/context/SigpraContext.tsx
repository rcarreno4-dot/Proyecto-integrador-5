import React, { createContext, useContext, useState, useEffect } from "react";
import { User, Period, Institution, Agreement, Place, Assignment, Activity, StudentRequest, AuditEvent } from "../types";
import {
  initialUsers,
  initialPeriods,
  initialInstitutions,
  initialAgreements,
  initialPlaces,
  initialAssignments,
  initialActivities,
  initialRequests,
  initialAudit
} from "../data/seedData";

interface SigpraContextType {
  currentUser: User;
  setCurrentUser: (user: User) => void;
  users: User[];
  periods: Period[];
  institutions: Institution[];
  agreements: Agreement[];
  places: Place[];
  assignments: Assignment[];
  activities: Activity[];
  requests: StudentRequest[];
  auditLogs: AuditEvent[];
  currentView: string;
  setCurrentView: (view: string) => void;
  addPeriod: (period: Omit<Period, "id">) => boolean;
  updateRequestStatus: (requestId: number, status: StudentRequest["status"], justification?: string) => void;
  createAssignment: (studentId: number, placeId: number, teacherId: number, periodId: number) => void;
  addActivity: (activity: Omit<Activity, "id">) => void;
  auditAction: (event: string, entity: string) => void;
  toastMessage: string | null;
  showToast: (msg: string) => void;
}

const SigpraContext = createContext<SigpraContextType | undefined>(undefined);

export const SigpraProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [currentUser, setCurrentUser] = useState<User>(() => {
    const saved = localStorage.getItem("sigpra_react_user");
    return saved ? JSON.parse(saved) : initialUsers[0];
  });

  const [periods, setPeriods] = useState<Period[]>(() => {
    const saved = localStorage.getItem("sigpra_react_periods");
    return saved ? JSON.parse(saved) : initialPeriods;
  });

  const [institutions] = useState<Institution[]>(initialInstitutions);
  const [agreements] = useState<Agreement[]>(initialAgreements);
  const [places] = useState<Place[]>(initialPlaces);

  const [assignments, setAssignments] = useState<Assignment[]>(() => {
    const saved = localStorage.getItem("sigpra_react_assignments");
    return saved ? JSON.parse(saved) : initialAssignments;
  });

  const [activities, setActivities] = useState<Activity[]>(() => {
    const saved = localStorage.getItem("sigpra_react_activities");
    return saved ? JSON.parse(saved) : initialActivities;
  });

  const [requests, setRequests] = useState<StudentRequest[]>(() => {
    const saved = localStorage.getItem("sigpra_react_requests");
    return saved ? JSON.parse(saved) : initialRequests;
  });

  const [auditLogs, setAuditLogs] = useState<AuditEvent[]>(() => {
    const saved = localStorage.getItem("sigpra_react_audit");
    return saved ? JSON.parse(saved) : initialAudit;
  });

  const [currentView, setCurrentView] = useState<string>("dashboard");
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  const showToast = (msg: string) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 3500);
  };

  const auditAction = (event: string, entity: string) => {
    const newLog: AuditEvent = {
      id: Date.now(),
      date: new Date().toLocaleString("es-CO"),
      user: currentUser.name,
      event,
      entity
    };
    const updated = [newLog, ...auditLogs];
    setAuditLogs(updated);
    localStorage.setItem("sigpra_react_audit", JSON.stringify(updated));
  };

  const addPeriod = (periodData: Omit<Period, "id">): boolean => {
    const today = new Date().toISOString().split("T")[0];
    if (periodData.start < today) {
      showToast("❌ La fecha de inicio no puede ser anterior a la fecha actual.");
      return false;
    }
    if (periodData.start > periodData.end) {
      showToast("❌ La fecha de inicio debe ser anterior a la fecha de finalización.");
      return false;
    }
    if (periodData.minHours > 320) {
      showToast("❌ La intensidad horaria mínima no puede exceder las 320 horas reglamentarias.");
      return false;
    }
    if (periodData.minHours < 1) {
      showToast("❌ La intensidad horaria mínima debe ser de al menos 1 hora.");
      return false;
    }

    const newPeriod: Period = {
      ...periodData,
      id: Date.now()
    };
    const updated = [...periods, newPeriod];
    setPeriods(updated);
    localStorage.setItem("sigpra_react_periods", JSON.stringify(updated));
    auditAction(`Creó y publicó periodo ${newPeriod.program} (${newPeriod.year}-${newPeriod.semester})`, "PERIODO");
    showToast("✅ Periodo académico publicado exitosamente.");
    return true;
  };

  const updateRequestStatus = (requestId: number, status: StudentRequest["status"], justification?: string) => {
    const updated = requests.map(req => {
      if (req.id === requestId) {
        return {
          ...req,
          status,
          justification: justification || req.justification,
          reviewedAt: new Date().toLocaleString("es-CO")
        };
      }
      return req;
    });
    setRequests(updated);
    localStorage.setItem("sigpra_react_requests", JSON.stringify(updated));
    auditAction(`Dictaminó solicitud #${requestId} como ${status}`, "SOLICITUD");
    showToast(`✅ Solicitud #${requestId} dictaminada como ${status}.`);
  };

  const createAssignment = (studentId: number, placeId: number, teacherId: number, periodId: number) => {
    const newAssignment: Assignment = {
      id: Date.now(),
      studentId,
      placeId,
      teacherId,
      periodId,
      date: new Date().toISOString().split("T")[0],
      status: "ACTIVA",
      approvedHours: 0
    };
    const updated = [...assignments, newAssignment];
    setAssignments(updated);
    localStorage.setItem("sigpra_react_assignments", JSON.stringify(updated));
    auditAction(`Asignó plaza de práctica para estudiante ID ${studentId}`, "ASIGNACION");
    showToast("✅ Asignación de práctica realizada con éxito.");
  };

  const addActivity = (actData: Omit<Activity, "id">) => {
    const newAct: Activity = {
      ...actData,
      id: Date.now()
    };
    const updated = [newAct, ...activities];
    setActivities(updated);
    localStorage.setItem("sigpra_react_activities", JSON.stringify(updated));
    auditAction(`Registró reporte de actividad (${newAct.hours} hrs)`, "BITACORA");
    showToast("✅ Actividad y horas registradas en bitácora.");
  };

  useEffect(() => {
    localStorage.setItem("sigpra_react_user", JSON.stringify(currentUser));
  }, [currentUser]);

  return (
    <SigpraContext.Provider
      value={{
        currentUser,
        setCurrentUser,
        users: initialUsers,
        periods,
        institutions,
        agreements,
        places,
        assignments,
        activities,
        requests,
        auditLogs,
        currentView,
        setCurrentView,
        addPeriod,
        updateRequestStatus,
        createAssignment,
        addActivity,
        auditAction,
        toastMessage,
        showToast
      }}
    >
      {children}
    </SigpraContext.Provider>
  );
};

export const useSigpra = () => {
  const context = useContext(SigpraContext);
  if (!context) throw new Error("useSigpra debe usarse dentro de SigpraProvider");
  return context;
};
