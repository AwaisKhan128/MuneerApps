package com.example.muneerapps.Load_Products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muneerapps.PDFManager.Common;
import com.example.muneerapps.R;
import com.example.muneerapps.Transactional.Transactional_Class;
import com.example.muneerapps.Transactional.Transactional_Holder;
import com.example.muneerapps.dialogs.Progress_Monitor;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Product_Adaptor extends RecyclerView.Adapter<Product_Holder>{
    Context context;
    View view;
    ArrayList<Product_Class> data;
    Product_Holder transaction_holder;

    public Product_Adaptor(Context context, ArrayList<Product_Class> data)
    {
        this.context = context;
        this.data = data;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public Product_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.product_load, parent,false);
        transaction_holder = new Product_Holder(view);
        return transaction_holder;

    }

    @Override
    public void onBindViewHolder(@NonNull Product_Holder holder, int i) {
        holder.Category.setText(data.get(i).getCategory());
        holder.Name.setText(data.get(i).getName());
        holder.Unit.setText(data.get(i).getUnit());
        holder.Price.setText(data.get(i).getPrice());
        holder.setIsRecyclable(false);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
