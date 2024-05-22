package com.moutamid.trip4pet;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.anjlab.android.iab.v3.SkuDetails;
import com.fxn.stash.Stash;
import com.moutamid.trip4pet.databinding.ActivityMainBinding;
import com.moutamid.trip4pet.fragments.AroundMeFragment;
import com.moutamid.trip4pet.fragments.SettingFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    ActivityMainBinding binding;
    BillingProcessor bp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        requestLocationPermission();

        bp = BillingProcessor.newBillingProcessor(this, Constants.LICENSE_KEY, this);
        bp.initialize();

        checkSubscription();

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        // Optionally, you can set icons for the tabs
        binding.tabLayout.getTabAt(0).setIcon(R.drawable.compass);
        binding.tabLayout.getTabAt(1).setIcon(R.drawable.bars);
    }

    private void requestLocationPermission() {
        shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 111);
    }

    private void checkSubscription() {
        ArrayList<String> ids = new ArrayList<>();
        ids.add(Constants.VIP_YEAR);
        ids.add(Constants.VIP_MONTH);
        bp.getSubscriptionsListingDetailsAsync(ids, new BillingProcessor.ISkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(@Nullable List<SkuDetails> products) {
                Log.d("PURSS", "Size : " + products.size());
                for (int i = 0; i < products.size(); i++) {
                    boolean isSub = products.get(i).isSubscription;
                    Stash.put(Constants.ISVIP, isSub);
                }
            }

            @Override
            public void onSkuDetailsError(String error) {

            }
        });

        Stash.put(Constants.ISVIP, bp.isSubscribed(Constants.VIP_MONTH) || bp.isSubscribed(Constants.VIP_YEAR));

        if (!Stash.getBoolean(Constants.ISVIP)) {
            Map<String, Object> map = new HashMap<>();
            map.put("vip", false);
            Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid()).updateChildren(map);
        }
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    public class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new AroundMeFragment(); // Replace with your MapFragment
                case 1:
                    return new SettingFragment(); // Replace with your MenuFragment
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2; // Number of tabs
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Return tab titles here if needed
            return null;
        }
    }

}