package com.bhojnalya.vikas.app.adapter;

/**
 * Created by RaviAgrawal on 12-10-2015.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bhojnalya.vikas.app.ProductListActivity;
import com.bhojnalya.vikas.app.R;
import com.bhojnalya.vikas.app.model.Product;
import com.bhojnalya.vikas.app.model.ProductAttribute;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ProductItemAdapter extends BaseAdapter implements Filterable{

    Context context;
    List<Product> rowItems;
    List<Product> filterItems;

    ProductListActivity sc;
    public ProductItemAdapter(Context context, List<Product> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
        this.filterItems=rowItems;
        this.sc=(ProductListActivity)context;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                // Now we have to inform the adapter about the new list filtered

                if (results.count == 0)

                    notifyDataSetInvalidated();

                else {

                    rowItems = (List<Product>) results.values;

                    notifyDataSetChanged();

                }
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();

                // We implement here the filter logic

                if (constraint == null || constraint.length() == 0) {

                    // No filter implemented we return all the list

                    results.values = filterItems;

                    results.count = filterItems.size();

                } else {

                    // We perform filtering operation

                    List<Product> nProductList = new ArrayList<Product>();


                    for (Product p : filterItems) {

                        if (p.getProductName().toUpperCase().startsWith(constraint.toString().toUpperCase()))

                            nProductList.add(p);

                    }


                    results.values = nProductList;

                    results.count = nProductList.size();


                }

                return results;

            }
        };
        return filter;

    }



    /* private view holder class */
    private static class ViewHolder {

        public final ImageView product_pic;
        public final TextView pname;
        public final TextView price;
       // public final Button addBtn;
        public final Spinner view_weight_spinner;
        public final int ref;
        public final int weight_selected_id;

        public ViewHolder(ImageView product_pic,TextView pname, TextView price,Spinner view_weight_spinner,int ref, int weight_selected_id) {
            this.product_pic = product_pic;
            this.pname = pname;
            this.price = price;
            this.view_weight_spinner = view_weight_spinner;
            this.ref = ref;
            this.weight_selected_id=weight_selected_id;
        }
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
         final ImageView product_pic;
         final TextView pname;
         final TextView price;
         //final Button addBtn;
        final int veg_ref;
         Spinner weight_spinner;
        final int veg_weight_selected_id;

        final LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.product_item, null);
            product_pic = (ImageView) convertView.findViewById(R.id.pic);
            pname = (TextView) convertView.findViewById(R.id.name);
            //addBtn = (Button) convertView.findViewById(R.id.addBtn);
            price = (TextView) convertView.findViewById(R.id.price);
            weight_spinner = (Spinner) convertView.findViewById(R.id.spinner);
            veg_ref=position;
            veg_weight_selected_id=0;
            convertView.setTag(new ViewHolder(product_pic,pname,price,weight_spinner,veg_ref,veg_weight_selected_id));
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            product_pic = viewHolder.product_pic;
            pname = viewHolder.pname;
            price = viewHolder.price;
            //addBtn = viewHolder.addBtn;
            weight_spinner = viewHolder.view_weight_spinner;

           }
            final int pos=position;
            final Product row_pos = rowItems.get(position);

        ImageLoader.getInstance().displayImage(row_pos.getImageUrl(), product_pic);
        pname.setText(row_pos.getProductName());

        ArrayAdapter<ProductAttribute> myAdapter = new ArrayAdapter<>(context, R.layout.spinner_text_view, row_pos.productAttributes);
        weight_spinner.setAdapter(myAdapter);

        weight_spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        ProductAttribute selectedCatagory=row_pos.getProductAttributes().get(position);
                        selectedCatagory.price=(selectedCatagory.percentage*row_pos.price)/100;
                        price.setText(String.format("%.2f",selectedCatagory.getPrice()));
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        if(position % 2 == 0){
            convertView.setBackgroundColor(Color.rgb(255, 255, 255));
        }
        else {
            convertView.setBackgroundColor(Color.rgb(255, 255, 255));
        }
        return convertView;
    }
}



