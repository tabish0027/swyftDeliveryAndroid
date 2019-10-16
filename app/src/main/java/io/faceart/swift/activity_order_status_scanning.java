package io.faceart.swift;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.faceart.swift.adapters.adapter_status_packages_scanning;
import io.faceart.swift.data_models.model_order_item;

public class activity_order_status_scanning extends Activity {

    public androidx.constraintlayout.widget.ConstraintLayout con_orders_scanned,con_orders_remaining;

    public RecyclerView order_list_remaining,order_list_scanned;
    public adapter_status_packages_scanning ad_orders_scanned,ad_orders_remaining;
    public TextView tx_count_scanned,tx_count_remaining;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_order_status);

        order_list_remaining= findViewById(R.id.order_list_remaining);
        order_list_scanned= findViewById(R.id.order_list_scanned);


        con_orders_scanned= findViewById(R.id.con_orders_scanned);
        con_orders_remaining= findViewById(R.id.con_orders_remaining);



        tx_count_scanned= findViewById(R.id.tx_count_scanned);
        tx_count_remaining= findViewById(R.id.tx_count_remaining);


        Databackbone.getinstance().ar_orders_scanned = new ArrayList<>();
        Databackbone.getinstance().ar_orders_remaining= new ArrayList<>();

        generate_test_Data();
        ad_orders_scanned = new adapter_status_packages_scanning(Databackbone.getinstance().ar_orders_scanned, this);
        ad_orders_remaining = new adapter_status_packages_scanning(Databackbone.getinstance().ar_orders_remaining, this);


        order_list_remaining.setAdapter(ad_orders_remaining);
        order_list_scanned.setAdapter(ad_orders_scanned);



        con_orders_scanned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(order_list_scanned.getVisibility() == View.VISIBLE)
                {
                    order_list_scanned.setVisibility(View.GONE);
                }
                else
                {
                    order_list_scanned.setVisibility(View.VISIBLE);
                }
            }
        });
        con_orders_remaining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(order_list_remaining.getVisibility() == View.VISIBLE)
                {
                    order_list_remaining.setVisibility(View.GONE);
                }
                else
                {
                    order_list_remaining.setVisibility(View.VISIBLE);
                }
            }
        });

        final ImageView btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_order_status_scanning.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        update_view();
    }



    public void generate_test_Data() {
        Databackbone.getinstance().ar_orders_scanned.clear();
        Databackbone.getinstance().ar_orders_remaining.clear();

        ArrayList<model_order_item>  temp_ar_orders_scanned = new ArrayList<>();
        ArrayList<model_order_item>  temp_ar_orders_remaining= new ArrayList<>();
        for(int i =0;i<10;i++){
            temp_ar_orders_scanned.add(new model_order_item("123123"+Integer.toString(i), "10/10/2018", "14:12AM", "scan"));
            temp_ar_orders_remaining.add(new model_order_item("123123"+Integer.toString(i), "10/10/2018", "14:12AM", "remain"));
        }
        Databackbone.getinstance().ar_orders_scanned.addAll(temp_ar_orders_scanned);
        Databackbone.getinstance().ar_orders_remaining.addAll(temp_ar_orders_remaining);


    }
    public void update_view(){
        tx_count_scanned.setText(Integer.toString(Databackbone.getinstance().ar_orders_scanned.size()));
        tx_count_remaining.setText(Integer.toString(Databackbone.getinstance().ar_orders_remaining.size()));


        ad_orders_scanned.update_list();
        ad_orders_remaining.update_list();


    }

}
