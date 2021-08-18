package com.example.muneerapps.Transactional;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muneerapps.PDFManager.Common;
import com.example.muneerapps.Payment;
import com.example.muneerapps.PDFManager.PdfDocumentAdapter;
import com.example.muneerapps.R;
import com.example.muneerapps.Transaction_Encoder;
import com.example.muneerapps.dialogs.Progress_Monitor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.MODE_PRIVATE;

public class Transactional_Adaptor extends RecyclerView.Adapter<Transactional_Holder> {
    Context context;
    View view;
    ArrayList<Transactional_Class> data;
    Transactional_Holder transaction_holder;
    final static int conn = 3333;
    DatabaseReference mRef;
    FirebaseDatabase mDatabase;
    Document document;
    String Path = "";
    Progress_Monitor progress_monitor;
    ArrayList<String> Categories_list,Dates_list;



    public Transactional_Adaptor(Context context, ArrayList<Transactional_Class> data)
    {
        this.context = context;
        this.data = data;
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Payments");
        Path = Common.getAppPath(context)+"/MuneerApps.pdf";
        progress_monitor = new Progress_Monitor(context);
        Categories_list = new ArrayList<>();
        Dates_list = new ArrayList<>();
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

        holder.imageView2.setOnClickListener(view -> {
            Permission_Monitor(holder.key.getText().toString()
                    ,holder.Status.getText().toString(),holder.customer_val.getText().toString());
        });
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


    public void Permission_Monitor(String key,String Status, String customer_val)
    {
        if (checkPermission()) {
//            CreatePDFFile(context.getExternalFilesDir(null).getAbsolutePath() +"/test_pdf.pdf");

            progress_monitor.Setup_Progressing("Please Wait", "While System is generating PDF");
            if (!Retrieve_preference("Current_Date").isEmpty() && Retrieve_preference("Current_Date")!=null)
            {
                mRef.child("Transactions").child(Status).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        snapshot.getRef()
                                .child("Product").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dateSnapshot) {
                                final double[] Grand_Total = {0};
                                document = new Document();
                                ArrayList<String> Date_wise = new ArrayList<>();

                                if (new File(Path).exists())
                                    new File(Path).delete();
                                try {
                                    PdfWriter.getInstance(document, new FileOutputStream(Path));

                                } catch (DocumentException e) {
                                    e.printStackTrace();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }

                                for (DataSnapshot dataSnapshot2 : dateSnapshot.getChildren())
                                { // DateWise

                                    if (!Date_wise.contains(dataSnapshot2.getKey()))
                                    {
                                        Date_wise.add(dataSnapshot2.getKey());
                                    }
                                }

                                try {

                                    dateSnapshot.getRef()
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot3) {
                                                    ArrayList<String> arrayList = new ArrayList<>();
//                                                String datesKey = "12-Aug-2021";


                                                    String datesKey = Retrieve_preference("Current_Date");
//                                                    for (String datesKey : Date_wise)
//                                                    {

                                                    if (snapshot3.child(datesKey).exists()) {
                                                        Toaster("Generating PDF Please Wait");
                                                        document.open();
                                                        for (DataSnapshot mySnapshot : snapshot3.child(datesKey).getChildren()) {

                                                            Map<String, Object> TransProduct_Val = (Map<String, Object>) mySnapshot.getValue();
//                                                            mTV.append("\n Name : " + TransProduct_Val.get("Name"));

                                                            if (!(arrayList.contains(TransProduct_Val.get("Category").toString()))) {
                                                                arrayList.add((String) TransProduct_Val.get("Category").toString());
                                                            }

                                                        }

                                                        if (arrayList.size() > 0) {
//                                                    document.open();

                                                            for (String cat : arrayList) { //Category wise
                                                                document.newPage();
                                                                double Total = 0;
                                                                for (DataSnapshot mySnapshot : snapshot3.child(datesKey).getChildren()) {

                                                                    Map<String, Object> TransProduct_Val = (Map<String, Object>) mySnapshot.getValue();
//
                                                                    if (TransProduct_Val.get("Category").toString().toLowerCase().contains(cat.toLowerCase()))
                                                                    {
//                                                            mTV.append("\n Quantity : " + TransProduct_Val.get("Quantity"));
                                                                        if(!Categories_list.contains(TransProduct_Val.get("Category").toString())) {
                                                                            initialPrint(Status, datesKey, customer_val, cat);
                                                                            Categories_list.add(TransProduct_Val.get("Category").toString());
                                                                        }
                                                                        else
                                                                        {

                                                                        }

                                                                        CreatePDFFile(Path
                                                                                , Status, datesKey, customer_val,
                                                                                TransProduct_Val.get("Category").toString(),
                                                                                TransProduct_Val.get("Name").toString()
                                                                                , TransProduct_Val.get("Rate").toString()
                                                                                , TransProduct_Val.get("Quantity").toString()
                                                                                , TransProduct_Val.get("Amount").toString());
                                                                        Total = Total + Double.parseDouble(TransProduct_Val.get("Amount").toString());

                                                                    }

                                                                }
                                                                try {
                                                                    if (Total > 0) {
                                                                        addNewItemWithLeftAndRight(document,
                                                                                " Total ", "", "", "" + Total);
                                                                    }

                                                                } catch (DocumentException e) {
                                                                    e.printStackTrace();
                                                                }


                                                                Grand_Total[0] = Grand_Total[0] + Total;

//                                                        addLineSpace(document);
//                                                        addLineSeparator(document);
//                                                        addLineSeparator(document);
                                                                Categories_list = new ArrayList<>();
                                                            }

                                                        }

//                                                        document.newPage();

//                                                    }


                                                        try {

                                                                        addLineSpace(document);
                                                                        addLineSeparator(document);
                                                            addNewItem(document, "Grand Total", Element.ALIGN_CENTER);
                                                                        addLineSeparator(document);
                                                            addNewItemWithLeftAndRight(document,
                                                                    " Grand Total ", "", "", "" + Grand_Total[0]);
                                                            addNewItemWithLeftAndRight(document,
                                                                    " Total - Return ", ""
                                                                    , "", "" + new Transaction_Encoder().getDecoded(
                                                                            snapshot.child("Amount").getValue(String.class))
                                                            );
                                                            addNewItemWithLeftAndRight(document,
                                                                    " Credit/Debit ", "", ""
                                                                    , "" + new Transaction_Encoder().getDecoded(
                                                                            snapshot.child("Credit").getValue(String.class)));
                                                        } catch (DocumentException e) {
                                                            e.printStackTrace();
                                                        }
                                                        document.close();
                                                        Toaster("PDF Generated Success");
                                                    }
                                                    else {
                                                        Toaster("No Document for the Selected Date");
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });


                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }

//                                    document.open();
//                                document.newPage();
//                                    document.close();

//                                }

//                                document.close();


                                PrintPDF();

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                progress_monitor.Drop_Progressing();
                            }
                        });

//                    }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progress_monitor.Drop_Progressing();

                    }
                });
            }
            else
            {
                mRef.child("Transactions").child(Status).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        snapshot.getRef()
                                .child("Product").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dateSnapshot) {
                                final double[] Grand_Total = {0};
                                document = new Document();
                                ArrayList<String> Date_wise = new ArrayList<>();

                                if (new File(Path).exists())
                                    new File(Path).delete();
                                try {
                                    PdfWriter.getInstance(document, new FileOutputStream(Path));

                                } catch (DocumentException e) {
                                    e.printStackTrace();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }

                                for (DataSnapshot dataSnapshot2 : dateSnapshot.getChildren())
                                { // DateWise

                                    if (!Date_wise.contains(dataSnapshot2.getKey()))
                                    {
                                        Date_wise.add(dataSnapshot2.getKey());
                                    }
                                }

                                try {

                                    dateSnapshot.getRef()
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot3) {
                                                    ArrayList<String> arrayList = new ArrayList<>();
//                                                String datesKey = "12-Aug-2021";
                                                    document.open();
                                                    Toaster("Generating PDF Please Wait");

                                                    for (String datesKey : Date_wise)
                                                    {
                                                        document.newPage();

                                                        for (DataSnapshot mySnapshot : snapshot3.child(datesKey).getChildren())
                                                        {

                                                            Map<String, Object> TransProduct_Val = (Map<String, Object>) mySnapshot.getValue();
//                                                            mTV.append("\n Name : " + TransProduct_Val.get("Name"));

                                                            if (!(arrayList.contains(TransProduct_Val.get("Category").toString()))) {
                                                                arrayList.add((String) TransProduct_Val.get("Category").toString());
                                                            }

                                                        }

                                                        if (arrayList.size() > 0) {
//                                                    document.open();

                                                            for (String cat : arrayList)
                                                            { //Category wis

                                                                document.newPage();
                                                                double Total = 0;



                                                                for (DataSnapshot mySnapshot : snapshot3.child(datesKey).getChildren())
                                                                {

                                                                    Map<String, Object> TransProduct_Val = (Map<String, Object>) mySnapshot.getValue();
//
                                                                    if (TransProduct_Val.get("Category").toString().toLowerCase().contains(cat.toLowerCase())) {
//                                                            mTV.append("\n Quantity : " + TransProduct_Val.get("Quantity"));

                                                                        if(!Categories_list.contains(TransProduct_Val.get("Category").toString())) {
                                                                            initialPrint(Status, datesKey, customer_val, cat);
                                                                            Categories_list.add(TransProduct_Val.get("Category").toString());
                                                                        }
                                                                        else
                                                                        {

                                                                        }

                                                                        CreatePDFFile(Path
                                                                                , Status, datesKey, customer_val,
                                                                                TransProduct_Val.get("Category").toString(),
                                                                                TransProduct_Val.get("Name").toString()
                                                                                , TransProduct_Val.get("Rate").toString()
                                                                                , TransProduct_Val.get("Quantity").toString()
                                                                                , TransProduct_Val.get("Amount").toString());
                                                                        Total = Total + Double.parseDouble(TransProduct_Val.get("Amount").toString());

                                                                    }

                                                                }
                                                                try {
                                                                    if  (Total > 0) {
                                                                        addNewItemWithLeftAndRight(document,
                                                                                " Total ", "", "", "" + Total);
                                                                    }

                                                                } catch (DocumentException e) {
                                                                    e.printStackTrace();
                                                                }


                                                                Grand_Total[0] = Grand_Total[0] + Total;

//                                                        addLineSpace(document);
//                                                        addLineSeparator(document);
//                                                        addLineSeparator(document);
                                                                Categories_list = new ArrayList<>();
                                                            }

                                                        }



                                                    }


                                                    try {
                                                        addLineSpace(document);
                                                        addLineSeparator(document);
                                                        addNewItem(document, "Grand Total", Element.ALIGN_CENTER);
                                                        addLineSeparator(document);
                                                        addNewItemWithLeftAndRight(document,
                                                                " Grand Total ", "", "", "" + Grand_Total[0]);
                                                        addNewItemWithLeftAndRight(document,
                                                                " Total - Return ", ""
                                                                , "", "" + new Transaction_Encoder().getDecoded(
                                                                        snapshot.child("Amount").getValue(String.class))
                                                        );
                                                        addNewItemWithLeftAndRight(document,
                                                                " Credit/Debit ", "", ""
                                                                , "" + new Transaction_Encoder().getDecoded(
                                                                        snapshot.child("Credit").getValue(String.class)));
                                                    } catch (DocumentException e) {
                                                        e.printStackTrace();
                                                    }
                                                    document.close();
                                                    Toaster("PDF Generated Success");

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    progress_monitor.Drop_Progressing();
                                                }
                                            });


                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }

