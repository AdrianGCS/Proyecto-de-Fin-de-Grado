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

import java.lang.reflect.Array;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IllData extends AppCompatActivity {
    public static String use, nom, ape, dir, tel, enfermo;
    TextView n, nombre, apellidos, telefono, direccion;
    private AccessServiceAPI miservicio;
    private ProgressDialog dialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ill_data);
        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        direccion = findViewById(R.id.direccion2);
        telefono = findViewById(R.id.telefono2);
        miservicio = new AccessServiceAPI();
        coger();

    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.guardar:
                if ("".equals(nombre.getText().toString())) {
                    nombre.setError("Se requiere Nombre");
                    return;
                }
                if ("".equals(apellidos.getText().toString())) {
                    apellidos.setError("Se requiere apellidos");
                    return;
                }
                if ("".equals(telefono.getText().toString())) {
                    telefono.setError("Se requiere telefono");
                    return;
                }


                if ("".equals(direccion.getText().toString())) {
                    direccion.setError("Mete la direccion");
                    return;
                }
                new TaskRegister().execute(enfermo, nombre.getText().toString(), apellidos.getText().toString(), telefono.getText().toString(), direccion.getText().toString());
                break;
            case R.id.log:
                startActivity(new Intent(IllData.this, DatosQuien.class));
                break;


        }


    }


    public void coger() {
        use = getIntent().getStringExtra("iduser");
        //n.setText(ids);
        enfermo = getIntent().getStringExtra("idEnfermo");
        nom = getIntent().getStringExtra("nombre");
        nombre.setText(nom);
        ape = getIntent().getStringExtra("apellido");
        apellidos.setText(ape);
        tel = getIntent().getStringExtra("telefono");
        telefono.setText(tel);
        dir = getIntent().getStringExtra("direccion");
        direccion.setText(dir);

    }

    public class TaskRegister extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogo = ProgressDialog.show(IllData.this, "Espere un momento", "Procesando datos...", true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "ActualizarEn");
            postParam.put("id", params[0]);
            postParam.put("nombre", params[1]);
            postParam.put("apellido", params[2]);
            postParam.put("telefono", params[3]);
            postParam.put("direccion", params[3]);

            //llama al PHP
            try {
                String jsonString = miservicio.getJSONStringWithParam_POST(Common.SERVICE_API_URL, postParam);
                JSONObject jsonObject = new JSONObject(jsonString);
                //id= jsonObject.getString("id");
                return jsonObject.getInt("result");
            } catch (Exception e) {
                e.printStackTrace();
                return Common.RESULT_ERROR;
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dialogo.dismiss();
            if (integer == Common.RESULT_SUCCESS) {
                Toast.makeText(IllData.this, "Registrado con exito", Toast.LENGTH_LONG).show();
               // Intent i = new Intent(getApplicationContext(), IllData.class);
               // i.putExtra("nombre", nombre.getText().toString());
               // i.putExtra("apellidos", apellidos.getText().toString());
               // i.putExtra("correo", telefono.getText().toString());
               // i.putExtra("direccion", direccion.getText().toString());
               // setResult(1, i);
               // startActivity(i);
               // finish();
            } else {
                Toast.makeText(IllData.this, "Registro fallido", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MenuUser.class);
        i.putExtra("iduser", use);
        startActivity(i);
        finish();
    }


}
