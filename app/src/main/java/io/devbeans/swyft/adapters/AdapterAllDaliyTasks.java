package io.devbeans.swyft.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.devbeans.swyft.BarCodeScannerActivity;
import io.devbeans.swyft.Databackbone;
import io.devbeans.swyft.interface_retrofit.TodayAssignmentData;
import io.swyft.swyft.R;

public class AdapterAllDaliyTasks extends RecyclerView.Adapter<AdapterAllDaliyTasks.MyViewHolder> implements Filterable {
    private Context context;
    CustomItemClickListener listener;
    List<TodayAssignmentData> list = new ArrayList<>();
    List<TodayAssignmentData> filtered = new ArrayList<>();
    boolean status;
    Gson gson = new Gson();
    int sending_position = 0;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "ScannedList";

    public AdapterAllDaliyTasks(boolean Status, Context context, List<TodayAssignmentData> home_list, CustomItemClickListener listener) {
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
        view = inflater.inflate(R.layout.adapter_daily_tasks, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mEditor = sharedpreferences.edit();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        AdapterDailyTasks adapterDailyTasks = new AdapterDailyTasks(position, status, context, filtered.get(position).getPickupLocations(), filtered, new AdapterDailyTasks.CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int i) {

                if (!Databackbone.getinstance().riderdetails.getType().equals("delivery")){
                    if (filtered.get(position).getParcels().isEmpty()){
                        Databackbone.getinstance().showAlsertBox(context, context.getResources().getString(R.string.error), "No Parcel found");
                    }else {
                        Databackbone.getinstance().task_to_show = Databackbone.getinstance().todayassignments.getData().get(position).getVendorId();

                        for (int j = 0; j < list.size(); j++){

                            if (filtered.get(position).getVendorName().equals(list.get(j).getVendorName())){
                                sending_position = j;
                                break;
                            }

                        }

                        Intent orders = new Intent(context, BarCodeScannerActivity.class);
                        orders.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        orders.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        orders.putExtra("position", String.valueOf(sending_position));
                        orders.putExtra("locationPosition", String.valueOf(i));
                        context.startActivity(orders);

                    }

                }

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        holder.recyclerView.setLayoutManager(linearLayoutManager);
        holder.recyclerView.setAdapter(adapterDailyTasks);

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
                    List<TodayAssignmentData> resultsModel = new ArrayList<>();
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

                filtered = (List<TodayAssignmentData>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        MyViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.daily_tasks_adapter_recycler);
        }
    }

    public interface CustomItemClickListener {
        void onItemClick(View v, int position);
    }
}