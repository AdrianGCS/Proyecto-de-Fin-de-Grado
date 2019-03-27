package com.example.usuario.proyecto;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.String;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreacionEnfermo extends AppCompatActivity {
    private EditText nombre;
    private EditText apellidos;
    private EditText telefono;
    private EditText calle;
    private AccessServiceAPI miser;
    private Dialog midialogo;
    private Button boto;
    ImageView qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_enfermo);
        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        telefono = findViewById(R.id.telefono);
        miser = new AccessServiceAPI();
        boto = findViewById(R.id.entrar);
    }

    public void onClick(View view) {
        Pattern pattern = Pattern.compile("^(?:(?:\\+|00)?34)?[89]\\d{8}$");
        if ("".equals(nombre.getText().toString())) {
            nombre.setError("Introduce el nombre");
            return;
        }
        if ("".equals(apellidos.getText().toString())) {
            apellidos.setError("Introduce los apellidos");
            return;
        }
        if ("".equals(telefono.getText().toString())) {
            telefono.setError("Introduce el telefono");
            return;
        }
        Matcher mather = pattern.matcher(telefono.getText());
        if (mather.find() == false) {
            telefono.setError("Introduce telefono valido");
            return;
        }
        new TaskRegister().execute(nombre.getText().toString(), apellidos.getText().toString(), telefono.getText().toString());

    }

    public class TaskRegister extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            midialogo = ProgressDialog.show(CreacionEnfermo.this, "Espere un momento", "Procesando datos...", true);

        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "add");
            postParam.put("username", params[0]);
            postParam.put("phone", params[1]);
            //postParam.put("adress", params[2]); opcional ya que es la direccion
            postParam.put("lastname", params[3]);
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
                Toast.makeText(CreacionEnfermo.this, "Registrado con exito", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), Login.class);
                i.putExtra("nombre", nombre.getText().toString());
                i.putExtra("apellidos", apellidos.getText().toString());
                i.putExtra("telefono", telefono.getText().toString());
                i.putExtra("direccion", calle.getText().toString());
                setResult(1, i);
                finish();
            } else if (integer == Common.RESULT_USER_EXISTS) {
                Toast.makeText(CreacionEnfermo.this, "El usuario ya existe en la base de datos", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(CreacionEnfermo.this, "Registro fallido", Toast.LENGTH_LONG).show();
            }
        }
    }

    /*private void tomar() {
        boto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                darle();
            }

            private void darle() {
                String a = nombre.getText().toString() + "/" + apellidos.getText().toString() + "/";
                MultiFormatWriter formaescribir = new MultiFormatWriter();
                try {
                    BitMatrix codigo = formaescribir.encode(a, BarcodeFormat.QR_CODE, 164, 196);
                    BarcodeEncoder codigoqr = new BarcodeEncoder();
                    Bitmap bit = codigoqr.createBitmap(codigo);
                    qr.setImageBitmap(bit);

                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void nume() {
        char[] chars = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
        int s = chars.length;

        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 5; i++) {
            buffer.append(chars[random.nextInt(chars.length)]);

        }
        buffer.toString();
    }*/
}
