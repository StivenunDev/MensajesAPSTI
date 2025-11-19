package com.nickdev.mensajesapsti.ui.dialog;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.nickdev.mensajesapsti.data.model.Estudiante;
import com.nickdev.mensajesapsti.databinding.FragmentDialogoDetalleBinding;
import com.nickdev.mensajesapsti.R;


public class DialogoDetalle extends DialogFragment {

    private static final String ARG_STUDENT = "student";
    private FragmentDialogoDetalleBinding binding;
    private Estudiante student;

    // Método estático para crear una instancia del diálogo y pasarle el estudiante
    public static DialogoDetalle newInstance(Estudiante student) {
        DialogoDetalle fragment = new DialogoDetalle();
        Bundle args = new Bundle();
        args.putParcelable(ARG_STUDENT, student);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            student = getArguments().getParcelable(ARG_STUDENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDialogoDetalleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (student != null) {
            populateUI();
        }

        binding.closeButton.setOnClickListener(v -> dismiss());
        binding.copyButton.setOnClickListener(v -> copyStudentInfoToClipboard());
    }

    private void populateUI() {
        binding.studentNameText.setText(student.getNombreCompleto());
        binding.studentEmailText.setText(student.getCorreoElectronico());
        binding.studentPhoneText.setText(student.getTelefono());// por implementar
        String careerAndPeriod = student.getCarrera() + " - " + student.getPeriodo();
        binding.studentCareerText.setText(careerAndPeriod);
    }

    private void copyStudentInfoToClipboard() {
        String info = "Nombre: " + student.getNombreCompleto() + "\n" +
                "Email: " + student.getCorreoElectronico() + "\n" +
                "Teléfono: " + student.getTelefono() + "\n" +
                "Carrera: " + student.getCarrera() + " - " + student.getPeriodo();

        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("student_info", info);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "Información copiada al portapapeles", Toast.LENGTH_SHORT).show();
        }
    }


    // MÉTODO AÑADIDO PARA AJUSTAR EL TAMAÑO
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            // Establecer el ancho para que ocupe casi toda la pantalla
            params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
            // Quitar el fondo por defecto para que los bordes redondeados se vean bien
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }
}
