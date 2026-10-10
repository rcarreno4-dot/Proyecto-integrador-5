import { sigpraStore } from "../models/SigpraStore";

export class ReportService {
  static consolidated() {
    const data = sigpraStore.all();

    return data.assignments.map((assignment) => {
      const student = data.users.find((item) => item.id === assignment.studentId);
      const teacher = data.users.find((item) => item.id === assignment.teacherId);
      const place = data.places.find((item) => item.id === assignment.placeId);
      const agreement = data.agreements.find((item) => item.id === place?.agreementId);
      const institution = data.institutions.find((item) => item.id === agreement?.institutionId);
      const activities = data.activities.filter((item) => item.assignmentId === assignment.id);
      const visits = data.visits.filter((item) => item.assignmentId === assignment.id);
      const approvedHours = activities
        .filter((item) => item.status === "APROBADA")
        .reduce((sum, item) => sum + Number(item.hours), 0);

      return {
        assignmentId: assignment.id,
        student: student?.name,
        teacher: teacher?.name,
        institution: institution?.name,
        approvedHours: approvedHours || assignment.approvedHours,
        activities: activities.length,
        visits: visits.length,
        status: assignment.status
      };
    });
  }
}
