
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

    public GeoPoints getGeoPoints() {
        return geopoints;
    }

    public void setGeoPoints(GeoPoints geoPoints) {
        this.geopoints = geoPoints;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
