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

import com.example.muneerapps.R;
import com.example.muneerapps.Reset_User;
import com.example.muneerapps.Transaction_Encoder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

public class Reset_Pin extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button button17;
    public EditText input_admin_pin;



    public Reset_Pin(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.admin_pin);
        input_admin_pin = findViewById(R.id.input_admin_pin);
        button17 = findViewById(R.id.button17);

        button17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input_admin_pin.getText().toString().length()>0)
                {
                    Transaction_Encoder transaction_encoder = new Transaction_Encoder();
                    FirebaseDatabase.getInstance().getReference("PIN_CODE")
                            .setValue(transaction_encoder.getEncoded(input_admin_pin.getText().toString())).addOnCompleteListener(c, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toaster("Key Updated Success");
                            dismiss();
                        }
                    });

                }
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
