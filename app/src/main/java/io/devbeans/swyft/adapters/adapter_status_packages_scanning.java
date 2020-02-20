package io.devbeans.swyft.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.devbeans.swyft.Databackbone;
import io.swyft.swyft.R;
import io.devbeans.swyft.data_models.model_order_item;

public class adapter_status_packages_scanning extends RecyclerView.Adapter<adapter_status_packages_scanning.model_order_item_holder> implements Filterable {


    public List<String> list =null;
    public List<String> filtered =null;
    public Context mContext;
    Gson gson = new Gson();
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "ScannedList";
    int abc = 0;
    int cde = 0;


    public adapter_status_packages_scanning(int Abc, int Cde, List<String> orderList, Context context) {
        this.list = orderList;
        this.filtered = orderList;
        this.mContext = context;
        this.abc = Abc;
        this.cde = Cde;
    }


    @Override
    public model_order_item_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_item_package_order_status, parent, false);

        sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mEditor = sharedpreferences.edit();

        return new model_order_item_holder(view);
    }

    @Override
    public int getItemCount() {
        int size= filtered.size();
        return  size;
    }





    @Override
    public void onBindViewHolder(@NonNull model_order_item_holder holder, final int position) {
        final String order = filtered.get(position);


        holder.mb_order_id.setText(order);
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtered.remove(position);
                String jsonP = gson.toJson(Databackbone.getinstance().scannedParcelsIds);
                mEditor.putString(Databackbone.getinstance().todayassignmentdata.get(abc).getVendorId() + Databackbone.getinstance().todayassignmentdata.get(abc).getPickupLocations().get(cde).getId(), jsonP).commit();
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, filtered.size());
            }
        });

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
                    List<String> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(int i = 0; i < list.size(); i++){
                        if(list.get(i).contains(searchStr)){
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

                filtered = (List<String>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }


    public class model_order_item_holder extends RecyclerView.ViewHolder {


        public TextView mb_order_id;
        ImageView cancel;

        public model_order_item_holder(View itemView) {
            super(itemView);

            mb_order_id = itemView.findViewById(R.id.parcel_id);
            cancel = itemView.findViewById(R.id.cancel_id);

        }
    }
    public void update_list(){
        notifyDataSetChanged();
    }
}
