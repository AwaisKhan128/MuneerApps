package com.example.muneerapps.Transactions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

    @NonNull
    @Override
    public Transaction_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Transaction_Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
