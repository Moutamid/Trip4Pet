package com.moutamid.trip4pet.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.databinding.ActivityAddPlaceBinding;

public class AddPlaceActivity extends AppCompatActivity {
    ActivityAddPlaceBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    boolean isGiven = false;
    boolean isCoordinates = false;
    String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        isGiven = getIntent().getBooleanExtra("GIVEN", false);
        isCoordinates = getIntent().getBooleanExtra("GIVEN", false);
        name = getIntent().getStringExtra("COORDINATES");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        binding.back.setOnClickListener(v -> onBackPressed());
        binding.setting.setOnClickListener(v -> {
            startActivity(new Intent(this, AroundPlaceActivity.class));
            finish();
        });
    }

    private OnMapReadyCallback callback = googleMap -> {

        if (!isGiven) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                        } else {
                            // Location not found, handle the case where there is no last known location
                            Toast.makeText(this, "Location not found!", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            String[] cord = name.split(", ");
            LatLng currentLatLng = new LatLng(Double.parseDouble(cord[0]), Double.parseDouble(cord[1])); // lat, long
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f));
//            if (isCoordinates) {
//
//            } else {
//                // City name is provided
//            }
        }
        googleMap.setMaxZoomPreference(20f);
//        googleMap.setMinZoomPreference(12f);
        googleMap.setOnCameraIdleListener(() -> {
            LatLng centerOfMap = googleMap.getCameraPosition().target;
            double latitude = centerOfMap.latitude;
            double longitude = centerOfMap.longitude;
            // Use latitude and longitude as needed
            Log.d(TAG, "lat & long:  " + latitude + "  " + longitude);
        });

    };
    private static final String TAG = "AddPlaceActivity";
}