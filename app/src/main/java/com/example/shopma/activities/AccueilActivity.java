package com.example.shopma.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shopma.R;

public class AccueilActivity extends AppCompatActivity {

    private LinearLayout btnAll, btnElec, btnJewel, btnMen, btnWomen, btnMaps;
    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        tvWelcome = findViewById(R.id.tvWelcome);
        SharedPreferences pref = getSharedPreferences("UserSession", MODE_PRIVATE);
        String nomUtilisateur = pref.getString("USER_NAME", "Khadija");
        tvWelcome.setText("Bonjour, " + nomUtilisateur);

        btnAll = findViewById(R.id.btnAll);
        btnElec = findViewById(R.id.btnElec);
        btnJewel = findViewById(R.id.btnJewel);
        btnMen = findViewById(R.id.btnMen);
        btnWomen = findViewById(R.id.btnWomen);
        btnMaps = findViewById(R.id.btnMaps);

        btnAll.setOnClickListener(v -> {
            Intent intent = new Intent(AccueilActivity.this, CatalogueActivity.class);
            startActivity(intent);
        });

        btnElec.setOnClickListener(v -> {
            Intent intent = new Intent(AccueilActivity.this, CatalogueActivity.class);
            intent.putExtra("CATEGORY", "electronics");
            startActivity(intent);
        });

        btnJewel.setOnClickListener(v -> {
            Intent intent = new Intent(AccueilActivity.this, CatalogueActivity.class);
            intent.putExtra("CATEGORY", "jewelery");
            startActivity(intent);
        });

        btnMen.setOnClickListener(v -> {
            Intent intent = new Intent(AccueilActivity.this, CatalogueActivity.class);
            intent.putExtra("CATEGORY", "men's clothing");
            startActivity(intent);
        });

        btnWomen.setOnClickListener(v -> {
            Intent intent = new Intent(AccueilActivity.this, CatalogueActivity.class);
            intent.putExtra("CATEGORY", "women's clothing");
            startActivity(intent);
        });

        btnMaps.setOnClickListener(v -> {
            Intent intent = new Intent(AccueilActivity.this, MapsActivity.class);
            startActivity(intent);
        });
    }
}