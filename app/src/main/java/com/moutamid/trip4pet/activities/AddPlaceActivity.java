package com.moutamid.trip4pet.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moutamid.trip4pet.Stash;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.databinding.ActivityAddPlaceBinding;
import com.moutamid.trip4pet.models.LocationsModel;

import java.util.ArrayList;

public class AddPlaceActivity extends AppCompatActivity {
    ActivityAddPlaceBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    boolean isGiven = false;
    String name = "";
    GoogleMap mMap;
    Location loc = new Location("");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.setLocale(getBaseContext(), Stash.getString(Constants.LANGUAGE, "en"));
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        isGiven = getIntent().getBooleanExtra("GIVEN", false);
        name = getIntent().getStringExtra(Constants.COORDINATES);
        String place = getIntent().getStringExtra(Constants.PLACE);
        if (place == null){
            place = ",";
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        binding.back.setOnClickListener(v -> onBackPressed());
        binding.setting.setOnClickListener(v -> {
            startActivity(new Intent(this, AroundPlaceActivity.class));
            finish();
        });

        String finalPlace = place;
        binding.pick.setOnClickListener(v -> {
            LatLng centerOfMap = mMap.getCameraPosition().target;
            ArrayList<LocationsModel> places = Stash.getArrayList(Constants.PLACES, LocationsModel.class);
            loc.setLatitude(centerOfMap.latitude);
            loc.setLongitude(centerOfMap.longitude);
            boolean isNearBy = false;
            for (LocationsModel model : places) {
                Location loc2 = new Location("");
                loc2.setLatitude(model.latitude);
                loc2.setLongitude(model.longitude);
                Log.d(TAG, "distanceTo: " + loc.distanceTo(loc2));
                if (loc.distanceTo(loc2) <= 50) {
                    isNearBy = true;
                    break;
                }
            }
            String COORDINATES = centerOfMap.latitude + ", " + centerOfMap.longitude;
            if (!isNearBy) {
                startActivity(new Intent(this, PlaceAddActivity.class).putExtra(Constants.COORDINATES, COORDINATES).putExtra(Constants.PLACE, finalPlace));
                finish();
            } else {
                new MaterialAlertDialogBuilder(this)
                        .setTitle(R.string.confirm_location)
                        .setMessage(R.string.our_system_detected_another_nearby_location_is_the_entered_location_correct)
                        .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss())
                        .setPositiveButton(R.string.yes, (dialog, which) -> {
                            dialog.dismiss();
                            startActivity(new Intent(this, PlaceAddActivity.class).putExtra(Constants.COORDINATES, COORDINATES).putExtra(Constants.PLACE, finalPlace));
                            finish();
                        })
                        .show();
            }
        });

        binding.aroundMe.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f));
                        } else {
                            Toast.makeText(this, getString(R.string.location_not_found), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private OnMapReadyCallback callback = googleMap -> {
        this.mMap = googleMap;
        if (!isGiven) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f));
                        } else {
                            // Location not found, handle the case where there is no last known location
                            Toast.makeText(this, getString(R.string.location_not_found), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            String[] cord = name.split(", ");
            LatLng currentLatLng = new LatLng(Double.parseDouble(cord[0]), Double.parseDouble(cord[1])); // lat, long
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f));
        }
        mMap.setMaxZoomPreference(20f);
//        googleMap.setMinZoomPreference(12f);
        mMap.setOnCameraIdleListener(() -> {
            LatLng centerOfMap = mMap.getCameraPosition().target;
            double latitude = centerOfMap.latitude;
            double longitude = centerOfMap.longitude;
            // Use latitude and longitude as needed
            Log.d(TAG, "lat & long:  " + latitude + "  " + longitude);
        });
        mMap.setMyLocationEnabled(true);
    };
    private static final String TAG = "AddPlaceActivity";
}