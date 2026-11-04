import React, { createContext, useContext, useState, useEffect } from "react";
import { User, Period, Institution, Agreement, Place, Assignment, Activity, StudentRequest, AuditEvent, Visit } from "../types";
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
  createStudent: (student: Omit<User, "id" | "role" | "initials">) => User | null;
  periods: Period[];
  institutions: Institution[];
  agreements: Agreement[];
  places: Place[];
  assignments: Assignment[];
  activities: Activity[];
  requests: StudentRequest[];
  visits: Visit[];
  auditLogs: AuditEvent[];
  currentView: string;
  setCurrentView: (view: string) => void;
  addPeriod: (period: Omit<Period, "id">) => boolean;
  addInstitution: (institution: Omit<Institution, "id">) => void;
  addAgreement: (agreement: Omit<Agreement, "id">) => void;
  addPlace: (place: Omit<Place, "id" | "occupied" | "status">) => void;
  createRequest: (request: Omit<StudentRequest, "id" | "status" | "submittedAt">) => void;
  updateRequestStatus: (requestId: number, status: StudentRequest["status"], justification?: string) => void;
  createAssignment: (studentId: number, placeId: number, teacherId: number, periodId: number) => void;
  addActivity: (activity: Omit<Activity, "id">) => void;
  resubmitActivity: (activityId: number, activity: Omit<Activity, "id" | "status">) => void;
  updateActivityStatus: (activityId: number, status: Activity["status"], feedback?: string) => void;
  addVisit: (visit: Omit<Visit, "id">) => void;
  auditAction: (event: string, entity: string) => void;
  toastMessage: string | null;
  showToast: (msg: string) => void;
}

const SigpraContext = createContext<SigpraContextType | undefined>(undefined);

