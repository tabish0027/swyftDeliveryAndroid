package io.faceart.swift;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import io.faceart.swift.interface_retrofit.swift_api;
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_order_status);
        final ImageView btn_back = findViewById(R.id.btn_back);
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        progressBar = (ProgressBar)findViewById(R.id.url_loading_animation);
        progressBar.setVisibility(View.GONE);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_daily_order_status.this.finish();
            }
        });
        order_list_daily = findViewById(R.id.order_list_daily);


        load_Data();

        //generate_test_Data();
        ad_orders_daily = new adapter_status_daily_packages(Databackbone.getinstance().ar_orders_daily, this);


        order_list_daily.setAdapter(ad_orders_daily);

        ad_orders_daily.setOnItemClickListener(new adapter_status_daily_packages.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                Toast.makeText(activity_daily_order_status.this,"onItemClick position: " + position,Toast.LENGTH_LONG).show();
                if(Databackbone.getinstance().ar_orders_daily.get(position).status)
                {
                    Databackbone.getinstance().pickup_to_process = position;
                    Intent barcode_scanner = new Intent(activity_daily_order_status.this,activity_barcode_scanner.class);
                    activity_daily_order_status.this.startActivity(barcode_scanner);

                }
                else{
                    Databackbone.getinstance().showAlsertBox(activity_daily_order_status.this,"Error","Task Not Active");
                }
            }

            @Override
            public void onItemLongClick(int position, View v) {

                Toast.makeText(activity_daily_order_status.this,"onItemLongClick position: " + position,Toast.LENGTH_LONG).show();
            }
        });

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadParcels();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        update_view();
    }


    public void load_Data() {
        Databackbone.getinstance().ar_orders_daily.clear();

        ArrayList<model_daily_package_item> temp_ar_orders_daily = new ArrayList<>();
         if(Databackbone.getinstance().parcels == null)
            return;
         Boolean activated_task = false;
        for (int i = 0; i < Databackbone.getinstance().parcels.size(); i++) {
            LatLng m_location = new LatLng(Databackbone.getinstance().parcels.get(i).getLocation().getGeoPoints().getLat(),Databackbone.getinstance().parcels.get(i).getLocation().getGeoPoints().getLng());
            if(!Databackbone.getinstance().parcels.get(i).getTaskStatus().equals("pending"))
            activated_task = true;
            double distance = CalculationByDistance(Databackbone.getinstance().parcels.get(i).getLocation().getGeoPoints().getLat(),Databackbone.getinstance().parcels.get(i).getLocation().getGeoPoints().getLng());
            temp_ar_orders_daily.add(new model_daily_package_item(Databackbone.getinstance().parcels.get(i).getTaskId(), Databackbone.getinstance().parcels.get(i).getName(), Databackbone.getinstance().parcels.get(i).getLocation().getAddress(), Double.toString(distance)+"KM",Databackbone.getinstance().parcels.get(i).getLocation().getAddress(),activated_task,m_location));
            activated_task = false;
        }
        Databackbone.getinstance().ar_orders_daily.addAll(temp_ar_orders_daily);



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
    public void activate_Task(){

    }

}
