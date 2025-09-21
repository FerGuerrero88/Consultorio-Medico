package com.example.clinicaoasis;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView logoImage;
    private TextView welcomeText;
    private Button btnWelcome;

    // SharedPreferences para mantener la sesión
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoImage = findViewById(R.id.logoApp);
        welcomeText = findViewById(R.id.welcomeText);
        btnWelcome = findViewById(R.id.btnWelcome);

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        // Animaciones al iniciar
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logoImage.startAnimation(fadeIn);
        welcomeText.startAnimation(fadeIn);
        btnWelcome.startAnimation(fadeIn);

        // Al presionar el botón, verificar si hay sesión activa
        btnWelcome.setOnClickListener(v -> {
            // Verificar si ya hay sesión guardada
            String savedUsername = sharedPreferences.getString("username", null);

            // Si hay sesión activa, redirigir a UserActivity
            if (savedUsername != null) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                intent.putExtra("username", savedUsername);
                startActivity(intent);
                finish();
            } else {
                // Si no hay sesión activa, redirigir al login
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // Animación al cerrar actividad
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
