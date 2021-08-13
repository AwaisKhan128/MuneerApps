package com.example.muneerapps.Transactional;

import android.widget.ImageView;

public class Transactional_Class {
   public String key, Status, pays, customer_val, product_val, total_val, due_val, paid_val,Customer_key;


   public Transactional_Class(String key, String Status, String pays,String Customer_key, String customer_val,
                              String product_val, String total_val, String due_val, String paid_val)
   {
      this.key = key;
      this.Status = Status;
      this.pays = pays;
      this.customer_val = customer_val;
      this.product_val = product_val;
      this.total_val = total_val;
      this.due_val = due_val;
      this.paid_val = paid_val;
      this.Customer_key = Customer_key;
   }

   public String getKey() {
      return key;
   }

   public String getStatus() {
      return Status;
   }

   public String getPays() {
      return pays;
   }

   public String getCustomer_val() {
      return customer_val;
   }

   public String getProduct_val() {
      return product_val;
   }

   public String getTotal_val() {
      return total_val;
   }

   public String getDue_val() {
      return due_val;
   }

   public String getPaid_val() {
      return paid_val;
   }

   public String getCustomer_key() {
      return Customer_key;
   }
}
