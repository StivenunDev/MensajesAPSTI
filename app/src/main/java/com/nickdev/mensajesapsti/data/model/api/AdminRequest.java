package com.nickdev.mensajesapsti.data.model.api;

import com.google.gson.annotations.SerializedName;

public class AdminRequest {
    @SerializedName("nombres")
    private String nombres;
    @SerializedName("apellidos")
    private String apellidos;
    @SerializedName("correo")
    private String correo;
    @SerializedName("password")
    private String password;
    @SerializedName("fotoUrl")
    private String fotoUrl;
    @SerializedName("rol")
    private String rol; // "admin" o "operario"

    public AdminRequest(String nombres, String apellidos, String correo, String password, String fotoUrl, String rol) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.password = password;
        this.fotoUrl = fotoUrl;
        this.rol = rol;
    }
}