package com.nickdev.mensajesapsti.ui.viewmodel;


import android.app.Application;
import android.text.TextUtils;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nickdev.mensajesapsti.data.network.ApiService;
import com.nickdev.mensajesapsti.data.network.RetrofitClient;
import com.nickdev.mensajesapsti.data.network.models.Admin;
import com.nickdev.mensajesapsti.data.network.models.LoginRequest;
import com.nickdev.mensajesapsti.data.network.models.LoginResponse;
import com.nickdev.mensajesapsti.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> _loginResult = new MutableLiveData<>();
    public final LiveData<Boolean> loginResult = _loginResult;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public final LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public final LiveData<String> errorMessage = _errorMessage;

    private final SessionManager sessionManager;
    private final ApiService apiService;
    public LoginViewModel(Application application) {
        super(application);
        this.sessionManager = new SessionManager(application.getApplicationContext());
        this.apiService = RetrofitClient.getPublicApiService(); // Usamos el cliente PÚBLICO
    }

    public void login(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            _errorMessage.setValue("El correo y la contraseña no pueden estar vacíos.");
            return;
        }
        _isLoading.setValue(true);

        // --- LÓGICA DE API REAL ---
        LoginRequest request = new LoginRequest(email, password);
        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                _isLoading.setValue(false);

                if (response.isSuccessful() && response.code() == 201) {
                    LoginResponse loginData = response.body();

                    if (loginData != null && loginData.getAccessToken() != null && loginData.getAdmin() != null) {
                        Admin admin = loginData.getAdmin();
                        String token = loginData.getAccessToken();
                        String nombreCompleto = admin.getNombres() + " " + admin.getApellidos();

                        // Guardar en sesión segura
                        sessionManager.createLoginSession(token, admin.getId(), admin.getCorreo(), nombreCompleto);

                        _loginResult.setValue(true);
                    } else {
                        // Respuesta exitosa pero cuerpo vacío o inesperado
                        _errorMessage.setValue("Respuesta inesperada del servidor.");
                    }

                } else {
                    // Manejar errores HTTP según lo solicitado
                    handleApiError(response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                _isLoading.setValue(false);
                // Error 500 o de conexión
                _errorMessage.setValue("Error en el servidor, intente más tarde. (" + t.getMessage() + ")");
            }
        });
    }


    private void handleApiError(int code) {
        switch (code) {
            case 401:
                _errorMessage.setValue("Usuario o contraseña incorrectos.");
                break;
            case 404:
                _errorMessage.setValue("El usuario no existe.");
                break;
            default:
                _errorMessage.setValue("Error: " + code);
                break;
        }
    }

    // Getters públicos para observar desde la Activity
    public LiveData<Boolean> getLoginResult() { return loginResult; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
}