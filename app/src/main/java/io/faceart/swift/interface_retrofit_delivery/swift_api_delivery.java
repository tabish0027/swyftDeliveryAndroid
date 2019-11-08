package io.faceart.swift.interface_retrofit_delivery;

import android.graphics.Bitmap;

import java.util.List;

import io.faceart.swift.interface_retrofit.DeliveryParcel;
import io.faceart.swift.interface_retrofit.Rider;
import io.faceart.swift.interface_retrofit.RiderActivity;
import io.faceart.swift.interface_retrofit.login;
import io.faceart.swift.interface_retrofit.manage_task;
import io.faceart.swift.interface_retrofit.online;
import io.faceart.swift.interface_retrofit.parcel_scan;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface swift_api_delivery {


    // delivery apis
    @GET("Riders/get-tasks")
    Call<List<RiderActivityDelivery>> manageTaskfordelivery(@Header("Authorization") String Authorization,@Query("riderId") String riderid);

    @POST("RiderTasks/{taskid}/manage-task")
    Call<List<RiderActivityDelivery>> manageTask(@Header("Authorization") String Authorization, @Path("taskid") String taskid,@Body manage_task user_task);


    @POST("Parcels/manage-parcel")
    Call<List<RiderActivityDelivery>> markParcelComplete(@Header("Authorization") String Authorization, @Body mark_parcel_complete parcels);



    //@POST("patch-upload")
   //Call<parcel_signature_upload> uploadSignature(  @Body patch_upload signature_data);

    @POST("Parcels/scan-delivery-parcel")
    Call<List<RiderActivityDelivery>> scan_parcels_delivery(@Header("Authorization") String Authorization, @Body parcel_scan_delivery scan);

    @Multipart
    @POST("patch-upload")
    Call<parcel_signature_upload> uploadSignature(   @Part MultipartBody.Part file,
                                                     @Part("patchModel") RequestBody patchModel,
                                                    @Part("modelInstanceId") RequestBody modelInstanceId,
                                                    @Part("uploadContainer") RequestBody uploadContainer,
                                                    @Part("updateOn") RequestBody updateOn
                                                    );


}
