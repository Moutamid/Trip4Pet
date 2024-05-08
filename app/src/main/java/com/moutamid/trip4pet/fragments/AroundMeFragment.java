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
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
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
            ListDialog filterDialog = new ListDialog();
            filterDialog.setListener(() -> {
                binding.mapIcon.setImageResource(R.drawable.list_solid);
            });
            filterDialog.show(requireActivity().getSupportFragmentManager(), filterDialog.getTag());
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private OnMapReadyCallback callback = googleMap -> {
        float density = getResources().getDisplayMetrics().density;
        int widthPx = (int) (20 * density);
        int heightPx = (int) (32 * density);
        int heightPx2 = (int) (25 * density);
        ArrayList<LocationsModel> list = Constants.getList(requireContext());
        Map<String, LocationsModel> map = new HashMap<>();
        for (LocationsModel model : list) {
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


//        LatLng hungry = new LatLng(47.5333, 21.6333);
//        LatLng parkolo = new LatLng(47.521024288430404, 21.62947502487398);
//        LatLng kossuth = new LatLng(47.531415874646726, 21.624710724874703);
//        LatLng malompark = new LatLng(47.54208223607607, 21.619344651863127);
//        LatLng arena = new LatLng(47.54571354304601, 21.64295856720483);
//        LatLng decathlon = new LatLng(47.543754716462416, 21.593888096040097);
//        LatLng egyetem = new LatLng(47.55174025908643, 21.62166138069917);
//
//        googleMap.addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromBitmap(convertVectorToBitmap(requireContext(), R.drawable.beach_mark, widthPx, heightPx)))
//                .position(parkolo).title("Vasutallomas parkolo Debrecen"));
//        googleMap.addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromBitmap(convertVectorToBitmap(requireContext(), R.drawable.resturant_mark, widthPx, heightPx2)))
//                .position(kossuth).title("kossuth ter tram station Debrecen"));
//        googleMap.addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromBitmap(convertVectorToBitmap(requireContext(), R.drawable.tree_mark, widthPx, heightPx)))
//                .position(malompark).title("Malompark Debrecen"));
//        googleMap.addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromBitmap(convertVectorToBitmap(requireContext(), R.drawable.tree_mark, widthPx, heightPx)))
//                .position(arena).title("Fönix Arena Debrecen"));
//        googleMap.addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromBitmap(convertVectorToBitmap(requireContext(), R.drawable.resturant_mark, widthPx, heightPx2)))
//                .position(decathlon).title("Decathlon Debrecen"));
//        googleMap.addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromBitmap(convertVectorToBitmap(requireContext(), R.drawable.beach_mark, widthPx, heightPx)))
//                .position(egyetem).title("Egyetem ter, Debrecen"));

//        googleMap.setMaxZoomPreference(20f);
//        googleMap.setMinZoomPreference(12f);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(30.3753, 69.3451)));

    };

}