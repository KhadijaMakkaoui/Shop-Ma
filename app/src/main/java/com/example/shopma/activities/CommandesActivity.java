package com.example.shopma.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shopma.R;
import com.example.shopma.adapters.CommandeAdapter;
import com.example.shopma.database.DatabaseHelper;

public class CommandesActivity extends AppCompatActivity {

    private ListView lvCommandes;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commandes);

        lvCommandes = findViewById(R.id.lvCommandes);
        db = new DatabaseHelper(this);

        // Charger et afficher la liste des commandes depuis SQLite
        Cursor cursor = db.getCommandes();
        CommandeAdapter adapter = new CommandeAdapter(this, cursor, 0);
        lvCommandes.setAdapter(adapter);
    }
}