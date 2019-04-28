package com.example.usuario.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class OpcionUser extends AppCompatActivity {
    public static String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcion_user);
        cogerDatos();
    }

    public void onClick(View view) {
        /*Intent intento= new Intent(Principal.this,Login.class);
        startActivity(intento);
        */
        switch (view.getId()) {

            case R.id.crear:
                envio();
                startActivity(new Intent(this, CreacionEnfermo.class));

                break;
            case R.id.unirse:
                startActivity(new Intent(this, UnirseEnfermo.class));
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getApplicationContext(), "No puede ir atras", Toast.LENGTH_LONG).show();
        startActivity(new Intent(OpcionUser.this, OpcionUser.class));
    }

    public void cogerDatos() {
        id = getIntent().getStringExtra("id");
    }

    public void envio() {
        Intent i = new Intent(this, CreacionEnfermo.class);
        i.putExtra("id", id);
    }
}
