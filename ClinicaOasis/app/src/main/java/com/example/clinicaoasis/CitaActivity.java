package com.example.clinicaoasis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CitaActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Button btnAgregarCita;
    private long fechaSeleccionada;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cita);

        calendarView = findViewById(R.id.calendarView);
        btnAgregarCita = findViewById(R.id.btnAgregarCita);


        fechaSeleccionada = calendarView.getDate();


        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {

            fechaSeleccionada = new GregorianCalendar(year, month, dayOfMonth).getTimeInMillis();
        });

        btnAgregarCita.setOnClickListener(v -> {
            long hoy = System.currentTimeMillis();

            if (fechaSeleccionada <= hoy) {
                Toast.makeText(this, "No se puede agendar en el mismo día ni días anteriores", Toast.LENGTH_SHORT).show();
                return;
            }


            Date date = new Date(fechaSeleccionada);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String fechaFormateada = sdf.format(date);


            Intent intent = new Intent(CitaActivity.this, AgendarCitaActivity.class);
            intent.putExtra("fechaSeleccionada", fechaFormateada);
            startActivity(intent);
        });
    }
}
