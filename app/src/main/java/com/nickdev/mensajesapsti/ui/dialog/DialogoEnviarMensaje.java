package com.nickdev.mensajesapsti.ui.dialog;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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


    // Lista para guardar los URIs de los archivos adjuntos
    private ArrayList<Uri> attachmentUris = new ArrayList<>();

    // Launcher para el selector de archivos
    private ActivityResultLauncher<String[]> filePickerLauncher;


    public interface SendMessageListener {
        void onSendMessage(String message, boolean sendSms, boolean sendEmail, ArrayList<Estudiante> students, ArrayList<Uri> attachments);
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

        // Inicializamos el launcher que se encargará de abrir el selector de archivos
        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), uris -> {
            // Este código se ejecuta cuando el usuario selecciona los archivos
            if (uris != null && !uris.isEmpty()) {
                attachmentUris.addAll(uris);
                updateAttachmentsInfo(); // Actualiza el texto en la UI
                Toast.makeText(getContext(), uris.size() + " archivo(s) adjuntado(s).", Toast.LENGTH_SHORT).show();
            }
        });
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

        updateAttachmentsInfo(); // Inicializa el texto de adjuntos

        binding.closeButton.setOnClickListener(v -> dismiss());

        // El botón "Adjuntar Archivos" ahora lanza el selector de archivos
        binding.attachFilesButton.setOnClickListener(v -> {
            // Permite seleccionar cualquier tipo de archivo
            filePickerLauncher.launch("*/*");
        });

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

            listener.onSendMessage(message, sendSms, sendEmail, selectedStudents, attachmentUris);
            dismiss();
        });

        // Lógica para adjuntar archivos (simplificada por ahora)
        binding.attachFilesButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Función para adjuntar archivos no implementada aún.", Toast.LENGTH_SHORT).show();
        });
    }
    // Método para actualizar la UI con la cantidad de archivos adjuntos
    private void updateAttachmentsInfo() {
        int count = attachmentUris.size();
        if (count == 0) {
            binding.attachmentsInfoText.setText("Ningún archivo adjunto.");
        } else {
            binding.attachmentsInfoText.setText(count + " archivo(s) adjunto(s). Los archivos solo se enviarán por correo.");
        }
    }
}
