import { sigpraStore } from "../models/SigpraStore";
import { Period } from "../models/types";

export class PeriodService {
  static list() {
    return sigpraStore.periods();
  }

  static create(input: Omit<Period, "id">) {
    return sigpraStore.createPeriod(input);
  }
}
