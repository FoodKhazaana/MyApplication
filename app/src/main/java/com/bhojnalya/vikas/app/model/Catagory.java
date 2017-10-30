package com.bhojnalya.vikas.app.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Vikas on 3/29/2017.
 */

public class Catagory implements Serializable {
    public long catid;
    public String name;
    public boolean active;
    public String imagepath;
    public ArrayList<Product> products;
    public ArrayList<ProductAttribute> weightAttributes;

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public long getCatid() {
        return catid;
    }

    public void setCatid(long catid) {
        this.catid = catid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<ProductAttribute> getWeightAttributes() {
        return weightAttributes;
    }

    public void setWeightAttributes(ArrayList<ProductAttribute> weightAttributes) {
        this.weightAttributes = weightAttributes;
    }

    public String toString()
    {
        return name;
    }
}
