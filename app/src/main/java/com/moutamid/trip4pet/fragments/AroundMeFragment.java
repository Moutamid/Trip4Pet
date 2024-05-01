package com.moutamid.trip4pet.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.bottomsheets.FavoruiteDialog;
import com.moutamid.trip4pet.bottomsheets.FilterDialog;
import com.moutamid.trip4pet.bottomsheets.ListDialog;
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

        LatLng hungry = new LatLng(47.5333, 21.6333);
        LatLng parkolo = new LatLng(47.521024288430404, 21.62947502487398);
        LatLng kossuth = new LatLng(47.531415874646726, 21.624710724874703);
        LatLng malompark = new LatLng(47.54208223607607, 21.619344651863127);
        LatLng arena = new LatLng(47.54571354304601, 21.64295856720483);
        LatLng decathlon = new LatLng(47.543754716462416, 21.593888096040097);
        LatLng egyetem = new LatLng(47.55174025908643, 21.62166138069917);

        float density = getResources().getDisplayMetrics().density;
        int widthPx = (int) (20 * density);
        int heightPx = (int) (32 * density);
        int heightPx2 = (int) (25 * density);

        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(convertVectorToBitmap(requireContext(), R.drawable.beach_mark, widthPx, heightPx)))
                .position(parkolo).title("Vasutallomas parkolo Debrecen"));
        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(convertVectorToBitmap(requireContext(), R.drawable.resturant_mark, widthPx, heightPx2)))
                .position(kossuth).title("kossuth ter tram station Debrecen"));
        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(convertVectorToBitmap(requireContext(), R.drawable.tree_mark, widthPx, heightPx)))
                .position(malompark).title("Malompark Debrecen"));
        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(convertVectorToBitmap(requireContext(), R.drawable.tree_mark, widthPx, heightPx)))
                .position(arena).title("Fönix Arena Debrecen"));
        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(convertVectorToBitmap(requireContext(), R.drawable.resturant_mark, widthPx, heightPx2)))
                .position(decathlon).title("Decathlon Debrecen"));
        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(convertVectorToBitmap(requireContext(), R.drawable.beach_mark, widthPx, heightPx)))
                .position(egyetem).title("Egyetem ter, Debrecen"));

        googleMap.setMaxZoomPreference(20f);
        googleMap.setMinZoomPreference(12f);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(hungry));

    };

    public static Bitmap convertVectorToBitmap(Context context, int vectorDrawableId, int width, int height) {
        // Get the VectorDrawable from the resources
        Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
        if (vectorDrawable instanceof VectorDrawable) {
            // Create a Bitmap with the desired width and height
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            // Create a Canvas to draw the vector onto the Bitmap
            Canvas canvas = new Canvas(bitmap);
            // Set the bounds for the vector drawable
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            // Draw the vector onto the Canvas
            vectorDrawable.draw(canvas);
            return bitmap;
        } else {
            // Return null if the drawable is not a VectorDrawable
            return null;
        }
    }

}