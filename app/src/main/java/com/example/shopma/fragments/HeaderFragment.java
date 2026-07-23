package com.example.shopma.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shopma.R;
import com.example.shopma.activities.PanierActivity; // Adaptez selon le nom de votre activité Panier
import com.example.shopma.activities.ProfilActivity; // Adaptez selon le nom de votre activité Profil
import com.example.shopma.database.DatabaseHelper;

public class HeaderFragment extends Fragment {

    private TextView tvCartBadge;
    private FrameLayout btnPanier;
    private ImageView btnProfil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);

        tvCartBadge = view.findViewById(R.id.tvCartBadge);
        btnPanier = view.findViewById(R.id.btnPanier);
        btnProfil = view.findViewById(R.id.btnProfil);

        if (btnPanier != null) {
            btnPanier.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), PanierActivity.class);
                startActivity(intent);
            });
        }

        if (btnProfil != null) {
            btnProfil.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), ProfilActivity.class);
                startActivity(intent);
            });
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mettreAJourBadgePanier();
    }

    public void mettreAJourBadgePanier() {
        if (getContext() != null && tvCartBadge != null) {
            DatabaseHelper db = new DatabaseHelper(getContext());
            int nbArticles = db.getQuantiteTotalePanier();

            if (nbArticles > 0) {
                tvCartBadge.setVisibility(View.VISIBLE);
                tvCartBadge.setText(String.valueOf(nbArticles));
            } else {
                tvCartBadge.setVisibility(View.GONE);
            }
        }
    }
}