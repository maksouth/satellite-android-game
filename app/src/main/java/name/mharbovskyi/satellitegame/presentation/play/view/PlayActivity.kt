package name.mharbovskyi.satellitegame.presentation.play.view

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
import name.mharbovskyi.satellitegame.presentation.play.PlayContract
import name.mharbovskyi.satellitegame.presentation.play.PlayPresenterFactory

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
            presenter.start()
        }

        restart_button.setOnClickListener {
            presenter.restart()
        }

        play_surface.setTouchEventListener { presenter.onTouchEvent(it) }
    }

    override fun onStart() {
        super.onStart()
        play_surface.start()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
        play_surface.stop()
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
        val dialog = FinishSuccessDialog(this)
        dialog.show()
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

    override fun showTrajectoryHint(trajectory: List<Location>) {
        play_surface.drawTrajectoryHint(trajectory)
    }

    override fun clearTrajectoryHint() {
        play_surface.clearTrajectoryHint()
    }
}
