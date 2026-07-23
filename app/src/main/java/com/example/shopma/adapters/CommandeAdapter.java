package com.example.shopma.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.shopma.R;

import java.util.Locale;

public class CommandeAdapter extends CursorAdapter {

    public CommandeAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_commande, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvNum = view.findViewById(R.id.tvCommandeNum);
        TextView tvDetails = view.findViewById(R.id.tvCommandeDetails);
        TextView tvTotal = view.findViewById(R.id.tvCommandeTotal);
        TextView tvStatut = view.findViewById(R.id.tvCommandeStatut);

        // Récupération des données depuis le Cursor
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
        int nbArticles = cursor.getInt(cursor.getColumnIndexOrThrow("nb_articles"));
        double montantTotal = cursor.getDouble(cursor.getColumnIndexOrThrow("montant_total"));
        String statut = cursor.getString(cursor.getColumnIndexOrThrow("statut"));

        // Formatage du numéro (#001, #002...)
        tvNum.setText(String.format(Locale.getDefault(), "Commande #%03d", id));

        // Formatage du détail : "15/03/2025 : 3 articles"
        String articleText = nbArticles > 1 ? "articles" : "article";
        tvDetails.setText(String.format(Locale.getDefault(), "%s : %d %s", date, nbArticles, articleText));

        // Formatage du montant total
        tvTotal.setText(String.format(Locale.getDefault(), "%.2f DH", montantTotal));

        // Personnalisation dynamique du Badge de Statut
        GradientDrawable badgeBackground = new GradientDrawable();
        badgeBackground.setCornerRadius(30f); // Bords très arrondis (Pill style)

        if (statut != null && (statut.equalsIgnoreCase("livree") || statut.equalsIgnoreCase("Livrée"))) {
            badgeBackground.setColor(Color.parseColor("#E8F5E9")); // Vert clair
            tvStatut.setTextColor(Color.parseColor("#2E7D32"));   // Vert foncé
            tvStatut.setText("Livrée");
        } else {
            badgeBackground.setColor(Color.parseColor("#FFF3E0")); // Orange clair
            tvStatut.setTextColor(Color.parseColor("#EF6C00"));   // Orange foncé
            tvStatut.setText("En cours");
        }

        tvStatut.setBackground(badgeBackground);
    }
}