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

import com.example.muneerapps.R;
import com.google.android.material.textfield.TextInputLayout;

public class Product_dialog extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public EditText title,description;
    public RadioButton radioButton,radioButton2,radioButton3;


    public Button button11;

    public TextInputLayout product_cat;
    public EditText product_name, product_unit, price;


    public Product_dialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_product);
        product_cat = (TextInputLayout) findViewById(R.id.product_cat);
        product_name = (EditText) findViewById(R.id.product_name);
        product_unit = (EditText) findViewById(R.id.product_unit);
        price = (EditText) findViewById(R.id.price);
        button11 = (Button) findViewById(R.id.button11);


    }

    public void Toaster(String s)
    {
        Toast.makeText(c.getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

    }
}
