package com.example.muneerapps;

import com.google.firebase.database.FirebaseDatabase;

public class Firebase_class extends android.app.Application  {

    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
