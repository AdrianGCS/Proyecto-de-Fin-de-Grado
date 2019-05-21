package com.example.usuario.proyecto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MenuUser extends AppCompatActivity {
    public static String id;
    public static String iduser,localizacion,modificacion;
    TextView ids, bnd;
    private AccessServiceAPI miservicio;
    private ProgressDialog midialogo;
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
                //i.putExtra("id_enfermo", id);
                startActivity(i);
                finish();
                break;
            case R.id.permisos:
                Intent p = new Intent(this, Permisos.class);
                p.putExtra("iduser", iduser);
                //i.putExtra("id_enfermo", id);
                startActivity(p);
                finish();
               // new TaskRegister().execute(localizacion,modificacion,iduser);
                break;
            case R.id.calendario:
                Intent b = new Intent(this, Calendario.class);
                b.putExtra("iduser", iduser);
                // p.putExtra("id_enfermo", id);
                startActivity(b);
                finish();
                break;
            case R.id.historial:
                Intent c = new Intent(this, Permisos.class);
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

   /* public class TaskRegister extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            midialogo = ProgressDialog.show(MenuUser.this, "Espere un momento", "Procesando datos...", true);

        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "pedirpermisos");
            postParam.put("localizacion", params[0]);
            postParam.put("modificacion", params[1]);
            postParam.put("id", iduser);
            //llama al PHP

            try {
                String jsonString = miservicio.getJSONStringWithParam_POST(Common.SERVICE_API_URL, postParam);
                JSONObject jsonObject = new JSONObject(jsonString);

                localizacion=jsonObject.getString("localizacion");
                modificacion=jsonObject.getString("modificacion");


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
                Toast.makeText(MenuUser.this, "Registrado con exito", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), Permisos.class);
                i.putExtra("localizacion", localizacion);
                i.putExtra("modificacion", modificacion);
                i.putExtra("iduser", iduser);
                setResult(1, i);
                startActivity(i);

                finish();

            } else {
                Toast.makeText(MenuUser.this, "Union  fallida", Toast.LENGTH_LONG).show();
            }

        }
    }*/

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

}
