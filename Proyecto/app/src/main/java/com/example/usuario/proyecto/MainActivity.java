package com.example.usuario.proyecto;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.Picture;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
//import com.mapbox.mapboxsdk.maps.Style;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnScanBarcode, registro, log, otra;
    private MapView mapView;
    private TextView Datos;
    TelephonyManager manager;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    }

    private void initViews() {
        btnScanBarcode = findViewById(R.id.btnScanBarcode);
        registro = findViewById(R.id.registro);
        otra = findViewById(R.id.otra);
        log = findViewById(R.id.log);
        btnScanBarcode.setOnClickListener(this);
        registro.setOnClickListener(this);
        log.setOnClickListener(this);
        otra.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnScanBarcode:
                startActivity(new Intent(MainActivity.this, EscanerQr.class));
                cogerimei();
                break;
            case R.id.log:
                startActivity(new Intent(MainActivity.this, Login.class));
                break;
            case R.id.registro:
                startActivity(new Intent(MainActivity.this, Registro.class));
                break;
            case R.id.otra:
                startActivity(new Intent(MainActivity.this, GeneradorQR.class));
                break;


        }
    }

    public void cogerimei() {
        StringBuilder builder = new StringBuilder();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        builder.append(manager.getDeviceId());
    }


}
