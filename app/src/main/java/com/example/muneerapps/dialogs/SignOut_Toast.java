package com.example.muneerapps.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.muneerapps.R;
import com.example.muneerapps.starter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignOut_Toast extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;



    public SignOut_Toast(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    Button yes,no;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.signout_toast);
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                c.startActivity(new Intent(c.getApplicationContext(), starter.class));
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
