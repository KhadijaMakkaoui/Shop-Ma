package com.example.shopma.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.shopma.R;
import com.example.shopma.adapters.CartAdapter;
import com.example.shopma.database.DatabaseHelper;

public class PanierActivity extends AppCompatActivity {
    private ListView lv;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier);

        lv = findViewById(R.id.lvCart);
        db = new DatabaseHelper(this);

        Cursor c = db.getPanierItems();
        lv.setAdapter(new CartAdapter(this, c, 0));

        findViewById(R.id.btnValider).setOnClickListener(v -> {
            if(lv.getCount() > 0) {
                db.ajouterCommande("29/06/2026", lv.getCount(), 450.0, "Confirmée");
                db.viderPanier();
                lancerNotification();
                Toast.makeText(this, "Commande passée !", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
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