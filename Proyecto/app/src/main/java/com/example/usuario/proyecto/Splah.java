package com.example.usuario.proyecto;

import android.content.Intent;
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

          Intent intent = new Intent(Splah.this,MainActivity.class);
          startActivity(intent);
          finish();
            }
        },2000);
    }
}
