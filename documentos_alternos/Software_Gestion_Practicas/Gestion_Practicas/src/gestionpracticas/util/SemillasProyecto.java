package com.gestionpracticas.util;

import com.gestionpracticas.modelo.Usuario;
import java.sql.*;
import java.util.*;

public class SemillasProyecto {

    public static String asegurarDatosBase(Usuario usuario) {
        StringBuilder log = new StringBuilder();
        try {
            Integer idInstitucion = asegurarInstitucion();
            log.append("Institución receptora: OK\n");
            Integer idPrograma = asegurarPrograma();
            log.append("Programa académico: OK\n");
            Integer idCurso = asegurarCurso(idPrograma.intValue());
            log.append("Curso académico: OK\n");
            Integer idDocente = asegurarUsuarioBasico("maria@proyectojd.com", "Maria", "Docente", "DOCENTE", "1234");
            Integer idCoordinador = asegurarUsuarioBasico("carlos@proyectojd.com", "Carlos", "Coordinador", "COORDINADOR", "1234");
            log.append("Usuarios docente/coordinador: OK\n");
            Integer idGrupo = asegurarGrupo(idCurso.intValue(), idDocente.intValue());
            log.append("Grupo de práctica: OK\n");
            Integer idPractica = asegurarPractica(idCurso.intValue(), idGrupo.intValue(), idInstitucion.intValue());
            log.append("Práctica académica: OK\n");
            if (usuario != null && esEstudiante(usuario)) {
                asegurarMatricula(usuario.getIdUsuario(), idPractica.intValue(), idGrupo.intValue());
                log.append("Matrícula del estudiante actual: OK\n");
            }
            asegurarRubrica(idDocente.intValue(), idPractica.intValue());
            log.append("Rúbrica base: OK\n");
            return log.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "No se pudieron crear todos los datos base: " + e.getMessage();
        }
    }

    private static boolean esEstudiante(Usuario u) {
        return u.getTipoUsuario() != null && u.getTipoUsuario().equalsIgnoreCase("ESTUDIANTE");
    }

    private static Integer asegurarUsuarioBasico(String email, String nombre, String apellido, String tipo, String pass) throws Exception {
        Integer existente = DBHelper.buscarIdPorTexto("USUARIO", "ID_USUARIO", "EMAIL", email);
        if (existente == null) existente = DBHelper.buscarIdPorTexto("USUARIO", "ID_USUARIO", "CORREO", email);
        if (existente != null) return existente;
        int id = DBHelper.siguienteId("USUARIO", "ID_USUARIO");
        LinkedHashMap<String,Object> d = new LinkedHashMap<String,Object>();
        d.put("ID_USUARIO", Integer.valueOf(id));
        d.put("NOMBRE", nombre);
        d.put("APELLIDO", apellido);
        d.put("EMAIL", email);
        d.put("CORREO", email);
        d.put("PASSWORD", pass);
        d.put("CONTRASENA", pass);
        d.put("ROL", tipo);
        d.put("TIPO_USUARIO", tipo);
        d.put("ESTADO", "ACTIVO");
        DBHelper.insertar("USUARIO", d);
        return Integer.valueOf(id);
    }

    private static Integer asegurarInstitucion() throws Exception {
        if (!DBHelper.existeTabla("INSTITUCION")) return Integer.valueOf(1);
        Integer existente = DBHelper.buscarIdPorTexto("INSTITUCION", "ID_INSTITUCION", "NOMBRE", "Institucion Educativa Receptora PROYECTOJD");
        if (existente != null) return existente;
        int id = DBHelper.siguienteId("INSTITUCION", "ID_INSTITUCION");
        LinkedHashMap<String,Object> d = new LinkedHashMap<String,Object>();
        d.put("ID_INSTITUCION", Integer.valueOf(id));
        d.put("NOMBRE", "Institucion Educativa Receptora PROYECTOJD");
        d.put("DIRECCION", "Calle 10 # 20-30");
        d.put("TELEFONO", "6070000000");
        d.put("EMAIL", "empresa@proyectojd.com");
        d.put("CORREO", "empresa@proyectojd.com");
        d.put("NIT", "900123456-1");
        d.put("CIUDAD", "Bucaramanga");
        d.put("MUNICIPIO", "Bucaramanga");
        d.put("REPRESENTANTE", "Rector Institucional");
        d.put("CONTACTO_NOMBRE", "Rector Institucional");
        d.put("RECTOR", "Rector Institucional");
        d.put("ESTADO", "ACTIVO");
        d.put("FECHA_REGISTRO", new java.util.Date());
        DBHelper.insertar("INSTITUCION", d);
        return Integer.valueOf(id);
    }

