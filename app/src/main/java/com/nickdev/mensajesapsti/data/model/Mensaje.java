package com.nickdev.mensajesapsti.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Mensaje implements Parcelable {

    @SerializedName("id")
    private int id;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("cuerpo") // El backend suele enviar "cuerpo"
    private String cuerpo;

    @SerializedName("creadoEn") // El backend envía la fecha aquí
    private String fechaHora;

    @SerializedName("adjuntos")
    private List<Adjunto> adjuntos; // Lista de URLs

    // Constructor vacío
    public Mensaje() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public List<Adjunto> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(List<Adjunto> adjuntos) {
        this.adjuntos = adjuntos;
    }

    // Helper para contar adjuntos
    public int getConteoAdjunto() {
        return adjuntos != null ? adjuntos.size() : 0;
    }

    // --- Implementación Parcelable ---
    protected Mensaje(Parcel in) {
        id = in.readInt();
        titulo = in.readString();
        cuerpo = in.readString();
        fechaHora = in.readString();
        // Lectura optimizada de lista tipada
        adjuntos = in.createTypedArrayList(Adjunto.CREATOR);
    }
    public static final Creator<Mensaje> CREATOR = new Creator<Mensaje>() {
        @Override
        public Mensaje createFromParcel(Parcel in) { return new Mensaje(in); }

        @Override
        public Mensaje[] newArray(int size) { return new Mensaje[size]; }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(titulo);
        dest.writeString(cuerpo);
        dest.writeString(fechaHora);
        // Escritura optimizada de lista tipada
        dest.writeTypedList(adjuntos);
    }
}
