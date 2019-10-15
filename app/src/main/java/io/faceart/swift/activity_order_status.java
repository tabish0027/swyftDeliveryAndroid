package io.faceart.swift;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.faceart.swift.data_models.model_order_item;
import io.faceart.swift.adapters.adapter_status_packages_list;

public class activity_order_status  extends Activity {
    public Button btn_today;
    public Button btn_month;
    public Button btn_week;
    public androidx.constraintlayout.widget.ConstraintLayout con_orders_diclined,con_orders_reattempt,con_orders_delivered;

    public RecyclerView order_list_reattempt,order_list_declined,order_list_delivered;
    public adapter_status_packages_list ad_orders_diclined,ad_orders_reattempt,ad_orders_delivered;

    public TextView tx_count_delivered,tx_count_declined,tx_count_reattempt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        btn_month = findViewById(R.id.btn_month);
        btn_today = findViewById(R.id.btn_today);
        btn_week = findViewById(R.id.btn_week);
        order_list_reattempt= findViewById(R.id.order_list_reattempt);
        order_list_declined= findViewById(R.id.order_list_declined);
        order_list_delivered= findViewById(R.id.order_list_delivered);

        con_orders_diclined= findViewById(R.id.con_orders_diclined);
        con_orders_reattempt= findViewById(R.id.con_orders_reattempt);
        con_orders_delivered= findViewById(R.id.con_orders_delivered);

        tx_count_delivered= findViewById(R.id.tx_count_delivered);
        tx_count_declined= findViewById(R.id.tx_count_declined);
        tx_count_reattempt= findViewById(R.id.tx_count_reattempt);


        Databackbone.getinstance().ar_orders_diclined = new ArrayList<>();
        Databackbone.getinstance().ar_orders_reattempt= new ArrayList<>();
        Databackbone.getinstance().ar_orders_delivered= new ArrayList<>();;
        generate_test_Data();
        ad_orders_diclined = new adapter_status_packages_list(Databackbone.getinstance().ar_orders_diclined, this);
        ad_orders_reattempt = new adapter_status_packages_list(Databackbone.getinstance().ar_orders_reattempt, this);
        ad_orders_delivered = new adapter_status_packages_list(Databackbone.getinstance().ar_orders_delivered, this);

        order_list_reattempt.setAdapter(ad_orders_reattempt);
        order_list_declined.setAdapter(ad_orders_diclined);
        order_list_delivered.setAdapter(ad_orders_delivered);

        btn_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDay();
            }
        });

        btn_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectWeek();
            }
        });

        btn_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectMonth();
            }
        });
        con_orders_diclined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(order_list_declined.getVisibility() == View.VISIBLE)
                {
                    order_list_declined.setVisibility(View.GONE);
                }
                else
                {
                    order_list_declined.setVisibility(View.VISIBLE);
                }
            }
        });
        con_orders_reattempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(order_list_reattempt.getVisibility() == View.VISIBLE)
                {
                    order_list_reattempt.setVisibility(View.GONE);
                }
                else
                {
                    order_list_reattempt.setVisibility(View.VISIBLE);
                }
            }
        });
        con_orders_delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(order_list_delivered.getVisibility() == View.VISIBLE)
                {
                    order_list_delivered.setVisibility(View.GONE);
                }
                else
                {
                    order_list_delivered.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        update_view();
    }

    public void SelectDay(){
        btn_today.setBackgroundResource(R.drawable.button_earn_select);
        btn_week.setBackgroundResource(R.drawable.button_earn_unselect);
        btn_month.setBackgroundResource(R.drawable.button_earn_unselect);
        btn_today.setTextColor(Color.parseColor("#221e1f"));
        btn_week.setTextColor(Color.parseColor("#ffffff"));
        btn_month.setTextColor(Color.parseColor("#ffffff"));


    }
    public void SelectMonth(){
        btn_month.setBackgroundResource(R.drawable.button_earn_select);
        btn_week.setBackgroundResource(R.drawable.button_earn_unselect);
        btn_today.setBackgroundResource(R.drawable.button_earn_unselect);
        btn_month.setTextColor(Color.parseColor("#221e1f"));
        btn_week.setTextColor(Color.parseColor("#ffffff"));
        btn_today.setTextColor(Color.parseColor("#ffffff"));
    }
    public void SelectWeek(){
        btn_week.setBackgroundResource(R.drawable.button_earn_select);
        btn_today.setBackgroundResource(R.drawable.button_earn_unselect);
        btn_month.setBackgroundResource(R.drawable.button_earn_unselect);
        btn_week.setTextColor(Color.parseColor("#221e1f"));
        btn_today.setTextColor(Color.parseColor("#ffffff"));
        btn_month.setTextColor(Color.parseColor("#ffffff"));
    }

    public void generate_test_Data() {
        Databackbone.getinstance().ar_orders_diclined.clear();
        Databackbone.getinstance().ar_orders_reattempt.clear();
        Databackbone.getinstance().ar_orders_delivered.clear();

        ArrayList<model_order_item>  temp_ar_orders_diclined = new ArrayList<>();
        ArrayList<model_order_item>  temp_ar_orders_reattempt= new ArrayList<>();
        ArrayList<model_order_item>  temp_ar_orders_delivered= new ArrayList<>();
        for(int i =0;i<10;i++){
            temp_ar_orders_diclined.add(new model_order_item("123123"+Integer.toString(i), "10/10/2018", "14:12AM", "declined"));
            temp_ar_orders_reattempt.add(new model_order_item("123123"+Integer.toString(i), "10/10/2018", "14:12AM", "reattempt"));
            temp_ar_orders_delivered.add(new model_order_item("123123"+Integer.toString(i), "10/10/2018", "14:12AM", "delivered"));
        }
        Databackbone.getinstance().ar_orders_delivered.addAll(temp_ar_orders_delivered);
        Databackbone.getinstance().ar_orders_diclined.addAll(temp_ar_orders_diclined);
        Databackbone.getinstance().ar_orders_reattempt.addAll(temp_ar_orders_reattempt);


    }
    public void update_view(){
        tx_count_declined.setText(Integer.toString(Databackbone.getinstance().ar_orders_diclined.size()));
        tx_count_reattempt.setText(Integer.toString(Databackbone.getinstance().ar_orders_reattempt.size()));
        tx_count_delivered.setText(Integer.toString(Databackbone.getinstance().ar_orders_delivered.size()));



        //ad_orders_diclined = new adapter_status_packages_list(Databackbone.getinstance().ar_orders_diclined, this);
        //ad_orders_reattempt = new adapter_status_packages_list(Databackbone.getinstance().ar_orders_reattempt, this);
        //ad_orders_delivered = new adapter_status_packages_list(Databackbone.getinstance().ar_orders_delivered, this);

       // order_list_reattempt.setAdapter(ad_orders_reattempt);
        //order_list_declined.setAdapter(ad_orders_diclined);
        //order_list_delivered.setAdapter(ad_orders_delivered);


        ad_orders_diclined.update_list();
        ad_orders_reattempt.update_list();
        ad_orders_delivered.update_list();


    }

}
