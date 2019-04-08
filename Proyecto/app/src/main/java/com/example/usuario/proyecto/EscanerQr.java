package com.example.usuario.proyecto;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
    Button btnAction;
    public static String intentData;
    private AccessServiceAPI miservicio;
    private ProgressDialog dialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escaner_qr);
        initViews();
        miservicio = new AccessServiceAPI();
    }

    private void initViews() {
        texto = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);
        btnAction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(EscanerQr.this, Principal.class));
               /* if (intentData.length() > 0) {

                        startActivity(new Intent(EscanerQr.this,Principal.class));

                   // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intentData)));
                    //esto es lo que lleva a la direccion de del qr
                }*/


            }
        });
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
                    if (ActivityCompat.checkSelfPermission(EscanerQr.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(EscanerQr.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
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

        class TaskLogin extends AsyncTask<String, Void, Integer> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //Open progress dialog during login
                dialogo = ProgressDialog.show(EscanerQr.this, "Porfavor, Espere", "Procesando", true);
            }
            @Override
            protected Integer doInBackground(String... params) {
                //Create data to pass in param
                Map<String, String> param = new HashMap<>();
                param.put("action", "login");
                param.put("datos", params[1]);


                JSONObject jObjResult;
                try {

                    jObjResult = miservicio.convertJSONString2Obj(miservicio.getJSONStringWithParam_POST(Common.SERVICE_API_URL, param));
                    intentData=jObjResult.getString("datos");
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
                    Toast.makeText(getApplicationContext(), "Login Correcto", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), OpcionUser.class);
                    i.putExtra("datos",texto.getText().toString());
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Login Mal", Toast.LENGTH_LONG).show();
                }
            }
        }
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
                                btnAction.setText("Coge la URL");
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


}
