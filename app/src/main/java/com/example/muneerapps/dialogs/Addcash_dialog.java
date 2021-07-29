package com.example.muneerapps.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.renderscript.Sampler;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Addcash_dialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;

    public TextInputLayout  select_categ,  select_rate;
    public EditText quatity_add,select_custom,select_prod;
    public TextView textView10;
    public Button button12;
    public ListView custom_list,product_list;
    public AutoCompleteTextView select_categ_Ac,select_rate_Ac;

    public ValueEventListener customer,product;
    private String Category,Rates;
    ArrayAdapter<String> ChaptersArrayAdapter, RateArrayAdapter;


    public void SetValueEvents()
    {
        customer = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        product = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }


    public Addcash_dialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_amount);
        button12 = (Button) findViewById(R.id.button12);
        textView10 = (TextView) findViewById(R.id.textView10);
        quatity_add = (EditText) findViewById(R.id.quatity_add);
        select_custom = (EditText) findViewById(R.id.select_cutom);
        select_categ = (TextInputLayout) findViewById(R.id.select_categ);
        select_prod = (EditText) findViewById(R.id.select_prod);
        select_rate = (TextInputLayout) findViewById(R.id.select_rate);
        custom_list = (ListView) findViewById(R.id.custom_list);
        product_list = (ListView) findViewById(R.id.product_list);
        select_categ_Ac = findViewById(R.id.select_categ_Ac);
        select_rate_Ac = findViewById(R.id.select_rate_Ac);


