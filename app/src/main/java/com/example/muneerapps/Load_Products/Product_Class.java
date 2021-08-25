package com.example.muneerapps.Load_Products;

public class Product_Class {

    String Category,Name,unit,Price;

    public Product_Class(String Category, String Name, String unit, String Price)
    {
        this.Category = Category;
        this.Name = Name;
        this.unit = unit;
        this.Price = Price;
    }

    public String getCategory() {
        return Category;
    }

    public String getName() {
        return Name;
    }

    public String getUnit() {
        return unit;
    }

    public String getPrice() {
        return Price;
    }
}
