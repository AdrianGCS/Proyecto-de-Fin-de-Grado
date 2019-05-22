package com.example.usuario.proyecto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Principal extends AppCompatActivity {
    Bundle datos;
    public static String telefono = "";
    public static String fone = "",calle,longitud,latitud;
    TextView phon, hy, u;
    public static String ids = "";
    public static String imei = "";
    public static String direccion = "";
    Button lo;
    public static JSONArray a;
    public static JSONObject localizacion;
    public static JSONObject modificacion;
    public static String mod;
    public static String loc;

    private AccessServiceAPI miservicio;
    private ProgressDialog midialogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        phon = findViewById(R.id.tele);
        hy = findViewById(R.id.idss);
        u = findViewById(R.id.imeiee);
        lo = findViewById(R.id.loca);
        miservicio = new AccessServiceAPI();
        cogerDatos();
        //new TaskRegister().execute(ids);
    }

    public void onClick(View view) {
        /*Intent intento= new Intent(Principal.this,Login.class);
        startActivity(intento);
        */
        switch (view.getId()) {

            case R.id.envio:
                startActivity(new Intent(this, Sms.class));
                enviarDatoss();
                break;
            case R.id.llamada:
                startActivity(new Intent(this, LLamar.class));
                enviarDatos();
                break;
            case R.id.loca:
                Intent i = new Intent(getApplicationContext(), Localizacion.class);
                i.putExtra("direccion", direccion);
                i.putExtra("telefono", telefono);
                i.putExtra("id_enfermo", ids);
                i.putExtra("imei", imei);
                startActivity(i);
                finish();
                break;
            case R.id.salir:
                startActivity(new Intent(this, MainActivity.class));
                break;

        }
    }

    private void cogerDatos() {
        telefono = getIntent().getStringExtra("telefono");
        ids = getIntent().getStringExtra("id_enfermo");
        imei = getIntent().getStringExtra("imei");
        direccion = getIntent().getStringExtra("direccion");
        calle=getIntent().getStringExtra("calle");
        longitud=getIntent().getStringExtra("longitd");
        latitud=getIntent().getStringExtra("latitud");
     if ("".equals(direccion) || direccion == null ) {

         lo.setEnabled(false);
        } else {
         lo.setEnabled(true);
        }
        hy.setText(ids);
        phon.setText(telefono);
        u.setText(imei);
    }

    private void enviarDatos() {

        Intent i = new Intent(getApplicationContext(), LLamar.class);
        i.putExtra("telefono", telefono);
        i.putExtra("id_enfermo", ids);
        i.putExtra("imei", imei);


        startActivity(i);
        finish();
    }

    private void enviarDatoss() {


        Intent i = new Intent(getApplicationContext(), Sms.class);
        i.putExtra("telefono", telefono);
        i.putExtra("id_enfermo", ids);
        i.putExtra("imei", imei);
        startActivity(i);
        finish();
    }
    public class TaskRegister extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            midialogo = ProgressDialog.show(Principal.this, "Espere un momento", "Procesando datos...", true);

        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "verificar");
            postParam.put("id", ids);
            //llama al PHP

            try {
                String jsonString = miservicio.getJSONStringWithParam_POST(Common.SERVICE_API_URL, postParam);
                JSONObject jsonObject = new JSONObject(jsonString);
                a = jsonObject.getJSONArray("Permisos");

                localizacion = a.getJSONObject(0);
                loc=localizacion.getString("Localizacion");
                modificacion = a.getJSONObject(1);
                mod=modificacion.getString("Modificacion");

                if ("".equals(direccion) || direccion == null || loc.equals(0) ) {

                    lo.setEnabled(false);
                } else {
                    lo.setEnabled(true);
                }

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
                Toast.makeText(Principal.this, "Registrado con exito", Toast.LENGTH_LONG).show();
                /*Intent i = new Intent(getApplicationContext(), Permisos.class);
                i.putExtra("localizacion", loc);
                i.putExtra("modificacion", mod);
                i.putExtra("iduser", ids);
                setResult(1, i);
                startActivity(i);

                finish();
*/
            } else {
                Toast.makeText(Principal.this, "Union  fallida", Toast.LENGTH_LONG).show();
            }

        }
    }


}


