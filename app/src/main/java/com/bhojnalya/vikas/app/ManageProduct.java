package com.bhojnalya.vikas.app;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bhojnalya.vikas.app.model.Catagory;
import com.bhojnalya.vikas.app.model.Product;
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


public class ManageProduct extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    ArrayList<Catagory> catagories;
    Spinner mySpinner;
    Button btnSaveProduct,btnUploadProductImage;
    TextView textViewWeightDesc;
    EditText editTextProductName,editTextProductPrice;
    ImageView imgProduct;

    Product product;
    public String imagePath="images/";
    public static final String TAG="ManageProduct";

    private StorageReference storageRef;
    FirebaseDatabase database;
    DatabaseReference catagoryReference;

    public static int RESULT_LOAD_IMAGE=1;
    boolean saveDone=false;
    //ProductAttribAdapter adapter;
    //Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_product);

        database = FirebaseDatabase.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        catagoryReference = database.getReference("catagory");

        mySpinner = (Spinner) findViewById(R.id.spinner);
        btnSaveProduct=(Button)findViewById(R.id.btnSaveProduct);
        btnUploadProductImage=(Button)findViewById(R.id.btnUploadProductImage);
        textViewWeightDesc=(TextView)findViewById(R.id.textViewWeightDesc);
        editTextProductName=(EditText)findViewById(R.id.edtTxtProductName);
        editTextProductPrice=(EditText)findViewById(R.id.edtTxtPrice);
        imgProduct=(ImageView)findViewById(R.id.imageView);

        btnSaveProduct.setOnClickListener(this);
        btnUploadProductImage.setOnClickListener(this);
        mySpinner.setOnItemSelectedListener(this);
        //productAttribLstView=(ListView)findViewById(R.id.productAttribLstView);
        //res=getResources();


        Bundle b=this.getIntent().getExtras();
        if(b!=null){
            catagories= (ArrayList<Catagory>) b.getSerializable("catagories");
        }
        if(product==null) {
            product = new Product();
            product.productAttributes=new ArrayList<>();
        }

        //adapter=new ProductAttribAdapter(this, product.productAttributes,res,product.selectedProductAttribId);
        //productAttribLstView.setAdapter(adapter);

        //ArrayAdapter<Catagory> myAdapter = new ArrayAdapter<Catagory>(this, android.R.layout.simple_spinner_item, catagories);
        ArrayAdapter<Catagory> myAdapter = new ArrayAdapter<Catagory>(this, R.layout.spinner_text_view, catagories);
        mySpinner.setAdapter(myAdapter);

        catagoryReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(saveDone) {
                    Toast.makeText(ManageProduct.this,"Product added successfully",Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v==btnSaveProduct){
            try {
                Catagory selectedCatagory;
                if (!(mySpinner.getSelectedItem() == null)) {
                    selectedCatagory = (Catagory) mySpinner.getSelectedItem();
                    if (selectedCatagory.products == null)
                        selectedCatagory.products = new ArrayList<>();
                    product.pid = selectedCatagory.products.size();
                    product.productName = editTextProductName.getText().toString();
                    product.price = Double.parseDouble(editTextProductPrice.getText().toString());
                    product.catid=selectedCatagory.catid;
                    product.isActive=true;
                    saveProduct(selectedCatagory.catid, product);
                }
            }catch (Exception e){
                System.out.print(e.toString());
            }
        }
        else if(v==btnUploadProductImage){
            try {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }catch (Exception e){
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
                imgProduct.setImageURI(selectedImage);
                uploadImage(selectedImage);
            }
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Catagory selectedCatagory=catagories.get(position);
        product.productAttributes.clear();
        for (ProductAttribute pa:selectedCatagory.weightAttributes) {
            product.productAttributes.add(pa);
            if(product.selectedProductAttribId==pa.productAttribId)
                textViewWeightDesc.setText(pa.getWeightAttrib());
        }
        //adapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void saveProduct(long catid,Product product){
        saveDone=true;
        catagoryReference.child(String.valueOf(catid)).child("products").child(String.valueOf(product.pid)).setValue(product);
    }

    private void uploadImage(Uri file){
        try {
            product.productName = editTextProductName.getText().toString();
            final StorageReference riversRef = storageRef.child(imagePath + product.productName);
            Toast.makeText(this, imagePath + product.productName, Toast.LENGTH_LONG).show();

            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Log.w(TAG, downloadUrl.toString());
                            product.imageUrl = downloadUrl.toString();
                            Toast.makeText(ManageProduct.this, product.imageUrl, Toast.LENGTH_LONG).show();
                            btnSaveProduct.setEnabled(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            Toast.makeText(ManageProduct.this, "Unable to upload image", Toast.LENGTH_LONG).show();
                        }
                    });
        }catch (Exception e){
            System.out.print(e.toString());
        }
    }
}
