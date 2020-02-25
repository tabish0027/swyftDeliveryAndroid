package io.devbeans.swyft.interface_retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoadsheetHistoryModel {

    @SerializedName("parcelIds")
    @Expose
    private List<String> parcelIds = null;
    @SerializedName("riderId")
    @Expose
    private String riderId = "";
    @SerializedName("pickupLocationId")
    @Expose
    private String pickupLocationId = "";
    @SerializedName("vendorId")
    @Expose
    private String vendorId = "";
    @SerializedName("geopoints")
    @Expose
    private GeoPoints geopoints = null;
    @SerializedName("signatureUrl")
    @Expose
    private String signatureUrl = "";
    @SerializedName("pickupSheetUrl")
    @Expose
    private String pickupSheetUrl = "";
    @SerializedName("id")
    @Expose
    private String id = "";
    @SerializedName("createdAt")
    @Expose
    private String createdAt = "";
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt = "";
    @SerializedName("pickupLocation")
    @Expose
    private PickupLocationModel pickupLocation = null;

    public List<String> getParcelIds() {
        return parcelIds;
    }

    public void setParcelIds(List<String> parcelIds) {
        this.parcelIds = parcelIds;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
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

    public String getPickupSheetUrl() {
        return pickupSheetUrl;
    }

    public void setPickupSheetUrl(String pickupSheetUrl) {
        this.pickupSheetUrl = pickupSheetUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PickupLocationModel getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(PickupLocationModel pickupLocation) {
        this.pickupLocation = pickupLocation;
    }
}
