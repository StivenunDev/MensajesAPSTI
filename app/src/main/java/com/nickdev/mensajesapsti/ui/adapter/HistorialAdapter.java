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
    private final OnItemClickListener listener;

    // Interfaz para comunicar el clic a la Actividad
    public interface OnItemClickListener {
        void onItemClick(Mensaje mensaje);
    }

    // Constructor actualizado
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

        // 1. Asignar Título y Fecha
        holder.binding.tvTituloIM.setText(mensaje.getTitulo());
        // Asegúrate de que tu modelo Mensaje tenga este método getter, si se llama diferente (ej: getFechaCreacion), ajústalo aquí.
        // Si tu backend envía "creadoEn", es posible que necesites formatearlo o usar ese campo.
        holder.binding.tvFechaIM.setText(mensaje.getFechaHora());

        // 2. Lógica de Adjuntos Actualizada
        // Verificamos el tamaño de la lista de objetos 'Adjunto'
        int count = 0;
        if (mensaje.getAdjuntos() != null) {
            count = mensaje.getAdjuntos().size();
        }

        if (count > 0) {
            holder.binding.llyAttachments.setVisibility(View.VISIBLE);
            holder.binding.tvAttachmentCount.setText(count + (count == 1 ? " Archivo" : " Archivos"));
            holder.binding.llyAttachmentIcons.setVisibility(View.VISIBLE);
        } else {
            holder.binding.llyAttachments.setVisibility(View.GONE);
            holder.binding.llyAttachmentIcons.setVisibility(View.GONE);
        }

        // 3. Ocultar checkbox (solo lectura)
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