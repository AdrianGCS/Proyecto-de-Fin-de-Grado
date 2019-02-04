package com.example.usuario.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        Intent intento=null;
        switch (view.getId()){
            case R.id.log:
                intento=new Intent(Principal.this,Login.class);
                break;
            case R.id.anonimo:
                break;
            case R.id.registro:
                intento=new Intent(Principal.this,Registro.class);
                break;
        }

    }
}
