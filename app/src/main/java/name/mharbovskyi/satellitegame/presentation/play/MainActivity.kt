package name.mharbovskyi.satellitegame.presentation.play

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import name.mharbovskyi.satellitegame.R
import name.mharbovskyi.satellitegame.domain.*
import name.mharbovskyi.satellitegame.domain.entity.*
import name.mharbovskyi.satellitegame.domain.physics.calculation.primaryOrbitSpeed
import name.mharbovskyi.satellitegame.domain.physics.measurement.FirstMeasurementSystem
import name.mharbovskyi.satellitegame.presentation.observeBy

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

        planet.x = width.toFloat() / 2 + planet.width / 2
        planet.y = height.toFloat() / 2 + planet.height / 2

        val screenSize = ScreenSize(height, width)
        val measurementSystem = FirstMeasurementSystem

        Log.d(tag, "H: $height, W: $width, PH: ${planet.y}, PW: ${planet.x}")

        val planet = Planet(
            "Ventura",
            measurementSystem.planet.weight.heavy,
            measurementSystem.planet.radius.medium,
            Location(0.0, 0.0)
        )

        val initialSpeed = primaryOrbitSpeed(planet, measurementSystem.g)
        val satellite = ObjectState(
            Speed( 0.3 * initialSpeed, 0.0),
            Acceleration(0.0, 0.0),
            Location(0.0, 4 * planet.radius)
        )

        viewModel = PlayViewModel(
            satellite = satellite,
            scaledValues = scaledValuesFor(measurementSystem, screenSize, scaledPeriod = 8.0),
            planet = planet,
            trajectoryBuilder = trajectorySequenceBuilder(planet, measurementSystem.g),
            hasCollision = ::hasCollision,
            locationScalerFactory = { coordinateTransformer(it.coordinateScale, width, height) }
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
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
        Log.d(tag, "Failure ${getString(resId)}")
    }

    private fun updateSatelliteLocation(location: Location) {
        //Log.d(tag, "New location $location")
        doge.x = location.x.toFloat() + doge.width / 2
        doge.y = location.y.toFloat() + doge.height / 2
    }
}
