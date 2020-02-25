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
}
