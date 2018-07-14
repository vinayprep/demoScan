package com.example.demoscan;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    public String[] PERMISSIONS = {android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE};
    AppCompatButton btn;
    CaptureSerialActivityPresenter captureSerialActivityPresenter;
    AppCompatTextView tv;
    boolean perStatus;
    SharedPreferences permissionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);
        captureSerialActivityPresenter = new CaptureSerialActivityPresenter(this);
        tv = findViewById(R.id.value);

    }

    @Override
    public void onClick(View view) {
        if (hasPermissions(PERMISSIONS)) {
            Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == AppCompatActivity.RESULT_OK) {

            String result = data.getStringExtra("result");
            captureSerialActivityPresenter.postCheckSerial("1", result, "1");
            tv.setText(result);

        }
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(this, PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, PERMISSIONS[1]) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, PERMISSIONS[2]) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, PERMISSIONS[3]) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, PERMISSIONS[4]) == PackageManager.PERMISSION_GRANTED
                    ) {
                //Got Permission
            }
        }
    }

    public boolean hasPermissions(final String... permissions) {
        perStatus = false;
        permissionStatus = this.getSharedPreferences("permissionStatus", MODE_PRIVATE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && this != null && permissions != null) {
            if (ActivityCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, permissions[1]) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, permissions[2]) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, permissions[3]) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, permissions[4]) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[1]) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[2]) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[3]) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[4])) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage("This app needs Camera, Location, and Storage permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMISSION_CALLBACK_CONSTANT);
                            perStatus = true;
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            perStatus = false;
                        }
                    });
                    builder.show();
                } else if (permissionStatus.getBoolean(permissions[0], false) ||
                        permissionStatus.getBoolean(permissions[1], false) ||
                        permissionStatus.getBoolean(permissions[2], false) ||
                        permissionStatus.getBoolean(permissions[3], false) ||
                        permissionStatus.getBoolean(permissions[4], false)) {
                    //Previously Permission Request was cancelled with 'Dont Ask Again',
                    // Redirect to Settings after showing Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("This app needs Camera, Location, and Storage permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                            perStatus = true;
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            perStatus = false;
                        }
                    });
                    builder.show();
                } else {
                    //just request the permission
                    ActivityCompat.requestPermissions(this, permissions, PERMISSION_CALLBACK_CONSTANT);
                    perStatus = true;
                }
                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(permissions[0], true);
                editor.putBoolean(permissions[1], true);
                editor.putBoolean(permissions[2], true);
                editor.putBoolean(permissions[3], true);
                editor.putBoolean(permissions[4], true);
                editor.apply();
                return perStatus;
            } else {
                //Got Permission
                return true;
            }
        }
        return perStatus;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }
            permissionStatus = this.getSharedPreferences("permissionStatus", MODE_PRIVATE);

            if (allgranted) {
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[3])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[4])) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera, Location, and Storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissions[0], false) ||
                    permissionStatus.getBoolean(permissions[1], false) ||
                    permissionStatus.getBoolean(permissions[2], false) ||
                    permissionStatus.getBoolean(permissions[3], false) ||
                    permissionStatus.getBoolean(permissions[4], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera, Location, and Storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
//                Toast.makeText(this,"Unable to get Permission",Toast.LENGTH_LONG).show();
            }
        }
    }


}
