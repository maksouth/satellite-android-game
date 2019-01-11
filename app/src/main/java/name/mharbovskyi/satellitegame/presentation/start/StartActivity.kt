package name.mharbovskyi.satellitegame.presentation.start

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import kotlinx.android.synthetic.main.activity_start.*
import name.mharbovskyi.satellitegame.R
import name.mharbovskyi.satellitegame.domain.entity.*
import name.mharbovskyi.satellitegame.presentation.play.PlayActivity

class StartActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        start_button.setOnClickListener {
            startActivity(Intent(this, PlayActivity::class.java))
        }

        val planet = Planet(
            "Ventura",
            100.0,
            80.0,
            Location(480.0, 794.0)
        )

        val satellite = ObjectState(
            Speed(0.0, 0.0),
            Acceleration(0.0, 0.0),
            Location(320.0, 794.0)
        )

        val satellite_next = satellite.copy(
            location = satellite.location.copy(x = 640.0)
        )

        play_surface.drawPlanet(planet)
//        play_surface.drawSatellite(satellite)

        var handler = Handler(Looper.getMainLooper())
//        handler.postDelayed({ play_surface.drawSatellite(satellite_next) }, 2000)
    }
}
