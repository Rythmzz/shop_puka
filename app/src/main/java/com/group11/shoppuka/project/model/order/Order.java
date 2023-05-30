package com.group11.shoppuka.project.model.order;

public class Order {
    private int id;
    private AttributesOrder attributes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AttributesOrder getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributesOrder attributes) {
        this.attributes = attributes;
    }
}
