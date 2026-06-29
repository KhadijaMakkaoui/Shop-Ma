package com.example.shopma.adapters;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.shopma.R;

public class CartAdapter extends CursorAdapter {
    public CartAdapter(Context context, Cursor c, int flags) { super(context, c, flags); }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_panier, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView title = view.findViewById(R.id.tvCartItemTitle);
        TextView qty = view.findViewById(R.id.tvCartItemQty);
        TextView price = view.findViewById(R.id.tvCartItemPrice);

        title.setText(cursor.getString(cursor.getColumnIndexOrThrow("title")));
        int q = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
        qty.setText("Quantité : " + q);
        price.setText(String.format("%.2f DH", cursor.getDouble(cursor.getColumnIndexOrThrow("price")) * 10 * q));
    }
}