package com.example.clinicaoasis;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NewUserActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword, editTextRole;
    private Button btnSave;
    private SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newuser);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRole = findViewById(R.id.editTextRole);
        btnSave = findViewById(R.id.btnSave);

        dbHelper = new SQLiteHelper(this);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String role = editTextRole.getText().toString().trim();

                if (!username.isEmpty() && !password.isEmpty() && !role.isEmpty()) {
                    long result = dbHelper.addUser(username, password, role);
                    if (result > 0) {
                        Toast.makeText(NewUserActivity.this, "Usuario guardado", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(NewUserActivity.this, "Error al guardar el usuario", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NewUserActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
