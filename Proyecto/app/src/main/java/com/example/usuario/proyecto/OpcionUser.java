package com.example.usuario.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class OpcionUser extends AppCompatActivity {
    public static String id;
    TextView c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcion_user);
        c = findViewById(R.id.cod);
        cogerDatos();
    }

    public void onClick(View view) {
        /*Intent intento= new Intent(Principal.this,Login.class);
        startActivity(intento);
        */
        switch (view.getId()) {

            case R.id.crear:
                Intent i = new Intent(this, CreacionEnfermo.class);
                i.putExtra("iduser", id);
                startActivity(i);

                break;
            case R.id.unirse:
                Intent j = new Intent(this, UnirseEnfermo.class);
                j.putExtra("iduser", id);
                startActivity(j);
                break;

        }
    }

    @Override
    public void onBackPressed() {
       Toast.makeText(getApplicationContext(), "No puede ir atras", Toast.LENGTH_LONG).show();
        //startActivity(new Intent(OpcionUser.this, OpcionUser.class));
    }

    public void cogerDatos() {
        id = getIntent().getStringExtra("iduser");
        c.setText(id);
    }


}
