package com.bhojnalya.vikas.app;


import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bhojnalya.vikas.app.adapter.CatagoryAttribAdapter;
import com.bhojnalya.vikas.app.dialogFragment.ProductWeightFragment;
import com.bhojnalya.vikas.app.model.Catagory;
import com.bhojnalya.vikas.app.model.ProductAttribute;
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

import java.util.ArrayList;

public class ManageCatagory extends AppCompatActivity implements View.OnClickListener,ProductWeightFragment.WeightAddListener {

    public EditText edtTxtCatagoryName;
    ImageView imgCatagory;
    Button btnUpload,btnSave,btnAddWeight;
    CheckBox checkBoxActiveCatagory;

    private StorageReference storageRef;
    FirebaseDatabase database;
    DatabaseReference catagoryReference;

    Catagory catagory=new Catagory();
    public static int RESULT_LOAD_IMAGE=1;
    public static final String TAG="ManageCatagory";
    public String imagePath="images/";
    long maxID;
    ListView productAttribLstView;
    CatagoryAttribAdapter adapter;
    Resources res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_catagory);
        initialization();
        database = FirebaseDatabase.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        catagoryReference = database.getReference("catagory");
        catagory.weightAttributes=new ArrayList<>();
        productAttribLstView=(ListView)findViewById(R.id.weightAttriblistView);
        res=getResources();
        adapter=new CatagoryAttribAdapter(this, catagory.weightAttributes,res);
        productAttribLstView.setAdapter( adapter );
        catagoryReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Iterable<DataSnapshot> idata=dataSnapshot.getChildren();
                for(DataSnapshot ds :idata){
                    Catagory value= ds.getValue(Catagory.class);
                }

                maxID=dataSnapshot.getChildrenCount();
                Toast.makeText(ManageCatagory.this,String.valueOf(dataSnapshot.getChildrenCount()),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void initialization(){
        findViewById();
        setListener();
    }

    public void findViewById(){
        edtTxtCatagoryName=(EditText)findViewById(R.id.edtTxtCatagoryName);
        imgCatagory=(ImageView)findViewById(R.id.imgViewCatagory);
        btnUpload=(Button)findViewById(R.id.btnUpload);
        btnSave=(Button)findViewById(R.id.btnSave);
        checkBoxActiveCatagory=(CheckBox)findViewById(R.id.checkBoxCatagoryActive);
        btnAddWeight=(Button)findViewById(R.id.btnAddWeight);
    }

    public void setListener(){
        btnSave.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnAddWeight.setOnClickListener(this);
        checkBoxActiveCatagory.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==btnSave){
            catagory.catid=maxID+1;
            catagory.name=edtTxtCatagoryName.getText().toString();
            catagoryReference.child(String.valueOf(catagory.catid)).setValue(catagory);
        }
        else if(v==btnUpload){
            try {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }catch (Exception e){
                System.out.print(e.toString());
            }
        }
        else if(v==checkBoxActiveCatagory){
            catagory.active=checkBoxActiveCatagory.isChecked();
        }
        else if(v==btnAddWeight){
            try {
                FragmentManager manager = getSupportFragmentManager();

                ProductWeightFragment editNameDialog = ProductWeightFragment.newInstance();
                editNameDialog.show(manager, "fragment_add_weight");
            }catch(Exception e){
                System.out.print(e.toString());
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                imgCatagory.setImageURI(selectedImage);
                uploadImage(selectedImage);
            }
        }catch (Exception e){
            System.out.print(e.toString());
        }


    }

    private void uploadImage(Uri file){
        try {
            catagory.name = edtTxtCatagoryName.getText().toString();
            final StorageReference riversRef = storageRef.child(imagePath + catagory.getName());
            Toast.makeText(this, imagePath + catagory.name, Toast.LENGTH_LONG).show();

            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Log.w(TAG, downloadUrl.toString());
                            catagory.imagepath = downloadUrl.toString();
                            Toast.makeText(ManageCatagory.this, catagory.imagepath, Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            Toast.makeText(ManageCatagory.this, "Unable to upload image", Toast.LENGTH_LONG).show();
                        }
                    });
        }catch (Exception e){
            System.out.print(e.toString());
        }
    }

    @Override
    public void addWeightAttribute(ProductAttribute productAttribute) {
         int id=catagory.weightAttributes.size();
         productAttribute.productAttribId=id+1;
         catagory.weightAttributes.add(productAttribute);
    }
}
