package com.example.clinicaoasis;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.HorizontalScrollView;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword, editTextConfirmPassword, editTextAlias;
    private Button buttonAccept, buttonSelectImage;
    private CheckBox checkBoxShowPassword;
    private ImageView imageViewProfile;
    private SQLiteHelper dbHelper;

    private String selectedImageName = null; // Guardar nombre de imagen seleccionada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextAlias = findViewById(R.id.editTextAlias);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonAccept = findViewById(R.id.buttonAccept);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        checkBoxShowPassword = findViewById(R.id.checkBoxShowPassword);
        imageViewProfile = findViewById(R.id.imageProfile);
        dbHelper = new SQLiteHelper(this);

        // Referencias a los perfiles
        ImageView perfil1 = findViewById(R.id.option1);
        ImageView perfil2 = findViewById(R.id.option2);
        ImageView perfil3 = findViewById(R.id.option3);
        ImageView perfil4 = findViewById(R.id.option4);
        ImageView perfil5 = findViewById(R.id.option5);
        ImageView perfil6 = findViewById(R.id.option6);
        ImageView perfil7 = findViewById(R.id.option7);
        ImageView perfil8 = findViewById(R.id.option8);
        ImageView perfil9 = findViewById(R.id.option9);
        ImageView perfil10 = findViewById(R.id.option10);
        ImageView perfil11 = findViewById(R.id.option11);

        HorizontalScrollView scrollImageSelector = findViewById(R.id.scrollImageSelector);
        scrollImageSelector.setVisibility(View.GONE); // Oculto al iniciar

        // Mostrar/ocultar selector de imágenes
        buttonSelectImage.setOnClickListener(v -> {
            if (scrollImageSelector.getVisibility() == View.GONE) {
                scrollImageSelector.setAlpha(0f);
                scrollImageSelector.setTranslationY(50);
                scrollImageSelector.setVisibility(View.VISIBLE);
                scrollImageSelector.animate()
                        .alpha(1f)
                        .translationY(0)
                        .setDuration(300)
                        .start();
            } else {
                scrollImageSelector.animate()
                        .alpha(0f)
                        .translationY(50)
                        .setDuration(300)
                        .withEndAction(() -> scrollImageSelector.setVisibility(View.GONE))
                        .start();
            }
        });

        // Listeners para seleccionar imagen
        perfil1.setOnClickListener(view -> {
            seleccionarImagen("perfil1", R.drawable.perfil1);
            ocultarSelectorConAnimacion(scrollImageSelector);
        });
        perfil2.setOnClickListener(view -> {
            seleccionarImagen("perfil2", R.drawable.perfil2);
            ocultarSelectorConAnimacion(scrollImageSelector);
        });
        perfil3.setOnClickListener(view -> {
            seleccionarImagen("perfil3", R.drawable.perfil3);
            ocultarSelectorConAnimacion(scrollImageSelector);
        });
        perfil4.setOnClickListener(view -> {
            seleccionarImagen("perfil4", R.drawable.perfil4);
            ocultarSelectorConAnimacion(scrollImageSelector);
        });
        perfil5.setOnClickListener(view -> {
            seleccionarImagen("perfil5", R.drawable.perfil5);
            ocultarSelectorConAnimacion(scrollImageSelector);
        });
        perfil6.setOnClickListener(view -> {
            seleccionarImagen("perfil6", R.drawable.perfil6);
            ocultarSelectorConAnimacion(scrollImageSelector);
        });
        perfil7.setOnClickListener(view -> {
            seleccionarImagen("perfil7", R.drawable.perfil7);
            ocultarSelectorConAnimacion(scrollImageSelector);
        });
        perfil8.setOnClickListener(view -> {
            seleccionarImagen("perfil8", R.drawable.perfil8);
            ocultarSelectorConAnimacion(scrollImageSelector);
        });
        perfil9.setOnClickListener(view -> {
            seleccionarImagen("perfil9", R.drawable.perfil9);
            ocultarSelectorConAnimacion(scrollImageSelector);
        });
        perfil10.setOnClickListener(view -> {
            seleccionarImagen("perfil10", R.drawable.perfil10);
            ocultarSelectorConAnimacion(scrollImageSelector);
        });
        perfil11.setOnClickListener(view -> {
            seleccionarImagen("perfil11", R.drawable.perfil11);
            ocultarSelectorConAnimacion(scrollImageSelector);
        });

        // Mostrar/ocultar contraseña
        checkBoxShowPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editTextPassword.setTransformationMethod(isChecked ?
                    HideReturnsTransformationMethod.getInstance() :
                    PasswordTransformationMethod.getInstance());

            editTextConfirmPassword.setTransformationMethod(isChecked ?
                    HideReturnsTransformationMethod.getInstance() :
                    PasswordTransformationMethod.getInstance());
        });

        // Botón aceptar - guardar usuario
        buttonAccept.setOnClickListener(view -> {
            String username = editTextUsername.getText().toString().trim();
            String alias = editTextAlias.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String confirmPassword = editTextConfirmPassword.getText().toString().trim();

            // Validaciones
            if (username.isEmpty() || password.isEmpty() || alias.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, " Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar correo con estructura
            if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                Toast.makeText(this, " Ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, " Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedImageName == null) {
                Toast.makeText(this, " Debes seleccionar una imagen de perfil", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!esContrasenaSegura(password)) {
                Toast.makeText(this,
                        "La contraseña debe tener al menos 8 caracteres, " +
                                "una mayúscula, una minúscula, un número y un carácter especial",
                        Toast.LENGTH_LONG).show();
                return;
            }

            // Guardar en SQLite (luego aquí iría Firebase)
            long result = dbHelper.addUser(alias, password, selectedImageName);
            if (result != -1) {
                Toast.makeText(this, " Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                finish(); // Cierra y vuelve al login
            } else {
                Toast.makeText(this, " Error al registrar el usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean esContrasenaSegura(String password) {
        String patron = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[.@#$%^&+=!])(?=\\S+$).{8,}$";
        return password.matches(patron);
    }
    private void seleccionarImagen(String nombrePerfil, int imagenId) {
        imageViewProfile.setImageResource(imagenId);
        selectedImageName = nombrePerfil; // Guardar imagen seleccionada
    }

    private void ocultarSelectorConAnimacion(View view) {
        view.animate()
                .alpha(0f)
                .translationY(50)
                .setDuration(300)
                .withEndAction(() -> view.setVisibility(View.GONE))
                .start();
    }
}
