package com.example.usuario.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Registro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }

    public void onClick(View view){
        Intent intento=null;
        switch (view.getId()){
            case R.id.entrar:
                intento=new Intent(Registro.this,Principal.class);
                break;

        }
        startActivity(intento);


    }
}
