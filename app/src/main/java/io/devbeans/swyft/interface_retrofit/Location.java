
package io.devbeans.swyft.interface_retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("geopoints")
    @Expose
    private GeoPoints geopoints;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("id")
    @Expose
    private String id;


    public GeoPoints getGeopoints() {
        return geopoints;
    }

    public void setGeopoints(GeoPoints geopoints) {
        this.geopoints = geopoints;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
