package io.faceart.swift;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.ui.AppBarConfiguration;

import java.util.ArrayList;

import io.faceart.swift.data_models.model_daily_package_item;
import mumayank.com.airlocationlibrary.AirLocation;

public class activity_mapview extends Activity implements OnMapReadyCallback {
    GoogleMap mMapServiceView;
    MapView mMapView;
    ImageView img_rider_activity_button;
    boolean img_rider_activity_button_State = false;
    ConstraintLayout offlineTag = null;
    ConstraintLayout Task1,Task2,Task3,Task4,Task5 = null;
    ConstraintLayout btn_walled,btn_earning = null;

    Boolean check_parcel_scanning_complete = false;
    ImageView btn_slider_menu;
    private AppBarConfiguration mAppBarConfiguration;
    NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    ConstraintLayout pendingTask;
    private AirLocation airLocation;
    ImageView btn_get_current_locationc,profile_image2;
    TextView tx_parcels_status_count;

    //private BottomSheetBehavior sheetBehavior;
    //private ConstraintLayout bottom_sheet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_drawer);
        mMapView =  findViewById(R.id.ridermapView);
        offlineTag = findViewById(R.id.img_rider_activity_button_State);
        img_rider_activity_button = findViewById(R.id.img_rider_activity_button);
        navigationView = findViewById(R.id.nav_view);
        pendingTask = findViewById(R.id.pendingTask);
        btn_get_current_locationc = findViewById(R.id.btn_get_current_locationc);
        profile_image2 = findViewById(R.id.profile_image2);
        Task1= findViewById(R.id.item1);
        Task2= findViewById(R.id.item2);
        Task3= findViewById(R.id.item3);
        Task4= findViewById(R.id.item4);
        Task5= findViewById(R.id.item5);

        btn_walled= findViewById(R.id.btn_wallet);
        btn_earning= findViewById(R.id.btn_earning);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.slider_menu);
        btn_slider_menu = findViewById(R.id.btn_slider_menu);
        tx_parcels_status_count = findViewById(R.id.tx_parcels_status_count);


        generate_test_Data_for_daily();

        if(Databackbone.getinstance().ar_orders_daily.size() > 0)
        {
            check_parcel_scanning_complete = true;
            tx_parcels_status_count.setText(Integer.toString(Databackbone.getinstance().ar_orders_daily.size()) + " Pickups Remaining");
        }
        else
        {
            check_parcel_scanning_complete = false;
            tx_parcels_status_count.setText(Integer.toString(0) + " Scanning Parcels");

        }
        Task1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Task2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openBarCode = new Intent(activity_mapview.this,activity_help.class);
                activity_mapview.this.startActivity(openBarCode);
            }
        });
        Task3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openBarCode = new Intent(activity_mapview.this,activity_faq.class);
                activity_mapview.this.startActivity(openBarCode);
            }
        });
        Task4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openBarCode = new Intent(activity_mapview.this, activity_settings.class);
                activity_mapview.this.startActivity(openBarCode);
            }
        });
        Task5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_mapview.this.finish();
            }
        });
        btn_get_current_locationc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });
        btn_walled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openBarCode = new Intent(activity_mapview.this,activity_wallet_orders.class);
                activity_mapview.this.startActivity(openBarCode);
            }
        });

        btn_earning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openBarCode = new Intent(activity_mapview.this,activity_earning.class);
                activity_mapview.this.startActivity(openBarCode);
            }
        });
        //mMapView.onResume();

        try {
            MapsInitializer.initialize(this.getApplicationContext());
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);
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


        mAppBarConfiguration = new AppBarConfiguration.Builder()
                .setDrawerLayout(mDrawerLayout)
                .build();

        btn_slider_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawerLayout.openDrawer(Gravity.LEFT);
                navigationView.bringToFront();

            }
        });
        profile_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openBarCode = new Intent(activity_mapview.this,activity_profile.class);
                activity_mapview.this.startActivity(openBarCode);
                //overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );


            }
        });
        pendingTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openBarCode = null;
                if(check_parcel_scanning_complete){
                    openBarCode = new Intent(activity_mapview.this, activity_daily_order_status.class);
                    activity_mapview.this.startActivity(openBarCode);
                    overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                }
                else{
                     openBarCode = new Intent(activity_mapview.this, activity_barcode_scanner.class);
                    activity_mapview.this.startActivity(openBarCode);
                    overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                }


            }
        });

        /*
        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        // click event for show-dismiss bottom sheet
        btn_get_current_locationc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    //btn_bottom_sheet.setText("Close sheet");
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    //btn_bottom_sheet.setText("Expand sheet");
                }
            }
        });
// callback for do something
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //btn_bottom_sheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                       // btn_bottom_sheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        */

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

    public void getCurrentLocation(){
        airLocation = new AirLocation(this, true, true, new AirLocation.Callbacks() {
            @Override
            public void onSuccess( Location location) {
                if(location == null || mMapServiceView == null)
                    return ;
                LatLng current_location = new LatLng(location.getLatitude(),location.getLongitude());
                mMapServiceView.addMarker(new MarkerOptions().position(current_location).title("Current Location"));
                //mMapServiceView.moveCamera(CameraUpdateFactory.newLatLngZoom(current_location, 15));
                CameraUpdate location_animation = CameraUpdateFactory.newLatLngZoom(current_location, 15);
                mMapServiceView.animateCamera(location_animation);
                //mMapServiceView.moveCamera(CameraUpdateFactory.newLatLng(current_location));
            }

            @Override
            public void onFailed( AirLocation.LocationFailedEnum locationFailedEnum) {
                // do something
                String message = locationFailedEnum.name();
                System.out.println(message);
            }
        });
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
        getCurrentLocation();

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
        mDrawerLayout.closeDrawer(Gravity.LEFT);
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




    ////////////////////////////////////////////////////////////////////////////////////////////
    public void generate_test_Data_for_daily() {
        Databackbone.getinstance().ar_orders_daily.clear();

        ArrayList<model_daily_package_item> temp_ar_orders_daily = new ArrayList<>();
        temp_ar_orders_daily.add(new model_daily_package_item("4384745", "Amir " + Integer.toString(0), "G 47 DHA lahore", "17."+Integer.toString(0)+" KM","DHA Phase 1",true));

        for (int i = 1; i < 10; i++) {
            temp_ar_orders_daily.add(new model_daily_package_item("4384745", "Amir " + Integer.toString(i), "G 47 DHA lahore", "17."+Integer.toString(i)+" KM","DHA Phase 1",false));
        }
        Databackbone.getinstance().ar_orders_daily.addAll(temp_ar_orders_daily);



    }
}
