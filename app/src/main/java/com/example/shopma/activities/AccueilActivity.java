package com.example.shopma.activities;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shopma.R;

public class AccueilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        findViewById(R.id.btnAll).setOnClickListener(v -> naviguer("all"));
        findViewById(R.id.btnElec).setOnClickListener(v -> naviguer("electronics"));
        findViewById(R.id.btnJewel).setOnClickListener(v -> naviguer("jewelery"));
        findViewById(R.id.btnMen).setOnClickListener(v -> naviguer("men's clothing"));
        findViewById(R.id.btnWomen).setOnClickListener(v -> naviguer("women's clothing"));
    }

    private void naviguer(String cat) {
        Intent i = new Intent(this, CatalogueActivity.class);
        i.putExtra("CATEGORIE", cat);
        startActivity(i);
    }
}