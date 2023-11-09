package com.example.rsu_itcjapp.datos;

import java.util.HashMap;

public class Bitacora {


    private int matriculaUsuario;
    private String idUsuario;
    private HashMap<String, String> fechaIngreso;

    public Bitacora() {

    }

    public Bitacora(String idUsuario, HashMap<String, String> fechaIngreso, int matriculaUsuario){
        this.idUsuario = idUsuario;
        this.fechaIngreso = fechaIngreso;
        this.matriculaUsuario = matriculaUsuario;
    }

    public HashMap<String, String> getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(HashMap<String, String> fecha) {
        this.fechaIngreso = fecha;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public int getMatriculaUsuario() {
        return matriculaUsuario;
    }

    public void setMatriculaUsuario(int matriculaUsuario) {
        this.matriculaUsuario = matriculaUsuario;
    }
}
