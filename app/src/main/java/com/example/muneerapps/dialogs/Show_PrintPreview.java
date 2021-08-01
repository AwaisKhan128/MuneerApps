package com.example.muneerapps.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.muneerapps.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;
import com.tejpratapsingh.pdfcreator.views.PDFTableView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFLineSeparatorView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFTextView;

import java.io.File;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class Show_PrintPreview extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;

    public Uri myUri;




    public Show_PrintPreview(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }


    TextView textView14,textView15;
    Button button16;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.show_printprev);
        textView14 = findViewById(R.id.textView14);
        textView15 = findViewById(R.id.textView15);
        button16= findViewById(R.id.button16);
        button16.setVisibility(View.GONE);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        button16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent share = new Intent(Intent.ACTION_SEND);

                    // If you want to share a png image only, you can do:
                    // setType("image/png"); OR for jpeg: setType("image/jpeg");
                    share.setType("pdf/*");

                    // Make sure you put example png image named myImage.png in your
                    // directory
//                    String imagePath = Environment.getExternalStorageDirectory()
//                            + "/myImage.png";
//
//                    File imageFileToShare = new File(imagePath);
//
//                    Uri uri = Uri.fromFile(imageFileToShare);
                    Toaster(String.valueOf(myUri));
                    share.putExtra(Intent.EXTRA_STREAM, myUri);

                    c.startActivity(Intent.createChooser(share, "Share PDF!"));

// Try to invoke the intent.

//                    c.startActivity(chooser);
                } catch (Exception e) {
                    // Define what your app should do if no activity can handle the intent.
                    Log.e("Share error",e.getLocalizedMessage());
                }
            }
        });
        SharedPreferences pref1 =  c.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String publish_At = pref1.getString("Print_Value", null);

        textView14.setText(publish_At);

        textView15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewPdf();
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

    public void CreateNewPdf()
    {
        {

//            String[] textInTable = {"Order", "Customer", "Category", "Product"
//                    , "Rate", "User", "Quantity", "Amount", "Date_time"};
//
//// Create table column headers
//            PDFTableView.PDFTableRowView tableHeader = new PDFTableView.PDFTableRowView(c.getApplicationContext());
//            for (String s : textInTable) {
//                PDFTextView pdfTextView = new PDFTextView(c.getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
//                pdfTextView.setText("Header Title: " + s);
//                tableHeader.addToRow(pdfTextView);
//            }
//// Create first row
//            PDFTableView.PDFTableRowView tableRowView1 = new PDFTableView.PDFTableRowView(c.getApplicationContext());
//            for (String s : data) {
//                PDFTextView pdfTextView = new PDFTextView(c.getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
//                pdfTextView.setText("Row 1 : " + s);
//                tableRowView1.addToRow(pdfTextView);
//            }
//
//// PDFTableView takes table header and first row at once because if page ends after adding header then first row will be on next page. To avoid confusion to user, table header and first row is printed together.
//            PDFTableView tableView = new PDFTableView(c.getApplicationContext(), tableHeader, tableRowView1);
//            for (int i = 0; i < 9; i++) {
//                // Create 10 rows and add to table.
//                PDFTableView.PDFTableRowView tableRowView = new PDFTableView.PDFTableRowView(c.getApplicationContext());
//                for (String s : data) {
//                    PDFTextView pdfTextView = new PDFTextView(c.getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
//                    pdfTextView.setText("" + textInTable[i] + ": " + s);
//                    tableRowView.addToRow(pdfTextView);
//                }
//                tableView.addRow(tableRowView);
//            }
//
////                tableView.getView().setMinimumHeight(20);
////                tableView.getView().setMinimumWidth(20);
//            LayoutInflater vi = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View myText = tableView.getView();
//
//            PDFLineSeparatorView lineSeparatorWhite = new PDFLineSeparatorView(c.getApplicationContext()).setBackgroundColor(Color.WHITE);
//            PDFLineSeparatorView lineSeparatorBlack = new PDFLineSeparatorView(c.getApplicationContext()).setBackgroundColor(Color.BLACK);
//// Get View
//            View separatorView = lineSeparatorWhite.getView();
//

// Get View

            List<View> mylist = new ArrayList<>();

            mylist.add(textView14);



//                final File savedPDFFile = FileManager.getInstance().createTempFile(context, "pdf", false);
//                Uri pdfUri = Uri.fromFile(savedPDFFile);
            try {

                PDFUtil.getInstance().generatePDF(mylist, String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"muneer.pdf"), new PDFUtil.PDFUtilListener() {
                    @Override
                    public void pdfGenerationSuccess(File savedPDFFile) {

//                        Toaster("PDF Created Success");
                        try {
//                            Intent share = new Intent(Intent.ACTION_SEND);
                            Uri pdfUri = Uri.fromFile(savedPDFFile);
                            Log.e("URI : ", String.valueOf(pdfUri));
                            button16.setVisibility(View.VISIBLE);
                            myUri = pdfUri;





//                            Uri uri = Uri.fromFile(savedPDFFile);
//                            share.putExtra(Intent.EXTRA_STREAM, uri);
//
//                            c.getApplicationContext().startActivity(Intent.createChooser(share, "Share PDF!"));

                            }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void pdfGenerationFailure(Exception exception) {
                        try{
                        Log.e("Generate Error : ", String.valueOf(exception.getLocalizedMessage()));}
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

//                        Toaster("Error Processed");
                    }
                });
            }
            catch (Exception e)
            {
                Log.e("Write Error",e.getLocalizedMessage());
            }


        }
    }
    private String createFolder(Context context, String folderName) {

        //getting app directory
        final File externalFileDir = context.getExternalFilesDir(null);

        //creating new folder instance
        File createdDir = new File(externalFileDir.getAbsoluteFile(),folderName);

        if(!createdDir.exists()){

            //making new directory if it doesn't exist already
            createdDir.mkdir();

        }
        return externalFileDir.getAbsolutePath() ;
    }


        private void sharePDF(File file) {
            Intent share = new Intent(Intent.ACTION_SEND);

            // If you want to share a png image only, you can do:
            // setType("image/png"); OR for jpeg: setType("image/jpeg");
            share.setType("pdf/*");

            // Make sure you put example png image named myImage.png in your
            // directory

            File imageFileToShare = file;

            Uri uri = Uri.fromFile(imageFileToShare);
            share.putExtra(Intent.EXTRA_STREAM, uri);

            c.startActivity(Intent.createChooser(share, "Share PDF!"));
        }


}
