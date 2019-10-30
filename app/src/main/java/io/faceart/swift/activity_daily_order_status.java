package io.faceart.swift;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.faceart.swift.adapters.*;
import io.faceart.swift.data_models.*;
import io.faceart.swift.interface_retrofit.DeliveryParcel;
import io.faceart.swift.interface_retrofit.Location;
import io.faceart.swift.interface_retrofit.RiderActivity;
import io.faceart.swift.interface_retrofit.manage_task;
import io.faceart.swift.interface_retrofit.online;
import io.faceart.swift.interface_retrofit.swift_api;
import io.faceart.swift.interface_retrofit_delivery.RiderActivityDelivery;
import io.faceart.swift.interface_retrofit_delivery.swift_api_delivery;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_daily_order_status  extends Activity {


    public RecyclerView order_list_daily;
    public adapter_status_daily_packages ad_orders_daily;
    public SwipeRefreshLayout swipeToRefresh;
    ProgressBar progressBar = null;
    ConstraintLayout pendingTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_order_status);
        final ImageView btn_back = findViewById(R.id.btn_back);
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        progressBar = (ProgressBar)findViewById(R.id.url_loading_animation);
        progressBar.setVisibility(View.GONE);
        pendingTask = findViewById(R.id.pendingTask);



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_daily_order_status.this.finish();
            }
        });
        order_list_daily = findViewById(R.id.order_list_daily);




        //generate_test_Data();
        ad_orders_daily = new adapter_status_daily_packages(Databackbone.getinstance().ar_orders_daily, this);


        order_list_daily.setAdapter(ad_orders_daily);

        ad_orders_daily.setOnItemClickListener(new adapter_status_daily_packages.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if(Databackbone.getinstance().rider.getUser().getType().equalsIgnoreCase("delivery"))
                    StartDeliveryTask(position);
                else
                    startPicupTask(position);


            }

            @Override
            public void onItemLongClick(int position, View v) {

                Toast.makeText(activity_daily_order_status.this,"onItemLongClick position: " + position,Toast.LENGTH_LONG).show();
            }
        });

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Databackbone.getinstance().rider.getUser().getType().equalsIgnoreCase("delivery"))
                    LoadParcelsForDelivery();
                else


                LoadParcels();
            }
        });
        if(!Databackbone.getinstance().rider.getUser().getType().equalsIgnoreCase("delivery"))
        {
            pendingTask.setVisibility(View.GONE);
        }
        pendingTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pendingtask = new Intent(activity_daily_order_status.this, activity_barcode_scanner.class);
                pendingtask.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pendingtask.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity_daily_order_status.this.startActivity(pendingtask);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        load_Data();
        update_view();
    }

    public void startPicupTask(int position){
//Toast.makeText(activity_daily_order_status.this,"onItemClick position: " + position,Toast.LENGTH_LONG).show();
        if(Databackbone.getinstance().ar_orders_daily.get(position).m_remaining_parcels_to_scan ==0){
            completeTaskConfirmation(Databackbone.getinstance().parcels.get(position).getName(),Databackbone.getinstance().parcels.get(position).getTaskId());

        }
        else if(Databackbone.getinstance().ar_orders_daily.get(position).status)
        {
            Databackbone.getinstance().pickup_to_process = position;
            Intent barcode_scanner = new Intent(activity_daily_order_status.this,activity_barcode_scanner.class);
            barcode_scanner.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            barcode_scanner.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            activity_daily_order_status.this.startActivity(barcode_scanner);

        }

        else{
            if(check_is_any_pickup_task_active()){
                Databackbone.getinstance().showAlsertBox(activity_daily_order_status.this,"Error","The is already an active task");
                return ;

            }
            startTaskConfirmation(Databackbone.getinstance().parcels.get(position).getName(),Databackbone.getinstance().parcels.get(position).getTaskId());
            //Databackbone.getinstance().showAlsertBox(activity_daily_order_status.this,"Error","Task Not Active");

        }
    }
    public void StartDeliveryTask(int position){

        if(Databackbone.getinstance().ar_orders_daily.get(position).status)
        {
            Databackbone.getinstance().delivery_to_show = position;
            Intent barcode_scanner = new Intent(activity_daily_order_status.this,activity_form.class);
            barcode_scanner.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            barcode_scanner.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            activity_daily_order_status.this.startActivity(barcode_scanner);

        }
        else if(Databackbone.getinstance().ar_orders_daily.get(position).m_remaining_parcels_to_scan ==0 &&!Databackbone.getinstance().ar_orders_daily.get(position).status){
            if(check_is_any_delivery_task_active()){
                Databackbone.getinstance().showAlsertBox(activity_daily_order_status.this,"Error","The is already an active task");
                return ;

            }
            else {
                startTaskConfirmation(Databackbone.getinstance().parcelsdelivery.get(position).getData().get(0).getName(), Databackbone.getinstance().parcelsdelivery.get(position).getTaskId());
            }
        }
        /*


        else{
           // completeTaskConfirmation(Databackbone.getinstance().parcelsdelivery.get(position).getData().get(0).getName(),Databackbone.getinstance().parcelsdelivery.get(position).getTaskId());

            //Databackbone.getinstance().showAlsertBox(activity_daily_order_status.this,"Error","Task Not Active");
        }*/
    }

    public void load_Data() {

        if(!Databackbone.getinstance().rider.getUser().getType().equalsIgnoreCase("delivery")) {
            Databackbone.getinstance().ar_orders_daily.clear();

            ArrayList<model_daily_package_item> temp_ar_orders_daily = new ArrayList<>();
            if (Databackbone.getinstance().parcels == null)
                return;
            Boolean activated_task = false;
            for (int i = 0; i < Databackbone.getinstance().parcels.size(); i++) {
                LatLng m_location = new LatLng(Databackbone.getinstance().parcels.get(i).getLocation().getGeoPoints().getLat(), Databackbone.getinstance().parcels.get(i).getLocation().getGeoPoints().getLng());

                if (!Databackbone.getinstance().parcels.get(i).getTaskStatus().equalsIgnoreCase("pending"))
                    activated_task = true;
                int size = 0;
                int total_parcel = Databackbone.getinstance().parcels.get(i).getParcels().size();
                for (int parcelscancount = 0; parcelscancount < total_parcel; parcelscancount++) {
                    if (!Databackbone.getinstance().parcels.get(i).getParcels().get(parcelscancount).getScanned())
                        size++;
                }
                double distance = CalculationByDistance(Databackbone.getinstance().parcels.get(i).getLocation().getGeoPoints().getLat(), Databackbone.getinstance().parcels.get(i).getLocation().getGeoPoints().getLng());
                model_daily_package_item dataModelValue = new model_daily_package_item(Databackbone.getinstance().parcels.get(i).getTaskId(), Databackbone.getinstance().parcels.get(i).getName(), Databackbone.getinstance().parcels.get(i).getLocation().getAddress(), Double.toString(distance) + "KM", Databackbone.getinstance().parcels.get(i).getLocation().getAddress(), activated_task, m_location, size);
                temp_ar_orders_daily.add(dataModelValue);
                activated_task = false;
            }
            Databackbone.getinstance().ar_orders_daily.addAll(temp_ar_orders_daily);
        }else{
            Databackbone.getinstance().ar_orders_daily.clear();

            ArrayList<model_daily_package_item> temp_ar_orders_daily = new ArrayList<>();
            if (Databackbone.getinstance().parcelsdelivery == null)
                return;
            Boolean activated_task = false;
            for (int i = 0; i < Databackbone.getinstance().parcelsdelivery.size(); i++) {
                LatLng m_location = new LatLng(Databackbone.getinstance().parcelsdelivery.get(i).getData().get(0).getLocation().getGeoPoints().getLat(), Databackbone.getinstance().parcelsdelivery.get(i).getData().get(0).getLocation().getGeoPoints().getLng());

                if (!Databackbone.getinstance().parcelsdelivery.get(i).getTaskStatus().equalsIgnoreCase("pending"))
                    activated_task = true;
                int size = 0;
                int total_parcel = Databackbone.getinstance().parcelsdelivery.get(i).getData().size();

                double distance = CalculationByDistance(Databackbone.getinstance().parcelsdelivery.get(i).getData().get(0).getLocation().getGeoPoints().getLat(), Databackbone.getinstance().parcelsdelivery.get(i).getData().get(0).getLocation().getGeoPoints().getLng());
                model_daily_package_item dataModelValue = new model_daily_package_item(Databackbone.getinstance().parcelsdelivery.get(i).getTaskId(), Databackbone.getinstance().parcelsdelivery.get(i).getData().get(0).getName(), Databackbone.getinstance().parcelsdelivery.get(0).getData().get(0).getLocation().getAddress(), Double.toString(distance) + "KM", Databackbone.getinstance().parcelsdelivery.get(0).getData().get(0).getLocation().getAddress(), activated_task, m_location, size);
                temp_ar_orders_daily.add(dataModelValue);
                activated_task = false;
            }
            Databackbone.getinstance().ar_orders_daily.addAll(temp_ar_orders_daily);
        }
        check_is_task_active_and_complete();

    }
    public void check_is_task_active_and_complete(){
        if(!Databackbone.getinstance().rider.getUser().getType().equalsIgnoreCase("delivery")) {
            for (int i = 0; i < Databackbone.getinstance().ar_orders_daily.size(); i++) {
                if(Databackbone.getinstance().ar_orders_daily.get(i).m_remaining_parcels_to_scan == 0 && Databackbone.getinstance().ar_orders_daily.get(i).status){
                    activate_Task_activater(Databackbone.getinstance().ar_orders_daily.get(i).mb_order_id,"completed");
                    return;
                }
            }
        }else
        {


        }
    }
    public double CalculationByDistance(double Lat,double Lng) {

        if(Databackbone.getinstance().current_location == null)
            return -1;
        LatLng EndP = new LatLng(Lat,Lng);
        LatLng StartP = Databackbone.getinstance().current_location;
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        double finalDistance = 0.0;
        finalDistance = round(km, 1);
        if(finalDistance < 0)
            finalDistance = finalDistance * -1;
        return finalDistance;
    }
    public void LoadParcels(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Databackbone.getinstance().Base_URL).addConverterFactory(GsonConverterFactory.create()).build();
        swift_api riderapi = retrofit.create(swift_api.class);
        EnableLoading();
        retrofit2.Call<List<DeliveryParcel>> call = riderapi.getParcelsByRiders(Databackbone.getinstance().rider.getId(),Databackbone.getinstance().rider.getUserId());
        call.enqueue(new Callback<List<DeliveryParcel>>() {
            @Override
            public void onResponse(retrofit2.Call<List<DeliveryParcel>> call, Response<List<DeliveryParcel>> response) {
                if(response.isSuccessful()){

                    List<DeliveryParcel> parcels = response.body();
                    // System.out.println(parcels.size());

                    Databackbone.getinstance().parcels = parcels;
                    load_Data();
                    update_view();
                    DisableLoading();

                }
                else{
                    DisableLoading();
                }

            }

            @Override
            public void onFailure(Call<List<DeliveryParcel>> call, Throwable t) {
                System.out.println(t.getCause());

                DisableLoading();
            }
        });


    }
    public void DisableLoading(){

        progressBar.setVisibility(View.GONE);
    }
    public void EnableLoading(){
        swipeToRefresh.setRefreshing(false);
        progressBar.setVisibility(View.VISIBLE);
    }
    public void update_view() {

        ad_orders_daily.update_list();

    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    public void startTaskConfirmation(final String name , final String taskid){
        new AlertDialog.Builder(activity_daily_order_status.this)
                .setTitle("Notice")
                .setMessage("You are about to start Task for " + name )

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(Databackbone.getinstance().rider.getUser().getType().equalsIgnoreCase("delivery"))
                            activate_Task_delivery_activater(taskid,"started");
                        else
                            activate_Task_activater(taskid,"started");



                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public void completeTaskConfirmation(final String name , final String taskid){
        new AlertDialog.Builder(activity_daily_order_status.this)
                .setTitle("Notice")
                .setMessage("You have already scanned all the parcels" )

                .setPositiveButton("Completed", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(Databackbone.getinstance().rider.getUser().getType().equalsIgnoreCase("delivery"))
                            activate_Task_delivery_activater(taskid,"completed");
                        else
                            activate_Task_activater(taskid,"completed");

                    }
                })
                .setNegativeButton("Issue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public void activate_Task_activater(String taskId,final String action){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Databackbone.getinstance().Base_URL).addConverterFactory(GsonConverterFactory.create()).build();
        swift_api riderapi = retrofit.create(swift_api.class);
        EnableLoading();
        Call<List<DeliveryParcel>> call = riderapi.manageTask(Databackbone.getinstance().rider.getId(),taskId,new manage_task(action));
        call.enqueue(new Callback<List<DeliveryParcel>>() {
            @Override
            public void onResponse(Call<List<DeliveryParcel>> call, Response<List<DeliveryParcel>> response) {
                if(response.isSuccessful()){

                    List<DeliveryParcel> parcels = response.body();
                    // System.out.println(parcels.size());

                    Databackbone.getinstance().parcels = parcels;
                    load_Data();
                    update_view();
                    Databackbone.getinstance().showAlsertBox(activity_daily_order_status.this,"confirmation","Task "+action);
                    DisableLoading();

                }
                else{
                    DisableLoading();
                }

            }

            @Override
            public void onFailure(Call<List<DeliveryParcel>> call, Throwable t) {
                DisableLoading();
            }
        });


    }
/*
    public void activate_Task_delivery_activater(String taskId,String Action){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Databackbone.getinstance().Base_URL).addConverterFactory(GsonConverterFactory.create()).build();
        swift_api riderapi = retrofit.create(swift_api.class);
        EnableLoading();
        Call<List<DeliveryParcel>> call = riderapi.manageTask(Databackbone.getinstance().rider.getId(),taskId,new manage_task(Action));
        call.enqueue(new Callback<List<DeliveryParcel>>() {
            @Override
            public void onResponse(Call<List<DeliveryParcel>> call, Response<List<DeliveryParcel>> response) {
                if(response.isSuccessful()){

                    List<DeliveryParcel> parcels = response.body();
                    // System.out.println(parcels.size());

                    Databackbone.getinstance().parcels = parcels;
                    load_Data();
                    update_view();
                    Databackbone.getinstance().showAlsertBox(activity_daily_order_status.this,"confirmation","Task Completed");
                    DisableLoading();

                }
                else{
                    DisableLoading();
                }

            }

            @Override
            public void onFailure(Call<List<DeliveryParcel>> call, Throwable t) {
                DisableLoading();
            }
        });


    }
    */

    public void activate_Task_delivery_activater(String taskId,final String action){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Databackbone.getinstance().Base_URL).addConverterFactory(GsonConverterFactory.create()).build();
        swift_api_delivery riderapi = retrofit.create(swift_api_delivery.class);
        EnableLoading();
        Call<List<RiderActivityDelivery>> call = riderapi.manageTask(Databackbone.getinstance().rider.getId(),taskId,new manage_task(action));
        call.enqueue(new Callback<List<RiderActivityDelivery>>() {
            @Override
            public void onResponse(Call<List<RiderActivityDelivery>> call, Response<List<RiderActivityDelivery>> response) {
                if(response.isSuccessful()){

                    List<RiderActivityDelivery> parcels = response.body();
                    // System.out.println(parcels.size());
                    Databackbone.getinstance().parcelsdelivery = parcels;
                    load_Data();
                    update_view();
                    DisableLoading();
                    Databackbone.getinstance().showAlsertBox(activity_daily_order_status.this,"confirmation","Task "+action);


                }
                else{
                    DisableLoading();
                }

            }

            @Override
            public void onFailure(Call<List<RiderActivityDelivery>> call, Throwable t) {
                System.out.println(t.getCause());

                DisableLoading();
            }
        });


    }
    public void LoadParcelsForDelivery(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Databackbone.getinstance().Base_URL).addConverterFactory(GsonConverterFactory.create()).build();
        swift_api_delivery riderapidata = retrofit.create(swift_api_delivery.class);
        EnableLoading();
        Call<List<RiderActivityDelivery>> call = riderapidata.manageTaskfordelivery(Databackbone.getinstance().rider.getId(),(Databackbone.getinstance().rider.getUserId()));
        call.enqueue(new Callback<List<RiderActivityDelivery>>() {
            @Override
            public void onResponse(Call<List<RiderActivityDelivery>> call, Response<List<RiderActivityDelivery>> response) {
                if(response.isSuccessful()){

                    List<RiderActivityDelivery> parcels = response.body();
                    // System.out.println(parcels.size());
                    Databackbone.getinstance().parcelsdelivery = parcels;

                    DisableLoading();
                    load_Data();
                    update_view();
                }
                else{
                    DisableLoading();
                }

            }

            @Override
            public void onFailure(Call<List<RiderActivityDelivery>> call, Throwable t) {
                System.out.println(t.getCause());

                DisableLoading();
                load_Data();
            }
        });


    }
    public boolean check_is_any_delivery_task_active()
    {
        for(int i =0;i<Databackbone.getinstance().ar_orders_daily.size();i++)
        {
            if(Databackbone.getinstance().ar_orders_daily.get(i).status)
                return true;
        }
        return false;
    }
    public boolean check_is_any_pickup_task_active()
    {
        for(int i =0;i<Databackbone.getinstance().parcels.size();i++)
        {
            if(!Databackbone.getinstance().parcels.get(i).getTaskStatus().equalsIgnoreCase("pending"))
                return true;
        }
        return false;
    }
}
