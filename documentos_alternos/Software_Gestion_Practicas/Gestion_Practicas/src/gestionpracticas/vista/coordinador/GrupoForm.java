package com.gestionpracticas.vista.coordinador;

import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.vista.comun.CrudTablaFrame;

public class GrupoForm extends CrudTablaFrame {

    public GrupoForm(Usuario usuario) {
        super("Gestión de Grupos de Práctica", "GRUPO", "ID_GRUPO",
            new String[]{
                "ID_GRUPO", "NOMBRE", "ID_CURSO", "ID_DOCENTE", "ID_PRACTICA",
                "SEMESTRE", "PERIODO_ACADEMICO", "ANIO", "FECHA_INICIO", "FECHA_FIN",
                "CUPO_MAXIMO", "ESTADO"
            }
        );
    }
}
