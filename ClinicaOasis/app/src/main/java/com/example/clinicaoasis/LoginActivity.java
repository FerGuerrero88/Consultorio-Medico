package com.example.clinicaoasis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    LinearLayout userListLayout;
    ImageView imageViewSelectedProfile;
    TextView textViewSelectedName;
    EditText editTextPassword;
    CheckBox checkBoxShowPassword;
    Button btnLogin, btnRegister, btnDeleteProfile;

    List<User> userList = new ArrayList<>();
    User selectedUser;

    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", null);
        if (savedUsername != null) {
            startActivity(new Intent(LoginActivity.this, UserActivity.class)
                    .putExtra("username", savedUsername)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }

        // Inicializar vistas
        userListLayout = findViewById(R.id.userListLayout);
        imageViewSelectedProfile = findViewById(R.id.imageViewSelectedProfile);
        textViewSelectedName = findViewById(R.id.textViewSelectedName);
        editTextPassword = findViewById(R.id.editTextPassword);
        checkBoxShowPassword = findViewById(R.id.checkBoxShowPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnDeleteProfile = findViewById(R.id.btnDeleteProfile);

        // Ocultar detalles al inicio
        imageViewSelectedProfile.setVisibility(View.GONE);
        textViewSelectedName.setVisibility(View.GONE);
        editTextPassword.setVisibility(View.GONE);
        checkBoxShowPassword.setVisibility(View.GONE);
        btnLogin.setVisibility(View.GONE);
        btnDeleteProfile.setVisibility(View.GONE);

        loadUsers();

        checkBoxShowPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        btnLogin.setOnClickListener(v -> {
            if (selectedUser == null) {
                Toast.makeText(this, "Selecciona un usuario", Toast.LENGTH_SHORT).show();
                return;
            }

            String password = editTextPassword.getText().toString();
            if (password.equals(selectedUser.password)) {
                sharedPreferences.edit().putString("username", selectedUser.username).apply();
                startActivity(new Intent(LoginActivity.this, UserActivity.class)
                        .putExtra("username", selectedUser.username)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            } else {
                Toast.makeText(this, " Contraseña incorrecta", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });

        btnDeleteProfile.setOnClickListener(v -> {
            if (selectedUser != null) {
                SQLiteHelper dbHelper = new SQLiteHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("users", "id = ?", new String[]{String.valueOf(selectedUser.id)});
                db.close();

                selectedUser = null;
                editTextPassword.setText("");
                hideSelectedUser();
                loadUsers();
            }
        });
    }

    private void loadUsers() {
        SQLiteHelper dbHelper = new SQLiteHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM users", null);
        userList.clear();

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.id = cursor.getInt(0);
                user.username = cursor.getString(1);
                user.password = cursor.getString(2);
                user.imageName = cursor.getString(3);
                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        userListLayout.removeAllViews();

        if (userList.isEmpty()) {
            Toast.makeText(this, " No hay usuarios registrados", Toast.LENGTH_SHORT).show();
            return;
        }

        for (User user : userList) {
            LinearLayout profileLayout = new LinearLayout(this);
            profileLayout.setOrientation(LinearLayout.VERTICAL);
            profileLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(16, 0, 16, 0);
            profileLayout.setLayoutParams(layoutParams);

            ImageView profileImage = new ImageView(this);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(180, 180);
            profileImage.setLayoutParams(imageParams);
            profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

// Verificamos si la imagen está presente
            if (user.imageName != null && !user.imageName.isEmpty()) {
                int imageResId = getResources().getIdentifier(user.imageName, "drawable", getPackageName());
                if (imageResId != 0) {
                    profileImage.setImageResource(imageResId);
                } else {
                    profileImage.setImageResource(R.drawable.perfil_default); // Por si el nombre no coincide
                }
            } else {
                profileImage.setImageResource(R.drawable.perfil_default);
            }


            TextView usernameText = new TextView(this);
            usernameText.setText(user.username);
            usernameText.setTextColor(getResources().getColor(android.R.color.black));
            usernameText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            usernameText.setPadding(0, 10, 0, 0);

            profileLayout.addView(profileImage);
            profileLayout.addView(usernameText);

            profileLayout.setOnClickListener(v -> {
                if (selectedUser != null && selectedUser.id == user.id) {
                    hideSelectedUser();
                } else {
                    selectedUser = user;
                    showSelectedUser();
                }
            });

            profileLayout.setOnLongClickListener(v -> {
                if (selectedUser != null && selectedUser.id == user.id) {
                    btnDeleteProfile.setVisibility(View.VISIBLE);
                }
                return true;
            });

            userListLayout.addView(profileLayout);
        }
    }

    private void showSelectedUser() {
        if (selectedUser.imageName != null && !selectedUser.imageName.isEmpty()) {
            int imageResId = getResources().getIdentifier(selectedUser.imageName, "drawable", getPackageName());
            if (imageResId != 0) {
                imageViewSelectedProfile.setImageResource(imageResId);
            } else {
                imageViewSelectedProfile.setImageResource(R.drawable.perfil_default); // Por si el nombre no coincide
            }
        } else {
            imageViewSelectedProfile.setImageResource(R.drawable.perfil_default);
        }



        textViewSelectedName.setText(selectedUser.username);

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(400);

        imageViewSelectedProfile.setVisibility(View.VISIBLE);
        textViewSelectedName.setVisibility(View.VISIBLE);
        editTextPassword.setVisibility(View.VISIBLE);
        checkBoxShowPassword.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.VISIBLE);

        imageViewSelectedProfile.startAnimation(fadeIn);
        textViewSelectedName.startAnimation(fadeIn);
        editTextPassword.startAnimation(fadeIn);
        checkBoxShowPassword.startAnimation(fadeIn);
        btnLogin.startAnimation(fadeIn);

        btnDeleteProfile.setVisibility(View.GONE);
    }

    private void hideSelectedUser() {
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(400);

        imageViewSelectedProfile.startAnimation(fadeOut);
        textViewSelectedName.startAnimation(fadeOut);
        editTextPassword.startAnimation(fadeOut);
        checkBoxShowPassword.startAnimation(fadeOut);
        btnLogin.startAnimation(fadeOut);
        btnDeleteProfile.startAnimation(fadeOut);

        fadeOut.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {}

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                imageViewSelectedProfile.setVisibility(View.GONE);
                textViewSelectedName.setVisibility(View.GONE);
                editTextPassword.setVisibility(View.GONE);
                checkBoxShowPassword.setVisibility(View.GONE);
                btnLogin.setVisibility(View.GONE);
                btnDeleteProfile.setVisibility(View.GONE);
                editTextPassword.setText("");
                selectedUser = null;
            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {}
        });
    }

    static class User {
        int id;
        String username;
        String password;
        String imageName;

    }
}