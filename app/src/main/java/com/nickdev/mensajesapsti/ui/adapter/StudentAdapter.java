package com.nickdev.mensajesapsti.ui.adapter;


import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.nickdev.mensajesapsti.R;
import com.nickdev.mensajesapsti.data.model.Estudiante;
import com.nickdev.mensajesapsti.databinding.ItemEstudianteBinding;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private List<Estudiante> studentList = new ArrayList<>();
    private OnItemClickListener listener;


    public interface OnItemClickListener {
        void onItemClick(Estudiante student);

        boolean onNavigationItemSelected(@NonNull MenuItem item);
    }

    public void setStudents(List<Estudiante> students) {
        this.studentList = students;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Usamos View Binding para crear la vista del item.
        ItemEstudianteBinding binding = ItemEstudianteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StudentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Estudiante student = studentList.get(position);
        // Pasamos el estudiante Y el listener al método bind.
        holder.bind(student, listener);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    // ViewHolder class
    static class StudentViewHolder extends RecyclerView.ViewHolder {
        private final ItemEstudianteBinding binding;

        // El constructor ahora es más simple, solo configura la vista.
        public StudentViewHolder(ItemEstudianteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // El método bind ahora recibe el listener y se encarga de toda la lógica.
        public void bind(final Estudiante student, final OnItemClickListener listener) {
            binding.studentNameTextView.setText(student.obtenerNombreCompleto());

            // Tu lógica para el checkbox está muy bien.
            // Para evitar que el listener se dispare al reciclar vistas, lo ponemos a null primero.
            binding.studentCheckbox.setOnCheckedChangeListener(null);
            binding.studentCheckbox.setChecked(student.estaSeleccionado());

            // Actualiza el estado del estudiante cuando se hace clic en el checkbox
            binding.studentCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                student.establecerSeleccionado(isChecked);
            });

            // CORRECCIÓN: El listener del clic se configura aquí, donde tiene acceso a todo.
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(student);
                }
            });
        }
    }
}