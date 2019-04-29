package com.example.usuario.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MenuUser extends AppCompatActivity {
    public static String id;
    public static String iduser;
    TextView ids,bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_user);
        ids = findViewById(R.id.idenfermo);
        cogerDatos();
        cogerDatosuser();
    }
    public void onClick(View view) {
        /*Intent intento= new Intent(Principal.this,Login.class);
        startActivity(intento);
        */
        switch (view.getId()) {

            case R.id.datos:
                Intent i=new Intent(this,DatosQuien.class);
                i.putExtra("ideuser",iduser);
                startActivity(i);
                finish();
                break;

        }
    }

    public void cogerDatos() {
        id = getIntent().getStringExtra("id_enfermo");
        ids.setText(id);
    }
    public void cogerDatosuser() {
        iduser = getIntent().getStringExtra("iduser");
        ids.setText(iduser);
    }

}
