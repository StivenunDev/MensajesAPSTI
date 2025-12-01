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
import com.nickdev.mensajesapsti.data.model.Mensaje;
import com.nickdev.mensajesapsti.databinding.FragmentDialogoDetalleMensajeBinding;
import com.nickdev.mensajesapsti.ui.adapter.AdjuntoLecturaAdapter;
import com.nickdev.mensajesapsti.util.DateUtils;

public class DialogoDetalleMensaje extends DialogFragment {

    private FragmentDialogoDetalleMensajeBinding binding;
    private static final String ARG_MENSAJE_OBJ = "arg_mensaje_obj"; // Nombre de argumento constante

    // Ahora recibimos el OBJETO, no un string
    public static DialogoDetalleMensaje newInstance(Mensaje mensaje) {
        DialogoDetalleMensaje fragment = new DialogoDetalleMensaje();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MENSAJE_OBJ, mensaje); // ¡Mucho más rápido!
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDialogoDetalleMensajeBinding.inflate(inflater, container, false);
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
            // Recuperamos el objeto directamente
            Mensaje mensaje = getArguments().getParcelable(ARG_MENSAJE_OBJ);
            if (mensaje != null) {
                setupUI(mensaje);
            }
        }
        binding.imgbCloseDPU.setOnClickListener(v -> dismiss());
    }

    private void setupUI(Mensaje mensaje) {
        binding.tvTituloMensaje.setText(mensaje.getTitulo());
        binding.tvContenidoMensaje.setText(mensaje.getCuerpo());

        // Usamos la utilidad de fecha
        binding.tvFechaHora.setText(DateUtils.formatearFecha(mensaje.getFechaHora()));
        binding.tvFechaHora.setVisibility(View.VISIBLE);

        int count = (mensaje.getAdjuntos() != null) ? mensaje.getAdjuntos().size() : 0;

        if (count > 0) {
            binding.rbAdjuntos.setVisibility(View.VISIBLE);
            binding.linearLayout2.setVisibility(View.VISIBLE);

            String label = count == 1 ? " Archivo Adjunto" : " Archivos Adjuntos";
            binding.tvContadorArchivos.setText(count + label);

            binding.rbAdjuntos.setLayoutManager(new LinearLayoutManager(getContext()));
            // El adaptador ya está bien, solo pasamos la lista
            AdjuntoLecturaAdapter adapter = new AdjuntoLecturaAdapter(mensaje.getAdjuntos());
            binding.rbAdjuntos.setAdapter(adapter);
        } else {
            binding.rbAdjuntos.setVisibility(View.GONE);
            binding.linearLayout2.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Usar un porcentaje seguro (90%) del ancho
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}