//        ------------Quantity Add---------------
        quatity_add.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (quatity_add.getText().toString().length()>0)
                {
                    if ( !select_rate_Ac.getText().toString().isEmpty() &&
                            select_rate_Ac.getText().toString().compareToIgnoreCase("Empty")!=0 && !quatity_add.getText().toString().isEmpty())
                    new Background_Calc().execute(select_rate_Ac.getText().toString(),charSequence.toString());
                    else
                    {
//                        textView10.setText("0");
                        new Background_Calc().execute("0","0");
                    }
                }
                else {
//                    textView10.setText("0");
                    new Background_Calc().execute(select_rate_Ac.getText().toString(),"0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });





//        -------------Rates----------------
        select_rate_Ac.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Rates =  ChaptersArrayAdapter.getItem(i).toString();

                select_categ_Ac.setText(ChaptersArrayAdapter.getItem(i).toString());

                FirebaseDatabase.getInstance().getReference("Products")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                List<String> rate = new ArrayList<String>();;
                                if (snapshot.hasChildren()) {
                                    try {

                                        for (DataSnapshot d : snapshot.getChildren()) {

                                            if (d.child("Category").getValue(String.class).compareToIgnoreCase(select_categ_Ac.getText().toString())==0
                                                    && d.child("Name").getValue(String.class).compareToIgnoreCase(select_prod.getText().toString())==0)
                                            {
                                                String data = String.valueOf((d.child("Price").getValue()));
                                                rate.add(data);
                                            }
                                            else {
                                                select_rate_Ac.setText("");
                                            }

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



                                    if (rate != null) {
                                        RateArrayAdapter = new ArrayAdapter<String>(c.getApplicationContext(), R.layout.option_item, rate);
                                    } else {
                                        List<String> none_values = Arrays.asList(new String[]{"Empty"});
                                        RateArrayAdapter = new ArrayAdapter<String>(c.getApplicationContext(), R.layout.option_item, none_values);
                                    }
                                    RateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            product_cat.setText(ChaptersArrayAdapter.getItem(0).toString(), false);
                                    select_rate_Ac.setAdapter(RateArrayAdapter);
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });
//        ----------------------------------


//        -----------------Customer ListView---------------------
        custom_list.setOnItemClickListener((adapterView, view, i, l) -> {
            select_custom.setText(adapterView.getAdapter().getItem(i).toString());
            custom_list.setVisibility(View.GONE);


        });
//        --------------------------------------------------------





//        ------------------Product ListView----------------------
        product_list.setOnItemClickListener((adapterView, view, i, l) -> {
            select_prod.setText(adapterView.getAdapter().getItem(i).toString());
            FirebaseDatabase.getInstance().getReference("Products")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<String> rate = new ArrayList<String>();;
                            if (snapshot.hasChildren()) {
                                try {

                                    for (DataSnapshot d : snapshot.getChildren()) {

                                        if (d.child("Category").getValue(String.class).compareToIgnoreCase(select_categ_Ac.getText().toString())==0
                                                && d.child("Name").getValue(String.class).compareToIgnoreCase(select_prod.getText().toString())==0)
                                        {
                                            String data = String.valueOf((d.child("Price").getValue()));
                                            rate.add(data);
                                        }
                                        else {
                                            select_rate_Ac.setText("");
                                        }

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



                                if (rate != null) {
                                    RateArrayAdapter = new ArrayAdapter<String>(c.getApplicationContext(), R.layout.option_item, rate);
                                } else {
                                    List<String> none_values = Arrays.asList(new String[]{"Empty"});
                                    RateArrayAdapter = new ArrayAdapter<String>(c.getApplicationContext(), R.layout.option_item, none_values);
                                }
                                RateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            product_cat.setText(ChaptersArrayAdapter.getItem(0).toString(), false);
                                select_rate_Ac.setAdapter(RateArrayAdapter);
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            product_list.setVisibility(View.GONE);

        });
//        ---------------------------------------------------------


        try{
        arrayAdapter.clear();
        arrayAdapter.notifyDataSetChanged();
        productAdapter.clear();
        productAdapter.notifyDataSetChanged();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

//        ----------Product---------------
        select_prod.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (select_prod.getText().toString().trim().compareToIgnoreCase("") == 0)
                {
                    try
                    {
                        productAdapter.clear();
                        productAdapter.notifyDataSetChanged();
                        product_list.setVisibility(View.GONE);
                    }

                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
                else
                {
                    try {
                        product_list.setVisibility(View.VISIBLE);
                        new Product_Class().execute(charSequence.toString().toLowerCase());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//        --------------------------------


//        ----------Customer---------------
        select_custom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                arrayAdapter.clear();
//                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (select_custom.getText().toString().trim().compareToIgnoreCase("") == 0)
                {
                    try
                    {
                        arrayAdapter.clear();
                        arrayAdapter.notifyDataSetChanged();
                        custom_list.setVisibility(View.GONE);

                    }

                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
                else
                 {
                    try {
                        custom_list.setVisibility(View.VISIBLE);
                        new Customers_Class().execute(charSequence.toString().toLowerCase());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//        ---------------------------------

//        -----------Categories----------
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
                            select_categ_Ac.setAdapter(ChaptersArrayAdapter);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        select_categ_Ac.setOnItemClickListener((adapterView, view, i, l) -> {
            Category =  ChaptersArrayAdapter.getItem(i).toString();

            select_categ_Ac.setText(ChaptersArrayAdapter.getItem(i).toString());


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
                                select_categ_Ac.setAdapter(ChaptersArrayAdapter);
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            FirebaseDatabase.getInstance().getReference("Products")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<String> rate = new ArrayList<String>();;
                            if (snapshot.hasChildren()) {
                                try {

                                    for (DataSnapshot d : snapshot.getChildren()) {

                                        if (d.child("Category").getValue(String.class).compareToIgnoreCase(select_categ_Ac.getText().toString())==0
                                                && d.child("Name").getValue(String.class).compareToIgnoreCase(select_prod.getText().toString())==0)
                                        {
                                            String data = String.valueOf((d.child("Price").getValue()));
                                            rate.add(data);
                                        }
                                        else {
                                            select_rate_Ac.setText("");
                                        }

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



                                if (rate != null) {
                                    RateArrayAdapter = new ArrayAdapter<String>(c.getApplicationContext(), R.layout.option_item, rate);
                                } else {
                                    List<String> none_values = Arrays.asList(new String[]{"Empty"});
                                    RateArrayAdapter = new ArrayAdapter<String>(c.getApplicationContext(), R.layout.option_item, none_values);
                                }
                                RateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            product_cat.setText(ChaptersArrayAdapter.getItem(0).toString(), false);
                                select_rate_Ac.setAdapter(RateArrayAdapter);
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



//                product_cat_hint.setPlaceholderText();
        });
//        -------------------------------

        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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


    List<String> your_array_list;
    ArrayAdapter<String> arrayAdapter;
    class Customers_Class extends AsyncTask<String, Void, Void>
    {

        @Override
        protected Void doInBackground(final String... voids) {

            FirebaseDatabase.getInstance().getReference("Customers")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    your_array_list = new ArrayList<>();

                    if (snapshot.exists())
                    {
                        for(DataSnapshot d : snapshot.getChildren())
                        {
                            if (d.child("Name").getValue(String.class).toLowerCase().contains(voids[0]))
                            {
                                your_array_list.add(d.child("Name").getValue(String.class));
//                                Toaster("Matched");
                            }
                        }

                        arrayAdapter = new ArrayAdapter<String>(
                                c.getApplicationContext(),
                                android.R.layout.simple_list_item_1,
                                your_array_list );

                        custom_list.setAdapter(arrayAdapter);
                    }
                    else {
                        try
                        {
                            arrayAdapter.clear();
                            arrayAdapter.notifyDataSetChanged();
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
            return null;
        }
    }


    List<String> product_array_list;
    ArrayAdapter<String> productAdapter;
    class Product_Class extends AsyncTask<String, Void, Void>
    {

        @Override
        protected Void doInBackground(final String... voids) {

            FirebaseDatabase.getInstance().getReference("Products")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            product_array_list = new ArrayList<>();

                            if (snapshot.exists())
                            {
                                for(DataSnapshot d : snapshot.getChildren())
                                {
                                    if (d.child("Name").getValue(String.class).toLowerCase().contains(voids[0]))
                                    {
                                        product_array_list.add(d.child("Name").getValue(String.class));
//                                        Toaster("Matched");
                                    }
                                }

                                productAdapter = new ArrayAdapter<String>(
                                        c.getApplicationContext(),
                                        android.R.layout.simple_list_item_1,
                                        product_array_list );

                                product_list.setAdapter(productAdapter);

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

                        }
                    });
            FirebaseDatabase.getInstance().getReference("Products")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<String> rate = new ArrayList<String>();;
                            if (snapshot.hasChildren()) {
                                try {

                                    for (DataSnapshot d : snapshot.getChildren()) {

                                        if (d.child("Category").getValue(String.class).compareToIgnoreCase(select_categ_Ac.getText().toString())==0
                                                && d.child("Name").getValue(String.class).compareToIgnoreCase(select_prod.getText().toString())==0)
                                        {
                                            String data = String.valueOf((d.child("Price").getValue()));
                                            rate.add(data);
                                        }
                                        else {
                                            select_rate_Ac.setText("");
                                        }

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



                                if (rate != null) {
                                    RateArrayAdapter = new ArrayAdapter<String>(c.getApplicationContext(), R.layout.option_item, rate);
                                } else {
                                    List<String> none_values = Arrays.asList(new String[]{"Empty"});
                                    RateArrayAdapter = new ArrayAdapter<String>(c.getApplicationContext(), R.layout.option_item, none_values);
                                }
                                RateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            product_cat.setText(ChaptersArrayAdapter.getItem(0).toString(), false);
                                select_rate_Ac.setAdapter(RateArrayAdapter);
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            return null;
        }
    }



    class Background_Calc extends AsyncTask<String,Void,Void>
    {

        @Override
        protected Void doInBackground(String... strings) {

            String val1 = strings[0];
            String val2 = strings[1];
            try{
            double final_val = Double.parseDouble(val1) * Double.parseDouble(val2);
            if (final_val!=0)
            textView10.setText(""+(final_val));
            else
            {
                textView10.setText("0");
            }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }
    }
}
