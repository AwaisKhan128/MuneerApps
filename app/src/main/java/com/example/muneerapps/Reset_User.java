package com.example.muneerapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.muneerapps.dialogs.Customer_dialog;
import com.example.muneerapps.dialogs.Show_user_dlg;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Reset_User extends AppCompatActivity {
    ListView listView;
    List<String> user_array_list;
    ArrayAdapter<String> userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset__user);
        listView = findViewById(R.id.listView);


        FirebaseDatabase.getInstance().getReference("Users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren())
                        {
                            new Show_Users().execute();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        listView.setOnItemClickListener((adapterView, view, i, l) -> {

            Show_user_dlg cdd=new Show_user_dlg(Reset_User.this);
            SharedPreferences pref_access = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref_access.edit();
            editor.putString("User_name_key", adapterView.getAdapter().getItem(i).toString());  // Saving string
            editor.apply(); // commit changes
            cdd.show();
        });



    }


    public class Show_Users extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
                user_array_list.clear();
                userAdapter.notifyDataSetChanged();
                listView.getAdapter().notify();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user_array_list = new ArrayList<>();
                    if (snapshot.hasChildren())
                    {
                        for (DataSnapshot db :snapshot.getChildren())
                        {
                            user_array_list.add(db.child("Name").getValue(String.class));
                        }
                        userAdapter = new ArrayAdapter<String>(
                                getApplicationContext(),
                                android.R.layout.simple_list_item_1,
                                user_array_list );

                        listView.setAdapter(userAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            return null;
        }
    }
}