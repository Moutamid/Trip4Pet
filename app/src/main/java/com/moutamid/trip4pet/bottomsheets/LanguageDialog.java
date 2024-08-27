package com.moutamid.trip4pet.bottomsheets;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.moutamid.trip4pet.Stash;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.databinding.LanguageSelectionBinding;
import com.moutamid.trip4pet.listener.BottomSheetDismissListener;

public class LanguageDialog extends BottomSheetDialogFragment {

    LanguageSelectionBinding binding;
    MaterialCardView selected;
    String language;
    private BottomSheetDismissListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LanguageSelectionBinding.inflate(getLayoutInflater(), container, false);
        language = Stash.getString(Constants.LANGUAGE, "en");
        switch (language) {
            case "en":
                selected = binding.english;
                break;
            case "es":
                selected = binding.spanish;
                break;
            case "fr":
                selected = binding.french;
                break;
            case "it":
                selected = binding.italian;
                break;
            case "nl":
                selected = binding.dutch;
                break;
        }
        selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.blue_light));
        selected.setStrokeColor(requireContext().getResources().getColor(R.color.blue));

        binding.close.setOnClickListener(v -> dismiss());
        binding.apply.setOnClickListener(v -> {
            Stash.put(Constants.LANGUAGE, language);
            dismiss();
        });

        binding.english.setOnClickListener(v -> {
            language = "en";
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.grey3));
            selected.setStrokeColor(requireContext().getResources().getColor(R.color.grey));
            selected = binding.english;
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.blue_light));
            selected.setStrokeColor(requireContext().getResources().getColor(R.color.blue));
        });

        binding.french.setOnClickListener(v -> {
            language = "fr";
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.grey3));
            selected.setStrokeColor(requireContext().getResources().getColor(R.color.grey));
            selected = binding.french;
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.blue_light));
            selected.setStrokeColor(requireContext().getResources().getColor(R.color.blue));
        });

        binding.dutch.setOnClickListener(v -> {
            language = "nl";
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.grey3));
            selected.setStrokeColor(requireContext().getResources().getColor(R.color.grey));
            selected = binding.dutch;
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.blue_light));
            selected.setStrokeColor(requireContext().getResources().getColor(R.color.blue));
        });

        binding.spanish.setOnClickListener(v -> {
            language = "es";
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.grey3));
            selected.setStrokeColor(requireContext().getResources().getColor(R.color.grey));
            selected = binding.spanish;
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.blue_light));
            selected.setStrokeColor(requireContext().getResources().getColor(R.color.blue));
        });

        binding.italian.setOnClickListener(v -> {
            language = "it";
            selected.setCardBackgroundColor(requireContext().getResources().getColor(R.color.grey3));
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

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onBottomSheetDismissed();
        }
    }

    public void setListener(BottomSheetDismissListener listener) {
        this.listener = listener;
    }

}
