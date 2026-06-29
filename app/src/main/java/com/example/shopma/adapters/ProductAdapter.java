package com.example.shopma.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shopma.R;
import com.example.shopma.models.Product;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {
    public ProductAdapter(Context c, List<Product> p) { super(c, 0, p); }

    @Override
    public View getView(int pos, View v, ViewGroup p) {
        if (v == null) v = LayoutInflater.from(getContext()).inflate(R.layout.item_produit, p, false);
        Product prod = getItem(pos);

        TextView title = v.findViewById(R.id.tvProductTitle);
        TextView price = v.findViewById(R.id.tvProductPrice);
        ImageView img = v.findViewById(R.id.ivProductImage);

        title.setText(prod.getTitle());
        price.setText(String.format("%.2f DH", prod.getPrice() * 10));
        Picasso.get().load(prod.getImage()).into(img);

        return v;
    }
}