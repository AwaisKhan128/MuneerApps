package com.example.muneerapps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muneerapps.dialogs.Deadline;
import com.example.muneerapps.dialogs.Reset_Pin;
import com.example.muneerapps.dialogs.Update_Notice;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.w3c.dom.Text;

public class starter extends AppCompatActivity {
    Context mContext;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth mAuth;
    ProgressBar progressBar4;

    @Override
    protected void onStart() {
        super.onStart();
        authStateListener.onAuthStateChanged(mAuth);

    }

    public void Enable_Button() {
        button.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        progressBar4.setVisibility(View.GONE);
        button.setClickable(true);
        button2.setClickable(true);

    }

    public void Disable_Button() {
        button.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);
        progressBar4.setVisibility(View.VISIBLE);
        button.setClickable(false);
        button2.setClickable(false);
    }

    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_front, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                Refresh_Internet();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Refresh_Internet() {
        if (internetIsConnected()) {
            Enable_Button();
            button.setClickable(true);
            button2.setClickable(true);
        } else {
            Enable_Button();
            button.setClickable(false);
            button2.setClickable(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                if (Environment.isExternalStorageManager()) {

                    Toaster("Permission Granted Successfully");
                    // do your Work here
//
//                    CreatePDFFile(context.getExternalFilesDir(null).getAbsolutePath() +"/test_pdf.pdf");
//                    Toast.makeText(context, "PDF Generated", Toast.LENGTH_SHORT).show();

                } else {

                    Toaster("Permission Failed Executed");
//                    Toast.makeText(context, "PDF Not Generated because of permission restriction", Toast.LENGTH_LONG).show();
                }
            }

        }
    }


    TextView textView26;
    Button button, button2;
    Handler handler;
    String currentVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        mContext = this;
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        progressBar4 = findViewById(R.id.progressBar4);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        new GetVersionCode().execute();
        cdd = new Deadline(starter.this);
        Disable_Button();


        textView26 = findViewById(R.id.textView26);
        authStateListener = firebaseAuth -> {

            FirebaseDatabase.getInstance().getReference("Deadline").child("Key")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                boolean key = snapshot.getValue(Boolean.class);
                                if (key) {
                                    Reset_deadline();
                                } else {
                                    cdd.dismiss();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        // Sign in logic here.

                                        startActivity(new Intent(starter.this, Selector.class));
                                    }


                                }

                            } else {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    // Sign in logic here.

                                    startActivity(new Intent(starter.this, Selector.class));
                                }

                            }
                            Enable_Button();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Enable_Button();
                        }
                    });


        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission(starter.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(starter.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

            } else {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", new Object[]{getApplicationContext().getPackageName()})));
                    startActivityIfNeeded(intent, 2000);


                } catch (Exception e) {

                    Intent obj = new Intent();
                    obj.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(obj, 2000);

                }
            }

        } else {
            Check_Permissions();
        }

        FirebaseDatabase.getInstance().getReference("Deadline")
                .child("Date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Enable_Button();
                    textView26.setText("Deadline is " + snapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Enable_Button();
            }
        });


    }

    public void Check_Permissions() {
        if (ContextCompat.checkSelfPermission(starter.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(starter.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This Muneer Apps required your Storage Permission")
                    .setPositiveButton("ok", (dialog, which) -> ActivityCompat.requestPermissions(starter.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                                    , Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1))
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();

        }
    }

    public void adminOnclick(View view) {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(mContext);
        View promptsView = li.inflate(R.layout.prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mContext);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        (dialog, id) -> {
                            // get user input and set it to result
                            // edit text
                            FirebaseDatabase.getInstance().getReference("PIN_CODE").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String pin = new Transaction_Encoder().getDecoded(snapshot.getValue(String.class));
                                        if (userInput.getText().toString().equals(pin)) {
                                            Toaster("Verification Succeed");
                                            startActivity(new Intent(starter.this, MainActivity.class));
                                        } else {
                                            Toaster("Verification Failed");

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void userOnClick(View view) {
        startActivity(new Intent(starter.this, signin.class));
    }

    public void Toaster(String s) {
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }

    Deadline cdd;

    private void Reset_deadline() {

        Enable_Button();
        cdd.setCanceledOnTouchOutside(false);
        cdd.setCancelable(false);
        cdd.show();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        FirebaseDatabase.getInstance().getReference("Deadline").child("Key")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            boolean key = snapshot.getValue(Boolean.class);
                            if (key) {
                                Reset_deadline();
                            }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private class GetVersionCode extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + starter.this.getPackageName() + "&hl=it")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select(".hAyfc .htlgb")
                        .get(7)
                        .ownText();
                return newVersion;
            } catch (Exception e) {
                return newVersion;
            }
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
                    //show dialog
                    Update_Notice update_notice = new Update_Notice(starter.this);
                    update_notice.setCanceledOnTouchOutside(false);
                    update_notice.show();
                }
            }
        }
    }
}