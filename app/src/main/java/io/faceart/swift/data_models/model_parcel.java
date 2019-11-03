package io.faceart.swift.data_models;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import io.faceart.swift.interface_retrofit_delivery.Parcel;

public class model_parcel {
   public String mb_task_id = "";
   public String Parcelid = "";
   public String mb_name = "";
   public String mb_address = "";
   public String status = "";
   public int ammount = 0;
   public String Description = "";
   public boolean selected = false;

   public model_parcel(String mb_task_id, String parcelid, String mb_name, String mb_address, String status, int ammount, String description, boolean selected) {
      this.mb_task_id = mb_task_id;
      Parcelid = parcelid;
      this.mb_name = mb_name;
      this.mb_address = mb_address;
      this.status = status;
      this.ammount = ammount;
      Description = description;
      this.selected = selected;
   }
}
