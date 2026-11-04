import { User, Period, Institution, Agreement, Place, Assignment, Activity, StudentRequest, AuditEvent } from "../types";

export const initialUsers: User[] = [
  { id: 1, name: "Coordinador Prácticas", email: "coordinador@udi.edu.co", role: "COORDINADOR", initials: "CP" },
  { id: 2, name: "Laura Gómez", email: "estudiante@udi.edu.co", role: "ESTUDIANTE", initials: "LG", code: "UDI-2026-001", semester: 8 },
  { id: 3, name: "Prof. Carlos Mendoza", email: "docente@udi.edu.co", role: "DOCENTE_ASESOR", initials: "CM" },
  { id: 4, name: "Director Programa", email: "director@udi.edu.co", role: "DIRECTOR", initials: "DP" },
  { id: 5, name: "Miguel Ríos", email: "miguel@udi.edu.co", role: "ESTUDIANTE", initials: "MR", code: "UDI-2026-002", semester: 8 },
  { id: 6, name: "Prof. Martha Pinzón", email: "martha@udi.edu.co", role: "DOCENTE_ASESOR", initials: "MP" },
  { id: 7, name: "Sofía Martínez", email: "sofia@udi.edu.co", role: "ESTUDIANTE", initials: "SM", code: "UDI-2026-003", semester: 8 },
  { id: 8, name: "Daniel Torres", email: "daniel@udi.edu.co", role: "ESTUDIANTE", initials: "DT", code: "UDI-2026-004", semester: 8 },
  { id: 9, name: "Valentina Ruiz", email: "valentina@udi.edu.co", role: "ESTUDIANTE", initials: "VR", code: "UDI-2026-005", semester: 8 },
  { id: 10, name: "Ana Francisca", email: "jobixiita@gmail.com", role: "ESTUDIANTE", initials: "AF", code: "UDI-2026-006", semester: 8 }
];

export const initialPeriods: Period[] = [
  {
    id: 1,
    program: "Ingeniería de Sistemas",
    level: "IX semestre",
    year: 2026,
    semester: 2,
    start: "2026-08-17",
    end: "2026-11-28",
    reportLimit: "2026-11-30",
    minHours: 160,
    status: "PUBLICADO",
    rubric: [
      { name: "Cumplimiento de horas", weight: 35 },
      { name: "Calidad de evidencias", weight: 25 },
      { name: "Desempeño técnico", weight: 25 },
      { name: "Informe final", weight: 15 }
    ]
  },
  {
    id: 2,
    program: "Licenciatura en Educación Infantil",
    level: "VIII semestre",
    year: 2026,
    semester: 2,
    start: "2026-08-17",
    end: "2026-11-28",
    reportLimit: "2026-11-30",
    minHours: 320,
    status: "PUBLICADO",
    rubric: [
      { name: "Cumplimiento de horas", weight: 40 },
      { name: "Acompañamiento pedagógico", weight: 30 },
      { name: "Evaluación del centro", weight: 30 }
    ]
  }
];

export const initialInstitutions: Institution[] = [
  { id: 1, name: "Centro Educativo La Esperanza", nit: "900123456-1", contact: "Sandra Pérez", phone: "607 555 1234", email: "contacto@laesperanza.edu.co", active: true },
  { id: 2, name: "Fundación Aprender", nit: "901222333-4", contact: "Paula Vargas", phone: "607 555 7788", email: "practicas@aprender.org", active: true },
  { id: 3, name: "Soluciones TI S.A.S.", nit: "900999888-2", contact: "Ing. Mauricio Celis", phone: "607 634 9000", email: "talento@solucionesti.co", active: true }
];

export const initialAgreements: Agreement[] = [
  { id: 1, institutionId: 1, number: "CV-2026-013", start: "2026-01-15", end: "2027-06-30", status: "VIGENTE", document: "convenio-la-esperanza.pdf" },
  { id: 2, institutionId: 2, number: "CV-2025-044", start: "2025-03-01", end: "2026-12-15", status: "VIGENTE", document: "convenio-aprender.pdf" },
  { id: 3, institutionId: 3, number: "CV-2026-089", start: "2026-02-01", end: "2028-02-01", status: "VIGENTE", document: "convenio-soluciones-ti.pdf" }
];

