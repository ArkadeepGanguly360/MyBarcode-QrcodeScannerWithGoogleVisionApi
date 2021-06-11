package com.development.mybarcodescannerwithgooglevisionapi

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.SparseArray
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class BarcodeFragment : Fragment() {

    private lateinit var cameraSource: CameraSource
    private lateinit var detector: BarcodeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_barcode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupControls()
    }

    private fun setupControls() {
        detector = BarcodeDetector.Builder(requireActivity()).build()
        cameraSource = CameraSource.Builder(requireActivity(), detector)
            .setAutoFocusEnabled(true)
            .build()
        cameraSurfaceView.holder.addCallback(surfaceCallback)
        detector.setProcessor(processor)
    }


    private val surfaceCallback = object : SurfaceHolder.Callback{
        override fun surfaceCreated(holder: SurfaceHolder) {
            try {
                if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
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