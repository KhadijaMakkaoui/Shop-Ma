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
        String cat = getIntent().getStringExtra("CATEGORIE");

        ApiService api = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        // Safely check for "all" or null category
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
}