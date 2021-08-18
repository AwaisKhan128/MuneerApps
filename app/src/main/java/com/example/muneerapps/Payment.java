package com.example.muneerapps;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muneerapps.PDFManager.Common;
import com.example.muneerapps.Transactional.Transactional_Adaptor;
import com.example.muneerapps.Transactional.Transactional_Class;
import com.example.muneerapps.Transactions.Transaction_Adaptor;
import com.example.muneerapps.Transactions.Transaction_Class;
import com.example.muneerapps.dialogs.Addcash_dialog;
import com.example.muneerapps.dialogs.Category_dialog;
import com.example.muneerapps.dialogs.Date_Pick;
import com.example.muneerapps.dialogs.ReturnPurchase_dialog;
import com.example.muneerapps.dialogs.ReturnSell_dialog;
import com.example.muneerapps.dialogs.SignOut_Toast;
import com.example.muneerapps.dialogs.Subcash_dialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import com.ozcanalasalvar.library.view.datePicker.DatePicker;
import com.ozcanalasalvar.library.view.popup.DatePickerPopup;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import github.com.st235.lib_expandablebottombar.ExpandableBottomBar;

public class Payment extends AppCompatActivity {
    ArrayList<Transactional_Class> Data_log = new ArrayList<>();
    ArrayList<Transactional_Class> Data_log_copy = new ArrayList<>();

    public RecyclerView recycler;
    Context context;
    LinearLayoutManager linearLayout;
    String current_status = "Sell";
    BottomBar bottomBar;
    FloatingActionButton floatingActionButton2;
    String Path ="";
    DatePicker datePicker;

