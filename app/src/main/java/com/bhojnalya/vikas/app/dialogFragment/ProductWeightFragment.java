package com.bhojnalya.vikas.app.dialogFragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bhojnalya.vikas.app.R;
import com.bhojnalya.vikas.app.model.ProductAttribute;

public class ProductWeightFragment extends DialogFragment implements View.OnClickListener {

    private EditText editTextWeight;
    private EditText editTextPercentage;
    private Button btnAddWeight;



    public ProductWeightFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ProductWeightFragment newInstance() {
        ProductWeightFragment fragment = new ProductWeightFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catagory_weight_attrib, container);
        getDialog().setTitle("Add New Weight");
        editTextWeight = (EditText) view.findViewById(R.id.editTextWeightDesc);
        editTextPercentage = (EditText) view.findViewById(R.id.editTextPercentage);
        btnAddWeight=(Button)view.findViewById(R.id.btnAddWeightfr);
        btnAddWeight.setOnClickListener(this);
        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
         if(v==btnAddWeight){
             ProductAttribute productAttribute=new ProductAttribute();
             productAttribute.weightAttrib=editTextWeight.getText().toString();
             productAttribute.percentage=Double.parseDouble(editTextPercentage.getText().toString());
             WeightAddListener activity = (WeightAddListener) getActivity();
             activity.addWeightAttribute(productAttribute);
             this.dismiss();
         }
    }

    public interface WeightAddListener{
        void addWeightAttribute(ProductAttribute productAttribute);
    }
}
