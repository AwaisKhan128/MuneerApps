package com.example.muneerapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.muneerapps.Transactions.Transaction_Adaptor;
import com.example.muneerapps.Transactions.Transaction_Class;
import com.example.muneerapps.dialogs.Addcash_dialog;
import com.example.muneerapps.dialogs.Category_dialog;
import com.example.muneerapps.dialogs.Subcash_dialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Payment extends AppCompatActivity {
    ArrayList<Transaction_Class> Data_log = new ArrayList<>();
    public RecyclerView recycler;
    Context context;
    LinearLayoutManager linearLayout;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_payment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                Refresh_Now();
                return true;
            case R.id.signout:
                SignOut_Now();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void Refresh_Now()
    {
        FirebaseDatabase.getInstance().getReference("Payments")
                .child("Transactions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren())
                {

                    new Manage_Users().execute();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void SignOut_Now()
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Payment.this, starter.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_sheet);
        recycler = findViewById(R.id.recycler);
        context = this;
        linearLayout = new LinearLayoutManager(context);
        recycler.setLayoutManager(linearLayout);
        recycler.addItemDecoration(new RecyclerMainSpacing(16));
        recycler.setVisibility(View.GONE);


        FirebaseDatabase.getInstance().getReference("Payments")
                .child("Transactions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren())
                {

                    new Manage_Users().execute();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void cashout(View view)
    {
        Subcash_dialog cdd=new Subcash_dialog(Payment.this);
        cdd.show();
    }

    public void cashin(View view)
    {
        Addcash_dialog cdd=new Addcash_dialog(Payment.this);
        cdd.show();

    }



    public class Manage_Users extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
            Data_log.clear();
            recycler.removeAllViewsInLayout();
            recycler.removeAllViews();
            recycler.getAdapter().notifyDataSetChanged();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            FirebaseDatabase.getInstance()
                    .getReference("Payments")
                    .child("Transactions").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren())
                    {
                        Transaction_Encoder transaction_encoder = new Transaction_Encoder();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            String Order = dataSnapshot.child("Type").getValue(String.class);
                            String Customer = dataSnapshot.child("Customer").getValue(String.class);
                            String Category = dataSnapshot.child("Category").getValue(String.class);
                            String Product = dataSnapshot.child("Product").getValue(String.class);
                            String Rate = dataSnapshot.child("Rate").getValue(String.class);
                            String User = dataSnapshot.child("User").getValue(String.class);
                            String Quantity = dataSnapshot.child("Quantity").getValue(String.class);
                            String Amount = dataSnapshot.child("Amount").getValue(String.class);
                            String Date_Time = dataSnapshot.child("Time").getValue(String.class);

                            if (Order.compareToIgnoreCase("Sell")==0)
                            Data_log.add(new Transaction_Class(Order,Customer,Category
                                    ,Product,Rate,User,Quantity,transaction_encoder.getDecoded(Amount),Date_Time, getResources().getColor(R.color.green)));
                            else
                            {
                                Data_log.add(new Transaction_Class(Order,Customer,Category
                                        ,Product,Rate,User,Quantity,transaction_encoder.getDecoded(Amount),Date_Time, getResources().getColor(R.color.red)));
                            }

                        }
                        try {
                            Transaction_Adaptor viewAdaptor = new Transaction_Adaptor(context, Data_log);
                            viewAdaptor.notifyDataSetChanged();
                            recycler.setVisibility(View.VISIBLE);
                            recycler.setAdapter(viewAdaptor);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            FirebaseDatabase.getInstance()
                    .getReference("Payments").child("Balance").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        String balance = new Transaction_Encoder().getDecoded(snapshot.getValue(String.class));
                        getSupportActionBar().setTitle("Balance Rs. "+balance);
                    }
                    else {
                        getSupportActionBar().setTitle("Balance Rs. "+0);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return null;
        }
    }



}