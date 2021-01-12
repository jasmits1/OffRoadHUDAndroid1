package com.example.offroadhudandroid1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.offroadhudandroid1.UI.DashboardFragment
import dagger.hilt.android.AndroidEntryPoint

const val LOCATION_REQUEST = 100
const val GPS_REQUEST = 101

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, DashboardFragment.newInstance())
                    .commitNow()
        }
    }
}

/*
@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private val dashboardViewModel: DashboardViewModel by viewModels()
    //private lateinit var dashboardViewModel: DashboardViewModel


    // TextViews to display output values
    private lateinit var mPitchTextView: TextView
    private lateinit var mRollTextView: TextView

    private lateinit var mGaugeView: GaugeView
    private lateinit var mRollBarRight: ProgressBar
    private lateinit var mRollBarLeft: ProgressBar
    private lateinit var mPitchBarUp: ProgressBar
    private lateinit var mPitchBarDown: ProgressBar

    private lateinit var mTrackingToggle: ToggleButton

    private var isGPSEnabled = false
    private var isTracking = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        //dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)

        mGaugeView = findViewById(R.id.gauge_view)

        // Initialize TextViews
        mPitchTextView = findViewById(R.id.value_pitch)
        mRollTextView = findViewById(R.id.value_roll)

        // Initialize visual incline indicators
        mRollBarRight = findViewById(R.id.progress_bar_roll_right)
        mRollBarLeft = findViewById(R.id.progress_bar_roll_left)
        mPitchBarUp = findViewById(R.id.progress_bar_pitch_up)
        mPitchBarDown = findViewById(R.id.progress_bar_pitch_down)

        mTrackingToggle = findViewById(R.id.tracking_toggle)
       mTrackingToggle.setOnCheckedChangeListener { _, isChecked ->
           trackingToggleChangedListener(isChecked)
        }


        Utils(this).turnGPSOn(object: Utils.OnGpsListener {

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
            val speedMPH = it.speedMS?.times(2.23694f) ?: 0.0f
            println("Speed MPH: $speedMPH")
            if(speedMPH < 77)
                mGaugeView.setTargetValue(speedMPH)
            if(isTracking)
                postNewLocaton(it)
        })
    }

    private fun postNewLocaton(locationModel: LocationModel) {
        dashboardViewModel.saveLocationData(locationModel)
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

    private fun trackingToggleChangedListener(isChecked: Boolean) {
        if(isChecked) {
            val beginRouteDialogLayout: View = layoutInflater.inflate(R.layout.new_route_dialog, null)
            val beginRouteDialogBuilder = MaterialAlertDialogBuilder(this)
                    .setTitle("Enter Route Name")
                    .setView(beginRouteDialogLayout)
                    .setPositiveButton("Begin Route", null)
               //     .setPositiveButton("Begin Route") { dialog, which ->
                 //       isTracking = true
                   // }
                    .setNegativeButton("Cancel") {dialog, which ->
                        mTrackingToggle.isChecked = false
                    }
            val beginRouteDialog = beginRouteDialogBuilder.create()


            beginRouteDialog.setOnShowListener {
                val dialogPositiveButton = beginRouteDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                dialogPositiveButton.setOnClickListener(View.OnClickListener() {
                    it.setOnClickListener {
                        val text: String= beginRouteDialog.findViewById<EditText>(R.id.route_name_text)!!.text.toString()
                        if(!text.isNullOrEmpty()) {
                            isTracking = true
                            dashboardViewModel.startNewRoute(text)
                            beginRouteDialog.dismiss()
                        }
                    }
                })
            }

            beginRouteDialog.show()



        } else {
            isTracking = false
            dashboardViewModel.endCurrentRoute()
        }
    }


}
*/