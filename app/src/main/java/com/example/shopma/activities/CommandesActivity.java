package com.example.shopma.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shopma.R;
import com.example.shopma.database.DatabaseHelper;

public class CommandesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue); // Réutilisation d'une structure de liste simple

        ListView lv = findViewById(R.id.lvProducts);
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor c = db.getCommandes();

        lv.setAdapter(new CursorAdapter(this, c, 0) {
            @Override
            public View newView(android.content.Context ctx, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(ctx).inflate(android.R.layout.simple_list_item_2, parent, false);
            }
            @Override
            public void bindView(View view, android.content.Context ctx, Cursor cursor) {
                TextView t1 = view.findViewById(android.R.id.text1);
                TextView t2 = view.findViewById(android.R.id.text2);
                t1.setText("Commande du : " + cursor.getString(cursor.getColumnIndexOrThrow("date")));
                t2.setText(String.format("Total : %.2f DH - %s", cursor.getDouble(cursor.getColumnIndexOrThrow("montant_total")), cursor.getString(cursor.getColumnIndexOrThrow("statut"))));
            }
        });
    }
}