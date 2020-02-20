package io.devbeans.swyft.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AdapterAllDaliyTasks extends RecyclerView.Adapter<AdapterAllDaliyTasks.MyViewHolder> {
    private Context context;
    CustomItemClickListener listener;
    List<TodayAssignmentData> list;
    boolean status;
    Gson gson = new Gson();

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "ScannedList";

    public AdapterAllDaliyTasks(boolean Status, Context context, List<TodayAssignmentData> home_list, CustomItemClickListener listener) {
        this.list = home_list;
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

        AdapterDailyTasks adapterDailyTasks = new AdapterDailyTasks(position, status, context, list.get(position).getPickupLocations(), list, new AdapterDailyTasks.CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int i) {

                if (!Databackbone.getinstance().riderdetails.getType().equals("delivery")){
                    if (list.get(position).getParcels().isEmpty()){
                        Databackbone.getinstance().showAlsertBox(context, context.getResources().getString(R.string.error), "No Parcel found");
                    }else {
                        Databackbone.getinstance().task_to_show = Databackbone.getinstance().todayassignments.getData().get(position).getVendorId();

//                        List<String> arrayList = new ArrayList<>();
//                        String json = sharedpreferences.getString("vendorIds", "");
//                        if (!(json.equals(null) || json.equals(""))) {
//                            Type type = new TypeToken<List<String>>() {
//                            }.getType();
//                            arrayList = gson.fromJson(json, type);
//                            arrayList.add(list.get(position).getVendorId());
//                            Databackbone.getinstance().vendorIdsList = arrayList;
//                        }else {
//                            arrayList.add(list.get(position).getVendorId());
//                        }
//
//                        String jsonP = gson.toJson(Databackbone.getinstance().scannedParcelsIds);
//                        mEditor.putString("vendorIds", jsonP).commit();

                        Intent orders = new Intent(context, BarCodeScannerActivity.class);
                        orders.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        orders.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        orders.putExtra("position", String.valueOf(position));
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
        return list.size();
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