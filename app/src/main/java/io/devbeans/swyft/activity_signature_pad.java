package io.devbeans.swyft;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.ImageQuality;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.devbeans.swyft.data_models.LoadSheetModel;
import io.devbeans.swyft.data_models.SignatureURLModel;
import io.devbeans.swyft.interface_retrofit.PasswordResetRequest;
import io.devbeans.swyft.interface_retrofit.swift_api;
import io.devbeans.swyft.interface_retrofit_delivery.Datum;
import io.devbeans.swyft.interface_retrofit_delivery.RiderActivityDelivery;
import io.devbeans.swyft.interface_retrofit_delivery.mark_parcel_complete;
import io.devbeans.swyft.interface_retrofit_delivery.parcel_signature_upload;
import io.devbeans.swyft.interface_retrofit_delivery.swift_api_delivery;
import io.swyft.swyft.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class activity_signature_pad extends AppCompatActivity {

    Button btn_submit, capture_image;
    SignaturePad mSignaturePad;
    ImageView btn_cross;
    EditText user_name;
    ProgressBar progressBar = null;
    Bitmap signature_image = null;
    Bitmap cam_image = null;
    Boolean has_signature_image = false;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "ScannedList";

    SharedPreferences sharedpreferences_default;
    SharedPreferences.Editor mEditor_default;
    public static final String MyPREFERENCES_default = "MyPrefs";

    List<String> scannedIds = new ArrayList<>();
    Gson gson = new Gson();
    int position = 0;
    int inner_position = 0;

    List<LoadSheetModel> savedLoadsheets = new ArrayList<>();

    String image_url, sig_url;

    ArrayList<String> ImagereturnValue = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mEditor = sharedpreferences.edit();
        sharedpreferences_default = getSharedPreferences(MyPREFERENCES_default, Context.MODE_PRIVATE);
        mEditor_default = sharedpreferences_default.edit();

        position = Integer.valueOf(getIntent().getStringExtra("position"));
        inner_position = Integer.valueOf(getIntent().getStringExtra("locationPosition"));

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        btn_cross = findViewById(R.id.btn_cross);
        user_name = findViewById(R.id.textView11);
        btn_cross.setVisibility(View.GONE);
        btn_submit = findViewById(R.id.btn_submit);
        capture_image = findViewById(R.id.capture_image);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_name.getText().toString().trim().isEmpty()){
                    Toast.makeText(activity_signature_pad.this, "Please enter the name first", Toast.LENGTH_SHORT).show();
                    user_name.setFocusable(true);
                }else {
                    uploadSignature();
                }
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.url_loading_animation);
        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignaturePad.clear();
                btn_cross.setVisibility(View.GONE);
                has_signature_image = false;
            }
        });
        mSignaturePad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard(view);
                return false;
            }
        });
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
                btn_cross.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
                has_signature_image = true;
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
            }
        });
        final ImageView btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_signature_pad.this.finish();
            }
        });

        capture_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Options options = Options.init()
                        .setRequestCode(100)                                                 //Request code for activity results
                        .setCount(1)                                                         //Number of images to restict selection count
                        .setFrontfacing(false)                                                //Front Facing camera on start
                        .setImageQuality(ImageQuality.HIGH)                                  //Image Quality
                        .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)           //Orientaion
                        .setPath("/pix/images");                                             //Custom Path For Image Storage

                Pix.start(activity_signature_pad.this, options);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            ImagereturnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            uploadImage();
        }
    }

    protected void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) activity_signature_pad.this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isAcceptingText()){
        InputMethodManager in = (InputMethodManager) activity_signature_pad.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
    }

    public void uploadImage() {
        EnableLoading();

        File image = new File(ImagereturnValue.get(0));
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        cam_image = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);

        File f = new File(this.getCacheDir(), "cam_image.jpeg");


