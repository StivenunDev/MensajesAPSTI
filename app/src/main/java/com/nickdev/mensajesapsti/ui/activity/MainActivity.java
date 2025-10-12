package com.nickdev.mensajesapsti.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.navigation.NavigationView;
import com.nickdev.mensajesapsti.R;
import com.nickdev.mensajesapsti.data.model.Estudiante;
import com.nickdev.mensajesapsti.databinding.ActivityMainBinding;
import com.nickdev.mensajesapsti.ui.adapter.StudentAdapter;
import com.nickdev.mensajesapsti.ui.dialog.DialogoDetalle;
import com.nickdev.mensajesapsti.ui.viewmodel.MainViewModel;


public class MainActivity extends AppCompatActivity implements StudentAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener{

    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;
    private StudentAdapter studentAdapter;
    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setupToolbarAndDrawer();
        setupRecyclerView();
        setupListeners();
        setupObservers();

        // Esta llamada carga la lista inicial
        mainViewModel.loadStudents();

    }
    private void setupToolbarAndDrawer() {
        setSupportActionBar(binding.toolbar);
        drawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // Le damos la acción a tu ícono de menú personalizado para abrir el drawer
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
        binding.chbSeleccionarT.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Solo actuar si el cambio es hecho por el usuario, no por el código
            if (buttonView.isPressed()) {
                mainViewModel.selectAllVisible(isChecked);
            }
        });

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
    }

    private void setupObservers() {
        mainViewModel.getStudents().observe(this, students -> {
            if (students != null) {
                studentAdapter.setStudents(students);
            }
        });

        mainViewModel.getFilterTitle().observe(this, title -> {
            binding.lblFiltroAplicado.setText(title);
        });
    }

    @Override
    public void onItemClick(Estudiante student) {
        DialogoDetalle dialog = DialogoDetalle.newInstance(student);
        dialog.show(getSupportFragmentManager(), "StudentDetailDialog");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_all) {
            mainViewModel.clearFilters();
            uncheckAllMenuItems(); // Limpiamos la selección visual de otros filtros
            item.setChecked(true); // Dejamos "Todos" seleccionado
        } else {
            binding.navView.getMenu().findItem(R.id.nav_all).setChecked(false);
            item.setChecked(!item.isChecked());
            boolean isChecked = item.isChecked();

            // Lógica de filtros
            if (itemId == R.id.nav_career_apsti) mainViewModel.toggleCareerFilter("APSTI", isChecked);
            else if (itemId == R.id.nav_career_contabilidad) mainViewModel.toggleCareerFilter("Contabilidad", isChecked);
            else if (itemId == R.id.nav_career_construccion) mainViewModel.toggleCareerFilter("Construcción Civil", isChecked);
            else if (itemId == R.id.nav_career_mecatronica) mainViewModel.toggleCareerFilter("Mecatrónica", isChecked);
            else if (itemId == R.id.nav_career_electricidad) mainViewModel.toggleCareerFilter("Electricidad", isChecked);
            else if (itemId == R.id.nav_periodo_III) mainViewModel.togglePeriodFilter("III", isChecked);
            else if (itemId == R.id.nav_periodo_IV) mainViewModel.togglePeriodFilter("IV", isChecked);
            else if (itemId == R.id.nav_periodo_V) mainViewModel.togglePeriodFilter("V", isChecked);
            else if (itemId == R.id.nav_periodo_VI) mainViewModel.togglePeriodFilter("VI", isChecked);
        }

        mainViewModel.applyFilters();
        return true; // Devuelve true para que el menú refleje la selección correctamente
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
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
