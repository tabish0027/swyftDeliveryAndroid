package io.devbeans.swyft.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import io.devbeans.swyft.Databackbone;
import io.devbeans.swyft.interface_retrofit.LoadsheetHistoryModel;
import io.devbeans.swyft.interface_retrofit.Location;
import io.devbeans.swyft.interface_retrofit.TodayAssignmentData;
import io.swyft.swyft.R;

public class AdapterLoadsheetHistory extends RecyclerView.Adapter<AdapterLoadsheetHistory.MyViewHolder> {
    private Context context;
    CustomItemClickListener listener;
    List<LoadsheetHistoryModel> list;

    public AdapterLoadsheetHistory(Context context, List<LoadsheetHistoryModel> home_list, CustomItemClickListener listener) {
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

        holder.loadsheet_id_text.setText(list.get(position).getId());
        holder.loadsheet_vendorname_text.setText(list.get(position).getPickupLocation().getVendor().getName());
        holder.loadsheet_totalparcels_text.setText(String.valueOf(list.get(position).getParcelIds().size()) + " Parcels");

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