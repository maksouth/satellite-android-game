package name.mharbovskyi.satellitegame.presentation.play

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import name.mharbovskyi.satellitegame.R
import name.mharbovskyi.satellitegame.domain.coordinateTransformer
import name.mharbovskyi.satellitegame.domain.entity.*
import name.mharbovskyi.satellitegame.domain.objectStateTransformer
import name.mharbovskyi.satellitegame.domain.physics.primaryOrbitSpeed
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

        val planet = Planet(
            "Earth",
            mass = 5.97e24,
            radius = 6371000.0,
            location = Location(0.0, 0.0)
        )

        val satellite = ObjectState(
            Speed(primaryOrbitSpeed(planet)/2, 0.0),
            Acceleration(0.0, 0.0),
            Location(0.0, planet.radius)
        )

        val factory = PlayViewModelFactory(
            satellite,
            planet,
            objectStateTransformer(planet),
            coordinateTransformer(width, height)
        )
        viewModel = ViewModelProviders.of(this, factory)[PlayViewModel::class.java]

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
