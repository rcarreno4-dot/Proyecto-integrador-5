import React, { useState } from "react";
import { useSigpra } from "../../context/SigpraContext";

interface LoginViewProps {
  onLogin: () => void;
}

const DEMO_PASSWORD = "demo2026";

export const LoginView: React.FC<LoginViewProps> = ({ onLogin }) => {
  const { users, setCurrentUser, createStudent, showToast } = useSigpra();
  const [isRegistering, setIsRegistering] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [studentName, setStudentName] = useState("");
  const [studentEmail, setStudentEmail] = useState("");
  const [studentCode, setStudentCode] = useState("");
  const [semester, setSemester] = useState(8);

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const user = users.find(
      (candidate) => candidate.email.toLowerCase() === email.trim().toLowerCase()
    );

    if (!user || password !== DEMO_PASSWORD) {
      setError("Correo o contraseña incorrectos.");
      return;
    }

    setCurrentUser(user);
    showToast(`Bienvenido, ${user.name}.`);
    onLogin();
  };

  const handleRegister = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError("");

    const newStudent = createStudent({
      name: studentName,
      email: studentEmail,
      code: studentCode,
      semester
    });

    if (!newStudent) {
      setError("Ya existe una cuenta con ese correo o código estudiantil.");
      return;
    }

    showToast(`Cuenta creada. Bienvenido, ${newStudent.name}.`);
    onLogin();
    setStudentName("");
    setStudentEmail("");
    setStudentCode("");
  };

  return (
    <main className="min-vh-100 d-flex align-items-center justify-content-center bg-light p-3 p-md-4">
      <section className="card border-0 shadow-lg rounded-4 overflow-hidden w-100" style={{ maxWidth: 1040 }}>
        <div className="row g-0">
          <div className="col-md-6 d-none d-md-block">
            <img
              alt="Universidad de Investigación y Desarrollo — UDI"
              className="w-100 h-100"
              src="/udi-login-illustration.svg"
              style={{ display: "block", minHeight: 650, objectFit: "cover" }}
            />
          </div>
          <div className="col-12 col-md-6">
            <div className="d-md-none">
              <img
                alt="Universidad de Investigación y Desarrollo — UDI"
                className="w-100"
                src="/udi-login-illustration.svg"
                style={{ display: "block", height: 150, objectFit: "cover", objectPosition: "center 30%" }}
              />
            </div>
            <div className="card-body p-4 p-lg-5">
          <div className="text-center mb-4">
            <div className="d-inline-flex align-items-center justify-content-center rounded-3 mb-3 text-white fw-bold" style={{ width: 48, height: 48, background: "#0f766e", fontSize: "1.25rem" }} aria-hidden="true">S</div>
            <h1 className="h3 fw-bold mb-1">SIGPRA</h1>
            <p className="text-secondary mb-0">Acceso a prácticas académicas</p>
          </div>

          {isRegistering ? (
            <>
              <h2 className="h5 fw-semibold mb-3">Crear cuenta de estudiante</h2>
              <form onSubmit={handleRegister}>
                <div className="mb-3">
                  <label className="form-label" htmlFor="student-name">Nombre completo</label>
                  <input autoComplete="name" className="form-control" id="student-name" maxLength={100} onChange={event => setStudentName(event.target.value)} required value={studentName} />
                </div>
                <div className="mb-3">
                  <label className="form-label" htmlFor="student-email">Correo electrónico</label>
                  <input autoComplete="email" className="form-control" id="student-email" onChange={event => setStudentEmail(event.target.value)} required type="email" value={studentEmail} />
                </div>
                <div className="row g-2 mb-3">
                  <div className="col-7">
                    <label className="form-label" htmlFor="student-code">Código estudiantil</label>
                    <input className="form-control" id="student-code" maxLength={30} onChange={event => setStudentCode(event.target.value)} required value={studentCode} />
                  </div>
                  <div className="col-5">
                    <label className="form-label" htmlFor="student-semester">Semestre</label>
                    <select className="form-select" id="student-semester" onChange={event => setSemester(Number(event.target.value))} value={semester}>
                      {Array.from({ length: 10 }, (_, index) => index + 1).map(value => <option key={value} value={value}>{value}°</option>)}
                    </select>
                  </div>
                </div>
                {error && <div className="alert alert-danger py-2" role="alert">{error}</div>}
                <button className="btn btn-success w-100 py-2 fw-semibold" type="submit">Crear cuenta</button>
                <div className="alert alert-info small mt-3 mb-0" role="note">
                  Cuenta de demostración; al registrarte podrás ingresar con la contraseña <code>demo2026</code>.
                </div>
                <button className="btn btn-link w-100 mt-2" onClick={() => { setError(""); setIsRegistering(false); }} type="button">Volver a iniciar sesión</button>
              </form>
            </>
          ) : (
            <>
              <h2 className="h5 fw-semibold mb-3">Iniciar sesión</h2>
              <form onSubmit={handleSubmit}>
                <div className="mb-3">
                  <label className="form-label" htmlFor="login-email">Cuenta de prueba</label>
                  <select autoComplete="username" className="form-select" id="login-email" onChange={event => setEmail(event.target.value)} required value={email}>
                    <option value="" disabled>Selecciona una cuenta</option>
                    {users.map(user => (
                      <option key={user.id} value={user.email}>
                        {user.name} — {user.email} ({user.role})
                      </option>
                    ))}
                  </select>
                </div>
                <div className="mb-3">
                  <label className="form-label" htmlFor="login-password">Contraseña</label>
                  <input autoComplete="current-password" className="form-control" id="login-password" onChange={event => setPassword(event.target.value)} required type="password" value={password} />
                </div>
                {error && <div className="alert alert-danger py-2" role="alert">{error}</div>}
                <button className="btn btn-success w-100 py-2 fw-semibold" type="submit">Entrar</button>
              </form>
              <div className="alert alert-info small mt-4 mb-0" role="note">
                <strong>Modo de demostración:</strong> selecciona una cuenta de la lista. La contraseña
                para todas es <code>demo2026</code>.
              </div>
              <div className="text-center mt-3">
                <span className="small text-muted">¿Eres estudiante nuevo?</span>
                <button className="btn btn-link btn-sm fw-semibold" onClick={() => { setError(""); setIsRegistering(true); }} type="button">Crear cuenta de estudiante</button>
              </div>
            </>
          )}
            </div>
          </div>
        </div>
      </section>
    </main>
  );
};
