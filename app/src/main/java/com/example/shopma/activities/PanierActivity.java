package com.example.shopma.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.shopma.R;
import com.example.shopma.adapters.CartAdapter;
import com.example.shopma.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PanierActivity extends AppCompatActivity {

    private ListView lvCart;
    private TextView tvTotalPanier;
    private Button btnValider;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier);

        lvCart = findViewById(R.id.lvCart);
        tvTotalPanier = findViewById(R.id.tvTotalPanier);
        btnValider = findViewById(R.id.btnValider);
        db = new DatabaseHelper(this);

        chargerPanierEtTotal();

        btnValider.setOnClickListener(v -> {
            Cursor c = db.getPanierItems();

            if (c != null && c.getCount() > 0) {
                double totalDouble = 0.0;
                int totalArticles = 0;

                if (c.moveToFirst()) {
                    int indexPrice = c.getColumnIndexOrThrow("price");
                    int indexQty = c.getColumnIndexOrThrow("quantity");

                    do {
                        double price = c.getDouble(indexPrice);
                        int qty = c.getInt(indexQty);
                        totalDouble += (price * qty);
                        totalArticles += qty;
                    } while (c.moveToNext());
                }

                String dateAujourdhui = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                // Enregistrement dans la table 'commandes' selon le schéma d'examen
                db.ajouterCommande(dateAujourdhui, totalArticles, totalDouble, "en_cours");
                db.viderPanier();

                lancerNotification();
                Toast.makeText(this, "Commande passée avec succès !", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Votre panier est vide !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void chargerPanierEtTotal() {
        Cursor c = db.getPanierItems();
        CartAdapter adapter = new CartAdapter(this, c, 0);
        lvCart.setAdapter(adapter);

        double total = 0.0;
        if (c != null && c.moveToFirst()) {
            int indexPrice = c.getColumnIndexOrThrow("price");
            int indexQty = c.getColumnIndexOrThrow("quantity");

            do {
                total += (c.getDouble(indexPrice) * c.getInt(indexQty));
            } while (c.moveToNext());
        }

        if (tvTotalPanier != null) {
            tvTotalPanier.setText(String.format(Locale.getDefault(), "%.2f DH", total));
        }
    }

    private void lancerNotification() {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(new NotificationChannel("chan", "ShopMa", NotificationManager.IMPORTANCE_DEFAULT));
        }
        NotificationCompat.Builder b = new NotificationCompat.Builder(this, "chan")
                .setSmallIcon(android.R.drawable.stat_sys_data_bluetooth)
                .setContentTitle("ShopMa")
                .setContentText("Commande validée avec succès !");
        nm.notify(1, b.build());
    }
}