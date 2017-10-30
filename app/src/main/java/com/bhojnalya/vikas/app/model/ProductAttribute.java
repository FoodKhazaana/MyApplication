package com.bhojnalya.vikas.app.model;

import java.io.Serializable;

/**
 * Created by Vikas on 4/13/2017.
 */

public class ProductAttribute  implements Serializable {
    public int productAttribId;
    public String weightAttrib;
    public double price;
    public double percentage;

    public int getProductAttribId() {
        return productAttribId;
    }

    public void setProductAttribId(int productAttribId) {
        this.productAttribId = productAttribId;
    }

    public String getWeightAttrib() {
        return weightAttrib;
    }

    public void setWeightAttrib(String weightAttrib) {
        this.weightAttrib = weightAttrib;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String toString()
    {
        return weightAttrib;
    }
}
