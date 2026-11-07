import React, { useState } from "react";
import { useSigpra } from "../../context/SigpraContext";

type ManagementTab = "institutions" | "agreements" | "places";

export const AgreementsView: React.FC = () => {
  const {
    institutions,
    agreements,
    places,
    addInstitution,
    addAgreement,
    toggleAgreementStatus,
    addPlace,
    showToast
  } = useSigpra();

  const [activeTab, setActiveTab] = useState<ManagementTab>("institutions");
  const [institutionName, setInstitutionName] = useState("");
  const [nit, setNit] = useState("");
  const [contact, setContact] = useState("");
  const [phone, setPhone] = useState("");
  const [email, setEmail] = useState("");
  const [agreementInstitution, setAgreementInstitution] = useState("");
  const [agreementNumber, setAgreementNumber] = useState("");
  const [agreementStart, setAgreementStart] = useState("");
  const [agreementEnd, setAgreementEnd] = useState("");
  const [agreementDocument, setAgreementDocument] = useState("");
  const [placeAgreement, setPlaceAgreement] = useState("");
  const [program, setProgram] = useState("Ingeniería de Sistemas");
  const [level, setLevel] = useState("Práctica Profesional");
  const [shift, setShift] = useState<"MANANA" | "TARDE" | "COMPLETA">("MANANA");
  const [offered, setOffered] = useState(1);
  const [tutor, setTutor] = useState("");

  const getDaysRemaining = (endDateStr: string) => {
    const end = new Date(endDateStr);
    const now = new Date();
    const diffTime = end.getTime() - now.getTime();
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  };

  const expiringSoonAgreements = agreements.filter(a => {
    if (a.status !== "VIGENTE") return false;
    const days = getDaysRemaining(a.end);
    return days >= 0 && days <= 60;
  });

  const handleInstitutionSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    addInstitution({
      name: institutionName.trim(),
      nit: nit.trim(),
      contact: contact.trim(),
      phone: phone.trim(),
      email: email.trim(),
      active: true
    });
    setInstitutionName("");
    setNit("");
    setContact("");
    setPhone("");
    setEmail("");
  };

  const handleAgreementSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (agreementStart > agreementEnd) {
      showToast("❌ La fecha de inicio del convenio debe ser anterior a su vencimiento.");
      return;
    }
    const status = agreementEnd >= new Date().toISOString().slice(0, 10) ? "VIGENTE" : "VENCIDO";
    addAgreement({
      institutionId: Number(agreementInstitution),
      number: agreementNumber.trim(),
      start: agreementStart,
      end: agreementEnd,
      status,
      document: agreementDocument.trim() || "Sin documento adjunto"
    });
    setAgreementNumber("");
    setAgreementStart("");
    setAgreementEnd("");
    setAgreementDocument("");
  };

  const handlePlaceSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const agreement = agreements.find(item => item.id === Number(placeAgreement));
    if (!agreement || agreement.status !== "VIGENTE") {
      showToast("❌ Selecciona un convenio vigente para registrar la plaza.");
      return;
    }
    addPlace({
      agreementId: agreement.id,
      program,
      level,
      shift,
      offered: Number(offered),
      tutor: tutor.trim()
    });
    setTutor("");
    setOffered(1);
  };

  const tabs: { id: ManagementTab; label: string }[] = [
    { id: "institutions", label: `Instituciones (${institutions.length})` },
    { id: "agreements", label: `Convenios (${agreements.length})` },
    { id: "places", label: `Plazas (${places.length})` }
  ];

  return (
    <div>
      <div className="mb-3">
        <h2 className="fw-bold mb-1 text-primary">Gestión de convenios y plazas</h2>
        <p className="text-muted small mb-0">Administra instituciones aliadas, vigencia de convenios y cupos de práctica.</p>
      </div>

      <ul className="nav nav-tabs mb-3">
        {tabs.map(tab => (
          <li className="nav-item" key={tab.id}>
            <button
              className={`nav-link ${activeTab === tab.id ? "active fw-bold" : ""}`}
              onClick={() => setActiveTab(tab.id)}
              type="button"
            >
              {tab.label}
            </button>
          </li>
        ))}
      </ul>

      {activeTab === "institutions" && (
        <>
          <form className="card card-body border-0 shadow-xs mb-3" onSubmit={handleInstitutionSubmit}>
            <h5 className="fw-bold">Registrar institución aliada</h5>
            <div className="row g-2">
              <div className="col-md-4"><label className="form-label">Nombre</label><input className="form-control" required value={institutionName} onChange={event => setInstitutionName(event.target.value)} /></div>
              <div className="col-md-2"><label className="form-label">NIT</label><input className="form-control" required value={nit} onChange={event => setNit(event.target.value)} /></div>
              <div className="col-md-2"><label className="form-label">Contacto</label><input className="form-control" required value={contact} onChange={event => setContact(event.target.value)} /></div>
              <div className="col-md-2"><label className="form-label">Teléfono</label><input className="form-control" required value={phone} onChange={event => setPhone(event.target.value)} /></div>
              <div className="col-md-2"><label className="form-label">Correo</label><input className="form-control" required type="email" value={email} onChange={event => setEmail(event.target.value)} /></div>
            </div>
            <div className="text-end mt-3"><button className="btn btn-primary" type="submit">Registrar institución</button></div>
          </form>
          <div className="table-responsive card border-0 shadow-xs">
            <table className="table table-hover align-middle mb-0">
              <thead className="table-light"><tr><th>Institución</th><th>NIT</th><th>Contacto</th><th>Teléfono</th><th>Correo</th><th>Estado</th></tr></thead>
              <tbody>
                {institutions.map(item => (
                  <tr key={item.id}>
                    <td><strong>{item.name}</strong></td>
                    <td>{item.nit}</td>
                    <td>{item.contact}</td>
                    <td>{item.phone}</td>
                    <td>{item.email}</td>
                    <td><span className={`badge ${item.active ? "bg-success" : "bg-secondary"}`}>{item.active ? "Activa" : "Inactiva"}</span></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      )}

      {activeTab === "agreements" && (
        <>
          {expiringSoonAgreements.length > 0 && (
            <div className="alert alert-warning border border-warning-subtle d-flex align-items-center gap-2 mb-3 shadow-xs">
              <span className="fs-5">⏳</span>
              <div>
                <strong>Aviso de Convenios Próximos a Vencer (Alerta temprana a 60 días):</strong> Se identificaron {expiringSoonAgreements.length} convenio(s) con fecha de vencimiento límite en los próximos 60 días. Se recomienda coordinar la prórroga o renovación contractual con las entidades receptoras.
              </div>
            </div>
          )}

          <form className="card card-body border-0 shadow-xs mb-3" onSubmit={handleAgreementSubmit}>
            <h5 className="fw-bold">Registrar convenio</h5>
            <div className="row g-2">
              <div className="col-md-3">
                <label className="form-label">Institución</label>
                <select className="form-select" required value={agreementInstitution} onChange={event => setAgreementInstitution(event.target.value)}>
                  <option value="">Selecciona institución</option>
                  {institutions.filter(item => item.active).map(item => (
                    <option key={item.id} value={item.id}>{item.name}</option>
                  ))}
                </select>
              </div>
              <div className="col-md-2"><label className="form-label">Número</label><input className="form-control" required value={agreementNumber} onChange={event => setAgreementNumber(event.target.value)} /></div>
              <div className="col-md-2"><label className="form-label">Fecha inicio</label><input className="form-control" required type="date" value={agreementStart} onChange={event => setAgreementStart(event.target.value)} /></div>
              <div className="col-md-2"><label className="form-label">Vencimiento</label><input className="form-control" required type="date" value={agreementEnd} onChange={event => setAgreementEnd(event.target.value)} /></div>
              <div className="col-md-3"><label className="form-label">Documento (nombre)</label><input className="form-control" placeholder="convenio.pdf" value={agreementDocument} onChange={event => setAgreementDocument(event.target.value)} /></div>
            </div>
            <div className="text-end mt-3"><button className="btn btn-primary" type="submit">Registrar convenio</button></div>
          </form>

          <div className="table-responsive card border-0 shadow-xs">
            <table className="table table-hover align-middle mb-0">
              <thead className="table-light">
                <tr>
                  <th>Número</th>
                  <th>Institución</th>
                  <th>Vigencia</th>
                  <th>Documento</th>
                  <th>Estado</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {agreements.map(item => {
                  const days = getDaysRemaining(item.end);
                  const isExpiring = item.status === "VIGENTE" && days >= 0 && days <= 60;
                  return (
                    <tr key={item.id}>
                      <td><strong>{item.number}</strong></td>
                      <td>{institutions.find(inst => inst.id === item.institutionId)?.name || "—"}</td>
                      <td>
                        {item.start} a {item.end}
                        {isExpiring && (
                          <span className="badge bg-warning text-dark ms-2">⚠️ Vence en {days} días</span>
                        )}
                      </td>
                      <td>{item.document}</td>
                      <td>
                        <span className={`badge ${
                          item.status === "VIGENTE"
                            ? "bg-success"
                            : item.status === "SUSPENDIDO"
                            ? "bg-warning text-dark"
                            : "bg-secondary"
                        }`}>
                          {item.status}
                        </span>
                      </td>
                      <td>
                        <button
                          type="button"
                          onClick={() => toggleAgreementStatus(item.id)}
                          className={`btn btn-sm py-0 px-2 ${
                            item.status === "SUSPENDIDO" ? "btn-outline-success" : "btn-outline-warning"
                          }`}
                          style={{ fontSize: "0.78rem" }}
                          title={item.status === "SUSPENDIDO" ? "Reactivar convenio" : "Suspender temporalmente convenio"}
                        >
                          {item.status === "SUSPENDIDO" ? "▶ Reactivar" : "⏸ Suspender"}
                        </button>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </>
      )}

      {activeTab === "places" && (
        <>
          <form className="card card-body border-0 shadow-xs mb-3" onSubmit={handlePlaceSubmit}>
            <h5 className="fw-bold">Publicar plaza de práctica</h5>
            <div className="row g-2">
              <div className="col-md-3">
                <label className="form-label">Convenio vigente</label>
                <select className="form-select" required value={placeAgreement} onChange={event => setPlaceAgreement(event.target.value)}>
                  <option value="">Selecciona convenio</option>
                  {agreements.filter(item => item.status === "VIGENTE").map(item => (
                    <option key={item.id} value={item.id}>
                      {item.number} — {institutions.find(institution => institution.id === item.institutionId)?.name}
                    </option>
                  ))}
                </select>
              </div>
              <div className="col-md-3"><label className="form-label">Programa</label><input className="form-control" required value={program} onChange={event => setProgram(event.target.value)} /></div>
              <div className="col-md-2"><label className="form-label">Nivel / perfil</label><input className="form-control" required value={level} onChange={event => setLevel(event.target.value)} /></div>
              <div className="col-md-2"><label className="form-label">Jornada</label><select className="form-select" value={shift} onChange={event => setShift(event.target.value as typeof shift)}><option value="MANANA">Mañana</option><option value="TARDE">Tarde</option><option value="COMPLETA">Completa</option></select></div>
              <div className="col-md-1"><label className="form-label">Cupos</label><input className="form-control" type="number" min={1} required value={offered} onChange={event => setOffered(Number(event.target.value))} /></div>
              <div className="col-md-1"><label className="form-label">Tutor</label><input className="form-control" required value={tutor} onChange={event => setTutor(event.target.value)} /></div>
            </div>
            <div className="text-end mt-3"><button className="btn btn-primary" type="submit">Publicar plaza</button></div>
          </form>
          <div className="table-responsive card border-0 shadow-xs">
            <table className="table table-hover align-middle mb-0">
              <thead className="table-light"><tr><th>Institución</th><th>Programa / nivel</th><th>Jornada</th><th>Cupos ocupados</th><th>Tutor</th><th>Estado</th></tr></thead>
              <tbody>
                {places.map(item => (
                  <tr key={item.id}>
                    <td>{institutions.find(institution => institution.id === agreements.find(agreement => agreement.id === item.agreementId)?.institutionId)?.name}</td>
                    <td>{item.program} — {item.level}</td>
                    <td>{item.shift}</td>
                    <td>{item.occupied} / {item.offered}</td>
                    <td>{item.tutor}</td>
                    <td><span className={`badge ${item.status === "DISPONIBLE" ? "bg-success" : "bg-secondary"}`}>{item.status}</span></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      )}
    </div>
  );
};
