package com.example.muneerapps.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.example.muneerapps.R;
import com.example.muneerapps.starter;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;

public class ShopClosed extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;



    public ShopClosed(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }



    Button button25;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shop_close);
        button25 = findViewById(R.id.button25);
        button25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getContext(), starter.class);
                getContext().startActivity(i);
            }
        });




    }



    public void Toaster(String s)
    {
        Toast.makeText(c.getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

    }



}
