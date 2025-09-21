package com.example.clinicaoasis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {

    private TextView textUsername;
    private ImageView imageProfile;
    private LinearLayout menuOptions;
    private Button btnLogout, btnSelectAct;
    private Button btnDatosMedicos,btnCita, btnComunicados, btnHistorial;
    private Animation fadeIn;

    // SharedPreferences para mantener la sesión
    SharedPreferences sharedPreferences;

    private SQLiteHelper dbHelper;

    @SuppressLint({"MissingInflatedId", "Range"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Inicialización de vistas
        textUsername = findViewById(R.id.textUsername);
        imageProfile = findViewById(R.id.imageProfile);
        menuOptions = findViewById(R.id.menuOptions);
        btnLogout = findViewById(R.id.btnLogout);
        btnCita = findViewById(R.id.btnCita);
        btnSelectAct = findViewById(R.id.btnSelectAct);
        btnDatosMedicos = findViewById(R.id.btnDatosMedicos);
        btnComunicados = findViewById(R.id.btnComunicados);
        btnHistorial = findViewById(R.id.btnHistorial);

        // Inicializar SQLiteHelper
        dbHelper = new SQLiteHelper(this);

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        // Verificar si hay sesión guardada
        String savedUsername = sharedPreferences.getString("username", null);
        if (savedUsername == null) {
            // Si no hay sesión guardada, redirigir a la pantalla de login
            Intent intent = new Intent(UserActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Animación fade_in
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Configuración de la imagen de perfil y nombre
        textUsername.setText("Hola, " + savedUsername);

        // Obtener el nombre de la imagen desde la base de datos sin usar Cursor
        String imageName = null;
        Cursor cursor = dbHelper.getUserByUsername(savedUsername);
        if (cursor != null && cursor.moveToFirst()) {
            // Recuperar el nombre de la imagen desde la columna 'image' de la base de datos
            imageName = cursor.getString(cursor.getColumnIndex("image"));
            cursor.close();
        }
        if (imageName != null) {
            setProfileImage(imageName);
        } else {
            setProfileImage("perfil_default"); // Imagen por defecto si no se encuentra
        }
        // Establecer la imagen de perfil desde la base de datos

        // Acción al hacer clic en la imagen de perfil
        imageProfile.setOnClickListener(v -> toggleMenuOptions());

        // Acción para cerrar sesión
        btnLogout.setOnClickListener(v -> {
            // Eliminar usuario guardado en SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("username");
            editor.apply();

            // Volver a la pantalla principal
            finish();
            startActivity(new Intent(UserActivity.this, MainActivity.class));
        });

        // Acción para mostrar las actividades al hacer clic en "Seleccionar act"
        btnSelectAct.setOnClickListener(v -> toggleActivityOptions());

        // Botones de las actividades que se muestran al seleccionar "Seleccionar act"
        btnDatosMedicos.setOnClickListener(v -> {
            startActivity(new Intent(UserActivity.this, DatosMedicosActivity.class));
        });

        btnComunicados.setOnClickListener(v -> {
            startActivity(new Intent(UserActivity.this, ComunicadosActivity.class));
        });

        btnHistorial.setOnClickListener(v -> {
            startActivity(new Intent(UserActivity.this, HistorialActivity.class));
        });

        // Funcionalidades para las opciones del menú
        findViewById(R.id.btnEditProfile).setOnClickListener(v -> {
            startActivity(new Intent(UserActivity.this, EditProfileActivity.class));
        });

        findViewById(R.id.btnAbout).setOnClickListener(v -> {
            startActivity(new Intent(UserActivity.this, AboutActivity.class));
        });
        findViewById(R.id.btnCita).setOnClickListener(v -> {
            startActivity(new Intent(UserActivity.this, CitaActivity.class));
        });
    }

    // Método para establecer la imagen del perfil
    private void setProfileImage(String imageName) {
        if (imageName != null && !imageName.isEmpty()) {
            // Buscar el recurso de la imagen en el directorio drawable
            int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if (resId != 0) {
                // Si la imagen existe, cargarla
                imageProfile.setImageResource(resId);
            } else {
                // Si no existe, cargar una imagen por defecto
                imageProfile.setImageResource(R.drawable.perfil_default);
            }
        }
    }

    // Mostrar u ocultar el menú de opciones (editar perfil, acerca de, cerrar sesión)
    private void toggleMenuOptions() {
        if (menuOptions.getVisibility() == View.GONE) {
            menuOptions.setVisibility(View.VISIBLE);
            menuOptions.startAnimation(fadeIn); // Animación de fade_in al mostrar
        } else {
            menuOptions.setVisibility(View.GONE);

        }
    }

    // Mostrar u ocultar los botones de actividades
    private void toggleActivityOptions() {
        if (btnDatosMedicos.getVisibility() == View.GONE) {
            btnDatosMedicos.setVisibility(View.VISIBLE);
            btnCita.setVisibility(View.VISIBLE);
            btnComunicados.setVisibility(View.VISIBLE);
            btnHistorial.setVisibility(View.VISIBLE);

            // Animación fade_in
            btnDatosMedicos.startAnimation(fadeIn);
            btnComunicados.startAnimation(fadeIn);
            btnDatosMedicos.startAnimation(fadeIn);
            btnHistorial.startAnimation(fadeIn);
        } else {
            btnDatosMedicos.setVisibility(View.GONE);
            btnDatosMedicos.setVisibility(View.GONE);
            btnComunicados.setVisibility(View.GONE);
            btnHistorial.setVisibility(View.GONE);

        }
    }
}
