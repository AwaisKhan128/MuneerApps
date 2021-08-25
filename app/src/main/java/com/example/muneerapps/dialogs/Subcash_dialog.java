package com.example.muneerapps.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.core.content.ContextCompat;

import com.example.muneerapps.R;
import com.example.muneerapps.Transaction_Encoder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class Subcash_dialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;

    public TextInputLayout  select_categ,  select_rate;
    public EditText select_custom,select_prod,partial_amount;
    public TextView textView10;
    public Button button12;
    public ListView custom_list,product_list;
    public AutoCompleteTextView select_categ_Ac;

    public ValueEventListener customer,product;
    private String Category,Rates;
    ArrayAdapter<String> ChaptersArrayAdapter, RateArrayAdapter;
    public ListView products_list_update;
    Progress_Monitor progress_monitor;
    boolean isCustomSelect = false;

    public RadioButton radioButton9, radioButton10, radioButton11;


    public Subcash_dialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    ArrayList arrayList_products = new ArrayList();
    ArrayAdapter<String> productAdapter_new ;
    //    HashMap mMap = new HashMap();
    private Map<String, Float> mMap = new HashMap<String, Float>();
    TextView textView12,textView14,textView8,textView9,textView11,textView13;
    boolean owner = false;
    private String Retrieve_preference(String key)
    {
        SharedPreferences pref = c.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        return pref.getString(key, "");         // getting you_bool
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sub_amount);
        if (Retrieve_preference("Owner").compareToIgnoreCase("no")==0)
        {
            owner = false;
        }
        else
        {
            owner = true;
        }
        button12 = (Button) findViewById(R.id.button12);
        textView10 = (TextView) findViewById(R.id.textView10);
        textView10.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().length()>0)
                {
                    if(Float.parseFloat(charSequence.toString())>0) {
                        new CalcFin_Due().execute(charSequence.toString(),textView12.getText().toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        textView12 = (TextView) findViewById(R.id.textView12);
        textView14 = (TextView) findViewById(R.id.textView14);

        textView9 = (TextView) findViewById(R.id.textView9);
        textView11 = (TextView) findViewById(R.id.textView11);
        textView13 = (TextView) findViewById(R.id.textView13);
        textView8 = (TextView) findViewById(R.id.textView8);

        progress_monitor = new Progress_Monitor(c);
        select_custom = (EditText) findViewById(R.id.select_cutom);
        select_custom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    select_custom.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text1));
                }
                if (!hasFocus){
                    select_custom.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));

                }
            }
        });
        select_categ = (TextInputLayout) findViewById(R.id.select_categ);
        select_prod = (EditText) findViewById(R.id.select_prod);
        select_prod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    select_prod.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text1));
                }
                if (!hasFocus){
                    select_prod.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));

                }
            }
        });

        custom_list = (ListView) findViewById(R.id.custom_list);
        product_list = (ListView) findViewById(R.id.product_list);
        select_categ_Ac = findViewById(R.id.select_categ_Ac);
        products_list_update = findViewById(R.id.products_list);
        radioButton9 = findViewById(R.id.radioButton9);
        radioButton10 = findViewById(R.id.radioButton10);
        radioButton11 = findViewById(R.id.radioButton11);

        if (owner)
        {
            radioButton11.setVisibility(View.GONE);
            radioButton9.setVisibility(View.GONE);
            select_categ.setVisibility(View.GONE);
            select_prod.setVisibility(View.GONE);
            textView8.setText("Send");
            button12.setText("Sent");
            textView9.setVisibility(View.GONE);
            textView10.setVisibility(View.GONE);
        }
        else {

            textView12.setVisibility(View.GONE);
            textView14.setVisibility(View.GONE);
            textView11.setVisibility(View.GONE);
            textView13.setVisibility(View.GONE);
        }

        partial_amount = findViewById(R.id.partial_amount);
        partial_amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    partial_amount.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text1));
                }
                if (!hasFocus){
                    partial_amount.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));

                }
            }
        });
        partial_amount.setVisibility(View.GONE);
        partial_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().length()>0) {
                    try {
                        if (Float.parseFloat(charSequence.toString()) > 0 && Float.parseFloat(textView14.getText().toString()) > 0) {
                            if (Float.parseFloat(textView14.getText().toString()) >= Float.parseFloat(charSequence.toString())) {
                                partial_amount.setBackground(ContextCompat.getDrawable(c, R.drawable.pass_match));

                            } else {
                                partial_amount.setBackground(ContextCompat.getDrawable(c, R.drawable.pass_unmatch));
                            }
                        }


                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        Log.e("Error",e.getLocalizedMessage());
                    }
                }
                else{



                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        radioButton9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButton9.setChecked(true);
                radioButton10.setChecked(false);
                radioButton11.setChecked(false);
                partial_amount.setVisibility(View.GONE);
                partial_amount.setText("");
                textView14.setText(textView12.getText().toString());
            }
        });

        radioButton10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButton9.setChecked(false);
                radioButton10.setChecked(true);
                radioButton11.setChecked(false);
                partial_amount.setVisibility(View.VISIBLE);
                float val1 = Float.parseFloat(textView10.getText().toString());
                float val2 = Float.parseFloat(textView12.getText().toString());
                float finl = val1 + val2;
                textView14.setText(""+finl);
            }
        });

        radioButton11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButton9.setChecked(false);
                radioButton10.setChecked(false);
                radioButton11.setChecked(true);
                partial_amount.setVisibility(View.GONE);
                partial_amount.setText("");
                float val1 = Float.parseFloat(textView10.getText().toString());
                float val2 = Float.parseFloat(textView12.getText().toString());
                float finl = val1 + val2;
                textView14.setText(""+finl);
            }
        });

        products_list_update.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                try{
//                    select_prod.setText(adapterView.getAdapter().getItem(i).toString());
//                    mMap.remove(adapterView.getAdapter().getItem(i).toString())
                    mMap.clear();
                    arrayList_products.clear();
                    synchronized(arrayList_products){
                        arrayList_products.notify();
                    }
                    arrayList_products.add(mMap);

                    productAdapter_new = new ArrayAdapter<String>(
                            c.getApplicationContext(),
                            android.R.layout.simple_list_item_1,
                            arrayList_products);

                    products_list_update.setAdapter(productAdapter_new);
                    arrayAdapter.notifyDataSetChanged();
                    products_list_update.setVisibility(View.GONE);
                    textView10.setText("0");
                    textView14.setText("0");
                    Toaster("Removed Successfully");

                    return true;
                }catch (Exception e)
                {
                    Toaster("Removed Failed");
                    return false;
                }



            }
        });





