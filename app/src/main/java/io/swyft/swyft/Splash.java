package io.swyft.swyft;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import io.devbeans.swyft.Databackbone;
import io.swyft.swyft.R;
import io.devbeans.swyft.activity_login;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Databackbone.getinstance().contextapp = getApplicationContext();
        Context con = Databackbone.getinstance().contextapp;
        Thread thr= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    Intent i = new Intent(Splash.this, activity_login.class);
                    Splash.this.startActivity(i);
                }catch (Exception i){

                }
            }
        });
        thr.start();

    }
}
