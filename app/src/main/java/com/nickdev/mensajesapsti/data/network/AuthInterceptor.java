package com.nickdev.mensajesapsti.data.network;
import android.content.Context;
import androidx.annotation.NonNull;

import com.nickdev.mensajesapsti.util.SessionManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor de OkHttp que añade automáticamente el token JWT
 * a todas las peticiones que usen el cliente autenticado.
 */
public class AuthInterceptor implements Interceptor {

    private SessionManager sessionManager;

    public AuthInterceptor(Context context) {
        // Obtenemos una instancia de SessionManager para leer el token
        this.sessionManager = new SessionManager(context.getApplicationContext());
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String token = sessionManager.getAuthToken();

        // Si no hay token, simplemente dejamos pasar la petición original (fallará si requiere auth)
        if (token == null) {
            return chain.proceed(originalRequest);
        }

        // Si hay token, construimos una nueva petición con el header "Authorization"
        Request newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();

        return chain.proceed(newRequest);
    }
}