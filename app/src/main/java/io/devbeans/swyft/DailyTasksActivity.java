package io.devbeans.swyft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import io.devbeans.swyft.Fragments.ActiveDailyTasks;
import io.devbeans.swyft.Fragments.AllDailyTasks;
import io.devbeans.swyft.adapters.DailyTasksViewPagerAdapter;
import io.swyft.swyft.R;

public class DailyTasksActivity extends AppCompatActivity {

    TabLayout tabLayout;
    private ViewPager viewPager;
    DailyTasksViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_tasks);
        Initialization();
    }

    private void Initialization(){
        viewPager = findViewById(R.id.contain);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager){
        adapter = new DailyTasksViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new AllDailyTasks(),"All");
        adapter.addFragment(new ActiveDailyTasks(),"Active");
        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
    }
}
