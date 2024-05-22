package com.moutamid.trip4pet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.google.android.material.card.MaterialCardView;
import com.moutamid.trip4pet.databinding.ActivitySubscriptionBinding;

public class SubscriptionActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    String selectedPlan = Constants.VIP_YEAR;
    BillingProcessor bp;
    MaterialCardView selectedCard;
    ActivitySubscriptionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySubscriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bp = BillingProcessor.newBillingProcessor(this, Constants.LICENSE_KEY, this);
        bp.initialize();

        selectedCard = binding.annual;

        binding.close.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.annual.setOnClickListener(v -> {
            selectedPlan = Constants.VIP_YEAR;
            updateView(binding.annual);
        });

        binding.month.setOnClickListener(v -> {
            selectedPlan = Constants.VIP_MONTH;
            updateView(binding.month);
        });

        binding.start.setOnClickListener(v -> {
            bp.subscribe(SubscriptionActivity.this, selectedPlan);
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void updateView(MaterialCardView selected) {
        selectedCard.setCardBackgroundColor(getResources().getColor(R.color.grey3));
        selectedCard.setStrokeColor(getResources().getColor(R.color.grey));
        selectedCard = selected;
        selectedCard.setCardBackgroundColor(getResources().getColor(R.color.green_card));
        selectedCard.setStrokeColor(getResources().getColor(R.color.green));
    }


    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {
        Toast.makeText(this, "Thank you for being our valuable user", Toast.LENGTH_SHORT).show();
        onBackPressed();
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