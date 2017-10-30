package com.bhojnalaya.vikas.app.customer;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.bhojnalya.vikas.app.R;
import com.bhojnalya.vikas.app.model.ContactPerson;
import com.bhojnalya.vikas.app.util.NetworkHelper;
import com.bhojnalya.vikas.app.util.SharedPrefHelper;
import com.bhojnalya.vikas.app.util.Utility;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity   implements ValueEventListener {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    FirebaseDatabase database;
    DatabaseReference contactPerson;
    ContactPerson cp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPrefHelper.initializePreferences(this);
        database = FirebaseDatabase.getInstance();
        contactPerson = database.getReference("contactPerson");
        contactPerson.addValueEventListener(this);
        if(!NetworkHelper.isNetworkAvailable(this)){
            //checkOnserver code
            String contact=SharedPrefHelper.getObjectAsString(SharedPrefHelper.CONTACT_KEY);
            if(contact==null){
                SharedPrefHelper.putObjectAsString(SharedPrefHelper.CONTACT_KEY, Utility.CONTACT);
            }
        }
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, HomeScreen.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        cp = dataSnapshot.getValue(ContactPerson.class);
        // Check for null
        if (cp == null) {
            SharedPrefHelper.putObjectAsString(SharedPrefHelper.CONTACT_KEY, Utility.CONTACT);
        }
        else{
            SharedPrefHelper.putObjectAsString(SharedPrefHelper.CONTACT_KEY, Utility.TEL_PREFIX+cp.contact);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
