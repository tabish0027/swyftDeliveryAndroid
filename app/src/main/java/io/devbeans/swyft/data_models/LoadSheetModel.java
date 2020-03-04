package io.devbeans.swyft.data_models;

import java.util.List;

import io.devbeans.swyft.interface_retrofit.GeoPoints;

public class LoadSheetModel {
    public List<String> parcelIds;
    public GeoPoints geopoints;
    public String signatureUrl;
    public String pickupLocationId;
    public String vendorId;
    public String pickupSheetUrl;
    public String name;

    public List<String> getParcelIds() {
        return parcelIds;
    }

    public void setParcelIds(List<String> parcelIds) {
        this.parcelIds = parcelIds;
    }

    public GeoPoints getGeopoints() {
        return geopoints;
    }

    public void setGeopoints(GeoPoints geopoints) {
        this.geopoints = geopoints;
    }

    public String getSignatureUrl() {
        return signatureUrl;
    }

    public void setSignatureUrl(String signatureUrl) {
        this.signatureUrl = signatureUrl;
    }

    public String getPickupLocationId() {
        return pickupLocationId;
    }

    public void setPickupLocationId(String pickupLocationId) {
        this.pickupLocationId = pickupLocationId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getPickupSheetUrl() {
        return pickupSheetUrl;
    }

    public void setPickupSheetUrl(String pickupSheetUrl) {
        this.pickupSheetUrl = pickupSheetUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
