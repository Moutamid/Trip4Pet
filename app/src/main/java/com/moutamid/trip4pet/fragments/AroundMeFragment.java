package com.moutamid.trip4pet.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.fxn.stash.Stash;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.activities.DetailActivity;
import com.moutamid.trip4pet.bottomsheets.FavoruiteDialog;
import com.moutamid.trip4pet.bottomsheets.FilterDialog;
import com.moutamid.trip4pet.bottomsheets.ListDialog;
import com.moutamid.trip4pet.databinding.FragmentAroundMeBinding;
import com.moutamid.trip4pet.models.Cities;
import com.moutamid.trip4pet.models.CommentModel;
import com.moutamid.trip4pet.models.FilterModel;
import com.moutamid.trip4pet.models.LocationsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AroundMeFragment extends Fragment {
    FragmentAroundMeBinding binding;
    GoogleMap mMap;
    private ArrayList<Marker> currentMarkers = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationClient;

    Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    public AroundMeFragment() {
        // Required empty public constructor
    }

    ArrayList<LocationsModel> places = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAroundMeBinding.inflate(getLayoutInflater(), container, false);
        Constants.setLocale(requireContext(), Stash.getString(Constants.LANGUAGE, "en"));
        binding.filter.setOnClickListener(v -> {
            FilterDialog filterDialog = new FilterDialog();
            filterDialog.setListener(() -> {
                filterPlaces();
            });
            filterDialog.show(requireActivity().getSupportFragmentManager(), filterDialog.getTag());
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        binding.star.setOnClickListener(v -> {
            FavoruiteDialog favoruiteDialog = new FavoruiteDialog();
            favoruiteDialog.setListener(() -> {
                // dialog is dismissed
            });
            favoruiteDialog.show(requireActivity().getSupportFragmentManager(), favoruiteDialog.getTag());
        });
        binding.list.setOnClickListener(v -> {
            binding.mapIcon.setImageResource(R.drawable.map);
            ListDialog listDialog = new ListDialog(places);
            listDialog.setListener(() -> {
                binding.mapIcon.setImageResource(R.drawable.list_solid);
            });
            listDialog.show(requireActivity().getSupportFragmentManager(), listDialog.getTag());
        });

//        Constants.initDialog(requireContext());
//         Constants.showDialog();

        return binding.getRoot();
    }

    private void filterPlaces() {

        float density = getResources().getDisplayMetrics().density;
        int widthPx = (int) (20 * density);
        int heightPx = (int) (32 * density);
        int heightPx2 = (int) (25 * density);

        ArrayList<String> filters = Stash.getArrayList(Constants.FILTERS, String.class);
        for (Marker marker : currentMarkers) {
            marker.remove();
        }
        currentMarkers.clear();
        if (filters.isEmpty()) {
            showAll();
        } else {
            Map<String, LocationsModel> map = new HashMap<>();
            for (LocationsModel model : places) {
                int icon = 0;
                if (model.typeOfPlace.equals("Park")) {
                    icon = R.drawable.tree_mark;
                } else if (model.typeOfPlace.equals("Restaurant")) {
                    icon = R.drawable.resturant_mark;
                } else if (model.typeOfPlace.equals("Beach")) {
                    icon = R.drawable.beach_mark;
                }
                for (String filter : filters) {
                    float total = 0;
                    if (model.comments != null) {
                        float rating = 0;
                        for (CommentModel commentModel : model.comments) {
                            rating += commentModel.rating;
                        }
                        total = rating / model.comments.size();
                    }
                    if (model.typeOfPlace.contains(filter) || (model.rating == total && total != 0) || getFilter(model, filter)) {
                        LatLng latLng = new LatLng(model.latitude, model.longitude);
                        if (model.activities != null) {
                            for (FilterModel filterModel : model.activities) {
                                View customMarker = getLayoutInflater().inflate(R.layout.marker, null, false);
                                ImageView iconImage = customMarker.findViewById(R.id.image);
                                iconImage.setImageResource(filterModel.icon);
                                MarkerOptions markerOptions = new MarkerOptions()
                                        .icon(BitmapDescriptorFactory.fromBitmap(Constants.createDrawableFromView(requireContext(), customMarker)))
                                        .position(latLng).title(model.name);
                                Marker marker = mMap.addMarker(markerOptions);
                                marker.setTag(model.id);
                                currentMarkers.add(marker);
                            }
                        }
                        if (model.services != null) {
                            for (FilterModel filterModel : model.services) {
                                View customMarker = getLayoutInflater().inflate(R.layout.custom_marker, null, false);
                                ImageView iconImage = customMarker.findViewById(R.id.icon);
                                iconImage.setImageResource(filterModel.icon);
                                MarkerOptions markerOptions = new MarkerOptions()
                                        .icon(BitmapDescriptorFactory.fromBitmap(Constants.createDrawableFromView(requireContext(), customMarker)))
                                        .position(latLng).title(model.name);
                                Marker marker = mMap.addMarker(markerOptions);
                                marker.setTag(model.id);
                                currentMarkers.add(marker);

                            }
                        }

                        if (model.typeOfPlace.equals("Other")) {
                            MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)).position(latLng).title(model.name);
                            Marker marker = mMap.addMarker(markerOptions);
                            marker.setTag(model.id);
                            currentMarkers.add(marker);
                        }

                        if (model.activities == null && model.services == null) {
                            if (icon != 0) {
                                MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(Constants.convertVectorToBitmap(getContext(), icon, widthPx, heightPx))).position(latLng).title(model.name);
                                Marker marker = mMap.addMarker(markerOptions);
                                marker.setTag(model.id);
                                currentMarkers.add(marker);
                            }
                        }

                        map.put(model.id, model);
                    }

                    mMap.setOnMarkerClickListener(marker -> {
                        LocationsModel location = map.get(marker.getTag());
                        Stash.put(Constants.MODEL, location);
                        startActivity(new Intent(requireContext(), DetailActivity.class));
                        return true;
                    });

                    mMap.setMinZoomPreference(4f);
                    if (!places.isEmpty()) {
                        LatLng latLng = new LatLng(places.get(0).latitude, places.get(0).longitude);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8f));
                    }
                }
            }
        }
    }

    private boolean getFilter(LocationsModel model, String filter) {
        boolean service = false, activity = false;
        if (model.services != null) {
            for (FilterModel ser : model.services) {
                if (ser.name.contains(filter)) {
                    service = true;
                    break;
                }
            }
        }
        if (model.activities != null) {
            for (FilterModel act : model.activities) {
                if (act.name.contains(filter)) {
                    activity = true;
                    break;
                }
            }
        }
        return service || activity;
    }

    private void getPlaces() {
        Constants.databaseReference().child(Constants.PLACE)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Constants.dismissDialog();
                        if (snapshot.exists()) {
                            places.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                    LocationsModel model = dataSnapshot2.getValue(LocationsModel.class);
                                    places.add(model);
                                }
                            }
                        }
                        showAll();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Constants.dismissDialog();
                        Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        getPlaces();
    }

    @Override
    public void onResume() {
        super.onResume();
        Cities cities = (Cities) Stash.getObject(Constants.AROUND_PLACE, Cities.class);
        if (cities == null) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), location -> {
                        if (location != null) {
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 6f));
                        } else {
                            new MaterialAlertDialogBuilder(requireContext())
                                    .setMessage(getString(R.string.this_function_requires_a_gps_connection))
                                    .setCancelable(false)
                                    .setPositiveButton(getString(R.string.open_setting), (dialog, which) -> {
                                        dialog.dismiss();
                                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivity(intent);
                                    }).setNegativeButton(getString(R.string.close), (dialog, which) -> dialog.dismiss())
                                    .show();
                        }
                    });
        }
    }

    private final OnMapReadyCallback callback = googleMap -> {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        Cities cities = (Cities) Stash.getObject(Constants.AROUND_PLACE, Cities.class);
        if (cities != null) {
            LatLng currentLatLng = new LatLng(cities.latitude, cities.longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 6f));
        }
    };

    private void showAll() {

        float density = getResources().getDisplayMetrics().density;
        int widthPx = (int) (20 * density);
        int heightPx = (int) (32 * density);
        int heightPx2 = (int) (25 * density);

        for (Marker marker : currentMarkers) {
            marker.remove();
        }
        currentMarkers.clear();
        Map<String, LocationsModel> map = new HashMap<>();
        for (LocationsModel model : places) {
            LatLng latLng = new LatLng(model.latitude, model.longitude);
            int icon = 0;
            if (model.typeOfPlace.equals("Park")) {
                icon = R.drawable.tree_mark;
            } else if (model.typeOfPlace.equals("Restaurant")) {
                icon = R.drawable.resturant_mark;
            } else if (model.typeOfPlace.equals("Beach")) {
                icon = R.drawable.beach_mark;
            }
            if (model.activities != null) {
                for (FilterModel filterModel : model.activities) {
                    View customMarker = getLayoutInflater().inflate(R.layout.marker, null, false);
                    ImageView iconImage = customMarker.findViewById(R.id.image);
                    iconImage.setImageResource(filterModel.icon);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromBitmap(Constants.createDrawableFromView(requireContext(), customMarker)))
                            .position(latLng).title(model.name);
                    Marker marker = mMap.addMarker(markerOptions);
                    marker.setTag(model.id);
                    currentMarkers.add(marker);
                }
            }
            if (model.services != null) {
                for (FilterModel filterModel : model.services) {
                    View customMarker = getLayoutInflater().inflate(R.layout.custom_marker, null, false);
                    ImageView iconImage = customMarker.findViewById(R.id.icon);
                    iconImage.setImageResource(filterModel.icon);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromBitmap(Constants.createDrawableFromView(requireContext(), customMarker)))
                            .position(latLng).title(model.name);
                    Marker marker = mMap.addMarker(markerOptions);
                    marker.setTag(model.id);
                    currentMarkers.add(marker);
                }
            }
            if (model.typeOfPlace.equals("Other")) {
                MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)).position(latLng).title(model.name);
                Marker marker = mMap.addMarker(markerOptions);
                marker.setTag(model.id);
                currentMarkers.add(marker);
            }

            if (model.activities == null && model.services == null) {
                if (icon != 0) {
                    MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(Constants.convertVectorToBitmap(getContext(), icon, widthPx, heightPx))).position(latLng).title(model.name);
                    Marker marker = mMap.addMarker(markerOptions);
                    marker.setTag(model.id);
                    currentMarkers.add(marker);
                }
            }
            map.put(model.id, model);
        }

        mMap.setOnMarkerClickListener(marker -> {
            LocationsModel model = map.get(marker.getTag());
            Stash.put(Constants.MODEL, model);
            startActivity(new Intent(requireContext(), DetailActivity.class));
            return true;
        });

        mMap.setMinZoomPreference(4f);
//        if (!places.isEmpty()) {
//            LatLng latLng = new LatLng(places.get(0).latitude, places.get(0).longitude);
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        }
    }

}