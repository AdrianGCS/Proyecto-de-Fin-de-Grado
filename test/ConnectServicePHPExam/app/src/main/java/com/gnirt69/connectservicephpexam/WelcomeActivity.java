package com.gnirt69.connectservicephpexam;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by NgocTri on 4/11/2016.
 */
public class WelcomeActivity extends Activity {
    TextView tvWelcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        tvWelcome = (TextView)findViewById(R.id.tv_welcome);
        tvWelcome.setText("Welcome: " + getIntent().getStringExtra("username"));

    }
    public void btnBack_Click(View v) {
        finish();
    }
}
