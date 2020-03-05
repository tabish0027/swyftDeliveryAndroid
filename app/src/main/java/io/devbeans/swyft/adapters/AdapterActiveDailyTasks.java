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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import io.devbeans.swyft.Databackbone;
import io.devbeans.swyft.interface_retrofit.ActiveAssignment;
import io.devbeans.swyft.interface_retrofit.TodayAssignmentData;
import io.swyft.swyft.R;

public class AdapterActiveDailyTasks extends RecyclerView.Adapter<AdapterActiveDailyTasks.MyViewHolder> implements Filterable {
    private Context context;
    CustomItemClickListener listener;
    List<ActiveAssignment> list = new ArrayList<>();
    List<ActiveAssignment> filtered = new ArrayList<>();
    boolean status;

    public AdapterActiveDailyTasks(boolean Status, Context context, List<ActiveAssignment> home_list, CustomItemClickListener listener) {
        this.list = home_list;
        this.filtered = home_list;
        this.context = context;
        this.listener = listener;
        this.status = Status;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.row_item_today_package_order, parent, false);
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

        holder.mb_name.setText(filtered.get(position).getVendorName());
        holder.mb_address.setText(filtered.get(position).getAddress());

        if (status) {
            holder.mb_parcel_type.setImageResource(R.drawable.icon_circle_deliverd);
            holder.parcel_type_bottom_bar.setBackgroundColor(Color.parseColor("#90703090"));
            holder.mb_parcel_type_background.setBackgroundResource(R.drawable.new_daily_task_background);
            if (!Databackbone.getinstance().riderdetails.getType().equalsIgnoreCase("delivery")) {
                holder.btn_navigation.setVisibility(View.VISIBLE);
                holder.btn_navigation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new AlertDialog.Builder(context)
                                .setTitle("Navigation Request")
                                .setMessage("Activate Navigation for " + filtered.get(position).getVendorName())

                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        Offlice_Activity(new LatLng(filtered.get(position).getGeopoints().getLat(), filtered.get(position).getGeopoints().getLng()));

                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })

                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
            } else {
                holder.btn_navigation.setVisibility(View.INVISIBLE);
            }


        } else {
            holder.mb_parcel_type.setImageResource(R.drawable.icon_circle_reattempt);
            holder.parcel_type_bottom_bar.setBackgroundColor(Color.parseColor("#90f15b22"));
            holder.down_colored_relative.setBackgroundResource(R.drawable.dailly_task_down_half_filled_deactivate);
            holder.mb_parcel_type_background.setBackgroundResource(R.drawable.round_daily_package_deactive);
            holder.btn_navigation.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        if (filtered != null){
            return filtered.size();
        }else {
            return 0;
        }
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = list.size();
                    filterResults.values = list;

                }else{
                    List<ActiveAssignment> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(int i = 0; i < list.size(); i++){
                        String vendor_name = list.get(i).getVendorName().toLowerCase();
                        if(vendor_name.contains(searchStr)){
                            resultsModel.add(list.get(i));
                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }


                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filtered = (List<ActiveAssignment>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mb_name ;
        TextView mb_address ;
        TextView mb_zone ;
        ImageView mb_parcel_type;
        LinearLayout parcel_type_bottom_bar;
        LinearLayout mb_parcel_type_background;
        ImageView btn_navigation;
        LinearLayout down_colored_relative;

        MyViewHolder(View itemView) {
            super(itemView);
            mb_name =itemView.findViewById(R.id.mb_name); ;
            mb_address =itemView.findViewById(R.id.mb_address);
            mb_zone =itemView.findViewById(R.id.mb_zone);
            mb_parcel_type =itemView.findViewById(R.id.parcel_type);
            parcel_type_bottom_bar =itemView.findViewById(R.id.parcel_type_bottom_bar);
            mb_parcel_type_background =itemView.findViewById(R.id.mb_parcel_type_background);
            btn_navigation =itemView.findViewById(R.id.btn_navigation);

            down_colored_relative =itemView.findViewById(R.id.down_colored_relative);
        }
    }

    public void Offlice_Activity(LatLng location) {
        String location_to_string = Double.toString(location.latitude) + "," + Double.toString(location.longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=" + location_to_string));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.getApplicationContext().startActivity(intent);
    }

    public interface CustomItemClickListener {
        void onItemClick(View v, int position);
    }
}