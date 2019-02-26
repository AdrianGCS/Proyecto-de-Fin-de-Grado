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

                case R.id.llamada:
                    startActivity(new Intent(Principal.this,LLamar.class));
                    break;
                /*case R.id.log:
                    startActivity(new Intent(Principal.this,Login.class));
                    break;
                case R.id.registro:
                    startActivity(new Intent(Principal.this,Registro.class));
                    break;
                case R.id.otra:
                    startActivity(new Intent(Principal.this,GeneradorQR.class));
                    break;

*/
            }
        }
    }


