package com.nickdev.mensajesapsti.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Estudiante implements Parcelable {

    private String id_estudiante; // Lo mantenemos por si lo usas en el futuro con la BD
    private String nombreCompleto;
    private String correoElectronico;
    private String telefono;
    private String carrera;
    private String periodo;
    private boolean seleccionado;

    /**
     * CORRECCIÓN: Este es ahora el único constructor.
     * Recibe los datos necesarios y se asegura de inicializar todas las variables.
     */
    public Estudiante(String nombreCompleto, String correoElectronico, String telefono, String carrera, String periodo) {
        this.nombreCompleto = nombreCompleto;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
        this.carrera = carrera;
        this.periodo = periodo;
        this.seleccionado = false; // Se asegura de que 'seleccionado' siempre tenga un valor inicial.
    }

    // --- Constructor para Parcelable (uso interno de Android) ---
    protected Estudiante(Parcel in) {
        id_estudiante = in.readString();
        nombreCompleto = in.readString();
        correoElectronico = in.readString();
        telefono = in.readString();
        carrera = in.readString();
        periodo = in.readString();
        // CORRECCIÓN: Leemos el estado de 'seleccionado' desde el Parcel.
        seleccionado = in.readByte() != 0;
    }

    // --- Métodos de la interfaz Parcelable ---
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id_estudiante);
        dest.writeString(nombreCompleto);
        dest.writeString(correoElectronico);
        dest.writeString(telefono);
        dest.writeString(carrera);
        dest.writeString(periodo);
        // CORRECCIÓN: Escribimos el estado de 'seleccionado' en el Parcel.
        dest.writeByte((byte) (seleccionado ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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

    // --- Getters y Setters (sin cambios, ya estaban bien) ---
    public String getNombreCompleto() { return nombreCompleto; }
    public String getEmail() { return correoElectronico; }
    public String getTelefono() { return telefono; }
    public String getCarrera() { return carrera; }
    public String getPeriodo() { return periodo; }
    public boolean estaSeleccionado() { return seleccionado; }

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

