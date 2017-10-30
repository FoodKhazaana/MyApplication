package com.bhojnalya.vikas.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bhojnalya.vikas.app.R;
import com.bhojnalya.vikas.app.model.ProductAttribute;

import java.util.ArrayList;

/**
 * Created by Vikas on 4/25/2017.
 */

public class CatagoryAttribAdapter extends BaseAdapter implements View.OnClickListener {

    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    ProductAttribute tempValues=null;
    int i=0;

    /*************  CustomAdapter Constructor *****************/
    public CatagoryAttribAdapter(Activity a, ArrayList d, Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data=d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public int getCount() {
        if(data.size()<=0)
            return 0;
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onClick(View v) {

    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView txtViewWeight;
        public TextView txtViewPercent;
        public ImageButton imgBtnEditWeight;
        public ImageButton imgBtnDeleteWeight;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        CatagoryAttribAdapter.ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.catagory_attrib_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new CatagoryAttribAdapter.ViewHolder();
            holder.txtViewWeight = (TextView) vi.findViewById(R.id.textViewWeight);
            holder.txtViewPercent=(TextView)vi.findViewById(R.id.textViewPercent);
            holder.imgBtnEditWeight=(ImageButton)vi.findViewById(R.id.imageButtonEditWeight);
            holder.imgBtnDeleteWeight=(ImageButton)vi.findViewById(R.id.imageButtonDeleteWeight);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(CatagoryAttribAdapter.ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            //holder.text.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = (ProductAttribute) data.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.txtViewWeight.setText( String.valueOf(tempValues.getWeightAttrib()) );
            holder.txtViewPercent.setText(String.valueOf(tempValues.getPercentage()));
            //holder.text1.setText( tempValues.getUrl() );
            /*holder.image.setImageResource(
                    res.getIdentifier(
                            "com.androidexample.customlistview:drawable/"+tempValues.getImage()
                            ,null,null));
*/
            /******** Set Item Click Listner for LayoutInflater for each row *******/

            vi.setOnClickListener(this);
        }
        return vi;
    }
}
