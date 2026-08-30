package com.gestionpracticas.logica;

import com.gestionpracticas.dao.AsignacionDocenteDAO;
import com.gestionpracticas.modelo.AsignacionDocente;
import com.gestionpracticas.util.Utilidades;

import java.util.List;

public class AsignacionDocenteLogica {

    private AsignacionDocenteDAO dao;

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public AsignacionDocenteLogica() {

        dao = new AsignacionDocenteDAO();
    }

    // =====================================
    // ASIGNAR DOCENTE
    // =====================================

    public boolean asignar(
            AsignacionDocente a
    ) {

        // VALIDAR OBJETO

        if (a == null) {

            throw new IllegalArgumentException(
                  "Asignación nula"
            );
        }

        // VALIDAR PRACTICA

        if (a.getPractica() == null) {

            throw new IllegalArgumentException(
                  "La práctica es obligatoria"
            );
        }

        // VALIDAR DOCENTE

        if (a.getUsuarios() == null
                || a.getUsuarios().isEmpty()) {

            throw new IllegalArgumentException(
                  "Debe seleccionar un docente"
            );
        }

        // VALIDAR ROL

        if (Utilidades.esVacio(
                a.getRolEnPractica()
        )) {

            throw new IllegalArgumentException(
                  "El rol es obligatorio"
            );
        }

        // ESTADO

        a.setEstado(
                Utilidades.ESTADO_ACTIVO
        );

        // INSERTAR

        return dao.insertar(a);
    }

    // =====================================
    // LISTAR POR PRACTICA
    // =====================================

    public List listarPorPractica(
            int idPractica
    ) {

        if (idPractica <= 0) {

            throw new IllegalArgumentException(
                  "ID práctica inválido"
            );
        }

        return dao.listarPorPractica(
                idPractica
        );
    }

    // =====================================
    // LISTAR TODAS
    // =====================================

    public List listarTodas() {

        return dao.listarTodos();
    }

    // =====================================
    // ELIMINAR
    // =====================================

    public boolean eliminar(
            int id
    ) {

        if (id <= 0) {

            throw new IllegalArgumentException(
                  "ID inválido"
            );
        }

        return dao.eliminar(id);
    }

}