package com.example.muneerapps.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muneerapps.R;

import static android.content.Context.MODE_PRIVATE;

public class Quanity_Class extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public EditText quantity_text;
    public Button button21;



    public Quanity_Class(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.quanity);
        button21 = findViewById(R.id.button21);
        quantity_text = findViewById(R.id.quantity_text);


        button21.setOnClickListener(view ->
        {
            if (quantity_text.getText().toString().length()>0)
            {
                Add2Preference(Integer.parseInt(quantity_text.getText().toString()));
                dismiss();
            }
            else {
                quantity_text.setError("Missing");
            }
        });

    }

    private void Add2Preference(int quantity)
    {
        SharedPreferences pref = c.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("product_quant", quantity);  // Saving string
        // Save the changes in SharedPreferences
        editor.apply(); // commit changes
    }


    public void Toaster(String s)
    {
        Toast.makeText(c.getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

    }



}
