package com.gnirt69.connectservicephpexam;

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

/**
 * Created by NgocTri on 4/11/2016.
 */
public class LoginActivity extends Activity {
    private EditText txtUsername;
    private EditText txtPassword;
    private AccessServiceAPI m_ServiceAccess;
    private ProgressDialog m_ProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUsername = (EditText) findViewById(R.id.txt_username_login);
        txtPassword = (EditText) findViewById(R.id.txt_pwd_login);
        m_ServiceAccess = new AccessServiceAPI();
    }

    public void btnLogin_Click(View v) {
        //Validate input
        if ("".equals(txtUsername.getText().toString())) {
            txtUsername.setError("Username is required!");
            return;
        }
        if ("".equals(txtPassword.getText().toString())) {
            txtPassword.setError("Password is required!");
            return;
        }
        //Call async task to login
        new TaskLogin().execute(txtUsername.getText().toString(), txtPassword.getText().toString());
    }

    public void btnGoRegister_Click(View v) {
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1) {
            txtUsername.setText(data.getStringExtra("username"));
            txtPassword.setText(data.getStringExtra("password"));
        }
    }

    public class TaskLogin extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Open progress dialog during login
            m_ProgressDialog = ProgressDialog.show(LoginActivity.this, "Please wait...", "Processing...", true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            //Create data to pass in param
            Map<String, String> param = new HashMap<>();
            param.put("action", "login");
            param.put("username", params[0]);
            param.put("password", params[1]);

            JSONObject jObjResult;
            try {

                jObjResult = m_ServiceAccess.convertJSONString2Obj(m_ServiceAccess.getJSONStringWithParam_POST(Common.SERVICE_API_URL, param));
                return jObjResult.getInt("result");
            } catch (Exception e) {
                return Common.RESULT_ERROR;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            m_ProgressDialog.dismiss();
            if(Common.RESULT_SUCCESS == result) {
                Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
                i.putExtra("username", txtUsername.getText().toString());
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "Login fail", Toast.LENGTH_LONG).show();
            }
        }
    }
}
