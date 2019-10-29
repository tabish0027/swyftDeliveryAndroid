package io.faceart.swift;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class bottomsheet_orderdeclined extends BottomSheetDialogFragment {

    ConstraintLayout btn_order_canceled,btn_order_diclined;
    ImageView btn_close;
    public bottomsheet_orderdeclined() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottomsheet_orderdeclined, container, false);
        btn_order_canceled = v.findViewById(R.id.btn_order_canceled);
        btn_order_diclined = v.findViewById(R.id.btn_order_declined);
        btn_close = v.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomsheet_orderdeclined.this.dismiss();
            }
        });
        btn_order_canceled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent  declined = new Intent(bottomsheet_orderdeclined.this.getActivity(), activity_delivery_status.class);
                declined.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                declined.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                getActivity().startActivity(declined);
                bottomsheet_orderdeclined.this.dismiss();
            }
        });
        btn_order_diclined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomsheet_reattempt bottomSheetFragment = new bottomsheet_reattempt();

                bottomSheetFragment.show(getActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());
                bottomsheet_orderdeclined.this.dismiss();
            }
        });
        return v;
    }
}
