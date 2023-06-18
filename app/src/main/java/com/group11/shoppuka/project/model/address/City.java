package com.group11.shoppuka.project.model.address;


import java.util.List;

public class City {
    public String name;
    public List<District> districts;

    public String getName() {
        return name;
    }

    public List<District> getDistricts() {
        return districts;
    }
}
