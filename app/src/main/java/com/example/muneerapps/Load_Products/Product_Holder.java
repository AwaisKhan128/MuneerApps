package com.example.muneerapps.Load_Products;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muneerapps.R;

public class Product_Holder extends RecyclerView.ViewHolder {
    TextView Category, Name, Unit,Price;

    public Product_Holder(@NonNull View itemView) {
        super(itemView);
        Category = itemView.findViewById(R.id.Category);
        Name = itemView.findViewById(R.id.Name);
        Unit = itemView.findViewById(R.id.Unit);
        Price = itemView.findViewById(R.id.Price);
    }
}
