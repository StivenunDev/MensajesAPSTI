package com.nickdev.mensajesapsti.ui.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nickdev.mensajesapsti.databinding.ItemArchivoAdjuntoBinding;
import java.util.ArrayList;
import java.util.List;

public class AdjuntoAdapter extends RecyclerView.Adapter<AdjuntoAdapter.AdjuntoViewHolder> {

    private final List<Uri> attachments = new ArrayList<>();
    private final OnDeleteListener deleteListener;

    public interface OnDeleteListener {
        void onDelete(int position);
    }

    public AdjuntoAdapter(OnDeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public void setAdjunto(List<Uri> newAttachments) {
        attachments.clear();
        attachments.addAll(newAttachments);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdjuntoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemArchivoAdjuntoBinding binding = ItemArchivoAdjuntoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new AdjuntoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdjuntoViewHolder holder, int position) {
        Uri uri = attachments.get(position);
        holder.bind(uri, position);
    }

    @Override
    public int getItemCount() {
        return attachments.size();
    }

    class AdjuntoViewHolder extends RecyclerView.ViewHolder {
        private final ItemArchivoAdjuntoBinding binding;

        public AdjuntoViewHolder(ItemArchivoAdjuntoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Uri uri, int position) {
            // Obtenemos el nombre del archivo (simplificado)
            String path = uri.getPath();
            String NombreFile = path != null ? path.substring(path.lastIndexOf("/") + 1) : "Archivo " + (position + 1);
            binding.tvNombreFile.setText(NombreFile);

            binding.imbEliminarFile.setOnClickListener(v -> {
                if (deleteListener != null) deleteListener.onDelete(position);
            });
        }
    }
}