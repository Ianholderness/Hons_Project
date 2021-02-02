package com.example.foodallergyapp.App.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodallergyapp.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
/**
**************************************
Created :
@author : Ian Holderness 14023756
@version  : 1.0.0
Last Update:

Version Updates:

Refrences:

**************************************
 */

public class BarcodeScanner extends AppCompatActivity {

    SurfaceView surfaceView;
    CameraSource cameraSource;
    TextView textView;
    BarcodeDetector barcodeDetector;
    Button ManualBcode;
    ImageButton ExitManual;
    FrameLayout manual;
    EditText txtManual;

    final int RequestCameraPermissions = 1;
    public boolean scanned = false;
    public boolean DiscScanner = false;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        surfaceView= findViewById(R.id.cameraPreview);
        textView = findViewById(R.id.txtBarcode);
        ManualBcode = findViewById(R.id.btnManualBarcode);
        ExitManual = findViewById(R.id.btnExcape);
        manual = findViewById(R.id.manualLayout);
        txtManual = findViewById(R.id.txtManualBarcode);



        //Sets the barcode format to read 8 & 13
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.EAN_8 | Barcode.EAN_13).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector).setAutoFocusEnabled(true)
            .setRequestedPreviewSize(640,480).build();

        //Key listener if user presses ENTER when using manual Entry
        txtManual.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode==KeyEvent.KEYCODE_ENTER ) {
                    Intent intent = new Intent(BarcodeScanner.this, ScannedData.class);
                    String Bcode = txtManual.getText().toString();
                    intent.putExtra("Barcode", Bcode);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

        ManualBcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscScanner = true;
                manual.setVisibility(View.VISIBLE);
            }
        });

        ExitManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscScanner = false;
                manual.setVisibility(View.GONE);
            }
        });


        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {

            /**
             *
             * @param holder
             */
            @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    // checks permissions to use camera

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) !=
                            PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(BarcodeScanner.this, new String[]{Manifest.permission.CAMERA},RequestCameraPermissions);
                        return;
                    }
                    try{
                        cameraSource.start(surfaceView.getHolder());
                    }catch (IOException e){
                        Toast.makeText(BarcodeScanner.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

            /**
             *
             * @param holder
             * @param i
             * @param i1
             * @param i2
             */
                @Override
                public void surfaceChanged(SurfaceHolder holder, int i, int i1, int i2) {

                }

            /**
             *
             * @param holder
             */
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });

            barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                /**
                 *
                 */
                @Override
                public void release() {

                }

                /**
                 *
                 * @param detections
                 */
                // IF the barcode is detected
                @Override
                public void receiveDetections(Detector.Detections<Barcode> detections) {
                    final SparseArray<Barcode> sparseArray = detections.getDetectedItems();
                    //Check if the parseArray is not 0
                    if (sparseArray.size() != 0 & !scanned & !DiscScanner){
                        //
                        textView.post(new Runnable() {
                            /**
                             *
                             */
                            @Override
                            public void run() {
                                //Vibrate the device to let the user know that product has scanned
                                Vibrator vibrate = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                vibrate.vibrate(1000);
                                // set the intent to clode the scanner and move to the Scanned data
                                Intent intent = new Intent(BarcodeScanner.this, ScannedData.class);
                                String Bcode = sparseArray.valueAt(0).displayValue;
                                // Set the bcode pramete to be passed to Scanned data
                                intent.putExtra("Barcode", Bcode );
                                // Starts the inteted activity
                                startActivity(intent);
                                textView.setText(sparseArray.valueAt(0).displayValue);
                                finish();
                                scanned = true;
                            }
                        });
                    }
                }
            });
            // Creates the naviagtion bar and cerates the evenelistener for each of the actions
            BottomNavigationView navigation = findViewById(R.id.navigation);
            navigation.setSelectedItemId(R.id.scan);
            navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                /**
                 *
                 * @param item
                 * @return
                 */
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            startActivity(new Intent(BarcodeScanner.this, MainActivity.class));
                            finish();
                            break;
                        case R.id.allergens:
                             Intent b = new Intent(BarcodeScanner.this,Allergens.class);
                             startActivity(b);
                            break;
                    }
                    return false;
                }
            });
        }

    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            switch (requestCode){

                case RequestCameraPermissions:
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        if(ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        }else{
                            try {
                                // if permissions are granted
                                cameraSource.start(surfaceView.getHolder());
                            }catch (IOException e){
                                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            }
        }

    /**
     *
     */
    @Override
    public void onBackPressed() {
        this.finish();
    }


}
