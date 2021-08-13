package com.example.muneerapps.Transactional;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muneerapps.R;

public class Transactional_Holder extends RecyclerView.ViewHolder {

    public TextView key,Status,pays,Customer_key,customer_val,product_val,total_val,due_val,paid_val;
    public ImageView imageView2;
    public Transactional_Holder(@NonNull View itemView) {
        super(itemView);

        key = itemView.findViewById(R.id.textView30);
        Status = itemView.findViewById(R.id.textView31);
        pays = itemView.findViewById(R.id.textView32);
        customer_val = itemView.findViewById(R.id.Customer_val);
        product_val =itemView.findViewById(R.id.Product_val);
        total_val = itemView.findViewById(R.id.Total_val);
        due_val = itemView.findViewById(R.id.Due_val);
        paid_val = itemView.findViewById(R.id.Paid_val);
        imageView2 = itemView.findViewById(R.id.imageView2);
        Customer_key = itemView.findViewById(R.id.Customer_key);


    }
}
