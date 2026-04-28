package com.vinculo.domain.inventory.model;

import jakarta.persistence.Embeddable;

@Embeddable
public record Location(String address,Double latitude,Double longitude) {

    public Location(String address, Double latitude, Double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAddress() { return address; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
}