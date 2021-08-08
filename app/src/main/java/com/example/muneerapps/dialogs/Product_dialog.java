package com.example.muneerapps.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Product_dialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public EditText title,description;
    public RadioButton radioButton,radioButton2,radioButton3;


    public Button button11;

    public AutoCompleteTextView product_cat;
    public TextInputLayout product_cat_hint;
    public EditText product_name, product_unit, price;

    private String Category;

    public Product_dialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }
    ArrayAdapter<String> ChaptersArrayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_product);
        product_cat = findViewById(R.id.product_cat);
        product_name = (EditText) findViewById(R.id.product_name);
        product_unit = (EditText) findViewById(R.id.product_unit);
        price = (EditText) findViewById(R.id.price);
        product_cat_hint = findViewById(R.id.product_cat_hint);
        button11 = (Button) findViewById(R.id.button11);

        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(product_cat.getText().toString().length()>0
                        && product_name.getText().toString().length()>0
                        && product_unit.getText().toString().length()>0
                        && price.getText().toString().length()>0)
                {
                    FirebaseDatabase.getInstance().getReference("Products")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChildren()) {

                                boolean isProduct = false;

                                for (DataSnapshot dataSnapshot :snapshot.getChildren())
                                {
                                    if (dataSnapshot.child("Name")
                                            .getValue(String.class).compareToIgnoreCase(product_name.getText().toString())==0)
                                    {
                                        isProduct= true;
                                    }
                                }

                                if (!isProduct) {
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                            .getReference("Products")
                                            .child(String.valueOf(snapshot.getChildrenCount()));


                                    databaseReference.child("Category").setValue(product_cat.getText().toString());
                                    databaseReference.child("Name").setValue(product_name.getText().toString());
                                    databaseReference.child("Unit").setValue(product_unit.getText().toString());
                                    databaseReference.child("Price").setValue(price.getText().toString());

                                    Toaster("Success");
                                    product_cat.setText("");
                                    product_name.setText("");
                                    product_unit.setText("");
                                    price.setText("");
                                }
                                else {
                                    Toaster("Product Name Already exist");
                                    product_name.setError("Matched");
                                }


                            } else {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                        .getReference("Products")
                                        .child(String.valueOf(0));


                                databaseReference.child("Category").setValue(product_cat.getText().toString());
                                databaseReference.child("Name").setValue(product_name.getText().toString());
                                databaseReference.child("Unit").setValue(product_unit.getText().toString());
                                databaseReference.child("Price").setValue(price.getText().toString());

                                Toaster("Success");
                                product_cat.setText("");
                                product_name.setText("");
                                product_unit.setText("");
                                price.setText("");
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            Toaster("Failed to Saved Try again");
                        }
                    });

                }
                else
                {
                    product_cat.setError("Check Empty");
                    product_name.setError("Check Empty");
                    product_unit.setError("Check Empty");
                    price.setError("Check Empty");
                }
            }
        });

//        <Populate Category with Lists>

        FirebaseDatabase.getInstance().getReference("Categories")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> Categories = new ArrayList<String>();;
                        if (snapshot.hasChildren()) {
                            try {

                                for (DataSnapshot d : snapshot.getChildren()) {

                                    String data = String.valueOf((d.getValue()));
//                                    Toaster(data);
                                    Categories.add(data);

                                }


                            }
                            catch (Exception e)
                            {
//                                e.printStackTrace();
                                Log.e("Error is",e.getLocalizedMessage());
//                                Toaster(e.getMessage());
//                                if(Categories!=null)
//                                Log.e("Categories are ",Categories.get(0) +" "+ Categories.get(1));

                            }



                            if (Categories != null) {
                                ChaptersArrayAdapter = new ArrayAdapter<String>(c.getApplicationContext(), R.layout.option_item, Categories);
                            } else {
                                List<String> none_values = Arrays.asList(new String[]{"Empty"});
                                ChaptersArrayAdapter = new ArrayAdapter<String>(c.getApplicationContext(), R.layout.option_item, none_values);
                            }
                            ChaptersArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            product_cat.setText(ChaptersArrayAdapter.getItem(0).toString(), false);
                            product_cat.setAdapter(ChaptersArrayAdapter);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });







        product_cat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Category =  ChaptersArrayAdapter.getItem(i).toString();
//               product_cat_hint.setHint(ChaptersArrayAdapter.getItem(i).toString());
//                product_cat_hint.setPlaceholderText(ChaptersArrayAdapter.getItem(i).toString());
//                product_cat.setPlaceholderText(ChaptersArrayAdapter.getItem(i).toString());
                product_cat.setText(ChaptersArrayAdapter.getItem(i).toString());


                FirebaseDatabase.getInstance().getReference("Categories")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                List<String> Categories = new ArrayList<String>();;
                                if (snapshot.hasChildren()) {
                                    try {

                                        for (DataSnapshot d : snapshot.getChildren()) {

                                            String data = String.valueOf((d.getValue()));
//                                    Toaster(data);
                                            Categories.add(data);

                                        }


                                    }
                                    catch (Exception e)
                                    {
//                                e.printStackTrace();
                                        Log.e("Error is",e.getLocalizedMessage());
//                                Toaster(e.getMessage());
//                                if(Categories!=null)
//                                Log.e("Categories are ",Categories.get(0) +" "+ Categories.get(1));

                                    }



                                    if (Categories != null) {
                                        ChaptersArrayAdapter = new ArrayAdapter<String>(c.getApplicationContext(), R.layout.option_item, Categories);
                                    } else {
                                        List<String> none_values = Arrays.asList(new String[]{"Empty"});
                                        ChaptersArrayAdapter = new ArrayAdapter<String>(c.getApplicationContext(), R.layout.option_item, none_values);
                                    }
                                    ChaptersArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                    product_cat.setText(ChaptersArrayAdapter.getItem(0).toString(), false);
                                    product_cat.setAdapter(ChaptersArrayAdapter);
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



//                product_cat_hint.setPlaceholderText();
            }
        });


//        <------------------>







    }

    public void Toaster(String s)
    {
        Toast.makeText(c.getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

    }
}
