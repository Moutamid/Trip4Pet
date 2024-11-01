package com.moutamid.trip4pet.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.databinding.ActivityContractBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ContractActivity extends AppCompatActivity {
    ActivityContractBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityContractBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.toolbar.title.setText(R.string.terms_of_services);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.webView.setWebViewClient(new WebViewClient()); // Open links within the WebView

        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        binding.webView.loadUrl("https://docs.google.com/document/d/e/2PACX-1vQBBalryFMlmEjLg3oVeNGbC7eqWXeEIGzGHOgGYzjOd1bTNQBtmEIw8Jc91obslirdHKHky7T5JWXy/pub");

    }

    private static final String TAG = "ContractActivity";
}