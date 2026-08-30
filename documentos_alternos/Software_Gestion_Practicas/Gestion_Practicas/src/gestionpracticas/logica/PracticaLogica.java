package com.gestionpracticas.logica;

import com.gestionpracticas.dao.PracticaDAO;
import com.gestionpracticas.modelo.Practica;
import com.gestionpracticas.util.Utilidades;

import java.util.List;

public class PracticaLogica {

    private PracticaDAO dao;

    public PracticaLogica() {

        dao = new PracticaDAO();
    }

    // =====================================
    // GUARDAR PRACTICA
    // =====================================

    public boolean guardar(
        Practica p
    ) {

        validarPractica(p);

        p.setEstado(
            Utilidades.ESTADO_ACTIVO
        );

        return dao.insertar(p);
    }

    // =====================================
    // ACTUALIZAR PRACTICA
    // =====================================

    public boolean actualizar(
        Practica p
    ) {

        if(p == null ||
           p.getIdPractica() <= 0) {

            throw new IllegalArgumentException(
              "Práctica inválida"
            );
        }

        validarPractica(p);

        return dao.actualizar(p);
    }

    // =====================================
    // ELIMINAR PRACTICA
    // =====================================

    public boolean eliminar(
        int id
    ) {

        if(id <= 0) {

            throw new IllegalArgumentException(
              "ID inválido"
            );
        }

        return dao.eliminar(id);
    }

    // =====================================
    // LISTAR TODAS
    // =====================================

    public List obtenerTodas() {

        return dao.listarTodas();
    }

    // =====================================
    // BUSCAR POR ID
    // =====================================

    public Practica buscarPorId(
        int id
    ) {

        if(id <= 0) {

            throw new IllegalArgumentException(
              "ID inválido"
            );
        }

        return dao.buscarPorId(id);
    }

    // =====================================
    // VALIDACIONES
    // =====================================

    private void validarPractica(
        Practica p
    ) {

        if(p == null) {

            throw new IllegalArgumentException(
              "La práctica es nula"
            );
        }

        // TITULO

        if(p.getTitulo() == null ||
           p.getTitulo().trim().equals("")) {

            throw new IllegalArgumentException(
              "Ingrese el título"
            );
        }

        // DESCRIPCION

        if(p.getDescripcion() == null ||
           p.getDescripcion().trim().equals("")) {

            throw new IllegalArgumentException(
              "Ingrese la descripción"
            );
        }

        // HORAS

        if(p.getHorasRequeridas() <= 0) {

            throw new IllegalArgumentException(
              "Horas inválidas"
            );
        }

        // TIPO

        if(p.getTipoPractica() == null ||
           p.getTipoPractica().trim().equals("")) {

            throw new IllegalArgumentException(
              "Seleccione el tipo"
            );
        }

        // En la versión conectada a la base de datos de la U,
        // el formulario de práctica registra los datos básicos.
        // Las relaciones con programa, grupo e institución se manejan
        // en módulos separados de asignación y matrícula.
    }
    public boolean activar(int id) {
        return dao.cambiarEstado(id, "ACTIVO");
    }

    public boolean inactivar(int id) {
        return dao.cambiarEstado(id, "INACTIVO");
    }

    public boolean eliminarDefinitivo(int id) {
        return dao.eliminarFisico(id);
    }
}