package com.example.shopma.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shopma.R;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private CheckBox chkRemember;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        chkRemember = findViewById(R.id.cbRemember);
        prefs = getSharedPreferences("ShopMaPrefs", MODE_PRIVATE);

        if(prefs.getBoolean("remember", false)) {
            startActivity(new Intent(this, AccueilActivity.class));
            finish();
        }

        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if(!email.contains("@") || pass.length() < 4) {
                Toast.makeText(this, "Identifiants invalides", Toast.LENGTH_SHORT).show();
            } else {
                if(chkRemember.isChecked()) {
                    prefs.edit().putBoolean("remember", true).putString("email", email).apply();
                }
                startActivity(new Intent(this, AccueilActivity.class));
                finish();
            }
        });
    }
}