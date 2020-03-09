package io.devbeans.swyft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.devbeans.swyft.adapters.AdapterLoadsheetHistory;
import io.devbeans.swyft.adapters.CanceledLoadSheetAdapter;
import io.devbeans.swyft.data_models.LoadSheetModel;
import io.devbeans.swyft.interface_retrofit.LoadsheetHistoryModel;
import io.devbeans.swyft.interface_retrofit.swift_api;
import io.swyft.pickup.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoadsheetHistoryActivity extends AppCompatActivity {

    ImageView btn_back;
    RecyclerView recyclerView, canceled_recyclerView;
    ProgressBar progressBar;
    public static LinearLayout canceled_headings;
    TextView no_data_text, succeed_textView;
    public static TextView canceled_textView;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "MyPrefs";

    SharedPreferences sharedpreferences_loadsheet;
    SharedPreferences.Editor mEditor_loadsheet;
    public static final String MyPREFERENCES_loadsheet = "LoadSheet";

    List<LoadsheetHistoryModel> loadsheetList;
    List<LoadSheetModel> savedLoadsheets = new ArrayList<>();

    Gson gson = new Gson();
    String encoded;

    @Override
    public void onBackPressed() {
        if (getIntent().getStringExtra("activity").equals("home")){
            finish();
        }else {
            Intent intent = new Intent(LoadsheetHistoryActivity.this, activity_mapview.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadsheet_history);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        mEditor = sharedpreferences.edit();
        sharedpreferences_loadsheet = getSharedPreferences(MyPREFERENCES_loadsheet, Context.MODE_PRIVATE);
        mEditor_loadsheet = sharedpreferences_loadsheet.edit();
        Initialization();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getStringExtra("activity").equals("home")){
                    finish();
                }else {
                    Intent intent = new Intent(LoadsheetHistoryActivity.this, activity_mapview.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        getLoadSheetHistory();
    }

    private void Initialization(){
        btn_back = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.loadsheet_recycler);
        canceled_recyclerView = findViewById(R.id.canceled_loadsheet_recycler);
        succeed_textView = findViewById(R.id.succeed_textView);
        canceled_textView = findViewById(R.id.canceled_textView);
        canceled_headings = findViewById(R.id.canceled_headings);
        progressBar = findViewById(R.id.url_loading_animation);
        no_data_text = findViewById(R.id.no_data_text);

        List<LoadSheetModel> arrayList_loadsheet = new ArrayList<>();
        String json_loadsheet = sharedpreferences_loadsheet.getString("PendingLoadsheet", "");

        if (json_loadsheet != null){
            if (!json_loadsheet.equals("")){
                Type type_loadsheet = new TypeToken<List<LoadSheetModel>>() {}.getType();
                arrayList_loadsheet = gson.fromJson(json_loadsheet, type_loadsheet);
                savedLoadsheets = arrayList_loadsheet;
                canceled_textView.setVisibility(View.VISIBLE);
                canceled_headings.setVisibility(View.VISIBLE);
                canceled_recyclerView.setVisibility(View.VISIBLE);
            }else {
                canceled_textView.setVisibility(View.GONE);
                canceled_headings.setVisibility(View.GONE);
                canceled_recyclerView.setVisibility(View.GONE);
            }
        }else {
            canceled_textView.setVisibility(View.GONE);
            canceled_headings.setVisibility(View.GONE);
            canceled_recyclerView.setVisibility(View.GONE);
        }
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

        /*LoadsheetURLModel loadsheetURLModel = new LoadsheetURLModel();

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
        catch(Exception e){}*/

        Call<List<LoadsheetHistoryModel>> call = riderapi.getLoadsheetHistory(sharedpreferences.getString("AccessToken", ""), sharedpreferences.getString("RiderID", ""),"vendor", "createdAt DESC");
        call.enqueue(new Callback<List<LoadsheetHistoryModel>>() {
            @Override
            public void onResponse(Call<List<LoadsheetHistoryModel>> call, Response<List<LoadsheetHistoryModel>> response) {
                if (response.isSuccessful()) {

                    loadsheetList = response.body();
                    Databackbone.getinstance().loadSheetHistoryList = loadsheetList;

                    if (loadsheetList.isEmpty() && savedLoadsheets.isEmpty()){
                        no_data_text.setVisibility(View.VISIBLE);
                        succeed_textView.setVisibility(View.GONE);
                        canceled_textView.setVisibility(View.GONE);
                        canceled_headings.setVisibility(View.GONE);
                    }else {
                        if (loadsheetList.isEmpty()){
                            succeed_textView.setVisibility(View.GONE);
                        }else {
                            succeed_textView.setVisibility(View.VISIBLE);
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
                            if (savedLoadsheets.isEmpty()){
                                canceled_textView.setVisibility(View.GONE);
                                canceled_headings.setVisibility(View.GONE);
                            }else {
                                canceled_textView.setVisibility(View.VISIBLE);
                                canceled_headings.setVisibility(View.VISIBLE);
                                canceled_recyclerView.setVisibility(View.VISIBLE);

                                CanceledLoadSheetAdapter canceledLoadSheetAdapter = new CanceledLoadSheetAdapter(LoadsheetHistoryActivity.this, savedLoadsheets, new CanceledLoadSheetAdapter.CustomItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int position) {

                                    }
                                });

                                LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(LoadsheetHistoryActivity.this);

                                canceled_recyclerView.setLayoutManager(linearLayoutManager1);
                                canceled_recyclerView.setAdapter(canceledLoadSheetAdapter);

                            }
                        }
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