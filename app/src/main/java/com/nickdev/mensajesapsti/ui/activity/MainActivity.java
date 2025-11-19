package com.nickdev.mensajesapsti.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.navigation.NavigationView;
import com.nickdev.mensajesapsti.R;
import com.nickdev.mensajesapsti.data.model.Estudiante;
import com.nickdev.mensajesapsti.data.model.api.MensajeRequest;
import com.nickdev.mensajesapsti.databinding.ActivityMainBinding;
import com.nickdev.mensajesapsti.ui.adapter.StudentAdapter;
import com.nickdev.mensajesapsti.ui.dialog.DialogPerfilUser;
import com.nickdev.mensajesapsti.ui.dialog.DialogoDetalle;
import com.nickdev.mensajesapsti.ui.dialog.DialogoEnviarMensaje;
import com.nickdev.mensajesapsti.ui.viewmodel.MainViewModel;
import com.nickdev.mensajesapsti.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        StudentAdapter.OnItemClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        DialogPerfilUser.UserProfileDialogListener,
        DialogoEnviarMensaje.SendMessageListener {

    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;
    private StudentAdapter studentAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        sessionManager = new SessionManager(this);

        setupToolbarAndDrawer();
        setupRecyclerView();
        setupListeners();
        setupObservers();
        setupBackNavigation();

        // CARGA INICIAL DE DATOS
        mainViewModel.loadStudents(this);
    }

    private void setupBackNavigation() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    private void setupToolbarAndDrawer() {
        setSupportActionBar(binding.toolbar);
        drawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        binding.menuIcon.setOnClickListener(v -> binding.drawerLayout.openDrawer(GravityCompat.START));
        binding.navView.setNavigationItemSelectedListener(this);
    }

    private void setupRecyclerView() {
        studentAdapter = new StudentAdapter();
        binding.studentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.studentsRecyclerView.setAdapter(studentAdapter);
        studentAdapter.setOnItemClickListener(this);
    }

    private void setupListeners() {
        // Perfil de usuario
        binding.profileImage.setOnClickListener(v -> {
            String adminName = sessionManager.getAdminName();
            String adminEmail = sessionManager.getAdminEmail();
            DialogPerfilUser dialog = DialogPerfilUser.newInstance(adminName, adminEmail);
            dialog.show(getSupportFragmentManager(), "UserProfileDialog");
        });

        // Checkbox "Seleccionar Todos"
        binding.chbSeleccionarT.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                mainViewModel.selectAllVisible(isChecked);
            }
        });

        // Buscador
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mainViewModel.setSearchQuery(s.toString());
                mainViewModel.applyFilters();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Botón Flotante (Abrir diálogo de envío)
        binding.messageFab.setOnClickListener(v -> {
            ArrayList<Estudiante> selectedStudents = mainViewModel.getSelectedStudents();
            if (selectedStudents.isEmpty()) {
                Toast.makeText(this, "Selecciona al menos un estudiante", Toast.LENGTH_SHORT).show();
            } else {
                DialogoEnviarMensaje dialog = DialogoEnviarMensaje.newInstance(selectedStudents);
                dialog.show(getSupportFragmentManager(), "SendMessageDialog");
            }
        });
    }

    private void setupObservers() {
        // Lista de estudiantes
        mainViewModel.getStudents().observe(this, students -> {
            if (students != null) {
                studentAdapter.setStudents(students);
            }
        });

        // Estado de carga (ProgressBar)
        mainViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                binding.loadingProgressBar.setVisibility(View.VISIBLE);
                binding.studentsRecyclerView.setVisibility(View.GONE);
            } else {
                binding.loadingProgressBar.setVisibility(View.GONE);
                binding.studentsRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        // Título de filtros
        mainViewModel.getFilterTitle().observe(this, title -> {
            binding.lblFiltroAplicado.setText(title);
        });
    }

    @Override
    public void onItemClick(Estudiante student) {
        DialogoDetalle dialog = DialogoDetalle.newInstance(student);
        dialog.show(getSupportFragmentManager(), "StudentDetailDialog");
    }

    // --- IMPLEMENTACIÓN DE FILTROS DEL MENÚ LATERAL ---
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_all) {
            mainViewModel.clearFilters();
            uncheckAllMenuItems();
            item.setChecked(true);
        } else {
            item.setChecked(!item.isChecked());
            boolean isChecked = item.isChecked();

            // Carreras (Abreviaturas BD v5)
            if (itemId == R.id.nav_career_apsti) mainViewModel.toggleCareerFilter("APSTI", isChecked);
            else if (itemId == R.id.nav_career_contabilidad) mainViewModel.toggleCareerFilter("Contab", isChecked);
            else if (itemId == R.id.nav_career_construccion) mainViewModel.toggleCareerFilter("CC", isChecked);
            else if (itemId == R.id.nav_career_mecatronica) mainViewModel.toggleCareerFilter("MA", isChecked);
            else if (itemId == R.id.nav_career_electricidad) mainViewModel.toggleCareerFilter("ElctriI", isChecked);

                // Ciclos
            else if (itemId == R.id.nav_periodo_I) mainViewModel.togglePeriodFilter("I", isChecked);
            else if (itemId == R.id.nav_periodo_II) mainViewModel.togglePeriodFilter("II", isChecked);
            else if (itemId == R.id.nav_periodo_III) mainViewModel.togglePeriodFilter("III", isChecked);
            else if (itemId == R.id.nav_periodo_IV) mainViewModel.togglePeriodFilter("IV", isChecked);
            else if (itemId == R.id.nav_periodo_V) mainViewModel.togglePeriodFilter("V", isChecked);
            else if (itemId == R.id.nav_periodo_VI) mainViewModel.togglePeriodFilter("VI", isChecked);

            if (isChecked) {
                binding.navView.getMenu().findItem(R.id.nav_all).setChecked(false);
            }
        }

        mainViewModel.applyFilters();
        return true;
    }

    private void uncheckAllMenuItems() {
        Menu menu = binding.navView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                for (int j = 0; j < item.getSubMenu().size(); j++) {
                    item.getSubMenu().getItem(j).setChecked(false);
                }
            }
        }
    }

    @Override
    public void onHistoryClicked() {
        startActivity(new Intent(this, Historial.class));
    }

    @Override
    public void onLogoutClicked() {
        sessionManager.logoutUser();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // --- IMPLEMENTACIÓN DEL LISTENER DE ENVÍO ---
    @Override
    public void onSendMessage(String titulo, String message, ArrayList<Estudiante> students, ArrayList<Uri> attachments) {
        // 1. Obtener ID del Admin
        int adminId = sessionManager.getAdminId();
        if (adminId == -1) {
            Toast.makeText(this, "Error de Sesión", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Obtener IDs de estudiantes
        ArrayList<String> studentIds = new ArrayList<>();
        for (Estudiante s : students) {
            studentIds.add(String.valueOf(s.getId_estudiante()));
        }

        // 3. Crear el borrador del mensaje (sin URLs de adjuntos aún)
        // Nota: Se pasa una lista vacía de adjuntos inicialmente
        MensajeRequest request = new MensajeRequest(
                titulo,
                message,
                adminId,
                studentIds,
                new ArrayList<>()
        );

        // 4. Llamar a la lógica completa en el ViewModel
        mainViewModel.enviarMensajeCompleto(this, request, attachments);
    }
}