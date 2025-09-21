package com.example.clinicaoasis;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class AgendarCitaActivity extends AppCompatActivity {

    Spinner spinnerDoctor, spinnerConsultorio, spinnerHorario;
    Button btnConfirmarCita;
    TextView txtResumenCita, txtDiaSeleccionado;
    CheckBox checkboxNotificarCorreo;
    EditText editTextCorreo;

    Map<String, String[]> horariosPorTurno = new HashMap<>();
    String diaSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_cita);

        // Referencias a elementos del layout
        spinnerDoctor = findViewById(R.id.spinnerDoctor);
        spinnerConsultorio = findViewById(R.id.spinnerConsultorio);
        spinnerHorario = findViewById(R.id.spinnerHorario);
        btnConfirmarCita = findViewById(R.id.btnConfirmarCita);
        txtResumenCita = findViewById(R.id.txtResumenCita);
        txtDiaSeleccionado = findViewById(R.id.txtDiaSeleccionado);
        checkboxNotificarCorreo = findViewById(R.id.checkboxNotificarCorreo);
        editTextCorreo = findViewById(R.id.editTextCorreo);

        // Recibir fecha seleccionada desde Intent
        diaSeleccionado = getIntent().getStringExtra("fechaSeleccionada");
        if (diaSeleccionado != null && !diaSeleccionado.isEmpty()) {
            txtDiaSeleccionado.setText("Día seleccionado: " + diaSeleccionado);
        }

        // Lista de doctores
        String[] doctores = {
                "Dr. Juan Pérez", "Dra. Ana López",
                "Dr. Carlos Ruiz", "Dra. Laura Gómez",
                "Dr. Miguel Torres", "Dra. Isabel Díaz"
        };
        spinnerDoctor.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, doctores));

        // Lista de consultorios
        String[] consultorios = {"Consultorio 1", "Consultorio 2", "Consultorio 3"};
        spinnerConsultorio.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, consultorios));

        // Horarios por turno
        horariosPorTurno.put("Matutino", new String[]{"08:00 - 09:00", "09:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00"});
        horariosPorTurno.put("Vespertino", new String[]{"14:00 - 15:00", "15:00 - 16:00", "16:00 - 17:00", "17:00 - 18:00"});

        // Inicialmente mostrar horarios Matutino
        spinnerHorario.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, horariosPorTurno.get("Matutino")));

        // Selección automática de consultorio según doctor y cambio de horario
        spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int consultorioIndex;
                if (position <= 1) {           // Primeros dos doctores -> Consultorio 1
                    consultorioIndex = 0;
                } else if (position <= 3) {    // Siguientes dos doctores -> Consultorio 2
                    consultorioIndex = 1;
                } else {                        // Últimos dos doctores -> Consultorio 3
                    consultorioIndex = 2;
                }

                spinnerConsultorio.setSelection(consultorioIndex);

                // Cambiar horarios según consultorio (simulación de turno)
                String turno = (consultorioIndex % 2 == 0) ? "Matutino" : "Vespertino";
                spinnerHorario.setAdapter(new ArrayAdapter<>(AgendarCitaActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        horariosPorTurno.get(turno)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Mostrar campo de correo si se marca checkbox
        checkboxNotificarCorreo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editTextCorreo.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        // Confirmar cita
        btnConfirmarCita.setOnClickListener(v -> {
            String doctor = spinnerDoctor.getSelectedItem().toString();
            String consultorio = spinnerConsultorio.getSelectedItem().toString();
            String horario = spinnerHorario.getSelectedItem().toString();

            String resumen = "Cita agendada:\nDía: " + diaSeleccionado +
                    "\nDoctor: " + doctor +
                    "\nConsultorio: " + consultorio +
                    "\nHorario: " + horario;

            // Validar correo si quiere notificación
            if (checkboxNotificarCorreo.isChecked()) {
                String correo = editTextCorreo.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                    Toast.makeText(this, "Ingresa un correo válido", Toast.LENGTH_SHORT).show();
                    return;
                }
                enviarCorreo(correo, "Confirmación de Cita",
                        resumen + "\n\nSe enviará notificación 24 horas antes de la cita.");
                resumen += "\n\nNotificación enviada a: " + correo;
            }

            txtResumenCita.setText(resumen);
        });
    }

    // Método para enviar correo usando Intent
    private void enviarCorreo(String correo, String asunto, String mensaje) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + correo));
        intent.putExtra(Intent.EXTRA_SUBJECT, asunto);
        intent.putExtra(Intent.EXTRA_TEXT, mensaje);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
