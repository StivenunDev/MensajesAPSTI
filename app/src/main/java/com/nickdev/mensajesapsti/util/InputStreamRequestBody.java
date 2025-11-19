package com.nickdev.mensajesapsti.util;

import android.content.ContentResolver;
import android.net.Uri;
import androidx.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class InputStreamRequestBody extends RequestBody {

    private final ContentResolver contentResolver;
    private final Uri uri;

    public InputStreamRequestBody(ContentResolver contentResolver, Uri uri) {
        this.contentResolver = contentResolver;
        this.uri = uri;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        String type = contentResolver.getType(uri);
        return MediaType.parse(type != null ? type : "application/octet-stream");
    }

    @Override
    public long contentLength() {
        // Intentamos obtener el tamaño, pero devolvemos -1 si no se puede (chunked transfer)
        try {
            // Nota: Para una implementación perfecta, aquí deberíamos consultar el cursor
            // OpenableColumns.SIZE, pero -1 funciona para la mayoría de backends modernos.
            return -1;
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        InputStream inputStream = contentResolver.openInputStream(uri);
        if (inputStream == null) throw new IOException("No se pudo abrir el stream del Uri");

        try (Source source = Okio.source(inputStream)) {
            sink.writeAll(source);
        }
    }
}
