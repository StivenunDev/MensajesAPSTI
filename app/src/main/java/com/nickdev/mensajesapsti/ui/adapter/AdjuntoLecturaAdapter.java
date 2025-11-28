package com.nickdev.mensajesapsti.ui.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nickdev.mensajesapsti.databinding.ItemArchivoAdjuntoBinding;

import java.util.List;

public class AdjuntoLecturaAdapter extends RecyclerView.Adapter<AdjuntoLecturaAdapter.ViewHolder> {

    private List<String> urls; // Usamos Strings (URLs) no URIs

    public AdjuntoLecturaAdapter(List<String> urls) {
        this.urls = urls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Reutilizamos el mismo layout item_archivo_adjunto
        ItemArchivoAdjuntoBinding binding = ItemArchivoAdjuntoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = urls.get(position);
        // Extraer nombre del archivo de la URL
        String nombre = url.substring(url.lastIndexOf("/") + 1);
        holder.binding.tvNombreFile.setText(nombre);

        // 🔥 AQUÍ ESTÁ LA CLAVE: OCULTAR LA X
        holder.binding.imbEliminarFile.setVisibility(View.GONE);

        // Opcional: Abrir archivo al hacer clic
        holder.itemView.setOnClickListener(v -> {
            // Lógica para abrir la URL en el navegador
        });
    }

    @Override
    public int getItemCount() { return urls.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemArchivoAdjuntoBinding binding;
        public ViewHolder(ItemArchivoAdjuntoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}