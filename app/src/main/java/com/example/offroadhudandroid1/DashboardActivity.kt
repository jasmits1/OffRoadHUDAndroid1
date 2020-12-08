package com.example.offroadhudandroid1

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.example.offroadhudandroid1.Model.LocationModel
import com.example.offroadhudandroid1.Network.ApiService
import com.example.offroadhudandroid1.Util.GpsUtils
import com.example.offroadhudandroid1.ViewModel.DashboardViewModel
import com.ntt.customgaugeview.library.GaugeView

const val LOCATION_REQUEST = 100
const val GPS_REQUEST = 101

class DashboardActivity : AppCompatActivity() {

    private lateinit var dashboardViewModel: DashboardViewModel

    // TextViews to display output values
    private lateinit var mPitchTextView: TextView
    private lateinit var mRollTextView: TextView

    private lateinit var mGaugeView: GaugeView
    private lateinit var mRollBarRight: ProgressBar
    private lateinit var mRollBarLeft: ProgressBar
    private lateinit var mPitchBarUp: ProgressBar
    private lateinit var mPitchBarDown: ProgressBar

    private var isGPSEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)

        mGaugeView = findViewById(R.id.gauge_view)

        // Initialize TextViews
        mPitchTextView = findViewById(R.id.value_pitch)
        mRollTextView = findViewById(R.id.value_roll)

        // Initialize visual incline indicators
        mRollBarRight = findViewById(R.id.progress_bar_roll_right)
        mRollBarLeft = findViewById(R.id.progress_bar_roll_left)
        mPitchBarUp = findViewById(R.id.progress_bar_pitch_up)
        mPitchBarDown = findViewById(R.id.progress_bar_pitch_down)

        GpsUtils(this).turnGPSOn(object: GpsUtils.OnGpsListener {

            override fun gpsStatus(isGPSEnabled: Boolean) {
                this@DashboardActivity.isGPSEnabled = isGPSEnabled
            }

        })
    }

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
        startInclineUpdate()
    }

    private fun startInclineUpdate() {
        dashboardViewModel.getInclineData().observe(this, {
            mPitchTextView.text = resources.getString(
                R.string.value_format_incline, it.pitchDegrees)
            mRollTextView.text = resources.getString(
                R.string.value_format_incline, it.rollDegrees
            )

            // Update visual roll indicators
            if(it.rollDegrees >= 0) {
                mRollBarLeft.progress = it.rollDegrees
                mRollBarRight.progress = 0
            } else {
                mRollBarRight.progress = -it.rollDegrees
                mRollBarLeft.progress = 0
            }

            // Update visual pitch indicators
            if(it.pitchDegrees >= 0) {
                mPitchBarUp.progress = it.pitchDegrees
                mPitchBarDown.progress = 0
            } else {
                mPitchBarDown.progress = -it.pitchDegrees
                mPitchBarUp.progress = 0
            }
        })
    }

    private fun invokeLocationAction() {
        when {
            //!isGPSEnabled -> mLatLongTextView.text = getString(R.string.enable_gps)

            isLocationPermissionGranted() -> startLocationUpdate()

            //shouldShowRequestLocationPermissionRationale() -> mLatLongTextView.text = getString(R.string.permission_request)

            else -> ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    LOCATION_REQUEST
            )
        }
    }

    private fun startLocationUpdate() {
        dashboardViewModel.getLocationData().observe(this, {
            mGaugeView.setTargetValue(it.speedMS?.times(2.23694f) ?: 0.0f)
            postNewLocaton(it)
        })
    }

    private fun isLocationPermissionGranted() =
            ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestLocationPermissionRationale() =
            ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) && ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            )

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                invokeLocationAction()
            }
        }
    }

    //TODO: This does not belong here! This is for testing network calls before we create repo infastructure
    fun postNewLocaton(location: LocationModel) {
        val apiService = ApiService()
        apiService.postNewLocation(location) {
            if (it?.latitude != null) {
                println("Api Call Success!")
            } else {
                println("Api call error!")
            }
        }
    }

}