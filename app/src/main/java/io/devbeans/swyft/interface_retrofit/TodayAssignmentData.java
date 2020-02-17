package io.devbeans.swyft.interface_retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TodayAssignmentData {

    @SerializedName("vendorName")
    @Expose
    private String vendorName = "";
    @SerializedName("vendorId")
    @Expose
    private String vendorId = "";
    @SerializedName("pickupLocations")
    @Expose
    private List<Location> pickupLocations = null;
    @SerializedName("parcels")
    @Expose
    private List<String> parcels = null;

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

    public List<Location> getPickupLocations() {
        return pickupLocations;
    }

    public void setPickupLocations(List<Location> pickupLocations) {
        this.pickupLocations = pickupLocations;
    }

    public List<String> getParcels() {
        return parcels;
    }

    public void setParcels(List<String> parcels) {
        this.parcels = parcels;
    }
}
