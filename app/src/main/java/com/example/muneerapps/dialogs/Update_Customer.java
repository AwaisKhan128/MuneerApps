package com.example.muneerapps.dialogs;

import android.app.Activity;
import android.app.Dialog;
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
import androidx.core.content.ContextCompat;

import com.example.muneerapps.R;
import com.example.muneerapps.Transaction_Encoder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class Update_Customer extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public EditText prev_name, new_name, person_cnic, person_address;
    public ListView list_name;
    public Button update;




    public Update_Customer(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }
    Progress_Monitor progress_monitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_custom);

        prev_name = findViewById(R.id.prev_name);
        prev_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    prev_name.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text1));
                }
                if (!hasFocus){
                    prev_name.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));

                }
            }
        });
        new_name = findViewById(R.id.new_name);
        new_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    new_name.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text1));
                }
                if (!hasFocus){
                    new_name.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));

                }
            }
        });
        person_cnic = findViewById(R.id.person_cnic);
        person_cnic.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    person_cnic.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text1));
                }
                if (!hasFocus){
                    person_cnic.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));

                }
            }
        });
        person_address = findViewById(R.id.person_address);
        person_address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    person_address.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text1));
                }
                if (!hasFocus){
                    person_address.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));

                }
            }
        });
        list_name = findViewById(R.id.list_name);
        update = findViewById(R.id.button20);
        progress_monitor = new Progress_Monitor(c);



        list_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                prev_name.setText(adapterView.getAdapter().getItem(i).toString());
                list_name.setVisibility(View.GONE);
            }
        });

        prev_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (prev_name.getText().toString().length()==0)
                {
                    list_name.setVisibility(View.GONE);
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
                else
                {
                    list_name.setVisibility(View.VISIBLE);
                    new Customers_Class().execute(charSequence.toString().toLowerCase());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (prev_name.getText().toString().length()>0 && new_name.getText().toString().length()>0)
                {
                    new Update_Class().execute(prev_name.getText().toString().toLowerCase()
                            ,new_name.getText().toString()
                            ,person_cnic.getText().toString()
                            ,person_address.getText().toString());


                    new UpdateAllCustomer().execute(prev_name.getText().toString(),new_name.getText().toString());

                }
                else
                {
                    if (prev_name.getText().toString().length()==0)
                    {
                        prev_name.setError("Missing");
                        prev_name.setBackground(ContextCompat.getDrawable(c,R.drawable.pass_unmatch));

                    }
                    if (new_name.getText().toString().length()==0)
                    {
                        new_name.setError("Missing");
                        new_name.setBackground(ContextCompat.getDrawable(c,R.drawable.pass_unmatch));
                    }
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

            FirebaseDatabase.getInstance().getReference("Customers")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    your_array_list = new ArrayList<>();

                    if (snapshot.hasChildren())
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

                        list_name.setAdapter(arrayAdapter);
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

    public class Update_Class extends AsyncTask<String,Void,Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress_monitor.Setup_Progressing("Please Wait","Working in Progress");
        }

        @Override
        protected Void doInBackground(String... strings) {
            FirebaseDatabase.getInstance().getReference("Customers")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChildren())
                            {
                                DatabaseReference isFound = null;

                                for(DataSnapshot d : snapshot.getChildren())
                                {
                                    if (d.child("Name").getValue(String.class).toLowerCase().contains(strings[0]))
                                    {
                                       isFound = d.getRef();
                                    }
                                }

                                if (isFound!=null)
                                {
                                    isFound.child("Name").setValue(strings[1]);

                                    if(strings[2].length()>0)
                                    {
                                        isFound.child("CNIC").setValue(strings[2]);
                                    }
                                    if(strings[3].length()>0)
                                    {
                                        isFound.child("Address").setValue(strings[3]);
                                    }



                                }
                                else
                                {
                                    Toaster(" "+isFound);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            new_name.setBackground(ContextCompat.getDrawable(c,R.drawable.pass_unmatch));
                            Toaster(error.getMessage()+" Failed");
                        }
                    });


            return null;
        }
    }

    private void ResetAll() {
        prev_name.setText("");
        new_name.setText("");
        person_cnic.setText("");
        person_address.setText("");
        prev_name.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));
        new_name.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));
        person_cnic.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));
        person_address.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));
        progress_monitor.Drop_Progressing();

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


    public class UpdateAllCustomer extends AsyncTask<String,Void,Void>
    {

        @Override
        protected Void doInBackground(String... strings) {
            FirebaseDatabase.getInstance().getReference("Payments")
                    .child("Transactions")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                if (snapshot.hasChild("Sell"))
                                {

                                    for (DataSnapshot snapshot1: snapshot.child("Sell").getChildren())
                                    {
                                        if(strings[0] !=null && snapshot1.hasChild("Customer")) {
                                            if (snapshot1.child("Customer").getValue(String.class).toLowerCase()
                                                    .compareTo(strings[0].toLowerCase()) == 0) {
                                                snapshot1.getRef().child("Customer").setValue(strings[1]);
                                            }

                                        }
                                    }
                                }
                                if (snapshot.hasChild("Return_Sell"))
                                {

                                    for (DataSnapshot snapshot2: snapshot.child("Return_Sell").getChildren())
                                    {
                                        if(strings[0] !=null && snapshot2.hasChild("Customer")) {
                                            if (snapshot2.child("Customer").getValue(String.class).toLowerCase()
                                                    .compareTo(strings[0].toLowerCase())==0)
                                            {
                                                snapshot2.getRef().child("Customer").setValue(strings[1]);
                                            }}
                                    }
                                }
                                ResetAll();
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
