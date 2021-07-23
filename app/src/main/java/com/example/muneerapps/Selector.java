package com.example.muneerapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.muneerapps.dialogs.Category_dialog;
import com.example.muneerapps.dialogs.Customer_dialog;
import com.example.muneerapps.dialogs.Product_dialog;

public class Selector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectors);
    }

    public void create_custom(View view)
    {
        Customer_dialog cdd=new Customer_dialog(Selector.this);
        cdd.show();

    }

    public void create_categ(View view)
    {
        Category_dialog cdd=new Category_dialog(Selector.this);
        cdd.show();
    }

    public void create_pay(View view)
    {
        startActivity(new Intent(Selector.this,Payment.class));
    }

    public void create_prod(View view)
    {
        Product_dialog cdd=new Product_dialog(Selector.this);
        cdd.show();
    }

}