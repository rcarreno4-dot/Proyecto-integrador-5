import {
  Period,
  Institution,
  Agreement,
  Place,
  Assignment,
  Activity,
  StudentRequest,
  AuditEvent,
  Visit,
  User
} from "../types";

const getApiBaseUrl = (): string => {
  if (typeof window !== "undefined" && (window as any).SIGPRA_API_URL) {
    return (window as any).SIGPRA_API_URL;
  }
  const envUrl = (import.meta as any).env?.VITE_API_URL;
  if (envUrl) return envUrl;
  // In development with Vite proxy or directly on port 3000
  return "/api";
};

export interface BackendState {
  users: User[];
  periods: Period[];
  institutions: Institution[];
  agreements: Agreement[];
  places: Place[];
  assignments: Assignment[];
  activities: Activity[];
  requests: StudentRequest[];
  visits: Visit[];
  auditLogs: AuditEvent[];
  programs?: Array<{ id: number; name: string; levels: string[]; active: boolean }>;
}

export const api = {
  baseUrl: getApiBaseUrl(),

  async checkHealth(): Promise<boolean> {
    try {
      const res = await fetch(`${this.baseUrl}/health`, { method: "GET" });
      if (!res.ok) {
        // Fallback to absolute port 3000 if proxy failed
        const fallbackRes = await fetch("http://localhost:3000/api/health");
        return fallbackRes.ok;
      }
      return res.ok;
    } catch {
      try {
        const fallbackRes = await fetch("http://localhost:3000/api/health");
        return fallbackRes.ok;
      } catch {
        return false;
      }
    }
  },

  async getState(): Promise<BackendState | null> {
    try {
      let res = await fetch(`${this.baseUrl}/state`);
      if (!res.ok) {
        res = await fetch("http://localhost:3000/api/state");
      }
      if (!res.ok) return null;
      const json = await res.json();
      const raw = json.data || {};

      // Map requests
      const mappedRequests: StudentRequest[] = (raw.requests || []).map((r: any) => {
        const docNames: string[] = [];
        if (r.documents) {
          if (r.documents.matricula?.name) docNames.push(r.documents.matricula.name);
          if (r.documents.epsArl?.name) docNames.push(r.documents.epsArl.name);
          if (r.documents.pazYSalvo?.name) docNames.push(r.documents.pazYSalvo.name);
        } else if (Array.isArray(r.documentNames)) {
          docNames.push(...r.documentNames);
        }
        return {
          id: Number(r.id),
          studentId: Number(r.studentId),
          periodId: Number(r.periodId),
          level: r.level || "Práctica",
          modality: r.modality || "Convenio institucional",
          status: r.status,
          comments: r.comments,
          justification: r.justification,
          submittedAt: r.submittedAt,
          reviewedAt: r.reviewedAt,
          documentNames: docNames.length > 0 ? docNames : ["matricula.pdf", "eps_arl.pdf", "paz_y_salvo.pdf"]
        };
      });

      // Map audit
      const mappedAudit: AuditEvent[] = (raw.audit || []).map((a: any) => ({
        id: Number(a.id),
        date: a.date,
        user: a.user,
        event: a.event,
        entity: a.entity
      }));

      // Map visits
      const mappedVisits: Visit[] = (raw.visits || []).map((v: any) => ({
        id: Number(v.id),
        assignmentId: Number(v.assignmentId),
        teacherId: Number(v.teacherId),
        date: v.date,
        institutionContact: v.institutionContact || v.mode || "Contacto Institucional",
        observations: v.observations || v.observation || "",
        assessment: Number(v.assessment || 4.5)
      }));

      // Map activities
      const mappedActivities: Activity[] = (raw.activities || []).map((act: any) => ({
        id: Number(act.id),
        assignmentId: Number(act.assignmentId),
        date: act.date,
        type: act.type || "Actividad académica",
        description: act.description,
        hours: Number(act.hours || 0),
        status: act.status || "PENDIENTE",
        feedback: act.feedback || act.observation || undefined,
        evidenceNames: act.evidenceNames || (act.evidence ? [act.evidence] : [])
      }));

      return {
        users: raw.users || [],
        periods: raw.periods || [],
        institutions: raw.institutions || [],
        agreements: raw.agreements || [],
        places: raw.places || [],
        assignments: raw.assignments || [],
        activities: mappedActivities,
        requests: mappedRequests,
        visits: mappedVisits,
        auditLogs: mappedAudit,
        programs: raw.programs || []
      };
    } catch (err) {
      console.warn("No se pudo conectar con la API de SIGPRA:", err);
      return null;
    }
  },

  async saveState(state: BackendState): Promise<boolean> {
    try {
      const payload = {
        users: state.users,
        periods: state.periods,
        institutions: state.institutions,
        agreements: state.agreements,
        places: state.places,
        assignments: state.assignments,
        activities: state.activities.map(a => ({
          ...a,
          observation: a.feedback || ""
        })),
        requests: state.requests.map(r => ({
          ...r,
          documents: {
            matricula: { name: (r.documentNames && r.documentNames[0]) || "matricula.pdf", type: "application/pdf" },
            epsArl: { name: (r.documentNames && r.documentNames[1]) || "eps_arl.pdf", type: "application/pdf" },
            pazYSalvo: { name: (r.documentNames && r.documentNames[2]) || "paz_y_salvo.pdf", type: "application/pdf" }
          }
        })),
        visits: state.visits.map(v => ({
          ...v,
          mode: v.institutionContact,
          attendance: true,
          observation: v.observations
        })),
        audit: state.auditLogs,
        programs: state.programs || []
      };

      let res = await fetch(`${this.baseUrl}/state`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });

      if (!res.ok) {
        res = await fetch("http://localhost:3000/api/state", {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload)
        });
      }

      return res.ok;
    } catch (err) {
      console.error("Error al persistir estado en la base de datos:", err);
      return false;
    }
  }
};
