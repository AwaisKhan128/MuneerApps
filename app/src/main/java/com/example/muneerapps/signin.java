package com.example.muneerapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import connection.Connection;

public class signin extends AppCompatActivity {
    Context mContext;
    WebView WebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mContext = this;
        WebView = findViewById(R.id.webView_signin);
        WebView.loadUrl(Connection.API+"/signin");
        WebSettings webSettings = WebView.getSettings();
        webSettings.setAllowContentAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        WebView.getSettings().setLoadWithOverviewMode(true);
        WebView.getSettings().setUseWideViewPort(true);

    }
}