package io.faceart.swift;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import io.faceart.swift.interface_retrofit.*;
public class activity_login extends AppCompatActivity {

    Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(activity_login.this,activity_mapview.class);
                //activity_login.this.startActivity(i);

                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://13.235.240.229:3000/api/Riders/").addConverterFactory(GsonConverterFactory.create()).build();
                swift_api riderapi = retrofit.create(swift_api.class);

                login loginCredentials = new login();
                loginCredentials.username = "923216242785";
                loginCredentials.password = "12345";

                Call<Rider> call = riderapi.getRider(loginCredentials);
                call.enqueue(new Callback<Rider>() {
                    @Override
                    public void onResponse(Call<Rider> call, Response<Rider> response) {
                        if(response.isSuccessful()){

                            Rider rider = response.body();

                            Toast.makeText(activity_login.this,rider.getId(),Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Rider> call, Throwable t) {
                        System.out.println(t.getCause());
                    }
                });

            }
        });


    }
}
