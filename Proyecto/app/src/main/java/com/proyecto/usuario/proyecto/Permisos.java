package com.proyecto.usuario.proyecto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Permisos extends AppCompatActivity {
    public static String ids;
    TextView cid;
    private AccessServiceAPI miservicio;
    private ProgressDialog midialogo;
    Switch lo, mo;
    public static String modificacion = "0",localizacion = "1";
    Button bu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permisos);
        cid = findViewById(R.id.iduse);
        lo = findViewById(R.id.localix);
        mo = findViewById(R.id.escritura);
        bu = findViewById(R.id.guar);
        miservicio = new AccessServiceAPI();
        coger();

    }

    public void coger() {
        ids = getIntent().getStringExtra("iduser");
        cid.setText(ids);
      modificacion=getIntent().getStringExtra("modificacion");
        localizacion=getIntent().getStringExtra("localizacion");
        if(modificacion.equals("1")){
            mo.setChecked(true);
        }else{
            mo.setChecked(false);
        }
        if (localizacion.equals("1")){
            lo.setChecked(true);
        }else{
            lo.setChecked(false);
        }
    }

    public void onClick(View view) {

        if (lo.isChecked()) {
            lo.setChecked(true);
            localizacion = "1";
            Toast.makeText(Permisos.this, "El permiso de localizacion esta activo:"+lo.getTextOn().toString() + localizacion , Toast.LENGTH_LONG).show();
        } else {
            lo.setChecked(false);
            localizacion = "0";
            Toast.makeText(Permisos.this, "El permiso de localizacion esta desactivo: "+lo.getTextOff().toString() + localizacion , Toast.LENGTH_LONG).show();
        }

        if (mo.isChecked()) {
            mo.setChecked(true);
            modificacion= "1";
            Toast.makeText(Permisos.this, "El permiso de modificacion esta activo"+mo.getTextOn().toString() + modificacion, Toast.LENGTH_LONG).show();
        } else {

            mo.setChecked(false);
            modificacion = "0";

            Toast.makeText(Permisos.this, "El permiso de localizacion esta desactivo" + mo.getTextOff().toString() + modificacion, Toast.LENGTH_LONG).show();
        }


        //exec task register
        new TaskRegister().execute(localizacion, modificacion, ids);


    }

    public class TaskRegister extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            midialogo = ProgressDialog.show(Permisos.this, "Espere un momento", "Procesando datos...", true);

        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "permisos");
            postParam.put("localizacion", params[0]);
            postParam.put("modificacion", params[1]);
            postParam.put("id", ids);
            //llama al PHP

            try {
                String jsonString = miservicio.getJSONStringWithParam_POST(Common.SERVICE_API_URL, postParam);
                JSONObject jsonObject = new JSONObject(jsonString);

                // id_enfermo = jsonObject.getString("id");


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
                Toast.makeText(Permisos.this, "Registrado con exito", Toast.LENGTH_LONG).show();
            /*Intent i = new Intent(getApplicationContext(), Datos_Enfermo.class);
                i.putExtra("nombre", nombre.getText() + "");
                i.putExtra("apellidos", apellidos.getText() + "");
                i.putExtra("codigounion", codigoU.getText() + "");
                i.putExtra("iduser", idefami);
                setResult(1, i);
                startActivity(i);

                finish();
*/
            } else {
                Toast.makeText(Permisos.this, "Union  fallida", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MenuUser.class);
        i.putExtra("iduser", ids);
        startActivity(i);
        finish();
    }
}
