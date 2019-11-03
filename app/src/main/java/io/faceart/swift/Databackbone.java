package io.faceart.swift;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import io.faceart.swift.data_models.*;
import io.faceart.swift.interface_retrofit.DeliveryParcel;
import io.faceart.swift.interface_retrofit.Rider;
import io.faceart.swift.interface_retrofit.RiderActivity;
import io.faceart.swift.interface_retrofit_delivery.RiderActivityDelivery;

public class Databackbone {
    public static Databackbone databackbone=null;
    public ArrayList<model_order_item> ar_orders_diclined,ar_orders_reattempt,ar_orders_delivered,ar_orders_scanned,ar_orders_remaining;
    public ArrayList<model_daily_package_item> ar_orders_daily = new ArrayList<>();

    public ArrayList<model_daily_package_item> ar_task_daily_delivery = new ArrayList<>();
    public ArrayList<model_wallets_order> ar_orders_wallet = new ArrayList<>();
    public ArrayList<model_parcel> ar_orders_parcels_selections = new ArrayList<>();

    public Rider rider = null;
    public RiderActivity riderActivity = null;
    public String Base_URL = "http://13.235.240.229:3000/api/";
    List<DeliveryParcel> parcels = null;
    List<RiderActivityDelivery> parcelsdelivery = null;
    Boolean check_parcel_scanning_complete = true;
    Boolean user_type_pickup = true;
    LatLng current_location = null;
    public int pickup_to_process = -1 ;
    public int delivery_to_show = -1 ;
    public int task_to_show = -1 ;

    public List<String> parcel_to_process= new ArrayList<String>();


    public String not_delivered_reason = "";
    private Databackbone(){

    }

    public static Databackbone getinstance(){

        if(databackbone == null)
            databackbone = new Databackbone();
        return databackbone;
    }
    public void showAlsertBox(Context contect , String title , String message ){
        new AlertDialog.Builder(contect)
                .setTitle(title)
                .setMessage(message)

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public void remove_location_complete(){
        for(int i =0;i<parcelsdelivery.size();i++)
        {
            parcelsdelivery.get(i).removeCompletedActivities();
        }
    }
    private boolean haveNetworkConnection(Context ctx) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(ctx.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    public boolean checkInternet(Context ctx){
        if(!haveNetworkConnection(ctx)){
            this.showAlsertBox(ctx,"Error","No Internet");
            return true;
        }
        else {

            return false;
        }

    }
}
