package com.example.shopma.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.shopma.R;

public class ProfilActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;

    private ImageView ivAvatar;
    private Button btnCam;
    private Button btnHist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // 1. Initialisation des composants UI
        ivAvatar = findViewById(R.id.ivAvatar);
        btnCam = findViewById(R.id.btnCam);
        btnHist = findViewById(R.id.btnHist);

        // 2. Gestion du clic sur le bouton Photo / Caméra
        btnCam.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Demande de permission
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_CODE
                );
            } else {
                ouvrirCamera();
            }
        });

        // 3. Navigation vers la liste des commandes
        btnHist.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, CommandesActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Ouvre l'application Appareil photo
     */
    private void ouvrirCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * Traitement de la réponse à la demande de permission
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ouvrirCamera();
            } else {
                Toast.makeText(this, "Permission caméra refusée !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Récupération et affichage de la photo prise
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            if (data.getExtras() != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                if (photo != null) {
                    ivAvatar.setImageBitmap(photo);
                }
            }
        }
    }
}