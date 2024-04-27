package com.moutamid.trip4pet.bottomsheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.databinding.LanguageSelectionBinding;

public class LanguageDialog extends BottomSheetDialogFragment {

    LanguageSelectionBinding binding;
    MaterialCardView selected;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LanguageSelectionBinding.inflate(getLayoutInflater(), container, false);

        selected = binding.english;

        binding.close.setOnClickListener(v -> dismiss());
        binding.apply.setOnClickListener(v -> dismiss());

        binding.english.setOnClickListener(v -> {
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.grey));
            selected.setStrokeColor(requireContext().getResources().getColor(R.color.grey));
            selected = binding.english;
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.blue_light));
            selected.setStrokeColor(requireContext().getResources().getColor(R.color.blue));
        });

        binding.french.setOnClickListener(v -> {
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.grey));
            selected.setStrokeColor(requireContext().getResources().getColor(R.color.grey));
            selected = binding.french;
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.blue_light));
            selected.setStrokeColor(requireContext().getResources().getColor(R.color.blue));
        });

        binding.dutch.setOnClickListener(v -> {
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.grey));
            selected.setStrokeColor(requireContext().getResources().getColor(R.color.grey));
            selected = binding.dutch;
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.blue_light));
            selected.setStrokeColor(requireContext().getResources().getColor(R.color.blue));
        });

        binding.spanish.setOnClickListener(v -> {
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.grey));
            selected.setStrokeColor(requireContext().getResources().getColor(R.color.grey));
            selected = binding.spanish;
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.blue_light));
            selected.setStrokeColor(requireContext().getResources().getColor(R.color.blue));
        });

        binding.netherlands.setOnClickListener(v -> {
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.grey));
            selected.setStrokeColor(requireContext().getResources().getColor(R.color.grey));
            selected = binding.netherlands;
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.blue_light));
            selected.setStrokeColor(requireContext().getResources().getColor(R.color.blue));
        });

        binding.italian.setOnClickListener(v -> {
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.grey));
            selected.setStrokeColor(requireContext().getResources().getColor(R.color.grey));
            selected = binding.italian;
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.blue_light));
            selected.setStrokeColor(requireContext().getResources().getColor(R.color.blue));
        });

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set BottomSheet behavior to go full screen
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            BottomSheetBehavior.from((View) getView().getParent()).setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

}
