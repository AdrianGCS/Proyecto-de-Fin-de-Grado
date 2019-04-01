package com.example.usuario.proyecto;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.graphics.Picture;
import android.os.AsyncTask;
import android.os.Bundle;
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
    TelephonyManager manager;
    TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        manager= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

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
        texto=findViewById(R.id.imiei);
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnScanBarcode:

                startActivity(new Intent(MainActivity.this, EscanerQr.class));
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
    @SuppressLint({"MissingPermission", "NewApi"})
    public void ObtenerImei(){
        StringBuilder c =new StringBuilder();
        c.append("aa"+manager.getImei());
     texto.setText(c.toString());
    }


}
