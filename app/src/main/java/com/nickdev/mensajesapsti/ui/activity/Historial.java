package com.nickdev.mensajesapsti.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.nickdev.mensajesapsti.data.model.Mensaje;
import com.nickdev.mensajesapsti.databinding.ActivityHistorialBinding;
import com.nickdev.mensajesapsti.ui.adapter.HistorialAdapter;
import com.nickdev.mensajesapsti.ui.dialog.DialogoDetalleMensaje;
import com.nickdev.mensajesapsti.data.network.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Historial extends AppCompatActivity {

    private ActivityHistorialBinding binding;
    private HistorialAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistorialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        setupRecyclerView();
        cargarHistorial();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.historyToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Historial de Mensajes");
        }
    }

    private void setupRecyclerView() {
        // Eliminamos el 'new ArrayList<>()' del constructor porque el adaptador inicia vacío internamente
        adapter = new HistorialAdapter(this::mostrarDetalle);

        binding.rvHistorial.setLayoutManager(new LinearLayoutManager(this));
        binding.rvHistorial.setAdapter(adapter);
    }

    private void mostrarDetalle(Mensaje mensaje) {


        DialogoDetalleMensaje dialog = DialogoDetalleMensaje.newInstance(mensaje);
        dialog.show(getSupportFragmentManager(), "DetalleMensaje");
    }
    private void cargarHistorial() {
        binding.progressBar.setVisibility(View.VISIBLE);

        // Llamada a la API
        RetrofitClient.getPrivateApiService(this).obtenerMensajesEnviados().enqueue(new Callback<List<Mensaje>>() {
            @Override
            public void onResponse(Call<List<Mensaje>> call, Response<List<Mensaje>> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    // Actualizar adaptador con los datos recibidos
                    adapter.updateList(response.body());
                } else {
                    Toast.makeText(Historial.this, "Error al cargar historial", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Mensaje>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(Historial.this, "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}