//                                    document.open();
//                                document.newPage();
//                                    document.close();

//                                }

//                                document.close();


                                PrintPDF();

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                progress_monitor.Drop_Progressing();
                            }
                        });

//                    }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progress_monitor.Drop_Progressing();

                    }
                });
            }






        } else{

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Go to App Info and Allow all Permissions").setTitle("Storage Permission Required");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
//                    RequestPermission();

                }
            });

            builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    Toast.makeText(context, "Allow the Storage Permission to Generate PDF", Toast.LENGTH_LONG).show();
                }
            });

            builder.show();

        }
    }

    public void RequestPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", new Object[] {context.getApplicationContext().getPackageName()})));
                Payment payment = new Payment();
                (payment).startActivityIfNeeded(intent, 2000);

            }catch (Exception e){

                Intent obj = new Intent();
                obj.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                (new Payment()).startActivityIfNeeded(obj, 2000);

            }

        } else {

            ActivityCompat.requestPermissions((Activity) context, new String[] {WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, conn);

        }

    }

    public boolean checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            return Environment.isExternalStorageManager();

        } else{

            int write = ContextCompat.checkSelfPermission(context.getApplicationContext(), WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(context.getApplicationContext(), READ_EXTERNAL_STORAGE);

            return write == PackageManager.PERMISSION_GRANTED
                    && read == PackageManager.PERMISSION_GRANTED;


        }


    }

    private void CreatePDFFile(String path,String Order_Title,String Date, String CustomerName,String CategoryName,String ItemName
            , String ItemRate,String ItemQuantity,String ItemAmount) {


        Log.e("Order_Title",Order_Title);
        Log.e("Date",Date);
        Log.e("CustomerName",CustomerName);
        Log.e("CategoryName",CategoryName);
        Log.e("ItemName",ItemName);
        Log.e("ItemRate",ItemRate);
        Log.e("ItemQuantity",ItemQuantity);
        Log.e("ItemQuantity",ItemQuantity);

//        if (Dates_list.contains(Date))
//        {
//            if(!Categories_list.contains(CategoryName))
//            {
//                Categories_list.add(CategoryName);
//            }
//
//        }
//        else
//        {
//            Categories_list = new ArrayList<>()
//            Dates_list.add(Date);
//        }



        try {



//            document.open();

            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Muneer APPS");
//            document.addCreator("MBR");










            addNewItemWithLeftAndRight(document,""+ItemName, ""+ItemRate ,""+ItemQuantity, ""+ItemAmount);

            // Total
//            addLineSpace(document);
//            addLineSeparator(document);
//            addLineSeparator(document);




//            document.newPage();
//            document.close();
////            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
//
//            PrintPDF();

        }


        catch (DocumentException e) {
            e.printStackTrace();
        }
    }


