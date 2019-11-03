
package io.faceart.swift.interface_retrofit_delivery;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.faceart.swift.interface_retrofit.Location;
import io.faceart.swift.interface_retrofit_delivery.Parcel;

public class Datum {

    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("parcels")
    @Expose
    private List<Parcel> parcels = null;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Parcel> getParcels() {
        return parcels;
    }

    public void setParcels(List<Parcel> parcels) {
        this.parcels = parcels;
    }

    public void markAllParcelToBeComplete(){
        for(int i =0 ; i<parcels.size();i++){
            parcels.get(i).parcel_to_mark_complete = true;
        }
    }
}
