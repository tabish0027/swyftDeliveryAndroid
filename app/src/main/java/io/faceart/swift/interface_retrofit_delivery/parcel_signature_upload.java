 package io.faceart.swift.interface_retrofit_delivery;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class parcel_signature_upload {
    @SerializedName("currentStatusId")
    @Expose
    private String currentStatusId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("parcelBatchId")
    @Expose
    private String parcelBatchId;
    @SerializedName("vendorId")
    @Expose
    private String vendorId;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("weight")
    @Expose
    private Integer weight;
    @SerializedName("qty")
    @Expose
    private Integer qty;
    @SerializedName("vendorParcelId")
    @Expose
    private Integer vendorParcelId;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("orderType")
    @Expose
    private String orderType;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("boxType")
    @Expose
    private String boxType;
    @SerializedName("additionalServices")
    @Expose
    private List<String> additionalServices = null;
    @SerializedName("pickupLocationId")
    @Expose
    private String pickupLocationId;
    @SerializedName("customerDataId")
    @Expose
    private String customerDataId;
    @SerializedName("deliverySignature")
    @Expose
    private String deliverySignature;

    public String getCurrentStatusId() {
        return currentStatusId;
    }

    public void setCurrentStatusId(String currentStatusId) {
        this.currentStatusId = currentStatusId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParcelBatchId() {
        return parcelBatchId;
    }

    public void setParcelBatchId(String parcelBatchId) {
        this.parcelBatchId = parcelBatchId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getVendorParcelId() {
        return vendorParcelId;
    }

    public void setVendorParcelId(Integer vendorParcelId) {
        this.vendorParcelId = vendorParcelId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBoxType() {
        return boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }

    public List<String> getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(List<String> additionalServices) {
        this.additionalServices = additionalServices;
    }

    public String getPickupLocationId() {
        return pickupLocationId;
    }

    public void setPickupLocationId(String pickupLocationId) {
        this.pickupLocationId = pickupLocationId;
    }

    public String getCustomerDataId() {
        return customerDataId;
    }

    public void setCustomerDataId(String customerDataId) {
        this.customerDataId = customerDataId;
    }

    public String getDeliverySignature() {
        return deliverySignature;
    }

    public void setDeliverySignature(String deliverySignature) {
        this.deliverySignature = deliverySignature;
    }

}