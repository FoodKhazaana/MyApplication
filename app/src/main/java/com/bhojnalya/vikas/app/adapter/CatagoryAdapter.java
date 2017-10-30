package com.bhojnalya.vikas.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhojnalya.vikas.app.BuildConfig;
import com.bhojnalya.vikas.app.MainActivity;
import com.bhojnalya.vikas.app.R;
import com.bhojnalya.vikas.app.model.Catagory;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Vikas on 3/30/2017.
 */

public class CatagoryAdapter extends BaseAdapter implements View.OnClickListener {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    Catagory tempValues=null;
    int i=0;
    //ImageLoader imageLoader;

    /*************  CustomAdapter Constructor *****************/
    public CatagoryAdapter(Activity a, ArrayList d,Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data=d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //imageLoader = ImageLoader.getInstance();

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView text;
        public TextView text1;
        public TextView textWide;
        public ImageView image;

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){
            holder = new ViewHolder();
            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            if(BuildConfig.FLAVOR=="admin") {
                vi = inflater.inflate(R.layout.tabitem, null);
                holder.text = (TextView) vi.findViewById(R.id.text);
                holder.text1=(TextView)vi.findViewById(R.id.text1);
                holder.image=(ImageView)vi.findViewById(R.id.image);
            }
            else {
                vi = inflater.inflate(R.layout.catagory_view, null);
                holder.text = (TextView) vi.findViewById(R.id.catagory_name);
                holder.image=(ImageView)vi.findViewById(R.id.catagory_image);
            }
            holder.image.setTag(position);

            /****** View Holder Object to contain tabitem.xml file elements ******/




            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.text.setText("No Data");
        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = ( Catagory ) data.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.text.setText( tempValues.getName());
             // Get singleton instance
            ImageLoader.getInstance().displayImage(tempValues.getImagepath(), holder.image);

            /******** Set Item Click Listner for LayoutInflater for each row *******/

           // vi.setOnClickListener(new OnItemClickListener(position));
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    /********* Called when Item click in ListView ************/
    /*private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {


            MainActivity sct = (MainActivity)activity;

            *//****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****//*

            sct.onItemClick(mPosition);
        }
    }*/
}



