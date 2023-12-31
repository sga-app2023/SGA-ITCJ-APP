package com.example.rsu_itcjapp;

import com.example.rsu_itcjapp.datos.Alumno;
import com.example.rsu_itcjapp.datos.Usuario;

public class DatosSistema {

    public static final String LAYOUT = "layout";
    public static final String USUARIO = "tipoUsuario";
    public static final String USUARIOS = "usuarios";
    public static final String USUARIO_DOCENTE = "docente";
    public static final String USUARIO_ALUMNO = "alumno";
    public static final String USUARIO_TRABAJADOR = "trabajador";
    public static final String REC = "Reciclaje";
    public static final String MPT = "Marcadores y Pilas";
    public static final String RSP = "Residuos Peligrosos";
    public static final String STR = "Sistema de Riego";
    public static final String EMAIL = "Enviar correo";
    public static final String PERFIL = "Perfil";
    public static final String AVISO = "Generar aviso";
    public static final String CERRAR_SESION = "Cerrar sesión";
    public static final String REPORTE_BIMESTRAL = "Alumnos servicio social";

    public static final String CORREO_DIR = "dir_cdjuarez@tecnm.mx";

    public static class Layouts {
        public static final int RECICLAJE = R.layout.layout_reciclaje;
        public static final int MARCADORESPILAS = R.layout.layout_marcadores_pilas;
        public static final int RESIDUOSPELIGROSOS = R.layout.layout_residuos_peligrosos;
        public static final int SISTEMADERIEGO = R.layout.layout_sistema_riego;
        public static final int ENVIARCORREO = R.layout.layout_enviar_correo;
        public static final int VERPERFIL = R.layout.layout_informacion_usuario;
        public static final int GENERARAVISO = R.layout.layout_generar_aviso;
        public static final int ALUMNOSSERVICIO = R.layout.layout_reportes_alumnos_servicio;
    }

    public static class DatosUsuario {
        public static Usuario usuario = null;
        public static Alumno alumno = null;
    }
}
