import { sigpraStore } from "../models/SigpraStore";
import { Agreement } from "../models/types";

export class AgreementService {
  static list() {
    return sigpraStore.agreements();
  }

  static create(input: Omit<Agreement, "id">) {
    return sigpraStore.createAgreement(input);
  }
}
