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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

public class ReturnPurchase_dialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;

    public TextInputLayout  select_categ,  select_rate;
    public EditText select_custom,select_prod;
    public TextView textView10;
    public Button button12;
    public ListView custom_list,product_list;
    public AutoCompleteTextView select_categ_Ac;

    public ValueEventListener customer,product;
    private String Category,Rates;
    ArrayAdapter<String> ChaptersArrayAdapter, RateArrayAdapter;
    public ListView products_list_update;
    Progress_Monitor progress_monitor;


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


    public ReturnPurchase_dialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    ArrayList arrayList_products = new ArrayList();
    ArrayAdapter<String> productAdapter_new ;
//    HashMap mMap = new HashMap();
    private Map<String, Integer> mMap = new HashMap<String, Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.return_purchase);
        button12 = (Button) findViewById(R.id.button12);
        textView10 = (TextView) findViewById(R.id.textView10);
        select_custom = (EditText) findViewById(R.id.select_cutom);
        select_categ = (TextInputLayout) findViewById(R.id.select_categ);
        select_prod = (EditText) findViewById(R.id.select_prod);
        progress_monitor = new Progress_Monitor(c);

        custom_list = (ListView) findViewById(R.id.custom_list);
        product_list = (ListView) findViewById(R.id.product_list);
        select_categ_Ac = findViewById(R.id.select_categ_Ac);
        products_list_update = findViewById(R.id.products_list);








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





//        -------------Rates----------------

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
            Quanity_Class quanity_class = new Quanity_Class(c);
            quanity_class.setCanceledOnTouchOutside(false);
            quanity_class.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                            try{
                                if(!mMap.containsKey(select_prod.getText().toString())) {

                                    mMap.put(select_prod.getText().toString(), Receive_Preference());

                                }
                                else
                                {
//                                    mMap.put(select_prod.getText().toString(), Receive_Preference());

                                        mMap.remove(select_prod.getText().toString());
                                        mMap.put(select_prod.getText().toString(), Receive_Preference());


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
                                        if (dataSnapshot.child("Name").getValue(String.class).toLowerCase().contains(key.toLowerCase()))
                                        {
                                            int quanity = mMap.get(key);
                                            int rate = Integer.parseInt(dataSnapshot.child("Price").getValue(String.class));
                                            int answer = rate * quanity;

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
                                && select_categ_Ac.getText().toString().compareToIgnoreCase("Empty")!=0
                         && select_custom.getText().toString().length()>0)
                        {
                        product_list.setVisibility(View.VISIBLE);
                        new Product_Class().execute(charSequence.toString().toLowerCase()
                                , select_categ_Ac.getText().toString()
                                , select_custom.getText().toString());
                        }
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



//                product_cat_hint.setPlaceholderText();
        });
