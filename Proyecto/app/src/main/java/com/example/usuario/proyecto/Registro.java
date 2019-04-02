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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        if("".equals(nombre.getText().toString())) {
                    nombre.setError("Se requiere Nombre");
                    return;
                }
                if("".equals(contraseña.getText().toString())) {
                    contraseña.setError("Se requiere contraseña");
                    return;
                }
                if("".equals(correo.getText().toString())){
                    correo.setError("Se requiere correo");
                    return;
                }
                 Matcher mather = pattern.matcher(correo.getText());
                if (mather.find()==false){
                    correo.setError("Correo no valido ");
                    return;
                }
                if("".equals(confirmarcontraseña.getText().toString())) {
                    confirmarcontraseña.setError("Confirme la contraseña");
                    return;
                }
                if(contraseña.getText().toString().equals(confirmarcontraseña.getText().toString())) {
                    //exec task register
                    new TaskRegister().execute(nombre.getText().toString(), contraseña.getText().toString(),correo.getText().toString(),apellidos.getText().toString());
                } else {
                    confirmarcontraseña.setError("La contraseñas no coinciden ");
                }






    }
    public class TaskRegister extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogo = ProgressDialog.show(Registro.this, "Espere un momento", "Procesando datos...", true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "add");
            postParam.put("username", params[0]);
            postParam.put("password", params[1]);
            postParam.put("mail", params[2]);
            postParam.put("lastname", params[3]);
            //llama al PHP
            try{
                String jsonString = miservicio.getJSONStringWithParam_POST(Common.SERVICE_API_URL, postParam);
                JSONObject jsonObject = new JSONObject(jsonString);
                int ID = jsonObject.getInt("id");
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
                Toast.makeText(Registro.this, "Registrado con exito", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), OpcionUser.class);
                i.putExtra("nombre", nombre.getText().toString());
                i.putExtra("contraseña", contraseña.getText().toString());
                i.putExtra("apellidos", apellidos.getText().toString());
                i.putExtra("correo", correo.getText().toString());
                setResult(1, i);
                finish();
            } else if(integer == Common.RESULT_USER_EXISTS) {
                Toast.makeText(Registro.this, "El usuario ya existe en la base de datos", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Registro.this, "Registro fallido", Toast.LENGTH_LONG).show();
            }
        }
    }


}
