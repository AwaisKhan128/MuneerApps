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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class update_prod_dlg extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public EditText prev_prod, new_prod, categ, price, unit;
    public Button update_Prod;


    public update_prod_dlg(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_customer);
        update_Prod = (Button) findViewById(R.id.update_Prod);
        prev_prod = findViewById(R.id.prev_prod);
        new_prod = findViewById(R.id.new_prod);
        categ = findViewById(R.id.categ);
        price = findViewById(R.id.price);
        unit = findViewById(R.id.unit);



    }



    public void Toaster(String s)
    {
        Toast.makeText(c.getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

    }
}
