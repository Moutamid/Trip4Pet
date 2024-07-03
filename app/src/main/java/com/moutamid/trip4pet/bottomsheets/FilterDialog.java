package com.moutamid.trip4pet.bottomsheets;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fxn.stash.Stash;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.databinding.FilterFragmentBinding;
import com.moutamid.trip4pet.listener.BottomSheetDismissListener;
import com.moutamid.trip4pet.models.FilterModel;

import java.util.ArrayList;

public class FilterDialog extends BottomSheetDialogFragment {
    FilterFragmentBinding binding;
    private BottomSheetDismissListener listener;
    ArrayList<String> filters;
    public static String[] placesList = {
            "Park",
            "Restaurant",
            "Beach",
            "Other",
    };
    ArrayList<FilterModel> service = new ArrayList<>();
    ArrayList<FilterModel> activities = new ArrayList<>();
    ArrayList<FilterModel> places = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FilterFragmentBinding.inflate(getLayoutInflater(), container, false);
        Constants.setLocale(requireContext(), Stash.getString(Constants.LANGUAGE, "en"));
        binding.apply.setOnClickListener(v -> {
            getList();
        });

        filters = Stash.getArrayList(Constants.FILTERS, String.class);

        addPlaces();
        addServices();

        if (Stash.getBoolean(Constants.ISVIP, false)) {
            binding.rating3.setEnabled(true);
            binding.rating40.setEnabled(true);
            binding.rating47.setEnabled(true);

            binding.lock1.setVisibility(View.GONE);
            binding.lock2.setVisibility(View.GONE);
            binding.lock3.setVisibility(View.GONE);

            binding.ratingCard3.setOnClickListener(v -> {
                binding.rating3.setChecked(!binding.rating3.isChecked());
                binding.rating47.setChecked(false);
                binding.rating40.setChecked(false);
            });
            binding.ratingCard4.setOnClickListener(v -> {
                binding.rating40.setChecked(!binding.rating40.isChecked());
                binding.rating47.setChecked(false);
                binding.rating3.setChecked(false);
            });
            binding.ratingCard47.setOnClickListener(v -> {
                binding.rating47.setChecked(!binding.rating47.isChecked());
                binding.rating40.setChecked(false);
                binding.rating3.setChecked(false);
            });

        }

        binding.close.setOnClickListener(v -> {
            Stash.clear(Constants.FILTERS);
            dismiss();
        });

