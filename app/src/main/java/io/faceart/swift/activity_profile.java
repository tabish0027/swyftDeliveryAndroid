package io.faceart.swift;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class activity_profile  extends Activity {
    Button btn_today;
    Button btn_month;
    Button btn_week;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btn_month = findViewById(R.id.btn_month);
        btn_today = findViewById(R.id.btn_today);
        btn_week = findViewById(R.id.btn_week);

        btn_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDay();
            }
        });
        btn_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectWeek();
            }
        });

        btn_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectMonth();
            }
        });


    }
    public void SelectDay(){
        btn_today.setBackgroundResource(R.drawable.button_earn_select);
        btn_week.setBackgroundResource(R.drawable.button_earn_unselect);
        btn_month.setBackgroundResource(R.drawable.button_earn_unselect);
        btn_today.setTextColor(Color.parseColor("#221e1f"));
        btn_week.setTextColor(Color.parseColor("#ffffff"));
        btn_month.setTextColor(Color.parseColor("#ffffff"));


    }
    public void SelectMonth(){
        btn_month.setBackgroundResource(R.drawable.button_earn_select);
        btn_week.setBackgroundResource(R.drawable.button_earn_unselect);
        btn_today.setBackgroundResource(R.drawable.button_earn_unselect);
        btn_month.setTextColor(Color.parseColor("#221e1f"));
        btn_week.setTextColor(Color.parseColor("#ffffff"));
        btn_today.setTextColor(Color.parseColor("#ffffff"));
    }
    public void SelectWeek(){
        btn_week.setBackgroundResource(R.drawable.button_earn_select);
        btn_today.setBackgroundResource(R.drawable.button_earn_unselect);
        btn_month.setBackgroundResource(R.drawable.button_earn_unselect);
        btn_week.setTextColor(Color.parseColor("#221e1f"));
        btn_today.setTextColor(Color.parseColor("#ffffff"));
        btn_month.setTextColor(Color.parseColor("#ffffff"));
    }


}

