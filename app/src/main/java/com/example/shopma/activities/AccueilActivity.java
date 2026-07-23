package com.example.shopma.activities;

import android.content.Intent;
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
        btnAll = findViewById(R.id.btnAll);
        btnElec = findViewById(R.id.btnElec);
        btnJewel = findViewById(R.id.btnJewel);
        btnMen = findViewById(R.id.btnMen);
        btnWomen = findViewById(R.id.btnWomen);
        btnMaps = findViewById(R.id.btnMaps);

        // 1. "Tous les produits" -> Ouvre la nouvelle page avec la barre de recherche
        btnAll.setOnClickListener(v -> {
            Intent intent = new Intent(AccueilActivity.this, RechercheActivity.class);
            startActivity(intent);
        });

        // 2. Les catégories -> Ouvrent le Catalogue filtré
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

        // 3. Points de retrait -> Ouvre la carte
        btnMaps.setOnClickListener(v -> {
            Intent intent = new Intent(AccueilActivity.this, MapsActivity.class);
            startActivity(intent);
        });
    }
}