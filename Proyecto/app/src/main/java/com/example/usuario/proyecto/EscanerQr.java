package com.example.usuario.proyecto;

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

import java.io.IOException;

public class EscanerQr extends AppCompatActivity {
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String intentData = "";
    boolean isEmail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escaner_qr);
        initViews();
    }
    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);
        btnAction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (intentData.length() > 0) {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intentData)));

                }


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


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
@Override
public void release() {
        Toast.makeText(getApplicationContext(), "DETENIDO EL USO DE MEMORIA DEL TELEFONO", Toast.LENGTH_SHORT).show();
        }

@Override
public void receiveDetections(Detector.Detections<Barcode> detections) {
final SparseArray<Barcode> barcodes = detections.getDetectedItems();
        if (barcodes.size() != 0) {


        txtBarcodeValue.post(new Runnable() {

@Override
public void run() {

        if (barcodes.valueAt(0).email != null) {
        txtBarcodeValue.removeCallbacks(null);
        intentData = barcodes.valueAt(0).email.address;
        txtBarcodeValue.setText(intentData);
        isEmail = true;
        btnAction.setText("AÑADE");
        } else {
        isEmail = false;
        btnAction.setText("Coge la URL");
        intentData = barcodes.valueAt(0).displayValue;
        txtBarcodeValue.setText(intentData);

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
