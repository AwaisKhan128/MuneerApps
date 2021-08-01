package com.example.muneerapps.Transactions;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muneerapps.R;


public class Transaction_Holder extends RecyclerView.ViewHolder {
    public TextView Order_title,Customer,Categories,Products,Rates,User,Quantity,Amount,Date_time;
    public ImageView imageView7;

    public Transaction_Holder(@NonNull View itemView) {
        super(itemView);
        Order_title = itemView.findViewById(R.id.textView12);
        Customer = itemView.findViewById(R.id.textView18);
        Categories = itemView.findViewById(R.id.Category_data);
        Products = itemView.findViewById(R.id.textView16);
        Rates = itemView.findViewById(R.id.rate_data);
        User = itemView.findViewById(R.id.user_data);
        Quantity = itemView.findViewById(R.id.quantity_data);
        Amount = itemView.findViewById(R.id.paycount);
        Date_time = itemView.findViewById(R.id.textView24);
        imageView7 = itemView.findViewById(R.id.imageView7);

    }
}
