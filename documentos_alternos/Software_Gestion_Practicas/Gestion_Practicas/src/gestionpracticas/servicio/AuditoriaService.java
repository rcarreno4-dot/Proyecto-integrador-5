package com.gestionpracticas.servicio;

import com.gestionpracticas.dao.AuditoriaDAO;

/** Servicio de aplicación para registrar bitácora de operaciones críticas. */
public class AuditoriaService {

    private final AuditoriaDAO auditoriaDAO = new AuditoriaDAO();

    public void registrar(Integer idUsuario, String tabla, String idRegistro, String operacion,
                          String valorAnterior, String valorNuevo, String modulo) {
        auditoriaDAO.registrar(idUsuario, tabla, idRegistro, operacion, valorAnterior, valorNuevo, modulo);
    }
}
