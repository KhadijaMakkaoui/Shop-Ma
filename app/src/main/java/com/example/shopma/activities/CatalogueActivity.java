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
        // Récupérer la catégorie envoyée par le Dashboard
        String categorieChoisie = getIntent().getStringExtra("CATEGORY");

        if (categorieChoisie != null) {
            // 1. L'utilisateur a cliqué sur une catégorie spécifique (ex: "electronics")
            // Appelez la méthode Retrofit pour charger la catégorie : /products/category/{category}
            chargerProduitsParCategorie(categorieChoisie);
        } else {
            // 2. L'utilisateur a cliqué sur "Tous les produits" (extra est null)
            // Appelez la méthode Retrofit globale : /products
            chargerTousLesProduits();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        lv = findViewById(R.id.lvProducts);
        db = new DatabaseHelper(this);
        String cat = getIntent().getStringExtra("CATEGORIE");

        ApiService api = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Product>> call = ("all".equals(cat) || cat == null) ? api.getAllProducts() : api.getProductsByCategory(cat);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    lv.setAdapter(new ProductAdapter(CatalogueActivity.this, response.body()));
                    lv.setOnItemClickListener((parent, view, position, id) -> {
                        Product p = response.body().get(position);
                        db.ajouterAuPanier(p.getId(), p.getTitle(), p.getPrice(), 1);
                        Toast.makeText(CatalogueActivity.this, "Ajouté au panier !", Toast.LENGTH_SHORT).show();
                    });
                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(CatalogueActivity.this, "Erreur Réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void chargerTousLesProduits() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        apiService.getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> produits = response.body();
                } else {
                    Toast.makeText(CatalogueActivity.this, "Erreur serveur : impossible de charger les produits.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Respect de la consigne N°7 : Gestion propre des erreurs réseau sans crash
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
                    List<Product> produits = response.body();

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