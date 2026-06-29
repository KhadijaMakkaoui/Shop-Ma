package com.example.shopma.activities;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

import com.example.shopma.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mf != null) mf.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng kenitra = new LatLng(34.2541, -6.5890);
        googleMap.addMarker(new MarkerOptions().position(kenitra).title("Point Retrait ShopMa - ENSA Kénitra"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kenitra, 12.0f));
    }
}