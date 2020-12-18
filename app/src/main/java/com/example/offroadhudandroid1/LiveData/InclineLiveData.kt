package com.example.offroadhudandroid1.LiveData

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.core.math.MathUtils
import androidx.lifecycle.LiveData
import com.example.offroadhudandroid1.Model.InclineModel
import com.example.offroadhudandroid1.Util.DateUtil
import com.example.offroadhudandroid1.Util.MathUtil
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.*

class InclineLiveData(private val context: Context) : LiveData<InclineModel>(), SensorEventListener {
    private var mSensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    override fun onActive() {
        super.onActive()
        mSensorManager.registerListener(
            this, mSensorAccelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        when(sensorEvent.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> handleNewAccelerometerData(sensorEvent.values.clone()[0],
                                                                    sensorEvent.values[1],
                                                                    sensorEvent.values[2])
            else -> return
        }
    }

    private fun handleNewAccelerometerData(x: Float, y: Float, z: Float ) {
        val dateString = DateUtil.formatDate(Calendar.getInstance().timeInMillis)
        val normOfAccel = sqrt(x*x + y*y + z*z)

        var xN = x / normOfAccel
        var yN = y / normOfAccel
        var zN = z / normOfAccel

        val pitchDegrees = MathUtil.convertRadToDegRounded((acos(zN) - 1.5708f), 5)
        val rollDegrees = MathUtil.convertRadToDegRounded((atan2(xN, yN)), 5)

        //TODO: Round the pitch and roll and convert back to ints
        value = InclineModel(x, y, z, pitchDegrees, rollDegrees, dateString)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // Unused
    }

}