package com.example.muneerapps.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.muneerapps.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class Apply_Discount extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public EditText quantity_text;
    public Button button21;
    ImageView imageView6;



    public Apply_Discount(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }
    private float RetreiveAmount(String key)
    {
        SharedPreferences pref = c.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        return pref.getFloat(key, 0);         // getting you_bool
    }
    private void AddDTotalAmount(String key, float date)
    {
        SharedPreferences pref = c.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, date);  // Saving string
        editor.apply(); // commit changes
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.discount);
        button21 = findViewById(R.id.button21);
        quantity_text = findViewById(R.id.quantity_text);

        button21.setOnClickListener(view ->
        {
            if (quantity_text.getText().toString().length()>0)
            {

                AddDTotalAmount("Discount_Amount", Float.parseFloat(quantity_text.getText().toString()+"f"));
                dismiss();



//                AddQPreference(Float.parseFloat(quantity_text.getText().toString()+"f"));

            }
            else {
                quantity_text.setError("Missing");
            }
        }
        );

    }

    private void Add2Preference(int quantity)
    {
        SharedPreferences pref = c.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("product_quant", quantity);  // Saving string
        // Save the changes in SharedPreferences
        editor.apply(); // commit changes
    }
    private void AddDPreference(String date)
    {
        SharedPreferences pref = c.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Discount_date", date);  // Saving string
        editor.apply(); // commit changes
    }
    private String Retrieve_Date(String key)
    {
        SharedPreferences pref = c.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        return pref.getString(key, "");         // getting you_bool
    }


    public void Toaster(String s)
    {
        Toast.makeText(c.getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

    }




}
