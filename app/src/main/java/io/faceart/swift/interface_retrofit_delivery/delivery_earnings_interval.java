package io.faceart.swift.interface_retrofit_delivery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class delivery_earnings_interval {

    @SerializedName("fuel")
    @Expose
    private Integer fuel;
    @SerializedName("maintenance")
    @Expose
    private Integer maintenance;
    @SerializedName("earnings")
    @Expose
    private Integer earnings;

    public Integer getFuel() {
        return fuel;
    }

    public void setFuel(Integer fuel) {
        this.fuel = fuel;
    }

    public Integer getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Integer maintenance) {
        this.maintenance = maintenance;
    }

    public Integer getEarnings() {
        return earnings;
    }

    public void setEarnings(Integer earnings) {
        this.earnings = earnings;
    }

}