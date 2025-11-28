package com.nickdev.mensajesapsti.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nickdev.mensajesapsti.data.model.Mensaje;
import com.nickdev.mensajesapsti.databinding.ItemMensajeBinding;
import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.ViewHolder> {

    private List<Mensaje> mensajes;
    private Context context;
    private final OnItemClickListener listener; // 1. Variable para el listener

    // 2. Interfaz para comunicar el clic a la Actividad
    public interface OnItemClickListener {
        void onItemClick(Mensaje mensaje);
    }

    // 3. Constructor actualizado: Ahora pide la lista Y el listener
    public HistorialAdapter(List<Mensaje> mensajes, OnItemClickListener listener) {
        this.mensajes = mensajes;
        this.listener = listener;
    }

    public void updateList(List<Mensaje> nuevaLista) {
        this.mensajes = nuevaLista;
        notifyDataSetChanged();
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
        holder.binding.tvFechaIM.setText(mensaje.getFechaHora());

        int count = mensaje.getConteoAdjunto();
        if (count > 0) {
            holder.binding.llyAttachments.setVisibility(View.VISIBLE);
            holder.binding.tvAttachmentCount.setText(count + (count == 1 ? " Archivo" : " Archivos"));
        } else {
            holder.binding.llyAttachments.setVisibility(View.GONE);
        }

        // Ocultar checkbox ya que es solo historial
        holder.binding.cbIM.setVisibility(View.GONE);

        // 4. Configurar el clic
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(mensaje);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mensajes != null ? mensajes.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemMensajeBinding binding;
        public ViewHolder(ItemMensajeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}