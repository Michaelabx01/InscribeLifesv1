package com.valdiviezomazautp.inscribelifes.Objetos;

public class Nota {

    String id_nota, uid_usuario, correo_usuario, fecha_hora_actual, titulo, descripcion, fecha_nota, estado;

    public Nota() {

    }

    public Nota(String id_nota, String uid_usuario, String correo_usuario, String fecha_hora_actual, String titulo, String descripcion, String fecha_nota, String estado) {
        this.id_nota = id_nota;
        this.uid_usuario = uid_usuario;
        this.correo_usuario = correo_usuario;
        this.fecha_hora_actual = fecha_hora_actual;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha_nota = fecha_nota;
        this.estado = estado;
    }

    public String getId_nota() {
        return id_nota;
    }

    public void setId_nota(String id_nota) {
        this.id_nota = id_nota;
    }

    public String getUid_usuario() {
        return uid_usuario;
    }

    public void setUid_usuario(String uid_usuario) {
        this.uid_usuario = uid_usuario;
    }

    public String getCorreo_usuario() {
        return correo_usuario;
    }

    public void setCorreo_usuario(String correo_usuario) {
        this.correo_usuario = correo_usuario;
    }

    public String getFecha_hora_actual() {
        return fecha_hora_actual;
    }

    public void setFecha_hora_actual(String fecha_hora_actual) {
        this.fecha_hora_actual = fecha_hora_actual;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha_nota() {
        return fecha_nota;
    }

    public void setFecha_nota(String fecha_nota) {
        this.fecha_nota = fecha_nota;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
