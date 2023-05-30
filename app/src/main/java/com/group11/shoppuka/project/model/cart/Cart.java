package com.group11.shoppuka.project.model.cart;

import java.io.Serializable;

public class Cart implements Serializable {
    private int id;
    private AttributesCart attributes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AttributesCart getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributesCart attributes) {
        this.attributes = attributes;
    }
}
