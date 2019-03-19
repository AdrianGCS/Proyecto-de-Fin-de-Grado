package com.example.usuario.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }
    public void onClick(View view){
        /*Intent intento= new Intent(Principal.this,Login.class);
        startActivity(intento);
        */
        switch (view.getId()){

            case R.id.envio:
                startActivity(new Intent(this,Sms.class));

                break;
            case R.id.llamada:
                startActivity(new Intent(this,LLamar.class));
                break;
            case R.id.mapa:
                startActivity(new Intent(this,Localizacion.class));
                break;

                 }
        }
    }


