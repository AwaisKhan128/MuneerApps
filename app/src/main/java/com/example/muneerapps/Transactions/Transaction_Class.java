package com.example.muneerapps.Transactions;

import android.util.ArrayMap;

import com.example.muneerapps.R;

public class Transaction_Class {
    String Order_title,Customer,Category,Product,Rate,User,Quantity,Amount,Date_time;
    int amount_color;

    public Transaction_Class(String order,String Customer,String Categ
            ,String Prod,String Rate,String User,String Quantity, String amount,String date_time,int amount_color)
    {
        this.Order_title= order;
        this.Customer = Customer;
        this.Category = Categ;
        this.Product = Prod;
        this.Rate = Rate;
        this.User = User;
        this.Quantity = Quantity;
        this.Amount = amount;
        this.Date_time = date_time;
        this.amount_color = amount_color;
    }

    public String getOrder_title() {
        return Order_title;
    }

    public String getCustomer() {
        return Customer;
    }

    public String getCategory() {
        return Category;
    }

    public String getProduct() {
        return Product;
    }

    public String getRate() {
        return Rate;
    }

    public String getUser() {
        return User;
    }

    public String getQuantity() {
        return Quantity;
    }

    public String getAmount() {
        return Amount;
    }

    public String getDate_time() {
        return Date_time;
    }

    public int getAmount_color() {
        return amount_color;
    }
}
