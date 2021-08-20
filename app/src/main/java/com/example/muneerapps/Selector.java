package com.example.muneerapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.muneerapps.dialogs.Category_dialog;
import com.example.muneerapps.dialogs.Customer_dialog;
import com.example.muneerapps.dialogs.Deadline;
import com.example.muneerapps.dialogs.Product_dialog;
import com.example.muneerapps.dialogs.Supplier_dialog;
import com.example.muneerapps.dialogs.Update_Customer;
import com.example.muneerapps.dialogs.Update_Supplier;
import com.example.muneerapps.dialogs.update_prod_dlg;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;

public class Selector extends AppCompatActivity {
    Button button5,button6,button7,button8,button22,button23,button19,button18;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_selector, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference("Deadline").child("Key")
                .addListenerForSingleValueEvent(
                        new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            boolean key = snapshot.getValue(Boolean.class);
                            if (key)
                            {
                                Reset_deadline();
                            }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.signout) {
            SignOut_Now();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
    public void Refresh_Now()
    {

    }

    public void Firebase_Selector()
    {
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference db = firebaseDatabase.getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Access");
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("Category").getValue(Boolean.class))
                    {
                        button6.setVisibility(View.VISIBLE);
                        progressBar3.setVisibility(View.GONE);
                    }
                    else if (!(snapshot.child("Category").getValue(Boolean.class)))
                    {
                        button6.setVisibility(View.GONE);
                        progressBar3.setVisibility(View.GONE);
                    }

                    if (snapshot.child("Product_Update").getValue(Boolean.class))
                    {
                        button18.setVisibility(View.VISIBLE);
                        progressBar3.setVisibility(View.GONE);
                    }
                    else if (!(snapshot.child("Product_Update").getValue(Boolean.class)))
                    {
                        button18.setVisibility(View.GONE);
                        progressBar3.setVisibility(View.GONE);
                    }

                    if (snapshot.child("Customer_Update").getValue(Boolean.class))
                    {
                        button19.setVisibility(View.VISIBLE);
                        progressBar3.setVisibility(View.GONE);
                    }
                    else if (!(snapshot.child("Customer_Update").getValue(Boolean.class)))
                    {
                        button19.setVisibility(View.GONE);
                        progressBar3.setVisibility(View.GONE);
                    }

                    if (snapshot.child("Supplier").getValue(Boolean.class))
                    {
                        button22.setVisibility(View.VISIBLE);
                        progressBar3.setVisibility(View.GONE);
                    }
                    else if (!(snapshot.child("Supplier").getValue(Boolean.class)))
                    {
                        button22.setVisibility(View.GONE);
                        progressBar3.setVisibility(View.GONE);
                    }

                    if (snapshot.child("Supplier_Update").getValue(Boolean.class))
                    {
                        button23.setVisibility(View.VISIBLE);
                        progressBar3.setVisibility(View.GONE);
                    }
                    else if (!(snapshot.child("Supplier_Update").getValue(Boolean.class)))
                    {
                        button23.setVisibility(View.GONE);
                        progressBar3.setVisibility(View.GONE);
                    }




                    if (snapshot.child("Customer").getValue(Boolean.class))
                    {
                        button5.setVisibility(View.VISIBLE);
                        progressBar3.setVisibility(View.GONE);
                    }
                    else if (!(snapshot.child("Customer").getValue(Boolean.class)))
                    {
                        button5.setVisibility(View.GONE);
                        progressBar3.setVisibility(View.GONE);
                    }

                    if (snapshot.child("Payment").getValue(Boolean.class))
                    {
                        button8.setVisibility(View.VISIBLE);
                        progressBar3.setVisibility(View.GONE);
                    }
                    else if (!(snapshot.child("Payment").getValue(Boolean.class)))
                    {
                        button8.setVisibility(View.GONE);
                        progressBar3.setVisibility(View.GONE);
                    }


                    if (snapshot.child("Product").getValue(Boolean.class))
                    {
                        button7.setVisibility(View.VISIBLE);
                        progressBar3.setVisibility(View.GONE);

                    }
                    else if (!(snapshot.child("Product").getValue(Boolean.class)))
                    {
                        button7.setVisibility(View.GONE);
                        progressBar3.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(),"Database error refresh again",Toast.LENGTH_SHORT).show();
                    progressBar3.setVisibility(View.GONE);
                }
            });
        }
        else {
            SignOut_Now();
        }
    }

    public void SignOut_Now()
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Selector.this, starter.class));
    }

    private void Reset_deadline() {

        Deadline cdd=new Deadline(Selector.this);
        cdd.setCanceledOnTouchOutside(false);
        cdd.setCancelable(false);
        cdd.show();

    }

    ProgressBar progressBar3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Customer Portal");
        setContentView(R.layout.selectors);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);

        button18 = (Button) findViewById(R.id.button18);//Update Product
        button19 = (Button) findViewById(R.id.button19);//Update Customers
        button22 = (Button) findViewById(R.id.button22);//Create Supplier
        button23 = (Button) findViewById(R.id.button23);//Update Supplier

        progressBar3 = findViewById(R.id.progressBar3);
        progressBar3.setVisibility(View.VISIBLE);
        button5.setVisibility(View.GONE);
        button6.setVisibility(View.GONE);
        button7.setVisibility(View.GONE);
        button8.setVisibility(View.GONE);

        button18.setVisibility(View.GONE);
        button19.setVisibility(View.GONE);
        button22.setVisibility(View.GONE);
        button23.setVisibility(View.GONE);


        FirebaseApp.initializeApp(this);
        Firebase_Selector();



    }


    public void create_supplier(View view)
    {
        Supplier_dialog cdd=new Supplier_dialog(Selector.this);
        cdd.show();
    }
    public void update_supplier(View view)
    {
        Update_Supplier cdd=new Update_Supplier(Selector.this);
        cdd.show();
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

    public void update_prod(View view)
    {
        update_prod_dlg cdd=new update_prod_dlg(Selector.this);
        cdd.show();
    }

    public void update_customer(View view)
    {
        Update_Customer cdd=new Update_Customer(Selector.this);
        cdd.show();
    }


}