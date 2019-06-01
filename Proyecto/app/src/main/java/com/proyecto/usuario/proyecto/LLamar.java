package com.proyecto.usuario.proyecto;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LLamar extends AppCompatActivity {
    TextView tls;
    private Button boto;
    public static String phone = "";
    public static String ideess = "";
    public static String imie = "",dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamar);
        tls = findViewById(R.id.fono);
        boto = (Button) findViewById(R.id.llamar);
        cogerDatos();
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
        phone = getIntent().getStringExtra("telefono");
        tls.setText(phone);
        ideess = getIntent().getStringExtra("id_enfermo");
        imie = getIntent().getStringExtra("imei");
        dir=getIntent().getStringExtra("direccion");
    }

    public void llamartelefono() {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LLamar.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone));
                startActivity(callIntent);
                finishActivity(0);
                //tls.setText(phone);

            } else {
               /* Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone));
                startActivity(callIntent);

                finish();*/
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Principal.class);
        i.putExtra("telefono", phone);
        i.putExtra("id_enfermo", ideess);
        i.putExtra("imei", imie);
        i.putExtra("direccion",dir);
        startActivity(i);
        // finish();


    }

}
