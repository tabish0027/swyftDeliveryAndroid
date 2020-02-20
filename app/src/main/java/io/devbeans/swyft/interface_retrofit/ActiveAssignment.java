package io.devbeans.swyft.interface_retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActiveAssignment {

    @SerializedName("estimatedParcels")
    @Expose
    private int estimatedParcels = 0;
    @SerializedName("address")
    @Expose
    private String address= "";
    @SerializedName("vendorName")
    @Expose
    private String vendorName= "";
    @SerializedName("vendorId")
    @Expose
    private String vendorId= "";
    @SerializedName("geopoints")
    @Expose
    private GeoPoints geopoints = null;
    @SerializedName("pickupLocationAddress")
    @Expose
    private String pickupLocationAddress= "";

    public int getEstimatedParcels() {
        return estimatedParcels;
    }

    public void setEstimatedParcels(int estimatedParcels) {
        this.estimatedParcels = estimatedParcels;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public GeoPoints getGeopoints() {
        return geopoints;
    }

    public void setGeopoints(GeoPoints geopoints) {
        this.geopoints = geopoints;
    }

    public String getPickupLocationAddress() {
        return pickupLocationAddress;
    }

    public void setPickupLocationAddress(String pickupLocationAddress) {
        this.pickupLocationAddress = pickupLocationAddress;
    }
}
