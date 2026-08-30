package com.gestionpracticas.servicio;

import com.gestionpracticas.util.DBHelper;
import java.util.LinkedHashMap;

/** Servicio para emitir certificados con hash verificable. */
public class CertificadoService {

    private final AuditoriaService auditoriaService = new AuditoriaService();

    public int emitirCertificado(int idMatriculaPractica, int idUsuarioEmite) throws Exception {
        int id = DBHelper.siguienteId("CERTIFICADO_PRACTICA", "ID_CERTIFICADO_PRACTICA");
        String hash = ReportePdfService.sha256("CERTIFICADO|" + idMatriculaPractica + "|" + System.currentTimeMillis());
        LinkedHashMap<String, Object> datos = new LinkedHashMap<>();
        datos.put("ID_CERTIFICADO_PRACTICA", Integer.valueOf(id));
        datos.put("ID_MATRICULA_PRACTICA", Integer.valueOf(idMatriculaPractica));
        datos.put("ID_USUARIO_EMITE", Integer.valueOf(idUsuarioEmite));
        datos.put("HASH_VERIFICACION", hash);
        datos.put("FIRMA_DIGITAL_SIMULADA", "Firmado digitalmente por Coordinación de Prácticas");
        datos.put("ESTADO", "EMITIDO");
        if (DBHelper.insertar("CERTIFICADO_PRACTICA", datos)) {
            auditoriaService.registrar(Integer.valueOf(idUsuarioEmite), "CERTIFICADO_PRACTICA", String.valueOf(id),
                    "INSERT", null, datos.toString(), "CertificadoService");
            return id;
        }
        throw new IllegalStateException("No fue posible emitir el certificado.");
    }
}
