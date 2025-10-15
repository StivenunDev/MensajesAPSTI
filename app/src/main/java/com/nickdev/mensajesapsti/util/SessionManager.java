package com.nickdev.mensajesapsti.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "AppSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;


    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void createLoginSession() {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.commit();
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}