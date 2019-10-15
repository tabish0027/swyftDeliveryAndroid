package io.faceart.swift;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.faceart.swift.adapters.*;
import io.faceart.swift.data_models.*;

public class activity_daily_order_status  extends Activity {


    public RecyclerView order_list_daily;
    public adapter_status_daily_packages ad_orders_daily;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_order_status);

        order_list_daily = findViewById(R.id.order_list_daily);


        Databackbone.getinstance().ar_orders_daily = new ArrayList<>();


        generate_test_Data();
        ad_orders_daily = new adapter_status_daily_packages(Databackbone.getinstance().ar_orders_daily, this);


        order_list_daily.setAdapter(ad_orders_daily);






    }

    @Override
    protected void onResume() {
        super.onResume();

        update_view();
    }


    public void generate_test_Data() {
        Databackbone.getinstance().ar_orders_daily.clear();

        ArrayList<model_daily_package_item> temp_ar_orders_daily = new ArrayList<>();
        temp_ar_orders_daily.add(new model_daily_package_item("4384745", "Amir " + Integer.toString(0), "G 47 DHA lahore", "17."+Integer.toString(0)+" KM","DHA Phase 1",true));

        for (int i = 1; i < 10; i++) {
            temp_ar_orders_daily.add(new model_daily_package_item("4384745", "Amir " + Integer.toString(i), "G 47 DHA lahore", "17."+Integer.toString(i)+" KM","DHA Phase 1",false));
        }
        Databackbone.getinstance().ar_orders_daily.addAll(temp_ar_orders_daily);



    }

    public void update_view() {

        ad_orders_daily.update_list();

    }
}
