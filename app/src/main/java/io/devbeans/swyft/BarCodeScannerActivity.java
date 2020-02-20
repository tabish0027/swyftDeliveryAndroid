package io.devbeans.swyft;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.devbeans.swyft.interface_retrofit.Parcel;
import io.devbeans.swyft.interface_retrofit.PickupParcel;
import io.devbeans.swyft.interface_retrofit_delivery.Datum;
import io.devbeans.swyft.interface_retrofit_delivery.RiderActivityDelivery;
import io.swyft.swyft.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarCodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    LinearLayout barcodescannerview;
    TextView tx_barcode ;
    ConstraintLayout layout_scanned_id,layout_add_parcel;
    ImageView btn_refreash,btn_add_parcel;
    ConstraintLayout barcode_remaining_parcels ;
    TextView tx_parcels_to_scan;
    ProgressBar progressBar = null;
    int pending_parcels_to_scan = 0;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "ScannedList";

    EditText edt_parcel_id;
    Button btn_add;
    ImageView btn_back;
    int position = 0;
    int inner_position = 0;
    Gson gson = new Gson();

    List<String> localScannedIds = new ArrayList<>();

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_scanner);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mEditor = sharedpreferences.edit();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 2);
        }else {
            position = Integer.valueOf(getIntent().getStringExtra("position"));
            inner_position = Integer.valueOf(getIntent().getStringExtra("locationPosition"));
            Initializations();
            Actions();
        }
    }

    private void Actions(){

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                finish();
            }
        });

        barcode_remaining_parcels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showParcelList = new Intent(BarCodeScannerActivity.this,activity_order_status_scanning.class);
                showParcelList.putExtra("position", String.valueOf(position));
                showParcelList.putExtra("locationPosition", String.valueOf(inner_position));
                startActivity(showParcelList);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_parcel_id.getText().length()!=0){
                    String Scannedbarcode = edt_parcel_id.getText().toString();
                    mScannerView.stopCameraPreview();
//                    EnableLoading();
//                    check_parcel_to_scan(Scannedbarcode);
                }
            }
        });
        btn_refreash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                refreashScanner();
            }
        });
        //View child1 = LayoutInflater.from(this).inflate(mScannerView, null);
        layout_scanned_id = findViewById(R.id.constraintLayout5);
        layout_scanned_id.setVisibility(View.INVISIBLE);
        barcodescannerview.addView(mScannerView);
        load_parcels_to_scan();
        btn_add_parcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_add_parcel.setVisibility(View.VISIBLE);
                layout_scanned_id.setVisibility(View.INVISIBLE);

            }
        });
    }

    private void Initializations(){
        barcodescannerview =  findViewById(R.id.barcodescannerview);
        tx_barcode = findViewById(R.id.tx_barcode);
        mScannerView = new ZXingScannerView(this);
        btn_refreash = findViewById(R.id.btn_refreash);
        tx_parcels_to_scan = findViewById(R.id.tx_parcels_to_scan);
        btn_add_parcel = findViewById(R.id.btn_add_parcel);
        layout_add_parcel = findViewById(R.id.layout_add_parcel);
        progressBar = (ProgressBar)findViewById(R.id.url_loading_animation);
        progressBar.setVisibility(View.INVISIBLE);
        edt_parcel_id = findViewById(R.id.edt_parcel_id);
        btn_add = findViewById(R.id.btn_add);
        btn_back = findViewById(R.id.btn_back);
        barcode_remaining_parcels = findViewById(R.id.barcode_remaining_parcels);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 2){
            position = Integer.valueOf(getIntent().getStringExtra("position"));
            inner_position = Integer.valueOf(getIntent().getStringExtra("locationPosition"));
            Initializations();
            Actions();
            mScannerView = new ZXingScannerView(this);
            barcodescannerview.addView(mScannerView);
            mScannerView.startCamera();
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        String Scannedbarcode = rawResult.getText();
        mScannerView.stopCameraPreview();
        EnableLoading();
        check_parcel_to_scan(Scannedbarcode);
    }

    public void check_parcel_to_scan(String id){
        if(Databackbone.getinstance().riderdetails.getType().equalsIgnoreCase("delivery"))
        {
            /*Boolean check = false;
            //if(Databackbone.getinstance().task_to_show >= Databackbone.getinstance().parcelsdelivery.size()   )
            //    activity_barcode_scanner.this.finish();
            RiderActivityDelivery riderActivityDelivery = Databackbone.getinstance().getDeliveryTask();
            if(riderActivityDelivery == null)
                activity_barcode_scanner.this.finish();


            List<Datum> Locations= riderActivityDelivery.getData();

            for (int i = 0; i < Locations.size(); i++) {
                Datum data = Locations.get(i);

                for (int j = 0; j < data.getParcels().size(); j++) {
                    if(data.getParcels().get(j).getParcelId().equals(id) ){
                        if(!data.getParcels().get(j).getStatus().equals("pending")){
                            Databackbone.getinstance().showAlsertBox(this, getResources().getString(R.string.error), getResources().getString(R.string.parcel_already_scanned));
                            DisableLoading();
                            refreahScanner();
                            return;
                        }
                        check = true;
                        break;

                    }
                }
                if(check)break;
            }
            if(!check){
                Databackbone.getinstance().showAlsertBox(this, getResources().getString(R.string.error), "Parcel not found");
                DisableLoading();
                refreahScanner();
            }else{
                send_request_to_server_for_delivery(id);
            }*/
        }
        else {

            if(Databackbone.getinstance().parcelsIds == null)
                finish();

            Boolean check = false;
            Boolean innerCheck = false;

            if (Databackbone.getinstance().scannedParcelsIds != null && !Databackbone.getinstance().scannedParcelsIds.isEmpty()){
                for (int i = 0; i < Databackbone.getinstance().scannedParcelsIds.size(); i++){
                    if (id.equals(Databackbone.getinstance().scannedParcelsIds.get(i))){

                        innerCheck = true;

                        break;
                    }
                }
            }

            if (innerCheck){
                Databackbone.getinstance().showAlsertBox(this, getResources().getString(R.string.error), "Parcel already Scanned");

                DisableLoading();
                refreahScanner();
            }else {
                List<String> parcels = Databackbone.getinstance().parcelsIds;
                for (int i = 0; i < parcels.size(); i++) {
                    if (parcels.get(i).equals(id)) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    Databackbone.getinstance().showAlsertBox(this, getResources().getString(R.string.error), "Parcel not found");

                    DisableLoading();
                    refreahScanner();
                } else {
                    if (Databackbone.getinstance().scannedParcelsIds == null){
                        localScannedIds.add(id);
                        Databackbone.getinstance().scannedParcelsIds = localScannedIds;
                        String jsonP = gson.toJson(Databackbone.getinstance().scannedParcelsIds);
                        mEditor.putString(Databackbone.getinstance().todayassignmentdata.get(position).getVendorId() + Databackbone.getinstance().todayassignmentdata.get(position).getPickupLocations().get(inner_position).getId(), jsonP).commit();
                        Scan_successfull(id);
                    }else {
                        Databackbone.getinstance().scannedParcelsIds.add(id);
                        String jsonP = gson.toJson(Databackbone.getinstance().scannedParcelsIds);
                        mEditor.putString(Databackbone.getinstance().todayassignmentdata.get(position).getVendorId() + Databackbone.getinstance().todayassignmentdata.get(position).getPickupLocations().get(inner_position).getId(), jsonP).commit();
                        Scan_successfull(id);
                    }

                }
            }


        }

    }


    @Override
    public void onPause() {
        super.onPause();
        if (mScannerView != null){
            mScannerView.stopCamera();           // Stop camera on pause
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mScannerView != null){
            mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
            mScannerView.startCamera(); // Start camera on resume
        }
    }
    public void refreashScanner(){
        layout_add_parcel.setVisibility(View.GONE);
        layout_add_parcel.setVisibility(View.VISIBLE);

        mScannerView.resumeCameraPreview(BarCodeScannerActivity.this);
        layout_scanned_id.setVisibility(View.INVISIBLE);
        layout_add_parcel.setVisibility(View.GONE);
    }
    public void scanparceldone(String id){
        tx_barcode.setText(id);
        layout_scanned_id.setVisibility(View.VISIBLE);
        layout_add_parcel.setVisibility(View.GONE);
        this.mScannerView.playSoundEffect(SoundEffectConstants.CLICK);

        Vibrator v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        int resID=getResources().getIdentifier("tick", "raw", getPackageName());

        MediaPlayer mediaPlayer=MediaPlayer.create(this,resID);
        mediaPlayer.start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {

            v.vibrate(500);
        }
        refreahScanner();
    }

    public void Scan_successfull(String id){
        scanparceldone(id);
        load_parcels_to_scan();
        DisableLoading();
    }

    public void load_parcels_to_scan(){


        if(Databackbone.getinstance().riderdetails.getType().equalsIgnoreCase("delivery")){

            /*//if(Databackbone.getinstance().task_to_show >= Databackbone.getinstance().parcelsdelivery.size()   )
            //   activity_barcode_scanner.this.finish();
            RiderActivityDelivery riderActivityDelivery = Databackbone.getinstance().getDeliveryTask();
            if(riderActivityDelivery == null)
                finish();
            List<Datum> Locations = riderActivityDelivery.getData();
            //List<Datum> Locations= Databackbone.getinstance().parcelsdelivery.get(Databackbone.getinstance().task_to_show).getData();
            pending_parcels_to_scan = 0 ;
            for (int i = 0; i < Locations.size(); i++) {
                Datum data = Locations.get(i);

                for (int j = 0; j < data.getParcels().size(); j++) {
                    if((data.getParcels().get(j).getStatus().equals("pending"))){
                        pending_parcels_to_scan = pending_parcels_to_scan + 1;
                    }
                }
            }
            if(pending_parcels_to_scan <= 1)
                tx_parcels_to_scan.setText(Integer.toString(pending_parcels_to_scan)+" " + getResources().getString(R.string.parcel_left_to_scan));
            else tx_parcels_to_scan.setText(Integer.toString(pending_parcels_to_scan)+" " + getResources().getString(R.string.parcel_left_to_scan));
*/

        }else{


            Databackbone.getinstance().parcelsIds = Databackbone.getinstance().todayassignmentdata.get(position).getParcels();

            List<String> arrayList = new ArrayList<>();
            String json = sharedpreferences.getString(Databackbone.getinstance().todayassignmentdata.get(position).getVendorId() + Databackbone.getinstance().todayassignmentdata.get(position).getPickupLocations().get(inner_position).getId(), "");

            if (!(json.equals(null) || json.equals(""))) {
                Type type = new TypeToken<List<String>>() {
                }.getType();
                arrayList = gson.fromJson(json, type);
                Databackbone.getinstance().scannedParcelsIds = arrayList;
                pending_parcels_to_scan = Databackbone.getinstance().parcelsIds.size() - Databackbone.getinstance().scannedParcelsIds.size();
            }else {
                pending_parcels_to_scan = Databackbone.getinstance().parcelsIds.size();
            }

            if(pending_parcels_to_scan <= 1)
                tx_parcels_to_scan.setText(Integer.toString(pending_parcels_to_scan) + " " + getResources().getString(R.string.parcel_left_to_scan));
            else tx_parcels_to_scan.setText(Integer.toString(pending_parcels_to_scan) + " " + getResources().getString(R.string.parcel_left_to_scan));

        }



    }

    public void DisableLoading(){
        btn_add_parcel.setEnabled(true);
        btn_refreash.setEnabled(true);
        barcode_remaining_parcels.setClickable(true);

        progressBar.setVisibility(View.GONE);
    }

    public void RefreshList(){
        mEditor.clear().commit();
    }
    public void EnableLoading(){

        btn_add_parcel.setEnabled(false);
        btn_refreash.setEnabled(false);
        barcode_remaining_parcels.setClickable(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void refreahScanner(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(3000);
                    runOnUiThread (new Thread(new Runnable() {
                        public void run() {
                            refreashScanner();
                            AllParcelScanned();
                        }
                    }));


                }catch (Exception i){

                }
            }
        }).start();
    }
    public void AllParcelScanned(){
        if(pending_parcels_to_scan == 0)
            //return ;
            new AlertDialog.Builder(BarCodeScannerActivity.this)
                    .setTitle(getResources().getString(R.string.confirmation))
                    .setMessage(getResources().getString(R.string.all_parcels_scanned))

                    .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            BarCodeScannerActivity.this.finish();
                        }
                    })

                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
    }
}
