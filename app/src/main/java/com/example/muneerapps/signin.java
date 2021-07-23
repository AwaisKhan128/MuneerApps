package com.example.muneerapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import connection.Connection;

public class signin extends AppCompatActivity {
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mContext = this;

    }


    public void go_forward(View view)
    {
        startActivity(new Intent(this,Selector.class));
    }
}