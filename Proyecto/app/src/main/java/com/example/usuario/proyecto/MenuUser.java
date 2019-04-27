package com.example.usuario.proyecto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MenuUser extends AppCompatActivity {
    public static int id;
    TextView ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_user);
        ids = findViewById(R.id.idenfermo);
        cogerDatos();
    }

    public void cogerDatos() {
        id = getIntent().getIntExtra("id_enfermo", id);
        ids.setText(id);
    }
}
