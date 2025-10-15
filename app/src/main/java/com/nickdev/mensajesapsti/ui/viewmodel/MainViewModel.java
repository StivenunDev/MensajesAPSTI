package com.nickdev.mensajesapsti.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nickdev.mensajesapsti.data.model.Estudiante;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringJoiner;

public class MainViewModel extends ViewModel {

    // LiveData para la lista de estudiantes a mostrar en la UI
    private final MutableLiveData<List<Estudiante>> students = new MutableLiveData<>();
    // LiveData para el título que muestra los filtros aplicados
    private final MutableLiveData<String> filterTitle = new MutableLiveData<>();

    // Lista interna que guarda TODOS los estudiantes, nunca cambia.
    private final List<Estudiante> allStudents = new ArrayList<>();

    // Sets para guardar los filtros seleccionados por el usuario
    private final HashSet<String> selectedCareers = new HashSet<>();
    private final HashSet<String> selectedPeriods = new HashSet<>();
    private String currentSearchQuery = "";

    // Métodos públicos para que la Activity pueda observar los datos
    public LiveData<List<Estudiante>> getStudents() {
        return students;
    }
    public LiveData<String> getFilterTitle() {
        return filterTitle;
    }

    // Carga la lista de estudiantes si aún no ha sido cargada
    public void loadStudents() {
        if (allStudents.isEmpty()) {
            allStudents.add(new Estudiante("Sonia Urquiza Flores", "sonia@test.com", "987654321", "APSTI", "VI"));
            allStudents.add(new Estudiante("Carlos Figueroa Rojas", "carlos@test.com", "987654322", "Contabilidad", "IV"));
            allStudents.add(new Estudiante("Maria Delgado Solis", "maria@test.com", "987654323", "Construcción Civil", "V"));
            allStudents.add(new Estudiante("Juan Pérez García", "juan@test.com", "987654324", "Mecatrónica", "VI"));
            allStudents.add(new Estudiante("Ana Torres Mendoza", "ana@test.com", "987654325", "APSTI", "V"));
            allStudents.add(new Estudiante("Luis Chavez Costa", "luis@test.com", "987654326", "Electricidad", "III"));
            allStudents.add(new Estudiante("Sofia Ramirez Luna", "sofia@test.com", "987654327", "APSTI", "VI"));
        }
        // Aplica los filtros (al inicio, ninguno) para mostrar la lista completa
        applyFilters();
    }

    // Métodos para añadir o quitar filtros
    public void toggleCareerFilter(String career, boolean isChecked) {
        if (isChecked) selectedCareers.add(career); else selectedCareers.remove(career);
    }

    public void togglePeriodFilter(String period, boolean isChecked) {
        if (isChecked) selectedPeriods.add(period); else selectedPeriods.remove(period);
    }

    // Limpia todos los filtros de carrera y período
    public void clearFilters() {
        selectedCareers.clear();
        selectedPeriods.clear();
    }

    public void setSearchQuery(String query) {
        this.currentSearchQuery = query.toLowerCase().trim();
    }

    // Selecciona o deselecciona todos los estudiantes que están visibles en ese momento
    public void selectAllVisible(boolean select) {
        List<Estudiante> currentList = students.getValue();
        if (currentList != null) {
            for (Estudiante student : currentList) {
                student.SelectEstudiante(select);
            }
            // Notificamos a la UI que los datos han cambiado para que los checkboxes se actualicen
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


    // El método central que aplica toda la lógica de filtrado
    public void applyFilters() {
        List<Estudiante> filteredList = new ArrayList<>();
        String searchQuery = currentSearchQuery.toLowerCase();

        // Usamos un bucle 'for' que es 100% compatible y robusto
        for (Estudiante student : allStudents) {
            // Un estudiante pasa el filtro si cumple TODAS las condiciones
            boolean careerMatch = selectedCareers.isEmpty() || selectedCareers.contains(student.getCarrera());
            boolean periodMatch = selectedPeriods.isEmpty() || selectedPeriods.contains(student.getPeriodo());
            boolean searchMatch = searchQuery.isEmpty() || student.getNombreCompleto().toLowerCase().contains(searchQuery);

            if (careerMatch && periodMatch && searchMatch) {
                filteredList.add(student);
            }
        }
        // Actualizamos el LiveData, lo que hará que la UI se redibuje
        students.setValue(filteredList);
        updateFilterTitle();
    }

    // Genera el texto del título ("Todos", "APSTI | VI", etc.)
    private void updateFilterTitle() {
        if (selectedCareers.isEmpty() && selectedPeriods.isEmpty()) {
            filterTitle.setValue("Todos");
            return;
        }

        StringJoiner titleJoiner = new StringJoiner(" | ");
        if (!selectedCareers.isEmpty()) {
            titleJoiner.add(String.join(", ", selectedCareers));
        }
        if (!selectedPeriods.isEmpty()) {
            titleJoiner.add(String.join(", ", selectedPeriods));
        }
        filterTitle.setValue(titleJoiner.toString());
    }
}
