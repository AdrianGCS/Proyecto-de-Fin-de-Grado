package com.example.usuario.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class IllData extends AppCompatActivity {
    public static String  ids;
    TextView n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ill_data);
        coger();
    }

    public void coger() {
        ids = getIntent().getStringExtra("iduser");
        n.setText(ids );

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MenuUser.class);
        i.putExtra("iduser", ids);

        startActivity(i);
        finish();
    }
}
