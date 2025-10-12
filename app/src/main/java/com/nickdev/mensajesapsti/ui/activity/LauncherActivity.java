package com.nickdev.mensajesapsti.ui.activity;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nickdev.mensajesapsti.util.SessionManager;


public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager sessionManager = new SessionManager(getApplicationContext());

        // Comprueba si el usuario ya ha iniciado sesión
        if (sessionManager.isLoggedIn()) {
            // Si ya hay sesión, va a la pantalla principal
            startActivity(new Intent(LauncherActivity.this, MainActivity.class));
        } else {
            // Si no hay sesión, va a la pantalla de login
            startActivity(new Intent(LauncherActivity.this, login.class));
        }

        // Finaliza esta actividad para que el usuario no pueda volver a ella
        finish();
    }
}
