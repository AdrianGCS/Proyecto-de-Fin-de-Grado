package com.example.usuario.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class UserData extends AppCompatActivity {
    public static String ids, ide;
    TextView n;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        n=findViewById(R.id.nombre);
        coger();
    }
    public void coger(){
        ids=getIntent().getStringExtra("iduser");
        n.setText(ids);
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), DatosQuien.class);
        i.putExtra("iduser", ids);
        startActivity(i);
        finish();
    }
}
