package io.devbeans.swyft;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.navigation.ui.AppBarConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.devbeans.swyft.interface_retrofit.PickupParcel;
import io.devbeans.swyft.interface_retrofit.*;
import io.swyft.swyft.R;
import io.swyft.swyft.Splash;
import mumayank.com.airlocationlibrary.AirLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import io.devbeans.swyft.interface_retrofit_delivery.*;


public class activity_mapview extends Activity implements OnMapReadyCallback {
    GoogleMap mMapServiceView;
    MapView mMapView;
    ImageView img_rider_activity_button, btn_navigation;

    ConstraintLayout offlineTag = null;
    ConstraintLayout Task1, Task2, Task3, Task4, Task5, Task6 = null;
    ConstraintLayout btn_wallet, btn_earning = null;
    TextView tx_username, tx_rating = null;
    ProgressBar progressBar = null;

    ImageView btn_slider_menu;
    private AppBarConfiguration mAppBarConfiguration;
    NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    ConstraintLayout pendingTask;
    private AirLocation airLocation;
    ImageView btn_get_current_locationc, profile_image2;
    TextView tx_parcels_status_count, tx_earning_slider, tx_wallet_slider;
    Marker marker_destination_location = null;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "MyPrefs";

    ArrayList<MarkerOptions> markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_drawer);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        mEditor = sharedpreferences.edit();

        mMapView = findViewById(R.id.ridermapView);
        offlineTag = findViewById(R.id.img_rider_activity_button_State);
        img_rider_activity_button = findViewById(R.id.img_rider_activity_button);
        navigationView = findViewById(R.id.nav_view);
        pendingTask = findViewById(R.id.pendingTask);
        btn_get_current_locationc = findViewById(R.id.btn_get_current_locationc);
        profile_image2 = findViewById(R.id.profile_image2);
        Task1 = findViewById(R.id.item1);
        Task2 = findViewById(R.id.item2);
        Task3 = findViewById(R.id.item3);
        Task4 = findViewById(R.id.item4);
        Task5 = findViewById(R.id.item5);
        Task6 = findViewById(R.id.item6);
        btn_navigation = findViewById(R.id.btn_navigation);
        tx_username = findViewById(R.id.tx_username_slider);
        progressBar = (ProgressBar) findViewById(R.id.url_loading_animation);
        btn_wallet = findViewById(R.id.btn_wallet);
        btn_earning = findViewById(R.id.btn_earning);
        tx_rating = findViewById(R.id.tx_rating);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.slider_menu);
        btn_slider_menu = findViewById(R.id.btn_slider_menu);
        tx_parcels_status_count = findViewById(R.id.tx_parcels_status_count);
        progressBar.setVisibility(View.GONE);
        tx_earning_slider = findViewById(R.id.tx_earning_slider);
        tx_wallet_slider = findViewById(R.id.tx_wallet_slider);
        // generate_test_Data_for_daily();
        startService(new Intent(this, maneger_location.class));
        if (Databackbone.getinstance().ar_orders_daily.size() > 0) {
            // check_parcel_scanning_complete = true;
            //tx_parcels_status_count.setText(Integer.toString(Databackbone.getinstance().ar_orders_daily.size()) + " Pickups Remaining");
        } else {
            //check_parcel_scanning_complete = false;
            // tx_parcels_status_count.setText(Integer.toString(0) + " Scanning Parcels");

        }

