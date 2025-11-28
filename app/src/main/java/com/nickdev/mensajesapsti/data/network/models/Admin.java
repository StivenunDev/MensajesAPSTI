package com.nickdev.mensajesapsti.data.network.models;
import com.google.gson.annotations.SerializedName;
public class Admin {
    @SerializedName("id")
    private int id;

    @SerializedName("correo")
    private String correo;

    @SerializedName("nombres")
    private String nombres;

    @SerializedName("apellidos")
    private String apellidos;

    @SerializedName("rol")
    private String rol;

    // Getters
    public int getId() { return id; }
    public String getCorreo() { return correo; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getRol() { return rol; }
}