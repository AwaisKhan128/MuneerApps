package com.example.muneerapps.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.muneerapps.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Category_dialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;

    public EditText category_reg;
    public Button button9;


    public Category_dialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }
    Progress_Monitor progress_monitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_categ);
        category_reg = (EditText) findViewById(R.id.category_reg);
        button9 = (Button) findViewById(R.id.button9);
        progress_monitor = new Progress_Monitor(c);

        button9.setOnClickListener(view -> {

            progress_monitor.Setup_Progressing("Please Wait","Working in Progress");
            if (category_reg.getText().toString().length()>0) {
                FirebaseDatabase.getInstance().getReference("Categories")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChildren()) {
                                    FirebaseDatabase
                                            .getInstance().getReference("Categories")
                                            .child(String.valueOf(snapshot.getChildrenCount())).setValue(category_reg.getText().toString());
                                } else {
                                    FirebaseDatabase
                                            .getInstance().getReference("Categories")
                                            .child(String.valueOf(0)).setValue(category_reg.getText().toString());
                                }
                                category_reg.setText("");
                                Toaster("Success");
                                progress_monitor.Drop_Progressing();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toaster("Error due to "+error.getDetails());
                                progress_monitor.Drop_Progressing();
                            }
                        });
            }
            else {
                Toaster("Empty Blocks");
                category_reg.setError("Empty Blocks");
                progress_monitor.Drop_Progressing();
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
