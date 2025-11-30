package com.nickdev.mensajesapsti.data.model.api;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
public class MensajeRequest {

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("cuerpo")
    private String cuerpo;

    // ¡IMPORTANTE! Tu backend v5 usa "admin_id"
    @SerializedName("adminId")
    private int adminId;

    // Tu backend espera "estudiantes_ids" (array de números o strings)
    @SerializedName("estudiantesIds")
    private List<Integer> estudiantesIds;

    // Tu backend espera "adjuntos" (lista de URLs strings)
    @SerializedName("adjuntos")
    private List<String> adjuntos;


    @SerializedName("canales")
    private List<String> canales;

    @SerializedName("enviarATodos")
    private boolean enviarATodos;

    public MensajeRequest(String titulo, String cuerpo, int adminId, List<String> estudiantesIdsStrings, List<String> adjuntos, boolean enviarPush) {
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.adminId = adminId;
        this.adjuntos = adjuntos;

        this.estudiantesIds = new ArrayList<>();
        if (estudiantesIdsStrings != null) {
            for (String id : estudiantesIdsStrings) {
                try {
                    this.estudiantesIds.add(Integer.parseInt(id));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        // AQUÍ HACEMOS LA MAGIA: Traducir boolean -> Lista
        this.canales = new ArrayList<>();
        if (enviarPush) {
            this.canales.add("push");
        }else{
            this.canales.add("in-app");
        }

        this.enviarATodos = (this.estudiantesIds == null || this.estudiantesIds.isEmpty());
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<String> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(List<String> adjuntos) {
        this.adjuntos = adjuntos;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public List<Integer> getEstudiantesIds() {
        return estudiantesIds;
    }

    public void setEstudiantesIds(List<Integer> estudiantesIds) {
        this.estudiantesIds = estudiantesIds;
    }

    public List<String> getCanales() {
        return canales;
    }

    // También es bueno tener el Setter por si acaso
    public void setCanales(List<String> canales) {
        this.canales = canales;
    }
}
