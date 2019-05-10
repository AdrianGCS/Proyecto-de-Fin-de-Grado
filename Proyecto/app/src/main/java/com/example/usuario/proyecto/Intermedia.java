package com.example.usuario.proyecto;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class Intermedia extends AppCompatActivity {
    static int numBotones = 2;
    private Dialog midialogo;
    private AccessServiceAPI miser;
    public static String no, ap, di, te, cv;
    public static JSONArray a;
    public static String ids;
    public static JSONObject b;
    public static JSONObject c;
    public static Parcelable y;
    public static Serializable x;
    public static CharSequence z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermedia);
        miser = new AccessServiceAPI();
        coger();
        LinearLayout llBotonera = (LinearLayout) findViewById(R.id.llBotonera);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        new TaskRegister().execute(ids);


    }

    public void coger() {
        ids = getIntent().getStringExtra("iduser");


    }

    public class TaskRegister extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            midialogo = ProgressDialog.show(Intermedia.this, "Espere un momento", "Procesando datos...", true);

        }

        @Override
        protected Integer doInBackground(String... params) {

            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "DatosEnfermo");
            postParam.put("id", params[0]);
            //llama al PHP y envia los datos

            try {
                String jsonString = miser.getJSONStringWithParam_POST(Common.SERVICE_API_URL, postParam);
                JSONObject jsonObject = new JSONObject(jsonString);
                a = jsonObject.getJSONArray("datos");
                datosE();

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
                Toast.makeText(Intermedia.this, "Leido  con exito", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), Principal.class);
                i.putExtra("nombre", no);
                i.putExtra("apellidos", ap);
                i.putExtra("telefono", te);
                i.putExtra("direccion", di);
                setResult(1, i);
                startActivity(i);
                finish();

            } else {
                Toast.makeText(Intermedia.this, "Leido fallido", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void datosE() {


        for (int i = 0; i < a.length(); i++) {


            try {
                LinearLayout llBotonera = (LinearLayout) findViewById(R.id.llBotonera);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Button button = new Button(this);
                b = a.getJSONObject(i);


                //Asignamos propiedades de layout al boton

                //Asignamos Texto al botón
                cv = b.getString("Nombre") + "," + b.getString("Apellido") + "," + b.getString("Telefono");
                button.setLayoutParams(lp);
                button.setText(cv);

                //Asignamose el Listener
                //Añadimos el botón a la botonera
                llBotonera.addView(button);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

    class ButtonsOnClickListener implements View.OnClickListener {
        public ButtonsOnClickListener(Intermedia intermedia) {

        }

        @Override
        public void onClick(View v) {


            Toast.makeText(getApplicationContext(), "pedro", Toast.LENGTH_SHORT).show();

        }

    }


}