//        ------------Quantity Add---------------


//        -----------------Customer ListView---------------------
        custom_list.setOnItemClickListener((adapterView, view, i, l) -> {
            select_custom.setText(adapterView.getAdapter().getItem(i).toString());
            new DueSync().execute(adapterView.getAdapter().getItem(i).toString());
            custom_list.setVisibility(View.GONE);


        });
//        --------------------------------------------------------

//        ------------------Product ListView----------------------
        product_list.setOnItemClickListener((adapterView, view, i, l) -> {
            select_prod.setText(adapterView.getAdapter().getItem(i).toString());
            Quanity_Class quanity_class = new Quanity_Class(c);
            quanity_class.setCanceledOnTouchOutside(false);
            quanity_class.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    try{
                        if(!mMap.containsKey(select_prod.getText().toString())) {

                            mMap.put(select_prod.getText().toString(), Receive_PreferenceQ());

                        }
                        else
                        {
//                                    mMap.put(select_prod.getText().toString(), Receive_Preference());

                            mMap.remove(select_prod.getText().toString());
                            mMap.put(select_prod.getText().toString(), Receive_PreferenceQ());


                        }
                        arrayList_products.clear();
                        synchronized(arrayList_products){
                            arrayList_products.notify();
                        }
                        arrayList_products.add(mMap);

                        productAdapter_new = new ArrayAdapter<String>(
                                c.getApplicationContext(),
                                android.R.layout.simple_list_item_1,
                                arrayList_products);

                        products_list_update.setAdapter(productAdapter_new);
                        arrayAdapter.notifyDataSetChanged();
                        products_list_update.setVisibility(View.VISIBLE);


                        {
//                                    Updating Total Amount...
                            FirebaseDatabase.getInstance().getReference("Products")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            if (snapshot.hasChildren())
                                            {
                                                float total_in = 0;

                                                String total = "";
                                                for (String key : mMap.keySet())
                                                {
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                                    {
                                                        if (dataSnapshot.child("Name").getValue(String.class)
                                                                .toLowerCase().trim().compareToIgnoreCase(key.toLowerCase().trim())==0)
                                                        {
                                                            float quanity = mMap.get(key);
                                                            int rate = Integer.parseInt(dataSnapshot.child("Price").getValue(String.class));
                                                            float answer = rate * quanity;

                                                            total_in += (float) (answer);

                                                        }
                                                    }
                                                }
                                                textView10.setText(""+total_in);

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }


                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        Log.e("Mapping error",e.getLocalizedMessage());
                    }
                }
            });
            quanity_class.show();




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
                        if (select_categ_Ac.getText().toString().length()>0
                                && select_categ_Ac.getText().toString().compareToIgnoreCase("Empty")!=0)
                        {
                            product_list.setVisibility(View.VISIBLE);
                            new Product_Class().execute(charSequence.toString().toLowerCase(), select_categ_Ac.getText().toString());}
                        else
                        {
                            Toaster("Category is Empty");
                        }
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

        });
