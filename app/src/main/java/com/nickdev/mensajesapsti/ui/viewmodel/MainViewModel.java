package com.nickdev.mensajesapsti.ui.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nickdev.mensajesapsti.data.model.Estudiante;
import com.nickdev.mensajesapsti.data.model.api.MensajeRequest;
import com.nickdev.mensajesapsti.data.network.ApiService;
import com.nickdev.mensajesapsti.data.network.RetrofitClient;
import com.nickdev.mensajesapsti.util.InputStreamRequestBody;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    // --- LiveData para la UI ---
    private final MutableLiveData<List<Estudiante>> students = new MutableLiveData<>();
    private final MutableLiveData<String> filterTitle = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    // --- Listas y Filtros ---
    private final List<Estudiante> allStudents = new ArrayList<>();
    private final HashSet<String> selectedCareers = new HashSet<>();
    private final HashSet<String> selectedPeriods = new HashSet<>();
    private String currentSearchQuery = "";

    // --- Getters ---
    public LiveData<List<Estudiante>> getStudents() { return students; }
    public LiveData<String> getFilterTitle() { return filterTitle; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }


    // =================================================================================
    //  1. CARGA DE ESTUDIANTES (Desde Backend)
    // =================================================================================

    public void loadStudents(Context context) {

        if (!allStudents.isEmpty()) { applyFilters(); return; }

        isLoading.setValue(true);
        // Usar el cliente adecuado según tu configuración de seguridad
        ApiService apiService = RetrofitClient.getPublicApiService();

        apiService.getEstudiantes().enqueue(new Callback<List<Estudiante>>() {
            @Override
            public void onResponse(Call<List<Estudiante>> call, Response<List<Estudiante>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    allStudents.clear();
                    allStudents.addAll(response.body());
                    applyFilters();
                } else {
                    Toast.makeText(context, "Error carga: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Estudiante>> call, Throwable t) {
                isLoading.setValue(false);
                Toast.makeText(context, "Error red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // =================================================================================
    //  2. LÓGICA DE ENVÍO DE MENSAJE (Con Adjuntos y Notificación)
    // =================================================================================

    /**
     * Método principal llamado desde la Activity.
     */
    public void enviarMensajeCompleto(Context context, MensajeRequest requestDraft, List<Uri> fileUris) {
        isLoading.setValue(true);
        List<String> uploadedUrls = new ArrayList<>();

        if (fileUris == null || fileUris.isEmpty()) {
            finalizarEnvioMensaje(context, requestDraft, uploadedUrls);
        } else {
            subirArchivoRecursivo(context, requestDraft, fileUris, 0, uploadedUrls);
        }
    }

    private void subirArchivoRecursivo(Context context, MensajeRequest requestDraft, List<Uri> uris, int index, List<String> urlsAcumuladas) {
        if (index >= uris.size()) {
            finalizarEnvioMensaje(context, requestDraft, urlsAcumuladas);
            return;
        }

        Uri fileUri = uris.get(index);
        String filename = getFileName(context, fileUri);

        InputStreamRequestBody requestFile = new InputStreamRequestBody(context.getContentResolver(), fileUri);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", filename, requestFile);

        // Usamos el servicio privado para subir archivos (requiere token si el backend lo protege)
        ApiService api = RetrofitClient.getPrivateApiService(context);

        api.uploadFile(body).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String url = response.body().get("url");
                    if (url != null) {
                        urlsAcumuladas.add(url);
                    }
                    subirArchivoRecursivo(context, requestDraft, uris, index + 1, urlsAcumuladas);
                } else {
                    isLoading.setValue(false);
                    Toast.makeText(context, "Error al subir: " + filename, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                isLoading.setValue(false);
                Toast.makeText(context, "Fallo subida: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Paso final: Reconstruye el objeto MensajeRequest con las URLs y lo envía.
     */
    private void finalizarEnvioMensaje(Context context, MensajeRequest requestDraft, List<String> attachmentUrls) {

        // 1. Recuperar la decisión de "push" original
        boolean enviarPush = false;
        if (requestDraft.getCanales() != null && requestDraft.getCanales().contains("push")) {
            enviarPush = true;
        }

        // 2. CORRECCIÓN DEL ERROR: Convertir List<Integer> -> List<String>
        // El constructor espera Strings, pero el objeto ya tiene Integers.
        List<String> idsComoString = new ArrayList<>();
        if (requestDraft.getEstudiantesIds() != null) {
            for (Integer id : requestDraft.getEstudiantesIds()) {
                idsComoString.add(String.valueOf(id));
            }
        }

        // 3. Crear el request final con la lista convertida
        MensajeRequest finalRequest = new MensajeRequest(
                requestDraft.getTitulo(),
                requestDraft.getCuerpo(),
                requestDraft.getAdminId(),
                idsComoString,  // <--- ¡AQUÍ PASAMOS LA LISTA DE STRINGS!
                attachmentUrls,
                enviarPush
        );

        // 4. Llamada a la API
        ApiService api = RetrofitClient.getPrivateApiService(context);
        api.crearMensaje(finalRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    Toast.makeText(context, "¡Mensaje enviado correctamente!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Error al crear mensaje: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                isLoading.setValue(false);
                Toast.makeText(context, "Error final: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // =================================================================================
    //  3. LÓGICA DE FILTROS Y UTILIDADES
    // =================================================================================

    private String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if(index != -1) result = cursor.getString(index);
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) result = result.substring(cut + 1);
        }
        return result;
    }

    public void toggleCareerFilter(String career, boolean isChecked) {
        if (isChecked) selectedCareers.add(career); else selectedCareers.remove(career);
    }
    public void togglePeriodFilter(String period, boolean isChecked) {
        if (isChecked) selectedPeriods.add(period); else selectedPeriods.remove(period);
    }
    public void clearFilters() {
        selectedCareers.clear();
        selectedPeriods.clear();
    }
    public void setSearchQuery(String query) {
        this.currentSearchQuery = query.toLowerCase().trim();
    }

    public void selectAllVisible(boolean select) {
        List<Estudiante> currentList = students.getValue();
        if (currentList != null) {
            for (Estudiante student : currentList) {
                student.establecerSeleccionado(select);
            }
            students.setValue(new ArrayList<>(currentList));
        }
    }

    public ArrayList<Estudiante> getSelectedStudents() {
        ArrayList<Estudiante> selected = new ArrayList<>();
        for (Estudiante student : allStudents) {
            if (student.estaSeleccionado()) {
                selected.add(student);
            }
        }
        return selected;
    }

    public void applyFilters() {
        List<Estudiante> filteredList = new ArrayList<>();
        String searchQuery = currentSearchQuery.toLowerCase();

        for (Estudiante student : allStudents) {
            boolean careerMatch = selectedCareers.isEmpty() || selectedCareers.contains(student.getCarrera());
            boolean periodMatch = selectedPeriods.isEmpty() || selectedPeriods.contains(student.getPeriodo());
            boolean searchMatch = searchQuery.isEmpty() || student.getNombreCompleto().toLowerCase().contains(searchQuery);

            if (careerMatch && periodMatch && searchMatch) {
                filteredList.add(student);
            }
        }
        students.setValue(filteredList);
        updateFilterTitle();
    }

    private void updateFilterTitle() {
        if (selectedCareers.isEmpty() && selectedPeriods.isEmpty()) {
            filterTitle.setValue("Todos");
            return;
        }
        StringJoiner titleJoiner = new StringJoiner(" | ");
        if (!selectedCareers.isEmpty()) titleJoiner.add(String.join(", ", selectedCareers));
        if (!selectedPeriods.isEmpty()) titleJoiner.add(String.join(", ", selectedPeriods));
        filterTitle.setValue(titleJoiner.toString());
    }
}