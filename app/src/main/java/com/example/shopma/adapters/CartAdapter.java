package com.example.shopma.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.shopma.R;

import java.util.Locale;

public class CartAdapter extends CursorAdapter {

    public CartAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_panier, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvTitle = view.findViewById(R.id.tvCartItemTitle);
        TextView tvQty = view.findViewById(R.id.tvCartItemQty);
        TextView tvPrice = view.findViewById(R.id.tvCartItemPrice);

        // Récupération selon les noms stricts de la table 'panier'
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
        int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));

        tvTitle.setText(title);
        tvQty.setText("Quantité : " + quantity);
        tvPrice.setText(String.format(Locale.getDefault(), "%.2f DH", price));
    }
}