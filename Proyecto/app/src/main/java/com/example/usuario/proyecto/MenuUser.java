package com.example.usuario.proyecto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MenuUser extends AppCompatActivity {
    public static String id;
    TextView ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_user);
        ids = findViewById(R.id.idenfermo);
        cogerDatos();
    }

    public void cogerDatos() {
        id = getIntent().getStringExtra("id_enfermo");
        ids.setText(id);
    }
}
