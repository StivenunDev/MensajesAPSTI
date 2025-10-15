package com.nickdev.mensajesapsti.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.nickdev.mensajesapsti.databinding.FragmentDialogPerfilUserBinding;

public class DialogPerfilUser extends DialogFragment {

    private FragmentDialogPerfilUserBinding binding;
    private UserProfileDialogListener listener;
    // Argumentos para pasar los datos del usuario
    private static final String ARG_USER_NAME = "userName";
    private static final String ARG_USER_EMAIL = "userEmail";



    // Interfaz para comunicar eventos a la MainActivity
    public interface UserProfileDialogListener {
        void onHistoryClicked();
        void onLogoutClicked();
    }
    public static DialogPerfilUser newInstance(String userName, String userEmail) {
        DialogPerfilUser fragment = new DialogPerfilUser();
        Bundle args = new Bundle();
        args.putString(ARG_USER_NAME, userName);
        args.putString(ARG_USER_EMAIL, userEmail);
        fragment.setArguments(args);
        return fragment;
    }

    // Método para adjuntar el listener (la MainActivity)
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (UserProfileDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement UserProfileDialogListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDialogPerfilUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Recuperar los datos y mostrarlos en la UI
        if (getArguments() != null) {
            binding.profileDialogName.setText(getArguments().getString(ARG_USER_NAME));
            binding.profileDialogEmail.setText(getArguments().getString(ARG_USER_EMAIL));
        }


        // Configurar listeners
        binding.closeButton.setOnClickListener(v -> dismiss());


        binding.historyButton.setOnClickListener(v -> {
            listener.onHistoryClicked();
            dismiss(); // Cierra el diálogo después de la acción
        });

        binding.logoutButton.setOnClickListener(v -> {
            listener.onLogoutClicked();
            dismiss();
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            // 1. Establecer el ancho para que ocupe casi toda la pantalla
            params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            // 2. Establecer la gravedad en la parte superior
            params.gravity = Gravity.TOP;
            // 3. Añadir un margen superior
            params.y = 150; // Ajusta este valor para el margen deseado desde la parte superior

            window.setAttributes(params);
            // Quitar el fondo por defecto para que los bordes redondeados se vean bien
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }
}