//        -------------------------------

        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (select_categ_Ac.getText().length()>0
                        && select_categ_Ac.getText().toString().compareToIgnoreCase("Empty")!=0
                        && select_custom.getText().length()>0
                        && textView10.getText().toString().compareToIgnoreCase("0")!=0
                        && mMap.size()>0)

                {

                    {
                        Date c = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + c);

                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                        String formattedDate = df.format(c);

                        new Upload_Transact().execute(select_categ_Ac.getText().toString()
                                ,select_custom.getText().toString()
                                ,(textView10.getText().toString())
                                ,Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()
                                , String.valueOf(formattedDate)
                                );
                    }



                }
                else {
                    Toaster("Entries can not be Empty");
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


    List<String> product_array_list = new ArrayList<>();
    ArrayAdapter<String> productAdapter;
    class Product_Class extends AsyncTask<String, Void, Void>
    {

        @Override
        protected Void doInBackground( String... voids) {

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

//            FirebaseDatabase.getInstance().getReference("Payments")
//                    .child("Transactions").child("Sell")
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(snapshot.hasChildren())
//                    {
//                        product_array_list = new ArrayList<>();
//                        DatabaseReference mRef = null;
//                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
//                        {
//                            if (dataSnapshot.child("Customer").getValue(String.class)
//                                    .toLowerCase().contains(voids[2].toLowerCase()))
//                            {
//                                mRef = snapshot.getRef();
//                            }
//                        }
//
//                        if(mRef !=null)
//                        {
//                            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    for (DataSnapshot snapshot1 : snapshot.child("Product").getChildren())
//                                    {
//                                        for (DataSnapshot snapshot2 : snapshot1.getChildren())
//                                        {
//                                            if (snapshot2.child("Name").getValue(String.class).toLowerCase().contains(voids[0].toLowerCase())
//                                                    && snapshot2.child("Category").getValue(String.class).toLowerCase().contains(voids[1].toLowerCase()))
//                                            {
//                                                product_array_list.add(snapshot2.child("Name").getValue(String.class));
////                                        Toaster("Matched");
//                                            }
//                                            else{
//                                                product_array_list.add("No Matched Found");
//                                            }
//                                        }
//                                    }
//
//                                    productAdapter = new ArrayAdapter<String>(
//                                            c.getApplicationContext(),
//                                            android.R.layout.simple_list_item_1,
//                                            product_array_list );
//
//                                    product_list.setAdapter(productAdapter);
//                                    product_list.setVisibility(View.VISIBLE);
//                                    Log.e("Array_Size",""+product_array_list.size());
//
//
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });
//                        }
//                        else {
//                            try
//                            {
//                                arrayAdapter.clear();
//                                arrayAdapter.notifyDataSetChanged();
//                            }
//
//                            catch (Exception e)
//                            {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
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

    String rate = "0";

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
                    .child("Transactions").child("Return_Purchase")
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
                                                                            long quantity = (snapshot
                                                                                    .child("Quantity").getValue(Long.class));
                                                                            long new_quantity = (mMap.get(key));
                                                                            snapshot.getRef().child("Quantity").setValue((quantity+new_quantity));

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
                                                                        .child("Quantity").setValue((mMap.get(key)));
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
                                    df.child("Credit").getRef().setValue(new Transaction_Encoder().getEncoded("0"));


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
                                                        .child("Quantity").setValue(mMap.get(key));


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
                                                            .child("Quantity").setValue(mMap.get(key));


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
                                df.child("Credit").getRef().setValue(new Transaction_Encoder().getEncoded("0"));


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

//                            ----------Update Product Count------------


                            Reset_Inputs();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            FirebaseDatabase.getInstance().getReference("Payments")
                    .child("Transactions").child("Purchase")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChildren())
                            {
                                DatabaseReference mRef = null;

                                for (DataSnapshot snapshot1 : snapshot.getChildren())
                                {
                                    if(snapshot1.hasChild("Supplier"))
                                    {
                                        if (snapshot1.child("Supplier").getValue(String.class)
                                                .toLowerCase().contains(strings[1].toLowerCase()))
                                        {
                                            mRef = snapshot1.getRef();
                                        }
                                    }
                                }
                                if (mRef!=null)
                                {
                                    mRef.child("Amount").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists())
                                            {
                                                float previous_amount = Float.parseFloat(
                                                        new Transaction_Encoder().getDecoded(snapshot.getValue(String.class)));
                                                float new_amount = Float.parseFloat(strings[2]);
                                                snapshot.getRef().setValue(new Transaction_Encoder()
                                                        .getEncoded(String.valueOf(previous_amount-new_amount)));



                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    mRef.child("Credit").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists())
                                            {
                                                {

                                                    float pre_valC = Float.parseFloat(new Transaction_Encoder()
                                                            .getDecoded(snapshot.getValue(String.class)));
                                                    float new_Camount = Float.parseFloat(strings[2]);
                                                    snapshot.getRef().setValue(new Transaction_Encoder()
                                                            .getEncoded(String.valueOf(pre_valC-new_Camount)));

                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
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

    private void Reset_Inputs() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 2 seconds
                select_categ_Ac.setText("");
                select_prod.setText("");
//        select_rate_Ac.setText("");
                textView10.setText("0");
                select_custom.setText("");
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

    private String Retrieve_preference(String key)
    {
        SharedPreferences pref = c.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        return pref.getString(key, "");         // getting you_bool
    }


    private void Add2Preference(String key, String quantity)
    {
        SharedPreferences pref = c.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, quantity);  // Saving string
        // Save the changes in SharedPreferences
        editor.apply(); // commit changes
    }

    private int Receive_Preference()
    {
        SharedPreferences pref = c.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        return pref.getInt("product_quant", 0);         // getting you_bool
    }

    class Manage_Payments extends AsyncTask<String,Void,Void>
    {

        @Override
        protected Void doInBackground(String... strings) {
            try{
            double total = Double.parseDouble(strings[0].toString());
            double partial = Double.parseDouble(strings[1].toString());
            double final_cal = total-partial;
            textView10.setText(""+String.valueOf(final_cal));}
            catch (Exception e)
            {
                Log.e("Error Except: ",e.getLocalizedMessage());
            }
            return null;
        }
    }



}
