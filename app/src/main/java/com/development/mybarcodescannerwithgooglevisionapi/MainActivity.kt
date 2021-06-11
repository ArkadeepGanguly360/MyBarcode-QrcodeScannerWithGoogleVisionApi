package com.development.mybarcodescannerwithgooglevisionapi

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseArray
import android.view.SurfaceHolder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var detector: BarcodeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askForCameraPermission()
        } else {
            setupControls()
        }
    }

    private fun setupControls() {
        detector = BarcodeDetector.Builder(this@MainActivity).build()
        cameraSource = CameraSource.Builder(this@MainActivity, detector)
            .setAutoFocusEnabled(true)
            .build()
        cameraSurfaceView.holder.addCallback(surfaceCallback)
        detector.setProcessor(processor)
    }

    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(this@MainActivity,
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission)
    }


    private val surfaceCallback = object : SurfaceHolder.Callback{
        override fun surfaceCreated(holder: SurfaceHolder) {
            try {
                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        android.Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                cameraSource.start(holder)
            } catch (exception : Exception){

            }
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            cameraSource.stop()
        }
    }

    private val processor = object : Detector.Processor<Barcode>{
        override fun release() {
        }

        override fun receiveDetections(p0: Detector.Detections<Barcode>?) {
            if(p0 != null && p0.detectedItems.isNotEmpty()){
                val qrCodes: SparseArray<Barcode> = p0.detectedItems
                val code = qrCodes.valueAt(0)
                textScanResult.text = code.displayValue
            }
            else {
                textScanResult.text = ""
            }
        }
    }
}