//Convert bitmap to byte array
        Bitmap bitmap = cam_image;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = null;
        try {
            f.createNewFile();
            fos = new FileOutputStream(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
        MultipartBody.Part filedata = MultipartBody.Part.createFormData("file", f.getName(), reqFile);

        swift_api_delivery riderapidata = Databackbone.getinstance().getRetrofitbuilder().create(swift_api_delivery.class);

        RequestBody uploadContainer = RequestBody.create(MediaType.parse("multipart/form-data"), "Loadsheets");

        Call<SignatureURLModel> call = riderapidata.uploadSignature(filedata, uploadContainer);
        call.enqueue(new Callback<SignatureURLModel>() {
            @Override
            public void onResponse(Call<SignatureURLModel> call, Response<SignatureURLModel> response) {
                if (response.isSuccessful()) {

                    SignatureURLModel signatureURLModel = response.body();
                    Databackbone.getinstance().cam_image_data = signatureURLModel;
                    image_url = Databackbone.getinstance().cam_image_data.getMessage();

                    DisableLoading();

                } else {
                    Databackbone.getinstance().showAlsertBox(activity_signature_pad.this, getResources().getString(R.string.error), "Server code error 88");

                    DisableLoading();
                }

            }

            @Override
            public void onFailure(Call<SignatureURLModel> call, Throwable t) {
                System.out.println(t.getCause());
                Databackbone.getinstance().showAlsertBox(activity_signature_pad.this, getResources().getString(R.string.error), "Server code error 89 " + t.getMessage());
                DisableLoading();

            }
        });

    }


    public void uploadSignature() {
        if (!has_signature_image) {
            Databackbone.getinstance().showAlsertBox(activity_signature_pad.this, getResources().getString(R.string.error), getResources().getString(R.string.please_put_signature));

            return;
        }
        EnableLoading();
        signature_image = mSignaturePad.getSignatureBitmap();

        File f = new File(this.getCacheDir(), "file.jpeg");


//Convert bitmap to byte array
        Bitmap bitmap = signature_image;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = null;
        try {
            f.createNewFile();
            fos = new FileOutputStream(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
        MultipartBody.Part filedata = MultipartBody.Part.createFormData("file", f.getName(), reqFile);

        swift_api_delivery riderapidata = Databackbone.getinstance().getRetrofitbuilder().create(swift_api_delivery.class);

        RequestBody uploadContainer = RequestBody.create(MediaType.parse("multipart/form-data"), "Loadsheets");

        Call<SignatureURLModel> call = riderapidata.uploadSignature(filedata, uploadContainer);
        call.enqueue(new Callback<SignatureURLModel>() {
            @Override
            public void onResponse(Call<SignatureURLModel> call, Response<SignatureURLModel> response) {
                if (response.isSuccessful()) {

                    SignatureURLModel signatureURLModel = response.body();
                    Databackbone.getinstance().cam_image_data = signatureURLModel;
                    sig_url = Databackbone.getinstance().cam_image_data.getMessage();

                    loadSheet();

                } else {
                    Databackbone.getinstance().showAlsertBox(activity_signature_pad.this, getResources().getString(R.string.error), "Server code error 88");

                    DisableLoading();
                }

            }

            @Override
            public void onFailure(Call<SignatureURLModel> call, Throwable t) {
                System.out.println(t.getCause());
                Databackbone.getinstance().showAlsertBox(activity_signature_pad.this, getResources().getString(R.string.error), "Server code error 89 " + t.getMessage());
                DisableLoading();

            }
        });

    }

    public void loadSheet() {

        List<String> arrayList = new ArrayList<>();
        String json = sharedpreferences.getString(Databackbone.getinstance().todayassignmentdata.get(position).getVendorId() + Databackbone.getinstance().todayassignmentdata.get(position).getPickupLocations().get(inner_position).getId(), "");
        Type type = new TypeToken<List<String>>() {}.getType();
        arrayList = gson.fromJson(json, type);
        Databackbone.getinstance().scannedParcelsIds = arrayList;
        scannedIds = Databackbone.getinstance().scannedParcelsIds;

        List<LoadSheetModel> arrayList_loadsheet = new ArrayList<>();
        String json_loadsheet = sharedpreferences.getString("PendingLoadsheet", "");

        if (json_loadsheet != null){
            if (!json_loadsheet.equals("")){
                Type type_loadsheet = new TypeToken<List<LoadSheetModel>>() {}.getType();
                arrayList_loadsheet = gson.fromJson(json_loadsheet, type_loadsheet);
                savedLoadsheets = arrayList_loadsheet;
            }
        }

        LoadSheetModel loadSheetModel = new LoadSheetModel();

        loadSheetModel.setParcelIds(scannedIds);
        loadSheetModel.setGeopoints(Databackbone.getinstance().todayassignmentdata.get(position).getPickupLocations().get(inner_position).getGeopoints());
        loadSheetModel.setSignatureUrl(sig_url);
        loadSheetModel.setPickupSheetUrl(image_url);
        loadSheetModel.setName(user_name.getText().toString());
        loadSheetModel.setPickupLocationId(Databackbone.getinstance().todayassignmentdata.get(position).getPickupLocations().get(inner_position).getId());
        loadSheetModel.setVendorId(Databackbone.getinstance().todayassignmentdata.get(position).getVendorId());

        savedLoadsheets.add(loadSheetModel);

        swift_api riderapi = Databackbone.getinstance().getRetrofitbuilder().create(swift_api.class);

        Call<PasswordResetRequest> call = riderapi.generateLoadsheet(sharedpreferences_default.getString("AccessToken", ""), (sharedpreferences_default.getString("RiderID", "")), loadSheetModel);
        call.enqueue(new Callback<PasswordResetRequest>() {
            @Override
            public void onResponse(Call<PasswordResetRequest> call, Response<PasswordResetRequest> response) {
                if (response.isSuccessful()) {

                    new AlertDialog.Builder(activity_signature_pad.this)
                            .setTitle("Success")
                            .setMessage("Loadsheet generated")

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation\
                                    Intent i = new Intent(activity_signature_pad.this, LoadsheetHistoryActivity.class);
                                    startActivity(i);
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                    mEditor.clear().commit();


                    DisableLoading();
                } else {
                    String jsonP = gson.toJson(savedLoadsheets);
                    mEditor.putString("PendingLoadsheet", jsonP).commit();

                    new AlertDialog.Builder(activity_signature_pad.this)
                            .setTitle("Error")
                            .setMessage("Error has been occured during generating Loadsheet.\nTry again later!")

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation\
                                    Intent i = new Intent(activity_signature_pad.this, LoadsheetHistoryActivity.class);
                                    startActivity(i);
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                    DisableLoading();
                }

            }

            @Override
            public void onFailure(Call<PasswordResetRequest> call, Throwable t) {
                System.out.println(t.getCause());
                String jsonP = gson.toJson(savedLoadsheets);
                mEditor.putString("PendingLoadsheet", jsonP).commit();
                new AlertDialog.Builder(activity_signature_pad.this)
                        .setTitle("Error")
                        .setMessage("Error has been occured during generating Loadsheet.\nTry again later!")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation\
                                Intent i = new Intent(activity_signature_pad.this, LoadsheetHistoryActivity.class);
                                startActivity(i);
                            }
                        })

                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                DisableLoading();
            }
        });

    }



    public void DisableLoading() {
        btn_submit.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }

    public void EnableLoading() {
        btn_submit.setEnabled(false);

        progressBar.setVisibility(View.VISIBLE);
    }
}
