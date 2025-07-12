package com.one.societyAPI.dto;

import java.util.List;

public class PatchSocietyRequest {

    private String name;
    private String address;
    private Integer totalFlats;
    private List<FlatRequest> flats;

    // Getters and setters

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

    public Integer getTotalFlats() {
        return totalFlats;
    }

    public void setTotalFlats(Integer totalFlats) {
        this.totalFlats = totalFlats;
    }

    public List<FlatRequest> getFlats() {
        return flats;
    }

    public void setFlats(List<FlatRequest> flats) {
        this.flats = flats;
    }
}

