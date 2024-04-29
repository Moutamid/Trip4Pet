package com.moutamid.trip4pet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.bottomsheets.FilterDialog;
import com.moutamid.trip4pet.databinding.FragmentAroundMeBinding;

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

    };

}