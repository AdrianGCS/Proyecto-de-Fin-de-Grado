package com.example.usuario.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Calendario extends AppCompatActivity {
    public static String ids, ide;
    TextView cid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        cid=findViewById(R.id.iduse);
        coger();
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
