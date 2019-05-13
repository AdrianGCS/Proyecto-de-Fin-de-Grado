package com.example.usuario.proyecto;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
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
    public Spinner spi;
    public static ArrayList<String> dat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermedia);
        miser = new AccessServiceAPI();
        spi = findViewById(R.id.sp);
        coger();

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





                    return jsonObject.getInt("result");


                //datosE();
                //dat.add(b.getString("Nombre") + "," + b.getString("Apellido") + "," + b.getString("Telefono"));


                //coge los datos que le pasa php


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
                /*Intent i = new Intent(getApplicationContext(), Intermedia.class);
                i.putExtra("nombre", no);
                i.putExtra("apellidos", ap);
                i.putExtra("telefono", te);
                i.putExtra("direccion", di);
                setResult(1, i);
                startActivity(i);
                finish();

*/
                for (int i = 0; i < a.length(); i++) {

                    try {
                    b = a.getJSONObject(i);



                        dat.add(b.getString("Nombre") + "," + b.getString("Apellido") + "," + b.getString("Telefono"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ArrayAdapter cvc = new ArrayAdapter(Intermedia.this, android.R.layout.simple_spinner_dropdown_item, dat);
                    spi.setAdapter(cvc);

                    /*spi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String x = (String) spi.getAdapter().getItem(position);
                            Toast.makeText(Intermedia.this, ""x, Toast.LENGTH_LONG).show();
                        }
                    });*/
                    //cv = b.getString("Nombre") + "," + b.getString("Apellido") + "," + b.getString("Telefono");
                }
            } else {
                Toast.makeText(Intermedia.this, "Leido fallido", Toast.LENGTH_LONG).show();
            }
        }
    }

    /*public void datosE() {


            for (int i = 0; i < a.length(); i++) {


                try {

                    b = a.getJSONObject(i);
                   // ArrayList<String> dat = new ArrayList<>();
                    dat.add(b.getString("Nombre") + "," + b.getString("Apellido") + "," + b.getString("Telefono"));

                    ArrayAdapter cvc = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dat);
                    spi.setAdapter(cvc);

                    spi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String x = (String) spi.getAdapter().getItem(position);
                            Toast.makeText(Intermedia.this, x, Toast.LENGTH_LONG).show();
                        }
                    });


                    //cv = b.getString("Nombre") + "," + b.getString("Apellido") + "," + b.getString("Telefono");


                } catch (JSONException e) {
                    e.printStackTrace();
                }




        }


    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent m = new Intent(this, DatosQuien.class);
        m.putExtra("iduser", ids);
        startActivity(m);
        finish();
    }
}