//    public void Check_Permissions()
//    {
//        if (ContextCompat.checkSelfPermission(getApplicationContext(),
//                Manifest.permission.READ_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(),
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED)
//        {
//
//        }
//        else {
//            requestStoragePermission();
//        }
//    }
//
//    private void requestStoragePermission() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE))
//        {
//            new AlertDialog.Builder(this)
//                    .setTitle("Permission needed")
//                    .setMessage("This Muneer Apps required your Storage Permission")
//                    .setPositiveButton("ok", (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this,
//                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE
//                                    ,Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            1))
//                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    })
//                    .create().show();
//        }
//        else {
//            ActivityCompat.requestPermissions(this,
//                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE
//                            ,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
//        {
//            Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
//
//        }
//    }
//
//







//            // settings
//            document.setPageSize(PageSize.A4);
//            document.addCreationDate();
//            document.addAuthor("MBR Bilal");

//            addNewItem(document, "Muneer Apps MirpurKhas", Element.ALIGN_CENTER);
//            addNewItem(document, "Invoice (Generated)", Element.ALIGN_CENTER);
//            addLineSeparator(document);
//            addLineSeparator(document);

    private void initialPrint(String Order_Title,String Date,String CustomerName,String CategoryName)
    {
        try {
            addNewItem(document, "Muneer Sweets and Bakers Mirpurkhas", Element.ALIGN_CENTER);
            addNewItem(document, "Invoice (Generated)", Element.ALIGN_CENTER);
            addLineSeparator(document);
            addLineSeparator(document);

            addLineSpace(document);
            addNewItem(document, "\n", Element.ALIGN_CENTER);

            addNewItem(document, "Order Details", Element.ALIGN_CENTER);
            addNewItem(document, "Order Title : "+Order_Title, Element.ALIGN_LEFT); // purchase or sell
            addNewItem(document, "Order Date and Time : "+Date , Element.ALIGN_LEFT);

            addLineSeparator(document);
            addNewItem(document, "Customer Details", Element.ALIGN_CENTER);

            addLineSpace(document);
            addNewItem(document, "Customer Name : "+CustomerName, Element.ALIGN_LEFT);
//            addNewItem(document, "Customer Email : Abcd@gmail.com " , Element.ALIGN_LEFT);

            addLineSpace(document);
            addNewItem(document, "Category : "+CategoryName , Element.ALIGN_LEFT);

            addLineSeparator(document);
            addNewItemWithLeftAndRight(document, "Item Name", "Rate" , "Quantity", "Amount");

            addLineSeparator(document);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void PrintPDF() {


        progress_monitor.Drop_Progressing();
        PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);

//        try
//        {
//
//            if (new File(Path).exists()) {
//                PrintDocumentAdapter printDocumentAdapter =
//                        new PdfDocumentAdapter(context, Path);
//                printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());
//            }
//
//        } catch (Exception ex)
//        {
//            Toast.makeText(context, "error " + ex.getMessage(), Toast.LENGTH_SHORT).show();
//        }

    }



    private void addNewItemWithLeftAndRight(Document document, String ItemName, String ItemRate , String ItemQuantity , String ItemAmount)
            throws DocumentException {


        PdfPTable table = new PdfPTable(4);

        PdfPCell item = new PdfPCell(new Phrase(ItemName));
        PdfPCell rate = new PdfPCell(new Phrase(ItemRate));
        PdfPCell quantity = new PdfPCell(new Phrase(ItemQuantity));
        PdfPCell amount = new PdfPCell(new Phrase(ItemAmount));

        table.addCell(item);
        table.addCell(rate);
        table.addCell(quantity);
        table.addCell(amount);

        document.add(table);

    }


    private void addLineSeparator(Document document) throws DocumentException {

        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);

    }


    private void addLineSpace(Document document) throws DocumentException {

        document.add(new Paragraph(""));
    }



    private void addNewItem(Document document, String text, int align) throws DocumentException {

        Chunk chunk = new Chunk(text);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);

    }


    private String Retrieve_preference(String key)
    {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        return pref.getString(key, "");         // getting you_bool
    }

}
