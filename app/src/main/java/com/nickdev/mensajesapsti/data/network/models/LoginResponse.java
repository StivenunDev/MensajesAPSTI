package com.nickdev.mensajesapsti.data.network.models;

import com.google.gson.annotations.SerializedName;

// Este POJO captura la respuesta del backend: {"accessToken": "...", "admin": {...}}
// Asumo que el backend devuelve el token Y los datos del admin
public class LoginResponse {

    @SerializedName("access_token") // Asegúrate que coincida con tu JSON de respuesta
    private String access_token;

    @SerializedName("user") // Asumo que el backend devuelve el objeto del admin
    private Admin admin;

    // Getters
    public String getAccessToken() { return access_token; }
    public Admin getAdmin() { return admin; }
}