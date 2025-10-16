package com.nickdev.mensajesapsti.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
import com.nickdev.mensajesapsti.ui.dialog.DialogPerfilUser;
import com.nickdev.mensajesapsti.ui.dialog.DialogoDetalle;
import com.nickdev.mensajesapsti.ui.dialog.DialogoEnviarMensaje;
import com.nickdev.mensajesapsti.ui.viewmodel.MainViewModel;
import com.nickdev.mensajesapsti.util.SessionManager;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements
        StudentAdapter.OnItemClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        DialogPerfilUser.UserProfileDialogListener,
        DialogoEnviarMensaje.SendMessageListener{

    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;
    private StudentAdapter studentAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private SessionManager sessionManager; // Declarado aquí


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // CORRECCIÓN 1: Inicializamos el SessionManager aquí
        sessionManager = new SessionManager(this);

        setupToolbarAndDrawer();
        setupRecyclerView();
        setupListeners();
        setupObservers();

        mainViewModel.loadStudents();

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
        binding.profileImage.setOnClickListener(v -> {
            DialogPerfilUser dialog = DialogPerfilUser.newInstance("Sonia Delegada", "admin@test.com");
            dialog.show(getSupportFragmentManager(), "UserProfileDialog");
        });

        binding.chbSeleccionarT.setOnCheckedChangeListener((buttonView, isChecked) -> {
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
            uncheckAllMenuItems();
            item.setChecked(true);
        } else {
            binding.navView.getMenu().findItem(R.id.nav_all).setChecked(false);
            item.setChecked(!item.isChecked());
            boolean isChecked = item.isChecked();

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
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onHistoryClicked() {
        // CORRECCIÓN 2: Usamos el nombre de clase correcto "HistoryActivity"
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


    @Override
    public void onSendMessage(String message, boolean sendSms, boolean sendEmail, ArrayList<Estudiante> students, ArrayList<Uri> attachments) {
        if (sendSms) {
            sendSmsIntent(message, students);
        }
        if (sendEmail) {
            sendEmailIntent(message, students, attachments);
        }
        Toast.makeText(this, "Preparando envío de mensajes...", Toast.LENGTH_SHORT).show();
    }

    private void sendSmsIntent(String message, ArrayList<Estudiante> students) {
        StringBuilder numbers = new StringBuilder();
        for (Estudiante student : students) {
            numbers.append(student.getTelefono()).append(";");
        }
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + numbers));
        intent.putExtra("sms_body", message);
        startActivity(intent);
    }

    private void sendEmailIntent(String message, ArrayList<Estudiante> students, ArrayList<Uri> attachments) {
        String[] emails = new String[students.size()];
        for (int i = 0; i < students.size(); i++) {
            emails[i] = students.get(i).getCorreoElectronico();
        }

        Intent intent;
        if (attachments == null || attachments.isEmpty()) {
            // Si NO hay adjuntos, usamos un intent simple.
            intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        } else {
            // Si SÍ hay adjuntos, usamos un intent para múltiples archivos.
            intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("*/*"); // Permite cualquier tipo de archivo
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachments);
            // Otorgamos permiso temporal a la app de correo para leer los archivos.
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        intent.putExtra(Intent.EXTRA_EMAIL, emails);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Mensaje Institucional");
        intent.putExtra(Intent.EXTRA_TEXT, message);

        startActivity(Intent.createChooser(intent, "Enviar correo..."));
    }




}

