package com.one.societyAPI.dto;

import com.one.societyAPI.entity.Flat;

public class FlatRequest {

    private Long id;
    private String flatNumber;
    private String block;
    private String ownerName;

    public FlatRequest() {}  // 👈 required for Jackson

    public FlatRequest(Flat flat) {
        this.id = flat.getId();
        this.block = flat.getBlock();        // assuming your Flat entity has this
        this.flatNumber = flat.getFlatNumber();  // assuming your Flat entity has this
    }

    // Getters and Setters

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

