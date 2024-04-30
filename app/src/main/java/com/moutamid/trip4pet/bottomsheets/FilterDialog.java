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
import com.moutamid.trip4pet.models.FilterModel;

import java.util.ArrayList;

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
    ArrayList<FilterModel> service = new ArrayList<>();
    ArrayList<FilterModel> activities = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FilterFragmentBinding.inflate(getLayoutInflater(), container, false);

        binding.apply.setOnClickListener(v -> {
            dismiss();
        });

        addPlaces();
        addActivities();
        service.add(new FilterModel("Electricity (access possible)", R.drawable.plug_circle_check_solid));
        service.add(new FilterModel("Drinking water", R.drawable.faucet_drip_solid));
        service.add(new FilterModel("Black water", R.drawable.toilet_solid));
        service.add(new FilterModel("Wastewater", R.drawable.dumpster_solid));
        service.add(new FilterModel("Waste container", R.drawable.dumpster_solid));
        service.add(new FilterModel("Bakery", R.drawable.cake_candles_solid));
        service.add(new FilterModel("Public toilets", R.drawable.restroom_solid));
        service.add(new FilterModel("Showers (possible access)", R.drawable.shower_solid));
        service.add(new FilterModel("Internet access via WiFi", R.drawable.wifi_solid));
        service.add(new FilterModel("Winter-caravanning", R.drawable.snowman_solid));
        service.add(new FilterModel("Pets allowed", R.drawable.dog_solid));
        service.add(new FilterModel("Swimming pool", R.drawable.person_swimming_solid));
        service.add(new FilterModel("Laundry", R.drawable.jug_detergent_solid));
        service.add(new FilterModel("LPG station", R.drawable.gas_pump_solid));
        service.add(new FilterModel("Bottled gas service", R.drawable.fire_flame_simple_solid));
        service.add(new FilterModel("3G/4G internet", R.drawable.signal_solid));
        service.add(new FilterModel("Washing for motorhome", R.drawable.soap_solid));

        addServices();

        return binding.getRoot();
    }

    private void addPlaces() {
        for (String s : placesList) {
            LayoutInflater inflater = getLayoutInflater();
            View customEditTextLayout = inflater.inflate(R.layout.filter_check_layout, null);
            MaterialCheckBox checkbox = customEditTextLayout.findViewById(R.id.checkbox);
            TextView text = customEditTextLayout.findViewById(R.id.text);
            ImageView lock = customEditTextLayout.findViewById(R.id.lock);
            ImageView image = customEditTextLayout.findViewById(R.id.image);

            image.setVisibility(View.GONE);
            lock.setVisibility(View.GONE);
            text.setText(s);
            checkbox.setEnabled(true);
            binding.places.addView(customEditTextLayout);
        }
    }

    private void addServices() {
        for (FilterModel s : service) {
            LayoutInflater inflater = getLayoutInflater();
            View customEditTextLayout = inflater.inflate(R.layout.filter_check_layout, null);

            MaterialCheckBox checkbox = customEditTextLayout.findViewById(R.id.checkbox);
            ImageView image = customEditTextLayout.findViewById(R.id.image);
            TextView text = customEditTextLayout.findViewById(R.id.text);
            ImageView lock = customEditTextLayout.findViewById(R.id.lock);

            image.setImageResource(s.icon);
            text.setText(s.name);

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
