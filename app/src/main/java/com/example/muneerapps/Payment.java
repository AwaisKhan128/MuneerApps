package com.example.muneerapps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.muneerapps.dialogs.Addcash_dialog;
import com.example.muneerapps.dialogs.Category_dialog;
import com.example.muneerapps.dialogs.Subcash_dialog;

public class Payment
        extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_sheet);
    }

    public void cashout(View view)
    {
        Subcash_dialog cdd=new Subcash_dialog(Payment.this);
        cdd.show();
    }

    public void cashin(View view)
    {
        Addcash_dialog cdd=new Addcash_dialog(Payment.this);
        cdd.show();

    }
}