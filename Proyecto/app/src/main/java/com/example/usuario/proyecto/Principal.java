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
        Intent intento=null;
        switch (view.getId()){
            case R.id.atras:
                intento=new Intent(Principal.this,Login.class);
                break;

        }
startActivity(intento);
    }

}
