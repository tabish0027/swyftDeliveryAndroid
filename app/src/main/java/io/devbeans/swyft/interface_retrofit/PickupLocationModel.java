package io.devbeans.swyft.interface_retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PickupLocationModel {

    @SerializedName("id")
    @Expose
    private String id = "";
    @SerializedName("address")
    @Expose
    private String address = "";
    @SerializedName("vendorId")
    @Expose
    private String vendorId = "";
    @SerializedName("vendor")
    @Expose
    private VendorModel vendor = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public VendorModel getVendor() {
        return vendor;
    }

    public void setVendor(VendorModel vendor) {
        this.vendor = vendor;
    }
}
