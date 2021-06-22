package com.example.customviews

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Surface
import android.widget.TextView
import com.example.customviews.canvas.CupView

class MainActivity : AppCompatActivity(), SensorEventListener {

    private val cupView by lazy {
        findViewById<CupView>(R.id.cup)
    }
    private val tvAngle by lazy {
        findViewById<TextView>(R.id.tvAngle)
    }

    private lateinit var sensorManager: SensorManager

    private val mwindowManager by lazy {
        window.windowManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR).apply {
            sensorManager.registerListener(
                this@MainActivity,
                this,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
            updateAngle(event.values)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//        Log.d("SensorListener", "onAccuracy changed for ${sensor?.name} to $accuracy")
    }

    private fun updateAngle(rotationArr: FloatArray) {
        val mat = FloatArray(9)
        SensorManager.getRotationMatrixFromVector(mat, rotationArr)

        val (worldAxisForDeviceAxisX, worldAxisForDeviceAxisY) = when(mwindowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> Pair(SensorManager.AXIS_X, SensorManager.AXIS_Z)
            Surface.ROTATION_90 -> Pair(SensorManager.AXIS_Z, SensorManager.AXIS_MINUS_X)
            Surface.ROTATION_180 -> Pair(SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Z)
            Surface.ROTATION_270 -> Pair(SensorManager.AXIS_MINUS_Z, SensorManager.AXIS_X)
            else -> Pair(SensorManager.AXIS_X, SensorManager.AXIS_Z)
        }
        val adjustedRotationMatrix = FloatArray(9)
        SensorManager.remapCoordinateSystem(mat, worldAxisForDeviceAxisX, worldAxisForDeviceAxisY, adjustedRotationMatrix)

        val orientation = FloatArray(3)
        SensorManager.getOrientation(adjustedRotationMatrix, orientation)

        val roll = orientation[2] * -57

        tvAngle.text = "Roll: $roll"
        cupView.tiltCoke(roll)
    }
}