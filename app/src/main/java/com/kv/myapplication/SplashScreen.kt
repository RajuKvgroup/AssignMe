package com.kv.myapplication

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.coroutineScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val checkSelfPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            //Requests permissions to be granted to this application at runtime
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
            )
            lifecycle.coroutineScope.launchWhenCreated {
                delay(5000)
                startActivity(Intent(this@SplashScreen,MainActivity::class.java))
                finish()
            }
        }else{
            lifecycle.coroutineScope.launchWhenCreated {
                delay(5000)
                startActivity(Intent(this@SplashScreen,MainActivity::class.java))
                finish()
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantedResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantedResults)
        when (requestCode) {
            1 ->
                if (grantedResults.isNotEmpty() && grantedResults.get(0) ==
                    PackageManager.PERMISSION_GRANTED
                ) {

                } else {
                    Toast.makeText(this,"Unfortunately You are Denied Permission to Perform this Operation.",
                        Toast.LENGTH_SHORT).show()
                }
        }
    }
}