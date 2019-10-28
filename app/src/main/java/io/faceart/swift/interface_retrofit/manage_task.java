package io.faceart.swift.interface_retrofit;

public class manage_task {
    public String status;

    public String parcelId;

    public manage_task(String status, String parcelId) {
        this.status = status;
        this.parcelId = parcelId;
    }
    public manage_task(String status) {
        this.status = status;
    }
}
