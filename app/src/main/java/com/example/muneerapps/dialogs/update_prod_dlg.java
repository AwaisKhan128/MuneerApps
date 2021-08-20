package com.example.muneerapps.dialogs;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

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

public class update_prod_dlg extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public EditText prev_prod, new_prod, categ, price, unit;
    public Button update_Prod;
    public ListView previous_prod_list;

    public AutoCompleteTextView product_cat;
    public TextInputLayout product_cat_hint;



    public update_prod_dlg(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }
    Progress_Monitor progress_monitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_prod_dlg);
        progress_monitor = new Progress_Monitor(c);
        update_Prod = (Button) findViewById(R.id.update_Prod);
        prev_prod = findViewById(R.id.prev_prod);
        prev_prod.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                prev_prod.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text1));
            }
            if (!hasFocus){
                prev_prod.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));

            }
        });
        new_prod = findViewById(R.id.new_prod);
        new_prod.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                new_prod.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text1));
            }
            if (!hasFocus){
                new_prod.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));

            }
        });
//        categ = findViewById(R.id.categ);
        price = findViewById(R.id.price);
        price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    price.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text1));
                }
                if (!hasFocus){
                    price.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));

                }
            }
        });
        unit = findViewById(R.id.unit);
        unit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    unit.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text1));
                }
                if (!hasFocus){
                    unit.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));

                }
            }
        });
        previous_prod_list = findViewById(R.id.previous_prod_list);
        previous_prod_list.setVisibility(View.GONE);
        product_cat_hint = findViewById(R.id.product_cat_hint);

        product_cat = findViewById(R.id.product_cat);

        Update();

        update_Prod.setOnClickListener(view -> {

            Toaster("Updating Product");

            new Executing_update().execute(prev_prod.getText().toString().toLowerCase()
                    ,new_prod.getText().toString(),product_cat.getText().toString(),unit.getText().toString(),price.getText().toString());
        });

        prev_prod.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (prev_prod.getText().toString().length()==0)
                {
                    try
                    {
                        productAdapter.clear();
                        productAdapter.notifyDataSetChanged();
                    }

                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    return;
                }
                else {

                    previous_prod_list.setVisibility(View.VISIBLE);
                    new Populate_list().execute(charSequence.toString().toLowerCase());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        previous_prod_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                prev_prod.setText(adapterView.getAdapter().getItem(i).toString());
                previous_prod_list.setVisibility(View.GONE);
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

    ArrayList<String> product_array_list;
    ArrayAdapter<String> productAdapter;
    public class Populate_list extends AsyncTask<String,Void,Void>
    {

        @Override
        protected Void doInBackground(String... strings) {
            FirebaseDatabase.getInstance().getReference("Products").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    product_array_list = new ArrayList<>();
                    if (snapshot.hasChildren())
                    {
                        for (DataSnapshot snapshot1: snapshot.getChildren())
                        {
                            if (snapshot1.child("Name").getValue(String.class).toLowerCase().contains(strings[0]))
                            {
                                product_array_list.add(snapshot1.child("Name").getValue(String.class));
//                                        Toaster("Matched");
                            }
                        }
                        productAdapter = new ArrayAdapter<String>(
                                c.getApplicationContext(),
                                android.R.layout.simple_list_item_1,
                                product_array_list );

                        previous_prod_list.setAdapter(productAdapter);
                    }
                    else {
                        try
                        {
                            productAdapter.clear();
                            productAdapter.notifyDataSetChanged();
                        }

                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toaster(error.getMessage());
                    prev_prod.setBackground(ContextCompat.getDrawable(c,R.drawable.pass_unmatch));

                }
            });
            return null;
        }
    }

    public class Executing_update extends AsyncTask<String,Void,Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress_monitor.Setup_Progressing("Please Wait","Working in Progress");
        }

        @Override
        protected Void doInBackground(String... strings) {
            FirebaseDatabase.getInstance().getReference("Products")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren())
                    {
                        DatabaseReference ref = null;
                        for (DataSnapshot snapshot1: snapshot.getChildren())
                        {
                            if (snapshot1.child("Name").getValue(String.class).toLowerCase().contains(strings[0]))
                            {
                                ref = snapshot1.getRef();
                            }
                        }
                        if (strings[1].length()==0)
                        {
                            new_prod.setError("Missing");
                            new_prod.setBackground(ContextCompat.getDrawable(c,R.drawable.pass_unmatch));
                            return;
                        }
                        ref.child("Name").setValue(strings[1]);
                        if (strings[2].length()>0)
                        {
                            ref.child("Category").setValue(strings[2]);
                        }
                        if(strings[3].length()>0)
                        {
                            ref.child("Unit").setValue(strings[3]);
                        }
                        if(strings[4].length()>0)
                        {
                            ref.child("Price").setValue(strings[4]);
                        }

                        Reset_Inputs();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return null;
        }
    }

    private void Reset_Inputs() {
        prev_prod.setText("");
        new_prod.setText("");
//        categ.setText("");
        price.setText("");
        unit.setText("");
        prev_prod.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));
        new_prod.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));
        price.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));
        product_cat.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));
        product_cat_hint.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));
        unit.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));
        progress_monitor.Drop_Progressing();
        try
        {
            productAdapter.clear();
            productAdapter.notifyDataSetChanged();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    ArrayAdapter<String> ChaptersArrayAdapter;
    String Category;
    public void Update()
    {
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

    }

}
