import { sigpraStore } from "../models/SigpraStore";
import { User } from "../models/types";

export class AuthService {
  static login(email: string, password: string) {
    const user = sigpraStore.all().users.find((item) => item.email.toLowerCase() === email.toLowerCase() && item.password === password);
    if (!user) return null;
    const { password: _hidden, ...safeUser } = user;
    return safeUser;
  }

  static register(data: { name: string; email: string; password: string; code?: string; semester?: number }) {
    const storeData = sigpraStore.all();
    if (storeData.users.some((u) => u.email.toLowerCase() === data.email.toLowerCase())) {
      throw new Error("El correo institucional ya se encuentra registrado.");
    }

    const nameParts = data.name.trim().split(" ");
    const initials = nameParts.length >= 2
      ? (nameParts[0][0] + nameParts[1][0]).toUpperCase()
      : data.name.substring(0, 2).toUpperCase();

    const newUser: User = {
      id: Math.max(0, ...storeData.users.map((u) => u.id)) + 1,
      name: data.name.trim(),
      email: data.email.trim().toLowerCase(),
      password: data.password,
      role: "ESTUDIANTE",
      initials,
      code: data.code || `UDI-2026-${String(storeData.users.length + 1).padStart(3, "0")}`,
      semester: Number(data.semester) || 8,
      enabledForAssignment: false
    };

    storeData.users.push(newUser);
    storeData.audit.push({
      id: Math.max(0, ...storeData.audit.map((a) => a.id)) + 1,
      date: new Date().toISOString().replace("T", " ").substring(0, 16),
      user: newUser.name,
      event: `Registro de nuevo estudiante practicante (${newUser.email})`,
      entity: "USUARIO"
    });

    sigpraStore.replace(storeData);
    const { password: _hidden, ...safeUser } = newUser;
    return safeUser;
  }
}
