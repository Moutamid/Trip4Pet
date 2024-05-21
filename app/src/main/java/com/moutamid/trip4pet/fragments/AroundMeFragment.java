package com.moutamid.trip4pet.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fxn.stash.Stash;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.activities.AccountActivity;
import com.moutamid.trip4pet.activities.DetailActivity;
import com.moutamid.trip4pet.bottomsheets.FavoruiteDialog;
import com.moutamid.trip4pet.bottomsheets.FilterDialog;
import com.moutamid.trip4pet.bottomsheets.ListDialog;
import com.moutamid.trip4pet.databinding.FragmentAroundMeBinding;
import com.moutamid.trip4pet.models.FilterModel;
import com.moutamid.trip4pet.models.LocationsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AroundMeFragment extends Fragment {
    FragmentAroundMeBinding binding;

    public AroundMeFragment() {
        // Required empty public constructor
    }
    ArrayList<LocationsModel> places = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAroundMeBinding.inflate(getLayoutInflater(), container, false);

        binding.filter.setOnClickListener(v -> {
            FilterDialog filterDialog = new FilterDialog();
            filterDialog.setListener(() -> {
                // dialog is dismissed
            });
            filterDialog.show(requireActivity().getSupportFragmentManager(), filterDialog.getTag());
        });

        binding.star.setOnClickListener(v -> {
            FavoruiteDialog filterDialog = new FavoruiteDialog();
            filterDialog.setListener(() -> {
                // dialog is dismissed
            });
            filterDialog.show(requireActivity().getSupportFragmentManager(), filterDialog.getTag());
        });
        binding.list.setOnClickListener(v -> {
            binding.mapIcon.setImageResource(R.drawable.map);
            ListDialog filterDialog = new ListDialog(places);
            filterDialog.setListener(() -> {
                binding.mapIcon.setImageResource(R.drawable.list_solid);
            });
            filterDialog.show(requireActivity().getSupportFragmentManager(), filterDialog.getTag());
        });

        Constants.initDialog(requireContext());
        Constants.showDialog();
        getPlaces();

        return binding.getRoot();
    }

    private void getPlaces() {
        Constants.databaseReference().child(Constants.PLACE).child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Constants.dismissDialog();
                        if (snapshot.exists()) {
                            places.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                LocationsModel model = dataSnapshot.getValue(LocationsModel.class);
                                places.add(model);
                            }
                        }
                        showMap();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Constants.dismissDialog();
                        Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showMap();
    }

    private OnMapReadyCallback callback = googleMap -> {
        float density = getResources().getDisplayMetrics().density;
        int widthPx = (int) (20 * density);
        int heightPx = (int) (32 * density);
        int heightPx2 = (int) (25 * density);

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
            for (FilterModel filterModel : model.activities){
                View marker = getLayoutInflater().inflate(R.layout.custom_marker, null, false);
                ImageView iconImage = marker.findViewById(R.id.icon);
                iconImage.setImageResource(filterModel.icon);
                googleMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(Constants.createDrawableFromView(requireContext(), marker)))
                        .position(latLng).title(model.name)).setTag(model.id);
            }
            for (FilterModel filterModel : model.services){
                View marker = getLayoutInflater().inflate(R.layout.custom_marker, null, false);
                ImageView iconImage = marker.findViewById(R.id.icon);
                iconImage.setImageResource(filterModel.icon);
                googleMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(Constants.createDrawableFromView(requireContext(), marker)))
                        .position(latLng).title(model.name)).setTag(model.id);
            }
            if (icon != 0) {
                googleMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(Constants.convertVectorToBitmap(requireContext(), icon, widthPx, (model.typeOfPlace.equals("Restaurant") ? heightPx2 : heightPx))))
                        .position(latLng).title(model.name)).setTag(model.id);
            } else {
                googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)).position(latLng).title(model.name)).setTag(model.id);
            }
            map.put(model.id, model);
        }

        googleMap.setOnMarkerClickListener(marker -> {
            LocationsModel model = map.get(marker.getTag());
            Stash.put(Constants.MODEL, model);
            startActivity(new Intent(requireContext(), DetailActivity.class));
            return true;
        });

        googleMap.setMinZoomPreference(4f);
        if (!places.isEmpty()) {
            LatLng latLng = new LatLng(places.get(0).latitude, places.get(0).longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

    };

}