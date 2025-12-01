package com.nickdev.mensajesapsti.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.nickdev.mensajesapsti.data.model.api.AdminRequest;
import com.nickdev.mensajesapsti.data.network.ApiService;
import com.nickdev.mensajesapsti.data.network.RetrofitClient;
import com.nickdev.mensajesapsti.databinding.ActivityRegistroBinding;
import com.nickdev.mensajesapsti.util.InputStreamRequestBody;

import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registro extends AppCompatActivity {

    private ActivityRegistroBinding binding;
    private Uri selectedImageUri = null;

    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    binding.imgFotoPerfil.setImageURI(uri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgFotoPerfil.setOnClickListener(v -> pickImageLauncher.launch("image/*"));
        binding.lblCambiarFoto.setOnClickListener(v -> pickImageLauncher.launch("image/*"));
        binding.btnRegistrar.setOnClickListener(v -> {
            if (validarCampos()) {
                iniciarProcesoRegistro();
            }
        });
    }

    private boolean validarCampos() {
        // Limpiar errores previos
        binding.tilNombres.setError(null);
        binding.tilApellidos.setError(null);
        binding.tilCorreo.setError(null);
        binding.tilPassword.setError(null);
        binding.tilConfirmPassword.setError(null);

        String nombres = binding.etNombres.getText().toString().trim();
        String apellidos = binding.etApellidos.getText().toString().trim();
        String correo = binding.etCorreo.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirmPass = binding.etConfirmPassword.getText().toString().trim();

        boolean isValid = true;

        if (nombres.isEmpty()) {
            binding.tilNombres.setError("Ingrese sus nombres");
            isValid = false;
        }
        if (apellidos.isEmpty()) {
            binding.tilApellidos.setError("Ingrese sus apellidos");
            isValid = false;
        }

        // VALIDACIÓN DE CORREO MEJORADA
        if (correo.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            binding.tilCorreo.setError("Ingrese un correo electrónico válido");
            isValid = false;
        }

        // VALIDACIÓN DE CONTRASEÑA (MÍNIMO 8)
        if (password.length() < 8) {
            binding.tilPassword.setError("La contraseña debe tener al menos 8 caracteres");
            isValid = false;
        }

        // VALIDACIÓN DE COINCIDENCIA
        if (!password.equals(confirmPass)) {
            binding.tilConfirmPassword.setError("Las contraseñas no coinciden");
            isValid = false;
        }

        return isValid;
    }

    // ... (Métodos iniciarProcesoRegistro, subirImagenYRegistrar, registrarUsuarioEnBackend iguales a la respuesta anterior) ...
    // Asegúrate de copiar esos métodos aquí también.
    private void iniciarProcesoRegistro() {
        setLoading(true);
        if (selectedImageUri != null) {
            subirImagenYRegistrar();
        } else {
            registrarUsuarioEnBackend(null);
        }
    }

    private void subirImagenYRegistrar() {
        // ... (Código de subida de imagen - igual al anterior)
        try {
            ApiService apiService = RetrofitClient.getPublicApiService();
            RequestBody requestFile = new InputStreamRequestBody(getContentResolver(), selectedImageUri);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", "perfil.jpg", requestFile);

            apiService.uploadFile(body).enqueue(new Callback<Map<String, String>>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        registrarUsuarioEnBackend(response.body().get("url"));
                    } else {
                        setLoading(false);
                        Toast.makeText(Registro.this, "Error subiendo imagen", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    setLoading(false);
                    Toast.makeText(Registro.this, "Fallo conexión imagen", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            setLoading(false);
        }
    }

    private void registrarUsuarioEnBackend(String fotoUrl) {
        String nombres = binding.etNombres.getText().toString().trim();
        String apellidos = binding.etApellidos.getText().toString().trim();
        String correo = binding.etCorreo.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        // AdminRequest debe coincidir con tu backend
        AdminRequest request = new AdminRequest(nombres, apellidos, correo, password, fotoUrl, "operario");

        // CORRECCIÓN DEL ERROR 404: Asegurar llamada al endpoint correcto
        RetrofitClient.getPublicApiService().registrarAdmin(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    Toast.makeText(Registro.this, "¡Cuenta creada con éxito!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    // Manejo de errores (409 Conflict, 400 Bad Request, etc.)
                    Toast.makeText(Registro.this, "Error " + response.code() + ": No se pudo crear la cuenta", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                setLoading(false);
                Toast.makeText(Registro.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        binding.btnRegistrar.setEnabled(!isLoading);
        binding.btnRegistrar.setText(isLoading ? "Cargando..." : "CREAR CUENTA");
    }
}