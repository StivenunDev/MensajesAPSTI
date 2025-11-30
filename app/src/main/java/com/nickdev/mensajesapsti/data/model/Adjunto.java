package com.nickdev.mensajesapsti.data.model;

import com.google.gson.annotations.SerializedName;

public class Adjunto {
    @SerializedName("id")
    private int id;

    @SerializedName("nombreOriginal")
    private String nombreOriginal;

    @SerializedName("url")
    private String url;

    // Getters
    public String getUrl() {
        return url;
    }

    public String getNombreOriginal() {
        return nombreOriginal;
    }
}