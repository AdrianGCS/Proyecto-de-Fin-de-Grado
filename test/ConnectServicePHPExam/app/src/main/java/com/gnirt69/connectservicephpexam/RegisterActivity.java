package com.gnirt69.connectservicephpexam;

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

/**
 * Created by NgocTri on 4/11/2016.
 */
public class RegisterActivity extends Activity{
    private EditText txtUsername;
    private EditText txtPassword1;
    private EditText txtPassword2;
    private ProgressDialog m_ProgresDialog;
    private AccessServiceAPI m_AccessServiceAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtUsername = (EditText)findViewById(R.id.txt_username);
        txtPassword1 = (EditText)findViewById(R.id.txt_pwd1);
        txtPassword2 = (EditText) findViewById(R.id.txt_pwd2);
        m_AccessServiceAPI = new AccessServiceAPI();
    }
    public void btnRegister_Click(View v) {
        //Validate input
        if("".equals(txtUsername.getText().toString())) {
            txtUsername.setError("Username is required!");
            return;
        }
        if("".equals(txtPassword1.getText().toString())) {
            txtPassword1.setError("Password is required!");
            return;
        }
        if("".equals(txtPassword2.getText().toString())) {
            txtPassword2.setError("Confirm password is required!");
            return;
        }
        if(txtPassword1.getText().toString().equals(txtPassword2.getText().toString())) {
            //exec task register
            new TaskRegister().execute(txtUsername.getText().toString(), txtPassword1.getText().toString());
        } else {
            txtPassword2.setError("Confirm password not match!");
        }
    }

    public class TaskRegister extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_ProgresDialog = ProgressDialog.show(RegisterActivity.this, "Please wait", "Registration processing...", true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "add");
            postParam.put("username", params[0]);
            postParam.put("password", params[1]);
            try{
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(Common.SERVICE_API_URL, postParam);
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
            m_ProgresDialog.dismiss();
            if(integer == Common.RESULT_SUCCESS) {
                Toast.makeText(RegisterActivity.this, "Registration success", Toast.LENGTH_LONG).show();
                Intent i = new Intent();
                i.putExtra("username", txtUsername.getText().toString());
                i.putExtra("password", txtPassword1.getText().toString());
                setResult(1, i);
                finish();
            } else if(integer == Common.RESULT_USER_EXISTS) {
                Toast.makeText(RegisterActivity.this, "Username is exists!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(RegisterActivity.this, "Registration fail!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
