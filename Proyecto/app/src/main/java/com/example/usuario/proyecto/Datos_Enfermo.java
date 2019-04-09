package com.example.usuario.proyecto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Datos_Enfermo extends AppCompatActivity {
    private Button boto;
    private TextView nombre, apellidos, telefono, direccion;
    Bundle datos;
 private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos__enfermo);
        boto = findViewById(R.id.entrar);
        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        telefono = findViewById(R.id.telefono);
        direccion = findViewById(R.id.direccion);
        image=findViewById(R.id.qr_enfermo);
        cogerDatos();
    }

    public void onclick(View v) {
        startActivity(new Intent(this, MenuUser.class));

    }

    private void cogerDatos() {

        String n = getIntent().getStringExtra("nombre");
        nombre.setText(n);
        String a = getIntent().getStringExtra("apellidos");
        apellidos.setText(a);
        String t = getIntent().getStringExtra("telefono");
        telefono.setText(t);
        String d = getIntent().getStringExtra("direccion");
        direccion.setText(d);
        Intent id=getIntent();
        Bitmap bitmap=(Bitmap) getIntent().getParcelableExtra("BitmapImage");
        image.setImageBitmap(bitmap);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(this,MainActivity.class));


    }
}
