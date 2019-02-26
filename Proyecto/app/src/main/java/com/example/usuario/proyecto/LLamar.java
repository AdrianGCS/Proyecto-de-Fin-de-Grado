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
import android.widget.Button;
import android.widget.EditText;

public class LLamar extends AppCompatActivity {
    private EditText telefono;
private Button boto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamar);
        telefono=(EditText)findViewById(R.id.telefono);
boto=(Button) findViewById(R.id.llamar);
boto.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent llama=new Intent(Intent.ACTION_CALL);
        llama.setData(Uri.parse("#31#"+telefono.getText().toString()));
        if (ActivityCompat.checkSelfPermission(LLamar.this,Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            return;
        }
        startActivity(llama);
    }
});
    }
/*
    public void onClick(View view) {
            Intent llamada = new Intent(Intent.ACTION_CALL);
            llamada.setData(Uri.parse("#31#"+telefono.getText().toString()));
            //if (ActivityCompat.checkSelfPermission(LLamar.this, Manifest.permission.CALL_PHONE) ==
              //      PackageManager.PERMISSION_GRANTED)
                //return;
            startActivity(llamada);

    }*/
}
