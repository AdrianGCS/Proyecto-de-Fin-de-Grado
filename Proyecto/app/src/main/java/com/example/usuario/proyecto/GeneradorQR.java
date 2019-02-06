package com.example.usuario.proyecto;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GeneradorQR extends AppCompatActivity {
EditText texto;
Button convertir;
ImageView qr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generador_qr);
        inicializa();
        tomar();
    }

    private void tomar() {
        convertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
darle();
            }

            private void darle() {
                String a=texto.getText().toString();
                MultiFormatWriter formaescribir=new MultiFormatWriter();
                try {
                    BitMatrix codigo=formaescribir.encode(a, BarcodeFormat.QR_CODE,164,196);
                    BarcodeEncoder codigoqr=new BarcodeEncoder();
                    Bitmap bit=codigoqr.createBitmap(codigo);
                    qr.setImageBitmap(bit);

                }catch (WriterException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void inicializa() {
        texto=(EditText)findViewById(R.id.texto);
        convertir=(Button) findViewById(R.id.convertir);
        qr=(ImageView)findViewById(R.id.qr);
    }

    
}
