package com.example.muneerapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
    RadioButton radioButton,radioButton2,radioButton3,radioButton4;

    public void Reset_Users(View view)
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mContext = this;
        userName = (EditText) findViewById(R.id.userName);
        emails = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        password2 = (EditText) findViewById(R.id.password2);
        signin = (TextView) findViewById(R.id.signin);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        button3 = (Button) findViewById(R.id.button3);
        radioButton = (RadioButton) findViewById(R.id.radioButton);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        radioButton4 = (RadioButton) findViewById(R.id.radioButton4);
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



                                progressBar.setVisibility(View.GONE);
                                button3.setVisibility(View.VISIBLE);
                                userName.setText("");
                                emails.setText("");
                                password.setText("");
                                password2.setText("");
                                radioButton.setChecked(false);
                                radioButton2.setChecked(false);
                                radioButton3.setChecked(false);
                                radioButton4.setChecked(false);

                                access[0] = false;
                                access[1] = false;
                                access[2] = false;
                                access[3] = false;
                                ans.set(true);

                                FirebaseAuth.getInstance().signOut();

                            } else {
                                Toaster(task.getException().getMessage());
                                progressBar.setVisibility(View.GONE);
                                button3.setVisibility(View.VISIBLE);
                                ans.set(false);
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

    private boolean access[] = {false,false,false,false};
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
        }
    }


}