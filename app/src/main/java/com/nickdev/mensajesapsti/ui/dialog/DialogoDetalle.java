package com.nickdev.mensajesapsti.ui.dialog;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.nickdev.mensajesapsti.data.model.Estudiante;
import com.nickdev.mensajesapsti.databinding.FragmentDialogoDetalleBinding;

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
        binding.studentNameText.setText(student.obtenerNombreCompleto());
        binding.studentEmailText.setText(student.obtenerCorreoElectronico());
        binding.studentPhoneText.setText(student.obtenerTelefono());
        String careerAndPeriod = student.obtenerCarrera() + " - " + student.obtenerPeriodo();
        binding.studentCareerText.setText(careerAndPeriod);
    }

    private void copyStudentInfoToClipboard() {
        String info = "Nombre: " + student.obtenerNombreCompleto() + "\n" +
                "Email: " + student.obtenerCorreoElectronico() + "\n" +
                "Teléfono: " + student.obtenerTelefono() + "\n" +
                "Carrera: " + student.obtenerCarrera() + " - " + student.obtenerPeriodo();

        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("student_info", info);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "Información copiada al portapapeles", Toast.LENGTH_SHORT).show();
        }
    }
}
