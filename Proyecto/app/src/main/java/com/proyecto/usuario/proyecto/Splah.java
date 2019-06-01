package com.proyecto.usuario.proyecto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

    }

