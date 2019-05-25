package com.example.usuario.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Localizaciones extends AppCompatActivity {
public static String nombre, apellidos,telefono,direccion,iduser,idenfermo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizaciones);
        coger();
    }
    public void coger(){
        nombre=getIntent().getStringExtra("nombre");
        apellidos=getIntent().getStringExtra("apellidos");
        telefono=getIntent().getStringExtra("telefono");
        direccion=getIntent().getStringExtra("direccion");
        iduser=getIntent().getStringExtra("iduser");
        idenfermo=getIntent().getStringExtra("idEnfermo");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,Historial.class);
        intent.putExtra("iduser",iduser);
        intent.putExtra("idEnfermo",idenfermo);
        startActivity(intent);
        finish();
    }
}
