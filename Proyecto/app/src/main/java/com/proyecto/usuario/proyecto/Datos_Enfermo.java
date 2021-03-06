package com.proyecto.usuario.proyecto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Datos_Enfermo extends AppCompatActivity {
    private Button boto;
    private TextView nombre, apellidos, telefono, direccion, codigoe;
    Bundle datos;
    private ImageView image;
    public static String idenfer;
    public static String idfa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos__enfermo);
        boto = findViewById(R.id.salir);
        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        telefono = findViewById(R.id.telefono);
        direccion = findViewById(R.id.direccion);
        image = findViewById(R.id.qr_enfermo);
        codigoe = findViewById(R.id.codigo);
        cogerDatos();
        boto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarDatos();
                // Intent i=new Intent(Datos_Enfermo.this,MenuUser.class);

            }
        });

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
        Intent id = getIntent();
        Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("BitmapImage");
        image.setImageBitmap(bitmap);
        String co = getIntent().getStringExtra("Codigo");
        codigoe.setText(co);
        idenfer = getIntent().getStringExtra("id_enfermo");

        idfa = getIntent().getStringExtra("iduser");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(this, MainActivity.class));


    }

    public void enviarDatos() {
        Intent i = new Intent(this, MenuUser.class);
        i.putExtra("id_enfermo", idenfer);
        i.putExtra("iduser", idfa);
        startActivity(i);
    }


}
