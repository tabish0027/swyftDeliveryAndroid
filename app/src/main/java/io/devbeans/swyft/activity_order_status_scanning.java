package io.devbeans.swyft;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.devbeans.swyft.adapters.adapter_status_packages_scanning;
import io.swyft.pickup.R;

public class activity_order_status_scanning extends Activity {

    public RecyclerView recyclerView;
    public adapter_status_packages_scanning ad_orders_scanned;
    EditText search_edittext;
    TextView no_data_text, generate_loadsheet, add_more_btn;
    ProgressBar progressBar;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "ScannedList";

    SharedPreferences sharedpreferences_default;
    SharedPreferences.Editor mEditor_default;
    public static final String MyPREFERENCES_default = "MyPrefs";

    List<String> scannedIds = new ArrayList<>();
    Gson gson = new Gson();
    int position = 0;
    int inner_position = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_order_status);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mEditor = sharedpreferences.edit();
        sharedpreferences_default = getSharedPreferences(MyPREFERENCES_default, Context.MODE_PRIVATE);
        mEditor_default = sharedpreferences_default.edit();

        position = Integer.valueOf(getIntent().getStringExtra("position"));
        inner_position = Integer.valueOf(getIntent().getStringExtra("locationPosition"));

        List<String> arrayList = new ArrayList<>();
        String json = sharedpreferences.getString(Databackbone.getinstance().todayassignmentdata.get(position).getVendorId() + Databackbone.getinstance().todayassignmentdata.get(position).getPickupLocations().get(inner_position).getId(), "");
            Type type = new TypeToken<List<String>>() {}.getType();
            arrayList = gson.fromJson(json, type);
            Databackbone.getinstance().scannedParcelsIds = arrayList;
            scannedIds = Databackbone.getinstance().scannedParcelsIds;

        recyclerView= findViewById(R.id.scanned_parcels_recycler);
        search_edittext = findViewById(R.id.search_edittext);
        no_data_text = findViewById(R.id.no_data_text);
        generate_loadsheet = findViewById(R.id.generate_loadsheet);
        add_more_btn = findViewById(R.id.add_more_btn);
        progressBar = findViewById(R.id.url_loading_animation);
        progressBar.setVisibility(View.GONE);

        if (scannedIds != null && !scannedIds.isEmpty()){
            no_data_text.setVisibility(View.GONE);
            search_edittext.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            ad_orders_scanned = new adapter_status_packages_scanning(position, inner_position, scannedIds, this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(ad_orders_scanned);

        }else {
            no_data_text.setVisibility(View.VISIBLE);
            search_edittext.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);

        }

        search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable == null || editable.toString().isEmpty()){
                    ad_orders_scanned = new adapter_status_packages_scanning(position, inner_position, scannedIds, activity_order_status_scanning.this);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity_order_status_scanning.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(ad_orders_scanned);
                }else {
                    ad_orders_scanned.getFilter().filter(editable.toString());
                }
            }
        });

        add_more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        generate_loadsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (scannedIds != null && !scannedIds.isEmpty()){
                    Intent intent = new Intent(activity_order_status_scanning.this, activity_signature_pad.class);
                    intent.putExtra("position", String.valueOf(position));
                    intent.putExtra("locationPosition", String.valueOf(inner_position));
                    startActivity(intent);
                }else {
                    Toast.makeText(activity_order_status_scanning.this, "You haven't scanned any parcel yet!", Toast.LENGTH_LONG).show();
                }


            }
        });

        final ImageView btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}