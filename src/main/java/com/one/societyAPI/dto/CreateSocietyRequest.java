package com.one.societyAPI.dto;

import java.util.ArrayList;
import java.util.List;

public class CreateSocietyRequest {

    private String name;
    private String address;
    private int totalFlats;
    private List<FlatRequest> flats = new ArrayList<>();

    // Getters and Setters


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotalFlats() {
        return totalFlats;
    }

    public void setTotalFlats(int totalFlats) {
        this.totalFlats = totalFlats;
    }

    public List<FlatRequest> getFlats() {
        return flats;
    }

    public void setFlats(List<FlatRequest> flats) {
        this.flats = flats;
    }
}
