package com.example.usuario.proyecto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UnirseEnfermo extends AppCompatActivity {
    private AccessServiceAPI miservicio;
    private ProgressDialog midialogo;
    EditText nombre, apellidos, codigoU;
    public static String idefami, codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unirse_enfermo);
        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        codigoU = findViewById(R.id.codigo);

        miservicio = new AccessServiceAPI();
        c();
    }

    public void onClick(View view) {


        if ("".equals(nombre.getText().toString())) {
            nombre.setError("Introduce el nombre");
            return;
        }
        if ("".equals(apellidos.getText().toString())) {
            apellidos.setError("Introduce los apellidos");
            return;
        }
        if ("".equals(codigoU.getText().toString())) {
            codigoU.setError("Introduce el codigo union");
            return;
        }


        new TaskRegister().execute(nombre.getText().toString(), apellidos.getText().toString(), codigoU.getText().toString(), idefami);

    }

    public void c() {
        idefami = getIntent().getStringExtra("iduser");
    }

    public class TaskRegister extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            midialogo = ProgressDialog.show(UnirseEnfermo.this, "Espere un momento", "Procesando datos...", true);

        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "union");
            postParam.put("username", params[0]);
            postParam.put("lastname", params[1]);
            postParam.put("code", params[2]);
            postParam.put("id", idefami);


            //llama al PHP

            try {
                String jsonString = miservicio.getJSONStringWithParam_POST(Common.SERVICE_API_URL, postParam);
                JSONObject jsonObject = new JSONObject(jsonString);
         


                return jsonObject.getInt("result");


            } catch (Exception e) {
                e.printStackTrace();
                return Common.RESULT_ERROR;
            }


        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            midialogo.dismiss();
            if (integer == Common.RESULT_SUCCESS) {
                Toast.makeText(UnirseEnfermo.this, "Union  con exito", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), MenuUser.class);
                i.putExtra("iduser", idefami);
                setResult(1, i);
                startActivity(i);

                finish();

            } else {
                Toast.makeText(UnirseEnfermo.this, "Union  fallida", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onBackPressed() {
        Intent j = new Intent(this, OpcionUser.class);
        j.putExtra("iduser", idefami);
        startActivity(j);
        //startActivity(new Intent(OpcionUser.this, OpcionUser.class));
    }
}


