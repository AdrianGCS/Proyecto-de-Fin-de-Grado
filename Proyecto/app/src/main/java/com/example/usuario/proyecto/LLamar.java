package com.example.usuario.proyecto;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LLamar extends AppCompatActivity {
    private EditText telefono;
    private Button boto;
    Bundle datos;
public static String n;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamar);
        telefono = (EditText) findViewById(R.id.telefono);
        boto = (Button) findViewById(R.id.llamar);
        boto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamartelefono();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                llamartelefono();
            }
        }
    }
    public void cogerDatos() {

         n = getIntent().getStringExtra("nombre");
        telefono.setText(n);

    }
    public void llamartelefono() {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LLamar.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + telefono.getText().toString()));
                startActivity(callIntent);
                finish();
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + telefono.getText().toString()));
                startActivity(callIntent);
                finish();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
