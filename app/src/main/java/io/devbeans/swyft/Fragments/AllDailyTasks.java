package io.devbeans.swyft.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import io.devbeans.swyft.Databackbone;
import io.devbeans.swyft.activity_login;
import io.devbeans.swyft.adapters.AdapterAllDaliyTasks;
import io.devbeans.swyft.interface_retrofit.TodayAssignments;
import io.devbeans.swyft.interface_retrofit.swift_api;
import io.swyft.pickup.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class AllDailyTasks extends Fragment {


    public static RecyclerView recyclerView;
    public static AdapterAllDaliyTasks adapterAllDaliyTasks;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar = null;
    TextView tx_empty_view;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences_parcels;
    SharedPreferences.Editor mEditor_parcels;
    public static final String MyPREFERENCES_parcels = "ScannedList";

    public AllDailyTasks() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_daily_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        mEditor = sharedpreferences.edit();
        sharedpreferences_parcels = getActivity().getSharedPreferences(MyPREFERENCES_parcels, MODE_PRIVATE);
        mEditor_parcels = sharedpreferences_parcels.edit();
        swipeRefreshLayout = view.findViewById(R.id.all_parcels_swipe);
        recyclerView = view.findViewById(R.id.all_parcels_recycler);
        progressBar = view.findViewById(R.id.url_loading_animation);
        tx_empty_view = view.findViewById(R.id.tx_empty_view);
        progressBar.setVisibility(View.GONE);
        tx_empty_view.setVisibility(View.GONE);
//        getTodayAssignments();

        EnableLoading();

        adapterAllDaliyTasks = new AdapterAllDaliyTasks(false, getActivity(), Databackbone.getinstance().todayassignments.getData(), new AdapterAllDaliyTasks.CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterAllDaliyTasks);

        DisableLoading();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                getTodayAssignments();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void getTodayAssignments() {
        EnableLoading();
        Retrofit retrofit = Databackbone.getinstance().getRetrofitbuilder();
        swift_api todayAssignment = retrofit.create(swift_api.class);

        Call<TodayAssignments> call = todayAssignment.getTodayAssignment(sharedpreferences.getString("AccessToken", ""), sharedpreferences.getString("RiderID", ""));
        call.enqueue(new Callback<TodayAssignments>() {
            @Override
            public void onResponse(Call<TodayAssignments> call, Response<TodayAssignments> response) {
                if (response.isSuccessful()) {

                    TodayAssignments todayAssignments = response.body();
                    Databackbone.getinstance().todayassignments = todayAssignments;

                    AdapterAllDaliyTasks adapterAllDaliyTasks = new AdapterAllDaliyTasks(false, getActivity(), Databackbone.getinstance().todayassignments.getData(), new AdapterAllDaliyTasks.CustomItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {

                        }
                    });

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(adapterAllDaliyTasks);

                    DisableLoading();

                } else {
                    if (response.code() == 401) {
                        DisableLoading();
                        //sharedpreferences must be removed
                        mEditor.clear().commit();
                        mEditor_parcels.clear().commit();
                        Intent intent = new Intent(getActivity(), activity_login.class);
                        startActivity(intent);
                        getActivity().finishAffinity();
                    }else {
                        DisableLoading();
                        Databackbone.getinstance().showAlsertBox(getActivity(), getResources().getString(R.string.error), "Error Connecting To Server Error Code 33");
                    }
                    //DeactivateRider();
                }

            }

            @Override
            public void onFailure(Call<TodayAssignments> call, Throwable t) {
                System.out.println(t.getCause());
                DisableLoading();
                Databackbone.getinstance().showAlsertBox(getActivity(), getResources().getString(R.string.error), "Error Connecting To Server Error Code 34");

                //DeactivateRider();
            }
        });
    }


    public void DisableLoading(){

        swipeRefreshLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
    public void EnableLoading(){
        swipeRefreshLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.VISIBLE);
    }
}
