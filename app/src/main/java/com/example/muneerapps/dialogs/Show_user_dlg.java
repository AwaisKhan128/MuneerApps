package com.example.muneerapps.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.muneerapps.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class Show_user_dlg extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public RadioButton radioButton5,radioButton6
            ,radioButton7,radioButton8,radioButton15,radioButton16,radioButton17,radioButton18,radioButton19;
    public Button cancel,ok;


    public Show_user_dlg(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }


    public void RadioActivities(View view) {
        switch (view.getId())
        {
            case R.id.radioButton5:
                radioButton5.toggle();
            case R.id.radioButton6:
                radioButton6.toggle();
            case R.id.radioButton7:
                radioButton7.toggle();
            case R.id.radioButton8:
                radioButton8.toggle();
        }

    }

    boolean[] radios = {false,false,false,false, false,false,false,false,false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.show_user);
        radioButton5 = findViewById(R.id.radioButton5);
        radioButton6 = findViewById(R.id.radioButton6);
        radioButton7 = findViewById(R.id.radioButton7);
        radioButton8 = findViewById(R.id.radioButton8);

        radioButton15 = findViewById(R.id.radioButton15);//Supplier
        radioButton16 = findViewById(R.id.radioButton16);//Supplier Update
        radioButton17 = findViewById(R.id.radioButton17);//Customer Update
        radioButton18 = findViewById(R.id.radioButton18);//Product Update
        radioButton19 = findViewById(R.id.radioButton19);//Paymennt Overview




        cancel = findViewById(R.id.button14);
        ok = findViewById(R.id.button15);

        radioButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radios[0] = !radios[0];
                radioButton5.setChecked(radios[0]);
            }
        });

        radioButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radios[1] = !radios[1];
                radioButton6.setChecked(radios[1]);
            }
        });

        radioButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radios[2] = !radios[2];
                radioButton7.setChecked(radios[2]);
            }
        });

        radioButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radios[3] = !radios[3];
                radioButton8.setChecked(radios[3]);
            }
        });


        radioButton15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radios[4] = !radios[4];
                radioButton15.setChecked(radios[4]);
            }
        });

        radioButton16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radios[5] = !radios[5];
                radioButton16.setChecked(radios[5]);
            }
        });

        radioButton17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radios[6] = !radios[6];
                radioButton17.setChecked(radios[6]);
            }
        });

        radioButton18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radios[7] = !radios[7];
                radioButton18.setChecked(radios[7]);
            }
        });
        radioButton19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radios[8] = !radios[8];
                radioButton19.setChecked(radios[8]);
            }
        });


        try{
            FirebaseDatabase.getInstance().getReference("Users")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren())
                    {
                        SharedPreferences pref = c.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                        String name =pref.getString("User_name_key", null);         // getting Stringdescription

                        for (DataSnapshot db:snapshot.getChildren())
                        {

                            if (db.child("Name").getValue(String.class).compareToIgnoreCase(name)==0)
                            {
                                radioButton5.setChecked(db.child("Access").child("Category").getValue(Boolean.class));
                                radioButton6.setChecked(db.child("Access").child("Customer").getValue(Boolean.class));
                                radioButton7.setChecked(db.child("Access").child("Payment").getValue(Boolean.class));
                                radioButton8.setChecked(db.child("Access").child("Product").getValue(Boolean.class));

                                radioButton15.setChecked(db.child("Access").child("Supplier").getValue(Boolean.class));
                                radioButton16.setChecked(db.child("Access").child("Supplier_Update").getValue(Boolean.class));
                                radioButton17.setChecked(db.child("Access").child("Customer_Update").getValue(Boolean.class));
                                radioButton18.setChecked(db.child("Access").child("Product_Update").getValue(Boolean.class));

                                if (db.child("Access").hasChild("Payment_Overview")) {
                                    radioButton19.setChecked(db.child("Access").child("Payment_Overview").getValue(Boolean.class));
                                }



                                radios[0] = db.child("Access").child("Category").getValue(Boolean.class);
                                radios[1] = db.child("Access").child("Customer").getValue(Boolean.class);
                                radios[2] = db.child("Access").child("Payment").getValue(Boolean.class);
                                radios[3] = db.child("Access").child("Product").getValue(Boolean.class);

                                radios[4] = db.child("Access").child("Supplier").getValue(Boolean.class);
                                radios[5] = db.child("Access").child("Supplier_Update").getValue(Boolean.class);
                                radios[6] = db.child("Access").child("Customer_Update").getValue(Boolean.class);
                                radios[7] = db.child("Access").child("Product_Update").getValue(Boolean.class);
                                radios[8] = db.child("Access").child("Payment_Overview").getValue(Boolean.class);
                            }

                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



        cancel.setOnClickListener(view -> dismiss());

        ok.setOnClickListener(view -> FirebaseDatabase.getInstance()
                .getReference("Users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren())
                        {
                            SharedPreferences pref = c.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                            String name =pref.getString("User_name_key", null);         // getting Stringdescription
                            for(DataSnapshot dataSnapshot:snapshot.getChildren())
                            {

                                if (dataSnapshot.child("Name")
                                        .getValue(String.class).compareToIgnoreCase(name)==0)
                                {
                                    DatabaseReference df = dataSnapshot.getRef().child("Access");
                                    df.child("Category").setValue(radioButton5.isChecked());
                                    df.child("Customer").setValue(radioButton6.isChecked());
                                    df.child("Payment").setValue(radioButton7.isChecked());
                                    df.child("Product").setValue(radioButton8.isChecked());

                                    df.child("Supplier").setValue(radioButton15.isChecked());
                                    df.child("Supplier_Update").setValue(radioButton16.isChecked());
                                    df.child("Customer_Update").setValue(radioButton17.isChecked());
                                    df.child("Product_Update").setValue(radioButton18.isChecked());

                                    df.child("Payment_Overview").setValue(radioButton19.isChecked());

                                }
                            }
                            dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }));
    }



    public void Toaster(String s)
    {
        Toast.makeText(c.getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

    }



}
