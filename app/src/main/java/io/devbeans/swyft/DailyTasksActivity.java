package io.devbeans.swyft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import io.devbeans.swyft.Fragments.ActiveDailyTasks;
import io.devbeans.swyft.Fragments.AllDailyTasks;
import io.devbeans.swyft.adapters.AdapterActiveDailyTasks;
import io.devbeans.swyft.adapters.AdapterAllDaliyTasks;
import io.devbeans.swyft.adapters.DailyTasksViewPagerAdapter;
import io.swyft.pickup.R;

public class DailyTasksActivity extends AppCompatActivity {

    TabLayout tabLayout;
    private ViewPager viewPager;
    DailyTasksViewPagerAdapter adapter;
    ImageView btn_back, hide_search, search_image_btn;
    EditText search_editfield;
    TextView title;

    int sending_position_main = 0;
    int sending_position_location = 0;

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
        hide_search = findViewById(R.id.hide_search);
        btn_back = findViewById(R.id.btn_back);
        title = findViewById(R.id.title);
        search_image_btn = findViewById(R.id.search_image_btn);
        search_editfield = findViewById(R.id.search_editfield);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        search_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setVisibility(View.GONE);
                search_editfield.setVisibility(View.VISIBLE);
                search_editfield.setText("");
                btn_back.setVisibility(View.GONE);
                hide_search.setVisibility(View.VISIBLE);
                search_editfield.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(search_editfield, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        hide_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setVisibility(View.VISIBLE);
                search_editfield.setVisibility(View.GONE);
                search_editfield.setText("");
                btn_back.setVisibility(View.VISIBLE);
                hide_search.setVisibility(View.GONE);
                hideKeyboard(view);
            }
        });

        search_editfield.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable == null || editable.toString().isEmpty()){
                    AllDailyTasks.adapterAllDaliyTasks = new AdapterAllDaliyTasks(false, DailyTasksActivity.this, Databackbone.getinstance().todayassignments.getData(), new AdapterAllDaliyTasks.CustomItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {

                        }
                    });

                    if (ActiveDailyTasks.active_items_merged != null){
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DailyTasksActivity.this);
                        AllDailyTasks.recyclerView.setLayoutManager(linearLayoutManager);
                        AllDailyTasks.recyclerView.setAdapter(AllDailyTasks.adapterAllDaliyTasks);

                        ActiveDailyTasks.adapterAllDaliyTasks = new AdapterActiveDailyTasks(true, DailyTasksActivity.this, ActiveDailyTasks.active_items_merged, new AdapterActiveDailyTasks.CustomItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position) {
                                for (int i = 0; i < Databackbone.getinstance().todayassignmentdata.size(); i++){
                                    for (int j = 0; j < Databackbone.getinstance().todayassignmentdata.get(i).getPickupLocations().size(); j++){
                                        if (ActiveDailyTasks.active_items_merged.get(position).getVendorId().equals(Databackbone.getinstance().todayassignmentdata.get(i).getVendorId()) && ActiveDailyTasks.active_items_merged.get(position).getPickupLocationId().equals(Databackbone.getinstance().todayassignmentdata.get(i).getPickupLocations().get(j).getId())){
                                            sending_position_main = i;
                                            sending_position_location = j;
                                        }
                                    }
                                }

                                Intent orders = new Intent(DailyTasksActivity.this, BarCodeScannerActivity.class);
                                orders.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                orders.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                orders.putExtra("position", String.valueOf(sending_position_main));
                                orders.putExtra("locationPosition", String.valueOf(sending_position_location));
                                startActivity(orders);
                            }
                        });

                        LinearLayoutManager linearLayoutManageractive = new LinearLayoutManager(DailyTasksActivity.this);
                        ActiveDailyTasks.recyclerView.setLayoutManager(linearLayoutManageractive);
                        ActiveDailyTasks.recyclerView.setAdapter(ActiveDailyTasks.adapterAllDaliyTasks);
                    }
                }else {
                    AllDailyTasks.adapterAllDaliyTasks.getFilter().filter(editable.toString());
                    ActiveDailyTasks.adapterAllDaliyTasks.getFilter().filter(editable.toString());
                }
            }
        });

    }

    protected void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) DailyTasksActivity.this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isAcceptingText()){
        InputMethodManager in = (InputMethodManager) DailyTasksActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
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
