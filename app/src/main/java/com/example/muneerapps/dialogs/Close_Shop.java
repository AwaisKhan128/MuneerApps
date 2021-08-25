package com.example.muneerapps.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.muneerapps.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Close_Shop extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;



    public Close_Shop(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }



    Switch switch1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.close_shop);
        switch1 = findViewById(R.id.switch1);

        FirebaseDatabase.getInstance().getReference("Shop")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            switch1.setChecked(snapshot.getValue(Boolean.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    switch1.setText("Shop Open");
                    FirebaseDatabase.getInstance().getReference("Shop").setValue(b);

                }
                else {
                    switch1.setText("Shop Closed");
                    FirebaseDatabase.getInstance().getReference("Shop").setValue(b);
                }

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
