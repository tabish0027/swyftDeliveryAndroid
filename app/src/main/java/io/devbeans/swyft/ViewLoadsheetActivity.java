package io.devbeans.swyft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.List;

import io.devbeans.swyft.adapters.AdapterViewLoadsheet;
import io.devbeans.swyft.interface_retrofit.LoadsheetHistoryModel;
import io.swyft.pickup.R;

public class ViewLoadsheetActivity extends AppCompatActivity {

    TextView vendor_name, parcel_quantity, address_text, loadsheet_id;
    ImageView navigation, sig_img, sheet_img, btn_back;
    RecyclerView parcels_recycler;
    LinearLayout scan_parcels, navigation_layout;
    int position;
    List<LoadsheetHistoryModel> list = new ArrayList<>();
    private ArrayList<String> sign_image_viewerArray = new ArrayList<>();
    private ArrayList<String> sheet_image_viewerArray = new ArrayList<>();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_loadsheet);
        Fresco.initialize(ViewLoadsheetActivity.this);
        Initializations();
        Actions();
    }

    private void Initializations(){
        vendor_name = findViewById(R.id.vendor_name);
        parcel_quantity = findViewById(R.id.parcel_quantity);
        address_text = findViewById(R.id.address_text);
        loadsheet_id = findViewById(R.id.loadsheet_id);
        navigation = findViewById(R.id.navigation);
        sig_img = findViewById(R.id.sig_img);
        sheet_img = findViewById(R.id.sheet_img);
        btn_back = findViewById(R.id.btn_back);
        parcels_recycler = findViewById(R.id.parcels_recycler);
        scan_parcels = findViewById(R.id.scan_parcels);
        navigation_layout = findViewById(R.id.navigation_layout);

        position = Integer.valueOf(getIntent().getStringExtra("position"));
        list = Databackbone.getinstance().loadSheetHistoryList;
    }

    private void Actions(){

        vendor_name.setText(list.get(position).getPickupLocation().getVendor().getName());
        parcel_quantity.setText(String.valueOf(list.get(position).getParcelIds().size()) + " Parcels");
        address_text.setText(list.get(position).getPickupLocation().getAddress());
        loadsheet_id.setText(list.get(position).getId());

        AdapterViewLoadsheet adapterViewLoadsheet = new AdapterViewLoadsheet(ViewLoadsheetActivity.this, list.get(position).getParcelIds(), new AdapterViewLoadsheet.CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewLoadsheetActivity.this);
        parcels_recycler.setLayoutManager(linearLayoutManager);
        parcels_recycler.setAdapter(adapterViewLoadsheet);

        scan_parcels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewLoadsheetActivity.this, activity_mapview.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        Picasso.with(ViewLoadsheetActivity.this).load(list.get(position).getSignatureUrl()).into(sig_img);
        if (list.get(position).getPickupSheetUrl() != null && !list.get(position).getPickupSheetUrl().isEmpty()){
            sheet_img.setVisibility(View.VISIBLE);
            Picasso.with(ViewLoadsheetActivity.this).load(list.get(position).getPickupSheetUrl()).into(sheet_img);
        }else {
            sheet_img.setVisibility(View.GONE);
        }


        sign_image_viewerArray.clear();
        sheet_image_viewerArray.clear();
        sign_image_viewerArray.add(list.get(position).getSignatureUrl());
        sheet_image_viewerArray.add(list.get(position).getPickupSheetUrl());

        sig_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ImageViewer.Builder(ViewLoadsheetActivity.this, sign_image_viewerArray)
                        .setStartPosition(0)
                        .show();
            }
        });

        sheet_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ImageViewer.Builder(ViewLoadsheetActivity.this, sheet_image_viewerArray)
                        .setStartPosition(0)
                        .show();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        navigation_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Offlice_Activity(new LatLng(list.get(position).getGeopoints().getLat(), list.get(position).getGeopoints().getLng()));
            }
        });


    }

    public void Offlice_Activity(LatLng location){
        String location_to_string = Double.toString(location.latitude) + ","+Double.toString(location.longitude);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr="+location_to_string));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }
}
