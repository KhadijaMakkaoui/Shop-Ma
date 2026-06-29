package com.example.shopma.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shopma.R;
import com.example.shopma.activities.*;

public class HeaderFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);

        view.findViewById(R.id.btnHeaderMaps).setOnClickListener(v -> startActivity(new Intent(getActivity(), MapsActivity.class)));
        view.findViewById(R.id.btnHeaderCart).setOnClickListener(v -> startActivity(new Intent(getActivity(), PanierActivity.class)));
        view.findViewById(R.id.btnHeaderProfile).setOnClickListener(v -> startActivity(new Intent(getActivity(), ProfilActivity.class)));

        return view;
    }
}