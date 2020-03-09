package io.devbeans.swyft.interface_retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TodayAssignments {

    @SerializedName("activeAssignments")
    @Expose
    private List<ActiveAssignment> activeAssignments = null;
    @SerializedName("data")
    @Expose
    private List<TodayAssignmentData> data = null;

    public List<ActiveAssignment> getActiveAssignments() {
        return activeAssignments;
    }

    public void setActiveAssignments(List<ActiveAssignment> activeAssignments) {
        this.activeAssignments = activeAssignments;
    }

    public List<TodayAssignmentData> getData() {
        return data;
    }

    public void setData(List<TodayAssignmentData> data) {
        this.data = data;
    }
}
