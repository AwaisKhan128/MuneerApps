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
import androidx.core.content.ContextCompat;

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
    Progress_Monitor progress_monitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_customer);
        c_name = (EditText) findViewById(R.id.c_name);
        c_cnic = (EditText) findViewById(R.id.c_cnic);
        c_address = (EditText) findViewById(R.id.c_address);
        c_name.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                c_name.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text1));
            }
            if (!hasFocus){
                c_name.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));

            }
        });

        c_cnic.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                c_cnic.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text1));
            }
            if (!hasFocus){
                c_cnic.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));

            }
        });

        c_address.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                c_address.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text1));
            }
            if (!hasFocus){
                c_address.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));

            }
        });
        button10 = (Button) findViewById(R.id.button10);
        progress_monitor = new Progress_Monitor(c);

        button10.setOnClickListener(view -> {

            progress_monitor.Setup_Progressing("Please Wait","Working in Progress");
            if  (c_name.getText().toString().length()>0) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    FirebaseDatabase.getInstance().getReference("Customers")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChildren()) {
                                        boolean isfound = false;
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            if (dataSnapshot.child("Name")
                                                    .getValue(String.class).compareToIgnoreCase(c_name.getText().toString()) == 0) {
                                                isfound = true;
                                            }
                                        }

                                        if (!isfound) {
                                            if (c_name.getText().toString().length() > 0 && c_cnic.getText().toString().length() > 0
                                                    && c_address.getText().toString().length() > 0) {
                                                FirebaseDatabase.getInstance().getReference("Customers")
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
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
                                                        c_name.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));
                                                        c_cnic.setText("");
                                                        c_cnic.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));
                                                        c_address.setText("");
                                                        c_address.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));
                                                        progress_monitor.Drop_Progressing();


                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                        Toaster("Failed to Saved Try again");
                                                        progress_monitor.Drop_Progressing();
                                                        c_name.setBackground(ContextCompat.getDrawable(c,R.drawable.pass_unmatch));
                                                    }
                                                });
                                            } else {
                                                Toaster("Failed to put empty blocks");
                                                if (c_name.getText().toString().length() == 0) {
                                                    c_name.setError("Empty");
                                                    c_name.setBackground(ContextCompat.getDrawable(c,R.drawable.pass_unmatch));
                                                }
                                                if (c_cnic.getText().toString().length() == 0) {
                                                    c_cnic.setError("Empty");
                                                    c_cnic.setBackground(ContextCompat.getDrawable(c,R.drawable.pass_unmatch));
                                                }
                                                if (c_address.getText().toString().length() == 0) {
                                                    c_address.setError("Empty");
                                                    c_address.setBackground(ContextCompat.getDrawable(c,R.drawable.pass_unmatch));
                                                }
                                                progress_monitor.Drop_Progressing();
                                            }
                                        } else {
                                            Toaster("Already Exist");
                                            progress_monitor.Drop_Progressing();
                                            c_name.setBackground(ContextCompat.getDrawable(c,R.drawable.pass_unmatch));
                                        }
                                    }
                                    else {
                                        if (c_name.getText().toString().length() > 0 && c_cnic.getText().toString().length() > 0
                                                && c_address.getText().toString().length() > 0) {
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
                                                    c_name.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));
                                                    c_cnic.setText("");
                                                    c_cnic.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));
                                                    c_address.setText("");
                                                    c_address.setBackground(ContextCompat.getDrawable(c,R.drawable.dialog_text));
                                                    progress_monitor.Drop_Progressing();

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                    Toaster("Failed to Saved Try again");
                                                    progress_monitor.Drop_Progressing();
                                                    c_name.setBackground(ContextCompat.getDrawable(c,R.drawable.pass_unmatch));
                                                    c_cnic.setBackground(ContextCompat.getDrawable(c,R.drawable.pass_unmatch));
                                                    c_address.setBackground(ContextCompat.getDrawable(c,R.drawable.pass_unmatch));
                                                }
                                            });
                                        } else {
                                            Toaster("Failed to put empty blocks");
                                            if (c_name.getText().toString().length() == 0) {
                                                c_name.setError("Empty");
                                                c_name.setBackground(ContextCompat.getDrawable(c,R.drawable.pass_unmatch));
                                            }
                                            if (c_cnic.getText().toString().length() == 0) {
                                                c_cnic.setError("Empty");
                                                c_cnic.setBackground(ContextCompat.getDrawable(c,R.drawable.pass_unmatch));
                                            }
                                            if (c_address.getText().toString().length() == 0) {
                                                c_address.setError("Empty");
                                                c_address.setBackground(ContextCompat.getDrawable(c,R.drawable.pass_unmatch));
                                            }
                                            progress_monitor.Drop_Progressing();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progress_monitor.Drop_Progressing();
                                    c_name.setBackground(ContextCompat.getDrawable(c,R.drawable.pass_unmatch));
                                }
                            });


                }
            }
            else {
                Toaster("Customer name is empty");
                c_name.setError("Missing");
                progress_monitor.Drop_Progressing();
                c_name.setBackground(ContextCompat.getDrawable(c,R.drawable.pass_unmatch));
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
