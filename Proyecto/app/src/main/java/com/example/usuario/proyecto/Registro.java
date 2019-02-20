package com.example.usuario.proyecto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
public class Registro extends AppCompatActivity {
private EditText nombre;
private EditText apellidos;
    private EditText correo;
    private EditText contraseña;
    private EditText confirmarcontraseña;
    private AccessServiceAPI miservicio;
    private ProgressDialog dialogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        correo=(EditText)findViewById(R.id.correo);
        nombre=(EditText)findViewById(R.id.nombre);
        apellidos=(EditText)findViewById(R.id.apellidos);
        contraseña=(EditText)findViewById(R.id.contaseña);
        confirmarcontraseña=(EditText)findViewById(R.id.confirmacontraseña);
        miservicio = new AccessServiceAPI();
    }

    public void onClick(View view){


                if("".equals(nombre.getText().toString())) {
                    nombre.setError("Username is required!");
                    return;
                }
                if("".equals(contraseña.getText().toString())) {
                    contraseña.setError("Password is required!");
                    return;
                }
                if("".equals(confirmarcontraseña.getText().toString())) {
                    confirmarcontraseña.setError("Confirm password is required!");
                    return;
                }
                if(contraseña.getText().toString().equals(confirmarcontraseña.getText().toString())) {
                    //exec task register
                    new TaskRegister().execute(nombre.getText().toString(), contraseña.getText().toString(),correo.getText().toString(),apellidos.getText().toString());
                } else {
                    confirmarcontraseña.setError("Confirm password not match!");
                }






    }
    public class TaskRegister extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogo = ProgressDialog.show(Registro.this, "Please wait", "Registration processing...", true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "add");
            postParam.put("username", params[0]);
            postParam.put("password", params[1]);
            postParam.put("mail", params[2]);
            postParam.put("lastname", params[3]);
           // postParam.put("confirmar contraseña", params[4]);
            postParam.put("alpo", "1");//esto lo pasa pero como nulo
            //llama al PHP
            try{
                String jsonString = miservicio.getJSONStringWithParam_POST(Common.SERVICE_API_URL, postParam);
                JSONObject jsonObject = new JSONObject(jsonString);
                return jsonObject.getInt("result");
            }catch (Exception e) {
                e.printStackTrace();
                return Common.RESULT_ERROR;
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dialogo.dismiss();
            if(integer == Common.RESULT_SUCCESS) {
                Toast.makeText(Registro.this, "Registration success", Toast.LENGTH_LONG).show();
                Intent i = new Intent();
                i.putExtra("nombre", nombre.getText().toString());
                i.putExtra("contraseña", contraseña.getText().toString());
                i.putExtra("apellidos", apellidos.getText().toString());
                i.putExtra("correo", correo.getText().toString());
               // i.putExtra("confirmar contraseña", confirmarcontraseña.getText().toString());
                setResult(1, i);
                finish();
            } else if(integer == Common.RESULT_USER_EXISTS) {
                Toast.makeText(Registro.this, "Username is exists!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Registro.this, "Registration fail!", Toast.LENGTH_LONG).show();
            }
        }
    }


}
