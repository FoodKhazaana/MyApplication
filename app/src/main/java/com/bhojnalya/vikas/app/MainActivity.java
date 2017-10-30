package com.bhojnalya.vikas.app;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bhojnalya.vikas.app.adapter.CatagoryAdapter;
import com.bhojnalya.vikas.app.model.Catagory;
import com.bhojnalya.vikas.app.model.ContactPerson;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private StorageReference storageRef;
    FirebaseDatabase database;
    DatabaseReference catagory;
    DatabaseReference contactPerson;
    public static final String TAG="MainActivity";
    TextView tvMsg;
    Button add,addProduct,btnManageContact;
    public static int RESULT_LOAD_IMAGE=1;
    ArrayList<Catagory> catlist=new ArrayList<Catagory>();
    ListView list;
    CatagoryAdapter adapter;
    Resources res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            // Create global configuration and initialize ImageLoader with this config
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
            ImageLoader.getInstance().init(config);

            list = (ListView) findViewById(R.id.catagoryListView);  // List defined in XML ( See Below )
            res = getResources();

            /**************** Create Custom Adapter *********/

            // Write a message to the database
            database = FirebaseDatabase.getInstance();
            storageRef = FirebaseStorage.getInstance().getReference();
            add = (Button) findViewById(R.id.btnAdd);
            addProduct = (Button) findViewById(R.id.btnAddProd);
            btnManageContact=(Button) findViewById(R.id.btnManageContact);


            add.setOnClickListener(this);
            addProduct.setOnClickListener(this);
            btnManageContact.setOnClickListener(this);
            catagory = database.getReference("catagory");
            contactPerson = database.getReference("contactPerson");

            contactPerson.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                  ContactPerson  cp = dataSnapshot.getValue(ContactPerson.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            catagory.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    catlist.clear();
                    Iterable<DataSnapshot> idata = dataSnapshot.getChildren();
                    for (DataSnapshot ds : idata) {
                        Catagory value = ds.getValue(Catagory.class);
                        catlist.add(value);
                    }
                    adapter = new CatagoryAdapter(MainActivity.this, catlist, res);
                    list.setAdapter(adapter);
                    //Catagory value = dataSnapshot.getValue(Catagory.class);
                    //tvMsg.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                    Log.d(TAG, "Value is: " + dataSnapshot.getChildrenCount());
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }catch(Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImage(Uri file){

        //Uri file = Uri.fromFile(path);
        final StorageReference riversRef = storageRef.child("images/holi.png");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        @SuppressWarnings("VisibleForTests")Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.w(TAG, downloadUrl.toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    public void onItemClick(int mPosition)
    {
       /* ListModel tempValues = ( ListModel ) CustomListViewValuesArr.get(mPosition);


        // SHOW ALERT

        Toast.makeText(CustomListView,
                ""+tempValues.getCompanyName()
                        +"
                Image:"+tempValues.getImage()
            +"
        Url:"+tempValues.getUrl(),
        Toast.LENGTH_LONG)
                    .show();*/
    }

    @Override
    public void onClick(View v) {
        if(v==add){
            Intent intent=new Intent(this,ManageCatagory.class);
            startActivity(intent);
        }
        else if(v==addProduct){
            //Intent intent=new Intent(this,ManageProduct.class);
            Intent intent=new Intent(this,ProductListActivity.class);
            Bundle b=new Bundle();
            b.putSerializable("catagories",catlist);
            intent.putExtras(b);
            startActivity(intent);
        }
        else if(v==btnManageContact){
            Intent intent=new Intent(this,ManageContactActivity.class);

            startActivity(intent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            uploadImage(selectedImage);
        }


    }
}
