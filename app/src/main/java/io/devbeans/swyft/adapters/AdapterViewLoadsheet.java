package io.devbeans.swyft.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.devbeans.swyft.interface_retrofit.LoadsheetHistoryModel;
import io.swyft.swyft.R;

public class AdapterViewLoadsheet extends RecyclerView.Adapter<AdapterViewLoadsheet.MyViewHolder> {
    private Context context;
    CustomItemClickListener listener;
    List<String> list;

    public AdapterViewLoadsheet(Context context, List<String> home_list, CustomItemClickListener listener) {
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

        holder.textView.setText(list.get(position));
        holder.imageView.setVisibility(View.GONE);
        holder.loadsheet_vendorname_text.setVisibility(View.GONE);
        holder.loadsheet_totalparcels_text.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView, loadsheet_vendorname_text, loadsheet_totalparcels_text;
        ImageView imageView;

        MyViewHolder(View itemView) {
            super(itemView);
            imageView =itemView.findViewById(R.id.completion_icon);
            textView =itemView.findViewById(R.id.loadsheet_id_text);
            loadsheet_totalparcels_text =itemView.findViewById(R.id.loadsheet_totalparcels_text);
            loadsheet_vendorname_text =itemView.findViewById(R.id.loadsheet_vendorname_text);

        }
    }

    public interface CustomItemClickListener {
        void onItemClick(View v, int position);
    }
}