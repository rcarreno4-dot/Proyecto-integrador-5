import { sigpraStore } from "../models/SigpraStore";
import { Assignment } from "../models/types";

export class AssignmentService {
  static list() {
    return sigpraStore.assignments();
  }

  static create(input: Omit<Assignment, "id">) {
    return sigpraStore.createAssignment(input);
  }
}
