package com.example.usuario.proyecto;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserData extends AppCompatActivity {
    public static String ids, ide;
    public static String  nom,ape,cor,con;
    EditText nombre,apellidos,contra;
    TextView correo;
    private Dialog midialogo;
    private AccessServiceAPI miser;

    Button guar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        nombre=findViewById(R.id.nombre);
        apellidos=findViewById(R.id.apellidos);
        correo=findViewById(R.id.correo);
        contra=findViewById(R.id.contaseña);
        guar=findViewById(R.id.save);
        miser = new AccessServiceAPI();
        coger();


    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.save:
                new TaskRegister().execute(ids,nombre.getText().toString(),apellidos.getText().toString(),correo.getText().toString(),contra.getText().toString());
                break;
            case R.id.log:
                startActivity(new Intent(UserData.this, DatosQuien.class));
                break;


        }
    }
    public void coger(){
        ids=getIntent().getStringExtra("iduser");

        nom=getIntent().getStringExtra("nombre");
        nombre.setText(nom);
        ape=getIntent().getStringExtra("apellidos");
        apellidos.setText(ape);
        cor=getIntent().getStringExtra("correo");
        correo.setText(cor);
        con=getIntent().getStringExtra("contraseña");
        contra.setText(con);
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), DatosQuien.class);
        i.putExtra("iduser", ids);
        startActivity(i);
        finish();
    }
    public class TaskRegister extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            midialogo = ProgressDialog.show(UserData.this, "Espere un momento", "Procesando datos...", true);

        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Integer doInBackground(String... params) {

            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "ActualizarUs");
            postParam.put("id", params[0]);
            postParam.put("Nombre", params[1]);
            postParam.put("Apellido", params[2]);
            postParam.put("Correo", params[3]);
            postParam.put("Contrasenia", params[4]);

            //llama al PHP y envia los datos

            try {
                String jsonString = miser.getJSONStringWithParam_POST(Common.SERVICE_API_URL, postParam);
                JSONObject jsonObject = new JSONObject(jsonString);


                //coge los datos que le pasa php
                return jsonObject.getInt("result");

            } catch (Exception e) {
                e.printStackTrace();
                return Common.RESULT_ERROR;
            }


        }

        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            midialogo.dismiss();
            if (integer == Common.RESULT_SUCCESS) {
                Toast.makeText(UserData.this, "Leido  con exito", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), UserData.class);
                i.putExtra("nombre", nom);
                i.putExtra("apellidos", ape);
                i.putExtra("correo", contra.getText());
                i.putExtra("contraseña", con);
                setResult(1, i);
                startActivity(i);
                finish();

            } else {
                Toast.makeText(UserData.this, "Leido fallido", Toast.LENGTH_LONG).show();
            }
        }
    }
}
