package com.bhojnalya.vikas.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioButton;


import com.bhojnalya.vikas.app.R;

import com.bhojnalya.vikas.app.model.ProductAttribute;


import java.util.ArrayList;

/**
 * Created by Vikas on 4/25/2017.
 */

public class ProductAttribAdapter extends BaseAdapter implements View.OnClickListener {

    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    ProductAttribute tempValues=null;
    int i=0;
    int selectedWeightId=0;

    /*************  CustomAdapter Constructor *****************/
    public ProductAttribAdapter(Activity a, ArrayList d,Resources resLocal,int selectedWeightId) {

        /********** Take passed values **********/
        activity = a;
        data=d;
        res = resLocal;
        this.selectedWeightId=selectedWeightId;
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

        public EditText edtTxtPrice;
        public RadioButton radioBtnWeight;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ProductAttribAdapter.ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.product_attrib_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ProductAttribAdapter.ViewHolder();
            holder.edtTxtPrice = (EditText) vi.findViewById(R.id.editTextPrice);
            holder.radioBtnWeight=(RadioButton)vi.findViewById(R.id.radioBtnWeight);


            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ProductAttribAdapter.ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            //holder.text.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = (ProductAttribute) data.get(position);

            /************  Set Model values in Holder elements ***********/

            holder.edtTxtPrice.setText( String.valueOf(tempValues.getPrice()));
            holder.radioBtnWeight.setText(tempValues.getWeightAttrib());
            holder.radioBtnWeight.setChecked(tempValues.productAttribId==selectedWeightId);

            /******** Set Item Click Listner for LayoutInflater for each row *******/

            vi.setOnClickListener(this);
        }
        return vi;
    }
}
