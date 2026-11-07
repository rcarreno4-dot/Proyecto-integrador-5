import React, { createContext, useContext, useState, useEffect, useCallback, useRef } from "react";
import {
  User,
  Period,
  Institution,
  Agreement,
  Place,
  Assignment,
  Activity,
  StudentRequest,
  AuditEvent,
  Visit,
  AcademicProgram
} from "../types";
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
import { api, BackendState } from "../services/api";

const initialPrograms: AcademicProgram[] = [
  { id: 1, name: "Ingeniería de Sistemas", levels: ["IX semestre", "Práctica profesional II"], active: true },
  { id: 2, name: "Licenciatura en Educación Infantil", levels: ["VIII semestre"], active: true },
  { id: 3, name: "Licenciatura en Pedagogía", levels: ["Práctica I", "Práctica II"], active: true }
];

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
  programs: AcademicProgram[];
  currentView: string;
  setCurrentView: (view: string) => void;
  addPeriod: (period: Omit<Period, "id">) => boolean;
  closePeriod: (periodId: number) => void;
  addInstitution: (institution: Omit<Institution, "id">) => void;
  addAgreement: (agreement: Omit<Agreement, "id">) => void;
  toggleAgreementStatus: (agreementId: number) => void;
  addPlace: (place: Omit<Place, "id" | "occupied" | "status">) => void;
  addProgram: (name: string, levels: string[]) => void;
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
  dbConnected: boolean;
  isSyncing: boolean;
  refreshFromBackend: () => Promise<void>;
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

  const [programs, setPrograms] = useState<AcademicProgram[]>(() => {
    const saved = localStorage.getItem("sigpra_react_programs");
    return saved ? JSON.parse(saved) : initialPrograms;
  });

  const [currentView, setCurrentView] = useState<string>("dashboard");
  const [toastMessage, setToastMessage] = useState<string | null>(null);
  const [dbConnected, setDbConnected] = useState<boolean>(false);
  const [isSyncing, setIsSyncing] = useState<boolean>(false);

  const stateRef = useRef({
    users,
    periods,
    institutions,
    agreements,
    places,
    assignments,
    activities,
    requests,
    visits,
    auditLogs,
    programs
  });

  useEffect(() => {
    stateRef.current = {
      users,
      periods,
      institutions,
      agreements,
      places,
      assignments,
      activities,
      requests,
      visits,
      auditLogs,
      programs
    };
  }, [users, periods, institutions, agreements, places, assignments, activities, requests, visits, auditLogs, programs]);

  const showToast = useCallback((msg: string) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 3500);
  }, []);

  // Persists current snapshot to backend
  const pushToBackend = useCallback(async (overrides?: Partial<BackendState>) => {
    setIsSyncing(true);
    const snapshot: BackendState = {
      ...stateRef.current,
      ...overrides
    };
    try {
      const ok = await api.saveState(snapshot);
      if (ok) {
        setDbConnected(true);
      }
    } catch (err) {
      console.warn("Error guardando en backend:", err);
    } finally {
      setIsSyncing(false);
    }
  }, []);

  // Fetch initial or refreshed data from backend
  const refreshFromBackend = useCallback(async () => {
    setIsSyncing(true);
    try {
      const remote = await api.getState();
      if (remote) {
        setDbConnected(true);
        if (remote.users && remote.users.length > 0) {
          setUsers(remote.users);
          localStorage.setItem("sigpra_react_users", JSON.stringify(remote.users));
        }
        if (remote.periods) {
          setPeriods(remote.periods);
          localStorage.setItem("sigpra_react_periods", JSON.stringify(remote.periods));
        }
        if (remote.institutions) {
          setInstitutions(remote.institutions);
          localStorage.setItem("sigpra_react_institutions", JSON.stringify(remote.institutions));
        }
        if (remote.agreements) {
          setAgreements(remote.agreements);
          localStorage.setItem("sigpra_react_agreements", JSON.stringify(remote.agreements));
        }
        if (remote.places) {
          setPlaces(remote.places);
          localStorage.setItem("sigpra_react_places", JSON.stringify(remote.places));
        }
        if (remote.assignments) {
          setAssignments(remote.assignments);
          localStorage.setItem("sigpra_react_assignments", JSON.stringify(remote.assignments));
        }
        if (remote.activities) {
          setActivities(remote.activities);
          localStorage.setItem("sigpra_react_activities", JSON.stringify(remote.activities));
        }
        if (remote.requests) {
          setRequests(remote.requests);
          localStorage.setItem("sigpra_react_requests", JSON.stringify(remote.requests));
        }
        if (remote.visits) {
          setVisits(remote.visits);
          localStorage.setItem("sigpra_react_visits", JSON.stringify(remote.visits));
        }
        if (remote.auditLogs) {
          setAuditLogs(remote.auditLogs);
          localStorage.setItem("sigpra_react_audit", JSON.stringify(remote.auditLogs));
        }
        if (remote.programs && remote.programs.length > 0) {
          setPrograms(remote.programs);
          localStorage.setItem("sigpra_react_programs", JSON.stringify(remote.programs));
        }
        showToast("🟢 Base de datos SIGPRA sincronizada");
      } else {
        setDbConnected(false);
      }
    } catch (err) {
      console.warn("No se pudo conectar a la API:", err);
      setDbConnected(false);
    } finally {
      setIsSyncing(false);
    }
  }, [showToast]);

  // Initial load effect
  useEffect(() => {
    refreshFromBackend();
  }, [refreshFromBackend]);

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
    pushToBackend({ auditLogs: updated });
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

    pushToBackend({ users: updated, auditLogs: updatedAudit });
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

    const newLog: AuditEvent = {
      id: Date.now() + 1,
      date: new Date().toLocaleString("es-CO"),
      user: currentUser.name,
      event: `Creó y publicó periodo ${newPeriod.program} (${newPeriod.year}-${newPeriod.semester})`,
      entity: "PERIODO"
    };
    const updatedAudit = [newLog, ...auditLogs];
    setAuditLogs(updatedAudit);
    localStorage.setItem("sigpra_react_audit", JSON.stringify(updatedAudit));

    pushToBackend({ periods: updated, auditLogs: updatedAudit });
    showToast("✅ Periodo académico publicado y guardado en la base de datos.");
    return true;
  };

  const closePeriod = (periodId: number) => {
    const target = periods.find(p => p.id === periodId);
    if (!target) return;
    const updated = periods.map(p => p.id === periodId ? { ...p, status: "CERRADO" as const } : p);
    setPeriods(updated);
    localStorage.setItem("sigpra_react_periods", JSON.stringify(updated));

    const newLog: AuditEvent = {
      id: Date.now(),
      date: new Date().toLocaleString("es-CO"),
      user: currentUser.name,
      event: `Cerró formalmente periodo ${target.program} (${target.year}-${target.semester})`,
      entity: "PERIODO"
    };
    const updatedAudit = [newLog, ...auditLogs];
    setAuditLogs(updatedAudit);
    localStorage.setItem("sigpra_react_audit", JSON.stringify(updatedAudit));

    pushToBackend({ periods: updated, auditLogs: updatedAudit });
    showToast("✅ Periodo académico cerrado exitosamente.");
  };

  const addInstitution = (institutionData: Omit<Institution, "id">) => {
    const updated = [...institutions, { ...institutionData, id: Date.now() }];
    setInstitutions(updated);
    localStorage.setItem("sigpra_react_institutions", JSON.stringify(updated));

    const newLog: AuditEvent = {
      id: Date.now() + 1,
      date: new Date().toLocaleString("es-CO"),
      user: currentUser.name,
      event: `Registró institución ${institutionData.name}`,
      entity: "INSTITUCION"
    };
    const updatedAudit = [newLog, ...auditLogs];
    setAuditLogs(updatedAudit);
    localStorage.setItem("sigpra_react_audit", JSON.stringify(updatedAudit));

    pushToBackend({ institutions: updated, auditLogs: updatedAudit });
    showToast("✅ Institución registrada y guardada en BD.");
  };

  const addAgreement = (agreementData: Omit<Agreement, "id">) => {
    const updated = [...agreements, { ...agreementData, id: Date.now() }];
    setAgreements(updated);
    localStorage.setItem("sigpra_react_agreements", JSON.stringify(updated));

    const newLog: AuditEvent = {
      id: Date.now() + 1,
      date: new Date().toLocaleString("es-CO"),
      user: currentUser.name,
      event: `Registró convenio ${agreementData.number}`,
      entity: "CONVENIO"
    };
    const updatedAudit = [newLog, ...auditLogs];
    setAuditLogs(updatedAudit);
    localStorage.setItem("sigpra_react_audit", JSON.stringify(updatedAudit));

    pushToBackend({ agreements: updated, auditLogs: updatedAudit });
    showToast("✅ Convenio registrado y guardado en BD.");
  };

  const toggleAgreementStatus = (agreementId: number) => {
    const target = agreements.find(a => a.id === agreementId);
    if (!target) return;
    const nextStatus = target.status === "SUSPENDIDO" ? "VIGENTE" : "SUSPENDIDO";
    const updated = agreements.map(a => a.id === agreementId ? { ...a, status: nextStatus as Agreement["status"] } : a);
    setAgreements(updated);
    localStorage.setItem("sigpra_react_agreements", JSON.stringify(updated));

    const newLog: AuditEvent = {
      id: Date.now(),
      date: new Date().toLocaleString("es-CO"),
      user: currentUser.name,
      event: `Cambió estado de convenio ${target.number} a ${nextStatus}`,
      entity: "CONVENIO"
    };
    const updatedAudit = [newLog, ...auditLogs];
    setAuditLogs(updatedAudit);
    localStorage.setItem("sigpra_react_audit", JSON.stringify(updatedAudit));

    pushToBackend({ agreements: updated, auditLogs: updatedAudit });
    showToast(`✅ Convenio ${target.number} marcado como ${nextStatus}.`);
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

    const newLog: AuditEvent = {
      id: Date.now() + 1,
      date: new Date().toLocaleString("es-CO"),
      user: currentUser.name,
      event: `Registró plaza de práctica en convenio #${placeData.agreementId}`,
      entity: "PLAZA"
    };
    const updatedAudit = [newLog, ...auditLogs];
    setAuditLogs(updatedAudit);
    localStorage.setItem("sigpra_react_audit", JSON.stringify(updatedAudit));

    pushToBackend({ places: updated, auditLogs: updatedAudit });
    showToast("✅ Plaza registrada correctamente en BD.");
  };

  const addProgram = (name: string, levels: string[]) => {
    const newProg: AcademicProgram = {
      id: Date.now(),
      name: name.trim(),
      levels,
      active: true
    };
    const updated = [...programs, newProg];
    setPrograms(updated);
    localStorage.setItem("sigpra_react_programs", JSON.stringify(updated));

    const newLog: AuditEvent = {
      id: Date.now() + 1,
      date: new Date().toLocaleString("es-CO"),
      user: currentUser.name,
      event: `Registró programa académico ${newProg.name}`,
      entity: "PROGRAMA_ACADEMICO"
    };
    const updatedAudit = [newLog, ...auditLogs];
    setAuditLogs(updatedAudit);
    localStorage.setItem("sigpra_react_audit", JSON.stringify(updatedAudit));

    pushToBackend({ programs: updated, auditLogs: updatedAudit });
    showToast("✅ Programa académico guardado en BD.");
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

    const newLog: AuditEvent = {
      id: Date.now() + 1,
      date: new Date().toLocaleString("es-CO"),
      user: currentUser.name,
      event: `${previousRequest ? "Actualizó" : "Radicó"} solicitud formal de práctica #${newRequest.id}`,
      entity: "SOLICITUD"
    };
    const updatedAudit = [newLog, ...auditLogs];
    setAuditLogs(updatedAudit);
    localStorage.setItem("sigpra_react_audit", JSON.stringify(updatedAudit));

    pushToBackend({ requests: updated, auditLogs: updatedAudit });
    showToast("✅ Solicitud formal radicada y enviada a la base de datos.");
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

    const newLog: AuditEvent = {
      id: Date.now() + 1,
      date: new Date().toLocaleString("es-CO"),
      user: currentUser.name,
      event: `Dictaminó solicitud #${requestId} como ${status}`,
      entity: "SOLICITUD"
    };
    const updatedAudit = [newLog, ...auditLogs];
    setAuditLogs(updatedAudit);
    localStorage.setItem("sigpra_react_audit", JSON.stringify(updatedAudit));

    pushToBackend({ requests: updated, auditLogs: updatedAudit });
    showToast(`✅ Solicitud #${requestId} dictaminada como ${status} en BD.`);
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

    const newLog: AuditEvent = {
      id: Date.now() + 1,
      date: new Date().toLocaleString("es-CO"),
      user: currentUser.name,
      event: `Asignó plaza de práctica para estudiante ID ${studentId}`,
      entity: "ASIGNACION"
    };
    const updatedAudit = [newLog, ...auditLogs];
    setAuditLogs(updatedAudit);
    localStorage.setItem("sigpra_react_audit", JSON.stringify(updatedAudit));

    pushToBackend({
      assignments: updated,
      places: updatedPlaces,
      requests: updatedRequests,
      auditLogs: updatedAudit
    });
    showToast("✅ Asignación guardada en BD exitosamente.");
  };

  const addActivity = (actData: Omit<Activity, "id">) => {
    const newAct: Activity = {
      ...actData,
      id: Date.now()
    };
    const updated = [newAct, ...activities];
    setActivities(updated);
    localStorage.setItem("sigpra_react_activities", JSON.stringify(updated));

    const newLog: AuditEvent = {
      id: Date.now() + 1,
      date: new Date().toLocaleString("es-CO"),
      user: currentUser.name,
      event: `Registró reporte de actividad (${newAct.hours} hrs)`,
      entity: "BITACORA"
    };
    const updatedAudit = [newLog, ...auditLogs];
    setAuditLogs(updatedAudit);
    localStorage.setItem("sigpra_react_audit", JSON.stringify(updatedAudit));

    pushToBackend({ activities: updated, auditLogs: updatedAudit });
    showToast("✅ Actividad y horas registradas en BD.");
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

    const newLog: AuditEvent = {
      id: Date.now() + 1,
      date: new Date().toLocaleString("es-CO"),
      user: currentUser.name,
      event: `Corrigió y reenvió actividad #${activityId}`,
      entity: "BITACORA"
    };
    const updatedAudit = [newLog, ...auditLogs];
    setAuditLogs(updatedAudit);
    localStorage.setItem("sigpra_react_audit", JSON.stringify(updatedAudit));

    pushToBackend({ activities: updated, auditLogs: updatedAudit });
    showToast("✅ Actividad corregida y sincronizada con BD.");
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

    const newLog: AuditEvent = {
      id: Date.now() + 1,
      date: new Date().toLocaleString("es-CO"),
      user: currentUser.name,
      event: `Validó actividad #${activityId} como ${status}`,
      entity: "BITACORA"
    };
    const updatedAudit = [newLog, ...auditLogs];
    setAuditLogs(updatedAudit);
    localStorage.setItem("sigpra_react_audit", JSON.stringify(updatedAudit));

    pushToBackend({ activities: updated, auditLogs: updatedAudit });
    showToast(`✅ Actividad marcada como ${status} en BD.`);
  };

  const addVisit = (visitData: Omit<Visit, "id">) => {
    const updated = [{ ...visitData, id: Date.now() }, ...visits];
    setVisits(updated);
    localStorage.setItem("sigpra_react_visits", JSON.stringify(updated));

    const newLog: AuditEvent = {
      id: Date.now() + 1,
      date: new Date().toLocaleString("es-CO"),
      user: currentUser.name,
      event: `Registró visita de acompañamiento para asignación #${visitData.assignmentId}`,
      entity: "VISITA"
    };
    const updatedAudit = [newLog, ...auditLogs];
    setAuditLogs(updatedAudit);
    localStorage.setItem("sigpra_react_audit", JSON.stringify(updatedAudit));

    pushToBackend({ visits: updated, auditLogs: updatedAudit });
    showToast("✅ Visita y evaluación registradas en BD.");
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
        programs,
        currentView,
        setCurrentView,
        addPeriod,
        closePeriod,
        addInstitution,
        addAgreement,
        toggleAgreementStatus,
        addPlace,
        addProgram,
        createRequest,
        updateRequestStatus,
        createAssignment,
        addActivity,
        resubmitActivity,
        updateActivityStatus,
        addVisit,
        auditAction,
        toastMessage,
        showToast,
        dbConnected,
        isSyncing,
        refreshFromBackend
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
