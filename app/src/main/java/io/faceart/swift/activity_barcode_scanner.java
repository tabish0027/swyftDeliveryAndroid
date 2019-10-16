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
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.gcacace.signaturepad.views.SignaturePad;

import javax.xml.transform.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class activity_barcode_scanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    LinearLayout barcodescannerview;
    TextView tx_barcode ;
    ConstraintLayout layout_scanned_id;
    ImageView btn_refreash;
    ConstraintLayout barcode_remaining_parcels ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        barcodescannerview =  findViewById(R.id.barcodescannerview);
        tx_barcode = findViewById(R.id.tx_barcode);
        mScannerView = new ZXingScannerView(this);
        btn_refreash = findViewById(R.id.btn_refreash);
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
                mScannerView.resumeCameraPreview(activity_barcode_scanner.this);
                layout_scanned_id.setVisibility(View.INVISIBLE);
            }
        });
        //View child1 = LayoutInflater.from(this).inflate(mScannerView, null);
        layout_scanned_id = findViewById(R.id.constraintLayout5);
        layout_scanned_id.setVisibility(View.INVISIBLE);
        barcodescannerview.addView(mScannerView);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 2);

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

        tx_barcode.setText(Scannedbarcode);
        mScannerView.stopCameraPreview();
       // mScannerView.resumeCameraPreview(this);
        layout_scanned_id.setVisibility(View.VISIBLE);
        this.mScannerView.playSoundEffect(SoundEffectConstants.CLICK);
        scanparcel(Scannedbarcode);

    }
    public void scanparcel(String id){
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


}
