package com.example.muneerapps.Transactional;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.print.PrintAttributes;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muneerapps.Payment;
import com.example.muneerapps.R;
import com.example.muneerapps.Transactions.Transaction_Class;
import com.example.muneerapps.Transactions.Transaction_Holder;
import com.example.muneerapps.dialogs.Show_PrintPreview;
import com.google.firebase.database.annotations.NotNull;
import com.uttampanchasara.pdfgenerator.CreatePdf;

import java.io.File;
import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.MODE_PRIVATE;

public class Transactional_Adaptor extends RecyclerView.Adapter<Transactional_Holder> {
    Context context;
    View view;
    ArrayList<Transactional_Class> data;
    Transactional_Holder transaction_holder;
    final static int conn = 3333;



    public Transactional_Adaptor(Context context, ArrayList<Transactional_Class> data)
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
    public Transactional_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.transactional, parent,false);
        transaction_holder = new Transactional_Holder(view);
        return transaction_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Transactional_Holder holder, int i) {
        holder.key.setText(data.get(i).getKey());
        holder.Status.setText(data.get(i).getStatus());
        holder.pays.setText(data.get(i).getPays());
        holder.Customer_key.setText(data.get(i).getCustomer_key());
        holder.customer_val.setText(data.get(i).getCustomer_val());
        holder.product_val.setText(data.get(i).getProduct_val());
        holder.total_val.setText(data.get(i).getTotal_val());
        holder.due_val.setText(data.get(i).getDue_val());
        holder.paid_val.setText(data.get(i).getPaid_val());

        holder.imageView2.setOnClickListener(view -> {


        });
        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public void Toaster(String s)
    {
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }


    public void Permission_Monitor()
    {
        if (checkPermission()) {
//            CreatePDFFile(context.getExternalFilesDir(null).getAbsolutePath() +"/test_pdf.pdf");
            Toaster("PDR Generated");
        } else{

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Allow Storage Permission to Generate and Store PDF").setTitle("Storage Permission Required");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    RequestPermission();
                }
            });

            builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    Toast.makeText(context, "Allow the Storage Permission to Generate PDF", Toast.LENGTH_LONG).show();
                }
            });

            builder.show();

        }
    }

    public void RequestPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", new Object[] {context.getApplicationContext().getPackageName()})));
                Payment payment = new Payment();
                (payment).startActivityIfNeeded(intent, 2000);

            }catch (Exception e){

                Intent obj = new Intent();
                obj.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                (new Payment()).startActivityIfNeeded(obj, 2000);

            }

        } else {

            ActivityCompat.requestPermissions((Activity) context, new String[] {WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, conn);

        }

    }

    public boolean checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            return Environment.isExternalStorageManager();

        } else{

            int write = ContextCompat.checkSelfPermission(context.getApplicationContext(), WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(context.getApplicationContext(), READ_EXTERNAL_STORAGE);

            return write == PackageManager.PERMISSION_GRANTED
                    && read == PackageManager.PERMISSION_GRANTED;


        }


    }

}
