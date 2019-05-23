package com.example.usuario.proyecto;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    public static String mod,lati,longi;
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new
                    String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {


                    //locationStart();


        }
    }

    public void onClick(View view) {
        /*Intent intento= new Intent(Principal.this,Login.class);
        startActivity(intento);
        */
       // new TaskRegister().execute(ids,lati,longi,imei);
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
            if (params[1]==null) {
                postParam.put("action", "verificar");
                postParam.put("id", params[0]);
                //llama al PHP
            }else{
                postParam.put("action", "Local");
                postParam.put("id_enfermo", params[0]);
                postParam.put("altitude", params[1]);
                postParam.put("length", params[2]);
                postParam.put("imei", params[3]);
            }
            try {
                String jsonString = miservicio.getJSONStringWithParam_POST(Common.SERVICE_API_URL, postParam);
                JSONObject jsonObject = new JSONObject(jsonString);
                if(params[1]==null) {
                    a = jsonObject.getJSONArray("Permisos");

                    localizacion = a.getJSONObject(0);
                    loc = localizacion.getString("Localizacion");
                    modificacion = a.getJSONObject(1);
                    mod = modificacion.getString("Modificacion");

                    if ("".equals(direccion) || direccion == null || loc.equals(0)) {

                        lo.setEnabled(false);
                    } else {
                        lo.setEnabled(true);
                    }
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
 /*   private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);

    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }
    /* Aqui empieza la Clase Localizacion */
    /*public class Localizacion implements LocationListener {
        Principal mainActivity;
        public Principal getMainActivity() {
            return mainActivity;
        }
        public void setMainActivity(Principal mainActivity) {
            this.mainActivity = mainActivity;
        }
        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            longi= String.valueOf(loc.getLatitude());
            lati= String.valueOf(loc.getLongitude());


        }*/
       /* @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado

        }
        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado

        }*/
       // @Override
        //public void onStatusChanged(String provider, int status, Bundle extras) {
         //   switch (status) {
           //     case LocationProvider.AVAILABLE:
               //     Log.d("debug", "LocationProvider.AVAILABLE");
             //       break;
              //  case LocationProvider.OUT_OF_SERVICE:
             //       Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
             //       break;
             //   case LocationProvider.TEMPORARILY_UNAVAILABLE:
               //     Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
              //      break;
          //  }
        //}
  //  }
}





