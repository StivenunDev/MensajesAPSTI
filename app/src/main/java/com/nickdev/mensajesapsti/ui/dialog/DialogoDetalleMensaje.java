package com.nickdev.mensajesapsti.ui.dialog;

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
import com.nickdev.mensajesapsti.ui.adapter.AdjuntoLecturaAdapter;

public class DialogoDetalleMensaje extends DialogFragment {

    private FragmentDialogoDetalleMensajeBinding binding;
    private static final String ARG_MENSAJE_JSON = "arg_mensaje_json";

    // Método estático para crear la instancia recibiendo el mensaje serializado en JSON
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

        // Configurar fondo transparente para respetar las esquinas redondeadas del diseño
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
            // Gracias a la actualización del modelo Mensaje y Adjunto,
            // Gson ahora parseará correctamente los adjuntos como objetos y no fallará.
            Mensaje mensaje = new Gson().fromJson(json, Mensaje.class);
            setupUI(mensaje);
        }

        // Configurar botón cerrar
        binding.imgbCloseDPU.setOnClickListener(v -> dismiss());
    }

    private void setupUI(Mensaje mensaje) {
        if (mensaje == null) return;

        // 1. Llenar datos de texto
        binding.tvTituloMensaje.setText(mensaje.getTitulo());
        binding.tvContenidoMensaje.setText(mensaje.getCuerpo());

        // Si tienes un TextView para la fecha en el XML (ej: tvFechaDetalle), descomenta esto:
         if (mensaje.getFechaHora() != null) {
             binding.tvFechaHora.setText(mensaje.getFechaHora());
             binding.tvFechaHora.setVisibility(View.VISIBLE);
        }else {
             binding.tvFechaHora.setVisibility(View.GONE);
         }
        // 2. CONFIGURAR ADJUNTOS
        int count = (mensaje.getAdjuntos() != null) ? mensaje.getAdjuntos().size() : 0;

        if (count > 0) {
            binding.rbAdjuntos.setVisibility(View.VISIBLE);
            binding.linearLayout2.setVisibility(View.VISIBLE); // Contenedor del contador
            binding.tvContadorArchivos.setText(count + (count == 1 ? " Archivo Adjunto" : " Archivos Adjuntos"));

            binding.rbAdjuntos.setLayoutManager(new LinearLayoutManager(getContext()));
            // Usar el adaptador corregido que ya tienes
            AdjuntoLecturaAdapter adapter = new AdjuntoLecturaAdapter(mensaje.getAdjuntos());
            binding.rbAdjuntos.setAdapter(adapter);
        } else {
            // Ocultar todo lo relacionado con adjuntos si no hay
            binding.rbAdjuntos.setVisibility(View.GONE);
            binding.linearLayout2.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Hacer que el diálogo ocupe el 90% del ancho de la pantalla
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.90),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }
}