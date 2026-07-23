package com.example.shopma.activities;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shopma.R;
import com.example.shopma.adapters.ProductAdapter;
import com.example.shopma.database.DatabaseHelper;
import com.example.shopma.models.Product;
import com.example.shopma.network.ApiService;
import com.example.shopma.network.RetrofitClient;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogueActivity extends AppCompatActivity {
    private ListView lv;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        lv = findViewById(R.id.lvProducts);
        db = new DatabaseHelper(this);

        // 1. Récupération flexible de la catégorie (gère "CATEGORY" ou "CATEGORIE")
        String categorieChoisie = getIntent().getStringExtra("CATEGORY");
        if (categorieChoisie == null) {
            categorieChoisie = getIntent().getStringExtra("CATEGORIE");
        }

        // 2. Aiguillage vers la bonne méthode
        if (categorieChoisie != null && !categorieChoisie.isEmpty() && !"all".equalsIgnoreCase(categorieChoisie)) {
            chargerProduitsParCategorie(categorieChoisie);
        } else {
            chargerTousLesProduits();
        }
    }

    // Méthode utilitaire pour lier les produits au ListView et gérer les clics
    private void afficherProduits(List<Product> produits) {
        ProductAdapter adapter = new ProductAdapter(CatalogueActivity.this, produits);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            Product p = produits.get(position);
            db.ajouterAuPanier(p.getId(), p.getTitle(), p.getPrice(), 1);
            Toast.makeText(CatalogueActivity.this, "Ajouté au panier !", Toast.LENGTH_SHORT).show();
        });
    }

    private void chargerTousLesProduits() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        apiService.getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    afficherProduits(response.body());
                } else {
                    Toast.makeText(CatalogueActivity.this, "Erreur serveur : impossible de charger les produits.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(CatalogueActivity.this, "Connexion impossible. Vérifiez votre réseau.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void chargerProduitsParCategorie(String categorie) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        apiService.getProductsByCategory(categorie).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    afficherProduits(response.body());
                } else {
                    Toast.makeText(CatalogueActivity.this, "Erreur lors du filtrage de la catégorie.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(CatalogueActivity.this, "Erreur réseau : impossible de filtrer.", Toast.LENGTH_LONG).show();
            }
        });
    }
}