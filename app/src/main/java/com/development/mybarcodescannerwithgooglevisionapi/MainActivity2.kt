package com.development.mybarcodescannerwithgooglevisionapi

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment

class MainActivity2 : AppCompatActivity() {

    private val requestCodeCameraPermission = 1001
    private lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        initNavController()
        askForCameraPermission()
    }

    private fun initNavController() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainNavigationFragment) as NavHostFragment
        navController = findNavController(R.id.mainNavigationFragment)

        // set navigation graph
        val navGraph = navController.navInflater.inflate(R.navigation.navigation)

        navGraph.startDestination = R.id.demoFragment

        navController.graph = navGraph
    }

    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(this@MainActivity2,
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == requestCodeCameraPermission && grantResults.isEmpty()){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                navController.navigate(R.id.barcodeFragment)
            }
            else {
                Toast.makeText(this@MainActivity2,"Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}