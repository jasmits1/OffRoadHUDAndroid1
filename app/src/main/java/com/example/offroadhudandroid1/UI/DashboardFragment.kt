package com.example.offroadhudandroid1.UI

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.offroadhudandroid1.LOCATION_REQUEST
import com.example.offroadhudandroid1.Model.LocationModel
import com.example.offroadhudandroid1.R
import com.example.offroadhudandroid1.Util.Utils
import com.example.offroadhudandroid1.ViewModel.DashboardViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ntt.customgaugeview.library.GaugeView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    //region Class Properties

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var mContext: Context

    companion object {
        fun newInstance() = DashboardFragment()
    }

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

    //endregion

    //region Fragment Lifecycle Methods

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //val view = inflater.inflate(R.layout.activity_dashboard, container, false)
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        initView(view)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        Utils(context).turnGPSOn(object : Utils.OnGpsListener {

            override fun gpsStatus(isGPSEnabled: Boolean) {
                this@DashboardFragment.isGPSEnabled = isGPSEnabled
            }

        })
    }

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
        startInclineUpdate()
    }

    private fun initView(view: View) {
        mGaugeView = view.findViewById(R.id.gauge_view)

        // Initialize TextViews
        mPitchTextView = view.findViewById(R.id.value_pitch)
        mRollTextView = view.findViewById(R.id.value_roll)

        // Initialize visual incline indicators
        mRollBarRight = view.findViewById(R.id.progress_bar_roll_right)
        mRollBarLeft = view.findViewById(R.id.progress_bar_roll_left)
        mPitchBarUp = view.findViewById(R.id.progress_bar_pitch_up)
        mPitchBarDown = view.findViewById(R.id.progress_bar_pitch_down)

        mTrackingToggle = view.findViewById(R.id.tracking_toggle)
        mTrackingToggle.setOnCheckedChangeListener { _, isChecked ->
            trackingToggleChangedListener(isChecked)
        }

    }

    //endregion

    //region Sensor Listeners and Callbacks

    private fun startInclineUpdate() {
        dashboardViewModel.getInclineData().observe(this, {
            mPitchTextView.text = resources.getString(
                    R.string.value_format_incline, it.pitchDegrees)
            mRollTextView.text = resources.getString(
                    R.string.value_format_incline, it.rollDegrees
            )

            // Update visual roll indicators
            if (it.rollDegrees >= 0) {
                mRollBarLeft.progress = it.rollDegrees
                mRollBarRight.progress = 0
            } else {
                mRollBarRight.progress = -it.rollDegrees
                mRollBarLeft.progress = 0
            }

            // Update visual pitch indicators
            if (it.pitchDegrees >= 0) {
                mPitchBarUp.progress = it.pitchDegrees
                mPitchBarDown.progress = 0
            } else {
                mPitchBarDown.progress = -it.pitchDegrees
                mPitchBarUp.progress = 0
            }
        })
    }

    /**
     * This method is the first step to starting location updates.
     *
     * It checks if we have location permissions. If so, it calls another method
     * to initiate location updates. If not, it initiates the process to get location permissions.
     */
    private fun invokeLocationAction() {
        when {
            isLocationPermissionGranted() -> startLocationUpdate()

            else -> requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    LOCATION_REQUEST
            )
        }
    }

    /**
     * Starts location updates from the dashboardViewModel.
     */
    private fun startLocationUpdate() {
        dashboardViewModel.getLocationData().observe(this, {
            val speedMPH = it.speedMS?.times(2.23694f) ?: 0.0f
            println("Speed MPH: $speedMPH")
            if (speedMPH < 77)
                mGaugeView.setTargetValue(speedMPH)
            if (isTracking)
                postNewLocaton(it)
        })
    }

    private fun postNewLocaton(locationModel: LocationModel) {
        dashboardViewModel.saveLocationData(locationModel)
    }

    //endregion

    //region UI Element Change Listeners

    private fun trackingToggleChangedListener(isChecked: Boolean) {
        if (isChecked) {
            val beginRouteDialogLayout: View = layoutInflater.inflate(R.layout.new_route_dialog, null)
            val beginRouteDialogBuilder = MaterialAlertDialogBuilder(mContext)
                    .setTitle("Enter Route Name")
                    .setView(beginRouteDialogLayout)
                    .setPositiveButton("Begin Route", null)
                    //     .setPositiveButton("Begin Route") { dialog, which ->
                    //       isTracking = true
                    // }
                    .setNegativeButton("Cancel") { dialog, which ->
                        mTrackingToggle.isChecked = false
                    }
            val beginRouteDialog = beginRouteDialogBuilder.create()


            beginRouteDialog.setOnShowListener {
                val dialogPositiveButton = beginRouteDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                dialogPositiveButton.setOnClickListener(View.OnClickListener() {
                    it.setOnClickListener {
                        val text: String = beginRouteDialog.findViewById<EditText>(R.id.route_name_text)!!.text.toString()
                        if (!text.isNullOrEmpty()) {
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

    //endregion

    //region Location Permissions Methods

    private fun isLocationPermissionGranted() =
            ActivityCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                invokeLocationAction()
            }
        }
    }

    //endregion
}