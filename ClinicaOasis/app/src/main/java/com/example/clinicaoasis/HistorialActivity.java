package com.example.clinicaoasis;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistorialActivity extends AppCompatActivity {

    private RecyclerView recyclerViewHistorial;
    private TextView textViewEmpty;
    private Button btnBack;
    private HistorialAdapter historialAdapter;
    private ArrayList<String> historialList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        recyclerViewHistorial = findViewById(R.id.recyclerViewHistorial);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        btnBack = findViewById(R.id.btnBack);

        // Inicializar lista vacía
        historialList = new ArrayList<>();

        // Configurar RecyclerView
        recyclerViewHistorial.setLayoutManager(new LinearLayoutManager(this));
        historialAdapter = new HistorialAdapter(historialList);
        recyclerViewHistorial.setAdapter(historialAdapter);

        // Mostrar mensaje si está vacío
        updateEmptyView();

        btnBack.setOnClickListener(v -> finish());
    }

    private void updateEmptyView() {
        if (historialList.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
            recyclerViewHistorial.setVisibility(View.GONE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
            recyclerViewHistorial.setVisibility(View.VISIBLE);
        }
    }
}
