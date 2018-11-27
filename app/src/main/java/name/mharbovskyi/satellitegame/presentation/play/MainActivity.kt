package name.mharbovskyi.satellitegame.presentation.play

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import name.mharbovskyi.satellitegame.R
import name.mharbovskyi.satellitegame.domain.coordinateTransformer
import name.mharbovskyi.satellitegame.domain.entity.*
import name.mharbovskyi.satellitegame.domain.trajectorySequenceBuilder
import name.mharbovskyi.satellitegame.presentation.observeBy
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private val tag = MainActivity::class.java.simpleName.toString()

    lateinit var viewModel: PlayViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        planet.x = width.toFloat()/2
        planet.y = height.toFloat() / 2

        val planet = Planet(
            "Ventura",
            100.0,
            10.0,
            Location(0.0, 0.0)
        )

        val g = 2 * Math.PI.pow(2) / 5
        val initialSpeed = sqrt( g * planet.mass / planet.radius )
        var satellite = ObjectState(
            Speed(  initialSpeed, 0.0),
            Acceleration(0.0, 0.0),
            Location(0.0, planet.radius)
        )

        viewModel = PlayViewModel(
            satellite,
            planet,
            trajectorySequenceBuilder(planet, g),
            coordinateTransformer(width, height)
        )

        viewModel.satellitePosition.observeBy(this,
            success = ::updateSatelliteLocation,
            failure = ::showFailure,
            loading = ::showLoading
        )
    }

    override fun onStart() {
        super.onStart()
        viewModel.start()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stop()
    }

    private fun showLoading() {
        Log.d(tag, "Start loading")
    }

    private fun showFailure(resId: Int) {
        Log.d(tag, "Failure ${getString(resId)}")
    }

    private fun updateSatelliteLocation(location: Location) {
        Log.d(tag, "New location $location")
        doge.x = location.x.toFloat()
        doge.y = location.y.toFloat()
    }
}
