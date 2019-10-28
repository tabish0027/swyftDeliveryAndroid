package io.faceart.swift.interface_retrofit_delivery;

import java.util.List;

import io.faceart.swift.interface_retrofit.DeliveryParcel;
import io.faceart.swift.interface_retrofit.Rider;
import io.faceart.swift.interface_retrofit.RiderActivity;
import io.faceart.swift.interface_retrofit.login;
import io.faceart.swift.interface_retrofit.manage_task;
import io.faceart.swift.interface_retrofit.online;
import io.faceart.swift.interface_retrofit.parcel_scan;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface swift_api_delivery {


    // delivery apis
    @GET("Riders/get-tasks")
    Call<List<RiderActivityDelivery>> manageTaskfordelivery(@Header("Authorization") String Authorization,@Query("riderId") String riderid);

    @POST("RiderTasks/{taskid}/manage-task")
    Call<List<RiderActivityDelivery>> manageTask(@Header("Authorization") String Authorization, @Path("taskid") String parcelid,@Body manage_task user_task);


}
