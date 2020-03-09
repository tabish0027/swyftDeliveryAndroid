package io.devbeans.swyft.interface_retrofit;

import androidx.room.Update;

import java.util.List;

import io.devbeans.swyft.data_models.LoadSheetModel;
import io.devbeans.swyft.data_models.UpdateToken;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface swift_api {

    @POST("Riders/login?include=user")
    Call<Rider> getRiderFromLogin(@Body login credentials);

    @PATCH("Riders/{riderId}")
    Call<RiderDetails> updateRiderforToken(@Header("Authorization") String Authorization, @Body UpdateToken credentials, @Path("riderId") String riderId);


    //@PATCH("Riders/{riderId}")
    //Call<RiderActivity> getRiderFromLogin(@Header("Authorization") String Authorization,@Path("riderId") String riderId);

    @GET("Riders/{riderId}")
    Call<RiderDetails> getRider(@Header("Authorization") String Authorization,@Path("riderId") String riderId);

    @GET("Riders/{riderId}/get-today-assignments")
    Call<TodayAssignments> getTodayAssignment(@Header("Authorization") String Authorization,@Path("riderId") String riderId);

    @POST("Riders/{riderId}/generate-loadsheet")
    Call<PasswordResetRequest> generateLoadsheet(@Header("Authorization") String Authorization, @Path("riderId") String riderId, @Body LoadSheetModel loadSheetModel);

    @GET("app-version-check")
    Call<Void> getversioncontrol(@Header("Authorization") String Authorization, @Query("version") String version);

    @GET("Riders/get-vendor-parcel-ids")
    Call<List<String>> getParcelsofVendor(@Header("Authorization") String Authorization, @Query("vendorId") String vendorId);

    @POST("Riders/mark-attendance")
    Call<RiderDetails> markattendance(@Header("Authorization") String Authorization,@Body markattendance status);

    @GET("Riders/get-tasks")
    Call<List<PickupParcel>> getParcelsByRiders(@Header("Authorization") String Authorization, @Query("riderId") String riderId);

    @GET("Riders/{riderId}/loadsheets")
    Call<List<LoadsheetHistoryModel>> getLoadsheetHistory(@Header("Authorization") String Authorization, @Path("riderId") String riderId, @Query("filter[include][pickupLocation]") String vendor, @Query("filter[order]") String encodedPath);

    @POST("Parcels/{parcelid}/scan-parcel")
    Call<List<PickupParcel>> scanParcels(@Header("Authorization") String Authorization, @Path("parcelid") String parcelid,@Body parcel_scan user_task);

    @POST("RiderTasks/{tasklid}")
    Call<List<PickupParcel>> RiderTasksupdate(@Header("Authorization") String Authorization, @Path("tasklid") String tasklid);

    @POST("RiderTasks/{taskid}/manage-task")
    Call<List<PickupParcel>> manageTask(@Header("Authorization") String Authorization, @Path("taskid") String parcelid,@Body manage_task user_task);

    @POST("Riders/forgot-password")
    Call<PasswordResetRequest> requestotp(@Body username name);

    @POST("Riders/reset-password-via-otp")
    Call<PasswordResetRequest> reset_password_forget(@Body reset_password name);

}
