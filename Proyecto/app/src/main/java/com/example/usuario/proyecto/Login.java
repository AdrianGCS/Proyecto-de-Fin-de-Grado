package com.example.usuario.proyecto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private EditText correouser;
    private EditText passuser;
    private AccessServiceAPI miservicio;
    private ProgressDialog dialogo;
    public static int x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        correouser = (EditText) findViewById(R.id.correo);
        passuser = (EditText) findViewById(R.id.password);
        miservicio = new AccessServiceAPI();

        SharedPreferences prefs =
                getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        String validacion = prefs.getString("Correo","Inserte el correo");
        correouser.setText(validacion);

    }

    public void onClick(View view) {
        if ("".equals(correouser.getText().toString())) {
            correouser.setError("Mete el correo");
            return;
        }

        if ("".equals(passuser.getText().toString())) {
            passuser.setError("Mete la contraseña");
            return;

        }
        new TaskLogin().execute(correouser.getText().toString(), passuser.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            correouser.setText(data.getStringExtra("Correo"));
            passuser.setText(data.getStringExtra("Contrasena"));
        }
    }

    public class TaskLogin extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Open progress dialog during login
            dialogo = ProgressDialog.show(Login.this, "Porfavor, Espere", "Procesando", true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            //Create data to pass in param
            Map<String, String> param = new HashMap<>();
            param.put("action", "login");
            param.put("password", params[1]);
            param.put("mail", params[0]);

            JSONObject jObjResult;
            try {

                jObjResult = miservicio.convertJSONString2Obj(miservicio.getJSONStringWithParam_POST(Common.SERVICE_API_URL, param));
                 x=jObjResult.getInt("id");
                return jObjResult.getInt("result");
            } catch (Exception e) {
                return Common.RESULT_ERROR;
            }
        }


        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            dialogo.dismiss();
            if (Common.RESULT_SUCCESS == result) {

                SharedPreferences prefs =
                        getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=prefs.edit();
                editor.putString("Correo",correouser.getText().toString());

                editor.commit();

                Toast.makeText(getApplicationContext(), "Login Correcto", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), MenuUser.class);
                i.putExtra("correo", correouser.getText().toString());
         startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "Login Mal", Toast.LENGTH_LONG).show();
            }
        }
    }

}


