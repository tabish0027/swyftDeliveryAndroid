package io.devbeans.swyft.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import io.devbeans.swyft.Databackbone;
import io.devbeans.swyft.LoadsheetHistoryActivity;
import io.devbeans.swyft.data_models.LoadSheetModel;
import io.devbeans.swyft.interface_retrofit.PasswordResetRequest;
import io.devbeans.swyft.interface_retrofit.swift_api;
import io.swyft.pickup.R;
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

    SharedPreferences sharedpreferences_loadsheet;
    SharedPreferences.Editor mEditor_loadsheet;
    public static final String MyPREFERENCES_loadsheet = "LoadSheet";

    Gson gson = new Gson();

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
        sharedpreferences_loadsheet = context.getSharedPreferences(MyPREFERENCES_loadsheet, Context.MODE_PRIVATE);
        mEditor_loadsheet = sharedpreferences_loadsheet.edit();

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
                int mCurrRotation = 0;
                mCurrRotation %= 360;
                float fromRotation = mCurrRotation;
                float toRotation = mCurrRotation += 180;

                final RotateAnimation rotateAnim = new RotateAnimation(
                        fromRotation, toRotation, holder.imageView.getWidth()/2, holder.imageView.getHeight()/2);

                rotateAnim.setDuration(2000); // Use 0 ms to rotate instantly
                rotateAnim.setFillAfter(true); // Must be true or the animation will reset

                holder.imageView.startAnimation(rotateAnim);
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

                    list.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeRemoved(position, list.size());

                    String json = gson.toJson(list);
                    mEditor_loadsheet.putString("PendingLoadsheet", json).commit();

                    new AlertDialog.Builder(context)
                            .setTitle("Success")
                            .setMessage("Loadsheet generated")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation\
                                    LoadsheetHistoryActivity.canceled_textView.setVisibility(View.GONE);
                                    LoadsheetHistoryActivity.canceled_headings.setVisibility(View.GONE);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                } else {

                    new AlertDialog.Builder(context)
                            .setTitle("Error")
                            .setMessage("Error has been occured during generating Loadsheet.\nTry again later!")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation\
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }

            }

            @Override
            public void onFailure(Call<PasswordResetRequest> call, Throwable t) {
                System.out.println(t.getCause());
                new AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage("Error has been occured during generating Loadsheet.\nTry again later!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation\
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
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
            imageView = itemView.findViewById(R.id.completion_icon);
            loadsheet_id_text = itemView.findViewById(R.id.loadsheet_id_text);
            loadsheet_vendorname_text = itemView.findViewById(R.id.loadsheet_vendorname_text);
            loadsheet_totalparcels_text = itemView.findViewById(R.id.loadsheet_totalparcels_text);

        }
    }

    public interface CustomItemClickListener {
        void onItemClick(View v, int position);
    }
}