package com.example.muneerapps;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends AppCompatActivity {

    Context mContext;
    EditText userName, emails,password,password2;
    TextView signin;
    Button button3;
    ProgressBar progressBar;

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
        progressBar.setVisibility(View.GONE);
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
                register_firebase_user.execute(email, password_u);
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
//            if ( !password_u.equals("") && !password2_u.equals("") )
            {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(strings[0], strings[1])
                        .addOnCompleteListener(MainActivity.this, task -> {
                            if (task.isSuccessful()) {
                                Toaster("User Created Successfully");
                                progressBar.setVisibility(View.GONE);
                                button3.setVisibility(View.VISIBLE);
                                ans.set(true);

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


}