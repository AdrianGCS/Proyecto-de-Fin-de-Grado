package com.example.usuario.proyecto;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatosQuien extends AppCompatActivity {
    public static String ids, ide;
    TextView cid;
    private Dialog midialogo;
    private AccessServiceAPI miser;
    public static JSONArray a;
    public static JSONObject b;
    public static JSONObject c;
    public  static String nom,ape,con,cor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_quien);
        cid = findViewById(R.id.iduse);
        miser = new AccessServiceAPI();
        coger();

    }

    public void onClick(View view) {
        /*Intent intento= new Intent(Principal.this,Login.class);
        startActivity(intento);
        */
        switch (view.getId()) {
            case R.id.ill:
                Intent v = new Intent(this, Intermedia.class);
                v.putExtra("iduser", ids);
                //v.putExtra("datos", a);
                startActivity(v);
                break;
            case R.id.user:
                new TaskRegister().execute(ids,"0");
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
    public class TaskRegister extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            midialogo = ProgressDialog.show(DatosQuien.this, "Espere un momento", "Procesando datos...", true);

        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Integer doInBackground(String... params) {

            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "DatosUsuario");
            postParam.put("id", params[0]);
            //llama al PHP y envia los datos

            try {
                String jsonString = miser.getJSONStringWithParam_POST(Common.SERVICE_API_URL, postParam);
                JSONObject jsonObject = new JSONObject(jsonString);

                a = jsonObject.getJSONArray("datos");
                 b = a.getJSONObject(0);
                nom=b.getString("Nombre");
                ape=b.getString("Apellido");
                c=a.getJSONObject(1);
                cor=c.getString("Correo");
                con=c.getString("Contrasenia");
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
                Toast.makeText(DatosQuien.this, "Leido  con exito", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), UserData.class);
                //estructura para pasar Array
                //ArrayList<String> Prueba =new ArrayList<>();
                //Prueba.add("Hola");
                //Prueba.add("Esto es una Prueba");

                //i.putExtra("Prueba",Prueba);
                i.putExtra("iduser",ids);
                i.putExtra("nombre", nom);
                i.putExtra("apellidos", ape);
                i.putExtra("correo", cor);
                i.putExtra("contraseña", con);
                setResult(1, i);
                startActivity(i);
                finish();

            } else {
                Toast.makeText(DatosQuien.this, "Leido fallido", Toast.LENGTH_LONG).show();
            }
        }
    }
}
