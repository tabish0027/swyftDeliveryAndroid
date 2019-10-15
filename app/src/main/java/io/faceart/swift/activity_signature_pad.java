package io.faceart.swift;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.gcacace.signaturepad.views.SignaturePad;

public class activity_signature_pad extends AppCompatActivity {

    Button btn_login;
    SignaturePad mSignaturePad;
    ImageView btn_cross;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        btn_cross = findViewById(R.id.btn_cross);
        btn_cross.setVisibility(View.GONE);
        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignaturePad.clear();
                btn_cross.setVisibility(View.GONE);
            }
        });
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
                btn_cross.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
            }
        });
    }
}
