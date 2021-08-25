package com.example.muneerapps.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.muneerapps.R;

import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Date_Pick extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;

    List<String> Months = Arrays.asList(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});


    public Date_Pick(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }



    DatePicker datePicker1 ;
    Button button16;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.date_picker);
        datePicker1 = findViewById(R.id.datePicker1);
        button16 = findViewById(R.id.button16);

        button16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String day = ""+datePicker1.getDayOfMonth();
                String monthDate = ""+datePicker1.getMonth();
                int month1 = datePicker1.getMonth()+1;
                String month = ""+month1;
                if (!(day.substring(0,0).compareToIgnoreCase("0")==0) && day.length()==1)
                {
                    day = "0"+day;
                }


                Add2Preference("Current_Date",day+"-"+Months.get(month1-1)+"-"+datePicker1.getYear());
                Add2Preference("Discount_date",day+"-"+Months.get(month1-1)+"-"+datePicker1.getYear());
                dismiss();

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

    private void Add2Preference(String key, String  quantity)
    {
        SharedPreferences pref = c.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, quantity);  // Saving string
        // Save the changes in SharedPreferences
        editor.apply(); // commit changes
    }
    private String Retrieve_preference(String key)
    {
        SharedPreferences pref = c.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        return pref.getString(key, "");         // getting you_bool
    }



}
