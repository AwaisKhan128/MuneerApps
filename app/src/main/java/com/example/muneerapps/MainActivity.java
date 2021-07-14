package com.example.muneerapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.Auth0Exception;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.Callback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;

public class MainActivity extends AppCompatActivity {

    private Auth0 account;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        account = new Auth0(getResources().getString(R.string.com_auth0_clientID),
                getResources().getString(R.string.com_auth0_domain));



    }

    public void LoginNow(View view)
    {
        WebAuthProvider.login(account)
                .withScheme("demo")
                .withScope("openid profile email")
                .start(mContext, new Callback<Credentials, AuthenticationException>() {
                    @Override
                    public void onSuccess(Credentials credentials) {
                        Toaster("Login Succeed Congratulations");
                    }

                    @Override
                    public void onFailure(AuthenticationException e) {
                        Toaster("Login Failed due to"+e.getDescription());
                    }
                });

    }
    public void Toaster(String s)
    {
        Toast.makeText(mContext,s,Toast.LENGTH_SHORT).show();
    }

    public void LogOut(View view)
    {
        WebAuthProvider.logout(account)
                .withScheme("demo")
                .start(mContext, new Callback<Void, AuthenticationException>() {
                    @Override
                    public void onFailure(AuthenticationException e) {
                        Toaster("Logout Failed.");
                    }

                    @Override
                    public void onSuccess(Void aVoid) {
                        Toaster("Logout Success");
                    }

                });
    }


}