    private static Integer asegurarPrograma() throws Exception {
        if (!DBHelper.existeTabla("PROGRAMA")) return Integer.valueOf(1);
        Integer existente = DBHelper.buscarIdPorTexto("PROGRAMA", "ID_PROGRAMA", "NOMBRE", "Ingeniería de Sistemas");
        if (existente != null) return existente;
        int id = DBHelper.siguienteId("PROGRAMA", "ID_PROGRAMA");
        LinkedHashMap<String,Object> d = new LinkedHashMap<String,Object>();
        d.put("ID_PROGRAMA", Integer.valueOf(id));
        d.put("NOMBRE", "Ingeniería de Sistemas");
        d.put("CODIGO", "ING-SIS");
        d.put("ID_GLO_PROGRAMA", "ING-SIS");
        d.put("DESCRIPCION", "Programa base para pruebas del sistema de prácticas académicas.");
        d.put("PROYECTO", "Software para la Gestión de Prácticas Académicas");
        d.put("FACULTAD", "Facultad de Ingenierías");
        d.put("DURACION_SEMESTRES", Integer.valueOf(8));
        d.put("ESTADO", "ACTIVO");
        DBHelper.insertar("PROGRAMA", d);
        return Integer.valueOf(id);
    }

    private static Integer asegurarCurso(int idPrograma) throws Exception {
        if (!DBHelper.existeTabla("CURSO")) return Integer.valueOf(1);
        Integer existente = DBHelper.buscarIdPorTexto("CURSO", "ID_CURSO", "NOMBRE", "Práctica Académica I");
        if (existente != null) return existente;
        int id = DBHelper.siguienteId("CURSO", "ID_CURSO");
        LinkedHashMap<String,Object> d = new LinkedHashMap<String,Object>();
        d.put("ID_CURSO", Integer.valueOf(id));
        d.put("NOMBRE", "Práctica Académica I");
        d.put("CODIGO", "PA-001");
        d.put("CREDITOS", Integer.valueOf(3));
        d.put("SEMESTRE", "4");
        d.put("ID_PROGRAMA", Integer.valueOf(idPrograma));
        d.put("DESCRIPCION", "Curso de práctica académica para seguimiento y evaluación.");
        d.put("ESTADO", "ACTIVO");
        DBHelper.insertar("CURSO", d);
        return Integer.valueOf(id);
    }

    private static Integer asegurarGrupo(int idCurso, int idDocente) throws Exception {
        if (!DBHelper.existeTabla("GRUPO")) return Integer.valueOf(1);
        Integer existente = DBHelper.buscarIdPorTexto("GRUPO", "ID_GRUPO", "NOMBRE", "Grupo A-2026");
        if (existente != null) return existente;
        int id = DBHelper.siguienteId("GRUPO", "ID_GRUPO");
        LinkedHashMap<String,Object> d = new LinkedHashMap<String,Object>();
        d.put("ID_GRUPO", Integer.valueOf(id));
        d.put("NOMBRE", "Grupo A-2026");
        d.put("NOMBRE_GRUPO", "Grupo A-2026");
        d.put("ID_CURSO", Integer.valueOf(idCurso));
        d.put("ID_DOCENTE", Integer.valueOf(idDocente));
        d.put("SEMESTRE", "I");
        d.put("ANIO", Integer.valueOf(2026));
        d.put("PERIODO_ACADEMICO", "2026-I");
        d.put("FECHA_INICIO", new java.util.Date());
        d.put("ESTADO", "ACTIVO");
        DBHelper.insertar("GRUPO", d);
        return Integer.valueOf(id);
    }

