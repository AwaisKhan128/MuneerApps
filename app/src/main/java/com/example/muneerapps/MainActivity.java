package com.example.muneerapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muneerapps.dialogs.Close_Shop;
import com.example.muneerapps.dialogs.Customer_dialog;
import com.example.muneerapps.dialogs.Reset_Pin;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends AppCompatActivity {

    Context mContext;
    EditText userName, emails,password,password2;
    TextView signin;
    Button button3;
    ProgressBar progressBar;
    RadioButton radioButton,radioButton2,radioButton3,radioButton4
            ,radioButton14,radioButton13,radioButton12,radioButton15,radioButton16;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset_pin:
                Reset_Pin();
                return true;
            case R.id.reset_User:
                Reset_Users();
                return true;
            case R.id.payments_reset:
                Reset_Payments();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
//        return super.onOptionsItemSelected(item);
    }

    private void Reset_Payments() {

        Close_Shop close_shop = new Close_Shop(MainActivity.this);
        close_shop.show();

    }

    private void Reset_Pin() {

        Reset_Pin cdd=new Reset_Pin(MainActivity.this);
        cdd.show();

    }

    public void Reset_Users()
    {
        startActivity(new Intent(MainActivity.this,Reset_User.class));

    }

    public void signin_transact(View view)
    {
        startActivity(new Intent(this,signin.class));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().setTitle("Signup");
        setContentView(R.layout.activity_main);
        mContext = this;
        userName = (EditText) findViewById(R.id.userName);
        userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    userName.setBackground(ContextCompat.getDrawable(mContext,R.drawable.dialog_text1));
                }
                if (!hasFocus){
                    userName.setBackground(ContextCompat.getDrawable(mContext,R.drawable.dialog_text));

                }
            }
        });
        emails = (EditText) findViewById(R.id.email);
        emails.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    emails.setBackground(ContextCompat.getDrawable(mContext,R.drawable.dialog_text1));
                }
                if (!hasFocus){
                    emails.setBackground(ContextCompat.getDrawable(mContext,R.drawable.dialog_text));

                }
            }
        });
        password = (EditText) findViewById(R.id.password);
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    password.setBackground(ContextCompat.getDrawable(mContext,R.drawable.dialog_text1));
                }
                if (!hasFocus){
                    password.setBackground(ContextCompat.getDrawable(mContext,R.drawable.dialog_text));

                }
            }
        });
        password2 = (EditText) findViewById(R.id.password2);
        password2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    password2.setBackground(ContextCompat.getDrawable(mContext,R.drawable.dialog_text1));
                }
                if (!hasFocus){
                    password2.setBackground(ContextCompat.getDrawable(mContext,R.drawable.dialog_text));

                }
            }
        });

        password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length()>0 && password.getText().toString().length()>0)
                {
                    new Password_Monitor().execute(password.getText().toString(),charSequence.toString());
                }
                else
                {
                    password.setBackground(ContextCompat.getDrawable(mContext,R.drawable.dialog_text));
                    password2.setBackground(ContextCompat.getDrawable(mContext,R.drawable.dialog_text1));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        signin = (TextView) findViewById(R.id.signin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        button3 = (Button) findViewById(R.id.button3);
        radioButton = (RadioButton) findViewById(R.id.radioButton); //Customer Registration
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);//Category Registration
        radioButton3 = (RadioButton) findViewById(R.id.radioButton3);//Product Registration
        radioButton4 = (RadioButton) findViewById(R.id.radioButton4);//Payment Check
        radioButton12 = (RadioButton) findViewById(R.id.radioButton12);//Product Update
        radioButton13 = (RadioButton) findViewById(R.id.radioButton13);//Customer Update
        radioButton14 = (RadioButton) findViewById(R.id.radioButton14);//Supplier Registration
        radioButton15 = (RadioButton) findViewById(R.id.radioButton15);//Supplier Update
        radioButton16 = (RadioButton) findViewById(R.id.radioButton16);//Payment Overview
        progressBar.setVisibility(View.GONE);
        FirebaseApp.initializeApp(this);
    }

    public void setButton3(View view)
    {
        String user = userName.getText().toString();
        String email = emails.getText().toString();
        String password_u = password.getText().toString();
        String password2_u = password2.getText().toString();

        if (password_u.compareToIgnoreCase(password2_u)==0 && password_u.length()>=6)
        {
            if (email.compareToIgnoreCase("")==0 && user.compareToIgnoreCase("")==0) {
                return;
            }
            else
            {
                Register_firebase_user register_firebase_user = new Register_firebase_user();
                register_firebase_user.execute(email, password_u,user);
            }

        }
        else
        {
            Toaster("Passwords must match or check length > 6");
            password.setError("Not Match");
            password2.setError("Not Match");
            reset_focus();
            password.setBackground(ContextCompat.getDrawable(mContext,R.drawable.pass_unmatch));
            password2.setBackground(ContextCompat.getDrawable(mContext,R.drawable.pass_unmatch));
        }




    }

    public void Toaster(String s)
    {
        Toast.makeText(mContext,s,Toast.LENGTH_SHORT).show();
    }

    public void LoginNow(View view)
    {

    }



    public class Register_firebase_user extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            button3.setVisibility(View.GONE);
            signin.setClickable(false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected String doInBackground(String... strings) {
            AtomicBoolean ans = new AtomicBoolean(false);

            {
                FirebaseDatabase.getInstance().getReference("Users")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChildren())
                                {
                                    boolean hasName = false;

                                    for (DataSnapshot db: snapshot.getChildren())
                                    {
                                        if (db.child("Name").getValue(String.class).compareToIgnoreCase(strings[2])==0){
                                            hasName = true;
                                        }

                                    }
                                    if (!hasName)
                                    {
                                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(strings[0], strings[1])
                                                .addOnCompleteListener(MainActivity.this, task -> {
                                                    if (task.isSuccessful()) {
                                                        Toaster("User Created Successfully");
                                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                                                .getReference("Users").child(FirebaseAuth.getInstance()
                                                                        .getCurrentUser().getUid());

                                                        databaseReference
                                                                .child("Name").setValue(strings[2]);



                                                        databaseReference
                                                                .child("Access").child("Customer").setValue(access[0]);

                                                        databaseReference
                                                                .child("Access").child("Category").setValue(access[1]);

                                                        databaseReference
                                                                .child("Access").child("Product").setValue(access[2]);

                                                        databaseReference
                                                                .child("Access").child("Payment").setValue(access[3]);

                                                        databaseReference
                                                                .child("Access").child("Product_Update").setValue(access[4]);

                                                        databaseReference
                                                                .child("Access").child("Customer_Update").setValue(access[5]);

                                                        databaseReference
                                                                .child("Access").child("Supplier").setValue(access[6]);

                                                        databaseReference
                                                                .child("Access").child("Supplier_Update").setValue(access[7]);

                                                        databaseReference
                                                                .child("Access").child("Payment_Overview").setValue(access[8]);




                                                        progressBar.setVisibility(View.GONE);
                                                        button3.setVisibility(View.VISIBLE);
                                                        signin.setClickable(true);
                                                        userName.setText("");
                                                        emails.setText("");
                                                        password.setText("");
                                                        password2.setText("");
                                                        radioButton.setChecked(false);
                                                        radioButton2.setChecked(false);
                                                        radioButton3.setChecked(false);
                                                        radioButton4.setChecked(false);

                                                        radioButton12.setChecked(false);
                                                        radioButton13.setChecked(false);
                                                        radioButton14.setChecked(false);
                                                        radioButton15.setChecked(false);
                                                        radioButton16.setChecked(false);

                                                        access[0] = false;
                                                        access[1] = false;
                                                        access[2] = false;
                                                        access[3] = false;

                                                        access[4] = false;
                                                        access[5] = false;
                                                        access[6] = false;
                                                        access[7] = false;
                                                        access[8] = false;
                                                        ans.set(true);
                                                        reset_focus();
                                                        FirebaseAuth.getInstance().signOut();

                                                    } else {
                                                        Toaster(task.getException().getMessage());
                                                        progressBar.setVisibility(View.GONE);
                                                        button3.setVisibility(View.VISIBLE);
                                                        signin.setClickable(true);
                                                        ans.set(false);
                                                        reset_focus();
                                                    }
                                                }).addOnCanceledListener(new OnCanceledListener() {
                                            @Override
                                            public void onCanceled() {
                                                progressBar.setVisibility(View.GONE);
                                                button3.setVisibility(View.VISIBLE);
                                                signin.setClickable(true);
                                                ans.set(false);
                                                reset_focus();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressBar.setVisibility(View.GONE);
                                                button3.setVisibility(View.VISIBLE);
                                                signin.setClickable(true);
                                                ans.set(false);
                                                reset_focus();
                                            }
                                        });
                                    }
                                    else {
                                        progressBar.setVisibility(View.GONE);
                                        button3.setVisibility(View.VISIBLE);
                                        signin.setClickable(true);
                                        ans.set(false);
                                        reset_focus();
                                        userName.setBackground(ContextCompat.getDrawable(mContext,R.drawable.pass_unmatch));
                                        Toaster("Name already in database try different");
                                    }

                                }
                                else
                                {
                                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(strings[0], strings[1])
                                            .addOnCompleteListener(MainActivity.this, task -> {
                                                if (task.isSuccessful()) {
                                                    Toaster("User Created Successfully");
                                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                                            .getReference("Users").child(FirebaseAuth.getInstance()
                                                                    .getCurrentUser().getUid());

                                                    databaseReference
                                                            .child("Name").setValue(strings[2]);

                                                    databaseReference
                                                            .child("Access").child("Customer").setValue(access[0]);

                                                    databaseReference
                                                            .child("Access").child("Category").setValue(access[1]);

                                                    databaseReference
                                                            .child("Access").child("Product").setValue(access[2]);

                                                    databaseReference
                                                            .child("Access").child("Payment").setValue(access[3]);

                                                    databaseReference
                                                            .child("Access").child("Product_Update").setValue(access[4]);

                                                    databaseReference
                                                            .child("Access").child("Customer_Update").setValue(access[5]);

                                                    databaseReference
                                                            .child("Access").child("Supplier").setValue(access[6]);

                                                    databaseReference
                                                            .child("Access").child("Supplier_Update").setValue(access[7]);

                                                    databaseReference
                                                            .child("Access").child("Payment_Overview").setValue(access[8]);




                                                    progressBar.setVisibility(View.GONE);
                                                    button3.setVisibility(View.VISIBLE);
                                                    signin.setClickable(true);
                                                    userName.setText("");
                                                    emails.setText("");
                                                    password.setText("");
                                                    password2.setText("");
                                                    radioButton.setChecked(false);
                                                    radioButton2.setChecked(false);
                                                    radioButton3.setChecked(false);
                                                    radioButton4.setChecked(false);

                                                    radioButton12.setChecked(false);
                                                    radioButton13.setChecked(false);
                                                    radioButton14.setChecked(false);
                                                    radioButton15.setChecked(false);
                                                    radioButton16.setChecked(false);

                                                    access[0] = false;
                                                    access[1] = false;
                                                    access[2] = false;
                                                    access[3] = false;

                                                    access[4] = false;
                                                    access[5] = false;
                                                    access[6] = false;
                                                    access[7] = false;
                                                    access[8] = false;
                                                    ans.set(true);
                                                    reset_focus();

                                                    FirebaseAuth.getInstance().signOut();

                                                } else {
                                                    Toaster(task.getException().getMessage());
                                                    progressBar.setVisibility(View.GONE);
                                                    button3.setVisibility(View.VISIBLE);
                                                    signin.setClickable(true);
                                                    ans.set(false);
                                                    reset_focus();
                                                }
                                            }).addOnCanceledListener(() -> {
                                                progressBar.setVisibility(View.GONE);
                                                button3.setVisibility(View.VISIBLE);
                                                signin.setClickable(true);
                                                ans.set(false);
                                            }).addOnFailureListener(e -> {
                                        progressBar.setVisibility(View.GONE);
                                        button3.setVisibility(View.VISIBLE);
                                        signin.setClickable(true);
                                        ans.set(false);
                                        reset_focus();
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toaster(error.getMessage());
                                progressBar.setVisibility(View.GONE);
                                button3.setVisibility(View.VISIBLE);
                                signin.setClickable(true);
                                ans.set(false);
                                reset_focus();
                            }
                        });


            }
            if (ans.get())
            {
                return "Success";
            }
            else
            {
                return "Failed";
            }


        }
    }

    private boolean access[] = {false,false,false,false, false,false,false,false,false};
    public void Access(View view)
    {
        switch (view.getId())
        {
            case R.id.radioButton:
//                radioButton.toggle();
                access[0]= !access[0];
                radioButton.setChecked(access[0]);
                break;

            case R.id.radioButton2:
//                radioButton2.toggle();
                access[1]= !access[1];
                radioButton2.setChecked(access[1]);
                break;

            case R.id.radioButton3:
//                radioButton3.toggle();
                access[2]= !access[2];
                radioButton3.setChecked(access[2]);
                break;

            case R.id.radioButton4:
//                radioButton4.toggle();
                access[3]= !access[3];
                radioButton4.setChecked(access[3]);
                break;

            case R.id.radioButton12:
//                radioButton4.toggle();
                access[4]= !access[4];
                radioButton12.setChecked(access[4]);
                break;

            case R.id.radioButton13:
                access[5]= !access[5];
                radioButton13.setChecked(access[5]);
                break;

            case R.id.radioButton14:
                access[6]= !access[6];
                radioButton14.setChecked(access[6]);
                break;

            case R.id.radioButton15:
                access[7]= !access[7];
                radioButton15.setChecked(access[7]);
                break;

            case R.id.radioButton16:
                access[8]= !access[8];
                radioButton16.setChecked(access[8]);
                break;
        }
    }

    public class Password_Monitor extends AsyncTask<String,Void,Void>
    {

        @Override
        protected Void doInBackground(String... strings) {
            if (strings[0].compareToIgnoreCase(strings[1])==0)
            {
                password.setBackground(ContextCompat.getDrawable(mContext,R.drawable.pass_match));
                password2.setBackground(ContextCompat.getDrawable(mContext,R.drawable.pass_match));

            }
            else
            {
                password.setBackground(ContextCompat.getDrawable(mContext,R.drawable.pass_unmatch));
                password2.setBackground(ContextCompat.getDrawable(mContext,R.drawable.pass_unmatch));
            }


            return null;
        }
    }
    public void reset_focus()
    {
        userName.setBackground(ContextCompat.getDrawable(mContext,R.drawable.dialog_text));
        emails.setBackground(ContextCompat.getDrawable(mContext,R.drawable.dialog_text));
        password.setBackground(ContextCompat.getDrawable(mContext,R.drawable.dialog_text));
        password2.setBackground(ContextCompat.getDrawable(mContext,R.drawable.dialog_text));
    }



}