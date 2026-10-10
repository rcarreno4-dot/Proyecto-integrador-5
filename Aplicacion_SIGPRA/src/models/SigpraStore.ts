import fs from "fs";
import path from "path";
import { seedData } from "./seedData";
import { Agreement, Assignment, Period, SigpraData } from "./types";

const storePath = path.join(process.cwd(), "data", "sigpra-store.json");

function clone<T>(value: T): T {
  return JSON.parse(JSON.stringify(value)) as T;
}

function nextId(items: Array<{ id: number }>) {
  return Math.max(0, ...items.map((item) => Number(item.id))) + 1;
}

export class SigpraStore {
  private data: SigpraData;

  constructor() {
    this.data = this.load();
  }

  all() {
    return clone(this.data);
  }

  replace(data: SigpraData) {
    this.data = clone(data);
    this.persist();
    return this.all();
  }

  periods() {
    return clone(this.data.periods);
  }

  createPeriod(input: Omit<Period, "id">) {
    const period = { ...input, id: nextId(this.data.periods) };
    this.data.periods.push(period);
    this.persist();
    return clone(period);
  }

  agreements() {
    return {
      institutions: clone(this.data.institutions),
      agreements: clone(this.data.agreements),
      places: clone(this.data.places)
    };
  }

  createAgreement(input: Omit<Agreement, "id">) {
    const agreement = { ...input, id: nextId(this.data.agreements) };
    this.data.agreements.push(agreement);
    this.persist();
    return clone(agreement);
  }

  assignments() {
    return clone(this.data.assignments);
  }

  createAssignment(input: Omit<Assignment, "id">) {
    const assignment = { ...input, id: nextId(this.data.assignments) };
    this.data.assignments.push(assignment);
    const place = this.data.places.find((item) => item.id === assignment.placeId);
    if (place) place.occupied += 1;
    this.persist();
    return clone(assignment);
  }

  private load() {
    try {
      if (fs.existsSync(storePath)) {
        return JSON.parse(fs.readFileSync(storePath, "utf8")) as SigpraData;
      }
    } catch {
      // Si el archivo local se corrompe, se restaura con datos demo.
    }
    return clone(seedData);
  }

  private persist() {
    fs.mkdirSync(path.dirname(storePath), { recursive: true });
    fs.writeFileSync(storePath, JSON.stringify(this.data, null, 2));
  }
}

export const sigpraStore = new SigpraStore();
