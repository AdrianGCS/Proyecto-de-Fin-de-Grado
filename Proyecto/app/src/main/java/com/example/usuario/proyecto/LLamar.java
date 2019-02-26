package com.example.usuario.proyecto;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LLamar extends AppCompatActivity {
private EditText telefono;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamar);

    }

    public void onClick(View view) {
            Intent llamada = new Intent(Intent.ACTION_CALL, Uri.parse("#31#" + telefono.getText().toString()));
            if (ActivityCompat.checkSelfPermission(LLamar.this, Manifest.permission.CALL_PHONE) ==
                    PackageManager.PERMISSION_GRANTED)
                return;
            startActivity(llamada);

    }
}
