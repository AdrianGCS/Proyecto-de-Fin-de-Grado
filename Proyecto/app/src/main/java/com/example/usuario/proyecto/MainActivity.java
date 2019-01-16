package com.example.usuario.proyecto;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final String url ="jdbc:mysql://localhost:3306/prueba";
    private static final String user = "root";
    private static final String pass = "1234";
    private TextView Datos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Datos = (TextView) findViewById(R.id.Datos);
        Button Conectar =(Button) findViewById(R.id.Conectar);

        Conectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyTask().execute();
            }
        });
    }
    public void onClick(View v) {
        Intent i = new Intent(MainActivity.this, QrCodeActivity.class);
        startActivityForResult(i, REQUEST_CODE_QR_SCAN);
    }
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(getApplicationContext(), "No hay respuesta", Toast.LENGTH_SHORT).show();
            String resultado = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (resultado != null) {
                Toast.makeText(getApplicationContext(), "Codigo QR desconocido", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data != null) {
                String lectura = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                Toast.makeText(getApplicationContext(), "Leído: " + lectura, Toast.LENGTH_SHORT).show();

            }
        }
    }
    private class MyTask extends AsyncTask<Void, Void, Void>{
        private String fName="";
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url,user,pass);

                Statement st = con.createStatement();
                String sql ="SELECT * FROM `prueba`";

                final ResultSet rs = st.executeQuery("SELECT * FROM `prueba`");//revisar esto , el servidor no lo ejecuta
                rs.next();
                fName=rs.getString(1);
                //estas lineas son de prueba , nunca se llegan a ejecutar en teoria

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            Datos.setText(fName);
            //Pruebas

            super.onPostExecute(result);
        }
    }
}
