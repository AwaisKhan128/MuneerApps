package com.example.muneerapps.Transactional;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.print.PrintAttributes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muneerapps.R;
import com.example.muneerapps.Transactions.Transaction_Class;
import com.example.muneerapps.Transactions.Transaction_Holder;
import com.example.muneerapps.dialogs.Show_PrintPreview;
import com.google.firebase.database.annotations.NotNull;
import com.uttampanchasara.pdfgenerator.CreatePdf;

import java.io.File;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Transactional_Adaptor extends RecyclerView.Adapter<Transactional_Holder> {
    Context context;
    View view;
    ArrayList<Transactional_Class> data;
    Transactional_Holder transaction_holder;


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


}
