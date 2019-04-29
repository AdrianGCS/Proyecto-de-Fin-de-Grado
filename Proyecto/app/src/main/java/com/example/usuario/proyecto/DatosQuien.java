package com.example.usuario.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DatosQuien extends AppCompatActivity {
public static String ids;
TextView cid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_quien);
        cid =findViewById(R.id.iduse);
        coger();
    }
    public void onClick(View view) {
        /*Intent intento= new Intent(Principal.this,Login.class);
        startActivity(intento);
        */
        switch (view.getId()) {



        }

    }
    public void coger(){
        ids=getIntent().getStringExtra("ideuser");
cid.setText(ids);
    }
}
