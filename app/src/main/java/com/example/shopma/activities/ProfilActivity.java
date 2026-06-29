package com.example.shopma.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.shopma.R;

public class ProfilActivity extends AppCompatActivity {
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        iv = findViewById(R.id.ivAvatar);
        findViewById(R.id.btnCam).setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 10);
            } else {
                ouvrirCam();
            }
        });

        findViewById(R.id.btnHist).setOnClickListener(v -> startActivity(new Intent(this, CommandesActivity.class)));
    }

    private void ouvrirCam() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 20);
    }

    @Override
    public void onRequestPermissionsResult(int rc, @NonNull String[] p, @NonNull int[] gr) {
        super.onRequestPermissionsResult(rc, p, gr);
        if (rc == 10) {
            if (gr.length > 0 && gr[0] == PackageManager.PERMISSION_GRANTED) {
                ouvrirCam();
            } else {
                Toast.makeText(this, "Permission caméra refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int rc, int res, Intent d) {
        super.onActivityResult(rc, res, d);
        if (rc == 20 && res == RESULT_OK && d != null) {
            Bitmap bmp = (Bitmap) d.getExtras().get("data");
            iv.setImageBitmap(bmp);
        }
    }
}