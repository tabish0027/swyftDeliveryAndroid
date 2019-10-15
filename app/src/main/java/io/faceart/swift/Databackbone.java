package io.faceart.swift;

import java.util.ArrayList;

import io.faceart.swift.data_models.*;

public class Databackbone {
    public static Databackbone databackbone=null;
    public ArrayList<model_order_item> ar_orders_diclined,ar_orders_reattempt,ar_orders_delivered,ar_orders_scanned,ar_orders_remaining;
    public ArrayList<model_daily_package_item> ar_orders_daily;

    private Databackbone(){

    }

    public static Databackbone getinstance(){

        if(databackbone == null)
            databackbone = new Databackbone();
        return databackbone;
    }
}
