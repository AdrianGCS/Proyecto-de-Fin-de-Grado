package com.example.usuario.proyecto;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.ActivityCompat;
import android.location.LocationProvider;
import android.provider.Settings;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;

import org.json.JSONObject;

public class Sms extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    Button dale;
    TextView telefono, imai, ide;
    TextView la;
    TextView lo, calle;
    public static String n;
    public static String Text;
    public static String longi;
    public static String idess;
    public static String imei;
    private AccessServiceAPI miser;
    private Dialog midialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        Toast.makeText(getApplicationContext(), "Enviar sms tiene cargo", Toast.LENGTH_LONG).show();
        dale = (Button) findViewById(R.id.dale);
        telefono = findViewById(R.id.ono);
        la = (TextView) findViewById(R.id.longitud);
        lo = (TextView) findViewById(R.id.latitud);
        calle = findViewById(R.id.dire);
        imai = findViewById(R.id.im);
        ide = findViewById(R.id.ide);
        miser = new AccessServiceAPI();
        cogerDatos();



        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest
                        .permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.SEND_SMS,}, 1000);
        } else {
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
        dale.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                enviarMensaje(n, calle.getText().toString());
                new TaskRegister().execute(idess,lo.getText().toString(),la.getText().toString() ,imei);
            }
        });
    }

    public void cogerDatos() {

        n = getIntent().getStringExtra("tel");
        telefono.setText(n);
        idess = getIntent().getStringExtra("id_enfermo");
        ide.setText(idess);
        imei = getIntent().getStringExtra("imei");
        imai.setText(imei);
    }

    private void enviarMensaje(String n, String mensaje) {
        try {

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(n, null, mensaje, null, null);
            Toast.makeText(getApplicationContext(), "Mensaje Enviado.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Mensaje no enviado, datos incorrectos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setSms(this);
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
        la.setText("Localización agregada");
        lo.setText("");
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    calle.setText(""
                            + DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        Sms sms;

        public Sms getSms() {
            return sms;
        }

        public void setSms(Sms sms) {
            this.sms = sms;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            loc.getLatitude();
            loc.getLongitude();
            Text = "" + loc.getLatitude();
            longi = "" + loc.getLongitude();
            la.setText(Text);
            lo.setText(longi);
            this.sms.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            la.setText(Text);
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            la.setText(Text);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }


  public class TaskRegister extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            midialogo = ProgressDialog.show(Sms.this, "Espere un momento", "Procesando datos...", true);

        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "Local");
            postParam.put("id_enfermo", params[0]);
            postParam.put("length", params[1]);
            postParam.put("altitude", params[2]);
            postParam.put("imei", params[3]);
            //llama al PHP

            try {
                String jsonString = miser.getJSONStringWithParam_POST(Common.SERVICE_API_URL, postParam);
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
                Toast.makeText(Sms.this, "Registrado con exito", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), Sms.class);
                i.putExtra("latitud", la.getText() + "");
                i.putExtra("longitud", lo.getText() + "");
                i.putExtra("telefono", telefono.getText() + "");
                i.putExtra("direccion", calle.getText() + "");
                setResult(1, i);
                startActivity(i);

                finish();

            } else {
                Toast.makeText(Sms.this, "Localizacion fallida", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(this, MainActivity.class));


    }
}


