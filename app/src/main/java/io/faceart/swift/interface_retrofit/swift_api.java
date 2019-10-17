package io.faceart.swift.interface_retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface swift_api {

    @POST("login?include=user")
    Call<Rider> getRider(@Body login credentials);
}
