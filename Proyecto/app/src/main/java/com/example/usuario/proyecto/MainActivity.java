package com.example.usuario.proyecto;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Camera;
import android.graphics.Picture;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{
Button btnScanBarcode,registro,log;
    private static final String url ="jdbc:mysql://85.56.88.205:3306/proyecto";
    private static final String user = "Consultas";
    private static final String pass = "Consultas";
    private TextView Datos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
initViews();
        Datos = (TextView) findViewById(R.id.Datos);
        Button Conectar =(Button) findViewById(R.id.Conectar);

        Conectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyTask().execute();
            }
        });
    }
private void initViews(){
       // btnTakePicture=findViewById(R.id.btnTakePicture);
        btnScanBarcode=findViewById(R.id.btnScanBarcode);
        registro=findViewById(R.id.registro);

    log=findViewById(R.id.log);
        //btnTakePicture.setOnClickListener(this);
        btnScanBarcode.setOnClickListener(this);
        registro.setOnClickListener(this);
        log.setOnClickListener(this);
}
public void onClick(View v){
        switch (v.getId()){

            case R.id.btnScanBarcode:
                startActivity(new Intent(MainActivity.this, ImagenQr.class));
                startActivity(new Intent(MainActivity.this,EscanerQr.class));
                break;
            case R.id.log:
                startActivity(new Intent(MainActivity.this,Login.class));
                break;
            case R.id.registro:
                startActivity(new Intent(MainActivity.this,Registro.class));
                break;

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

                final ResultSet rs = st.executeQuery("SELECT Nombre FROM `usuario` where ID=1");
                rs.next();
                fName=rs.getString(1);


            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            Datos.setText(fName);

            super.onPostExecute(result);
        }
    }
}
