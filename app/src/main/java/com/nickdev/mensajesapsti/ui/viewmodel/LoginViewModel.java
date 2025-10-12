package com.nickdev.mensajesapsti.ui.viewmodel;


import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nickdev.mensajesapsti.util.SessionManager;

public class LoginViewModel extends AndroidViewModel {

    // LiveData para el resultado del login (true si fue exitoso)
    private final MutableLiveData<Boolean> _loginResult = new MutableLiveData<>();


    // LiveData para el estado de carga (true si está cargando)
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public final LiveData<Boolean> isLoading = _isLoading;

    // LiveData para los mensajes de error
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();
    public final LiveData<String> errorMessage = _errorMessage;

    private final SessionManager sessionManager;

    public LoginViewModel(Application application) {
        super(application);
        this.sessionManager = new SessionManager(application.getApplicationContext());
    }

    public LiveData<Boolean> getLoginResult() {
        return _loginResult;
    }

    public LiveData<Boolean> getIsLoading() {
        return _isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return _errorMessage;
    }



    public void login(String email, String password) {
        // 1. Validación de entradas
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            _errorMessage.setValue("El correo y la contraseña no pueden estar vacíos.");
            return;
        }

        // 2. Iniciar el estado de carga
        _isLoading.setValue(true);

        // 3. Simular una llamada a red (ej. 1.5 segundos de espera)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Lógica de autenticación (los datos correctos)
            if ("admin@test.com".equals(email) && "1234".equals(password)) {
                sessionManager.createLoginSession(); // Guardamos la sesión
                _loginResult.setValue(true); // Éxito
            } else {
                _errorMessage.setValue("Credenciales incorrectas."); // Error específico
                _loginResult.setValue(false); // Fracaso
            }
            // 4. Finalizar el estado de carga
            _isLoading.setValue(false);
        }, 1500);
    }
}