package io.faceart.swift.interface_retrofit_delivery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class delivery_wallet {

    @SerializedName("amount")
    @Expose
    private int amount;


    public int getamount() {
        return amount;
    }

    public void setDaily(int amount) {
        this.amount = amount;
    }



}