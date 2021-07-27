package com.example.muneerapps.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.muneerapps.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

public class Customer_dialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public EditText title,description;
    public RadioButton radioButton,radioButton2,radioButton3;

    public EditText c_name, c_cnic, c_address;
    public Button button10;


    public Customer_dialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_customer);
        c_name = (EditText) findViewById(R.id.c_name);
        c_cnic = (EditText) findViewById(R.id.c_cnic);
        c_address = (EditText) findViewById(R.id.c_address);
        button10 = (Button) findViewById(R.id.button10);

        button10.setOnClickListener(view -> {

            if (FirebaseAuth.getInstance().getCurrentUser()!=null)
            {
                if (c_name.getText().toString().length()>0 && c_cnic.getText().toString().length()>0 && c_address.getText().toString().length()>0) {
                    FirebaseDatabase.getInstance().getReference("Customers").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChildren()) {

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                        .getReference("Customers")
                                        .child(String.valueOf(snapshot.getChildrenCount()));


                                databaseReference.child("Name").setValue(c_name.getText().toString());
                                databaseReference.child("CNIC").setValue(c_cnic.getText().toString());
                                databaseReference.child("Address").setValue(c_address.getText().toString());


                            } else {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                        .getReference("Customers")
                                        .child(String.valueOf(0));


                                databaseReference.child("Name").setValue(c_name.getText().toString());
                                databaseReference.child("CNIC").setValue(c_cnic.getText().toString());
                                databaseReference.child("Address").setValue(c_address.getText().toString());
                            }
                            Toaster("Success");
                            c_name.setText("");
                            c_cnic.setText("");
                            c_address.setText("");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            Toaster("Failed to Saved Try again");
                        }
                    });
                }
                else
                {
                    Toaster("Failed to put empty blocks");
                    if(c_name.getText().toString().length()==0)
                    {
                        c_name.setError("Empty");
                    }
                    if(c_cnic.getText().toString().length()==0)
                    {
                        c_cnic.setError("Empty");
                    }
                    if(c_address.getText().toString().length()==0)
                    {
                        c_address.setError("Empty");
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
}
