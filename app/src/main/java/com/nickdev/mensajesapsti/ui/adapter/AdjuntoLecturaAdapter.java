package com.nickdev.mensajesapsti.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nickdev.mensajesapsti.data.model.Adjunto; // Importamos el modelo correcto
import com.nickdev.mensajesapsti.databinding.ItemArchivoAdjuntoBinding;

import java.util.List;

public class AdjuntoLecturaAdapter extends RecyclerView.Adapter<AdjuntoLecturaAdapter.ViewHolder> {

    // 1. Usamos List<Adjunto> en lugar de List<String>
    private List<Adjunto> adjuntos;
    private Context context;

    // 2. Constructor actualizado para recibir la lista de objetos
    public AdjuntoLecturaAdapter(List<Adjunto> adjuntos) {
        this.adjuntos = adjuntos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        // Inflamos el layout usando Binding
        ItemArchivoAdjuntoBinding binding = ItemArchivoAdjuntoBinding.inflate(
                LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 3. Obtenemos el objeto Adjunto actual
        Adjunto adjunto = adjuntos.get(position);

        // Asignamos el nombre del archivo usando el ID del XML: tvNombreFile
        if (adjunto.getNombreOriginal() != null) {
            holder.binding.tvNombreFile.setText(adjunto.getNombreOriginal());
        } else {
            // Fallback si el nombre viene nulo
            holder.binding.tvNombreFile.setText("Archivo adjunto");
        }

        // 4. OCULTAMOS LA 'X' DE ELIMINAR (ID del XML: imbEliminarFile)
        holder.binding.imbEliminarFile.setVisibility(View.GONE);

        // 5. Configurar clic para abrir el archivo en el navegador
        holder.itemView.setOnClickListener(v -> {
            if (adjunto.getUrl() != null && !adjunto.getUrl().isEmpty()) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(adjunto.getUrl()));
                    // Flag recomendada cuando se lanza desde un contexto que no es una Activity directa a veces
                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(browserIntent);
                } catch (Exception e) {
                    e.printStackTrace(); // Manejar caso donde no haya navegador
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return adjuntos != null ? adjuntos.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemArchivoAdjuntoBinding binding;

        public ViewHolder(ItemArchivoAdjuntoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}