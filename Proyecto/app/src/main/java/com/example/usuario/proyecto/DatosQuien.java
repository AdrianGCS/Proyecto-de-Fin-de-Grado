package com.example.usuario.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DatosQuien extends AppCompatActivity {
    public static String ids, ide;
    TextView cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_quien);
        cid = findViewById(R.id.iduse);
        coger();
    }

    public void onClick(View view) {
        /*Intent intento= new Intent(Principal.this,Login.class);
        startActivity(intento);
        */
        switch (view.getId()) {
            case R.id.ill:
                Intent v = new Intent(this, IllData.class);
                v.putExtra("iduser", ids);
                startActivity(v);
                break;
            case R.id.user:
                Intent a = new Intent(this, UserData.class);
                a.putExtra("iduser", ids);
                startActivity(a);
                break;
        }

    }

    public void coger() {
        ids = getIntent().getStringExtra("iduser");
        cid.setText(ids);

    }
@Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MenuUser.class);
        i.putExtra("iduser", ids);
        startActivity(i);
        finish();
    }
}
