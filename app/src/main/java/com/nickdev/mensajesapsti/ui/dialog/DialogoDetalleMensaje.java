package com.nickdev.mensajesapsti.ui.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.nickdev.mensajesapsti.data.model.Mensaje;
import com.nickdev.mensajesapsti.databinding.FragmentDialogoDetalleMensajeBinding;
import com.nickdev.mensajesapsti.ui.adapter.AdjuntoLecturaAdapter; // Asegúrate de haber creado este adaptador

public class DialogoDetalleMensaje extends DialogFragment {

    private FragmentDialogoDetalleMensajeBinding binding;
    private static final String ARG_MENSAJE_JSON = "arg_mensaje_json";

    // Método estático para crear la instancia recibiendo el mensaje
    public static DialogoDetalleMensaje newInstance(String jsonMensaje) {
        DialogoDetalleMensaje fragment = new DialogoDetalleMensaje();
        Bundle args = new Bundle();
        args.putString(ARG_MENSAJE_JSON, jsonMensaje);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDialogoDetalleMensajeBinding.inflate(inflater, container, false);

        // Fondo transparente para que se vean las esquinas redondeadas
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            String json = getArguments().getString(ARG_MENSAJE_JSON);
            Mensaje mensaje = new Gson().fromJson(json, Mensaje.class);
            setupUI(mensaje);
        }

        // Botón cerrar
        binding.imgbCloseDPU.setOnClickListener(v -> dismiss());
    }

    private void setupUI(Mensaje mensaje) {
        if (mensaje == null) return;

        // 1. Llenar textos (Ids basados en tu XML)
        binding.tvTituloMensaje.setText(mensaje.getTitulo());
        binding.tvContenidoMensaje.setText(mensaje.getCuerpo());

        // Opcional: Si tu XML tiene TextView para fecha, úsalo:
        // binding.tvFechaDetalle.setText(mensaje.getFechaHora());

        // 2. Configurar lista de adjuntos
        if (mensaje.getAdjuntos() != null && !mensaje.getAdjuntos().isEmpty()) {
            binding.rbAdjuntos.setVisibility(View.VISIBLE);
            binding.rbAdjuntos.setLayoutManager(new LinearLayoutManager(getContext()));

            // Usamos el adaptador de SOLO LECTURA (sin la X de eliminar)
            AdjuntoLecturaAdapter adapter = new AdjuntoLecturaAdapter(mensaje.getAdjuntos());
            binding.rbAdjuntos.setAdapter(adapter);
        } else {
            binding.rbAdjuntos.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Hacer que el diálogo ocupe el ancho casi completo (opcional)
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.90),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }
}