package io.devbeans.swyft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.devbeans.swyft.adapters.AdapterLoadsheetHistory;
import io.devbeans.swyft.data_models.LoadsheetURLModel;
import io.devbeans.swyft.data_models.RiderIDModel;
import io.devbeans.swyft.data_models.includeModel;
import io.devbeans.swyft.data_models.innerIncludeModel;
import io.devbeans.swyft.data_models.innerScopeModel;
import io.devbeans.swyft.data_models.scopeModel;
import io.devbeans.swyft.interface_retrofit.LoadsheetHistoryModel;
import io.devbeans.swyft.interface_retrofit.RiderDetails;
import io.devbeans.swyft.interface_retrofit.swift_api;
import io.swyft.swyft.R;
import io.swyft.swyft.Splash;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoadsheetHistoryActivity extends AppCompatActivity {

    ImageView btn_back;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView no_data_text;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "MyPrefs";

    List<LoadsheetHistoryModel> loadsheetList;

    Gson gson = new Gson();
    String encoded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadsheet_history);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        mEditor = sharedpreferences.edit();
        Initialization();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getLoadSheetHistory();
    }

    private void Initialization(){
        btn_back = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.loadsheet_recycler);
        progressBar = findViewById(R.id.url_loading_animation);
        no_data_text = findViewById(R.id.no_data_text);
    }

    private void EnableProgress(){
        progressBar.setVisibility(View.VISIBLE);
        no_data_text.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    private void DisableProgress(){
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void getLoadSheetHistory() {
        EnableProgress();
        Retrofit retrofit = Databackbone.getinstance().getRetrofitbuilder();
        swift_api riderapi = retrofit.create(swift_api.class);

        LoadsheetURLModel loadsheetURLModel = new LoadsheetURLModel();

        RiderIDModel riderIDModel = new RiderIDModel();
        riderIDModel.setRiderId(sharedpreferences.getString("RiderID", ""));

        List<String> innerscopefield = new ArrayList<>();
        innerscopefield.add("name");

        innerScopeModel innerScopemodel = new innerScopeModel();
        innerScopemodel.setFields(innerscopefield);

        innerIncludeModel innerIncludemodel = new innerIncludeModel();
        innerIncludemodel.setRelation("vendor");
        innerIncludemodel.setScope(innerScopemodel);

        List<String> scopefield = new ArrayList<>();
        scopefield.add("address");
        scopefield.add("vendorId");

        scopeModel scopemodel = new scopeModel();
        scopemodel.setFields(scopefield);
        scopemodel.setInclude(innerIncludemodel);

        includeModel includemodel = new includeModel();
        includemodel.setRelation("pickupLocation");
        includemodel.setScope(scopemodel);

        loadsheetURLModel.setWhere(riderIDModel);
        loadsheetURLModel.setOrder("createdAt DESC");
        loadsheetURLModel.setInclude(includemodel);

        String jsonP = gson.toJson(loadsheetURLModel);

        try
        {
            encoded = Uri.encode(jsonP);
            Log.e("encodedString", encoded);
        }
        catch(Exception e){}

        Call<List<LoadsheetHistoryModel>> call = riderapi.getLoadsheetHistory(sharedpreferences.getString("AccessToken", ""), sharedpreferences.getString("RiderID", ""),"vendor", "createdAt DESC");
        call.enqueue(new Callback<List<LoadsheetHistoryModel>>() {
            @Override
            public void onResponse(Call<List<LoadsheetHistoryModel>> call, Response<List<LoadsheetHistoryModel>> response) {
                if (response.isSuccessful()) {

                    loadsheetList = response.body();
                    Databackbone.getinstance().loadSheetHistoryList = loadsheetList;

                    if (loadsheetList.isEmpty()){
                        no_data_text.setVisibility(View.VISIBLE);
                    }else {
                        AdapterLoadsheetHistory adapterLoadsheetHistory = new AdapterLoadsheetHistory(LoadsheetHistoryActivity.this, loadsheetList, new AdapterLoadsheetHistory.CustomItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {

                                Intent intent = new Intent(LoadsheetHistoryActivity.this, ViewLoadsheetActivity.class);
                                intent.putExtra("position", String.valueOf(position));
                                startActivity(intent);

                            }
                        });
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LoadsheetHistoryActivity.this);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapterLoadsheetHistory);
                    }


                    DisableProgress();
                } else {
                    if (response.code() == 401) {
                        DisableProgress();
                        //sharedpreferences must be removed
                        mEditor.clear().commit();
                        Intent intent = new Intent(LoadsheetHistoryActivity.this, activity_login.class);
                        startActivity(intent);
                        finishAffinity();
                    }else {
                        DisableProgress();
                        Databackbone.getinstance().showAlsertBox(LoadsheetHistoryActivity.this, getResources().getString(R.string.error), "Error Connecting To Server Error Code 33");
                    }
                }

            }

            @Override
            public void onFailure(Call<List<LoadsheetHistoryModel>> call, Throwable t) {
                System.out.println(t.getCause());
                Databackbone.getinstance().showAlsertBox(LoadsheetHistoryActivity.this, getResources().getString(R.string.error), "Error Connecting To Server Error Code 34");
                DisableProgress();
                //DeactivateRider();
            }
        });
    }

}