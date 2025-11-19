package com.nickdev.mensajesapsti.util;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class SessionManager {

    private static final String PREF_NAME = "AppSecureSession";
    private static final String KEY_AUTH_TOKEN = "authToken";
    private static final String KEY_ADMIN_ID = "adminId";
    private static final String KEY_ADMIN_EMAIL = "adminEmail";
    private static final String KEY_ADMIN_NAME = "adminName"; // Para el perfil
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn"; // Mantenemos este para LauncherActivity
    private final SharedPreferences prefs;



    public SessionManager(Context context) {
        SharedPreferences sharedPreferences = null;
        try {
            // 1. Crear o obtener la Master Key
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            // 2. Inicializar EncryptedSharedPreferences
            sharedPreferences = EncryptedSharedPreferences.create(
                    PREF_NAME,
                    masterKeyAlias,
                    context.getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            // Fallback a SharedPreferences normal (NO RECOMENDADO en producción)
            // prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        this.prefs = sharedPreferences;
    }

    public void createLoginSession(String token, int adminId, String email, String nombreCompleto) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.putInt(KEY_ADMIN_ID, adminId);
        editor.putString(KEY_ADMIN_EMAIL, email);
        editor.putString(KEY_ADMIN_NAME, nombreCompleto);
        editor.apply();
    }

    public void logoutUser() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }


    public String getAuthToken() {
        return prefs.getString(KEY_AUTH_TOKEN, null);
    }

    public int getAdminId() {
        return prefs.getInt(KEY_ADMIN_ID, -1);
    }

    public String getAdminEmail() {
        return prefs.getString(KEY_ADMIN_EMAIL, null);
    }

    public String getAdminName() {
        return prefs.getString(KEY_ADMIN_NAME, "Usuario");
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}