package com.nickdev.mensajesapsti.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.nickdev.mensajesapsti.databinding.ActivityHistorialBinding;


public class Historial extends AppCompatActivity {

    private ActivityHistorialBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistorialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configura la barra de herramientas para que el botón de atrás funcione
        setSupportActionBar(binding.historyToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    // Maneja el clic en el botón de atrás de la barra de herramientas
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
