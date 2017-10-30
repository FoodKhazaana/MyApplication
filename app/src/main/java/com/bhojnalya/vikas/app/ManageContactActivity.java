package com.bhojnalya.vikas.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bhojnalya.vikas.app.model.ContactPerson;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManageContactActivity extends AppCompatActivity  implements View.OnClickListener,ValueEventListener {
    FirebaseDatabase database;
    DatabaseReference contactPerson;
    Button btnSaveContact;
    EditText edtName,edtNumber;
    ContactPerson cp;
    private static final String TAG = ManageContactActivity.class.getSimpleName();
    boolean isSaved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_contact);
        setUI();
        database = FirebaseDatabase.getInstance();
        contactPerson = database.getReference("contactPerson");
        contactPerson.addValueEventListener(this);
    }

    public void setUI(){
        btnSaveContact=(Button)findViewById(R.id.btnSaveContact);
        btnSaveContact.setOnClickListener(this);

        edtName=(EditText)findViewById(R.id.edtName);
        edtNumber=(EditText)findViewById(R.id.edtNumber);
    }

    @Override
    public void onClick(View view) {
        if(view==btnSaveContact){
            isSaved=false;
            String name=edtName.getText().toString();
            String number=edtNumber.getText().toString();
            if(cp==null)
                cp=new ContactPerson();
            cp.setName(name);
            cp.setContact(number);
            contactPerson.setValue(cp);
            isSaved=true;
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        cp = dataSnapshot.getValue(ContactPerson.class);
        // Check for null
        if (cp == null) {
        return;
        }
        edtName.setText(cp.getName());
        edtNumber.setText(cp.getContact());
        if(isSaved){
            Toast.makeText(this,"Detail saved successfully",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
