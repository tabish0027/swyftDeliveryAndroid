package io.swyft.swyft;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import io.devbeans.swyft.Databackbone;
import io.devbeans.swyft.activity_mapview;
import io.devbeans.swyft.interface_retrofit.RiderDetails;
import io.devbeans.swyft.interface_retrofit.swift_api;
import io.devbeans.swyft.network.ApiController;
import io.swyft.swyft.R;
import io.devbeans.swyft.activity_login;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Splash extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        mEditor = sharedpreferences.edit();


        Databackbone.getinstance().contextapp = getApplicationContext();
        Context con = Databackbone.getinstance().contextapp;
        Thread thr= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);

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
//                    ApiController.getInstance().getEarnings();
//                    ApiController.getInstance().getwallet();
//                    ApiController.getInstance().gethistory();
                    if (ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
                        ActivityCompat.requestPermissions(Splash.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    else {
                        Intent i = new Intent(Splash.this, activity_mapview.class);
                        startActivity(i);
                        finish();
                    }

                } else {
                    if (response.code() == 401) {
                        //sharedpreferences must be removed
                        mEditor.clear().commit();
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


}
