package com.example.shopma.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.shopma.R;

public class ProfilActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final String PREF_NAME = "ShopMaPrefs";

    private ImageView ivAvatar;
    private EditText etNom, etEmail, etAdresse;
    private Button btnSauvegarder, btnHistorique, btnDeconnexion;
    private RelativeLayout layoutPhoto;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // 1. Initialisation des composants
        ivAvatar = findViewById(R.id.ivAvatar);
        layoutPhoto = findViewById(R.id.layoutPhoto);
        etNom = findViewById(R.id.etNom);
        etEmail = findViewById(R.id.etEmail);
        etAdresse = findViewById(R.id.etAdresse);

        btnSauvegarder = findViewById(R.id.btnSauvegarder);
        btnHistorique = findViewById(R.id.btnHistorique);
        btnDeconnexion = findViewById(R.id.btnDeconnexion);

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        chargerDonneesProfil();

        layoutPhoto.setOnClickListener(v -> verifierEtOuvrirCamera());

        btnSauvegarder.setOnClickListener(v -> sauvegarderDonneesProfil());

        btnHistorique.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, CommandesActivity.class);
            startActivity(intent);
        });

        btnDeconnexion.setOnClickListener(v -> deconnecterUtilisateur());
    }

    private void chargerDonneesProfil() {
        String nom = sharedPreferences.getString("user_nom", "");
        String email = sharedPreferences.getString("user_email", "");
        String adresse = sharedPreferences.getString("user_adresse", "");

        etNom.setText(nom);
        etEmail.setText(email);
        etAdresse.setText(adresse);
    }


    private void sauvegarderDonneesProfil() {
        String nom = etNom.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String adresse = etAdresse.getText().toString().trim();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_nom", nom);
        editor.putString("user_email", email);
        editor.putString("user_adresse", adresse);
        editor.apply();

        Toast.makeText(this, "Informations sauvegardées !", Toast.LENGTH_SHORT).show();
    }

    /**
     * Vérifie la permission Caméra avant d'ouvrir
     */
    private void verifierEtOuvrirCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE
            );
        } else {
            ouvrirCamera();
        }
    }

    private void ouvrirCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

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


    private void deconnecterUtilisateur() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("remember", false);
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ProfilActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}