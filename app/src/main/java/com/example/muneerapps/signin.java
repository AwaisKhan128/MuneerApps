package com.example.muneerapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import connection.Connection;

public class signin extends AppCompatActivity {
    Context mContext;
    ProgressBar progressBar2;
    Button button13;
    EditText email2,password3;
    TextView forgot_pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mContext = this;
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        button13 = (Button) findViewById(R.id.button13);
        email2 = (EditText) findViewById(R.id.email2);
        forgot_pass = findViewById(R.id.forgot_pass);

        email2.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                email2.setBackground(ContextCompat.getDrawable(mContext,R.drawable.dialog_text1));
            }
            if (!hasFocus){
                email2.setBackground(ContextCompat.getDrawable(mContext,R.drawable.dialog_text));

            }
        });
        password3 = (EditText) findViewById(R.id.password3);
        password3.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                password3.setBackground(ContextCompat.getDrawable(mContext,R.drawable.dialog_text1));
            }
            if (!hasFocus){
                password3.setBackground(ContextCompat.getDrawable(mContext,R.drawable.dialog_text));

            }
        });
        progressBar2.setVisibility(View.GONE);
    }

    public void go_forward(View view)
    {
        startActivity(new Intent(this,Selector.class));
    }

    public void SignIN(View view)
    {
        if (email2.getText().toString().length()>0 && password3.getText().toString().length()>=6)
        {
            Sign_In sign_in = new Sign_In();
            sign_in.execute(email2.getText().toString(),password3.getText().toString());

        }
    }

    public class Sign_In extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar2.setVisibility(View.VISIBLE);
            button13.setVisibility(View.GONE);
            forgot_pass.setClickable(false);

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
                FirebaseAuth.getInstance().signInWithEmailAndPassword(strings[0],strings[1])
                        .addOnCompleteListener(signin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                {
                                    Toaster("User Sign In Successfully");
                                    progressBar2.setVisibility(View.GONE);
                                    button13.setVisibility(View.VISIBLE);
                                    forgot_pass.setClickable(true);
                                    reset_focus();
                                    startActivity(new Intent(signin.this,Selector.class));

                                }
                                else
                                {
                                    Toaster("User Sign In Failed due to "+task.getException().getLocalizedMessage());
                                    progressBar2.setVisibility(View.GONE);
                                    button13.setVisibility(View.VISIBLE);
                                    forgot_pass.setClickable(true);
                                    reset_focus();
                                }
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

    public void Toaster(String s)
    {
        Toast.makeText(mContext,s,Toast.LENGTH_SHORT).show();
    }

    public void forget_pass(View view)
    {
        if (email2.getText().toString().length()>0 )
        {
            try{
            FirebaseAuth.getInstance().sendPasswordResetEmail(email2.getText().toString());
            Toaster("Reset Email sent successfully to you");
            reset_focus();
            }
            catch (Exception e){
                Toaster("Reset Email sent Unsuccessful due to "+e.getLocalizedMessage());
            }
        }
        else
        {
            Toaster("Type your email in the box");
            email2.setError("Blank");
            email2.setBackground(ContextCompat.getDrawable(mContext,R.drawable.pass_unmatch));
        }
    }

    public void reset_focus()
    {
        email2.setBackground(ContextCompat.getDrawable(mContext,R.drawable.dialog_text));
        password3.setBackground(ContextCompat.getDrawable(mContext,R.drawable.dialog_text));
    }
}