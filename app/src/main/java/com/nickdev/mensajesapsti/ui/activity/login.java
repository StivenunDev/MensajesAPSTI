package com.nickdev.mensajesapsti.ui.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.nickdev.mensajesapsti.databinding.ActivityLoginBinding;
import com.nickdev.mensajesapsti.ui.viewmodel.LoginViewModel;
import com.nickdev.mensajesapsti.util.SessionManager;


public class login extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            binding = ActivityLoginBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());


        // Inicializar ViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        sessionManager = new SessionManager(getApplicationContext());

        // Configurar el listener del botón para llamar al ViewModel
        binding.loginButton.setOnClickListener(v -> {
            hideKeyboard();
            String email = binding.emailEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString().trim();
            loginViewModel.login(email, password);
        });

        // Configurar los observadores que reaccionan a los cambios del ViewModel
        setupObservers();
    }

    private void setupObservers() {
        // Observador para el resultado del login
        loginViewModel.getLoginResult().observe(this, success -> {
            if (success != null && success) {
                // Si el login fue exitoso, navegar a MainActivity
                sessionManager.createLoginSession();
                Intent intent = new Intent(login.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finaliza LoginActivity para que no se pueda volver atrás
            }
        });

        // Observador para el estado de carga
        loginViewModel.isLoading.observe(this, isLoading -> {
            if (isLoading != null && isLoading) {
                binding.loadingProgressBar.setVisibility(View.VISIBLE);
                binding.loginButton.setEnabled(false); // Deshabilita el botón mientras carga
            } else {
                binding.loadingProgressBar.setVisibility(View.GONE);
                binding.loginButton.setEnabled(true); // Habilita el botón al terminar
            }
        });

        // Observador para los mensajes de error
        loginViewModel.errorMessage.observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}