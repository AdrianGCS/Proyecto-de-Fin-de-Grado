package com.proyecto.usuario.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.*;
import android.widget.Button;

public class Condiciones extends AppCompatActivity {
    Button button;
    Button end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condiciones);

        button=findViewById(R.id.aceptar);
        end=findViewById(R.id.Salir);

        button.setOnClickListener(this::onClick);
        end.setOnClickListener(this::onClick);
    }
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.aceptar:
                startActivity(new Intent(Condiciones.this, MainActivity.class));
                //Empieza a cargar la proxima interfaz
                SharedPreferences prefs =
                        getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=prefs.edit();
                editor.putInt("Estado",1);
                //Crea un archivo al que solo tendra acceso la aplicacion
                // y donde guardara el cambio de estado

                editor.commit();
                break;

            case R.id.Salir:
                finish();
                break;

        }





    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }


}
