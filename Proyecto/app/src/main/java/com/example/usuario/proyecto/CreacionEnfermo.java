package com.example.usuario.proyecto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class CreacionEnfermo extends AppCompatActivity {
    private EditText nombre;
    private EditText apellidos;
    private EditText telefono;
    private EditText calle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_enfermo);

    }

    public void onClick() {
        if ("".equals(nombre.getText().toString())) {
            nombre.setError("Introduce el nombre");
            return;
        }  if ("".equals(apellidos.getText().toString())) {
            apellidos.setError("Introduce los apellidos");
            return;
        } if ("".equals(telefono.getText().toString())) {
            telefono.setError("Introduce el telefono");
            return;
        }

    }
}
