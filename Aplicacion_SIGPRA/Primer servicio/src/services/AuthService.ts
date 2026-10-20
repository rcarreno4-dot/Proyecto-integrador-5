import { sigpraStore } from "../models/SigpraStore";

export class AuthService {
  static login(email: string, password: string) {
    const user = sigpraStore.all().users.find((item) => item.email === email && item.password === password);
    if (!user) return null;
    const { password: _hidden, ...safeUser } = user;
    return safeUser;
  }
}
