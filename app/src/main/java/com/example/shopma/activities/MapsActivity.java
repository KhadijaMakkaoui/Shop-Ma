package com.example.shopma.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import com.example.shopma.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Initialisation de la carte via SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 1. Définition d'au moins 4 points de retrait au Maroc
        LatLng casablanca = new LatLng(33.5731, -7.5898);
        LatLng rabat = new LatLng(34.0209, -6.8416);
        LatLng kenitra = new LatLng(34.2541, -6.5890);
        LatLng marrakech = new LatLng(31.6295, -7.9811);

        // 2. Ajout des marqueurs avec titre et snippet (adresse complète)
        mMap.addMarker(new MarkerOptions()
                .position(casablanca)
                .title("Point de Retrait - Casablanca")
                .snippet("Bd Mohammed V, Gauthier, Casablanca"));

        mMap.addMarker(new MarkerOptions()
                .position(rabat)
                .title("Point de Retrait - Rabat")
                .snippet("Avenue Hassan II, Agdal, Rabat"));

        mMap.addMarker(new MarkerOptions()
                .position(kenitra)
                .title("Point de Retrait - ENSA Kénitra")
                .snippet("Université Ibn Tofail, BP 242, Kénitra"));

        mMap.addMarker(new MarkerOptions()
                .position(marrakech)
                .title("Point de Retrait - Marrakech")
                .snippet("Avenue Mohammed VI, Guéliz, Marrakech"));

        // 3. Centrage de la caméra sur le Maroc avec un zoom adapté (6.5f)
        LatLng centreMaroc = new LatLng(33.0000, -7.3000);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centreMaroc, 6.5f));
    }
}