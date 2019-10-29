package io.faceart.swift;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class activity_form extends AppCompatActivity {
    ImageView btn_back;
    TextView tx_name,tx_address,tx_parcel_id,tx_payment_method;
    Button btn_delivered;
    ImageView btn_payment_method;
    Button btn_diclined;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        btn_back = findViewById(R.id.btn_back);
        btn_delivered = findViewById(R.id.btn_delivered);
        btn_payment_method = findViewById(R.id.btn_payment_method);


        btn_diclined = findViewById(R.id.btn_diclined);
        btn_diclined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // using BottomSheetDialogFragment
                bottomsheet_orderdeclined bottomSheetFragment = new bottomsheet_orderdeclined();

                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }

        });
        btn_delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent declined = new Intent(activity_form.this, activity_signature_pad.class);
                declined.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                declined.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity_form.this.startActivity(declined);


            }
        });
        btn_payment_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomsheet_payment_method bottomSheetFragment = new bottomsheet_payment_method();

                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity_form.this.finish();
            }
        });
        tx_name= findViewById(R.id.tx_name);
        tx_address= findViewById(R.id.tx_address);
        tx_parcel_id= findViewById(R.id.tx_parcel_id);
        tx_payment_method= findViewById(R.id.tx_payment_method);
        tx_name.setText(Databackbone.getinstance().parcelsdelivery.get(Databackbone.getinstance().delivery_to_show).getData().get(0).getName());
        tx_address.setText(Databackbone.getinstance().parcelsdelivery.get(Databackbone.getinstance().delivery_to_show).getData().get(0).getLocation().getAddress());
        tx_parcel_id.setText(Databackbone.getinstance().parcelsdelivery.get(Databackbone.getinstance().delivery_to_show).getData().get(0).getParcels().get(0).getParcelId());
        tx_payment_method.setText("Cash");
    }
}
