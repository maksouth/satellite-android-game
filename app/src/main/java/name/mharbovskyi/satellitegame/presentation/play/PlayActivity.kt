package name.mharbovskyi.satellitegame.presentation.play

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import name.mharbovskyi.satellitegame.R
import name.mharbovskyi.satellitegame.domain.entity.Location
import name.mharbovskyi.satellitegame.domain.entity.Planet
import name.mharbovskyi.satellitegame.domain.entity.TargetSpot

class PlayActivity : Activity(), PlayContract.View {
    private val tag = PlayActivity::class.java.simpleName.toString()

    lateinit var presenter: PlayContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        Log.d(tag, "height $height width $width")

        presenter = PlayPresenterFactory.build(this, width, height)
        presenter.load()

        start_button.setOnClickListener {
            presenter.start(0.8, 1.0)
        }

        restart_button.setOnClickListener {
            presenter.restart()
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun showPlanet(planet: Planet) {
        play_surface.drawPlanet(planet)
    }

    override fun showSatellite(satellite: Location) {
        play_surface.drawSatellite(satellite)
    }

    override fun showTarget(target: TargetSpot) {
        play_surface.drawTarget(target)
    }

    override fun showCollision(location: Location) {
        Toast.makeText(this, "Collision", Toast.LENGTH_SHORT).show()
    }

    override fun showFinish(location: Location) {
        Toast.makeText(this, "Finish", Toast.LENGTH_SHORT).show()
    }

    override fun hideLoading() {
        Toast.makeText(this, "Loading hidden", Toast.LENGTH_LONG).show()
    }

    override fun showLoading() {
        Toast.makeText(this, "Loading shown", Toast.LENGTH_LONG).show()
    }

    override fun clearTrajectoryTail() {
        play_surface.clearTrajectoryTail()
    }
}
