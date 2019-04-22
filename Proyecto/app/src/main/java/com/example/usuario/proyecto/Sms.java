package com.example.usuario.proyecto;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.app.Activity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class Sms extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    Button dale;
    TextView telefono;

    public static String n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        dale = (Button) findViewById(R.id.dale);
        telefono = findViewById(R.id.ono);
        cogerDatos();

        Toast.makeText(getApplicationContext(), "Enviar sms tiene cargo", Toast.LENGTH_LONG).show();

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest
                        .permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.SEND_SMS,}, 1000);
        } else {
        }
        ;
        dale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                cogerDatos();
                enviarMensaje(n, "Tu familiar necesita ayuda");
            }
        });
    }

    public void cogerDatos() {

        n = getIntent().getStringExtra("tel");
        telefono.setText(n);

    }

    private void enviarMensaje(String n, String mensaje) {
        try {
            n = getIntent().getStringExtra("tel");
            telefono.setText(n);
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(n, null, mensaje, null, null);
            Toast.makeText(getApplicationContext(), "Mensaje Enviado.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Mensaje no enviado, datos incorrectos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(this, MainActivity.class));


    }
}


