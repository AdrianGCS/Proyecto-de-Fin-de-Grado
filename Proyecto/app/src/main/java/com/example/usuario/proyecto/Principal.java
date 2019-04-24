package com.example.usuario.proyecto;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Principal extends AppCompatActivity {
    Bundle datos;
    public static String telefono = "";
    public static String fone = "";
    TextView phon, hy,u;
    public static String ids = "";
    public static String imei = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        phon = findViewById(R.id.tele);
        hy = findViewById(R.id.idss);
        u=findViewById(R.id.imeiee);
        cogerDatos();

    }

    public void onClick(View view) {
        /*Intent intento= new Intent(Principal.this,Login.class);
        startActivity(intento);
        */
        switch (view.getId()) {

            case R.id.envio:
                startActivity(new Intent(this, Sms.class));
                enviarDatoss();
                break;
            case R.id.llamada:
                startActivity(new Intent(this, LLamar.class));
                enviarDatos();
                break;
            case R.id.loca:
                startActivity(new Intent(this, Localizacion.class));
                break;

        }
    }

    private void cogerDatos() {
        telefono = getIntent().getStringExtra("telefono");
        ids = getIntent().getStringExtra("id_enfermo");
        imei = getIntent().getStringExtra("imei");
        hy.setText(ids);
        phon.setText(telefono);
        u.setText(imei);
    }

    private void enviarDatos() {

        Intent i = new Intent(getApplicationContext(), LLamar.class);
        i.putExtra("telefono", telefono);
        i.putExtra("id_enfermo", ids);
        i.putExtra("imei", imei);
        startActivity(i);
        finish();
    }

    private void enviarDatoss() {


        Intent i = new Intent(getApplicationContext(), Sms.class);
        i.putExtra("telefono", telefono);
        i.putExtra("id_enfermo", ids);
        i.putExtra("imei", imei);
        startActivity(i);
        finish();
    }
}


