package com.example.clinicaoasis;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private SQLiteHelper dbHelper;
    private EditText edtLoginUsername, edtLoginPassword;
    private EditText edtNewPassword, edtConfirmPassword;
    private ImageView profileImageView;
    private Button btnValidate, btnSave, btnChangeImage;
    private LinearLayout imageSelectionLayout, loginLayout, updateLayout;
    private String currentProfileImage = "perfil_default";
    private String username;
    private int userId = -1;
    private CheckBox showPasswordCheckBox,showLoginPasswordCheckBox;

    @SuppressLint({"MissingInflatedId", "Range"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        dbHelper = new SQLiteHelper(this);

        // Layouts
        loginLayout = findViewById(R.id.loginLayout);
        updateLayout = findViewById(R.id.userUpdateLayout);

        // Cuando el usuario se valide correctamente:
        loginLayout.setVisibility(View.VISIBLE);
        updateLayout.setVisibility(View.GONE);

        // Inputs para login manual
        edtLoginUsername = findViewById(R.id.currentUsernameEditText);
        edtLoginPassword = findViewById(R.id.currentPasswordEditText);
        btnValidate = findViewById(R.id.validateUserButton);

        // Inputs para actualización
        edtNewPassword = findViewById(R.id.newPasswordEditText);
        edtConfirmPassword = findViewById(R.id.confirmPasswordEditText);
        profileImageView = findViewById(R.id.profileImageView);
        btnChangeImage = findViewById(R.id.changeImageButton);
        imageSelectionLayout = findViewById(R.id.imageSelectionlayout);
        btnSave = findViewById(R.id.saveButton);

        // Checkbox para mostrar u ocultar contraseñas
        showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);
        showLoginPasswordCheckBox = findViewById(R.id.showLoginPasswordCheckBox);

        // 1. Intentar recuperar desde SharedPreferences
        SharedPreferences prefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String savedUsername = prefs.getString("username", null);
        if (savedUsername != null) {
            username = savedUsername;
            if (loadUserData(username)) {
                switchToUpdateMode(); // ya lo encontró, pasamos a actualizar
            }
        }

        // Validar usuario manualmente si no funcionó el SharedPreferences
        btnValidate.setOnClickListener(v -> {
            String enteredUsername = edtLoginUsername.getText().toString().trim();
            String enteredPassword = edtLoginPassword.getText().toString().trim();

            Cursor cursor = dbHelper.getUser(enteredUsername, enteredPassword);
            if (cursor != null && cursor.moveToFirst()) {
                username = enteredUsername;
                userId = cursor.getInt(cursor.getColumnIndex("id"));
                currentProfileImage = cursor.getString(cursor.getColumnIndex("image"));
                switchToUpdateMode();
                cursor.close();
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        });

        // Mostrar / ocultar selector de imágenes
        btnChangeImage.setOnClickListener(v -> {
            int visibility = imageSelectionLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
            imageSelectionLayout.setVisibility(visibility);
        });

        // Opciones de imagen
        int[] imageIds = {
                R.id.imageOption1, R.id.imageOption2, R.id.imageOption3,
                R.id.imageOption4, R.id.imageOption5, R.id.imageOption6, R.id.imageOption7, R.id.imageOption8, R.id.imageOption9, R.id.imageOption10, R.id.imageOption11
        };
        String[] imageNames = {
                "perfil1", "perfil2", "perfil3", "perfil4", "perfil5", "perfil6", "perfil7", "perfil8", "perfil9", "perfil10", "perfil11"
        };

        for (int i = 0; i < imageIds.length; i++) {
            int index = i;
            ImageView option = findViewById(imageIds[i]);
            option.setOnClickListener(v -> {
                currentProfileImage = imageNames[index];
                profileImageView.setImageResource(getResources().getIdentifier(currentProfileImage, "drawable", getPackageName()));
                imageSelectionLayout.setVisibility(View.GONE);
            });
        }

        // Guardar cambios
        btnSave.setOnClickListener(v -> {
            String newPass = edtNewPassword.getText().toString().trim();
            String confirmPass = edtConfirmPassword.getText().toString().trim();

            if (newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Rellena ambos campos de contraseña", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            // Usar ambos métodos para más seguridad
            int passUpdatedByUsername = dbHelper.updateUserPasswordByUsername(username, newPass);
            int passUpdatedById = userId != -1 ? dbHelper.updateUserPassword(userId, newPass) : 0;

            int imgUpdatedByUsername = dbHelper.updateUserImageByUsername(username, currentProfileImage);
            int imgUpdatedById = userId != -1 ? dbHelper.updateUserImage(userId, currentProfileImage) : 0;

            if (passUpdatedByUsername > 0 || passUpdatedById > 0) {
                Toast.makeText(this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar perfil", Toast.LENGTH_SHORT).show();
            }
        });

        // Funcionalidad del CheckBox para mostrar/ocultar contraseñas
        showLoginPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Mostrar contraseñas
                edtLoginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                }else {
                // Ocultar contraseñas
                edtLoginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Mostrar contraseñas
                edtNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                edtConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                // Ocultar contraseñas
                edtNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                edtConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
    }

    @SuppressLint("Range")
    private boolean loadUserData(String username) {
        Cursor cursor = dbHelper.getUserByUsername(username);
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex("id"));
            currentProfileImage = cursor.getString(cursor.getColumnIndex("image"));
            cursor.close();
            return true;
        }
        return false;
    }

    private void switchToUpdateMode() {
        loginLayout.setVisibility(View.GONE);
        updateLayout.setVisibility(View.VISIBLE);
        profileImageView.setImageResource(getResources().getIdentifier(currentProfileImage, "drawable", getPackageName()));
    }
}
