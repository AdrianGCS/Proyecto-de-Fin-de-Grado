package com.example.usuario.proyecto;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EscanerQr extends AppCompatActivity {
    SurfaceView surfaceView;
    TextView texto;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    TelephonyManager telephonyManager;
    private static final int REQUEST_READ_PHONE_STATE = 101;
    Button btnAction;
    public static String intentData = "";
    public static String telefono = "";
    boolean isEmail = false;
    private Dialog midialogo;
    private AccessServiceAPI miser;
    public static String id_enfermo;
    public static String imei;
    public static String direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escaner_qr);
        miser = new AccessServiceAPI();
        initViews();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    }

    private void initViews() {
        texto = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);
        btnAction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //startActivity(new Intent(EscanerQr.this, Principal.class));
                if ("".equals(texto.toString())) {
                    texto.setError("no es un qr");
                    return;
                    // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intentData)));
                    //esto es lo que lleva a la direccion de del qr
                }
                new TaskRegister().execute(texto.getText().toString());

            }
        });
        //startActivity(new Intent(EscanerQr.this,Principal.class));
    }

    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "LECTOR EMPEZADO", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)

                .build();


        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {

                    if (ActivityCompat.checkSelfPermission(EscanerQr.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(EscanerQr.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EscanerQr.this, new
                                String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA}, 1);
                    } else {
                        if (ActivityCompat.checkSelfPermission(EscanerQr.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(EscanerQr.this, new
                                    String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);

                        } else {
                            if (ActivityCompat.checkSelfPermission(EscanerQr.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(EscanerQr.this, new
                                        String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                            } else {

                                cameraSource.start(surfaceView.getHolder());
                            }

                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "DETENIDO EL USO DE MEMORIA DEL TELEFONO", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    texto.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                texto.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;
                                texto.setText(intentData);
                                isEmail = true;
                                btnAction.setText("AÑADE");
                            } else {
                                isEmail = false;
                                btnAction.setText("LEER QR");
                                intentData = barcodes.valueAt(0).displayValue;
                                texto.setText(intentData);

                            }
                        }
                    });

                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                try {
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public class TaskRegister extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            midialogo = ProgressDialog.show(EscanerQr.this, "Espere un momento", "Procesando datos...", true);

        }

        @Override
        protected Integer doInBackground(String... params) {
            String Imei = Imei();
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "qr");
            postParam.put("qr", params[0]);
            postParam.put("imei", Imei);
            //postParam.put("qr", params[4]);
            //llama al PHP y envia los datos

            try {
                String jsonString = miser.getJSONStringWithParam_POST(Common.SERVICE_API_URL, postParam);
                JSONObject jsonObject = new JSONObject(jsonString);
                //coge los datos que le pasa php
                 telefono = jsonObject.getString("Telefono");
                 id_enfermo = jsonObject.getString("id");
                 direccion=jsonObject.getString("direccion");
                return jsonObject.getInt("result");

            } catch (Exception e) {
                e.printStackTrace();
                return Common.RESULT_ERROR;
            }


        }

        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            midialogo.dismiss();
            if (integer == Common.RESULT_SUCCESS) {
                Toast.makeText(EscanerQr.this, "Leido  con exito", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), Principal.class);
                i.putExtra("qr", texto.getText() + "");
                i.putExtra("telefono", telefono);
                i.putExtra("id_enfermo", id_enfermo);
                i.putExtra("imei", imei);
                i.putExtra("direccion", direccion);
                setResult(1, i);
                startActivity(i);
                finish();

            } else {
                Toast.makeText(EscanerQr.this, "Leido fallido", Toast.LENGTH_LONG).show();
            }
        }

        private String Imei() {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
             imei = "";


            if (android.os.Build.VERSION.SDK_INT >= 26) {
                if (ActivityCompat.checkSelfPermission(EscanerQr.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return "Error";
                }
                imei = telephonyManager.getImei();
            }
            else
            {
                imei=telephonyManager.getDeviceId();
            }
            return imei;
        }

    }

}