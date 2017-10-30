package com.bhojnalya.vikas.app;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.bhojnalya.vikas.app.adapter.ProductItemAdapter;
import com.bhojnalya.vikas.app.model.Catagory;
import com.bhojnalya.vikas.app.model.Product;
import com.bhojnalya.vikas.app.util.SharedPrefHelper;
import com.bhojnalya.vikas.app.util.Utility;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<Catagory> catagories;
    ProductItemAdapter adapter;
    ListView list;
    ArrayList<Product> products;
    Button btnAddProduct;
    FloatingActionButton fab;
    final Activity activity=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        SharedPrefHelper.initializePreferences(this);
        btnAddProduct=(Button)findViewById(R.id.btnAddProduct);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        btnAddProduct.setOnClickListener(this);
        products=new ArrayList<>();
        Bundle b=this.getIntent().getExtras();
        if(b!=null){
            if(BuildConfig.FLAVOR=="admin") {
                fab.setVisibility(View.GONE);
                catagories = (ArrayList<Catagory>) b.getSerializable("catagories");
                for (Catagory catagory : catagories) {
                    if (catagory.products != null) {
                        for (Product product : catagory.products) {
                            products.add(product);
                        }
                    }
                }
            }
            else{
                products= (ArrayList<Product>) b.getSerializable("products");
                btnAddProduct.setVisibility(View.GONE);

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*/
                        makecall();

                    }
                });
            }
        }

        list=(ListView)findViewById(R.id.productListView);
        adapter=new ProductItemAdapter(this,products);
        list.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v==btnAddProduct){
            Intent intent=new Intent(this,ManageProduct.class);
            Bundle b=new Bundle();
            b.putSerializable("catagories",catagories);
            intent.putExtras(b);
            startActivity(intent);
        }
    }

    public void makecall(){
        if(Utility.isCallingAllowed(this)){
            executeCall();
        }else{
            requestCallingPermission();
        }

    }

    private void executeCall() {

        // start your call here
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String contact = SharedPrefHelper.getObjectAsString(SharedPrefHelper.CONTACT_KEY);
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse(contact));
                    activity.startActivity(callIntent);
                }catch (SecurityException se){

                }
            }
        });
    }

    //Requesting permission
    private void requestCallingPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CALL_PHONE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},Utility.CALL_PHONE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Utility.CALL_PHONE_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    executeCall();


                } else {
                    Toast.makeText(activity, "Application can not make calls without permission", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}
