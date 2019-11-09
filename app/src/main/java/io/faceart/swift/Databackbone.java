package io.faceart.swift;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.faceart.swift.data_models.*;
import io.faceart.swift.interface_retrofit.DeliveryParcel;
import io.faceart.swift.interface_retrofit.Location;
import io.faceart.swift.interface_retrofit.Rider;
import io.faceart.swift.interface_retrofit.RiderActivity;
import io.faceart.swift.interface_retrofit.RiderDetails;
import io.faceart.swift.interface_retrofit_delivery.RiderActivityDelivery;

public class Databackbone {
    public static Databackbone databackbone=null;
    public ArrayList<model_order_item> ar_orders_diclined,ar_orders_reattempt,ar_orders_delivered,ar_orders_scanned,ar_orders_remaining;
    public ArrayList<model_daily_package_item> ar_orders_daily = new ArrayList<>();

    public ArrayList<model_daily_package_item> ar_task_daily_delivery = new ArrayList<>();

    public ArrayList<model_daily_package_item> ar_task_daily_pickup = new ArrayList<>();
    public ArrayList<model_wallets_order> ar_orders_wallet = new ArrayList<>();
    public ArrayList<model_parcel> ar_orders_parcels_selections = new ArrayList<>();

    public Rider rider = null;
    public RiderActivity riderActivity = null;

    public RiderDetails riderdetails = null;
    //testcase
    // public String Base_URL = "http://13.235.240.229:3000/api/";
    // real
    public String Base_URL = "http://18.136.172.141:3000/api/";
    List<DeliveryParcel> parcels = null;
    List<RiderActivityDelivery> parcelsdelivery = null;
    Boolean check_parcel_scanning_complete = true;

    LatLng current_location = null;
    public int pickup_to_process = -1 ;
    public int delivery_to_show = -1 ;
    public int task_to_show = -1 ;

    public List<String> parcel_to_process= new ArrayList<String>();


    public String not_delivered_reason = "";

    public boolean RiderTypeDelivery = false;

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
    public void SaveData(String Key,String Value,Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences("swiftdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Key, Value);
        editor.commit();
    }
    public String GetData(String Key,Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences("swiftdata", Context.MODE_PRIVATE);
        return sharedpreferences.getString(Key,"none");

    }
    public void taskStart( String id ,double lat,double lon,double distance,Context context){
            String Protocol = id+","+Double.toString(lat)+","+Double.toString(lon)+","+Double.toString(distance);
            SaveData("swift_work_progress",Protocol,context);

    }
    public void updateDistance(Context context){
        String protocol = Databackbone.getinstance().GetData("swift_work_progress",context);
        if(protocol.equals("none"))return;
        else{
            String data[] = protocol.split(",");
            String id = data[0];
            double lat = Double.parseDouble(data[1]);
            double lon = Double.parseDouble(data[2]);
            double distance = Double.parseDouble(data[3]);


            double current_location_lat = this.current_location.latitude;
            double current_location_lon = this.current_location.longitude;
            double distance_calculated = CalculationByDistance(lat,lon);

            distance += distance_calculated;
            String Protocol = id+","+Double.toString(current_location_lat)+","+Double.toString(current_location_lon)+","+Double.toString(distance);
            SaveData("swift_work_progress",Protocol,context);
        }
    }
    public double getfinalcouvereddistance(String id_required_data,Context context){
        String protocol = Databackbone.getinstance().GetData("swift_work_progress",context);
        if(protocol.equals("none"))return 1.0;
        else{
            String data[] = protocol.split(",");
            String id = data[0];
            double distance = Double.parseDouble(data[3]);
            if(id.equals(id_required_data)) {

                return distance;
            }
            else
            {
                return 1.0;
            }

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
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
