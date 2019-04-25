package com.example.usuario.proyecto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.Settings.Secure;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Splah extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splah);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences prefs =
                        getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

                int validacion = prefs.getInt("Estado",0);

               if(validacion == 0) {
                   Intent intent = new Intent(Splah.this, Condiciones.class);
                   startActivity(intent);
                   finish();
               }
               else{
                   Intent intent = new Intent(Splah.this, MainActivity.class);
                   startActivity(intent);
                   finish();
               }


            }
        }

        ,2000);
    }
    public void getCoordenadas() {
        //Obtener la direccion de la calle a partir de la latitud y la longitud

            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocationName("Madrid",1);
                if (!list.isEmpty()) {
                    Address coordenadas =list.get(0);
                   double x = coordenadas.getLatitude();
                   double Y = coordenadas.getLongitude();
                   int Z = coordenadas.getMaxAddressLineIndex();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

