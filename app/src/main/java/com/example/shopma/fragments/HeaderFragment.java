package com.example.shopma.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.shopma.R;
import com.example.shopma.activities.PanierActivity;
import com.example.shopma.activities.ProfilActivity;

public class HeaderFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);
        view.findViewById(R.id.ivHeaderPanier).setOnClickListener(v -> startActivity(new Intent(getActivity(), PanierActivity.class)));
        view.findViewById(R.id.ivHeaderProfil).setOnClickListener(v -> startActivity(new Intent(getActivity(), ProfilActivity.class)));
        return view;
    }
}