package com.example.shopma.activities;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.shopma.R;
import com.example.shopma.adapters.ProductAdapter;
import com.example.shopma.database.DatabaseHelper;
import com.example.shopma.models.Product;
import com.example.shopma.network.ApiService;
import com.example.shopma.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RechercheActivity extends AppCompatActivity {

    private ListView lv;
    private SearchView searchView;
    private DatabaseHelper db;

    // Stockage de la liste complète des produits pour le filtrage
    private List<Product> listeProduitsComplete = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        lv = findViewById(R.id.lvProducts);
        searchView = findViewById(R.id.searchViewProduct);
        db = new DatabaseHelper(this);

        chargerTousLesProduits();

        setupSearchView();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrerProduits(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrerProduits(newText);
                return true;
            }
        });
    }

    private void filtrerProduits(String texte) {
        if (listeProduitsComplete == null) return;

        List<Product> listeFiltree = new ArrayList<>();
        String query = texte.toLowerCase().trim();

        for (Product p : listeProduitsComplete) {
            if (p.getTitle() != null && p.getTitle().toLowerCase().contains(query)) {
                listeFiltree.add(p);
            }
        }

        afficherProduits(listeFiltree);
    }

    private void afficherProduits(List<Product> produits) {
        ProductAdapter adapter = new ProductAdapter(
                RechercheActivity.this, produits);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            Product p = produits.get(position);
            db.ajouterAuPanier(p.getId(), p.getTitle(), p.getPrice(), 1);
            Toast.makeText(RechercheActivity.this, "Ajouté au panier avec succès !", Toast.LENGTH_SHORT).show();
        });
    }

    private void chargerTousLesProduits() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        apiService.getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listeProduitsComplete = response.body();

                    afficherProduits(listeProduitsComplete);
                } else {
                    Toast.makeText(RechercheActivity.this, "Erreur serveur : impossible de charger les produits.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(RechercheActivity.this, "Connexion impossible. Vérifiez votre réseau.", Toast.LENGTH_LONG).show();
            }
        });
    }
}