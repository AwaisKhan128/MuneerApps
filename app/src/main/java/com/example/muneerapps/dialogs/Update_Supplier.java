package com.example.muneerapps.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.muneerapps.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Update_Supplier extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public EditText prev_name, new_name, person_cnic, person_address;
    public ListView list_name;
    public Button update;




    public Update_Supplier(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }
    Progress_Monitor progress_monitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_supplier);

        prev_name = findViewById(R.id.prev_name);
        new_name = findViewById(R.id.new_name);
        person_cnic = findViewById(R.id.person_cnic);
        person_address = findViewById(R.id.person_address);
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

                }
                else
                {
                    if (prev_name.getText().toString().length()==0)
                    {
                        prev_name.setError("Missing");
                    }
                    if (new_name.getText().toString().length()==0)
                    {
                        new_name.setError("Missing");
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

            FirebaseDatabase.getInstance().getReference("Supplier")
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
            progress_monitor.Setup_Progressing("Please","Working in Progress");
        }

        @Override
        protected Void doInBackground(String... strings) {
            FirebaseDatabase.getInstance().getReference("Supplier")
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
                                    ResetAll();
                                }
                                else
                                {
                                    Toaster(" "+isFound);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progress_monitor.Drop_Progressing();
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



}
