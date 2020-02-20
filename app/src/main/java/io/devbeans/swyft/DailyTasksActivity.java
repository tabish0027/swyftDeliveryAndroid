package io.devbeans.swyft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import io.devbeans.swyft.Fragments.ActiveDailyTasks;
import io.devbeans.swyft.Fragments.AllDailyTasks;
import io.devbeans.swyft.adapters.DailyTasksViewPagerAdapter;
import io.devbeans.swyft.interface_retrofit.TodayAssignments;
import io.devbeans.swyft.interface_retrofit.swift_api;
import io.swyft.swyft.R;
import io.swyft.swyft.Splash;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DailyTasksActivity extends AppCompatActivity {

    TabLayout tabLayout;
    private ViewPager viewPager;
    DailyTasksViewPagerAdapter adapter;
    ImageView btn_back;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_tasks);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        mEditor = sharedpreferences.edit();
        Initialization();
    }

    private void Initialization(){
        viewPager = findViewById(R.id.contain);
        btn_back = findViewById(R.id.btn_back);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager){
        adapter = new DailyTasksViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new AllDailyTasks(),"All");
        adapter.addFragment(new ActiveDailyTasks(),"Active");
        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
    }
}
