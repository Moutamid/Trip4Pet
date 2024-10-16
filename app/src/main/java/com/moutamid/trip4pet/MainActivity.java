package com.moutamid.trip4pet;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

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
import com.moutamid.trip4pet.Stash;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moutamid.trip4pet.databinding.ActivityMainBinding;
import com.moutamid.trip4pet.fragments.AroundMeFragment;
import com.moutamid.trip4pet.fragments.AroundPlaceFragment;
import com.moutamid.trip4pet.fragments.SettingFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    ActivityMainBinding binding;
    BillingProcessor bp;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(binding.getRoot());
        Constants.setLocale(getBaseContext(), Stash.getString(Constants.LANGUAGE, "en"));
        Constants.checkApp(this);
        requestLocationPermission();

        bp = BillingProcessor.newBillingProcessor(this, Constants.LICENSE_KEY, this);
        bp.initialize();

        checkSubscription();

        binding.bottomNav.setItemActiveIndicatorColor(ColorStateList.valueOf(getColor(R.color.white2)));
        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.aroundMe) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new AroundMeFragment()).commit();
                }
                if (id == R.id.aroundPlace) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new AroundPlaceFragment()).commit();
                }
                if (id == R.id.menu) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new SettingFragment()).commit();
                }
                return true;
            }
        });

        binding.bottomNav.setSelectedItemId(R.id.aroundMe);

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
}