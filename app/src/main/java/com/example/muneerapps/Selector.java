package com.example.muneerapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.muneerapps.dialogs.Category_dialog;
import com.example.muneerapps.dialogs.Customer_dialog;
import com.example.muneerapps.dialogs.Product_dialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;

public class Selector extends AppCompatActivity {
    Button button5,button6,button7,button8;


    public void Firebase_Selector()
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference db = firebaseDatabase.getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Access");
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("Category").getValue(Boolean.class))
                    {
                        button6.setVisibility(View.VISIBLE);
                    }

                    if (snapshot.child("Customer").getValue(Boolean.class))
                    {
                        button5.setVisibility(View.VISIBLE);
                    }

                    if (snapshot.child("Payment").getValue(Boolean.class))
                    {
                        button8.setVisibility(View.VISIBLE);
                    }

                    if (snapshot.child("Product").getValue(Boolean.class))
                    {
                        button7.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectors);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button5.setVisibility(View.GONE);
        button6.setVisibility(View.GONE);
        button7.setVisibility(View.GONE);
        button8.setVisibility(View.GONE);
        Firebase_Selector();



    }

    public void create_custom(View view)
    {
        Customer_dialog cdd=new Customer_dialog(Selector.this);
        cdd.show();

    }

    public void create_categ(View view)
    {
        Category_dialog cdd=new Category_dialog(Selector.this);
        cdd.show();
    }

    public void create_pay(View view)
    {
        startActivity(new Intent(Selector.this,Payment.class));
    }

    public void create_prod(View view)
    {
        Product_dialog cdd=new Product_dialog(Selector.this);
        cdd.show();
    }

}