//        mDrawerLayout.closeDrawer(Gravity.LEFT);
        navigationView.bringToFront();

        Task1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity_order_status = new Intent(activity_mapview.this, activity_order_status.class);
                activity_mapview.this.startActivity(activity_order_status);
            }
        });
        Task2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openaactivity = new Intent(activity_mapview.this, activity_help.class);
                //activity_mapview.this.startActivity(openaactivity);
            }
        });
        Task3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openaactivity = new Intent(activity_mapview.this, activity_faq.class);
                //activity_mapview.this.startActivity(openaactivity);
            }
        });
        Task4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openaactivity = new Intent(activity_mapview.this, activity_settings.class);
                activity_mapview.this.startActivity(openaactivity);
            }
        });
        Task5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Databackbone.resetStaticPoint();
                activity_mapview.this.finish();
            }
        });

        Task6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity_mapview.this, "Sync it", Toast.LENGTH_SHORT).show();
            }
        });
        btn_get_current_locationc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });
        btn_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent openaactivity = new Intent(activity_mapview.this,activity_wallet_orders.class);
                //  activity_mapview.this.startActivity(openaactivity);
            }
        });

        btn_earning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openaactivity = new Intent(activity_mapview.this, activity_earning.class);
                activity_mapview.this.startActivity(openaactivity);
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
                EnableLoading();
                String attandanceID = "";
                if (Databackbone.getinstance().riderdetails != null)
                    attandanceID = Databackbone.getinstance().riderdetails.getAttendanceId();
                if (Databackbone.getinstance().riderdetails.getIsOnline()) {
//                    change_Activity_status(attandanceID, true);
                }
                else {
//                    change_Activity_status(attandanceID, true);
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
                Intent openBarCode = new Intent(activity_mapview.this, activity_profile.class);
                activity_mapview.this.startActivity(openBarCode);
                //overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );


            }
        });
        pendingTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Databackbone.getinstance().riderdetails.getIsOnline()) {
                    Intent pendingtask = null;
                    if (Databackbone.getinstance().check_parcel_scanning_complete) {
                        pendingtask = new Intent(activity_mapview.this, DailyTasksActivity.class);
                        pendingtask.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingtask.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        activity_mapview.this.startActivity(pendingtask);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    } else {
                        pendingtask = new Intent(activity_mapview.this, activity_barcode_scanner.class);
                        pendingtask.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pendingtask.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        activity_mapview.this.startActivity(pendingtask);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    }
                } else {
                    Databackbone.getinstance().showAlsertBox(activity_mapview.this, getResources().getString(R.string.error), getResources().getString(R.string.you_are_not_online));
                }
            }
        });

        btn_navigation.setVisibility(View.GONE);

        // data attributes set from server
        tx_username.setText("Hi " + Databackbone.getinstance().riderdetails.getFirstName());
        Picasso.with(this).load(Databackbone.getinstance().riderdetails.getProfilePicture()).into(profile_image2);
        tx_rating.setText(Databackbone.getinstance().riderdetails.getType());
        btn_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (marker_destination_location != null) {
                    new AlertDialog.Builder(activity_mapview.this)
                            .setTitle(getResources().getString(R.string.navigation_request))
                            .setMessage(getResources().getString(R.string.activate_navigation_for) + " " + marker_destination_location.getTitle())

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Offlice_Activity(marker_destination_location.getPosition());

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
        check_status_of_rider_activity();


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

    public void getCurrentLocation() {
        airLocation = new AirLocation(this, true, true, new AirLocation.Callbacks() {
            @Override
            public void onSuccess(Location location) {
                if (location == null || mMapServiceView == null)
                    return;
                LatLng current_location = new LatLng(location.getLatitude(), location.getLongitude());
                Databackbone.getinstance().current_location = current_location;
                Databackbone.getinstance().CalculateLocationFromPickupParcels(Databackbone.getinstance().parcels);

                CameraUpdate location_animation = CameraUpdateFactory.newLatLngZoom(current_location, 15);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(current_location)        // Sets the center of the map to Mountain View
                        .zoom(17)              // Sets the zoom
                        .bearing(90)           // Sets the orientation of the camera to east
                        .tilt(0)               // Sets the tilt of the camera to 30 degrees
                        .build();
                mMapServiceView.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }

            @Override
            public void onFailed(AirLocation.LocationFailedEnum locationFailedEnum) {
                // do something
                String message = locationFailedEnum.name();
                System.out.println(message);
            }
        });

    }

    public void ActivateRider() {
        img_rider_activity_button.setImageResource(R.drawable.icon_rider_event_start);
        offlineTag.setVisibility(View.GONE);
    }

    public void DeactivateRider() {
        img_rider_activity_button.setImageResource(R.drawable.icon_rider_event_stop);
        offlineTag.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapServiceView = googleMap;
        mMapServiceView.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTitle().contains("Pickup")) {
                    //btn_navigation.setVisibility(View.VISIBLE);
                    marker_destination_location = marker;
                }
                return false;
            }
        });
        mMapServiceView.setMyLocationEnabled(true);
        mMapServiceView.getUiSettings().setMyLocationButtonEnabled(false);
        getCurrentLocation();
        LoadResume();

        /*
        if(Databackbone.getinstance().riderdetails.getType().equalsIgnoreCase("delivery"))
            LoadParcelsForDelivery();
        else
            LoadParcels();

         */


    }

    @Override
    public void onResume() {

        mMapView.onResume();
        getCurrentLocation();
        super.onResume();
        LoadResume();
        getwallet();
        getEarnings();


    }

    public void LoadResume() {
        if (Databackbone.getinstance().riderdetails != null && Databackbone.getinstance().riderdetails.getIsOnline()) {
            ActivateRider();

        } else {
            DeactivateRider();
        }
        if (Databackbone.getinstance().riderdetails.getType().equalsIgnoreCase("delivery")) {
            Databackbone.getinstance().RiderTypeDelivery = true;
            markers.clear();
            LoadParcelsForDelivery();
        } else {
            Databackbone.getinstance().RiderTypeDelivery = false;
            markers.clear();
            getTodayAssignments();
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
                    LoadLocation(Databackbone.getinstance().todayassignmentdata);
                    //Databackbone.getinstance().parcels = parcels;
                    tx_parcels_status_count.setText(String.valueOf(todayAssignments.getData().size() + todayAssignments.getActiveAssignments().size()) + " " + getResources().getString(R.string.task_pending));
                    DisableLoading();


                } else {
                    DisableLoading();
                    if (response.code() == 401) {
                        //sharedpreferences must be removed
                        mEditor.clear().commit();
                        Intent intent = new Intent(activity_mapview.this, activity_login.class);
                        startActivity(intent);
                        finishAffinity();
                    }else {
                        Databackbone.getinstance().showAlsertBox(activity_mapview.this, getResources().getString(R.string.error), "Error Connecting To Server Error Code 33");
                    }
                    //DeactivateRider();
                }

            }

            @Override
            public void onFailure(Call<TodayAssignments> call, Throwable t) {
                System.out.println(t.getCause());
                Databackbone.getinstance().showAlsertBox(activity_mapview.this, getResources().getString(R.string.error), "Error Connecting To Server Error Code 34");

                //DeactivateRider();
            }
        });
    }

    /*public void LoadParcels() {
        Retrofit retrofit = Databackbone.getinstance().getRetrofitbuilder();
        swift_api riderapi = retrofit.create(swift_api.class);
        EnableLoading();
        Call<List<PickupParcel>> call = riderapi.getParcelsByRiders(sharedpreferences.getString("AccessToken", ""), sharedpreferences.getString("RiderID", ""));
        call.enqueue(new Callback<List<PickupParcel>>() {
            @Override
            public void onResponse(Call<List<PickupParcel>> call, Response<List<PickupParcel>> response) {
                if (response.isSuccessful()) {

                    List<PickupParcel> parcels = response.body();
                    parcels = Databackbone.getinstance().resortParcelsPickup(parcels);

                    // System.out.println(parcels.size());
                    LoadLocation(parcels);
                    Databackbone.getinstance().parcels = parcels;
                    //Databackbone.getinstance().parcels = parcels;
                    tx_parcels_status_count.setText(Integer.toString(parcels.size()) + " " + getResources().getString(R.string.task_pending));
                    DisableLoading();

                } else {
                    DisableLoading();
                }

            }

            @Override
            public void onFailure(Call<List<PickupParcel>> call, Throwable t) {
                System.out.println(t.getCause());
                tx_parcels_status_count.setText(getResources().getString(R.string.zero_task_pending));
                DisableLoading();
            }
        });


    }*/

    public void check_status_of_rider_activity() {
        if (Databackbone.getinstance().riderdetails != null) {
            if (Databackbone.getinstance().riderdetails.getIsOnline()) {
                ActivateRider();
//                change_Activity_status("", false);
            } else {

                DeactivateRider();
            }
        }
        //change_Activity_status("",false);
    }

    public void change_Activity_status(String id, final Boolean check) {
        Retrofit retrofit = Databackbone.getinstance().getRetrofitbuilder();
        swift_api riderapi = retrofit.create(swift_api.class);

        Call<RiderDetails> call = riderapi.markattendance(sharedpreferences.getString("AccessToken", ""), new markattendance(sharedpreferences.getString("RiderID", ""), id));
        call.enqueue(new Callback<RiderDetails>() {
            @Override
            public void onResponse(Call<RiderDetails> call, Response<RiderDetails> response) {
                if (response.isSuccessful()) {

                    RiderDetails riderActivity = response.body();
                    Databackbone.getinstance().riderdetails = riderActivity;
                    if (Databackbone.getinstance().riderdetails.getIsOnline()) {
                        ActivateRider();
                        Databackbone.getinstance().riderdetails.setIsOnline(true);
                        if (check)
                            Databackbone.getinstance().showAlsertBox(activity_mapview.this, getResources().getString(R.string.rider_app), getResources().getString(R.string.online));

                    } else {

                        Databackbone.getinstance().riderdetails.setIsOnline(Databackbone.getinstance().riderdetails.getIsOnline());
                        DeactivateRider();
                        if (check)
                            Databackbone.getinstance().showAlsertBox(activity_mapview.this, getResources().getString(R.string.rider_app), getResources().getString(R.string.offline));

                    }
                    DisableLoading();

                } else {
                    if (response.code() == 401) {
                        DisableLoading();
                        //sharedpreferences must be removed
                        mEditor.clear().commit();
                        Intent intent = new Intent(activity_mapview.this, activity_login.class);
                        startActivity(intent);
                        finishAffinity();
                    }else {
                        DisableLoading();
                        //DeactivateRider();
                        Databackbone.getinstance().showAlsertBox(activity_mapview.this, getResources().getString(R.string.error), "Error Connecting To Server Error Code 33");
                    }

                }

            }

            @Override
            public void onFailure(Call<RiderDetails> call, Throwable t) {
                System.out.println(t.getCause());
                DisableLoading();
                Databackbone.getinstance().showAlsertBox(activity_mapview.this, getResources().getString(R.string.error), "Error Connecting To Server Error Code 34");

                //DeactivateRider();
            }
        });


    }

    public void AddMarkers(Double lat, Double lng, final String title, int marker_image) {

        MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lng)).title("Pickup : " + title);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(marker_image);
        mMapServiceView.clear();
        marker.icon(icon);
        markers.add(marker);
        mMapServiceView.addMarker(marker);
    }

    public void Offlice_Activity(LatLng location) {
        String location_to_string = Double.toString(location.latitude) + "," + Double.toString(location.longitude);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(getResources().getString(R.string.google_map_link_address) + location_to_string));
        startActivity(intent);
    }

    public void DisableLoading() {
        img_rider_activity_button.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }

    public void EnableLoading() {
        img_rider_activity_button.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void LoadLocation(List<TodayAssignmentData> parcels) {
        markers.clear();
        btn_navigation.setVisibility(View.GONE);
        for (int i = 0; i < parcels.size(); i++) {
            for (int j = 0; j < parcels.get(i).getPickupLocations().size(); j++){
                AddMarkers(parcels.get(i).getPickupLocations().get(j).getGeoPoints().getLat(), parcels.get(i).getPickupLocations().get(j).getGeoPoints().getLng(), parcels.get(i).getVendorName(), R.drawable.icon_pickup);
            }
        }

        LoadAllMarkers();


    }

    public void LoadLocationForActiveParcels(List<RiderActivityDelivery> parcels) {
        markers.clear();
        btn_navigation.setVisibility(View.GONE);

        for (int k = 0; k < parcels.size(); k++) {
            if (parcels.get(k).getTaskStatus().equals("started"))
                for (int i = 0; i < parcels.get(k).getData().size(); i++) {
                    Datum data = parcels.get(k).getData().get(i);
                    AddMarkers(data.getLocation().getGeoPoints().getLat(), data.getLocation().getGeoPoints().getLng(), data.getName(), R.drawable.icon_delivery);
                    LoadAllMarkers();
                }
        }




    }

    public void LoadAllMarkers() {
        if (Databackbone.getinstance().current_location == null) {
            //Databackbone.getinstance().showAlsertBox(activity_mapview.this, "Error", "Activate your GPS");
            return;
        }
        MarkerOptions currentmarker = new MarkerOptions().position(new LatLng(Databackbone.getinstance().current_location.latitude, Databackbone.getinstance().current_location.longitude));
        markers.add(currentmarker);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (MarkerOptions marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 100; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMapServiceView.moveCamera(cu);
        mMapServiceView.animateCamera(cu);

    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void LoadParcelsForDelivery() {
        Retrofit retrofit = Databackbone.getinstance().getRetrofitbuilder();
        swift_api_delivery riderapidata = retrofit.create(swift_api_delivery.class);
        EnableLoading();
        Call<List<RiderActivityDelivery>> call = riderapidata.manageTaskfordelivery(sharedpreferences.getString("AccessToken", ""), sharedpreferences.getString("RiderID", ""));
        call.enqueue(new Callback<List<RiderActivityDelivery>>() {
            @Override
            public void onResponse(Call<List<RiderActivityDelivery>> call, Response<List<RiderActivityDelivery>> response) {
                if (response.isSuccessful()) {

                    List<RiderActivityDelivery> parcels = response.body();

                    // System.out.println(parcels.size());
                    if (parcels == null) {
                        tx_parcels_status_count.setText("0 Task Pending");
                    } else {
                        parcels = Databackbone.getinstance().resortDelivery(parcels);

                        LoadLocationForActiveParcels(parcels);
                        Databackbone.getinstance().parcelsdelivery = parcels;
                        Databackbone.getinstance().remove_location_complete();
                        tx_parcels_status_count.setText(Integer.toString(parcels.size()) + " " + getResources().getString(R.string.task_pending));
                    }
                    DisableLoading();

                } else {
                    if (response.code() == 401) {
                        DisableLoading();
                        //sharedpreferences must be removed
                        mEditor.clear().commit();
                        Intent intent = new Intent(activity_mapview.this, activity_login.class);
                        startActivity(intent);
                        finishAffinity();
                    }else {
                        DisableLoading();
                    }

                }

            }

            @Override
            public void onFailure(Call<List<RiderActivityDelivery>> call, Throwable t) {
                System.out.println(t.getCause());
                tx_parcels_status_count.setText(getResources().getString(R.string.zero_task_pending));
                DisableLoading();
            }
        });


    }

    public void getwallet() {
        Retrofit retrofit = Databackbone.getinstance().getRetrofitbuilder();
        swift_api_delivery riderapidata = retrofit.create(swift_api_delivery.class);

        Call<delivery_wallet> call = riderapidata.deliverywallet(sharedpreferences.getString("AccessToken", ""), sharedpreferences.getString("RiderID", ""));
        call.enqueue(new Callback<delivery_wallet>() {
            @Override
            public void onResponse(Call<delivery_wallet> call, Response<delivery_wallet> response) {
                if (response.isSuccessful()) {

                    delivery_wallet wallet = response.body();
                    Databackbone.getinstance().wallet = wallet;


                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            tx_wallet_slider.setText(Float.toString(wallet.getamount()) + " Pkr");
                        }
                    });
                    //  DisableLoading();
                    // load_Data();
                    // update_view();
                } else {
                    //DisableLoading();
                }

            }

            @Override
            public void onFailure(Call<delivery_wallet> call, Throwable t) {
                System.out.println(t.getCause());

                //DisableLoading();
                // load_Data();
            }
        });
    }

    public void getEarnings() {
        Retrofit retrofit = Databackbone.getinstance().getRetrofitbuilder();
        swift_api_delivery riderapidata = retrofit.create(swift_api_delivery.class);

        Call<delivery_earnings> call = riderapidata.deliveryEarning(sharedpreferences.getString("AccessToken", ""), sharedpreferences.getString("RiderID", ""));
        call.enqueue(new Callback<delivery_earnings>() {
            @Override
            public void onResponse(Call<delivery_earnings> call, Response<delivery_earnings> response) {
                if (response.isSuccessful()) {

                    delivery_earnings dailyearning = response.body();
                    Databackbone.getinstance().delivery_driver_earning = dailyearning;

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            tx_earning_slider.setText(Float.toString(dailyearning.getDaily().getEarnings()) + " Pkr");
                        }
                    });
                    //  DisableLoading();
                    // load_Data();
                    // update_view();
                } else {
                    //DisableLoading();
                }

            }

            @Override
            public void onFailure(Call<delivery_earnings> call, Throwable t) {
                System.out.println(t.getCause());

                //DisableLoading();
                // load_Data();
            }
        });
    }
}
