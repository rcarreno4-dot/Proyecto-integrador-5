import { sigpraStore } from "../models/SigpraStore";
import { Activity, Visit } from "../models/types";

/**
 * Servicio S3: Ejecucion y seguimiento (RF-04, RF-05, CU-04, CU-05, CU-06)
 * Responsabilidad: Registrar actividades, validar soportes y horas,
 * registrar visitas de acompanamiento y evaluacion de practica.
 */
export class TrackingService {
  static listActivities(assignmentId?: number): Activity[] {
    const activities = sigpraStore.activities();
    if (assignmentId) {
      return activities.filter((a) => a.assignmentId === assignmentId);
    }
    return activities;
  }

  static createActivity(input: {
    assignmentId: number;
    date: string;
    type: string;
    description: string;
    hours: number;
    observation?: string;
  }): Activity {
    if (!input.assignmentId || !input.date || !input.type || !input.description) {
      throw new Error("Datos incompletos para registrar la actividad");
    }
    if (Number(input.hours) <= 0) {
      throw new Error("El numero de horas debe ser mayor a cero");
    }

    const assignments = sigpraStore.assignments();
    const assignment = assignments.find((a) => a.id === Number(input.assignmentId));
    if (!assignment) {
      throw new Error("La asignacion indicada no existe");
    }

    return sigpraStore.createActivity({
      assignmentId: Number(input.assignmentId),
      date: input.date,
      type: input.type,
      description: input.description,
      hours: Number(input.hours),
      status: "PENDIENTE",
      observation: input.observation || ""
    });
  }

  static updateActivityStatus(id: number, status: string, observation: string = ""): Activity {
    const validStatuses = ["BORRADOR", "PENDIENTE", "APROBADA", "DEVUELTA", "RECHAZADA"];
    if (!validStatuses.includes(status)) {
      throw new Error(`Estado invalido: ${status}`);
    }

    const updated = sigpraStore.updateActivity(id, { status, observation });
    if (!updated) {
      throw new Error("Actividad no encontrada");
    }
    return updated;
  }

  static listVisits(assignmentId?: number): Visit[] {
    const visits = sigpraStore.visits();
    if (assignmentId) {
      return visits.filter((v) => v.assignmentId === assignmentId);
    }
    return visits;
  }

  static createVisit(input: {
    assignmentId: number;
    teacherId: number;
    date: string;
    mode: string;
    attendance: boolean;
    observation: string;
    score: number;
    status?: string;
  }): Visit {
    if (!input.assignmentId || !input.teacherId || !input.date) {
      throw new Error("Datos incompletos para registrar la visita");
    }
    if (input.score < 0 || input.score > 5) {
      throw new Error("La calificacion debe estar entre 0.0 y 5.0");
    }

    return sigpraStore.createVisit({
      assignmentId: Number(input.assignmentId),
      teacherId: Number(input.teacherId),
      date: input.date,
      mode: input.mode || "PRESENCIAL",
      attendance: Boolean(input.attendance),
      observation: input.observation || "",
      score: Number(input.score),
      status: input.status || "REGISTRADA"
    });
  }
}
