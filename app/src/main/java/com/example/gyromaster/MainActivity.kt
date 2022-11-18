package com.example.gyromaster

import android.content.Context
import android.hardware.*
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.gyromaster.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
//        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        var value: FloatArray

        sensorManager.registerListener(object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (!FirstFragment.send) return
                value = event.values
                println("GyroManager: ${value[0]} ${value[1]} ${value[2]}")
                // get first fragment and edit gyroValue text
                val firstFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
                val firstFragmentView = firstFragment?.view
                val gyroValue =
                    firstFragmentView?.findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.gyroValue)
                gyroValue?.text = "Gyro: \n X: ${value[0]}\n Y: ${value[1]}\n Z: ${value[2]}"
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                return
            }
        }, gyro, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}