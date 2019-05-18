package com.example.usuario.proyecto;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IllData extends AppCompatActivity {
    public static String use, nom, ape, dir, tel, enfermo, cdu, qr;
    EditText n, nombre, apellidos, telefono, direccion;
    private AccessServiceAPI miservicio;
    private ProgressDialog dialogo;
    TextView codun;
    ImageView c;
    public static Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ill_data);
        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        direccion = findViewById(R.id.domicilio);
        telefono = findViewById(R.id.telefono2);
        codun = findViewById(R.id.codigo);
        c = findViewById(R.id.qr_enfermo);
        miservicio = new AccessServiceAPI();

        coger();
        new TaskRegister().execute(enfermo, nombre.getText().toString(), apellidos.getText().toString(), telefono.getText().toString(), direccion.getText().toString(), "0");

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
                if ("".equals(telefono.getText().toString()) || (telefono.getText().length() < 9)) {
                    telefono.setError("Se requiere telefono");
                    return;
                }


                if (!"".equals(direccion.getText().toString()) && validarDireccion(direccion.getText().toString()) == false) {
                    direccion.setError("Añada una calle valida");
                    return;
                }
                new TaskRegister().execute(enfermo, nombre.getText().toString(), apellidos.getText().toString(), telefono.getText().toString(), direccion.getText().toString(), "1");
                codun.setText(cdu);
                QR(qr);
                c.setImageBitmap(bitmap);
                break;
            case R.id.salir:
                Intent i = new Intent(getApplicationContext(), DatosQuien.class);
                i.putExtra("iduser", use);
                startActivity(i);
                finish();
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
       /* cdu = getIntent().getStringExtra("codigounion");
        codun.setText(cdu);
        Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("BitmapImage");
        c.setImageBitmap(bitmap);*/
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
            postParam.put("direccion", params[4]);
            JSONObject jsonObject = null;
            //postParam.put("action", "enfermo");
            //llama al PHP
            try {
                if (params[5].contains("1")) {
                    String jsonString = miservicio.getJSONStringWithParam_POST(Common.SERVICE_API_URL, postParam);
                    jsonObject = new JSONObject(jsonString);
                }


                postParam.put("action", "QRyUnion");
                String jsonStringQR = miservicio.getJSONStringWithParam_POST(Common.SERVICE_API_URL, postParam);
                JSONObject jsonObjectQR = new JSONObject(jsonStringQR);


                cdu = jsonObjectQR.getString("Union");
                //  codun.setText(cdu);
                qr = jsonObjectQR.getString("Qr");
                QR(qr);
                c.setImageBitmap(bitmap);
                //qr=jsonObject.getString("Encriptado");
                if (params[5].contains("1")) {
                    return jsonObject.getInt("result");
                } else {
                    return 10;
                }
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
                codun.setText(cdu);


            } else {
                if (integer == 10) {
                    if (cdu != null) {
                        codun.setText(cdu);

                    }
                    return;
                } else
                    Toast.makeText(IllData.this, "Registro fallido", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void QR(String v) {
        MultiFormatWriter formaescribir = new MultiFormatWriter();//es una clase que forma el codigo qr
        try {
            BitMatrix codigo = formaescribir.encode(qr, BarcodeFormat.QR_CODE, 164, 196);
            //indicas las medidas del codigoqr y con que lo vas a componer

            BarcodeEncoder codigoqr = new BarcodeEncoder();
            //aqui generas el codigo qr
            bitmap = codigoqr.createBitmap(codigo);
            //una funcion que crea el mapa de bits que es lo que compone el qr
            // qr.setImageBitmap(bit);
            //con esto sacaria en qr en una imagen
        } catch (WriterException e) {
            e.printStackTrace();
        }
        //indicas las medidas del codigoqr y con que lo vas a componer


    }

    public boolean validarDireccion(String direccion) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> list = geocoder.getFromLocationName(direccion + ",Madrid,España", 10);


            if (!list.isEmpty() && !list.get(0).getFeatureName().equals("Madrid"))
                return true;
            else
                return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), DatosQuien.class);
        i.putExtra("iduser", use);
        startActivity(i);
        finish();
    }


}
