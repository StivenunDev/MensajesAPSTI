package com.nickdev.mensajesapsti.data.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Estudiante implements Parcelable {

    private  String id_estudiante;
    private String nombreCompleto;
    private String correoElectronico;
    private String telefono;
    private String carrera;
    private String periodo;
    private boolean seleccionado;

    public Estudiante(String soniaUrquizaFlores, String mail, String number, String apsti, String vi) {
    }

    public Estudiante(String id_estudiante,String nombreCompleto, String correoElectronico, String telefono, String carrera, String periodo) {
        this.id_estudiante = id_estudiante;
        this.nombreCompleto = nombreCompleto;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
        this.carrera = carrera;
        this.periodo = periodo;
        this.seleccionado = false; // Por defecto no está seleccionado
    }

    protected Estudiante(Parcel in) {
        id_estudiante = in.readString();
        nombreCompleto = in.readString();
        correoElectronico = in.readString();
        telefono = in.readString();
        carrera = in.readString();
        periodo = in.readString();
    }

    public static final Creator<Estudiante> CREATOR = new Creator<Estudiante>() {
        @Override
        public Estudiante createFromParcel(Parcel in) {
            return new Estudiante(in);
        }

        @Override
        public Estudiante[] newArray(int size) {
            return new Estudiante[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_estudiante);
        dest.writeString(nombreCompleto);
        dest.writeString(correoElectronico);
        dest.writeString(telefono);
        dest.writeString(carrera);
        dest.writeString(periodo);
    }

    // Getters
    public String obtenerNombreCompleto() { return nombreCompleto; }
    public String obtenerCorreoElectronico() { return correoElectronico; }
    public String obtenerTelefono() { return telefono; }
    public String obtenerCarrera() { return carrera; }
    public String obtenerPeriodo() { return periodo; }
    public boolean estaSeleccionado() { return seleccionado; }



    // Setters
    public void establecerSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public String getId_estudiante() {
        return id_estudiante;
    }

    public void setId_estudiante(String id_estudiante) {
        this.id_estudiante = id_estudiante;
    }
}