package com.gestionpracticas.vista.simple;

import com.gestionpracticas.modelo.Usuario;
import com.gestionpracticas.util.ConexionBD;
import com.gestionpracticas.util.DBHelper;
import com.gestionpracticas.util.Utilidades;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * Vista integral para coordinador, institución o docente.
 * Permite seleccionar grupo/práctica y ver estudiantes, institución, docente, horas,
 * actividades y evaluaciones sin escribir ID manualmente.
 */
public class SeguimientoIntegralSimpleForm extends JFrame {
    private final Usuario usuario;
    private JComboBox<SimpleData.Item> cmbGrupo;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel lblInfo;

    public SeguimientoIntegralSimpleForm(Usuario usuario) { this.usuario = usuario; init(); cargarGrupos(); cargarEstudiantesGrupo(); }

    private void init() {
        setTitle("Seguimiento integral de estudiantes en práctica"); setSize(1280, 740); setLocationRelativeTo(null); setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        com.gestionpracticas.util.PantallaUtil.instalarMenuResolucion(this);
        JPanel root = new JPanel(new BorderLayout(12,12)); root.setBackground(new Color(245,247,250)); root.setBorder(new EmptyBorder(12,12,12,12));
        JPanel header = new JPanel(new BorderLayout()); header.setBackground(new Color(33,97,140)); header.setBorder(new EmptyBorder(16,20,16,20));
        JLabel title = new JLabel("Seguimiento integral por grupo, estudiante e institución"); title.setFont(new Font("Segoe UI", Font.BOLD, 22)); title.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Seleccione un grupo y consulte estudiantes asignados, prácticas realizadas, horas validadas, docente y evaluaciones."); sub.setForeground(Color.WHITE);
        header.add(title, BorderLayout.NORTH); header.add(sub, BorderLayout.SOUTH);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)); top.setBackground(Color.WHITE);
        cmbGrupo = new JComboBox<SimpleData.Item>(); cmbGrupo.setPreferredSize(new Dimension(320, 32));
        JButton btnEst = boton("Estudiantes del grupo", new Color(52,152,219)); btnEst.addActionListener(e -> cargarEstudiantesGrupo());
        JButton btnAct = boton("Actividades y horas", new Color(39,174,96)); btnAct.addActionListener(e -> cargarActividadesHoras());
        JButton btnEval = boton("Evaluaciones", new Color(142,68,173)); btnEval.addActionListener(e -> cargarEvaluaciones());
        JButton btnHoras = boton("Abrir control horas", new Color(243,156,18)); btnHoras.addActionListener(e -> new HorasSeguimientoSimpleForm(usuario).setVisible(true));
        JButton btnRef = boton("Refrescar grupos", new Color(52,73,94)); btnRef.addActionListener(e -> { cargarGrupos(); cargarEstudiantesGrupo(); });
        top.add(new JLabel("Grupo:")); top.add(cmbGrupo); top.add(btnEst); top.add(btnAct); top.add(btnEval); top.add(btnHoras); top.add(btnRef);
        modelo = new DefaultTableModel(); tabla = new JTable(modelo); tabla.setRowHeight(28); lblInfo = new JLabel(" "); lblInfo.setBorder(new EmptyBorder(8,8,8,8));
        JPanel center = new JPanel(new BorderLayout()); center.setBackground(Color.WHITE); center.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220,220,220)), new EmptyBorder(8,8,8,8)));
        center.add(top, BorderLayout.NORTH); center.add(new JScrollPane(tabla), BorderLayout.CENTER); center.add(lblInfo, BorderLayout.SOUTH);
        root.add(header, BorderLayout.NORTH); root.add(center, BorderLayout.CENTER); setContentPane(root);
    }
    private JButton boton(String t, Color c) { JButton b = new JButton(t); b.setBackground(c); b.setForeground(Color.WHITE); b.setFocusPainted(false); b.setFont(new Font("Segoe UI", Font.BOLD, 12)); return b; }

    private void cargarGrupos() {
        cmbGrupo.removeAllItems();
        if (!DBHelper.existeTabla("GRUPO")) return;
        String nombre = SimpleData.grupoNombreExpr("G");
        String prac = DBHelper.existeTabla("PRACTICA") ? SimpleData.practicaTituloExpr("P") : SimpleData.lit("Sin práctica");
        String inst = DBHelper.existeTabla("INSTITUCION") ? SimpleData.institucionNombreExpr("I") : SimpleData.lit("Sin institución");
        String sql = "SELECT G.ID_GRUPO, " + nombre + " || ' - ' || " + prac + " || ' - ' || " + inst + " FROM GRUPO G LEFT JOIN PRACTICA P ON P.ID_PRACTICA=G.ID_PRACTICA LEFT JOIN INSTITUCION I ON I.ID_INSTITUCION=NVL(G.ID_INSTITUCION, P.ID_INSTITUCION) ORDER BY " + nombre;
        Statement st=null; ResultSet rs=null;
        try { st=ConexionBD.getConnection().createStatement(); rs=st.executeQuery(sql); while(rs.next()) cmbGrupo.addItem(new SimpleData.Item(rs.getInt(1), rs.getString(2))); }
        catch(Exception e) { System.err.println("Seguimiento.cargarGrupos: " + e.getMessage()); }
        finally { Utilidades.cerrar(rs); try{if(st!=null)st.close();}catch(Exception e){} }
    }

    private Integer grupoSeleccionado() { SimpleData.Item i=(SimpleData.Item)cmbGrupo.getSelectedItem(); return i==null?null:Integer.valueOf(i.id); }

    private String docenteAsignadoExpr() {
        if (SimpleData.has("GRUPO", "ID_DOCENTE")) {
            return "(SELECT " + SimpleData.usuarioNombreExpr("D") + " FROM USUARIO D WHERE D.ID_USUARIO=G.ID_DOCENTE AND ROWNUM=1)";
        }
        if (DBHelper.existeTabla("ASIGNACION_DOCENTE") && SimpleData.has("ASIGNACION_DOCENTE", "ID_DOCENTE")) {
            String filtro = SimpleData.has("ASIGNACION_DOCENTE", "ID_GRUPO") ? "A.ID_GRUPO=M.ID_GRUPO" : "1=1";
            if (SimpleData.has("ASIGNACION_DOCENTE", "ID_PRACTICA")) filtro += " OR A.ID_PRACTICA=M.ID_PRACTICA";
            return "(SELECT " + SimpleData.usuarioNombreExpr("D") + " FROM ASIGNACION_DOCENTE A LEFT JOIN USUARIO D ON D.ID_USUARIO=A.ID_DOCENTE WHERE (" + filtro + ") AND ROWNUM=1)";
        }
        return SimpleData.lit("Sin docente asignado");
    }

    private void cargarEstudiantesGrupo() {
        String estCol = SimpleData.matriculaEstudianteCol(); if(estCol==null) { error("No se encuentra columna de estudiante en matrícula."); return; }
        Integer idG = grupoSeleccionado();
        String instJoinExpr = SimpleData.matriculaInstitucionExpr("M", "P");
        String joins = " JOIN USUARIO U ON U.ID_USUARIO=M." + estCol + " LEFT JOIN PRACTICA P ON P.ID_PRACTICA=M.ID_PRACTICA LEFT JOIN GRUPO G ON G.ID_GRUPO=M.ID_GRUPO ";
        if(instJoinExpr!=null) joins += " LEFT JOIN INSTITUCION I ON I.ID_INSTITUCION=" + instJoinExpr + " ";
        String inst = instJoinExpr==null?SimpleData.lit("Sin institución"):SimpleData.institucionNombreExpr("I");
        String docente = docenteAsignadoExpr();
        String req = SimpleData.q("P", "PRACTICA", "HORAS_REQUERIDAS", "HORAS_REGLAMENTARIAS"); if(req==null) req="0";
        String where = " WHERE 1=1"; if(idG!=null && SimpleData.has("MATRICULA_PRACTICA","ID_GRUPO")) where += " AND M.ID_GRUPO=" + idG.intValue();
        String sql = "SELECT " + SimpleData.usuarioNombreExpr("U") + ", " + SimpleData.practicaTituloExpr("P") + ", " + SimpleData.grupoNombreExpr("G") + ", " + inst + ", NVL(" + docente + ", 'Sin docente asignado'), " + SimpleData.etapaMatriculaExpr("M") + ", " + SimpleData.estadoExpr("M", "MATRICULA_PRACTICA", "ACTIVO") + ", " + req + ", " + horasReportadasSub("M") + ", " + horasAprobadasSub("M") + " FROM MATRICULA_PRACTICA M " + joins + where + " ORDER BY " + SimpleData.usuarioNombreExpr("U");
        cargar("Estudiantes asignados al grupo", new String[]{"Estudiante", "Práctica", "Grupo", "Institución", "Docente", "Etapa", "Estado", "Horas requeridas", "Reportadas", "Aprobadas"}, sql);
    }

    private void cargarActividadesHoras() {
        String estCol = SimpleData.matriculaEstudianteCol(); if(estCol==null) { error("No se encuentra columna de estudiante."); return; }
        if(!DBHelper.existeTabla("REGISTRO_ACTIVIDAD")) { error("No existe REGISTRO_ACTIVIDAD."); return; }
        String matCol=SimpleData.col("REGISTRO_ACTIVIDAD","ID_MATRICULA_PRACTICA","ID_MATRICULA"); if(matCol==null) { error("REGISTRO_ACTIVIDAD no tiene matrícula."); return; }
        Integer idG=grupoSeleccionado();
        String fecha=SimpleData.q("R","REGISTRO_ACTIVIDAD","FECHA_ACTIVIDAD","FECHA_REGISTRO","FECHA"); if(fecha==null) fecha="SYSDATE";
        String horas=SimpleData.q("R","REGISTRO_ACTIVIDAD","HORAS","HORAS_REPORTADAS"); if(horas==null) horas="0";
        String desc=SimpleData.q("R","REGISTRO_ACTIVIDAD","DESCRIPCION","ACTIVIDAD"); if(desc==null) desc=SimpleData.lit("");
        String obs=SimpleData.q("R","REGISTRO_ACTIVIDAD","OBSERVACIONES","OBSERVACION"); if(obs==null) obs=SimpleData.lit("");
        String instJoinExpr = SimpleData.matriculaInstitucionExpr("M", "P");
        String joins=" JOIN MATRICULA_PRACTICA M ON M.ID_MATRICULA_PRACTICA=R."+matCol+" JOIN USUARIO U ON U.ID_USUARIO=M."+estCol+" LEFT JOIN PRACTICA P ON P.ID_PRACTICA=M.ID_PRACTICA LEFT JOIN GRUPO G ON G.ID_GRUPO=M.ID_GRUPO "; if(instJoinExpr!=null) joins += " LEFT JOIN INSTITUCION I ON I.ID_INSTITUCION="+instJoinExpr+" ";
        String where=" WHERE 1=1"; if(idG!=null && SimpleData.has("MATRICULA_PRACTICA","ID_GRUPO")) where += " AND M.ID_GRUPO="+idG.intValue();
        String inst=instJoinExpr==null?SimpleData.lit("Sin institución"):SimpleData.institucionNombreExpr("I");
        String sql="SELECT "+SimpleData.usuarioNombreExpr("U")+", "+SimpleData.practicaTituloExpr("P")+", "+SimpleData.grupoNombreExpr("G")+", "+inst+", TO_CHAR("+fecha+",'YYYY-MM-DD'), "+horas+", CASE WHEN UPPER(NVL(R.ESTADO,'PENDIENTE'))='APROBADO' THEN "+horas+" ELSE 0 END, "+SimpleData.estadoExpr("R","REGISTRO_ACTIVIDAD","PENDIENTE")+", "+desc+", "+obs+" FROM REGISTRO_ACTIVIDAD R "+joins+where+" ORDER BY "+fecha+" DESC";
        cargar("Actividades y horas del estudiante", new String[]{"Estudiante", "Práctica", "Grupo", "Institución", "Fecha", "Reportadas", "Aprobadas", "Estado", "Actividad", "Observación"}, sql);
    }

    private void cargarEvaluaciones() {
        String estCol = SimpleData.matriculaEstudianteCol(); if(estCol==null) { error("No se encuentra columna de estudiante."); return; }
        if(!DBHelper.existeTabla("EVALUACION")) { error("No existe EVALUACION."); return; }
        Integer idG=grupoSeleccionado();
        String where=" WHERE 1=1"; if(idG!=null && SimpleData.has("MATRICULA_PRACTICA","ID_GRUPO")) where += " AND M.ID_GRUPO="+idG.intValue();
        String respJoin=DBHelper.existeTabla("RESPUESTA")?" LEFT JOIN RESPUESTA R ON R.ID_PREGUNTA=Q.ID_PREGUNTA ":"";
        String pregunta=DBHelper.existeTabla("PREGUNTA")?SimpleData.preguntaTextoExpr("Q"):SimpleData.lit(""); String respuesta=DBHelper.existeTabla("RESPUESTA")?SimpleData.respuestaTextoExpr("R"):SimpleData.lit("");
        String obs=SimpleData.q("E","EVALUACION","OBSERVACIONES","OBSERVACION"); if(obs==null) obs=SimpleData.lit("");
        String sql="SELECT "+SimpleData.usuarioNombreExpr("U")+", "+SimpleData.practicaTituloExpr("P")+", "+pregunta+", "+respuesta+", "+SimpleData.estadoExpr("E","EVALUACION","PENDIENTE")+", "+SimpleData.notaExpr("E")+", "+obs+" FROM EVALUACION E JOIN MATRICULA_PRACTICA M ON M.ID_MATRICULA_PRACTICA=E.ID_MATRICULA_PRACTICA JOIN USUARIO U ON U.ID_USUARIO=M."+estCol+" LEFT JOIN PRACTICA P ON P.ID_PRACTICA=M.ID_PRACTICA LEFT JOIN PREGUNTA Q ON Q.ID_EVALUACION=E.ID_EVALUACION "+respJoin+where+" ORDER BY E.ID_EVALUACION DESC";
        cargar("Evaluaciones asignadas y respuestas", new String[]{"Estudiante", "Práctica", "Pregunta", "Respuesta", "Estado", "Nota", "Comentario docente"}, sql);
    }

    private String horasReportadasSub(String aliasM) { String s="0"; if(DBHelper.existeTabla("REGISTRO_ACTIVIDAD")) { String c=SimpleData.col("REGISTRO_ACTIVIDAD","HORAS","HORAS_REPORTADAS"); String m=SimpleData.col("REGISTRO_ACTIVIDAD","ID_MATRICULA_PRACTICA","ID_MATRICULA"); if(c!=null&&m!=null) s += "+(SELECT NVL(SUM(R."+c+"),0) FROM REGISTRO_ACTIVIDAD R WHERE R."+m+"="+aliasM+".ID_MATRICULA_PRACTICA)"; } if(DBHelper.existeTabla("HORAS_PRACTICA")) { String c=SimpleData.col("HORAS_PRACTICA","HORAS_REPORTADAS","HORAS"); String m=SimpleData.col("HORAS_PRACTICA","ID_MATRICULA_PRACTICA","ID_MATRICULA"); if(c!=null&&m!=null) s += "+(SELECT NVL(SUM(H."+c+"),0) FROM HORAS_PRACTICA H WHERE H."+m+"="+aliasM+".ID_MATRICULA_PRACTICA)"; } return "("+s+")"; }
    private String horasAprobadasSub(String aliasM) { String s="0"; if(DBHelper.existeTabla("REGISTRO_ACTIVIDAD")) { String c=SimpleData.col("REGISTRO_ACTIVIDAD","HORAS","HORAS_REPORTADAS"); String m=SimpleData.col("REGISTRO_ACTIVIDAD","ID_MATRICULA_PRACTICA","ID_MATRICULA"); if(c!=null&&m!=null) s += "+(SELECT NVL(SUM(R."+c+"),0) FROM REGISTRO_ACTIVIDAD R WHERE R."+m+"="+aliasM+".ID_MATRICULA_PRACTICA AND UPPER(NVL(R.ESTADO,'PENDIENTE'))='APROBADO')"; } if(DBHelper.existeTabla("HORAS_PRACTICA")) { String c=SimpleData.col("HORAS_PRACTICA","HORAS_APROBADAS"); String m=SimpleData.col("HORAS_PRACTICA","ID_MATRICULA_PRACTICA","ID_MATRICULA"); if(c!=null&&m!=null) s += "+(SELECT NVL(SUM(H."+c+"),0) FROM HORAS_PRACTICA H WHERE H."+m+"="+aliasM+".ID_MATRICULA_PRACTICA)"; } return "("+s+")"; }

    private void cargar(String nombre, String[] cols, String sql) { modelo.setDataVector(new Object[0][cols.length], cols); Statement st=null; ResultSet rs=null; int count=0; try { st=ConexionBD.getConnection().createStatement(); rs=st.executeQuery(sql); while(rs.next()) { Object[] row=new Object[cols.length]; for(int i=0;i<cols.length;i++) row[i]=rs.getObject(i+1); modelo.addRow(row); count++; } lblInfo.setText(nombre + " | Registros: " + count); } catch(Exception e) { lblInfo.setText("No se pudo cargar: " + e.getMessage()); System.err.println("Seguimiento SQL: " + sql); } finally { Utilidades.cerrar(rs); try{if(st!=null)st.close();}catch(Exception e){} } }
    private void error(String m) { modelo.setDataVector(new Object[0][1], new String[]{"Mensaje"}); lblInfo.setText(m); }
}