    public void Bottom_bars()
    {
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId)
                {
                    case R.id.sell:
                        cashin();
//                        new Manage_Insertion().execute("Sell");
                        break;

                    case R.id.purchase:
                        cashout();
                        break;

                    case R.id.search_bar:
                        Date_pick();
                        break;

                    case R.id.return_sell:
                        return_sell();
//                        new Manage_Insertion().execute("Return_Sell");
                        break;

                    case R.id.return_purchase:
                        return_purchase();
//                        new Manage_Insertion().execute("Return_Purchase");
                        break;

                    default:
                        break;
                }
            }
        },false);


        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(int tabId) {
                switch (tabId)
                {
                    case R.id.sell:
                        cashin();
//                        new Manage_Insertion().execute("Sell");
                        break;

                    case R.id.purchase:
                        cashout();
                        break;

                    case R.id.search_bar:
                        Date_pick();
                        break;

                    case R.id.return_sell:
                        return_sell();
//                        new Manage_Insertion().execute("Return_Sell");
                        break;

                    case R.id.return_purchase:
                        return_purchase();
//                        new Manage_Insertion().execute("Return_Purchase");
                        break;

                    default:
                        break;
                }

            }
        });
    }

    public class Graph_Provider extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
            graph.removeAllSeries();}
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            FirebaseDatabase.getInstance().getReference("Payments")
                    .child("History").child(current_status)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChildren())
                            {

                                DataPoint[] dataPoints = new DataPoint[(int) snapshot.getChildrenCount()];
//
//                                dataPoints[0] = new DataPoint(0, 4);
//                                dataPoints[1] = new DataPoint(1, 7);
                                int count = 0;

                                for (DataSnapshot df : snapshot.getChildren())
                                {
//                                    arrayList.add(df.getValue(String.class));
                                    dataPoints[count] = new  DataPoint
                                            (count ,(int) (float) Double.parseDouble( (
                                                    new Transaction_Encoder().getDecoded(df.getValue(String.class)))));
                                    count++;
                                }


                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
                                graph.addSeries(series);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


            
            
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_payment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.refresh:
                Refresh_Now();
                return true;
            case R.id.signout:
                SignOut_Now();
                return true;

            case R.id.sell:
                current_status = "Sell";
                Sell();
                return true;

            case R.id.purchase:
                current_status = "Purchase";
                Purchase();
                return true;
                
            case R.id.return_sell:
                current_status = "Return_Sell";
                Return_Sell();
                return true;
                
            case R.id.return_purchase:
                current_status = "Return_Purchase";
                Return_Purchase();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void Return_Purchase() {

        new Graph_Provider().execute();
        if (current_status.contains("Sell")) {
            searcher.setHint("Customer");
        }
        else
        {
            searcher.setHint("Supplier");
        }
        FirebaseDatabase.getInstance().getReference("Payments")
                .child("Transactions").child(current_status).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren())
                {

                    new Manage_Users().execute();
                }
                else {
                    try{
                        Data_log.clear();
                        synchronized (Data_log)
                        {
                            Data_log.notify();
                        }
                        recycler.removeAllViewsInLayout();
                        recycler.removeAllViews();
                        recycler.getAdapter().notifyDataSetChanged();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void Return_Sell() {
        if (current_status.contains("Sell")) {
            searcher.setHint("Customer");
        }
        else
        {
            searcher.setHint("Supplier");
        }

        new Graph_Provider().execute();
        FirebaseDatabase.getInstance().getReference("Payments")
                .child("Transactions").child(current_status).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren())
                {

                    new Manage_Users().execute();
                }
                else {
                    try{
                        Data_log.clear();
                        synchronized (Data_log)
                        {
                            Data_log.notify();
                        }
                        recycler.removeAllViewsInLayout();
                        recycler.removeAllViews();
                        recycler.getAdapter().notifyDataSetChanged();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void Purchase() {
        if (current_status.contains("Sell")) {
            searcher.setHint("Customer");
        }
        else
        {
            searcher.setHint("Supplier");
        }

        new Graph_Provider().execute();
        FirebaseDatabase.getInstance().getReference("Payments")
                .child("Transactions").child(current_status).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren())
                {

                    new Manage_Users().execute();
                }
                else {
                    try{
                        Data_log.clear();
                        recycler.removeAllViewsInLayout();
                        recycler.removeAllViews();
                        synchronized (Data_log)
                        {
                            Data_log.notify();
                        }
                        recycler.getAdapter().notifyDataSetChanged();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void Sell() {
        if (current_status.contains("Sell")) {
            searcher.setHint("Customer");
        }
        else
        {
            searcher.setHint("Supplier");
        }

        new Graph_Provider().execute();
        FirebaseDatabase.getInstance().getReference("Payments")
                .child("Transactions").child(current_status).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren())
                {

                    new Manage_Users().execute();
                }
                else {
                    try{
                        Data_log.clear();
                        synchronized (Data_log)
                        {
                            Data_log.notify();
                        }
                        recycler.removeAllViewsInLayout();
                        recycler.removeAllViews();
                        recycler.getAdapter().notifyDataSetChanged();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void Refresh_Now()
    {
        new Graph_Provider().execute();
        FirebaseDatabase.getInstance().getReference("Payments")
                .child("Transactions").child(current_status).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren())
                {

                    new Manage_Users().execute();
                }
                else {
                    try{
                        Data_log.clear();
                        recycler.removeAllViewsInLayout();
                        synchronized (Data_log)
                        {
                            Data_log.notify();
                        }
                        recycler.removeAllViews();
                        recycler.getAdapter().notifyDataSetChanged();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void SignOut_Now()
    {
        SignOut_Toast signOut_toast = new SignOut_Toast(this);
        signOut_toast.show();
    }

    public void Toaster(String s)
    {
        Toast.makeText(context.getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    DatePickerPopup datePickerPopup;
    public void Date_pick()
    {
       if ( linearLayoutS.getVisibility()==View.GONE)
       {
           linearLayoutS.setVisibility(View.VISIBLE);
           if (current_status.contains("Sell")) {
               searcher.setHint("Customer");
           }
           else
           {
               searcher.setHint("Supplier");
           }
       }
       else {
           linearLayoutS.setVisibility(View.GONE);
       }

    }

    GraphView graph;
    LinearLayout linearLayoutS;
    EditText searcher;
    ImageView imageView3;
    ListView listView;
    TextView reset_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_sheet);
        recycler = findViewById(R.id.recycler);
        context = this;
        Path = Common.getAppPath(context)+"/MuneerApps.pdf";
        linearLayout = new LinearLayoutManager(context);
        recycler.setLayoutManager(linearLayout);
        recycler.addItemDecoration(new RecyclerMainSpacing(16));
//        recycler.setVisibility(View.GONE);
        graph = (GraphView) findViewById(R.id.graph);
        linearLayoutS = findViewById(R.id.linearLayoutS);
        linearLayoutS.setVisibility(View.GONE);
        searcher = findViewById(R.id.searcher);
        listView = findViewById(R.id.listView);
        reset_date = findViewById(R.id.reset_date);
        floatingActionButton2 = findViewById(R.id.floatingActionButton2);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new File(Path).exists())
                {
                    Uri uri = Uri.fromFile(new File(Path));
                    try {
                        Intent share = new Intent(Intent.ACTION_SEND);

                        // If you want to share a png image only, you can do:
                        // setType("image/png"); OR for jpeg: setType("image/jpeg");
                        share.setType("pdf/*");

                        // Make sure you put example png image named myImage.png in your
                        // directory
//                    String imagePath = Environment.getExternalStorageDirectory()
//                            + "/myImage.png";
//
//                    File imageFileToShare = new File(imagePath);
//
//                    Uri uri = Uri.fromFile(imageFileToShare);
//                        Toaster(String.valueOf(myUri));
                        share.putExtra(Intent.EXTRA_STREAM, uri);

                        context.startActivity(Intent.createChooser(share, "Share PDF!"));

// Try to invoke the intent.

//                    c.startActivity(chooser);
                    } catch (Exception e) {
                        // Define what your app should do if no activity can handle the intent.
                        Log.e("Share error",e.getLocalizedMessage());
                    }
                }
            }
        });

        reset_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Add3Preference("Current_Date","");
                Toaster("Date Resetted");
            }
        });




        searcher.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (searcher.getText().toString().length()>0)
                {

                    new SearchByUser().execute(searcher.getHint().toString(),charSequence.toString());

                }
                else
                {
                    try {
                        Refresh_Now();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        imageView3 = findViewById(R.id.imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date_Pick date_pick = new Date_Pick(Payment.this);
                date_pick.setCanceledOnTouchOutside(false);
                date_pick.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        Toaster(Retrieve_preference("Current_Date"));
                    }
                });
                date_pick.show();
            }
        });
        Bottom_bars();



//        int[] array = new int[]{};


        new Graph_Provider().execute();


//
//
//        FirebaseDatabase.getInstance().getReference("Payments")
//                .child("Transactions").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.hasChildren())
//                {
//
//                    new Manage_Users().execute();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });



    }





    public class Manage_Insertion extends AsyncTask<String,Void,Void>
    {

        @Override
        protected Void doInBackground(String... strings) {
            if (strings[0].contains("Sell"))
            {
                cashin();
            }
            if (strings[0].contains("Purchase"))
            {
                cashout();
            }
            if(strings[0].contains("Return_Sell"))
            {

            }
            if(strings[0].contains("Return_Purchase"))
            {

            }
            return null;
        }
    }

    public void cashout()
    {
        Subcash_dialog cdd=new Subcash_dialog(Payment.this);
        cdd.show();
    }

    public void cashin()
    {
        Addcash_dialog cdd=new Addcash_dialog(Payment.this);
        cdd.show();

    }
    public void return_sell()
    {
        FirebaseDatabase.getInstance()
                .getReference("Payments")
                .child("Transactions").child("Sell").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    ReturnSell_dialog cdd=new ReturnSell_dialog(Payment.this);
                    cdd.show();
                }
                else
                {
                    Toast.makeText(context,"You don't have permitted to return sells now",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void return_purchase() {
        FirebaseDatabase.getInstance()
                .getReference("Payments")
                .child("Transactions").child("Purchase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    ReturnPurchase_dialog cdd=new ReturnPurchase_dialog(Payment.this);
                    cdd.show();
                }
                else
                {
                    Toast.makeText(context,"You don't have permitted to return purchase now",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    public class Manage_Users extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
            Data_log.clear();
            recycler.removeAllViewsInLayout();
            recycler.removeAllViews();
            recycler.getAdapter().notifyDataSetChanged();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            FirebaseDatabase.getInstance()
                    .getReference("Payments")
                    .child("Transactions").child(current_status)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren())
                    {
                        Transaction_Encoder transaction_encoder = new Transaction_Encoder();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        {

                            String key = dataSnapshot.getKey();
                            String Status = current_status;
                            String pays = "None";
                            String Customer_key = "None";
                            String Customer_Val = "None";
                            String Total = "0";
                            String Paid = "0";
                            String Due = "0";
                            long product_count_top = 0;

//                            ---------------To Inquire Paid or Unpaid---------------

                            double credit = Double.parseDouble(transaction_encoder.getDecoded(dataSnapshot.child("Credit").getValue(String.class)));

                            double amount = Double.parseDouble(transaction_encoder.getDecoded(dataSnapshot.child("Amount").getValue(String.class)));

                            Total = String.valueOf(amount);
                            Paid = String.valueOf(credit);
                            Due = String.valueOf(amount-credit);

                            if(credit !=0) {
                                if (amount - credit == 0) {
                                     pays = "Paid";
                                }
                                if (amount - credit > 0) {
                                     pays = "Partial Paid";
                                }
                            }
                            else
                            {
                                 pays = "UnPaid";
                            }
//                            -------------------------------------------------------
                            if (dataSnapshot.hasChild("Customer"))
                            {
                                Customer_key = "Customer";
                                Customer_Val = dataSnapshot.child("Customer").getValue(String.class);

                            }
                            else
                            {
                                Customer_key = "Supplier";
                                Customer_Val = dataSnapshot.child("Supplier").getValue(String.class);
                            }

                            product_count_top = dataSnapshot.child("Product").getChildrenCount();
//

                            String finalPays = pays;
                            String finalCustomer_key = Customer_key;
                            String finalCustomer_Val = Customer_Val;
                            String finalTotal = Total;
                            String finalDue = Due;
                            String finalPaid = Paid;
                            final long[] product_count_bottom1 = {0};




                            long final_count = product_count_top;
                            String product = String.valueOf(final_count);
                            Data_log.add(new Transactional_Class(key,Status, finalPays
                                    , finalCustomer_key, finalCustomer_Val,product, finalTotal, finalDue, finalPaid));
//                            --------------------------





                        }
                        try {
                            Transactional_Adaptor viewAdaptor = new Transactional_Adaptor(context, Data_log);
                            viewAdaptor.notifyDataSetChanged();
                            recycler.setVisibility(View.VISIBLE);
                            recycler.setAdapter(viewAdaptor);
                            recycler.getAdapter().notifyDataSetChanged();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            FirebaseDatabase.getInstance()
                    .getReference("Payments").child("Balance").child(current_status).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        String balance = new Transaction_Encoder().getDecoded(snapshot.getValue(String.class));
                        getSupportActionBar().setTitle("Balance Rs. "+balance);
                    }
                    else {
                        getSupportActionBar().setTitle("Balance Rs. "+0);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return null;
        }
    }

    public class SearchByUser extends AsyncTask<String,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
                Data_log.clear();
                recycler.removeAllViewsInLayout();
                synchronized (Data_log)
                {
                    Data_log.notify();
                }
                recycler.removeAllViews();
                recycler.getAdapter().notifyDataSetChanged();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            FirebaseDatabase.getInstance().getReference("Payments")
                    .child("Transactions").child(current_status)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChildren())
                            {
                                ArrayList<String> Users = new ArrayList<String>();
                                DatabaseReference mRef = null;
                                Transaction_Encoder transaction_encoder = new Transaction_Encoder();
                                String key = "0";double credit = 0;double amount=0;
                                String Status = current_status;
                                String pays = "None";
                                String Customer_key = "None";
                                String Customer_Val = "None";
                                String Total = "0";
                                String Paid = "0";
                                String Due = "0";
                                long product_count_top = 0;

                                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                                {
                                    if (dataSnapshot.child(strings[0])
                                            .getValue(String.class).toLowerCase()
                                            .contains(strings[1].toLowerCase()))
                                    {
                                        mRef = dataSnapshot.getRef();
                                        key = dataSnapshot.getKey();
                                        credit = Double.parseDouble(transaction_encoder.getDecoded(dataSnapshot.child("Credit").getValue(String.class)));

                                         amount = Double.parseDouble(transaction_encoder.getDecoded(dataSnapshot.child("Amount").getValue(String.class)));
                                        product_count_top = dataSnapshot.child("Product").getChildrenCount();
                                        if (dataSnapshot.hasChild("Customer"))
                                        {
                                            Customer_key = "Customer";
                                            Customer_Val = dataSnapshot.child("Customer").getValue(String.class);

                                        }
                                        else
                                        {
                                            Customer_key = "Supplier";
                                            Customer_Val = dataSnapshot.child("Supplier").getValue(String.class);
                                        }
                                    }
                                }

                                if (mRef!=null)
                                {


//                            ---------------To Inquire Paid or Unpaid---------------



                                    Total = String.valueOf(amount);
                                    Paid = String.valueOf(credit);
                                    Due = String.valueOf(amount-credit);

                                    if(credit !=0) {
                                        if (amount - credit == 0) {
                                            pays = "Paid";
                                        }
                                        if (amount - credit > 0) {
                                            pays = "Partial Paid";
                                        }
                                    }
                                    else
                                    {
                                        pays = "UnPaid";
                                    }
//                            -------------------------------------------------------
                                    String finalPays = pays;
                                    String finalCustomer_key = Customer_key;
                                    String finalCustomer_Val = Customer_Val;
                                    String finalTotal = Total;
                                    String finalDue = Due;
                                    String finalPaid = Paid;
                                    final long[] product_count_bottom1 = {0};




                                    long final_count = product_count_top;
                                    String product = String.valueOf(final_count);

                                    {
                                        Data_log.add(new Transactional_Class(key, Status, finalPays
                                                , finalCustomer_key, finalCustomer_Val, product, finalTotal, finalDue, finalPaid));

                                    }

//                            --------------------------
                                    try {
                                        Transactional_Adaptor viewAdaptor = new Transactional_Adaptor(context, Data_log);
                                        viewAdaptor.notifyDataSetChanged();
                                        recycler.setVisibility(View.VISIBLE);
                                        recycler.setAdapter(viewAdaptor);


                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();

                                    }
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            return null;
        }
    }




    private void Add2Preference(String key, long quantity)
    {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, quantity);  // Saving string
        // Save the changes in SharedPreferences
        editor.apply(); // commit changes
    }
    private void Add3Preference(String key, String quantity)
    {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, quantity);  // Saving string
        editor.apply(); // commit changes
    }
    private String Retrieve_preference(String key)
    {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        return pref.getString(key, "");         // getting you_bool
    }
//


}