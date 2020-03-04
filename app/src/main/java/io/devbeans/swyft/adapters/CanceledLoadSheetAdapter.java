package io.devbeans.swyft.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.devbeans.swyft.Databackbone;
import io.devbeans.swyft.LoadsheetHistoryActivity;
import io.devbeans.swyft.activity_signature_pad;
import io.devbeans.swyft.data_models.LoadSheetModel;
import io.devbeans.swyft.interface_retrofit.LoadsheetHistoryModel;
import io.devbeans.swyft.interface_retrofit.PasswordResetRequest;
import io.devbeans.swyft.interface_retrofit.swift_api;
import io.swyft.swyft.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CanceledLoadSheetAdapter extends RecyclerView.Adapter<CanceledLoadSheetAdapter.MyViewHolder> {
    private Context context;
    CustomItemClickListener listener;
    List<LoadSheetModel> list;

    SharedPreferences sharedpreferences_default;
    SharedPreferences.Editor mEditor_default;
    public static final String MyPREFERENCES_default = "MyPrefs";

    public CanceledLoadSheetAdapter(Context context, List<LoadSheetModel> home_list, CustomItemClickListener listener) {
        this.list = home_list;
        this.context = context;
        this.listener = listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.adapter_loadsheet_history, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);

        sharedpreferences_default = context.getSharedPreferences(MyPREFERENCES_default, Context.MODE_PRIVATE);
        mEditor_default = sharedpreferences_default.edit();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.loadsheet_id_text.setText(list.get(position).getVendorId());
        holder.loadsheet_vendorname_text.setText(list.get(position).getName());
        holder.loadsheet_totalparcels_text.setText(String.valueOf(list.get(position).getParcelIds().size()) + " Parcels");

        holder.imageView.setImageResource(R.drawable.ic_sync);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadSheet(position);
            }
        });

    }

    public void loadSheet(int position) {

        swift_api riderapi = Databackbone.getinstance().getRetrofitbuilder().create(swift_api.class);

        Call<PasswordResetRequest> call = riderapi.generateLoadsheet(sharedpreferences_default.getString("AccessToken", ""), (sharedpreferences_default.getString("RiderID", "")), list.get(position));
        call.enqueue(new Callback<PasswordResetRequest>() {
            @Override
            public void onResponse(Call<PasswordResetRequest> call, Response<PasswordResetRequest> response) {
                if (response.isSuccessful()) {



                } else {



                }

            }

            @Override
            public void onFailure(Call<PasswordResetRequest> call, Throwable t) {
                System.out.println(t.getCause());

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView loadsheet_id_text, loadsheet_vendorname_text, loadsheet_totalparcels_text;
        ImageView imageView;

        MyViewHolder(View itemView) {
            super(itemView);
            imageView =itemView.findViewById(R.id.completion_icon);
            loadsheet_id_text =itemView.findViewById(R.id.loadsheet_id_text);
            loadsheet_vendorname_text =itemView.findViewById(R.id.loadsheet_vendorname_text);
            loadsheet_totalparcels_text =itemView.findViewById(R.id.loadsheet_totalparcels_text);

        }
    }

    public interface CustomItemClickListener {
        void onItemClick(View v, int position);
    }
}