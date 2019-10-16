package io.faceart.swift;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import mumayank.com.airlocationlibrary.AirLocation;

public class activity_mapview extends Activity implements OnMapReadyCallback {
    GoogleMap mMapServiceView;
    MapView mMapView;
    ImageView img_rider_activity_button;
    boolean img_rider_activity_button_State = false;
    ConstraintLayout offlineTag = null;
    ImageView btn_slider_menu;
    private AppBarConfiguration mAppBarConfiguration;
    NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    ConstraintLayout pendingTask;
    private AirLocation airLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_drawer);
        mMapView =  findViewById(R.id.ridermapView);
        offlineTag = findViewById(R.id.img_rider_activity_button_State);
        img_rider_activity_button = findViewById(R.id.img_rider_activity_button);
        navigationView = findViewById(R.id.nav_view);
        pendingTask = findViewById(R.id.pendingTask);
        //mMapView.onResume();
        btn_slider_menu = findViewById(R.id.btn_slider_menu);
        try {
            MapsInitializer.initialize(this.getApplicationContext());
            mMapView.onCreate(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
        img_rider_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(img_rider_activity_button_State)
                {
                    img_rider_activity_button_State = false;
                    DeactivateRider();

                }else{
                    img_rider_activity_button_State = true;
                    ActivateRider();
                }
            }
        });
        mDrawerLayout = (DrawerLayout) findViewById(R.id.slider_menu);
        mAppBarConfiguration = new AppBarConfiguration.Builder()
                .setDrawerLayout(mDrawerLayout)
                .build();

        btn_slider_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        pendingTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openBarCode = new Intent(activity_mapview.this,activity_barcode_scanner.class);
                activity_mapview.this.startActivity(openBarCode);
                //overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );

            }
        });

       airLocation = new AirLocation(this, true, true, new AirLocation.Callbacks() {
            @Override
            public void onSuccess( Location location) {
                LatLng current_location = new LatLng(location.getLongitude(), location.getLatitude());
              //  mMapServiceView.addMarker(new MarkerOptions().position(current_location).title("Current Location"));
                mMapServiceView.moveCamera(CameraUpdateFactory.newLatLng(current_location));
            }

            @Override
            public void onFailed( AirLocation.LocationFailedEnum locationFailedEnum) {
                // do something
            }
        });

    }
    // override and call airLocation object's method by the same name
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        airLocation.onActivityResult(requestCode, resultCode, data);
    }

    // override and call airLocation object's method by the same name
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    public void ActivateRider(){
        img_rider_activity_button.setImageResource(R.drawable.icon_rider_event_start);
        offlineTag.setVisibility(View.GONE);
    }

    public void DeactivateRider(){
        img_rider_activity_button.setImageResource(R.drawable.icon_rider_event_stop);
        offlineTag.setVisibility(View.VISIBLE);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapServiceView = googleMap;


    }
    @Override
    public void onResume() {

       mMapView.onResume();
        super.onResume();
        if(img_rider_activity_button_State)
        {
            ActivateRider();

        }else{
            DeactivateRider();
        }


    }

    @Override
    public void onPause() {
        super.onPause();
       mMapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
       mMapView.onLowMemory();
    }
}
