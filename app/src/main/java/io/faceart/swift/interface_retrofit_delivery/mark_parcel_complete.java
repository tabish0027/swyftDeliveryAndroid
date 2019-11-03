package io.faceart.swift.interface_retrofit_delivery;

import java.lang.reflect.Array;
import java.util.List;

public class mark_parcel_complete {
    public List<String> parcelIds = null;
    public String status = "";
    public String taskId = "";

    public double lat = 0.0;
    public double lng = 0.0;
    public String reason = "";

    public mark_parcel_complete(List<String> parcelIds, String status, String taskId, double lat, double lng, String reason) {
        this.parcelIds = parcelIds;
        this.status = status;
        this.taskId = taskId;
        this.lat = lat;
        this.lng = lng;
        this.reason = reason;
    }
}
