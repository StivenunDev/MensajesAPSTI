package com.nickdev.mensajesapsti.ui.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.nickdev.mensajesapsti.databinding.ActivityLoginBinding;
import com.nickdev.mensajesapsti.ui.viewmodel.LoginViewModel;
import com.nickdev.mensajesapsti.util.SessionManager;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);// Inicializar ViewModel

        // Configurar el listener del botón para llamar al ViewModel
        binding.btnLogin.setOnClickListener(v -> {
            hideKeyboard();
            String email = binding.txtEmail.getText().toString().trim();
            String password = binding.txtPassword.getText().toString().trim();
            loginViewModel.login(email, password);
        });
        setupObservers();// Configurar los observadores que reaccionan a los cambios del ViewModel

        binding.lblCrearCuenta.setOnClickListener(view -> {
            // 3. Crear el Intent hacia RegistroActivity
            Intent intent = new Intent(LoginActivity.this, Registro.class);
            startActivity(intent);
        });
    }

    private void setupObservers() {
        // Observador para el resultado del login
        loginViewModel.getLoginResult().observe(this, success -> {
            if (success != null && success) {

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finaliza LoginActivity para que no se pueda volver atrás
            }
        });

        // Observador para el estado de carga
        loginViewModel.isLoading.observe(this, isLoading -> {
            if (isLoading != null && isLoading) {
                binding.loadingProgressBar.setVisibility(View.VISIBLE);
                binding.btnLogin.setEnabled(false); // Deshabilita el botón mientras carga
            } else {
                binding.loadingProgressBar.setVisibility(View.GONE);
                binding.btnLogin.setEnabled(true); // Habilita el botón al terminar
            }
        });

        // Observador para los mensajes de error
        loginViewModel.errorMessage.observe(this, error -> {
            if (!TextUtils.isEmpty(error)) {
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