export const initialPlaces: Place[] = [
  { id: 1, agreementId: 3, program: "Ingeniería de Sistemas", level: "Práctica Profesional", shift: "MANANA", offered: 5, occupied: 2, tutor: "Ing. Mauricio Celis", status: "DISPONIBLE" },
  { id: 2, agreementId: 1, program: "Licenciatura en Educación Infantil", level: "Práctica Pedagógica VIII", shift: "MANANA", offered: 8, occupied: 1, tutor: "Sandra Pérez", status: "DISPONIBLE" },
  { id: 3, agreementId: 2, program: "Ingeniería de Sistemas", level: "Práctica Profesional", shift: "TARDE", offered: 4, occupied: 0, tutor: "Paula Vargas", status: "DISPONIBLE" }
];

export const initialAssignments: Assignment[] = [
  { id: 1, studentId: 2, teacherId: 3, placeId: 1, periodId: 1, date: "2026-08-20", status: "ACTIVA", approvedHours: 96 },
  { id: 2, studentId: 5, teacherId: 6, placeId: 1, periodId: 1, date: "2026-08-21", status: "ACTIVA", approvedHours: 48 }
];

export const initialActivities: Activity[] = [
  { id: 1, assignmentId: 1, date: "2026-09-13", type: "Desarrollo de Software", description: "Diseño de base de datos PostgreSQL e integración de vistas MVC.", hours: 24, status: "APROBADA", feedback: "Excelente estructura relacional." },
  { id: 2, assignmentId: 1, date: "2026-09-20", type: "Pruebas de Calidad", description: "Elaboración de pruebas unitarias y validación con usuarios.", hours: 32, status: "APROBADA", feedback: "Cumple con los criterios de aceptación." },
  { id: 3, assignmentId: 1, date: "2026-10-05", type: "Despliegue en la Nube", description: "Configuración de CI/CD y despliegue en entorno Render PaaS.", hours: 40, status: "APROBADA", feedback: "Despliegue operativo y con SSL." },
  { id: 4, assignmentId: 2, date: "2026-09-15", type: "Levantamiento de Requisitos", description: "Entrevistas a usuarios de la institución y matriz de trazabilidad.", hours: 48, status: "APROBADA", feedback: "Documentación clara." },
  { id: 5, assignmentId: 1, date: "2026-10-28", type: "Documentación técnica", description: "Actualización de la guía técnica y registro de pruebas de aceptación.", hours: 8, status: "PENDIENTE", evidenceNames: ["guia-tecnica.pdf"] }
];

export const initialRequests: StudentRequest[] = [
  { id: 1, studentId: 2, periodId: 1, level: "IX semestre", modality: "Convenio institucional", status: "APROBADA", submittedAt: "2026-08-18 09:30", reviewedAt: "2026-08-19 14:15", justification: "Cumple totalidad de prerrequisitos documentales y académicos." },
  { id: 2, studentId: 5, periodId: 1, level: "IX semestre", modality: "Convenio institucional", status: "APROBADA", submittedAt: "2026-08-19 10:00", reviewedAt: "2026-08-20 11:00", justification: "Documentación verificada y paz y salvo vigente." },
  { id: 3, studentId: 7, periodId: 1, level: "IX semestre", modality: "Convenio institucional", status: "PENDIENTE_REVISION", submittedAt: "2026-08-22 15:40", comments: "Solicito plaza en Soluciones TI para área de desarrollo web." },
  { id: 4, studentId: 10, periodId: 1, level: "IX semestre", modality: "Autogestionada", status: "PENDIENTE_REVISION", submittedAt: "2026-10-27 03:52", comments: "Radico soportes para validación de modalidad autogestionada." }
];

export const initialAudit: AuditEvent[] = [
  { id: 1, date: "2026-08-18 09:30", user: "Laura Gómez", event: "Radicó solicitud formal de práctica #1", entity: "SOLICITUD" },
  { id: 2, date: "2026-08-19 14:15", user: "Director Programa", event: "Aprobó solicitud de práctica #1 y habilitó practicante", entity: "SOLICITUD" },
  { id: 3, date: "2026-08-20 08:30", user: "Coordinador Prácticas", event: "Asignó estudiante a Soluciones TI S.A.S.", entity: "ASIGNACION" }
];
