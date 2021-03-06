package io.swyft.pickup;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import io.devbeans.swyft.Databackbone;
import io.devbeans.swyft.activity_mapview;
import io.devbeans.swyft.interface_retrofit.RiderDetails;
import io.devbeans.swyft.interface_retrofit.TodayAssignments;
import io.devbeans.swyft.interface_retrofit.swift_api;
import io.swyft.pickup.R;
import io.devbeans.swyft.activity_login;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Splash extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences_parcels;
    SharedPreferences.Editor mEditor_parcels;
    public static final String MyPREFERENCES_parcels = "ScannedList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        mEditor = sharedpreferences.edit();
        sharedpreferences_parcels = getSharedPreferences(MyPREFERENCES_parcels, MODE_PRIVATE);
        mEditor_parcels = sharedpreferences_parcels.edit();


        Databackbone.getinstance().contextapp = getApplicationContext();
        Context con = Databackbone.getinstance().contextapp;
        Thread thr= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(0);

                    if (sharedpreferences.getString("TOKEN", "") != null && !sharedpreferences.getString("TOKEN", "").isEmpty()){
                        getRiderDetail();
                    }else {
                        Intent i = new Intent(Splash.this, activity_login.class);
                        startActivity(i);
                        finish();
                    }


                }catch (Exception i){

                }
            }
        });
        thr.start();

    }

    public void getRiderDetail() {
        Retrofit retrofit = Databackbone.getinstance().getRetrofitbuilder();
        swift_api riderapi = retrofit.create(swift_api.class);

        Call<RiderDetails> call = riderapi.getRider(sharedpreferences.getString("AccessToken", ""), sharedpreferences.getString("RiderID", ""));
        call.enqueue(new Callback<RiderDetails>() {
            @Override
            public void onResponse(Call<RiderDetails> call, Response<RiderDetails> response) {
                if (response.isSuccessful()) {

                    RiderDetails riderActivity = response.body();
                    Databackbone.getinstance().riderdetails = riderActivity;

                    if (Databackbone.getinstance().riderdetails.getType().equals("delivery")){
                        if (ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED &&
                                ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
                                ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                            ActivityCompat.requestPermissions(Splash.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        else {
                            Intent i = new Intent(Splash.this, activity_mapview.class);
                            startActivity(i);
                            finish();
                        }
                    }else {
                        getTodayAssignments();
                    }

                } else {
                    if (response.code() == 401) {
                        //sharedpreferences must be removed
                        mEditor.clear().commit();
                        mEditor_parcels.clear().commit();
                        Intent intent = new Intent(Splash.this, activity_login.class);
                        startActivity(intent);
                        finishAffinity();
                    }else {
                        Databackbone.getinstance().showAlsertBox(Splash.this, getResources().getString(R.string.error), "Error Connecting To Server Error Code 33");
                    }
                    //DeactivateRider();
                }

            }

            @Override
            public void onFailure(Call<RiderDetails> call, Throwable t) {
                System.out.println(t.getCause());
                Databackbone.getinstance().showAlsertBox(Splash.this, getResources().getString(R.string.error), "Error Connecting To Server Error Code 34");

                //DeactivateRider();
            }
        });
    }

    public void getTodayAssignments() {
        Retrofit retrofit = Databackbone.getinstance().getRetrofitbuilder();
        swift_api todayAssignment = retrofit.create(swift_api.class);

        Call<TodayAssignments> call = todayAssignment.getTodayAssignment(sharedpreferences.getString("AccessToken", ""), sharedpreferences.getString("RiderID", ""));
        call.enqueue(new Callback<TodayAssignments>() {
            @Override
            public void onResponse(Call<TodayAssignments> call, Response<TodayAssignments> response) {
                if (response.isSuccessful()) {

                    TodayAssignments todayAssignments = response.body();
                    Databackbone.getinstance().todayassignments = todayAssignments;
                    Databackbone.getinstance().todayassignmentdata = todayAssignments.getData();
                    Databackbone.getinstance().todayAssignmentactive = todayAssignments.getActiveAssignments();

                    if (ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED &&
                            ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
                            ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                        ActivityCompat.requestPermissions(Splash.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    else {
                        Intent i = new Intent(Splash.this, activity_mapview.class);
                        startActivity(i);
                        finish();
                    }

                } else {
                    if (response.code() == 401) {
                        //sharedpreferences must be removed
                        mEditor.clear().commit();
                        mEditor_parcels.clear().commit();
                        Intent intent = new Intent(Splash.this, activity_login.class);
                        startActivity(intent);
                        finishAffinity();
                    }else {
                        Databackbone.getinstance().showAlsertBox(Splash.this, getResources().getString(R.string.error), "Error Connecting To Server Error Code 33");
                    }
                    //DeactivateRider();
                }

            }

            @Override
            public void onFailure(Call<TodayAssignments> call, Throwable t) {
                System.out.println(t.getCause());
                Databackbone.getinstance().showAlsertBox(Splash.this, getResources().getString(R.string.error), "Error Connecting To Server Error Code 34");

                //DeactivateRider();
            }
        });
    }


}
