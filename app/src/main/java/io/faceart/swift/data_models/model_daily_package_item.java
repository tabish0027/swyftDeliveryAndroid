package io.faceart.swift.data_models;

public class model_daily_package_item {
   public String mb_order_id = "";
   public String mb_name = "";
   public String mb_address = "";
   public String mb_distance = "";
   public String mb_zone = "";
   public Boolean status = false;

   public model_daily_package_item(String mb_order_id, String mb_name, String mb_address, String mb_distance, String mb_zone,Boolean status) {
      this.mb_order_id = mb_order_id;
      this.mb_name = mb_name;
      this.mb_address = mb_address;
      this.mb_distance = mb_distance;
      this.mb_zone = mb_zone;
      this.status = status;
   }
}