    private static Integer asegurarPractica(int idCurso, int idGrupo, int idInstitucion) throws Exception {
        if (!DBHelper.existeTabla("PRACTICA")) return Integer.valueOf(1);
        String colPracticaNombre = DBHelper.primeraColumna("PRACTICA", "TITULO", "NOMBRE");
        Integer existente = null;
        if (colPracticaNombre != null) {
            existente = DBHelper.buscarIdPorTexto("PRACTICA", "ID_PRACTICA", colPracticaNombre, "Práctica Pedagógica I");
        }
        if (existente != null) return existente;
        int id = DBHelper.siguienteId("PRACTICA", "ID_PRACTICA");
        LinkedHashMap<String,Object> d = new LinkedHashMap<String,Object>();
        d.put("ID_PRACTICA", Integer.valueOf(id));
        d.put("TITULO", "Práctica Pedagógica I");
        d.put("NOMBRE", "Práctica Pedagógica I");
        d.put("DESCRIPCION", "Práctica académica base para pruebas del sistema.");
        d.put("OBJETIVOS", "Registrar actividades, horas, evidencias y evaluación del estudiante.");
        d.put("TIPO_PRACTICA", "PEDAGOGICA");
        d.put("TIPO_MODALIDAD", "PEDAGOGICA");
        d.put("HORAS_REQUERIDAS", Integer.valueOf(160));
        d.put("HORAS_REGLAMENTARIAS", Integer.valueOf(160));
        d.put("ID_CURSO", Integer.valueOf(idCurso));
        d.put("ID_GRUPO", Integer.valueOf(idGrupo));
        d.put("ID_INSTITUCION", Integer.valueOf(idInstitucion));
        d.put("FECHA_INICIO", new java.util.Date());
        d.put("ESTADO", "ACTIVO");
        DBHelper.insertar("PRACTICA", d);
        return Integer.valueOf(id);
    }

    private static Integer asegurarMatricula(int idEstudiante, int idPractica, int idGrupo) throws Exception {
        if (!DBHelper.existeTabla("MATRICULA_PRACTICA")) return Integer.valueOf(1);
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String idEst = DBHelper.primeraColumna("MATRICULA_PRACTICA", "ID_ESTUDIANTE", "ID_USUARIO");
            String idMat = DBHelper.primeraColumna("MATRICULA_PRACTICA", "ID_MATRICULA_PRACTICA", "ID_MATRICULA");
            if (idEst == null || idMat == null) return Integer.valueOf(1);
            ps = DBHelper.con().prepareStatement("SELECT " + idMat + " FROM MATRICULA_PRACTICA WHERE " + idEst + " = ?");
            ps.setInt(1, idEstudiante);
            rs = ps.executeQuery();
            if (rs.next()) return Integer.valueOf(rs.getInt(1));
        } finally {
            Utilidades.cerrar(rs); Utilidades.cerrar(ps);
        }
        String idMat = DBHelper.primeraColumna("MATRICULA_PRACTICA", "ID_MATRICULA_PRACTICA", "ID_MATRICULA");
        int id = DBHelper.siguienteId("MATRICULA_PRACTICA", idMat);
        LinkedHashMap<String,Object> d = new LinkedHashMap<String,Object>();
        d.put(idMat, Integer.valueOf(id));
        d.put("ID_ESTUDIANTE", Integer.valueOf(idEstudiante));
        d.put("ID_USUARIO", Integer.valueOf(idEstudiante));
        d.put("ID_PRACTICA", Integer.valueOf(idPractica));
        d.put("ID_GRUPO", Integer.valueOf(idGrupo));
        d.put("FECHA_MATRICULA", new java.util.Date());
        d.put("ESTADO", "ACTIVO");
        d.put("HORAS_COMPLETAS", "N");
        DBHelper.insertar("MATRICULA_PRACTICA", d);
        return Integer.valueOf(id);
    }

    private static void asegurarRubrica(int idDocente, int idPractica) throws Exception {
        if (!DBHelper.existeTabla("RUBRICA")) return;
        Integer existente = DBHelper.buscarIdPorTexto("RUBRICA", "ID_RUBRICA", "NOMBRE", "Rúbrica Base PROYECTOJD");
        if (existente != null) return;
        int id = DBHelper.siguienteId("RUBRICA", "ID_RUBRICA");
        LinkedHashMap<String,Object> d = new LinkedHashMap<String,Object>();
        d.put("ID_RUBRICA", Integer.valueOf(id));
        d.put("NOMBRE", "Rúbrica Base PROYECTOJD");
        d.put("DESCRIPCION", "Rúbrica base para valorar el desempeño del estudiante en práctica.");
        d.put("ID_DOCENTE", Integer.valueOf(idDocente));
        d.put("ID_PRACTICA", Integer.valueOf(idPractica));
        d.put("PUNTAJE_TOTAL", Double.valueOf(100));
        d.put("FECHA_CREACION", new java.util.Date());
        d.put("ESTADO", "ACTIVO");
        DBHelper.insertar("RUBRICA", d);
    }
}
