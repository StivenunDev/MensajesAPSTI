package com.nickdev.mensajesapsti.ui.dialog;

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
import com.nickdev.mensajesapsti.databinding.FragmentDialogoEnviarMensajeBinding;

import java.util.ArrayList;

public class DialogoEnviarMensaje extends DialogFragment {

    private FragmentDialogoEnviarMensajeBinding binding;
    private static final String ARG_SELECTED_STUDENTS = "selectedStudents";
    private SendMessageListener listener;
    private ArrayList<Estudiante> selectedStudents;

    public interface SendMessageListener {
        void onSendMessage(String message, boolean sendSms, boolean sendEmail, ArrayList<Estudiante> students);
    }

    public static DialogoEnviarMensaje newInstance(ArrayList<Estudiante> selectedStudents) {
        DialogoEnviarMensaje fragment = new DialogoEnviarMensaje();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_SELECTED_STUDENTS, selectedStudents);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SendMessageListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement SendMessageListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedStudents = getArguments().getParcelableArrayList(ARG_SELECTED_STUDENTS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDialogoEnviarMensajeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.closeButton.setOnClickListener(v -> dismiss());

        binding.sendButton.setOnClickListener(v -> {
            String message = binding.messageEditText.getText().toString().trim();
            boolean sendSms = binding.smsSwitch.isChecked();
            boolean sendEmail = binding.emailSwitch.isChecked();

            if (message.isEmpty()) {
                Toast.makeText(getContext(), "El mensaje no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!sendSms && !sendEmail) {
                Toast.makeText(getContext(), "Selecciona al menos un medio de envío", Toast.LENGTH_SHORT).show();
                return;
            }

            listener.onSendMessage(message, sendSms, sendEmail, selectedStudents);
            dismiss();
        });

        // Lógica para adjuntar archivos (simplificada por ahora)
        binding.attachFilesButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Función para adjuntar archivos no implementada aún.", Toast.LENGTH_SHORT).show();
        });
    }
}
