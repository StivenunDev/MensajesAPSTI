package com.nickdev.mensajesapsti.data.network;

import android.content.Context;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // URL BASE DE TU API
    // ¡Importante! Usa 10.0.2.2 si corres en el emulador de Android Studio
    // Usa tu IP de red local (ej: 192.168.1.5) si usas un dispositivo físico
    private static final String BASE_URL = "http://127.0.0.1:3000";

    private static Retrofit retrofit = null;
    private static ApiService publicApiService = null;
    private static ApiService privateApiService = null;

    // Cliente OkHttp para logging (ver peticiones en Logcat)
    private static OkHttpClient buildPublicClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    // Cliente OkHttp que incluye el Interceptor de Autenticación
    private static OkHttpClient buildPrivateClient(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(context)) // Añade el token
                .addInterceptor(logging)
                .build();
    }

    // Inicializador general
    private static Retrofit getClient(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Devuelve la instancia de ApiService PÚBLICA (para Login)
     */
    public static ApiService getPublicApiService() {
        if (publicApiService == null) {
            publicApiService = getClient(buildPublicClient()).create(ApiService.class);
        }
        return publicApiService;
    }

    /**
     * Devuelve la instancia de ApiService PRIVADA (para peticiones autenticadas)
     */
    public static ApiService getPrivateApiService(Context context) {
        if (privateApiService == null) {
            privateApiService = getClient(buildPrivateClient(context)).create(ApiService.class);
        }
        return privateApiService;
    }
}