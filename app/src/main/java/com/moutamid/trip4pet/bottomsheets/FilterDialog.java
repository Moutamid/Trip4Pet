package com.moutamid.trip4pet.bottomsheets;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputLayout;
import com.moutamid.trip4pet.BottomSheetDismissListener;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.databinding.FilterFragmentBinding;

public class FilterDialog extends BottomSheetDialogFragment {
    FilterFragmentBinding binding;
    private BottomSheetDismissListener listener;

    String[] activitiesNearby = {
            "Playground",
            "Point of view",
            "Swimming possible",
            "Climbing (Sites of)",
            "Canoe/kayak (Base of)",
            "Fishing spots",
            "Beach fisheries",
            "Departure of hikes",
            "Monuments to visit",
            "Mountain bike tracks",
            "Windsurf/kitesurf (Spot of)",
            "Beautiful motorcycle rides"
    };

    String[] nearbyServices = {
            "Electricity (access possible)",
            "Drinking water",
            "Black water",
            "Wastewater",
            "Waste container",
            "Bakery",
            "Public toilets",
            "Showers (possible access)",
            "Internet access via WiFi",
            "Winter-caravanning",
            "Pets allowed",
            "Swimming pool",
            "Laundry",
            "Wash & Fold",
            "Dry Cleaning",
            "Same Day Service (possible)",
            "Ironing",
            "Shirt Laundry",
            "Duvet Cleaning",
            "Special Care Items",
            "Shoe Cleaning",
            "Pick-Up & Delivery (possible)"
    };
    String[] placesList = {
            "Free motorhome area",
            "Paying motorhome area",
            "Private car park for campers",
            "MH parking without services",
            "Homestays accommodation",
            "Service area without parking",
            "Picnic area",
            "Rest area",
            "Camping",
            "On the farm (farm, vineyard...)",
            "Surrounded by nature",
            "Parking lot day/night",
            "Daily parking lot only",
            "Off-road (4x4)",
            "Extra services"
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FilterFragmentBinding.inflate(getLayoutInflater(), container, false);

        binding.apply.setOnClickListener(v -> {
            dismiss();
        });

        addPlaces();
        addServices();
        addActivities();

        return binding.getRoot();
    }

    private void addPlaces() {
        for (String s : placesList) {
            LayoutInflater inflater = getLayoutInflater();
            View customEditTextLayout = inflater.inflate(R.layout.filter_check_layout, null);
            MaterialCheckBox checkbox = customEditTextLayout.findViewById(R.id.checkbox);
            TextView text = customEditTextLayout.findViewById(R.id.text);
            ImageView lock = customEditTextLayout.findViewById(R.id.lock);
            lock.setVisibility(View.GONE);
            text.setText(s);
            checkbox.setEnabled(true);
            binding.places.addView(customEditTextLayout);
        }
    }

    private void addServices() {
        for (String s : nearbyServices) {
            LayoutInflater inflater = getLayoutInflater();
            View customEditTextLayout = inflater.inflate(R.layout.filter_check_layout, null);
            MaterialCheckBox checkbox = customEditTextLayout.findViewById(R.id.checkbox);
            TextView text = customEditTextLayout.findViewById(R.id.text);
            ImageView lock = customEditTextLayout.findViewById(R.id.lock);
            text.setText(s);
            binding.services.addView(customEditTextLayout);
        }
    }

    private void addActivities() {
        for (String s : activitiesNearby) {
            LayoutInflater inflater = getLayoutInflater();
            View customEditTextLayout = inflater.inflate(R.layout.filter_check_layout, null);
            MaterialCheckBox checkbox = customEditTextLayout.findViewById(R.id.checkbox);
            TextView text = customEditTextLayout.findViewById(R.id.text);
            ImageView lock = customEditTextLayout.findViewById(R.id.lock);
            text.setText(s);
            binding.activities.addView(customEditTextLayout);
        }
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

//    StepClickListner listner = model -> {
//        ArrayList<AddStepsChildModel> list = Stash.getArrayList(Constants.Steps, AddStepsChildModel.class);
//        list.add(model);
//        Stash.put(Constants.Steps, list);
//        this.dismiss();
//    };

}
