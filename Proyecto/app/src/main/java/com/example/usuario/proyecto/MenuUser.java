package com.example.usuario.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MenuUser extends AppCompatActivity {
    public static String id;
    public static String iduser;
    TextView ids, bnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_user);
        ids = findViewById(R.id.idenfermo);
        cogerDatos();
    }

    public void onClick(View view) {
        /*Intent intento= new Intent(Principal.this,Login.class);
        startActivity(intento);
        */
        switch (view.getId()) {

            case R.id.datos:
                Intent i = new Intent(this, DatosQuien.class);
                i.putExtra("iduser", iduser);
                //i.putExtra("id_enfermo", id);
                startActivity(i);
                finish();
                break;
            case R.id.permisos:
                Intent p = new Intent(this, Permisos.class);
                p.putExtra("iduser", iduser);
               // p.putExtra("id_enfermo", id);
                startActivity(p);
                finish();
                break;
            case R.id.calendario:
                Intent b = new Intent(this, Permisos.class);
                b.putExtra("iduser", iduser);
                // p.putExtra("id_enfermo", id);
                startActivity(b);
                finish();
                break;
            case R.id.historial:
                Intent c = new Intent(this, Permisos.class);
                c.putExtra("iduser", iduser);
                // p.putExtra("id_enfermo", id);
                startActivity(c);
                finish();
                break;
            case R.id.cuni:
                Intent n = new Intent(this, OpcionUser.class);
                n.putExtra("iduser", iduser);
                // p.putExtra("id_enfermo", id);
                startActivity(n);
                finish();
                break;
        }
    }

    public void cogerDatos() {
        id = getIntent().getStringExtra("id_enfermo");
        //ids.setText(id);*/
        iduser = getIntent().getStringExtra("iduser");
        ids.setText(iduser);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

}
