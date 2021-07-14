package com.example.muneerapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

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
                .start(mContext, (Callback)(new Callback() {

            public void onFailure(Auth0Exception var1) {
                this.onFailure((AuthenticationException)var1);
            }



            public void onSuccess(Object var1) {
                this.onSuccess((Credentials)var1);
                String accessToken = ((Credentials) var1).getAccessToken();
            }
        }));

    }


}