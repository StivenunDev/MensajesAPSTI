package com.nickdev.mensajesapsti.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil; // Importante
import androidx.recyclerview.widget.RecyclerView;
import com.nickdev.mensajesapsti.data.model.Mensaje;
import com.nickdev.mensajesapsti.databinding.ItemMensajeBinding;
import com.nickdev.mensajesapsti.util.DateUtils;
import java.util.ArrayList;
import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.ViewHolder> {

    private List<Mensaje> mensajes = new ArrayList<>();
    private final OnItemClickListener listener;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(Mensaje mensaje);
    }

    public HistorialAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    // --- MÉTODO PROFESIONAL PARA ACTUALIZAR LISTAS ---
    public void updateList(List<Mensaje> nuevaLista) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() { return mensajes.size(); }
            @Override
            public int getNewListSize() { return nuevaLista.size(); }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                // Comparamos IDs únicos para saber si es el mismo objeto
                return mensajes.get(oldItemPosition).getId() == nuevaLista.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                // Comparamos contenido para saber si hubo cambios visuales
                Mensaje oldMsg = mensajes.get(oldItemPosition);
                Mensaje newMsg = nuevaLista.get(newItemPosition);
                return oldMsg.getTitulo().equals(newMsg.getTitulo()) &&
                        oldMsg.getCuerpo().equals(newMsg.getCuerpo());
            }
        });

        this.mensajes.clear();
        this.mensajes.addAll(nuevaLista);
        diffResult.dispatchUpdatesTo(this); // Notifica solo lo que cambió
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ItemMensajeBinding binding = ItemMensajeBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mensaje mensaje = mensajes.get(position);

        holder.binding.tvTituloIM.setText(mensaje.getTitulo());
        holder.binding.tvFechaIM.setText(DateUtils.formatearFecha(mensaje.getFechaHora()));

        // Lógica segura de nulos
        int count = (mensaje.getAdjuntos() != null) ? mensaje.getAdjuntos().size() : 0;
        boolean hasAttachments = count > 0;

        holder.binding.llyAttachments.setVisibility(hasAttachments ? View.VISIBLE : View.GONE);
        // Usamos chequeo de nulidad seguro antes de acceder a vistas opcionales
        if (holder.binding.llyAttachmentIcons != null) {
            holder.binding.llyAttachmentIcons.setVisibility(hasAttachments ? View.VISIBLE : View.GONE);
        }

        if (hasAttachments) {
            String label = context != null
                    ? (count == 1 ? " Archivo" : " Archivos") // Podrías usar resources strings.xml
                    : " Archivos";
            holder.binding.tvAttachmentCount.setText(count + label);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(mensaje);
        });
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemMensajeBinding binding;
        public ViewHolder(ItemMensajeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}