        return binding.getRoot();
    }

    private void getList() {
        filters.clear();
        for (int i = 0; i < binding.places.getChildCount(); i++) {
            View view = binding.places.getChildAt(i);
            if (view instanceof RelativeLayout) {
                RelativeLayout main = (RelativeLayout) view;
                MaterialCheckBox checkbox = main.findViewById(R.id.checkbox);
                TextView text = main.findViewById(R.id.text);
                if (checkbox.isChecked()) {
                    filters.add(text.getText().toString());
                }
            }
        }
        for (int i = 0; i < binding.services.getChildCount(); i++) {
            View view = binding.services.getChildAt(i);
            if (view instanceof RelativeLayout) {
                RelativeLayout main = (RelativeLayout) view;
                MaterialCheckBox checkbox = main.findViewById(R.id.checkbox);
                TextView text = main.findViewById(R.id.text);
                if (checkbox.isChecked()) {
                    filters.add(text.getText().toString());
                }
            }
        }
        for (int i = 0; i < binding.activities.getChildCount(); i++) {
            View view = binding.activities.getChildAt(i);
            if (view instanceof RelativeLayout) {
                RelativeLayout main = (RelativeLayout) view;
                MaterialCheckBox checkbox = main.findViewById(R.id.checkbox);
                TextView text = main.findViewById(R.id.text);
                if (checkbox.isChecked()) {
                    filters.add(text.getText().toString());
                }
            }
        }

        if (Stash.getBoolean(Constants.ISVIP, false)) {
            if (binding.rating47.isChecked()) {
                filters.add("4.7");
            }
            if (binding.rating40.isChecked()) {
                filters.add("4.0");
            }
            if (binding.rating3.isChecked()) {
                filters.add("3");
            }
        }
        Stash.put(Constants.FILTERS, filters);
        dismiss();
    }

    private void addPlaces() {
        places = new ArrayList<>(Constants.getTypeOfPlaces(requireContext()));
        for (FilterModel s : places) {
            LayoutInflater inflater = getLayoutInflater();
            View customEditTextLayout = inflater.inflate(R.layout.filter_check_layout, null);
            MaterialCheckBox checkbox = customEditTextLayout.findViewById(R.id.checkbox);
            TextView text = customEditTextLayout.findViewById(R.id.text);
            ImageView lock = customEditTextLayout.findViewById(R.id.lock);
            ImageView image = customEditTextLayout.findViewById(R.id.image);

            lock.setVisibility(View.GONE);
            image.setImageResource(s.icon);
            text.setText(s.name);
            checkbox.setEnabled(true);

            MaterialCardView card = customEditTextLayout.findViewById(R.id.card);
            card.setOnClickListener(v -> {
                checkbox.setChecked(!checkbox.isChecked());
            });

            for (String filter : filters) {
                if (filter.trim().equalsIgnoreCase(s.name.trim())) {
                    checkbox.setChecked(true);
                    break;
                }
            }

            binding.places.addView(customEditTextLayout);
        }
    }

    private void addServices() {
        service = new ArrayList<>(Constants.getServices(requireContext()));
        for (FilterModel s : service) {
            LayoutInflater inflater = getLayoutInflater();
            View customEditTextLayout = inflater.inflate(R.layout.filter_check_layout, null);

            MaterialCheckBox checkbox = customEditTextLayout.findViewById(R.id.checkbox);
            ImageView lock = customEditTextLayout.findViewById(R.id.lock);

            ImageView image = customEditTextLayout.findViewById(R.id.image);
            TextView text = customEditTextLayout.findViewById(R.id.text);

            image.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));

            if (Stash.getBoolean(Constants.ISVIP, false)) {
                lock.setVisibility(View.GONE);
                checkbox.setEnabled(true);

                MaterialCardView card = customEditTextLayout.findViewById(R.id.card);
                card.setOnClickListener(v -> {
                    checkbox.setChecked(!checkbox.isChecked());
                });
                for (String filter : filters) {
                    if (filter.equals(s.name)) {
                        checkbox.setChecked(true);
                        break;
                    }
                }
            }

            image.setImageResource(s.icon);
            text.setText(s.name);
            binding.services.addView(customEditTextLayout);
        }
    }

/*    private void addActivities() {
        activities = new ArrayList<>(Constants.getActivities(requireContext()));

        for (FilterModel s : activities) {
            LayoutInflater inflater = getLayoutInflater();
            View customEditTextLayout = inflater.inflate(R.layout.filter_check_layout, null);

            MaterialCheckBox checkbox = customEditTextLayout.findViewById(R.id.checkbox);
            ImageView lock = customEditTextLayout.findViewById(R.id.lock);

            ImageView image = customEditTextLayout.findViewById(R.id.image);
            TextView text = customEditTextLayout.findViewById(R.id.text);
            image.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
            image.setImageResource(s.icon);
            text.setText(s.name);

            if (Stash.getBoolean(Constants.ISVIP, false)) {
                lock.setVisibility(View.GONE);
                checkbox.setEnabled(true);

                MaterialCardView card = customEditTextLayout.findViewById(R.id.card);
                card.setOnClickListener(v -> {
                    checkbox.setChecked(!checkbox.isChecked());
                });
                for (String filter : filters) {
                    if (filter.equals(s.name)) {
                        checkbox.setChecked(true);
                        break;
                    }
                }
            }

            binding.activities.addView(customEditTextLayout);
        }
    }*/

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
