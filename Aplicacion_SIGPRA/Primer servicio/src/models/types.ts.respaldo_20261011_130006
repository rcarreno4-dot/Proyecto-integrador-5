export type Role = "COORDINADOR" | "ESTUDIANTE" | "DOCENTE_ASESOR" | "DIRECTOR";

export interface User {
  id: number;
  name: string;
  email: string;
  password: string;
  role: Role;
  initials: string;
  code?: string;
  semester?: number;
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
  status: string;
  rubric: Array<{ name: string; weight: number }>;
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
  status: string;
  document: string;
}

export interface Place {
  id: number;
  agreementId: number;
  level: string;
  shift: string;
  offered: number;
  occupied: number;
  tutor: string;
  status: string;
}

export interface Assignment {
  id: number;
  studentId: number;
  teacherId: number;
  placeId: number;
  periodId: number;
  date: string;
  status: string;
  approvedHours: number;
  reassignmentReason?: string;
}

export interface Activity {
  id: number;
  assignmentId: number;
  date: string;
  type: string;
  description: string;
  hours: number;
  status: string;
  observation: string;
}

export interface Evidence {
  id: number;
  activityId: number;
  fileName: string;
  type: string;
  url: string;
  status: string;
  mongoId: string;
}

export interface Visit {
  id: number;
  assignmentId: number;
  teacherId: number;
  date: string;
  mode: string;
  attendance: boolean;
  observation: string;
  score: number;
  status: string;
}

export interface AuditEvent {
  id: number;
  date: string;
  user: string;
  event: string;
  entity: string;
}

export interface SigpraData {
  users: User[];
  periods: Period[];
  institutions: Institution[];
  agreements: Agreement[];
  places: Place[];
  assignments: Assignment[];
  activities: Activity[];
  evidences: Evidence[];
  visits: Visit[];
  audit: AuditEvent[];
}
