package com.example.usuario.proyecto;

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

        button=findViewById(R.id.acceptar);
        end=findViewById(R.id.Salir);

        button.setOnClickListener(this::onClick);
        end.setOnClickListener(this::onClick);
    }
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.acceptar:
                startActivity(new Intent(Condiciones.this, MainActivity.class));

                SharedPreferences prefs =
                        getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=prefs.edit();
                editor.putInt("Estado",1);

                editor.commit();
                break;

            case R.id.Salir:
                finish();
                break;

        }




    }


}
