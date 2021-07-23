package com.example.muneerapps.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.muneerapps.R;
import com.google.android.material.textfield.TextInputLayout;

public class Addcash_dialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;

    public TextInputLayout select_custom, select_categ, select_prod, select_rate;
    public EditText quatity_add;
    public TextView textView10;
    public Button button12;


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
        select_custom = (TextInputLayout) findViewById(R.id.select_custom);
        select_categ = (TextInputLayout) findViewById(R.id.select_categ);
        select_prod = (TextInputLayout) findViewById(R.id.select_prod);
        select_rate = (TextInputLayout) findViewById(R.id.select_rate);


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
}
