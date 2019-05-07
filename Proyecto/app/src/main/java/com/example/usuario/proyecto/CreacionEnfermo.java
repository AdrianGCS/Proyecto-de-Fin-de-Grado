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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.String;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

public class CreacionEnfermo extends AppCompatActivity {
    private EditText nombre;
    private EditText apellidos;
    private EditText telefono;
    private EditText calle;
    private EditText post;
    private AccessServiceAPI miser;
    private Dialog midialogo;
    private Button boto;
    private ImageView qr;
    private String a;
    public static Bitmap bitmap;
    public static String codigo;
    public static String idefami;
    public static String id_enfermo;
    TextView idsss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_enfermo);
        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        telefono = findViewById(R.id.telefono);
        miser = new AccessServiceAPI();
        boto = findViewById(R.id.salir);
        calle = findViewById(R.id.direccion);
        post=findViewById(R.id.postal);
        idsss = findViewById(R.id.isx);
        coger();

    }

    public void onClick(View view) {
        //  Pattern pattern = Pattern.compile("^(?:(?:\\+|00)?34)?[89]\\d{8}$");
        if (!"".equals(calle.getText().toString()) && validarDireccion(calle.getText().toString()) == false) {
            calle.setError("Añada una calle valida");
            return;
        }


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
        if (telefono.getText().length() < 9) {
            telefono.setError("Introduce un numero de telefono correcto");
            return;
        }
       if(!post.equals("28") && post.length()<5){
           post.setError("introduce un codigo correcto");
           return;
       }

        new TaskRegister().execute(nombre.getText().toString(), apellidos.getText().toString(), telefono.getText().toString(), calle.getText().toString()+","+post.getText().toString(), idefami);

    }

    public void coger() {
        idefami = getIntent().getStringExtra("id_familiar");
        idsss.setText(idefami);
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
            postParam.put("action", "enfermo");
            postParam.put("username", params[0]);
            postParam.put("lastname", params[1]);
            postParam.put("phone", params[2]);
            postParam.put("adress", params[3]);
            postParam.put("id", idefami);

            //postParam.put("qr", params[4]);
            //llama al PHP

            try {
                String jsonString = miser.getJSONStringWithParam_POST(Common.SERVICE_API_URL, postParam);
                JSONObject jsonObject = new JSONObject(jsonString);
                a = jsonObject.getString("Encriptado");
                codigo = jsonObject.getString("codigo");
               // id_enfermo = jsonObject.getString("id");

                if (!a.equals("8")) {
                    darle(a);
                    return jsonObject.getInt("result");

                } else {
                    return 1;
                }

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
                Intent i = new Intent(getApplicationContext(), Datos_Enfermo.class);
                i.putExtra("nombre", nombre.getText() + "");
                i.putExtra("apellidos", apellidos.getText() + "");
                i.putExtra("telefono", telefono.getText() + "");
                i.putExtra("direccion", calle.getText() + ","+post.getText().toString());
                i.putExtra("BitmapImage", bitmap);
                i.putExtra("Codigo", codigo);
                i.putExtra("id_enfermo", id_enfermo);
                i.putExtra("iduser", idefami);
                setResult(1, i);
                startActivity(i);

                finish();

            } else if (integer == Common.RESULT_USER_EXISTS) {
                Toast.makeText(CreacionEnfermo.this, "El usuario ya existe en la base de datos", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(CreacionEnfermo.this, "Registro fallido", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void darle(String c) {
        //c = nombre.getText().toString() + "/" + apellidos.getText().toString() + "/";
        MultiFormatWriter formaescribir = new MultiFormatWriter();
        try {
            BitMatrix codigo = formaescribir.encode(a, BarcodeFormat.QR_CODE, 164, 196);
            BarcodeEncoder codigoqr = new BarcodeEncoder();
            bitmap = codigoqr.createBitmap(codigo);
            // qr.setImageBitmap(bit);
        } catch (WriterException e) {
            e.printStackTrace();
        }
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

}
