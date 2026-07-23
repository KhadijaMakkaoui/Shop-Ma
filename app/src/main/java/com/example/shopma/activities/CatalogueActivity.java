package com.example.shopma.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

    private ListView lvProducts;
    private EditText etSearchCategory;
    private Button btnSearch;
    private TextView tvResultTitle;
    private Button btnFilterElec, btnFilterJewel, btnFilterMen, btnFilterWomen;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        // 1. Initialisation des composants UI
        lvProducts = findViewById(R.id.lvProducts);
        etSearchCategory = findViewById(R.id.etSearchCategory);
        btnSearch = findViewById(R.id.btnSearch);
        tvResultTitle = findViewById(R.id.tvResultTitle);

        btnFilterElec = findViewById(R.id.btnFilterElec);
        btnFilterJewel = findViewById(R.id.btnFilterJewel);
        btnFilterMen = findViewById(R.id.btnFilterMen);
        btnFilterWomen = findViewById(R.id.btnFilterWomen);

        db = new DatabaseHelper(this);

        // 2. Gestion de l'Intent venant de AccueilActivity
        String categorieChoisie = getIntent().getStringExtra("CATEGORY");
        if (categorieChoisie == null) {
            categorieChoisie = getIntent().getStringExtra("CATEGORIE");
        }

        if (categorieChoisie != null && !categorieChoisie.isEmpty() && !"all".equalsIgnoreCase(categorieChoisie)) {
            effectuerRecherche(categorieChoisie);
        } else {
            if (tvResultTitle != null) {
                tvResultTitle.setText("Tous les produits :");
            }
            chargerTousLesProduits();
        }

        // 3. Listener sur le bouton OK (Recherche manuelle)
        if (btnSearch != null) {
            btnSearch.setOnClickListener(v -> {
                String req = etSearchCategory.getText().toString().trim();
                if (req.isEmpty() || "all".equalsIgnoreCase(req)) {
                    if (tvResultTitle != null) tvResultTitle.setText("Tous les produits :");
                    chargerTousLesProduits();
                } else {
                    effectuerRecherche(req);
                }
            });
        }

        // 4. Listeners sur les boutons de filtres rapides (Chips)
        if (btnFilterElec != null) {
            btnFilterElec.setOnClickListener(v -> effectuerRecherche("electronics"));
        }
        if (btnFilterJewel != null) {
            btnFilterJewel.setOnClickListener(v -> effectuerRecherche("jewelery"));
        }
        if (btnFilterMen != null) {
            btnFilterMen.setOnClickListener(v -> effectuerRecherche("men's clothing"));
        }
        if (btnFilterWomen != null) {
            btnFilterWomen.setOnClickListener(v -> effectuerRecherche("women's clothing"));
        }
    }

    /**
     * Effectue la recherche par catégorie et met à jour les champs de texte
     */
    private void effectuerRecherche(String categorie) {
        if (etSearchCategory != null) {
            etSearchCategory.setText(categorie);
        }
        if (tvResultTitle != null) {
            tvResultTitle.setText("Résultats pour \"" + categorie + "\" :");
        }
        chargerProduitsParCategorie(categorie);
    }

    /**
     * Affiche la liste des produits et configure le clic pour l'ajout au panier
     */
    private void afficherProduits(List<Product> produits) {
        ProductAdapter adapter = new ProductAdapter(CatalogueActivity.this, produits);
        lvProducts.setAdapter(adapter);

        lvProducts.setOnItemClickListener((parent, view, position, id) -> {
            Product p = produits.get(position);
            // p.getPrice() contient maintenant le vrai prix en DH (ex: 569.90)
            db.ajouterAuPanier(p.getId(), p.getTitle(), p.getPrice(), 1);
            Toast.makeText(CatalogueActivity.this, p.getTitle() + " ajouté au panier !", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Appel API Retrofit : Charger tous les produits
     */
    private void chargerTousLesProduits() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        apiService.getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> produits = response.body();

                    // 🟢 Conversion USD -> DH pour tous les produits
                    for (Product p : produits) {
                        p.setPrice(p.getPrice() * 10.0);
                    }

                    afficherProduits(produits);
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

    /**
     * Appel API Retrofit : Charger les produits par catégorie
     */
    private void chargerProduitsParCategorie(String categorie) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        apiService.getProductsByCategory(categorie).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> produits = response.body();

                    // 🟢 Conversion USD -> DH pour les produits filtrés
                    for (Product p : produits) {
                        p.setPrice(p.getPrice() * 10.0);
                    }

                    afficherProduits(produits);
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