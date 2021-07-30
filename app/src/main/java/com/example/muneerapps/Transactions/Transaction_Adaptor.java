package com.example.muneerapps.Transactions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muneerapps.R;

import java.util.ArrayList;

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


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
