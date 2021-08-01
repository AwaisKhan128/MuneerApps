package com.example.muneerapps.Transactions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.print.PrintAttributes;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muneerapps.R;
import com.example.muneerapps.Reset_User;
import com.example.muneerapps.Selector;
import com.example.muneerapps.dialogs.Category_dialog;
import com.example.muneerapps.dialogs.Show_PrintPreview;
import com.example.muneerapps.dialogs.Show_user_dlg;
import com.google.firebase.database.annotations.NotNull;
import com.tejpratapsingh.pdfcreator.utils.FileManager;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;
import com.tejpratapsingh.pdfcreator.views.PDFTableView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFLineSeparatorView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFTextView;
import com.uttampanchasara.pdfgenerator.CreatePdf;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Transaction_Adaptor extends RecyclerView.Adapter<Transaction_Holder> {
    Context context;
    View view;
    ArrayList<Transaction_Class> data;
    Transaction_Holder transaction_holder;


    public Transaction_Adaptor(Context context, ArrayList<Transaction_Class> data)
    {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public Transaction_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.transactions, parent,false);
        transaction_holder = new Transaction_Holder(view);
        return transaction_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Transaction_Holder holder, int i) {
        holder.Order_title.setText(data.get(i).getOrder_title());
        holder.Customer.setText(data.get(i).getCustomer());
        holder.Categories.setText(data.get(i).getCategory());
        holder.Products.setText(data.get(i).getProduct());
        holder.Rates.setText(data.get(i).getRate());
        holder.User.setText(data.get(i).getUser());
        holder.Quantity.setText(data.get(i).getQuantity());
        holder.Amount.setText(data.get(i).getAmount());
        holder.Amount.setTextColor(data.get(i).getAmount_color());
        holder.Date_time.setText(data.get(i).getDate_time());

        holder.imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Backgroud_PDF().execute(holder.Order_title.getText().toString()
                        ,holder.Customer.getText().toString()
                        ,holder.Categories.getText().toString()
                        ,holder.Products.getText().toString()
                        ,holder.Rates.getText().toString()
                        ,holder.User.getText().toString()
                        ,holder.Quantity.getText().toString()
                        ,holder.Amount.getText().toString()
                        ,holder.Date_time.getText().toString());
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class Backgroud_PDF extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            ArrayList<Bitmap> bmp = new ArrayList<>();
            ArrayList<String> data = new ArrayList<>();

            data.add(strings[0]);
            data.add(strings[1]);
            data.add(strings[2]);
            data.add(strings[3]);
            data.add(strings[4]);
            data.add(strings[5]);
            data.add(strings[6]);
            data.add(strings[7]);
            data.add(strings[8]);


            String[] textInTable = {"Order", "Customer", "Category", "Product"
                            , "Rate", "User", "Quantity", "Amount", "Date_time"};

            String final_Text = "Invoice (AutoGenerated) "+"\n"+
                    textInTable[0]+": "+data.get(0)+"\n"+
                    textInTable[1]+": "+data.get(1)+"\n"+
                    textInTable[2]+": "+data.get(2)+"\n"+
                    textInTable[3]+": "+data.get(3)+"\n"+
                    textInTable[4]+": "+data.get(4)+"\n"+
                    textInTable[5]+": "+data.get(5)+"\n"+
                    textInTable[6]+": "+data.get(6)+"\n"+
                    textInTable[7]+": "+data.get(7)+"\n"+
                    textInTable[8]+": "+data.get(8)+"\n";

//            PDF_Generator(final_Text);


            return final_Text;

        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);

            Show_PrintPreview cdd=new Show_PrintPreview((Activity) context);
            SharedPreferences pref_access = context.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref_access.edit();
            editor.putString("Print_Value", str);  // Saving string
            editor.apply(); // commit changes
            cdd.show();

        }
    }
    public void Toaster(String s)
    {
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
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

    void deleteExternalStoragePrivateFile() {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File file = new File(context.getExternalFilesDir(null), "DemoFile.jpg");
        file.delete();
    }

    boolean hasExternalStoragePrivateFile() {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File file = new File(context.getExternalFilesDir(null), "DemoFile.jpg");
        return file.exists();
    }

public void PDF_Generator(String content)
{
    try {


        new CreatePdf(context)
                .setPdfName("Invoice " )
                .openPrintDialog(false)
                .setContentBaseUrl(null)
                .setPageSize(PrintAttributes.MediaSize.ISO_A4)
                .setContent(content)
                .setFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyPdf.pdf")
                .setCallbackListener(new CreatePdf.PdfCallbackListener() {
                    @Override
                    public void onFailure(@NotNull String s) {
                        // handle error
                    }

                    @Override
                    public void onSuccess(@NotNull String s) {
                        // do your stuff here

                    }
                })
                .create();
    }
    catch (Exception e)
    {
        Log.e("Error is",e.getLocalizedMessage());
    }
}







}
