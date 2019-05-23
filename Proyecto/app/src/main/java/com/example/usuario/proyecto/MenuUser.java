package com.example.usuario.proyecto;

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

public class MenuUser extends AppCompatActivity {
    public static String id;
    public static String iduser;
    public static JSONObject localizacion;
    public static JSONObject modificacion;
    public static String mod;
    public static String loc;
    TextView ids, bnd;
    private AccessServiceAPI miservicio;
    private ProgressDialog midialogo;
    public static JSONObject a;
    public static Intent p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_user);
        ids = findViewById(R.id.idenfermo);
        miservicio = new AccessServiceAPI();
        cogerDatos();
    }

    public void onClick(View view) {
        /*Intent intento= new Intent(Principal.this,Login.class);
        startActivity(intento);
        */
        switch (view.getId()) {

            case R.id.datos:
                Intent i = new Intent(this, DatosQuien.class);
                i.putExtra("iduser", iduser);
                //i.putExtra("modificacion",mod);
                //i.putExtra("id_enfermo", id);
                startActivity(i);
                finish();
                break;
            case R.id.permisos:

                new TaskRegister().execute(iduser);
               p = new Intent(this, Permisos.class);

                break;
            case R.id.calendario:
                Intent b = new Intent(this, Calendario.class);
                b.putExtra("iduser", iduser);
                // p.putExtra("id_enfermo", id);
                startActivity(b);
                finish();
                break;
            case R.id.historial:
                Intent c = new Intent(this, Historial.class);
                c.putExtra("iduser", iduser);
                // p.putExtra("id_enfermo", id);
                startActivity(c);
                finish();
                break;
            case R.id.cuni:
                Intent n = new Intent(this, OpcionUser.class);
                n.putExtra("iduser", iduser);
                // p.putExtra("id_enfermo", id);
                startActivity(n);
                finish();
                break;
        }
    }

    public void cogerDatos() {
        id = getIntent().getStringExtra("id_enfermo");
        //ids.setText(id);*/
        iduser = getIntent().getStringExtra("iduser");
        ids.setText(iduser);
    }

   public class TaskRegister extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            midialogo = ProgressDialog.show(MenuUser.this, "Espere un momento", "Procesando datos...", true);

        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "pedirpermisos");
            postParam.put("id", iduser);
            //llama al PHP

            try {
                String jsonString = miservicio.getJSONStringWithParam_POST(Common.SERVICE_API_URL, postParam);
                JSONObject jsonObject = new JSONObject(jsonString);
                a = jsonObject.getJSONObject("permisos");


                loc=a.getString("Localizacion");

                mod=a.getString("Modificacion");
                return jsonObject.getInt("result");


            } catch (Exception e) {
                e.printStackTrace();
                return Common.RESULT_ERROR;
            }


        }


        @Override
        protected void onPostExecute(Integer integer) {
            p.putExtra("iduser", iduser);
            p.putExtra("localizacion", loc);
            p.putExtra("modificacion", mod);
            startActivity(p);
            finish();
            super.onPostExecute(integer);
            midialogo.dismiss();
            if (integer == Common.RESULT_SUCCESS) {
                Toast.makeText(MenuUser.this, "Registrado con exito", Toast.LENGTH_LONG).show();


            } else {
                Toast.makeText(MenuUser.this, "Union  fallida", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

}
