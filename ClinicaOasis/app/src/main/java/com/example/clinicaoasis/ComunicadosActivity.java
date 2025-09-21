package com.example.clinicaoasis;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class ComunicadosActivity extends AppCompatActivity {

    private RecyclerView recyclerComunicados;
    private Button btnBack;
    private ArrayList<String> comunicadosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicados);

        recyclerComunicados = findViewById(R.id.recyclerComunicados);
        btnBack = findViewById(R.id.btnBack);

        comunicadosList = new ArrayList<>();
        // Comunicados iniciales
        comunicadosList.add("Cierre temporal del consultorio: 25/09/2025");
        comunicadosList.add("La app sigue en desarrollo, se agregarán nuevas funciones próximamente.");

        recyclerComunicados.setLayoutManager(new LinearLayoutManager(this));
        recyclerComunicados.setAdapter(new ComunicadosAdapter(comunicadosList));

        btnBack.setOnClickListener(v -> finish());
    }


    private class ComunicadosAdapter extends RecyclerView.Adapter<ComunicadosAdapter.ViewHolder> {

        private ArrayList<String> comunicados;

        public ComunicadosAdapter(ArrayList<String> comunicados) {
            this.comunicados = comunicados;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_comunicado, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textComunicado.setText(comunicados.get(position));
        }

        @Override
        public int getItemCount() {
            return comunicados.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textComunicado;
            MaterialCardView cardView;

            public ViewHolder(View itemView) {
                super(itemView);
                textComunicado = itemView.findViewById(R.id.textComunicado);
                cardView = itemView.findViewById(R.id.cardComunicado);
            }
        }
    }
}
