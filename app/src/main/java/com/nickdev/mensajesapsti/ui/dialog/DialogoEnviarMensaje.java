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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nickdev.mensajesapsti.data.model.Estudiante;
import com.nickdev.mensajesapsti.databinding.FragmentDialogoEnviarMensajeBinding;
import com.nickdev.mensajesapsti.ui.adapter.AdjuntoAdapter;

import java.util.ArrayList;

public class DialogoEnviarMensaje extends DialogFragment {

    private FragmentDialogoEnviarMensajeBinding binding;
    private static final String ARG_SELECTED_STUDENTS = "selectedStudents";
    private SendMessageListener listener;
    private ArrayList<Estudiante> selectedStudents;

    // Lista local de URIs
    private final ArrayList<Uri> attachmentUris = new ArrayList<>();

    private  AdjuntoAdapter adjuntoAdapter;
    private ActivityResultLauncher<String> filePickerLauncher;

    public interface SendMessageListener {
        void onSendMessage(String titulo, String message, ArrayList<Estudiante> students, ArrayList<Uri> attachments);
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

        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), uris -> {
            if (uris != null && !uris.isEmpty()) {
                attachmentUris.addAll(uris);
                adjuntoAdapter.setAdjunto(attachmentUris);
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

        // Configurar RecyclerView de Adjuntos
        adjuntoAdapter = new AdjuntoAdapter(position -> {
            attachmentUris.remove(position);
            adjuntoAdapter.setAdjunto(attachmentUris);
        });
        binding.attachmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.attachmentsRecyclerView.setAdapter(adjuntoAdapter);

        binding.closeButton.setOnClickListener(v -> dismiss());

        binding.attachFilesButton.setOnClickListener(v -> {
            filePickerLauncher.launch("*/*"); // Permite cualquier archivo
        });

        binding.sendButton.setOnClickListener(v -> {
            String titulo = binding.titleEditText.getText().toString().trim();
            String message = binding.messageEditText.getText().toString().trim();

            if (titulo.isEmpty()) {
                binding.titleInputLayout.setError("Asunto requerido");
                return;
            }
            if (message.isEmpty()) {
                binding.messageInputLayout.setError("Mensaje requerido");
                return;
            }

            listener.onSendMessage(titulo, message, selectedStudents, attachmentUris);
            dismiss();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}