
package io.faceart.swift.interface_retrofit_delivery;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RiderActivityDelivery {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("taskId")
    @Expose
    private String taskId;
    @SerializedName("taskStatus")
    @Expose
    private String taskStatus;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

}