//        -------------------------------

        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!owner) {
                    if (select_categ_Ac.getText().length() > 0
                            && select_categ_Ac.getText().toString().compareToIgnoreCase("Empty") != 0
                            && select_custom.getText().length() > 0
                            && textView10.getText().toString().compareToIgnoreCase("0") != 0
                            && mMap.size() > 0
                            && (radioButton9.isChecked() || radioButton10.isChecked() || radioButton11.isChecked())) {
                        if (radioButton10.isChecked()) {

                            {
                                if (partial_amount.getText().toString().length() > 0
                                        && Float.parseFloat(partial_amount.getText().toString()) <=
                                        Float.parseFloat(textView14.getText().toString())) {
                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time => " + c);

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                                    String formattedDate = df.format(c);

                                    new Upload_Transact().execute(select_categ_Ac.getText().toString()
                                            , select_custom.getText().toString()
                                            , (textView10.getText().toString())
                                            , Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()
                                            , String.valueOf(formattedDate)
                                            , partial_amount.getText().toString());
                                } else {
                                    Toaster("Partial Amount can not empty or greater than max due");
                                    partial_amount.setError("Empty");
                                    partial_amount.setBackground(ContextCompat.getDrawable(c, R.drawable.pass_unmatch));
                                }
                            }


                        } else {

                            {
                                Date c = Calendar.getInstance().getTime();
                                System.out.println("Current time => " + c);

                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                                String formattedDate = df.format(c);

                                new Upload_Transact().execute(select_categ_Ac.getText().toString()
                                        , select_custom.getText().toString()
                                        , (textView10.getText().toString())
                                        , Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()
                                        , String.valueOf(formattedDate)
                                        , partial_amount.getText().toString());
                            }

                        }


                    } else {
                        Toaster("Entries can not be Empty");
                        if (select_custom.getText().toString().length() == 0) {
                            select_custom.setError("Empty");
                            select_custom.setBackground(ContextCompat.getDrawable(c, R.drawable.pass_unmatch));
                        }
                        if (select_prod.getText().toString().length() == 0) {
                            select_prod.setError("Empty");
                            select_prod.setBackground(ContextCompat.getDrawable(c, R.drawable.pass_unmatch));
                        }
                    }

                }
                else {
                    if(select_custom.getText().toString().length()>0)
                    {
                        if (radioButton10.isChecked())
                        {
                            if (partial_amount.getText().toString().length() > 0
                                    && Float.parseFloat(partial_amount.getText().toString()) <=
                                    Float.parseFloat(textView14.getText().toString())) {
                                Date c = Calendar.getInstance().getTime();
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                                String formattedDate = df.format(c);

                                new Upload_Transact2().execute(select_custom.getText().toString()
                                        , Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()
                                        , String.valueOf(formattedDate)
                                        , partial_amount.getText().toString()
                                );
                            }
                            else {
                                Toaster("Partial Amount can not empty or greater than max Due");
                                partial_amount.setError("Empty");
                                partial_amount.setBackground(ContextCompat.getDrawable(c, R.drawable.pass_unmatch));
                            }
                        }
                        else
                        {
                            Date c = Calendar.getInstance().getTime();


                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                            String formattedDate = df.format(c);

                            new Upload_Transact2().execute(
                                    select_custom.getText().toString()
                                    , Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()
                                    , String.valueOf(formattedDate)
                                    , partial_amount.getText().toString());
                        }

                    }
                    else
                    {
                        select_custom.setError("Empty");
                        select_custom.setBackground(ContextCompat.getDrawable(c,R.drawable.pass_unmatch));
                    }
                }


            }
        });

    }

    List<String> your_array_list;
    ArrayAdapter<String> arrayAdapter;
    class Customers_Class extends AsyncTask<String, Void, Void>
    {

        @Override
        protected Void doInBackground(final String... voids) {

            FirebaseDatabase.getInstance().getReference("Supplier")
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
                                    if (d.child("Name").getValue(String.class).toLowerCase().contains(voids[0].toLowerCase())
                                            && d.child("Category").getValue(String.class).toLowerCase().contains(voids[1].toLowerCase()))
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
//            FirebaseDatabase.getInstance().getReference("Products")
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            List<String> rate = new ArrayList<String>();;
//                            if (snapshot.hasChildren()) {
//                                try {
//
//                                    for (DataSnapshot d : snapshot.getChildren()) {
//
//                                        if (d.child("Category").getValue(String.class).compareToIgnoreCase(select_categ_Ac.getText().toString())==0
//                                                && d.child("Name").getValue(String.class).compareToIgnoreCase(select_prod.getText().toString())==0)
//                                        {
//                                            String data = String.valueOf((d.child("Price").getValue()));
//                                            rate.add(data);
//                                        }
//                                        else {
//                                            select_rate_Ac.setText("");
//                                        }
//
//                                    }
//
//                                }
//                                catch (Exception e)
//                                {
////                                e.printStackTrace();
//                                    Log.e("Error is",e.getLocalizedMessage());
////                                Toaster(e.getMessage());
////                                if(Categories!=null)
////                                Log.e("Categories are ",Categories.get(0) +" "+ Categories.get(1));
//
//                                }
//
//
//
//                                if (rate != null) {
//                                    RateArrayAdapter = new ArrayAdapter<String>(c.getApplicationContext(), R.layout.option_item, rate);
//                                } else {
//                                    List<String> none_values = Arrays.asList(new String[]{"Empty"});
//                                    RateArrayAdapter = new ArrayAdapter<String>(c.getApplicationContext(), R.layout.option_item, none_values);
//                                }
//                                RateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////                            product_cat.setText(ChaptersArrayAdapter.getItem(0).toString(), false);
//                                select_rate_Ac.setAdapter(RateArrayAdapter);
//                            }
//
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
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
                textView10.setText(""+ final_val);
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



    public void Toaster(String s)
    {
        Toast.makeText(c.getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

    }
    class Upload_Transact extends AsyncTask<String,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress_monitor.Setup_Progressing("Please Wait","Working in Progress");
        }

        @Override
        protected Void doInBackground(String... strings) {

            FirebaseDatabase.getInstance().getReference("Payments")
                    .child("Transactions").child("Purchase")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChildren())
                            {
                                {
                                    Transaction_Encoder transaction_encoder = new Transaction_Encoder();
                                    DatabaseReference df = snapshot.getRef().child(String.valueOf(snapshot.getChildrenCount()));
                                    for (DataSnapshot snapshot1 : snapshot.getChildren())
                                    {
                                        if (snapshot1.child("Supplier").getValue(String.class).toLowerCase().contains(strings[1].toLowerCase()))
                                        {
                                            df = snapshot1.getRef();
                                        }
                                    }
//                                df.child("Category").setValue(strings[0]);
                                    df.child("Supplier").setValue(strings[1]);
                                    df.child("Product").child(strings[4])
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.hasChildren())
                                                    {
                                                        long count= snapshot.getChildrenCount();
                                                        DatabaseReference mRef1 = snapshot.getRef();
                                                        DatabaseReference mRef1_replace = null;
                                                        boolean ref_found = false;


                                                        for (String key : mMap.keySet())
                                                        {
                                                            mRef1 = snapshot.child(String.valueOf(count)).getRef();

                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                                            {
                                                                if(dataSnapshot.child("Name").getValue(String.class)
                                                                        .toLowerCase().contains(key.toLowerCase()))
                                                                {
                                                                    mRef1_replace = dataSnapshot.getRef();
                                                                    ref_found = true;
                                                                }
                                                                else {
                                                                    ref_found = false;
                                                                }
                                                            }

                                                            DatabaseReference mRef = mRef1;

                                                            long finalCount = count;

                                                            boolean finalRef_found = ref_found;

                                                            if (mRef1_replace!=null)
                                                            {
                                                                mRef1_replace.getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        if(snapshot.hasChildren())
                                                                        {
                                                                            float quantity = Long.parseLong(snapshot
                                                                                    .child("Quantity").getValue(String.class));
                                                                            float new_quantity = (mMap.get(key));
                                                                            snapshot.getRef().child("Quantity").setValue(""+(quantity+new_quantity));

                                                                            snapshot.getRef()
                                                                                    .child("Name").setValue(key);
                                                                        }


                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });
                                                            }
                                                            else
                                                            {
                                                                mRef.getRef()
                                                                        .child("Quantity").setValue(""+(mMap.get(key)));
                                                                mRef.getRef()
                                                                        .child("Name").setValue(key);
                                                            }


                                                            DatabaseReference finalMRef1_replace = mRef1_replace;
                                                            FirebaseDatabase.getInstance()
                                                                    .getReference("Products")
                                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            if(snapshot.exists())
                                                                            {
                                                                                String rate = "0";
                                                                                String cat = "0";
                                                                                for (DataSnapshot df : snapshot.getChildren())
                                                                                {
                                                                                    if(df.child("Name").getValue(String.class).toLowerCase()
                                                                                            .contains(key.toLowerCase()))
                                                                                    {
                                                                                        rate= df.child("Price").getValue(String.class);
                                                                                        cat= df.child("Category").getValue(String.class);
//                                                                    Toaster("Runned "+rate);
//                                                                    Add2Preference("Price_Now",df.child("Price").getValue(String.class));
                                                                                    }

                                                                                }

                                                                                if (finalMRef1_replace !=null)
                                                                                {
                                                                                    String finalCat = cat;
                                                                                    String finalRate = rate;
                                                                                    finalMRef1_replace.getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                            if(snapshot.hasChildren())
                                                                                            {
                                                                                                snapshot.getRef()
                                                                                                        .child("Category").setValue(""+ finalCat);

                                                                                                snapshot.getRef()
                                                                                                        .child("Rate").setValue(finalRate);


                                                                                                double amount = Double.parseDouble(finalRate.toString())
                                                                                                        * Double.parseDouble(String.valueOf(mMap.get(key)));

                                                                                                double pre_amount = Double.parseDouble(String.valueOf(snapshot.child("Amount")
                                                                                                        .getValue(String.class)));

                                                                                                snapshot.getRef()
                                                                                                        .child("Amount").setValue(String.valueOf(amount+pre_amount));
                                                                                            }


                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                                        }
                                                                                    });
                                                                                }
                                                                                else
                                                                                {
                                                                                    mRef.getRef()
                                                                                            .child("Category").setValue(""+cat);

                                                                                    mRef.getRef()
                                                                                            .child("Rate").setValue(rate);


                                                                                    double amount = Double.parseDouble(rate.toString())
                                                                                            * Double.parseDouble(String.valueOf(mMap.get(key)));


                                                                                    mRef.getRef()
                                                                                            .child("Amount").setValue("" + amount);
                                                                                }




                                                                            }
                                                                            else
                                                                            {
                                                                                Toaster("Not exists");
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });

                                                            if(mRef1_replace==null)
                                                            {
                                                                count++;
                                                            }

                                                            ref_found = false;
                                                            mRef1_replace = null;
                                                        }
                                                    }
                                                    else
                                                    {
                                                        {
                                                            long count= 0;

                                                            DatabaseReference mRef = snapshot.getRef();
                                                            for (String key : mMap.keySet())
                                                            {
                                                                mRef
                                                                        .child(String.valueOf(count))
                                                                        .child("Name").setValue(key);

                                                                mRef
                                                                        .child(String.valueOf(count))
                                                                        .child("Quantity").setValue(String.valueOf(mMap.get(key)));


                                                                long finalCount = count;
                                                                FirebaseDatabase.getInstance()
                                                                        .getReference("Products")
                                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                if(snapshot.exists())
                                                                                {
                                                                                    String rate = "0";
                                                                                    String cat = "0";
                                                                                    for (DataSnapshot df : snapshot.getChildren())
                                                                                    {
                                                                                        if(df.child("Name").getValue(String.class)
                                                                                                .toLowerCase().contains(key.toLowerCase()))
                                                                                        {
                                                                                            rate = df.child("Price").getValue(String.class);
                                                                                            cat= df.child("Category").getValue(String.class);
//                                                                                Toaster("Runned "+rate);
//                                                                                Add2Preference("Price_Now",df.child("Price").getValue(String.class));
                                                                                        }

                                                                                    }
                                                                                    mRef
                                                                                            .child(String.valueOf(finalCount))
                                                                                            .child("Category").setValue(""+cat);

                                                                                    mRef
                                                                                            .child(String.valueOf(finalCount))
                                                                                            .child("Rate").setValue(rate);

                                                                                    double amount = Double.parseDouble(rate.toString())
                                                                                            * Double.parseDouble(String.valueOf(mMap.get(key)));

                                                                                    mRef
                                                                                            .child(String.valueOf(finalCount))
                                                                                            .child("Amount").setValue(""+amount);
                                                                                }
                                                                                else
                                                                                {
                                                                                    Toaster("Not exists");
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                            }
                                                                        });




                                                                count+=1;
                                                            }
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                    df.child("Amount").getRef()
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists())
                                                    {
                                                        double val = Double.parseDouble(transaction_encoder
                                                                .getDecoded(snapshot.getValue(String.class)));
                                                        double finalVal = val + Double.parseDouble(strings[2]);
                                                        snapshot.getRef()
                                                                .setValue(transaction_encoder.getEncoded(String.valueOf(finalVal)));
                                                    }
                                                    else
                                                    {
                                                        snapshot.getRef().setValue(transaction_encoder.getEncoded(strings[2]));
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                    df.child("User").setValue(strings[3]);
                                    df.child("Time").setValue(strings[4]);
                                    df.child("Type").setValue("Sell");
                                    df.child("Credit").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists())
                                            {
                                                if(radioButton10.isChecked()) {
                                                    double val = Double.parseDouble(transaction_encoder
                                                            .getDecoded(snapshot.getValue(String.class)));
                                                    double finalVal = val + Double.parseDouble(strings[5]);
                                                    snapshot.getRef()
                                                            .setValue(transaction_encoder.getEncoded(String.valueOf(finalVal)));
                                                }

                                                if(radioButton9.isChecked())
                                                {
                                                    double val = Double.parseDouble(transaction_encoder
                                                            .getDecoded(snapshot.getValue(String.class)));
                                                    double finalVal = val + Double.parseDouble(strings[2]);
                                                    snapshot.getRef()
                                                            .setValue(transaction_encoder.getEncoded(String.valueOf(finalVal)));
                                                }

                                                if(radioButton11.isChecked())
                                                {
                                                    double val = Double.parseDouble(transaction_encoder
                                                            .getDecoded(snapshot.getValue(String.class)));
                                                    double finalVal = val + (0);
                                                    snapshot.getRef()
                                                            .setValue(transaction_encoder.getEncoded(String.valueOf(finalVal)));
                                                }
                                            }
                                            else
                                            {
                                                if(radioButton10.isChecked()) {
                                                    snapshot.getRef()
                                                            .setValue(transaction_encoder.getEncoded(String.valueOf(strings[5])));
                                                }
                                                if(radioButton9.isChecked())
                                                {
                                                    snapshot.getRef()
                                                            .setValue(transaction_encoder.getEncoded(String.valueOf(strings[2])));

                                                }

                                                if(radioButton11.isChecked())
                                                {
                                                    snapshot.getRef()
                                                            .setValue(transaction_encoder.getEncoded(String.valueOf(0)));

                                                }

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }

                            }
                            else
                            {
                                Transaction_Encoder transaction_encoder = new Transaction_Encoder();
                                DatabaseReference df = snapshot.getRef().child(String.valueOf(0));
//                                df.child("Category").setValue(strings[0]);
                                df.child("Supplier").setValue(strings[1]);
                                df.child("Product").child(strings[4])
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.hasChildren())
                                                {
                                                    long count= snapshot.getChildrenCount();
                                                    DatabaseReference mRef = snapshot.getRef();
                                                    for (String key : mMap.keySet())
                                                    {
                                                        mRef
                                                                .child(String.valueOf(count))
                                                                .child("Name").setValue(key);

                                                        mRef
                                                                .child(String.valueOf(count))
                                                                .child("Quantity").setValue(""+mMap.get(key));


                                                        long finalCount = count;
                                                        FirebaseDatabase.getInstance()
                                                                .getReference("Products").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if(snapshot.exists())
                                                                {
                                                                    String rate = "0";
                                                                    String cat = "0";
                                                                    for (DataSnapshot df : snapshot.getChildren())
                                                                    {
                                                                        if(df.child("Name").getValue(String.class).contains(key.toLowerCase()))
                                                                        {
                                                                            rate= df.child("Price").getValue(String.class);
                                                                            cat= df.child("Category").getValue(String.class);
//                                                                    Toaster("Runned "+rate);
//                                                                    Add2Preference("Price_Now",df.child("Price").getValue(String.class));
                                                                        }

                                                                    }

                                                                    mRef
                                                                            .child(String.valueOf(finalCount))
                                                                            .child("Category").setValue(""+cat);

                                                                    mRef
                                                                            .child(String.valueOf(finalCount))
                                                                            .child("Rate").setValue(rate);

                                                                    double amount = Double.parseDouble(rate.toString())
                                                                            * Double.parseDouble(String.valueOf(mMap.get(key)));

                                                                    mRef
                                                                            .child(String.valueOf(finalCount))
                                                                            .child("Amount").setValue(""+amount);

                                                                }
                                                                else
                                                                {
                                                                    Toaster("Not exists");
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });


                                                        count++;
                                                    }
                                                }
                                                else
                                                {
                                                    {
                                                        long count= 0;

                                                        DatabaseReference mRef = snapshot.getRef();
                                                        for (String key : mMap.keySet())
                                                        {
                                                            mRef
                                                                    .child(String.valueOf(count))
                                                                    .child("Name").setValue(key);

                                                            mRef
                                                                    .child(String.valueOf(count))
                                                                    .child("Quantity").setValue(""+mMap.get(key));


                                                            long finalCount = count;
                                                            FirebaseDatabase.getInstance()
                                                                    .getReference("Products")
                                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            if(snapshot.exists())
                                                                            {
                                                                                String rate = "0";
                                                                                String cat = "0";
                                                                                for (DataSnapshot df : snapshot.getChildren())
                                                                                {
                                                                                    if(df.child("Name").getValue(String.class).toLowerCase().contains(key.toLowerCase()))
                                                                                    {
                                                                                        rate = df.child("Price").getValue(String.class);
                                                                                        cat= df.child("Category").getValue(String.class);
//                                                                                Toaster("Runned "+rate);
//                                                                                Add2Preference("Price_Now",df.child("Price").getValue(String.class));
                                                                                    }

                                                                                }
                                                                                mRef
                                                                                        .child(String.valueOf(finalCount))
                                                                                        .child("Category").setValue(""+cat);

                                                                                mRef
                                                                                        .child(String.valueOf(finalCount))
                                                                                        .child("Rate").setValue(rate);

                                                                                double amount = Double.parseDouble(rate.toString())
                                                                                        * Double.parseDouble(String.valueOf(mMap.get(key)));

                                                                                mRef
                                                                                        .child(String.valueOf(finalCount))
                                                                                        .child("Amount").setValue(""+amount);
                                                                            }
                                                                            else
                                                                            {
                                                                                Toaster("Not exists");
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });




                                                            count+=1;
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                df.child("Amount").getRef()
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists())
                                                {
                                                    double val = Double.parseDouble(transaction_encoder
                                                            .getDecoded(snapshot.getValue(String.class)));
                                                    double finalVal = val + Double.parseDouble(strings[2]);
                                                    snapshot.getRef()
                                                            .setValue(transaction_encoder.getEncoded(String.valueOf(finalVal)));
                                                }
                                                else
                                                {
                                                    snapshot.getRef().setValue(transaction_encoder.getEncoded(strings[2]));
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                df.child("User").setValue(strings[3]);
                                df.child("Time").setValue(strings[4]);
                                df.child("Type").setValue("Sell");
                                df.child("Credit").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists())
                                        {
                                            if(radioButton10.isChecked()) {
                                                double val = Double.parseDouble(transaction_encoder
                                                        .getDecoded(snapshot.getValue(String.class)));
                                                double finalVal = val + Double.parseDouble(strings[5]);
                                                snapshot.getRef()
                                                        .setValue(transaction_encoder.getEncoded(String.valueOf(finalVal)));
                                            }

                                            if(radioButton9.isChecked())
                                            {
                                                double val = Double.parseDouble(transaction_encoder
                                                        .getDecoded(snapshot.getValue(String.class)));
                                                double finalVal = val + Double.parseDouble(strings[2]);
                                                snapshot.getRef()
                                                        .setValue(transaction_encoder.getEncoded(String.valueOf(finalVal)));
                                            }

                                            if(radioButton11.isChecked())
                                            {
                                                double val = Double.parseDouble(transaction_encoder
                                                        .getDecoded(snapshot.getValue(String.class)));
                                                double finalVal = val + (0);
                                                snapshot.getRef()
                                                        .setValue(transaction_encoder.getEncoded(String.valueOf(finalVal)));
                                            }
                                        }
                                        else
                                        {
                                            if(radioButton10.isChecked()) {
                                                snapshot.getRef()
                                                        .setValue(transaction_encoder.getEncoded(String.valueOf(strings[5])));
                                            }
                                            if(radioButton9.isChecked())
                                            {
                                                snapshot.getRef()
                                                        .setValue(transaction_encoder.getEncoded(String.valueOf(strings[2])));

                                            }

                                            if(radioButton11.isChecked())
                                            {
                                                snapshot.getRef()
                                                        .setValue(transaction_encoder.getEncoded(String.valueOf(0)));

                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                            Transaction_Encoder transaction_encoder = new Transaction_Encoder();
                            FirebaseDatabase.getInstance()
                                    .getReference("Payments").child("Balance").child("Purchase")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists())
                                            {
                                                String balance = snapshot.getValue(String.class);
                                                double balance1 = Double.parseDouble(transaction_encoder.getDecoded(balance));
                                                double newBalance = balance1 + Double.parseDouble(strings[2]);
                                                snapshot.getRef().setValue(transaction_encoder.getEncoded(String.valueOf(newBalance)));
                                            }
                                            else
                                            {
                                                snapshot.getRef().setValue(String.valueOf(transaction_encoder.getEncoded(strings[2])));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
//                            ------------------------------>

                            FirebaseDatabase.getInstance()
                                    .getReference("Payments").child("History").child("Purchase")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            snapshot.child(String.valueOf(snapshot.getChildrenCount()))
                                                    .getRef().setValue(String.valueOf(transaction_encoder.getEncoded(strings[2])));

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                            Reset_Inputs();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


            return null;
        }
    }

    class Upload_Transact2 extends AsyncTask<String,Void,Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress_monitor.Setup_Progressing("Please Wait","Working in Progress");
        }

        @Override
        protected Void doInBackground(String... strings) {

            FirebaseDatabase.getInstance().getReference("Payments")
                    .child("Transactions").child("Sell")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChildren())
                            {
                                {
                                    Transaction_Encoder transaction_encoder = new Transaction_Encoder();
                                    DatabaseReference df = snapshot.getRef().child(String.valueOf(snapshot.getChildrenCount()));
                                    for (DataSnapshot snapshot1 : snapshot.getChildren())
                                    {
                                        if (snapshot1.child("Supplier").getValue(String.class).toLowerCase().contains(strings[0].toLowerCase()))
                                        {
                                            df = snapshot1.getRef();
                                        }
                                    }
//                                df.child("Category").setValue(strings[0]);
                                    df.child("Supplier").setValue(strings[0]);




                                    df.child("Type").setValue("Sell");
                                    df.child("Credit").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists())
                                            {
                                                if(radioButton10.isChecked()) { //Partial
                                                    double val = Double.parseDouble(transaction_encoder
                                                            .getDecoded(snapshot.getValue(String.class)));
                                                    double finalVal = val + Double.parseDouble(strings[5]);
                                                    snapshot.getRef()
                                                            .setValue(transaction_encoder.getEncoded(String.valueOf(finalVal)));
                                                }




                                            }
                                            else
                                            {


                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }

                            }
                            else
                            {
                                Transaction_Encoder transaction_encoder = new Transaction_Encoder();
                                DatabaseReference df = snapshot.getRef().child(String.valueOf(0));
//                                df.child("Category").setValue(strings[0]);
                                df.child("Supplier").setValue(strings[0]);

                                df.child("Credit").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists())
                                        {
                                            if(radioButton10.isChecked()) {
                                                double val = Double.parseDouble(transaction_encoder
                                                        .getDecoded(snapshot.getValue(String.class)));
                                                double finalVal = val + Double.parseDouble(strings[3]);
                                                snapshot.getRef()
                                                        .setValue(transaction_encoder.getEncoded(String.valueOf(finalVal)));
                                            }


                                        }
                                        else
                                        {
                                            if(radioButton10.isChecked()) {
                                                snapshot.getRef()
                                                        .setValue(transaction_encoder.getEncoded(String.valueOf(strings[3])));
                                            }


                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                            Transaction_Encoder transaction_encoder = new Transaction_Encoder();



//                            ----------Update Product Count------------


                            Reset_Inputs();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


            return null;
        }
    }

    private void Reset_Inputs() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 5 seconds
                select_categ_Ac.setText("");
                select_prod.setText("");