export const SigpraProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [users, setUsers] = useState<User[]>(() => {
    const saved = localStorage.getItem("sigpra_react_users");
    return saved ? JSON.parse(saved) : initialUsers;
  });

  const [currentUser, setCurrentUser] = useState<User>(() => {
    const saved = localStorage.getItem("sigpra_react_user");
    return saved ? JSON.parse(saved) : initialUsers[0];
  });

  const [periods, setPeriods] = useState<Period[]>(() => {
    const saved = localStorage.getItem("sigpra_react_periods");
    return saved ? JSON.parse(saved) : initialPeriods;
  });

  const [institutions, setInstitutions] = useState<Institution[]>(() => {
    const saved = localStorage.getItem("sigpra_react_institutions");
    return saved ? JSON.parse(saved) : initialInstitutions;
  });
  const [agreements, setAgreements] = useState<Agreement[]>(() => {
    const saved = localStorage.getItem("sigpra_react_agreements");
    return saved ? JSON.parse(saved) : initialAgreements;
  });
  const [places, setPlaces] = useState<Place[]>(() => {
    const saved = localStorage.getItem("sigpra_react_places");
    return saved ? JSON.parse(saved) : initialPlaces;
  });

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

  const [visits, setVisits] = useState<Visit[]>(() => {
    const saved = localStorage.getItem("sigpra_react_visits");
    return saved ? JSON.parse(saved) : [];
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

  const createStudent = (studentData: Omit<User, "id" | "role" | "initials">): User | null => {
    const email = studentData.email.trim().toLowerCase();
    const code = studentData.code?.trim().toLowerCase();
    if (users.some(user => user.email.toLowerCase() === email)) {
      return null;
    }
    if (code && users.some(user => user.code?.toLowerCase() === code)) {
      return null;
    }

    const name = studentData.name.trim();
    const newStudent: User = {
      ...studentData,
      id: Date.now(),
      name,
      email,
      code: studentData.code?.trim(),
      role: "ESTUDIANTE",
      initials: name.split(/\s+/).slice(0, 2).map(part => part[0]?.toUpperCase() || "").join("")
    };
    const updated = [...users, newStudent];
    setUsers(updated);
    localStorage.setItem("sigpra_react_users", JSON.stringify(updated));
    setCurrentUser(newStudent);
    const newLog: AuditEvent = {
      id: Date.now() + 1,
      date: new Date().toLocaleString("es-CO"),
      user: "Registro de estudiante",
      event: `Creó cuenta de estudiante ${newStudent.name}`,
      entity: "USUARIO"
    };
    const updatedAudit = [newLog, ...auditLogs];
    setAuditLogs(updatedAudit);
    localStorage.setItem("sigpra_react_audit", JSON.stringify(updatedAudit));
    return newStudent;
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

  const addInstitution = (institutionData: Omit<Institution, "id">) => {
    const updated = [...institutions, { ...institutionData, id: Date.now() }];
    setInstitutions(updated);
    localStorage.setItem("sigpra_react_institutions", JSON.stringify(updated));
    auditAction(`Registró institución ${institutionData.name}`, "INSTITUCION");
    showToast("✅ Institución registrada correctamente.");
  };

  const addAgreement = (agreementData: Omit<Agreement, "id">) => {
    const updated = [...agreements, { ...agreementData, id: Date.now() }];
    setAgreements(updated);
    localStorage.setItem("sigpra_react_agreements", JSON.stringify(updated));
    auditAction(`Registró convenio ${agreementData.number}`, "CONVENIO");
    showToast("✅ Convenio registrado correctamente.");
  };

  const addPlace = (placeData: Omit<Place, "id" | "occupied" | "status">) => {
    const newPlace: Place = {
      ...placeData,
      id: Date.now(),
      occupied: 0,
      status: placeData.offered > 0 ? "DISPONIBLE" : "AGOTADA"
    };
    const updated = [...places, newPlace];
    setPlaces(updated);
    localStorage.setItem("sigpra_react_places", JSON.stringify(updated));
    auditAction(`Registró plaza de práctica en convenio #${placeData.agreementId}`, "PLAZA");
    showToast("✅ Plaza registrada correctamente.");
  };

  const createRequest = (requestData: Omit<StudentRequest, "id" | "status" | "submittedAt">) => {
    const previousRequest = requests.find(request => request.studentId === requestData.studentId);
    const newRequest: StudentRequest = previousRequest
      ? {
          ...previousRequest,
          ...requestData,
          status: "PENDIENTE_REVISION",
          submittedAt: new Date().toLocaleString("es-CO"),
          reviewedAt: undefined,
          justification: undefined
        }
      : {
          ...requestData,
          id: Date.now(),
          status: "PENDIENTE_REVISION",
          submittedAt: new Date().toLocaleString("es-CO")
        };
    const updated = previousRequest
      ? requests.map(request => request.studentId === requestData.studentId ? newRequest : request)
      : [newRequest, ...requests];
    setRequests(updated);
    localStorage.setItem("sigpra_react_requests", JSON.stringify(updated));
    auditAction(`${previousRequest ? "Actualizó" : "Radicó"} solicitud de práctica #${newRequest.id}`, "SOLICITUD");
    showToast("✅ Solicitud radicada. Está pendiente de revisión por la Dirección.");
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
    const selectedPlace = places.find(place => place.id === placeId);
    if (!selectedPlace || selectedPlace.occupied >= selectedPlace.offered) {
      showToast("❌ La plaza seleccionada no tiene cupos disponibles.");
      return;
    }

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
    const updatedPlaces = places.map(place => place.id === placeId
      ? {
          ...place,
          occupied: place.occupied + 1,
          status: place.occupied + 1 >= place.offered ? "AGOTADA" as const : "DISPONIBLE" as const
        }
      : place
    );
    setPlaces(updatedPlaces);
    localStorage.setItem("sigpra_react_places", JSON.stringify(updatedPlaces));
    const updatedRequests = requests.map(request => request.studentId === studentId && request.status === "APROBADA"
      ? { ...request, status: "APROBADA" as const, justification: `${request.justification || ""} Asignación realizada.`.trim() }
      : request
    );
    setRequests(updatedRequests);
    localStorage.setItem("sigpra_react_requests", JSON.stringify(updatedRequests));
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

  const resubmitActivity = (activityId: number, actData: Omit<Activity, "id" | "status">) => {
    const target = activities.find(activity => activity.id === activityId && activity.status === "DEVUELTA");
    if (!target) {
      showToast("❌ Solo se pueden corregir actividades devueltas por el docente.");
      return;
    }
    const updated = activities.map(activity => activity.id === activityId
      ? { ...activity, ...actData, status: "PENDIENTE" as const, feedback: undefined }
      : activity
    );
    setActivities(updated);
    localStorage.setItem("sigpra_react_activities", JSON.stringify(updated));
    auditAction(`Corrigió y reenvió actividad #${activityId}`, "BITACORA");
    showToast("✅ Actividad corregida y reenviada para validación.");
  };

  const updateActivityStatus = (activityId: number, status: Activity["status"], feedback?: string) => {
    const target = activities.find(activity => activity.id === activityId);
    if (!target) {
      showToast("❌ No se encontró la actividad seleccionada.");
      return;
    }
    const updated = activities.map(activity => activity.id === activityId
      ? { ...activity, status, feedback: feedback?.trim() || activity.feedback }
      : activity
    );
    setActivities(updated);
    localStorage.setItem("sigpra_react_activities", JSON.stringify(updated));
    auditAction(`Validó actividad #${activityId} como ${status}`, "BITACORA");
    showToast(`✅ Actividad marcada como ${status}.`);
  };

  const addVisit = (visitData: Omit<Visit, "id">) => {
    const updated = [{ ...visitData, id: Date.now() }, ...visits];
    setVisits(updated);
    localStorage.setItem("sigpra_react_visits", JSON.stringify(updated));
    auditAction(`Registró visita de acompañamiento para asignación #${visitData.assignmentId}`, "VISITA");
    showToast("✅ Visita y evaluación registradas.");
  };

  useEffect(() => {
    localStorage.setItem("sigpra_react_user", JSON.stringify(currentUser));
  }, [currentUser]);

  return (
    <SigpraContext.Provider
      value={{
        currentUser,
        setCurrentUser,
        users,
        createStudent,
        periods,
        institutions,
        agreements,
        places,
        assignments,
        activities,
        requests,
        visits,
        auditLogs,
        currentView,
        setCurrentView,
        addPeriod,
        addInstitution,
        addAgreement,
        addPlace,
        createRequest,
        updateRequestStatus,
        createAssignment,
        addActivity,
        resubmitActivity,
        updateActivityStatus,
        addVisit,
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
