package io.faceart.swift;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import io.faceart.swift.interface_retrofit.*;
public class activity_login extends AppCompatActivity {

    Button btn_login;
    ProgressBar progressBar = null;
    EditText username , password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login = findViewById(R.id.btn_login);
        progressBar = (ProgressBar)findViewById(R.id.url_loading_animation);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        username.setText("923049494294");
        password.setText("12345");
        Sprite doubleBounce = new DoubleBounce();
        progressBar.setVisibility(View.GONE);
        progressBar.setIndeterminateDrawable(doubleBounce);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(activity_login.this,activity_mapview.class);
                //activity_login.this.startActivity(i);


                Start_login();

    }
    public void Start_login(){
        username.setEnabled(false);
        password.setEnabled(false);
        btn_login.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);


        String username_data = username.getText().toString();
        String password_data = password.getText().toString();

        if(username_data.length() < 4 || password_data.length() < 4)
        {
            Databackbone.getinstance().showAlsertBox(activity_login.this,"Error","user or password too short");
            EnableLogin();
            return;
        }
        login loginCredentials = new login();
        loginCredentials.username = username.getText().toString();
        loginCredentials.password = password.getText().toString();



        Retrofit retrofit = new Retrofit.Builder().baseUrl(Databackbone.getinstance().Base_URL).addConverterFactory(GsonConverterFactory.create()).build();
        swift_api riderapi = retrofit.create(swift_api.class);

        Call<Rider> call = riderapi.getRider(loginCredentials);
        call.enqueue(new Callback<Rider>() {
            @Override
            public void onResponse(Call<Rider> call, Response<Rider> response) {
                if(response.isSuccessful()){

                    Rider rider = response.body();
                    Databackbone.getinstance().rider = rider;

                   // Toast.makeText(activity_login.this,rider.getId(),Toast.LENGTH_LONG).show();
                    Intent i = new Intent(activity_login.this,activity_mapview.class);
                    activity_login.this.startActivity(i);

                }

            }

            @Override
            public void onFailure(Call<Rider> call, Throwable t) {
                System.out.println(t.getCause());
                EnableLogin();
            }
        });

    }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        EnableLogin();

    }

    public void EnableLogin(){
        progressBar.setVisibility(View.GONE);
        username.setEnabled(true);
        password.setEnabled(true);
        btn_login.setEnabled(true);
    }
}