//        select_rate_Ac.setText("");
                textView10.setText("0");
                select_custom.setText("");
                textView12.setText("0");
                textView14.setText("0");
                select_custom.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));
                select_prod.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));
                if(radioButton10.isChecked())
                {
                    partial_amount.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));
                    partial_amount.setText("");
                }

                progress_monitor.Drop_Progressing();
                try{
//                    select_prod.setText(adapterView.getAdapter().getItem(i).toString());
//                    mMap.remove(adapterView.getAdapter().getItem(i).toString())
                    mMap.clear();
                    arrayList_products.clear();
                    synchronized(arrayList_products){
                        arrayList_products.notify();
                    }
                    arrayList_products.add(mMap);

                    productAdapter_new = new ArrayAdapter<String>(
                            c.getApplicationContext(),
                            android.R.layout.simple_list_item_1,
                            arrayList_products);

                    products_list_update.setAdapter(productAdapter_new);
                    arrayAdapter.notifyDataSetChanged();
                    products_list_update.setVisibility(View.GONE);


                }catch (Exception e)
                {
                    Toaster("Removed Failed");

                }
            }
        }, 2000);
    }

    private int Receive_Preference()
    {
        SharedPreferences pref = c.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        return pref.getInt("product_quant", 0);         // getting you_bool
    }
    public class DueSync extends AsyncTask<String,Void,Void>
    {

        @Override
        protected Void doInBackground(String... strings) {
            FirebaseDatabase.getInstance().getReference("Payments")
                    .child("Transactions").child("Purchase")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChildren())
                            {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    if (dataSnapshot.child("Supplier")
                                            .getValue(String.class)
                                            .compareToIgnoreCase(strings[0])==0)
                                    {
                                        float Amount = Float.parseFloat(new Transaction_Encoder()
                                                .getDecoded(dataSnapshot
                                                        .child("Amount").getValue(String.class)));
                                        float Credit = Float.parseFloat(new Transaction_Encoder()
                                                .getDecoded(dataSnapshot
                                                        .child("Credit").getValue(String.class)));
                                        float AmountToCredit = Amount-Credit;
                                        textView12.setText(""+ (AmountToCredit));

                                    }
                                    else {
//                                        textView12.setText(""+ (0));
                                    }

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

    public class CalcFin_Due extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            float Total = Float.parseFloat(strings[0].toString()+"f");
            float Due = Float.parseFloat(strings[1].toString()+"f");
            float final_val = Total+Due;
            return ""+final_val;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textView14.setText(""+s);
        }
    }

    public class Remain_Due extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            float total_due = Float.parseFloat(strings[1]);
            float partial = Float.parseFloat(strings[0]);
            float remain = total_due-partial;

            return ""+remain;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    public boolean IsCustomerValid(String s)
    {
        final boolean[] isCustom = {false};
        FirebaseDatabase.getInstance().getReference("Supplier")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren())
                        {
                            for (DataSnapshot snapshot1 : snapshot.getChildren())
                            {
                                if (snapshot1.child("Name").getValue(String.class).compareToIgnoreCase(s)==0)
                                {
                                    isCustom[0] = true;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        return isCustom[0];
    }
    private float Receive_PreferenceQ()
    {
        SharedPreferences pref = c.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        return pref.getFloat("product_quant", 0);         // getting you_bool
    }

}
