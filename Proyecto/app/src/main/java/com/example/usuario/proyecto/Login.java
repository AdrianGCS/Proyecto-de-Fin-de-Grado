package com.example.usuario.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        }
    public void onClick(View view){

        Intent intento=null;
        switch (view.getId()){
            //esta no funciona
            case R.id.atras:
                intento=new Intent(Login.this,MainActivity.class);
                break;
            case R.id.entrar:
                intento=new Intent(Login.this,Principal.class);
                break;
        }
    }
    }

