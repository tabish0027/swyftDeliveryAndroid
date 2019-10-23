package io.faceart.swift;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import io.faceart.swift.interface_retrofit.DeliveryParcel;
import io.faceart.swift.interface_retrofit.parcel_scan;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.util.List;

import javax.xml.transform.Result;

import io.faceart.swift.interface_retrofit.Parcel;
import io.faceart.swift.interface_retrofit.RiderActivity;
import io.faceart.swift.interface_retrofit.online;
import io.faceart.swift.interface_retrofit.swift_api;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_barcode_scanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    LinearLayout barcodescannerview;
    TextView tx_barcode ;
    ConstraintLayout layout_scanned_id,layout_add_parcel;
    ImageView btn_refreash,btn_add_parcel;
    ConstraintLayout barcode_remaining_parcels ;
    TextView tx_parcels_to_scan;
    ProgressBar progressBar = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        barcodescannerview =  findViewById(R.id.barcodescannerview);
        tx_barcode = findViewById(R.id.tx_barcode);
        mScannerView = new ZXingScannerView(this);
        btn_refreash = findViewById(R.id.btn_refreash);
        tx_parcels_to_scan = findViewById(R.id.tx_parcels_to_scan);
        btn_add_parcel = findViewById(R.id.btn_add_parcel);
        layout_add_parcel = findViewById(R.id.layout_add_parcel);
        progressBar = (ProgressBar)findViewById(R.id.url_loading_animation);
        progressBar.setVisibility(View.INVISIBLE);
        final ImageView btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_barcode_scanner.this.finish();
            }
        });
        barcode_remaining_parcels = findViewById(R.id.barcode_remaining_parcels);
        barcode_remaining_parcels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showParcelList = new Intent(activity_barcode_scanner.this,activity_order_status_scanning.class);
                activity_barcode_scanner.this.startActivity(showParcelList);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });
        btn_refreash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_add_parcel.setVisibility(View.GONE);
                layout_add_parcel.setVisibility(View.VISIBLE);

                mScannerView.resumeCameraPreview(activity_barcode_scanner.this);
                layout_scanned_id.setVisibility(View.INVISIBLE);
                layout_add_parcel.setVisibility(View.GONE);
            }
        });
        //View child1 = LayoutInflater.from(this).inflate(mScannerView, null);
        layout_scanned_id = findViewById(R.id.constraintLayout5);
        layout_scanned_id.setVisibility(View.INVISIBLE);
        barcodescannerview.addView(mScannerView);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 2);
        load_parcels_to_scan();
        btn_add_parcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_add_parcel.setVisibility(View.VISIBLE);
                layout_scanned_id.setVisibility(View.INVISIBLE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 2){
            mScannerView = new ZXingScannerView(this);
            barcodescannerview.addView(mScannerView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(com.google.zxing.Result rawResult) {

        String Scannedbarcode = rawResult.getText();
        mScannerView.stopCameraPreview();
        EnableLoading();
        check_parcel_to_scan(Scannedbarcode);
       // mScannerView.resumeCameraPreview(this);


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
    }
    public void load_parcels_to_scan(){
        int size = 0;
          List<Parcel> parcels=   Databackbone.getinstance().parcels.get(Databackbone.getinstance().pickup_to_process).getParcels();
            for(int i =0 ; i < parcels.size();i++){
                if(!parcels.get(i).getScanned()){
                    size++;
                }
            }

          tx_parcels_to_scan.setText(Integer.toString(size)+ " Parcels to scan");
        if(size == 0)
            activity_barcode_scanner.this.finish();

    }
    public void check_parcel_to_scan(String id){
       Boolean check = false;
        List<Parcel> parcels=   Databackbone.getinstance().parcels.get(Databackbone.getinstance().pickup_to_process).getParcels();
        for(int i =0 ; i < parcels.size();i++){
            if(parcels.get(i).getParcelId().equals(id)){
                check = true;
                break;
            }
        }
        if(!check){
            Databackbone.getinstance().showAlsertBox(this,"Error","Parcel not found");
            DisableLoading();
        }
        else
        {
            send_request_to_server(id);
        }

    }
    public void send_request_to_server(final String id){

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Databackbone.getinstance().Base_URL).addConverterFactory(GsonConverterFactory.create()).build();
        swift_api riderapi = retrofit.create(swift_api.class);

        Call<List<DeliveryParcel>> call = riderapi.scanParcels(Databackbone.getinstance().rider.getId(),(id),new parcel_scan(Databackbone.getinstance().parcels.get(Databackbone.getinstance().pickup_to_process).getTaskId(),Databackbone.getinstance().rider.getUserId()));
        call.enqueue(new Callback<List<DeliveryParcel>>() {
            @Override
            public void onResponse(Call<List<DeliveryParcel>> call, Response<List<DeliveryParcel>> response) {
                if(response.isSuccessful()){
                    List<DeliveryParcel> parcels = response.body();

                    Databackbone.getinstance().parcels = parcels;

                    Scan_successfull(id);


                }
                else{
                    DisableLoading();
                    //DeactivateRider();
                    Databackbone.getinstance().showAlsertBox(activity_barcode_scanner.this,"Error","QRcode Not Found Error Code 37");
                }

            }

            @Override
            public void onFailure(Call<List<DeliveryParcel>> call, Throwable t) {
                System.out.println(t.getCause());
                DisableLoading();
                Databackbone.getinstance().showAlsertBox(activity_barcode_scanner.this,"Error","Barcode Not Found Error Code 38");


            }
        });

    }
    public void Scan_successfull(String id){
        scanparceldone(id);
        load_parcels_to_scan();
        DisableLoading();
    }
    public void DisableLoading(){
        btn_add_parcel.setEnabled(true);
        btn_refreash.setEnabled(true);
        barcode_remaining_parcels.setClickable(true);

        progressBar.setVisibility(View.GONE);
    }
    public void EnableLoading(){

        btn_add_parcel.setEnabled(false);
        btn_refreash.setEnabled(false);
        barcode_remaining_parcels.setClickable(false);
        progressBar.setVisibility(View.VISIBLE);
    }
}
