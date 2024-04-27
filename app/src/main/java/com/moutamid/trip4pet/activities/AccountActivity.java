package com.moutamid.trip4pet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.card.MaterialCardView;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.databinding.ActivityAccountBinding;

public class AccountActivity extends AppCompatActivity {
    ActivityAccountBinding binding;
    MaterialCardView selected;
    TextView selectedText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        binding.back.setOnClickListener(v -> onBackPressed());
        binding.setting.setOnClickListener(v -> startActivity(new Intent(this, AccountSettingActivity.class)));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        selected = binding.addedPlaces;
        selectedText = binding.addedText;

        binding.addedPlaces.setOnClickListener(v -> {
            selectedText.setTextColor(getResources().getColor(R.color.text));
            selected.setCardBackgroundColor(getResources().getColor(R.color.white));
            selected = binding.addedPlaces;
            selectedText = binding.addedText;
            selectedText.setTextColor(getResources().getColor(R.color.white));
            selected.setCardBackgroundColor(getResources().getColor(R.color.blue));
        });
        binding.comments.setOnClickListener(v -> {
            selectedText.setTextColor(getResources().getColor(R.color.text));
            selected.setCardBackgroundColor(getResources().getColor(R.color.white));
            selected = binding.comments;
            selectedText = binding.commentsText;
            selectedText.setTextColor(getResources().getColor(R.color.white));
            selected.setCardBackgroundColor(getResources().getColor(R.color.blue));
        });
        binding.visited.setOnClickListener(v -> {
            selectedText.setTextColor(getResources().getColor(R.color.text));
            selected.setCardBackgroundColor(getResources().getColor(R.color.white));
            selected = binding.visited;
            selectedText = binding.visitedText;
            selectedText.setTextColor(getResources().getColor(R.color.white));
            selected.setCardBackgroundColor(getResources().getColor(R.color.blue));
        });

    }
    private OnMapReadyCallback callback = googleMap -> {

        LatLng hungry = new LatLng(47.5333, 21.6333);
        LatLng parkolo = new LatLng(47.521024288430404, 21.62947502487398);
        LatLng kossuth = new LatLng(47.531415874646726, 21.624710724874703);
        LatLng malompark = new LatLng(47.54208223607607, 21.619344651863127);
        LatLng arena = new LatLng(47.54571354304601, 21.64295856720483);
        LatLng decathlon = new LatLng(47.543754716462416, 21.593888096040097);
        LatLng egyetem = new LatLng(47.55174025908643, 21.62166138069917);

        googleMap.addMarker(new MarkerOptions().position(parkolo).title("Vasutallomas parkolo Debrecen"));
        googleMap.addMarker(new MarkerOptions().position(kossuth).title("kossuth ter tram station Debrecen"));
        googleMap.addMarker(new MarkerOptions().position(malompark).title("Malompark Debrecen"));
        googleMap.addMarker(new MarkerOptions().position(arena).title("Fönix Arena Debrecen"));
        googleMap.addMarker(new MarkerOptions().position(decathlon).title("Decathlon Debrecen"));
        googleMap.addMarker(new MarkerOptions().position(egyetem).title("Egyetem ter, Debrecen"));

        googleMap.setMaxZoomPreference(20f);
        googleMap.setMinZoomPreference(12f);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(hungry));

//        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            googleMap.setMyLocationEnabled(true);
//        }
//        googleMap.getUiSettings().setCompassEnabled(true);
//        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

    };

}