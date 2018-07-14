package com.example.demoscan;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rats on 14/6/2017.
 */

public class CaptureSerialActivityPresenter {

    private AppCompatActivity context;
    private CheckSerial checkSerial;

    CaptureSerialActivityPresenter(AppCompatActivity context) {
        this.context = context;
    }

    void postCheckSerial(final String brandId, final String serialNumber, final String location) {

        Call<CheckSerial> call = RestClient.post().postCheckSerial("blE4aUxETU5Wa0p5ckNTTzQwZUF0Mm5WRkxpTjc4UkRsUmtuYVpXNw==", brandId, serialNumber, location);
        call.enqueue(new Callback<CheckSerial>() {
            @Override
            public void onResponse(Call<CheckSerial> call, Response<CheckSerial> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        checkSerial = response.body();
                        context.findViewById(R.id.value).setVisibility(View.VISIBLE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("QR Code");
                        builder.setMessage("The Number is: " + checkSerial.getSerialId());
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Server Not Responding");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }

            @Override
            public void onFailure(Call<CheckSerial> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Server Not Responding");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

}
