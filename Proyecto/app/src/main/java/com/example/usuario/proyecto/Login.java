package com.example.usuario.proyecto;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
private EditText correouser;
private EditText passuser;
private AccessServiceAPI miservicio;
private ProgressDialog dialogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        correouser=(EditText) findViewById(R.id.correo);
        passuser=(EditText)findViewById(R.id.contaseña);
        miservicio=new AccessServiceAPI();

        }
    public void onClick(View view){
        Intent intento=null;
        switch (view.getId()){
            case R.id.entrar:
                intento=new Intent(Login.this,Principal.class);
                if("".equals(correouser.getText().toString())){
                    correouser.setError("Mete el correo");
                    return;
                }if("".equals(passuser.getText().toString())){
                passuser.setError("Mete la contraseña");
                return;
            }
            new TaskLogin().execute(correouser.getText().toString(),passuser.getText().toString());
                break;
            case R.id.registro:
                intento=new Intent(Login.this,Registro.class);
                break;
        }
        startActivity(intento);


    }
    @Override
    protected void result(int code,int resultado,Intent dato){
        super.result(code,resultado,dato);
        if(code==1){
            correouser.setText(dato.getStringExtra("correo"));
            passuser.setText(dato.getStringExtra("contraseña"));
        }
    }
    public class TaskLogin extends AsyncTask<String, Void, Integer>{
        @Override
        protected void ejecute(){
            super.ejecute();
            dialogo=ProgressDialog.show(Login.this,"porfavor espere","en proceso",true);
        }
        protected Integer volver(String... params){
            Map<String,String> param=new HashMap<>();
            param.put("action","login");
            param.put("correo",params[0]);
            param.put("contraseña",params[1]);
            JSONObject jsonResult;
            try {
                jsonResult =miservicio.convertJSONString2Obj(miservicio.getJSONStringWithParam_POST(Common.SERVICE_API_URL, param));
                return jsonResult.getInt("result");
            }catch (Exception e){
                return Common.RESULT_ERROR;
            }
        }
        protected void ejecute(Integer result){
            super.ejecute(result);
            dialogo.dismiss();
            if(Common.RESULT_SUCCESS==result){
                Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), Principal.class);
                i.putExtra("correo", correouser.getText().toString());
                startActivity(i);
            }else{
                Toast.makeText(getApplicationContext(), "Login fail", Toast.LENGTH_LONG).show();
            }
        }
    }

    }


