package com.nickdev.mensajesapsti.data.network.models;


public class LoginRequest {
    private String correo;
    private String password;
    public LoginRequest(String correo, String password) {
        this.correo = correo;
        this.password = password;
    }
}