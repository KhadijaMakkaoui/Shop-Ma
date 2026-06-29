package com.example.shopma.activities;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ProfilActivity extends AppCompatActivity {

    private static final int REQ_CODE_CAMERA = 101;
    private ImageView ivAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        ivAvatar = findViewById(R.id.ivAvatar);
        TextView tvNomUtilisateur = findViewById(R.id.tvNomUtilisateur);
        Button btnPrendrePhoto = findViewById(R.id.btnPrendrePhoto);
        Button btnHistorique = findViewById(R.id.btnHistorique);
        Button btnDeconnexion = findViewById(R.id.btnDeconnexion);

        // Récupérer le nom de l'utilisateur depuis SharedPreferences
        SharedPreferences prefs = getSharedPreferences("ShopMaPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", "Client ShopMa");
        tvNomUtilisateur.setText(username);

        btnPrendrePhoto.setOnClickListener(v -> verifierPermissionsEtOuvrirCamera());

        btnHistorique.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, CommandesActivity.class);
            startActivity(intent);
        });

        btnDeconnexion.setOnClickListener(v -> {
            // Effacer la mémorisation et retourner au login
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(ProfilActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void verifierPermissionsEtOuvrirCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            ouvrirCamera();
        } else {
            // Demande dynamique de la permission (Règle 5 du projet)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQ_CODE_CAMERA);
        }
    }

    private void ouvrirCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQ_CODE_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ouvrirCamera();
            } else {
                Toast.makeText(this, "Permission caméra refusée. Impossible de prendre une photo.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            ivAvatar.setImageBitmap(imageBitmap);
        }
    }
}