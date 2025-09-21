package com.example.clinicaoasis;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DatosMedicosActivity extends AppCompatActivity {

    private Spinner spinnerTipoSangre;
    private EditText editTextAlergias, editTextEnfermedades, editTextPeso, editTextAltura;
    private Button btnGuardarInfo;

    private String tipoSangre = "";
    private String alergias = "";
    private String enfermedades = "";
    private String peso = "";
    private String altura = "";

    // Nombre del archivo de SharedPreferences
    private static final String PREFS_NAME = "DatosMedicosPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_medicos);

        spinnerTipoSangre = findViewById(R.id.spinnerTipoSangre);
        editTextAlergias = findViewById(R.id.editTextAlergias);
        editTextEnfermedades = findViewById(R.id.editTextEnfermedades);
        editTextPeso = findViewById(R.id.editTextPeso);
        editTextAltura = findViewById(R.id.editTextAltura);
        btnGuardarInfo = findViewById(R.id.btnGuardarInfo);

        // Spinner de tipos de sangre
        String[] tiposSangre = {"Selecciona tipo de sangre", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tiposSangre);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoSangre.setAdapter(adapter);

        // Cargar datos guardados
        cargarInformacion();

        // Guardar información al presionar el botón
        btnGuardarInfo.setOnClickListener(v -> guardarInformacion());
    }

    private void guardarInformacion() {
        tipoSangre = spinnerTipoSangre.getSelectedItem().toString();
        alergias = editTextAlergias.getText().toString().trim();
        enfermedades = editTextEnfermedades.getText().toString().trim();
        peso = editTextPeso.getText().toString().trim();
        altura = editTextAltura.getText().toString().trim();

        if (tipoSangre.equals("Selecciona tipo de sangre")) {
            Toast.makeText(this, "Selecciona tu tipo de sangre", Toast.LENGTH_SHORT).show();
            return;
        }

        // Guardar en SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("tipoSangre", tipoSangre);
        editor.putString("alergias", alergias);
        editor.putString("enfermedades", enfermedades);
        editor.putString("peso", peso);
        editor.putString("altura", altura);
        editor.apply();

        Toast.makeText(this, "Información guardada", Toast.LENGTH_SHORT).show();
    }

    private void cargarInformacion() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        tipoSangre = prefs.getString("tipoSangre", "Selecciona tipo de sangre");
        alergias = prefs.getString("alergias", "");
        enfermedades = prefs.getString("enfermedades", "");
        peso = prefs.getString("peso", "");
        altura = prefs.getString("altura", "");

        // Actualizar las vistas con los valores guardados
        spinnerTipoSangre.setSelection(
                ((ArrayAdapter) spinnerTipoSangre.getAdapter()).getPosition(tipoSangre)
        );
        editTextAlergias.setText(alergias);
        editTextEnfermedades.setText(enfermedades);
        editTextPeso.setText(peso);
        editTextAltura.setText(altura);
    }

    // Métodos opcionales para obtener los datos guardados
    public String getTipoSangre() { return tipoSangre; }
    public String getAlergias() { return alergias; }
    public String getEnfermedades() { return enfermedades; }
    public String getPeso() { return peso; }
    public String getAltura() { return altura; }
}
