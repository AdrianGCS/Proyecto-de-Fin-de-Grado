package com.example.usuario.proyecto;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class IllData extends AppCompatActivity {
    public static String ids, nom, ape, cor, con;
    TextView n, nombre, apellidos, telefono, direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ill_data);
        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        direccion = findViewById(R.id.direccion);
        telefono = findViewById(R.id.telefono);

        coger();
    }

    public void coger() {
        ids = getIntent().getStringExtra("iduser");
        n.setText(ids);
        nom = getIntent().getStringExtra("nombre");
        nombre.setText(nom);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MenuUser.class);
        i.putExtra("iduser", ids);

        startActivity(i);
        finish();
    }

}
