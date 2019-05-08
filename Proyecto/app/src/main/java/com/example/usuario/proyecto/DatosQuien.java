package com.example.usuario.proyecto;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatosQuien extends AppCompatActivity {
    public static String ids, ide;
    TextView cid;
    private Dialog midialogo;
    private AccessServiceAPI miser;
    public static JSONArray a;

    public static String no, ap, di, te, coduni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_quien);
        cid = findViewById(R.id.iduse);
        miser = new AccessServiceAPI();
        coger();
        new TaskRegister().execute(ids);
    }

    public void onClick(View view) {
        /*Intent intento= new Intent(Principal.this,Login.class);
        startActivity(intento);
        */
        switch (view.getId()) {
            case R.id.ill:
                Intent v = new Intent(this, Intermedia.class);
                v.putExtra("iduser", ids);
                startActivity(v);
                break;
            case R.id.user:
                Intent i = new Intent(getApplicationContext(), UserData.class);
                i.putExtra("nombre", no);
                i.putExtra("apellidos", ap);
                i.putExtra("telefono", te);
                i.putExtra("direccion", di);
                setResult(1, i);
                startActivity(i);
                finish();
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
                i.putExtra("nombre", no);
                i.putExtra("apellidos", ap);
                i.putExtra("telefono", te);
                i.putExtra("direccion", di);
                setResult(1, i);
                startActivity(i);
                finish();

            } else {
                Toast.makeText(DatosQuien.this, "Leido fallido", Toast.LENGTH_LONG).show();
            }
        }
    }
}
