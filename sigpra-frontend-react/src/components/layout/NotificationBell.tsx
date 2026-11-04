import React, { useMemo, useState } from "react";
import { useSigpra } from "../../context/SigpraContext";

interface ActionNotification {
  id: string;
  title: string;
  description: string;
  count: number;
  view: string;
}

export const NotificationBell: React.FC = () => {
  const { currentUser, requests, assignments, activities, setCurrentView } = useSigpra();
  const [isOpen, setIsOpen] = useState(false);

  const notifications = useMemo<ActionNotification[]>(() => {
    if (currentUser.role === "DIRECTOR") {
      const count = requests.filter(request => request.status === "PENDIENTE_REVISION").length;
      return count ? [{
        id: "requests-to-review",
        title: "Solicitudes por revisar",
        description: "Hay solicitudes de práctica pendientes de dictamen.",
        count,
        view: "director-requests"
      }] : [];
    }

    if (currentUser.role === "COORDINADOR") {
      const count = requests.filter(request =>
        request.status === "APROBADA"
        && !assignments.some(assignment => assignment.studentId === request.studentId && assignment.status === "ACTIVA")
      ).length;
      return count ? [{
        id: "students-to-assign",
        title: "Estudiantes por asignar",
        description: "Hay estudiantes habilitados que aún no tienen plaza.",
        count,
        view: "assignments"
      }] : [];
    }

    if (currentUser.role === "DOCENTE_ASESOR") {
      const myAssignmentIds = assignments
        .filter(assignment => assignment.teacherId === currentUser.id && assignment.status === "ACTIVA")
        .map(assignment => assignment.id);
      const count = activities.filter(activity =>
        myAssignmentIds.includes(activity.assignmentId) && activity.status === "PENDIENTE"
      ).length;
      return count ? [{
        id: "activities-to-review",
        title: "Bitácoras por validar",
        description: "Tus estudiantes enviaron actividades que requieren revisión.",
        count,
        view: "teacher-validations"
      }] : [];
    }

    const notifications: ActionNotification[] = [];
    const returnedRequest = requests.find(request =>
      request.studentId === currentUser.id && request.status === "DEVUELTA"
    );
    if (returnedRequest) {
      notifications.push({
        id: `request-returned-${returnedRequest.id}`,
        title: "Solicitud devuelta",
        description: "Revisa las observaciones y corrige tu solicitud de práctica.",
        count: 1,
        view: "student-request"
      });
    }

    const returnedActivities = activities.filter(activity =>
      activity.status === "DEVUELTA"
      && assignments.some(assignment => assignment.id === activity.assignmentId
        && assignment.studentId === currentUser.id
        && assignment.status === "ACTIVA")
    ).length;
    if (returnedActivities) {
      notifications.push({
        id: "activities-returned",
        title: "Actividades por corregir",
        description: "Tu docente devolvió actividades que debes corregir y reenviar.",
        count: returnedActivities,
        view: "student-activities"
      });
    }
    return notifications;
  }, [currentUser, requests, assignments, activities]);

  const totalCount = notifications.reduce((total, notification) => total + notification.count, 0);

  const openNotification = (view: string) => {
    setCurrentView(view);
    setIsOpen(false);
  };

  return (
    <div className="position-relative">
      <button
        aria-expanded={isOpen}
        aria-label={totalCount ? `Notificaciones, ${totalCount} pendientes` : "Notificaciones"}
        className="btn btn-outline-light position-relative d-flex align-items-center justify-content-center"
        onClick={() => setIsOpen(open => !open)}
        type="button"
      >
        <svg aria-hidden="true" width="19" height="19" viewBox="0 0 24 24" fill="none">
          <path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" />
          <path d="M10 21h4" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" />
        </svg>
        {totalCount > 0 && (
          <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
            {totalCount > 99 ? "99+" : totalCount}
          </span>
        )}
      </button>

      {isOpen && (
        <section
          aria-label="Notificaciones pendientes"
          className="position-absolute end-0 mt-2 bg-white text-dark border rounded-3 shadow-lg"
          style={{ zIndex: 1100, width: "min(360px, calc(100vw - 24px))" }}
        >
          <div className="d-flex justify-content-between align-items-center px-3 py-2 border-bottom">
            <strong>Notificaciones</strong>
            {totalCount > 0 && <span className="badge bg-danger">{totalCount} pendientes</span>}
          </div>
          {notifications.length === 0 ? (
            <p className="small text-muted text-center px-3 py-4 mb-0">No tienes tareas importantes pendientes.</p>
          ) : (
            <ul className="list-unstyled mb-0">
              {notifications.map(notification => (
                <li key={notification.id}>
                  <button
                    className="btn btn-light text-start w-100 rounded-0 px-3 py-3 border-bottom"
                    onClick={() => openNotification(notification.view)}
                    type="button"
                  >
                    <span className="d-flex justify-content-between align-items-start gap-2">
                      <strong>{notification.title}</strong>
                      <span className="badge bg-primary rounded-pill">{notification.count}</span>
                    </span>
                    <span className="d-block small text-secondary mt-1">{notification.description}</span>
                    <span className="d-block small text-primary mt-2">Ir a revisar →</span>
                  </button>
                </li>
              ))}
            </ul>
          )}
        </section>
      )}
    </div>
  );
};
