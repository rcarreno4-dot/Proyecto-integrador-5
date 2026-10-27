import { SigpraData } from "./types";

export const seedData: SigpraData = {
  users: [
    { id: 1, name: "Rafael Carreno", email: "coordinador@udi.edu.co", password: "123456", role: "COORDINADOR", initials: "RC" },
    { id: 2, name: "Laura Gomez", email: "estudiante@udi.edu.co", password: "123456", role: "ESTUDIANTE", initials: "LG", code: "UDI-2026-001", semester: 8, enabledForAssignment: true },
    { id: 3, name: "Martha Rodriguez", email: "docente@udi.edu.co", password: "123456", role: "DOCENTE_ASESOR", initials: "MR" },
    { id: 4, name: "Director Programa", email: "director@udi.edu.co", password: "123456", role: "DIRECTOR", initials: "DP" },
    { id: 5, name: "Miguel Rios", email: "miguel@udi.edu.co", password: "123456", role: "ESTUDIANTE", initials: "MR", code: "UDI-2026-002", semester: 8, enabledForAssignment: true },
    { id: 6, name: "Carlos Mendoza", email: "docente2@udi.edu.co", password: "123456", role: "DOCENTE_ASESOR", initials: "CM" },
    { id: 7, name: "Sofia Martinez", email: "sofia@udi.edu.co", password: "123456", role: "ESTUDIANTE", initials: "SM", code: "UDI-2026-003", semester: 8, enabledForAssignment: false },
    { id: 8, name: "Daniel Torres", email: "daniel@udi.edu.co", password: "123456", role: "ESTUDIANTE", initials: "DT", code: "UDI-2026-004", semester: 8, enabledForAssignment: false },
    { id: 9, name: "Valentina Ruiz", email: "valentina@udi.edu.co", password: "123456", role: "ESTUDIANTE", initials: "VR", code: "UDI-2026-005", semester: 8, enabledForAssignment: false },
    { id: 10, name: "ana francisca", email: "jobixiita@gmail.com", password: "123456", role: "ESTUDIANTE", initials: "AF", code: "udi 2026", semester: 8, enabledForAssignment: false }
  ],
  programs: [
    { id: 1, name: "Ingenieria de Sistemas", levels: ["Practica profesional"], active: true },
    { id: 2, name: "Licenciatura en Pedagogia", levels: ["Practica I", "Practica II"], active: true },
    { id: 3, name: "Licenciatura en Educacion Infantil", levels: ["VIII semestre"], active: true }
  ],
  periods: [
    {
      id: 1,
      program: "Licenciatura en Educacion Infantil",
      level: "VIII semestre",
      year: 2026,
      semester: 2,
      start: "2026-08-17",
      end: "2026-11-28",
      reportLimit: "2026-11-30",
      minHours: 320,
      status: "PUBLICADO",
      rubric: [
        { name: "Cumplimiento de horas", weight: 35 },
        { name: "Calidad del soporte", weight: 25 },
        { name: "Desempeno institucional", weight: 25 },
        { name: "Informe final", weight: 15 }
      ]
    }
  ],
  institutions: [
    { id: 1, name: "Centro Educativo La Esperanza", nit: "900123456-1", contact: "Sandra Perez", phone: "607 555 1234", email: "contacto@laesperanza.edu.co", active: true },
    { id: 2, name: "Fundacion Aprender", nit: "901222333-4", contact: "Paula Vargas", phone: "607 555 7788", email: "practicas@aprender.org", active: true }
  ],
  agreements: [
    { id: 1, institutionId: 1, number: "CV-2026-013", start: "2026-01-15", end: "2027-06-30", status: "VIGENTE", document: "convenio-la-esperanza.pdf" },
    { id: 2, institutionId: 2, number: "CV-2025-044", start: "2025-03-01", end: "2026-12-15", status: "VIGENTE", document: "convenio-aprender.pdf" }
  ],
  places: [
    { id: 1, agreementId: 1, level: "Practica pedagogica VIII", shift: "MANANA", offered: 12, occupied: 1, tutor: "Sandra Perez", status: "DISPONIBLE" },
    { id: 2, agreementId: 2, level: "Practica pedagogica VIII", shift: "TARDE", offered: 6, occupied: 1, tutor: "Paula Vargas", status: "DISPONIBLE" },
    { id: 3, agreementId: 1, level: "Practica pedagogica VIII", shift: "TARDE", offered: 4, occupied: 0, tutor: "Sandra Perez", status: "DISPONIBLE" },
    { id: 4, agreementId: 1, level: "Practica pedagogica VIII", shift: "MIXTA", offered: 3, occupied: 0, tutor: "Sandra Perez", status: "DISPONIBLE" },
    { id: 5, agreementId: 2, level: "Practica pedagogica VIII", shift: "MANANA", offered: 5, occupied: 0, tutor: "Paula Vargas", status: "DISPONIBLE" }
  ],
  assignments: [
    { id: 1, studentId: 2, teacherId: 3, placeId: 1, periodId: 1, date: "2026-08-20", status: "ACTIVA", approvedHours: 96 },
    { id: 2, studentId: 5, teacherId: 6, placeId: 2, periodId: 1, date: "2026-08-21", status: "ACTIVA", approvedHours: 42 }
  ],
  activities: [
    { id: 1, assignmentId: 1, date: "2026-09-13", type: "Acompanamiento pedagogico", description: "Apoyo en planeacion y ejecucion de actividad de lectura guiada con grupo de transicion.", hours: 4, status: "PENDIENTE", observation: "" },
    { id: 2, assignmentId: 1, date: "2026-09-06", type: "Planeacion de clase", description: "Preparacion de material didactico y guia de clase.", hours: 6, status: "APROBADA", observation: "Soporte completo." },
    { id: 3, assignmentId: 2, date: "2026-09-10", type: "Refuerzo pedagogico", description: "Acompanamiento a estudiantes con dificultad lectora.", hours: 3, status: "DEVUELTA", observation: "Adjuntar soporte firmado por la institucion." }
  ],
  evidences: [
    { id: 1, activityId: 1, fileName: "evidencia_foto.pdf", type: "PDF", url: "storage/sigpra/2026-2/asignacion-1/actividad-1/evidencia_foto.pdf", status: "CARGADA", mongoId: "ev-demo-001" },
    { id: 2, activityId: 2, fileName: "planeacion_clase.docx", type: "DOCX", url: "storage/sigpra/2026-2/asignacion-1/actividad-2/planeacion_clase.docx", status: "VALIDADA", mongoId: "ev-demo-002" }
  ],
  visits: [
    { id: 1, assignmentId: 1, teacherId: 3, date: "2026-09-20", mode: "PRESENCIAL", attendance: true, observation: "Buen manejo del grupo. Se recomienda fortalecer el cierre de la actividad.", score: 4.4, status: "REGISTRADA" }
  ],
  requests: [
    {
      id: 1,
      studentId: 2,
      periodId: 1,
      level: "VIII semestre",
      modality: "Convenio institucional",
      placeId: 1,
      status: "APROBADA",
      documents: {
        matricula: { name: "matricula_laura.pdf", type: "application/pdf", uploadedAt: "2026-08-18" },
        epsArl: { name: "arl_sura_laura.pdf", type: "application/pdf", uploadedAt: "2026-08-18" },
        pazYSalvo: { name: "paz_y_salvo_laura.pdf", type: "application/pdf", uploadedAt: "2026-08-18" }
      },
      comments: "Solicitud radicada para realizar practica en Centro Educativo La Esperanza.",
      submittedAt: "2026-08-18 09:30",
      reviewedAt: "2026-08-19 14:15",
      reviewedBy: 4,
      justification: "Cumple con totalidad de créditos y documentación verificada.",
      createdAt: "2026-08-18 09:10",
      updatedAt: "2026-08-19 14:15"
    },
    {
      id: 2,
      studentId: 5,
      periodId: 1,
      level: "VIII semestre",
      modality: "Convenio institucional",
      placeId: 2,
      status: "APROBADA",
      documents: {
        matricula: { name: "matricula_miguel.pdf", type: "application/pdf", uploadedAt: "2026-08-19" },
        epsArl: { name: "arl_positiva_miguel.pdf", type: "application/pdf", uploadedAt: "2026-08-19" },
        pazYSalvo: { name: "paz_y_salvo_miguel.pdf", type: "application/pdf", uploadedAt: "2026-08-19" }
      },
      comments: "Interés en turno de la tarde.",
      submittedAt: "2026-08-19 10:00",
      reviewedAt: "2026-08-20 11:00",
      reviewedBy: 4,
      justification: "Documentación en regla.",
      createdAt: "2026-08-19 09:45",
      updatedAt: "2026-08-20 11:00"
    },
    {
      id: 3,
      studentId: 7,
      periodId: 1,
      level: "VIII semestre",
      modality: "Convenio institucional",
      placeId: 3,
      status: "PENDIENTE_REVISION",
      documents: {
        matricula: { name: "matricula_sofia.pdf", type: "application/pdf", uploadedAt: "2026-08-22" },
        epsArl: { name: "eps_sanitas_sofia.pdf", type: "application/pdf", uploadedAt: "2026-08-22" },
        pazYSalvo: { name: "paz_y_salvo_sofia.pdf", type: "application/pdf", uploadedAt: "2026-08-22" }
      },
      comments: "Solicitud pendiente de revision por Director de Programa.",
      submittedAt: "2026-08-22 15:40",
      createdAt: "2026-08-22 15:00",
      updatedAt: "2026-08-22 15:40"
    },
    {
      id: 4,
      studentId: 8,
      periodId: 1,
      level: "VIII semestre",
      modality: "Practica autogestionada",
      suggestedCompany: "Colegio Bilingüe San José",
      status: "BORRADOR",
      documents: {
        matricula: { name: "matricula_daniel.pdf", type: "application/pdf", uploadedAt: "2026-08-23" }
      },
      comments: "Borrador en curso. Pendiente anexar paz y salvo académico.",
      createdAt: "2026-08-23 11:20",
      updatedAt: "2026-08-23 11:25"
    },
    {
      id: 5,
      studentId: 9,
      periodId: 1,
      level: "VIII semestre",
      modality: "Convenio institucional",
      placeId: 4,
      status: "DEVUELTA",
      documents: {
        matricula: { name: "matricula_valentina.pdf", type: "application/pdf", uploadedAt: "2026-08-21" },
        epsArl: { name: "eps_sura_valentina.pdf", type: "application/pdf", uploadedAt: "2026-08-21" },
        pazYSalvo: { name: "paz_salvo_antiguo.pdf", type: "application/pdf", uploadedAt: "2026-08-21" }
      },
      comments: "Por favor revisar el documento de paz y salvo.",
      submittedAt: "2026-08-21 16:10",
      reviewedAt: "2026-08-22 09:00",
      reviewedBy: 4,
      justification: "El paz y salvo académico adjunto corresponde al periodo 2026-1. Debe adjuntar el paz y salvo vigente para 2026-2.",
      createdAt: "2026-08-21 15:30",
      updatedAt: "2026-08-22 09:00"
    },
    {
      id: 6,
      studentId: 10,
      periodId: 1,
      level: "VIII semestre",
      modality: "Convenio institucional",
      placeId: 2,
      status: "PENDIENTE_REVISION",
      documents: {
        matricula: { name: "matricula_ana_francisca.pdf", type: "application/pdf", uploadedAt: "2026-10-27 03:52" },
        epsArl: { name: "eps_arl_ana_francisca.pdf", type: "application/pdf", uploadedAt: "2026-10-27 03:52" },
        pazYSalvo: { name: "paz_y_salvo_ana_francisca.pdf", type: "application/pdf", uploadedAt: "2026-10-27 03:52" }
      },
      comments: "hacer mi practica",
      submittedAt: "2026-10-27 03:52",
      createdAt: "2026-10-27 03:52",
      updatedAt: "2026-10-27 03:52"
    }
  ],
  audit: [
    { id: 1, date: "2026-08-18 09:30", user: "Laura Gomez", event: "Radicó solicitud formal de práctica (CU-08)", entity: "SOLICITUD" },
    { id: 2, date: "2026-08-19 14:15", user: "Director Programa", event: "Aprobó solicitud de práctica #1 y habilitó estudiante", entity: "SOLICITUD" },
    { id: 3, date: "2026-08-22 09:00", user: "Director Programa", event: "Devolvió solicitud #5 por documento desactualizado", entity: "SOLICITUD" },
    { id: 4, date: "2026-08-22 15:40", user: "Sofia Martinez", event: "Radicó solicitud formal de práctica (CU-08)", entity: "SOLICITUD" }
  ]
};
