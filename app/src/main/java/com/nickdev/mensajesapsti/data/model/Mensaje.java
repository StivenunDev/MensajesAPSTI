package com.nickdev.mensajesapsti.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Mensaje {

    @SerializedName("id")
    private int id;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("cuerpo") // El backend suele enviar "cuerpo"
    private String cuerpo;

    @SerializedName("creadoEn") // El backend envía la fecha aquí
    private String fechaHora;

    @SerializedName("adjuntos")
    private List<String> adjuntos; // Lista de URLs

    // Constructor vacío
    public Mensaje() {}

    // Getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getCuerpo() { return cuerpo; }
    public String getFechaHora() { return fechaHora; }
    public List<String> getAdjuntos() { return adjuntos; }

    // Helper para contar adjuntos
    public int getConteoAdjunto() {
        return adjuntos != null ? adjuntos.size() : 0;
    }
}