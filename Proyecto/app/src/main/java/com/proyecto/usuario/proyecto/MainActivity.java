package com.proyecto.usuario.proyecto;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mapbox.mapboxsdk.maps.MapView;
//import com.mapbox.mapboxsdk.maps.Style;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnScanBarcode, registro, log, otra;
    private MapView mapView;
    private TextView datos;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

    }

    private void initViews() {
        btnScanBarcode = findViewById(R.id.btnScanBarcode);
        registro = findViewById(R.id.registro);
        log = findViewById(R.id.log);
        btnScanBarcode.setOnClickListener(this);
        registro.setOnClickListener(this);
        log.setOnClickListener(this);
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


        }
    }
    @Override public void onBackPressed() { }





}
