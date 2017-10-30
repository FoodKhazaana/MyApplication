package com.bhojnalaya.vikas.app.customer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bhojnalya.vikas.app.ProductListActivity;
import com.bhojnalya.vikas.app.R;
import com.bhojnalya.vikas.app.adapter.CatagoryAdapter;
import com.bhojnalya.vikas.app.model.Catagory;
import com.bhojnalya.vikas.app.util.SharedPrefHelper;
import com.bhojnalya.vikas.app.util.Utility;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;


public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private SliderLayout mDemoSlider;
    private StorageReference storageRef;
    FirebaseDatabase database;
    DatabaseReference catagory;
    public static final String TAG="HomeScreen";
    ArrayList<Catagory> catlist=new ArrayList<Catagory>();
    ListView list;
    CatagoryAdapter adapter;
    Resources res;
    final Activity activity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        try {
            SharedPrefHelper.initializePreferences(this);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*/
                    makecall();

                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            mDemoSlider = (SliderLayout) findViewById(R.id.slider);


            HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
            file_maps.put("Special Thali", R.drawable.image1);
            file_maps.put("Sweats", R.drawable.image2);
            file_maps.put("Rice", R.drawable.image3);
            file_maps.put("Paneer", R.drawable.image4);
            file_maps.put("Fruits", R.drawable.image5);
            file_maps.put("Vegetables", R.drawable.image6);

            for (String name : file_maps.keySet()) {
                TextSliderView textSliderView = new TextSliderView(this);
                // initialize a SliderLayout
                textSliderView
                        .description(name)
                        .image(file_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", name);

                mDemoSlider.addSlider(textSliderView);
            }
            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(4000);
            mDemoSlider.addOnPageChangeListener(this);

            list = (ListView) findViewById(R.id.catagoryListView);  // List defined in XML ( See Below )
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Object o = adapter.getItem(position);
                    Catagory catagory=(Catagory)o;//As you are using Default String Adapter
                    if(catagory.getProducts()==null || catagory.getProducts().size()==0) {
                        Toast.makeText(getBaseContext(), "Currently no product exist related to this catagory", Toast.LENGTH_SHORT).show();
                    return;
                    }
                        Intent intent=new Intent(HomeScreen.this,ProductListActivity.class);
                    Bundle b=new Bundle();
                    b.putSerializable("products",catagory.getProducts());
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
            res = getResources();
            database = FirebaseDatabase.getInstance();
            storageRef = FirebaseStorage.getInstance().getReference();
            catagory = database.getReference("catagory");
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
                    adapter = new CatagoryAdapter(HomeScreen.this, catlist, res);
                    list.setAdapter(adapter);
                    Log.d(TAG, "Value is: " + dataSnapshot.getChildrenCount());
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }catch (Exception e){
            Log.w(TAG,  e.toString(),null);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
