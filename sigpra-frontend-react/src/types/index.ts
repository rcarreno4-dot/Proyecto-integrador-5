export type Role = "COORDINADOR" | "ESTUDIANTE" | "DOCENTE_ASESOR" | "DIRECTOR";

export interface User {
  id: number;
  name: string;
  email: string;
  role: Role;
  initials: string;
  code?: string;
  semester?: number;
}

export interface RubricCriterion {
  name: string;
  weight: number;
}

export interface AcademicProgram {
  id: number;
  name: string;
  levels: string[];
  active: boolean;
}

export interface Period {
  id: number;
  program: string;
  level: string;
  year: number;
  semester: number;
  start: string;
  end: string;
  reportLimit: string;
  minHours: number;
  status: "BORRADOR" | "PUBLICADO" | "CERRADO";
  rubric?: RubricCriterion[];
}

export interface Institution {
  id: number;
  name: string;
  nit: string;
  contact: string;
  phone: string;
  email: string;
  active: boolean;
}

export interface Agreement {
  id: number;
  institutionId: number;
  number: string;
  start: string;
  end: string;
  status: "VIGENTE" | "VENCIDO" | "SUSPENDIDO";
  document: string;
}

export interface Place {
  id: number;
  agreementId: number;
  program?: string;
  level: string;
  shift: "MANANA" | "TARDE" | "COMPLETA";
  offered: number;
  occupied: number;
  tutor: string;
  status: "DISPONIBLE" | "AGOTADA";
}

export interface Assignment {
  id: number;
  studentId: number;
  teacherId: number;
  placeId: number;
  periodId: number;
  date: string;
  status: "ACTIVA" | "FINALIZADA" | "CANCELADA";
  approvedHours: number;
}

export interface Activity {
  id: number;
  assignmentId: number;
  date: string;
  type: string;
  description: string;
  hours: number;
  status: "PENDIENTE" | "APROBADA" | "DEVUELTA" | "RECHAZADA";
  feedback?: string;
  evidenceNames?: string[];
}

export interface StudentRequest {
  id: number;
  studentId: number;
  periodId: number;
  level: string;
  modality: string;
  status: "BORRADOR" | "PENDIENTE_REVISION" | "APROBADA" | "DEVUELTA" | "RECHAZADA";
  comments?: string;
  justification?: string;
  submittedAt?: string;
  reviewedAt?: string;
  documentNames?: string[];
}

export interface Visit {
  id: number;
  assignmentId: number;
  teacherId: number;
  date: string;
  institutionContact: string;
  observations: string;
  assessment: number;
}

export interface AuditEvent {
  id: number;
  date: string;
  user: string;
  event: string;
  entity: string;
}
