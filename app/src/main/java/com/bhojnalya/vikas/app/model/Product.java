package com.bhojnalya.vikas.app.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Vikas on 4/9/2017.
 */

public class Product implements Serializable {
    public long pid;
    public long catid;
    public String productName;
    public double price;
    public String imageUrl;
    public int selectedProductAttribId=1;
    public ArrayList<ProductAttribute> productAttributes;
    public boolean isActive;

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public long getCatid() {
        return catid;
    }

    public void setCatid(long catid) {
        this.catid = catid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<ProductAttribute> getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(ArrayList<ProductAttribute> productAttributes) {
        this.productAttributes = productAttributes;
    }

    public int getSelectedProductAttribId() {
        return selectedProductAttribId;
    }

    public void setSelectedProductAttribId(int selectedProductAttribId) {
        this.selectedProductAttribId = selectedProductAttribId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
