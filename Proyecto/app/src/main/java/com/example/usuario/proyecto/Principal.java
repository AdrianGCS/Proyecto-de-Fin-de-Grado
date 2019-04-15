package com.example.usuario.proyecto;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Principal extends AppCompatActivity {
    private AccessServiceAPI miser;
    private Dialog midialogo;
    private String a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        miser = new AccessServiceAPI();
        new TaskRegister().execute(a.toString());

    }

    public void onClick(View view) {
        /*Intent intento= new Intent(Principal.this,Login.class);
        startActivity(intento);
        */
        switch (view.getId()) {

            case R.id.envio:
                startActivity(new Intent(this, Sms.class));

                break;
            case R.id.llamada:
                startActivity(new Intent(this, LLamar.class));
                break;
            case R.id.loca:
                startActivity(new Intent(this, Localizacion.class));
                break;

        }
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
            postParam.put("action", "enfermo");
            postParam.put("phone", params[0]);

            //postParam.put("qr", params[4]);
            //llama al PHP

            try {
                String jsonString = miser.getJSONStringWithParam_POST(Common.SERVICE_API_URL, postParam);
                JSONObject jsonObject = new JSONObject(jsonString);
                a = jsonObject.getString("Encriptado");

                if (!a.equals("8")) {
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
                Toast.makeText(Principal.this, "Registrado con exito", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), Datos_Enfermo.class);
                i.putExtra("telefono", a.toString() + "");
                setResult(1, i);
                startActivity(i);

                finish();

            } else if (integer == Common.RESULT_USER_EXISTS) {
                Toast.makeText(Principal.this, "El usuario ya existe en la base de datos", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Principal.this, "Registro fallido", Toast.LENGTH_LONG).show();
            }
        }
    }
}


