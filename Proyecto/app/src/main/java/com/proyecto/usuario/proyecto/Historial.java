package com.proyecto.usuario.proyecto;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Historial extends AppCompatActivity {
    static int numBotones = 2;
    private Dialog midialogo;
    private AccessServiceAPI miser;
    public static String no, ap, di, te, cv,qr;
    public static JSONArray a;

    public static String ids,cud;
    public static JSONObject b;
    public static JSONObject c;
    public static Parcelable y;
    public static Serializable x;
    public static CharSequence z;
    public Spinner spi;
    public static ArrayList<String> dat = new ArrayList<>();
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermedia);
        miser = new AccessServiceAPI();
        spi = findViewById(R.id.sp);
        button = findViewById(R.id.button);
        coger();

        new TaskRegister().execute(ids);


    }

    public void coger() {
        ids = getIntent().getStringExtra("iduser");
cv=getIntent().getStringExtra("idEnfermo");


    }


    public class TaskRegister extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            midialogo = ProgressDialog.show(Historial.this, "Espere un momento", "Procesando datos...", true);

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


//qr();
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
                Toast.makeText(Historial.this, "Cargados con exito", Toast.LENGTH_LONG).show();
                /*Intent i = new Intent(getApplicationContext(), Intermedia.class);
                i.putExtra("nombre", no);
                i.putExtra("apellidos", ap);
                i.putExtra("telefono", te);
                i.putExtra("direccion", di);
                setResult(1, i);
                startActivity(i);
                finish();

*/
                spi.setSelection(0);
                dat.clear();
                for (int i = 0; i < a.length(); i++) {

                    try {
                        b = a.getJSONObject(i);


                        dat.add(b.getString("Nombre") + "," + b.getString("Apellido") + "," + b.getString("Telefono"));
                        //spi.getAdapter().getItem(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ArrayAdapter cvc = new ArrayAdapter(Historial.this, android.R.layout.simple_spinner_dropdown_item, dat);
                    spi.setAdapter(cvc);

                   /*spi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                String x = (String) spi.getAdapter().getItem(position);
                                Intent i = new Intent(getApplicationContext(), IllData.class);
                                i.putExtra("nombre", no);

                                setResult(1, i);
                                startActivity(i);
                             finish();


                        }
                    });*/
                    //cv = b.getString("Nombre") + "," + b.getString("Apellido") + "," + b.getString("Telefono");
                }
                // qr();
            } else {
                Toast.makeText(Historial.this, "Leido fallido", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onClick(View view){



        try {

            int posicion =  spi.getSelectedItemPosition();
            b = a.getJSONObject(posicion);


            Intent i = new Intent(getApplicationContext(), Localizaciones.class);
            i.putExtra("iduser", ids);
            i.putExtra("idEnfermo", b.getString("ID"));
            i.putExtra("nombre", b.getString("Nombre"));
            i.putExtra("apellido", b.getString("Apellido"));
            i.putExtra("telefono", b.getString("Telefono"));
            i.putExtra("direccion", b.getString("Direccion"));
            //i.putExtra("Bitmap",bitmap);
            //i.putExtra("codigounion",cud);
            setResult(1, i);
            startActivity(i);
            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }




        // Intent i=new Intent(Datos_Enfermo.this,MenuUser.class);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent m = new Intent(this, MenuUser.class);
        m.putExtra("iduser", ids);
        startActivity(m);
        finish();
    }
}
