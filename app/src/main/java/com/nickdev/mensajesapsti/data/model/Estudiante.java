package com.nickdev.mensajesapsti.data.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Estudiante implements Parcelable {

    private String id_estudiante;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private String carrera;
    private String periodo;
    private boolean seleccionado;


    public Estudiante(String id_estudiante,String nombreCompleto, String correoElectronico, String telefono, String carrera, String periodo) {
        this.id_estudiante = id_estudiante;
        this.nombreCompleto = nombreCompleto;
        this.email = correoElectronico;
        this.telefono = telefono;
        this.carrera = carrera;
        this.periodo = periodo;
        this.seleccionado = false; // Por defecto no está seleccionado
    }


    public Estudiante(String nombreCompleto, String correoElectronico, String telefono, String carrera, String periodo) {
        this.nombreCompleto = nombreCompleto;
        this.email = correoElectronico;
        this.telefono = telefono;
        this.carrera = carrera;
        this.periodo = periodo;
    }

    protected Estudiante(Parcel in) {
        id_estudiante = in.readString();
        nombreCompleto = in.readString();
        email = in.readString();
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
        dest.writeString(email);
        dest.writeString(telefono);
        dest.writeString(carrera);
        dest.writeString(periodo);
    }

    // Getters
    public String getNombreCompleto() { return nombreCompleto; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getCarrera() { return carrera; }
    public String getPeriodo() { return periodo; }
    public boolean EstudienteisSelect() { return seleccionado; }



    // Setters
    public void SelectEstudiante(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public String getId_estudiante() {
        return id_estudiante;
    }
    public void setId_estudiante(String id_estudiante) {
        this.id_estudiante = id_estudiante;
    }
}