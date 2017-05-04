package edu.ucuccs.ucumobilegrade;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * Custom Scannner Activity extending from Activity to display a custom layout form scanner view.
 */
public class CustomScannerActivity extends Activity implements
        DecoratedBarcodeView.TorchListener {
    boolean flag=false;
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private ImageView imgFlash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scanner);

        barcodeScannerView = (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
        barcodeScannerView.setTorchListener(this);

        imgFlash = (ImageView)findViewById(R.id.ivFlash);
        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
            imgFlash.setVisibility(View.GONE);
        }
        else{
            imgFlash.setVisibility(View.VISIBLE);
        }


        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight(View view) {

        if (!flag) {
            barcodeScannerView.setTorchOn();
            flag=true;
        } else {
            barcodeScannerView.setTorchOff();
            flag=false;
        }
    }

    @Override
    public void onTorchOn() {imgFlash.setBackgroundResource(R.drawable.flash_on);
    }

    @Override
    public void onTorchOff() {
        imgFlash.setBackgroundResource(R.drawable.flash_off);
    }
}
