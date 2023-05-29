package com.group11.shoppuka.project.model.product;

import java.io.Serializable;

public class Product implements Serializable {
   private int id;
   private AttributesProduct attributes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AttributesProduct getAttributes() {
        return attributes;
    }

    public void setAttributesProduct(AttributesProduct attributesProduct) {
        this.attributes = attributesProduct;
    }
}

