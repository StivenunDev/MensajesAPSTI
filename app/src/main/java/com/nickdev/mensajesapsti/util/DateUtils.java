package com.nickdev.mensajesapsti.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static String formatearFecha(String fechaIso) {
        if (fechaIso == null || fechaIso.isEmpty()) {
            return "Fecha desconocida";
        }

        // Formato que envía NestJS (ISO 8601 UTC)
        // Ejemplo: 2025-11-30T15:00:00.000Z
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Importante: El server envía en UTC

        // Formato legible para el usuario
        // Ejemplo: 30 Nov 2025, 10:00 am
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());

        try {
            Date date = inputFormat.parse(fechaIso);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return fechaIso; // Si falla, mostramos el texto original
        }
    }
}