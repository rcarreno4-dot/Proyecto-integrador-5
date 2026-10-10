import { sigpraStore } from "../models/SigpraStore";
import { SigpraData } from "../models/types";

export class StateService {
  static show() {
    return sigpraStore.all();
  }

  static replace(data: SigpraData) {
    return sigpraStore.replace(data);
  }
}
